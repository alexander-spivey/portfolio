import org.apache.spark.ml.{PipelineModel, Pipeline}
import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.ml.feature.{OneHotEncoder, VectorAssembler, StringIndexer, StandardScaler}
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.sql.{DataFrame, SparkSession}
import scala.util.Random

val dataWithoutHeader = spark.read. //parse the data directly from csv by following these options
	option("header", "false"). //keep header
	option("inferSchema", "true"). //this means infer type for column
	csv("iris2.csv") //this be where it stored

//If you try to read the csv in with headers, it will always say that variable we are dealing with is string
val data = dataWithoutHeader.toDF(
	"sepal_length", "sepal_width", "petal_length", "petal_width", "class") 	//adding our column labels to our dataWithoutHeader

data.show()
/*
+------------+-----------+------------+-----------+-----------+
|sepal_length|sepal_width|petal_length|petal_width|      class|
+------------+-----------+------------+-----------+-----------+
|         5.1|        3.5|         1.4|        0.2|Iris-setosa|
|         4.9|        3.0|         1.4|        0.2|Iris-setosa|
|         4.7|        3.2|         1.3|        0.2|Iris-setosa|
|         4.6|        3.1|         1.5|        0.2|Iris-setosa|
|         5.0|        3.6|         1.4|        0.2|Iris-setosa|
|         5.4|        3.9|         1.7|        0.4|Iris-setosa|
|         4.6|        3.4|         1.4|        0.3|Iris-setosa|
|         5.0|        3.4|         1.5|        0.2|Iris-setosa|
|         4.4|        2.9|         1.4|        0.2|Iris-setosa|
|         4.9|        3.1|         1.5|        0.1|Iris-setosa|
|         5.4|        3.7|         1.5|        0.2|Iris-setosa|
|         4.8|        3.4|         1.6|        0.2|Iris-setosa|
|         4.8|        3.0|         1.4|        0.1|Iris-setosa|
|         4.3|        3.0|         1.1|        0.1|Iris-setosa|
|         5.8|        4.0|         1.2|        0.2|Iris-setosa|
|         5.7|        4.4|         1.5|        0.4|Iris-setosa|
|         5.4|        3.9|         1.3|        0.4|Iris-setosa|
|         5.1|        3.5|         1.4|        0.3|Iris-setosa|
|         5.7|        3.8|         1.7|        0.3|Iris-setosa|
|         5.1|        3.8|         1.5|        0.3|Iris-setosa|
+------------+-----------+------------+-----------+-----------+
*/

data.select("class").groupBy("class").count().orderBy($"count".desc).show()
/*
+---------------+-----+
|          class|count|
+---------------+-----+
|    Iris-setosa|   50|
| Iris-virginica|   50|
|Iris-versicolor|   50|
+---------------+-----+
*/

val assembler = new VectorAssembler().
	setInputCols(data.columns.filter(_ != "class")).
	setOutputCol("featureVector")

val kmeans = new KMeans().
	setPredictionCol("cluster").
	setFeaturesCol("featureVector")
	
val pipeline = new Pipeline().setStages(Array(assembler, kmeans))
val pipelineModel = pipeline.fit(data)

val kmeansModel = pipelineModel.stages.last.asInstanceOf[KMeansModel]
kmeansModel.clusterCenters.foreach(println)

val withCluster = pipelineModel.transform(data)

withCluster.select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy($"cluster", $"count".desc).
	show(25)
/*
+-------+---------------+-----+
|cluster|          class|count|
+-------+---------------+-----+
|      0|    Iris-setosa|   50|
|      0|Iris-versicolor|    3|
|      1| Iris-virginica|   50|
|      1|Iris-versicolor|   47|
+-------+---------------+-----+
*/

def clusteringScore0(data: DataFrame, k: Int): Double = {	//input is DF and int for k, returns a double
	val assembler = new VectorAssembler().
		setInputCols(data.columns.filter(_ != "class")).
		setOutputCol("featureVector")	//create a vector including every column but class
	val kmeans = new KMeans().
		setSeed(Random.nextLong()).
		setK(k).	//expected number of clusters
		setPredictionCol("cluster").
		setFeaturesCol("featureVector")
	val pipeline = new Pipeline().setStages(Array(assembler, kmeans))
	val kmeansModel = pipeline.fit(data).stages.last.asInstanceOf[KMeansModel]		//grab our model
	kmeansModel.computeCost(assembler.transform(data)) / data.count() //"Compute mean from total squared distance (“cost”)"
}
(2 to 10 by 1).map(k => (k, clusteringScore0(data, k))).foreach(println)
/*
(2,1.0157913765156006)
(3,0.5262722761743098)
(4,0.47560297882910724)
(5,0.33374438771381715)
(6,0.25959159829060496)
(7,0.24888640031641263)
(8,0.20232136176001567)	//lowest value before increasing again
(9,0.20872691425647608)
(10,0.20203308618381602)
*/

def clusteringScore1(data: DataFrame, k: Int): Double = {	//input is DF and int for k, returns a double
	val assembler = new VectorAssembler().
		setInputCols(data.columns.filter(_ != "class")).
		setOutputCol("featureVector")	//create a vector including every column but class
	val kmeans = new KMeans().
		setSeed(Random.nextLong()).
		setK(k).	//expected number of clusters
		setMaxIter(40).	//increase from default 20 to 40
		setTol(1.0e-5).	//decrease from default 1.0e-4
		setPredictionCol("cluster").
		setFeaturesCol("featureVector")
	val pipeline = new Pipeline().setStages(Array(assembler, kmeans))
	val kmeansModel = pipeline.fit(data).stages.last.asInstanceOf[KMeansModel]		//grab our model
	kmeansModel.computeCost(assembler.transform(data)) / data.count() //"Compute mean from total squared distance (“cost”)"
}
(2 to 10 by 1).map(k => (k, clusteringScore1(data, k))).foreach(println)
/*
(2,1.0157913765156006)
(3,0.5263004388398425)
(4,0.47560297882910724)
(5,0.33245769509255996)
(6,0.26167887261758693)
(7,0.23361839968311168)	//lowest value before increasing again
(8,0.23935369068839407)
(9,0.20702411477411337)
(10,0.17662681818181974)
*/

def clusteringScore2(data: DataFrame, k: Int): Double = {
	val assembler = new VectorAssembler().
		setInputCols(data.columns.filter(_ != "class")).
		setOutputCol("featureVector")
		
	val scaler = new StandardScaler().
		setInputCol("featureVector").
		setOutputCol("scaledFeatureVector").
		setWithStd(true).
		setWithMean(false)
		
	val kmeans = new KMeans().
		setSeed(Random.nextLong()).
		setK(k).
		setPredictionCol("cluster").
		setFeaturesCol("scaledFeatureVector").
		setMaxIter(40).
		setTol(1.0e-5)
	
	val pipeline = new Pipeline().setStages(Array(assembler, scaler, kmeans))
	val pipelineModel = pipeline.fit(data)
	val kmeansModel = pipelineModel.stages.last.asInstanceOf[KMeansModel]
	kmeansModel.computeCost(pipelineModel.transform(data)) / data.count()
}
(2 to 10 by 1).map(k => (k, clusteringScore2(data, k))).foreach(println)
/*
(2,1.4816030602123282)
(3,0.9335069634658385)
(4,0.7628156641257224)
(5,0.705178958270994)
(6,0.6464178124155567)
(7,0.486393794433478)
(8,0.4335494544262959)
(9,0.3941283307562037)
(10,0.3155397406655387)
*/

def entropy(counts: Iterable[Int]): Double = {
	val values = counts.filter(_ > 0)
	val n = values.map(_.toDouble).sum
	values.map { v =>
		val p = v / n
		-p * math.log(p)
	}.sum
}

 def fitPipeline4(data: DataFrame, k: Int): PipelineModel = {
    val assembler = new VectorAssembler().
		setInputCols(data.columns.filter(_ != "class")).
		setOutputCol("featureVector")

    val scaler = new StandardScaler()
		.setInputCol("featureVector")
		.setOutputCol("scaledFeatureVector")
		.setWithStd(true)
		.setWithMean(false)

    val kmeans = new KMeans().
		setSeed(Random.nextLong()).
		setK(k).
		setPredictionCol("cluster").
		setFeaturesCol("scaledFeatureVector").
		setMaxIter(40).
		setTol(1.0e-5)

    val pipeline = new Pipeline().setStages(
		Array(assembler, scaler, kmeans))
    pipeline.fit(data)
  }
  
def clusteringScore4(data: DataFrame, k: Int): Double = {
	val pipelineModel = fitPipeline4(data, k)

	// Predict cluster for each datum
	val clusterLabel = pipelineModel.transform(data).
		select("cluster", "class").as[(Int, String)]
	val weightedClusterEntropy = clusterLabel.
		// Extract collections of labels, per cluster
		groupByKey { case (cluster, _) => cluster }.
		mapGroups { case (_, clusterLabels) =>
			val labels = clusterLabels.map { case (_, label) => label }.toSeq
			// Count labels in collections
			val labelCounts = labels.groupBy(identity).values.map(_.size)
			labels.size * entropy(labelCounts)
		}.collect()

	// Average entropy weighted by cluster size
	weightedClusterEntropy.sum / data.count()
}

(2 to 10 by 1).map(k => (k, clusteringScore4(data, k))).foreach(println)
/*
(2,0.4620981203732969)
(3,0.37303245806738555)
(4,0.34083198433786593)
(5,0.33534496305711625)
(6,0.31018676754053165)
(7,0.3317133492276407)
(8,0.19068803427125436)
(9,0.26344491728695874)
(10,0.1696281418987278)
*/
val pipelineModel = fitPipeline4(data, 2)
val countByClusterLabel = pipelineModel.transform(data).
	select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy("cluster", "class")
countByClusterLabel.show()
/*
+-------+---------------+-----+
|cluster|          class|count|
+-------+---------------+-----+
|      0|Iris-versicolor|   50|
|      0| Iris-virginica|   50|
|      1|    Iris-setosa|   50|
+-------+---------------+-----+
*/

val pipelineModel = fitPipeline4(data, 3)
val countByClusterLabel = pipelineModel.transform(data).
	select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy("cluster", "class")
countByClusterLabel.show()
/*
+-------+---------------+-----+
|cluster|          class|count|
+-------+---------------+-----+
|      0|Iris-versicolor|   50|
|      0| Iris-virginica|   50|
|      1|    Iris-setosa|   37|
|      2|    Iris-setosa|   13|
+-------+---------------+-----+
*/

/*

+-------+---------------+-----+
|cluster|          class|count|
+-------+---------------+-----+
|      0|    Iris-setosa|   50|
|      1|Iris-versicolor|   12|
|      1| Iris-virginica|   39|
|      2|Iris-versicolor|   38|
|      2| Iris-virginica|   11|
+-------+---------------+-----+

*/

/*
+-------+---------------+-----+
|cluster|          class|count|
+-------+---------------+-----+
|      0|Iris-versicolor|   11|
|      0| Iris-virginica|   36|
|      1|    Iris-setosa|   50|
|      2|Iris-versicolor|   39|
|      2| Iris-virginica|   14|
+-------+---------------+-----+
*/

val pipelineModel = fitPipeline4(data, 6)
val countByClusterLabel = pipelineModel.transform(data).
	select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy("cluster", "class")
countByClusterLabel.show()
/*
+-------+---------------+-----+
|cluster|          class|count|
+-------+---------------+-----+
|      0|Iris-versicolor|   22|
|      0| Iris-virginica|   15|
|      1|    Iris-setosa|   35|
|      2| Iris-virginica|   12|
|      3|Iris-versicolor|    9|
|      3| Iris-virginica|   21|
|      4|Iris-versicolor|   19|
|      4| Iris-virginica|    2|
|      5|    Iris-setosa|   15|
+-------+---------------+-----+
*/

val pipelineModel = fitPipeline4(data, 9)
val countByClusterLabel = pipelineModel.transform(data).
	select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy("cluster", "class")
countByClusterLabel.show()
/*
+-------+---------------+-----+
|cluster|          class|count|
+-------+---------------+-----+
|      0|Iris-versicolor|   19|
|      1|    Iris-setosa|   27|
|      2| Iris-virginica|    9|
|      3|    Iris-setosa|   23|
|      4|Iris-versicolor|   17|
|      4| Iris-virginica|    7|
|      5|Iris-versicolor|    4|
|      5| Iris-virginica|   14|
|      6|Iris-versicolor|   10|
|      6| Iris-virginica|    1|
|      7| Iris-virginica|   16|
|      8| Iris-virginica|    3|
+-------+---------------+-----+
*/


def fitPipeline5(data: DataFrame, k: Int): PipelineModel = {
    val assembler = new VectorAssembler().
		setInputCols(data.columns.filter(_ != "class")).
		setOutputCol("featureVector")

    

    val kmeans = new KMeans().
		setSeed(Random.nextLong()).
		setK(k).
		setPredictionCol("cluster").
		setFeaturesCol("featureVector").
		setMaxIter(40).
		setTol(1.0e-5)

    val pipeline = new Pipeline().setStages(
		Array(assembler, kmeans))
    pipeline.fit(data)
  }
  
def clusteringScore5(data: DataFrame, k: Int): Double = {
	val pipelineModel = fitPipeline5(data, k)

	// Predict cluster for each datum
	val clusterLabel = pipelineModel.transform(data).
		select("cluster", "class").as[(Int, String)]
	val weightedClusterEntropy = clusterLabel.
		// Extract collections of labels, per cluster
		groupByKey { case (cluster, _) => cluster }.
		mapGroups { case (_, clusterLabels) =>
			val labels = clusterLabels.map { case (_, label) => label }.toSeq
			// Count labels in collections
			val labelCounts = labels.groupBy(identity).values.map(_.size)
			labels.size * entropy(labelCounts)
		}.collect()

	// Average entropy weighted by cluster size
	weightedClusterEntropy.sum / data.count()
}

(2 to 10 by 1).map(k => (k, clusteringScore5(data, k))).foreach(println)

val pipelineModel = fitPipeline5(data, 2)
val countByClusterLabel = pipelineModel.transform(data).
	select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy("cluster", "class")
countByClusterLabel.show()


val pipelineModel = fitPipeline5(data, 3)
val countByClusterLabel = pipelineModel.transform(data).
	select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy("cluster", "class")
countByClusterLabel.show()


val pipelineModel = fitPipeline5(data, 6)
val countByClusterLabel = pipelineModel.transform(data).
	select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy("cluster", "class")
countByClusterLabel.show()

val pipelineModel = fitPipeline5(data, 9)
val countByClusterLabel = pipelineModel.transform(data).
	select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy("cluster", "class")
countByClusterLabel.show()

/*
countByClusterLabel: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [cluster: int, class: string ... 1 more field]
+-------+---------------+-----+
|cluster|          class|count|
+-------+---------------+-----+
|      0|Iris-versicolor|   47|
|      0| Iris-virginica|   50|
|      1|    Iris-setosa|   50|
|      1|Iris-versicolor|    3|
+-------+---------------+-----+

pipelineModel: org.apache.spark.ml.PipelineModel = pipeline_6c50ed69fb29
countByClusterLabel: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [cluster: int, class: string ... 1 more field]
+-------+---------------+-----+
|cluster|          class|count|
+-------+---------------+-----+
|      0|    Iris-setosa|   50|
|      1|Iris-versicolor|   47|
|      1| Iris-virginica|   14|
|      2|Iris-versicolor|    3|
|      2| Iris-virginica|   36|
+-------+---------------+-----+

pipelineModel: org.apache.spark.ml.PipelineModel = pipeline_5e962b361224
countByClusterLabel: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [cluster: int, class: string ... 1 more field]
+-------+---------------+-----+
|cluster|          class|count|
+-------+---------------+-----+
|      0|Iris-versicolor|   23|
|      0| Iris-virginica|   12|
|      1|    Iris-setosa|   50|
|      2| Iris-virginica|   18|
|      3| Iris-virginica|   11|
|      4| Iris-virginica|    8|
|      5|Iris-versicolor|   27|
|      5| Iris-virginica|    1|
+-------+---------------+-----+

pipelineModel: org.apache.spark.ml.PipelineModel = pipeline_c7bd8bef0fc3
countByClusterLabel: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [cluster: int, class: string ... 1 more field]
+-------+---------------+-----+
|cluster|          class|count|
+-------+---------------+-----+
|      0|Iris-versicolor|   24|
|      0| Iris-virginica|    1|
|      1|    Iris-setosa|   23|
|      2|Iris-versicolor|   21|
|      3| Iris-virginica|   12|
|      4| Iris-virginica|    3|
|      5|    Iris-setosa|   27|
|      6|Iris-versicolor|    4|
|      6| Iris-virginica|   12|
|      7|Iris-versicolor|    1|
|      7| Iris-virginica|   13|
|      8| Iris-virginica|    9|
+-------+---------------+-----+
*/