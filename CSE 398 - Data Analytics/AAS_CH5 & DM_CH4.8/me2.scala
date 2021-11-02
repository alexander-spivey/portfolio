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

def fitPipeline5(data: DataFrame, k: Int): PipelineModel = {
    val assembler = new VectorAssembler().
		setInputCols(data.columns.filter(_ != "class")).
		setOutputCol("featureVector")

    val kmeans = new KMeans().
		setSeed(Random.nextLong()).
		setK(3).
		setPredictionCol("cluster").
		setFeaturesCol("featureVector").
		setMaxIter(k).
		setTol(1.0e-10)

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

(20 to 200 by 20).map(k => (k, clusteringScore5(data, k), fitPipeline5(data, 2).transform(data).
	select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy("cluster", "class").show(false))).foreach(println)
	
/*
+-------+---------------+-----+
|cluster|class          |count|
+-------+---------------+-----+
|0      |Iris-versicolor|2    |
|0      |Iris-virginica |36   |
|1      |Iris-setosa    |50   |
|2      |Iris-versicolor|48   |
|2      |Iris-virginica |14   |
+-------+---------------+-----+

+-------+---------------+-----+
|cluster|class          |count|
+-------+---------------+-----+
|0      |Iris-versicolor|15   |
|0      |Iris-virginica |49   |
|1      |Iris-setosa    |50   |
|2      |Iris-versicolor|35   |
|2      |Iris-virginica |1    |
+-------+---------------+-----+

+-------+---------------+-----+
|cluster|class          |count|
+-------+---------------+-----+
|0      |Iris-setosa    |50   |
|1      |Iris-virginica |34   |
|2      |Iris-versicolor|50   |
|2      |Iris-virginica |16   |
+-------+---------------+-----+

+-------+---------------+-----+
|cluster|class          |count|
+-------+---------------+-----+
|0      |Iris-versicolor|48   |
|0      |Iris-virginica |14   |
|1      |Iris-setosa    |50   |
|2      |Iris-versicolor|2    |
|2      |Iris-virginica |36   |
+-------+---------------+-----+

+-------+---------------+-----+
|cluster|class          |count|
+-------+---------------+-----+
|0      |Iris-setosa    |50   |
|1      |Iris-versicolor|48   |
|1      |Iris-virginica |14   |
|2      |Iris-versicolor|2    |
|2      |Iris-virginica |36   |
+-------+---------------+-----+

+-------+---------------+-----+
|cluster|class          |count|
+-------+---------------+-----+
|0      |Iris-versicolor|45   |
|0      |Iris-virginica |9    |
|1      |Iris-setosa    |50   |
|2      |Iris-versicolor|5    |
|2      |Iris-virginica |41   |
+-------+---------------+-----+

+-------+---------------+-----+
|cluster|class          |count|
+-------+---------------+-----+
|0      |Iris-versicolor|48   |
|0      |Iris-virginica |14   |
|1      |Iris-setosa    |50   |
|2      |Iris-versicolor|2    |
|2      |Iris-virginica |36   |
+-------+---------------+-----+

+-------+---------------+-----+
|cluster|class          |count|
+-------+---------------+-----+
|0      |Iris-setosa    |30   |
|1      |Iris-versicolor|46   |
|1      |Iris-virginica |50   |
|2      |Iris-setosa    |20   |
|2      |Iris-versicolor|4    |
+-------+---------------+-----+

+-------+---------------+-----+
|cluster|class          |count|
+-------+---------------+-----+
|0      |Iris-versicolor|42   |
|0      |Iris-virginica |5    |
|1      |Iris-setosa    |50   |
|2      |Iris-versicolor|8    |
|2      |Iris-virginica |45   |
+-------+---------------+-----+

+-------+---------------+-----+
|cluster|class          |count|
+-------+---------------+-----+
|0      |Iris-versicolor|46   |
|0      |Iris-virginica |13   |
|1      |Iris-setosa    |50   |
|2      |Iris-versicolor|4    |
|2      |Iris-virginica |37   |
+-------+---------------+-----+

(20,0.2895730091214502,())
(40,0.2895730091214502,())
(60,0.273021191057774,())
(80,0.2895730091214502,())
(100,0.273021191057774,())
(120,0.273021191057774,())
(140,0.2895730091214502,())
(160,0.2895730091214502,())
(180,0.273021191057774,())
(200,0.2895730091214502,())
*/
val pipelineModel = fitPipeline5(data, 80)
val countByClusterLabel = pipelineModel.transform(data).
	select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy("cluster", "class")
countByClusterLabel.show()
/*
+-------+---------------+-----+
|cluster|class          |count|
+-------+---------------+-----+
|0      |Iris-versicolor|47   |
|0      |Iris-virginica |14   |
|1      |Iris-versicolor|3    |
|1      |Iris-virginica |36   |
|2      |Iris-setosa    |50   |
+-------+---------------+-----+
*/
val pipelineModel = fitPipeline5(data, 1000)
val countByClusterLabel = pipelineModel.transform(data).
	select("cluster", "class").
	groupBy("cluster", "class").count().
	orderBy("cluster", "class")
countByClusterLabel.show()