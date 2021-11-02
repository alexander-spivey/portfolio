import org.apache.spark.sql.functions._ //LIT WE MAKING FUNCTIONS
import org.apache.spark.ml.{PipelineModel, Pipeline}
import org.apache.spark.ml.classification.{DecisionTreeClassifier,
  RandomForestClassifier, RandomForestClassificationModel}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{VectorAssembler, VectorIndexer}
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.sql.{DataFrame, SparkSession}
import scala.util.Random
import spark.implicits._

//--Preparing the Data--
val dataWithoutHeaders = spark.read.
	option("inferschema", true).	//predict value type
	option("header", false).	//no header line
	csv("/proj/cse398-498/course/AAS_CH4/covtype.data")
	
val colNames = Seq(	//initial column names
		"Elevation", "Aspect", "Slope",
		"Horizontal_Distance_To_Hydrology", "Vertical_Distance_To_Hydrology",
		"Horizontal_Distance_To_Roadways",
		"Hillshade_9am", "Hillshade_Noon", "Hillshade_3pm",
		"Horizontal_Distance_To_Fire_Points"
	) ++ (	//++ concatenates collections together
		(0 until 4).map(i => s"Wilderness_Area_$i")	//cool trick to label each column differently
	) ++ (
		(0 until 40).map(i => s"Soil_Type_$i")	//same incrementing trick here
	) ++ Seq("Cover_Type")	//save sequence as

val data = dataWithoutHeaders.toDF(colNames: _*).
	withColumn("Cover_Type", $"Cover_Type".cast("double")) //cast each column to double (MLlib API req) and save it under Cover_Type sequence
	
//-Lets undoing this one-hot encoding, by taking 4 columns encoding wilderness type into one, with values ranging from 0 to 3-
 def unencodeOneHot(data: DataFrame): DataFrame = {
    val wildernessCols = (0 until 4).map(i => s"Wilderness_Area_$i").toArray

    val wildernessAssembler = new VectorAssembler().
      setInputCols(wildernessCols).
      setOutputCol("wilderness")

    val unhotUDF = udf((vec: Vector) => vec.toArray.indexOf(1.0).toDouble)

    val withWilderness = wildernessAssembler.transform(data).
      drop(wildernessCols:_*).
      withColumn("wilderness", unhotUDF($"wilderness"))

    val soilCols = (0 until 40).map(i => s"Soil_Type_$i").toArray

    val soilAssembler = new VectorAssembler().
      setInputCols(soilCols).
      setOutputCol("soil")

    soilAssembler.transform(withWilderness).
      drop(soilCols:_*).
      withColumn("soil", unhotUDF($"soil"))
}
	
//--A First Decision Tree--
val Array(trainData, testData) = data.randomSplit(Array(0.9,0.1))
trainData.cache()
testData.cache()
val unencTrainData = unencodeOneHot(trainData)
val unencTestData = unencodeOneHot(testData)

//-RandomTreeForest
val classifier = new RandomForestClassifier().
  setSeed(Random.nextLong()).
  setLabelCol("Cover_Type").
  setFeaturesCol("indexedVector").
  setPredictionCol("prediction")

//-The code below is to test the resultin decision tree forests-
import org.apache.spark.ml.feature.VectorIndexer
val inputCols = unencTrainData.columns.filter(_ != "Cover_Type") 	//all columns except the target are input features
val assembler = new VectorAssembler().
	setInputCols(inputCols).
	setOutputCol("featureVector")
val indexer = new VectorIndexer().
	setMaxCategories(60). //set to 40 because soil has 40 values
	setInputCol("featureVector").
	setOutputCol("indexedVector")
//this works because all values form 0-40 is present, but in scarce data set, may need to use VectorIndexerModel to manually map
val pipeline = new Pipeline().setStages(Array(assembler, indexer, classifier))

val paramGrid = new ParamGridBuilder().
	addGrid(classifier.impurity, Seq("entropy")). //use both impurity measures
	addGrid(classifier.maxDepth, Seq(30)).	//use max max depth
	addGrid(classifier.maxBins, Seq(100)).	//40 to 300 values for rules
	addGrid(classifier.minInfoGain, Seq(0.0)).	//0.0 since it doesn't seem to affect the data
	addGrid(classifier.minInstancesPerNode, Seq(1)).
	build()
val multiclassEval = new MulticlassClassificationEvaluator().
	setLabelCol("Cover_Type").
	setPredictionCol("prediction").
	setMetricName("accuracy")	//kinda self explanotary what happening here
	
val validator = new TrainValidationSplit().
	setSeed(Random.nextLong()).
	setEstimator(pipeline).
	setEvaluator(multiclassEval).
	setEstimatorParamMaps(paramGrid). //HYPER PARAMETERS CAN STILL OVER FIT
	setTrainRatio(0.9)	//take another 10 percent and set it aside
//the left out 10% is used as a crossvalidation set (evaluate parameters that fit to training set)
//the original 10% left out to evaluate hyperparameters that fit the CV^^ (examples that arent in CV but has not been trained on [real world data])

val validatorModel = validator.fit(unencTrainData)	//returns best overall pipeline

val bestModel = validatorModel.bestModel	//extract best model
bestModel.asInstanceOf[PipelineModel].stages.last.extractParamMap	//extract parameters
val paramsAndMetrics = validatorModel.validationMetrics.	//accessing the the models metrics
	zip(validatorModel.getEstimatorParamMaps).sortBy(-_._1)	//zip up all models params, sort by descending
paramsAndMetrics.foreach { case (metric, params) =>
    println(metric)
    println(params)
    println()
}


//-How well did the data do on the cv and test-
print("CV Results")
validatorModel.validationMetrics.max

print("UnencTestData")
multiclassEval.evaluate(bestModel.transform(unencTestData))

print("UnencTrainingData")
multiclassEval.evaluate(bestModel.transform(unencTrainData))

/*
val paramGrid = new ParamGridBuilder().
	addGrid(classifier.impurity, Seq("entropy")). //use both impurity measures
	addGrid(classifier.maxDepth, Seq(30)).	//use max max depth
	addGrid(classifier.maxBins, Seq(40)).	//40 values for rules
	addGrid(classifier.minInfoGain, Seq(0.0)).	//0.0 since it doesn't seem to affect the data
	addGrid(classifier.minInstancesPerNode, Seq(1)).
	build()

0.9595951851567891
{
        rfc_1e08a78abd5b-impurity: entropy,
        rfc_1e08a78abd5b-maxBins: 40,
        rfc_1e08a78abd5b-maxDepth: 30,
        rfc_1e08a78abd5b-minInfoGain: 0.0,
        rfc_1e08a78abd5b-minInstancesPerNode: 1
}
*/