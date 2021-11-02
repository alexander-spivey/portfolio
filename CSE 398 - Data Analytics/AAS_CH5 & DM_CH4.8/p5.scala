//--Categorical Variables--
/*
Normalizing the data is not enough. We can improve this more by putting our nominal values back, which is leaving out valuable information.
"The categorical features can translate into several binary indicator features using one-hot encoding, which can be viewed as numeric dimensions."
For example, instead of having one column to hold tcp, udp, and icmp, we can have 3 columns, where being tcp makes it 0,1,1 and udp 0,1,0
This is a two step process, which we can use the pipeline to take advantage off.
1. convert string values to integer indicies using StringIndexer
2. incode these integer into a vector using OneHotEncoder
*/
import org.apache.spark.ml.{PipelineModel, Pipeline}
import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.ml.feature.{OneHotEncoder, VectorAssembler, StringIndexer, StandardScaler}
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.sql.{DataFrame, SparkSession}
import scala.util.Random

def oneHotPipeline(inputCol: String): (Pipeline, String) = {
    val indexer = new StringIndexer().
		setInputCol(inputCol).
		setOutputCol(inputCol + "_indexed")
	
	val encoder = new OneHotEncoder().
		setInputCol(inputCol + "_indexed").
		setOutputCol(inputCol + "_vec")
	
	val pipeline = new Pipeline().setStages(Array(indexer, encoder))
	(pipeline, inputCol + "_vec")
}

def clusteringScore3(data: DataFrame, k: Int): Double = {
	val (protoTypeEncoder, protoTypeVecCol) = oneHotPipeline("protocol_type")
	val (serviceEncoder, serviceVecCol) = oneHotPipeline("service")
	val (flagEncoder, flagVecCol) = oneHotPipeline("flag")

	// Original columns, without label / string columns, but with new vector encoded cols
	val assembleCols = Set(data.columns: _*) --
		Seq("label", "protocol_type", "service", "flag") ++
		Seq(protoTypeVecCol, serviceVecCol, flagVecCol)
	val assembler = new VectorAssembler().
		setInputCols(assembleCols.toArray).
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
		Array(protoTypeEncoder, serviceEncoder, flagEncoder, assembler, scaler, kmeans))
	val pipelineModel = pipeline.fit(data)

	val kmeansModel = pipelineModel.stages.last.asInstanceOf[KMeansModel]
	kmeansModel.computeCost(pipelineModel.transform(data)) / data.count()
}


(60 to 270 by 30).map(k => (k, clusteringScore3(data, k))).
	foreach(println)
/*
(60,40.154767336639836)
(90,16.17255608429269)
(120,3.1997476335409756)
(150,2.161171637755817)
(180,1.5142138090107071)
(210,1.2522240551903454)
(240,1.1111356336856535)
(270,0.9741348064961258)
*/