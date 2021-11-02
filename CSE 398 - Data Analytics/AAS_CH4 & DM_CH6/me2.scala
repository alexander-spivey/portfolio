import java.util.Calendar;
import org.apache.spark.sql.functions._
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

//Spark MLlib requires all inputs to be vectors (arrays of doubles)
val inputCols = unencTrainData.columns.filter(_ != "Cover_Type") //training data is made into input by taking out covertype columns
val assembler = new VectorAssembler().	//create assembler
	setInputCols(inputCols).	//using input cols
	setOutputCol("featureVector")	//save output columns as a featureVector
val classifier = new DecisionTreeClassifier().
	setSeed(Random.nextLong()). //set as a random seed
	setLabelCol("Cover_Type").
	setFeaturesCol("featureVector").
	setPredictionCol("prediction")
val pipeline = new Pipeline().setStages(Array(assembler, classifier)) //pipeline is multiprocess, use Array to order stages

//ITERATION 0
print("ITERATION 0\n")
val paramGrid = new ParamGridBuilder().
	addGrid(classifier.maxMemoryInMB, Seq(128)).
	addGrid(classifier.impurity, Seq("entropy")). //use both impurity measures
	addGrid(classifier.maxDepth, Seq(30)).	//depth of 1 to 20
	addGrid(classifier.maxBins, Seq(20, 40, 60, 80, 100)).	//40 to 300 values for rules
	addGrid(classifier.minInfoGain, Seq(0.0)).	//ranges from no changes to impurity to improve impurity by 0.05
	//addGrid(classifier.minInstancesPerNode, Seq(500, 1000, 2000))
	//the first time I ran this, I tried to use 500, 1000, 2000 for minInstance, but ended up drastically dropping the success of the function
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

val start = Calendar.getInstance()
val sh = start.get(Calendar.HOUR)
val sm = start.get(Calendar.MINUTE)
val ss = start.get(Calendar.SECOND)
printf("Start Time: %02d:%02d:%02d\n", sh, sm, ss);

val validatorModel = validator.fit(unencTrainData)	//returns best overall pipeline

val end = Calendar.getInstance()
val eh = end.get(Calendar.HOUR)
val em = end.get(Calendar.MINUTE)
val es = end.get(Calendar.SECOND)
printf("End Time: %02d:%02d:%02d\n", eh, em, es);

val dh = eh-sh
val dm = em-sm
val ds = es-ss
printf("Total Time: %02d:%02d:%02d\n", dh, dm, ds);



//ITERATION 1
print("ITERATION 1\n")
val paramGrid = new ParamGridBuilder().
	addGrid(classifier.maxMemoryInMB, Seq(256)).
	addGrid(classifier.impurity, Seq("entropy")). //use both impurity measures
	addGrid(classifier.maxDepth, Seq(30)).	//depth of 1 to 20
	addGrid(classifier.maxBins, Seq(20, 40, 60, 80, 100)).	//40 to 300 values for rules
	addGrid(classifier.minInfoGain, Seq(0.0)).	//ranges from no changes to impurity to improve impurity by 0.05
	//addGrid(classifier.minInstancesPerNode, Seq(500, 1000, 2000))
	//the first time I ran this, I tried to use 500, 1000, 2000 for minInstance, but ended up drastically dropping the success of the function
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

val start = Calendar.getInstance()
val sh = start.get(Calendar.HOUR)
val sm = start.get(Calendar.MINUTE)
val ss = start.get(Calendar.SECOND)
printf("Start Time: %02d:%02d:%02d\n", sh, sm, ss);

val validatorModel = validator.fit(unencTrainData)	//returns best overall pipeline

val end = Calendar.getInstance()
val eh = end.get(Calendar.HOUR)
val em = end.get(Calendar.MINUTE)
val es = end.get(Calendar.SECOND)
printf("End Time: %02d:%02d:%02d\n", eh, em, es);

val dh = eh-sh
val dm = em-sm
val ds = es-ss
printf("Total Time: %02d:%02d:%02d\n", dh, dm, ds);


//ITERATION 2
print("ITERATION 2\n")
val paramGrid = new ParamGridBuilder().
	addGrid(classifier.maxMemoryInMB, Seq(512)).
	addGrid(classifier.impurity, Seq("entropy")). //use both impurity measures
	addGrid(classifier.maxDepth, Seq(30)).	//depth of 1 to 20
	addGrid(classifier.maxBins, Seq(20, 40, 60, 80, 100)).	//40 to 300 values for rules
	addGrid(classifier.minInfoGain, Seq(0.0)).	//ranges from no changes to impurity to improve impurity by 0.05
	//addGrid(classifier.minInstancesPerNode, Seq(500, 1000, 2000))
	//the first time I ran this, I tried to use 500, 1000, 2000 for minInstance, but ended up drastically dropping the success of the function
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

val start = Calendar.getInstance()
val sh = start.get(Calendar.HOUR)
val sm = start.get(Calendar.MINUTE)
val ss = start.get(Calendar.SECOND)
printf("Start Time: %02d:%02d:%02d\n", sh, sm, ss);

val validatorModel = validator.fit(unencTrainData)	//returns best overall pipeline

val end = Calendar.getInstance()
val eh = end.get(Calendar.HOUR)
val em = end.get(Calendar.MINUTE)
val es = end.get(Calendar.SECOND)
printf("End Time: %02d:%02d:%02d\n", eh, em, es);

val dh = eh-sh
val dm = em-sm
val ds = es-ss
printf("Total Time: %02d:%02d:%02d\n", dh, dm, ds);

//ITERATION 3
print("ITERATION 3\n")
val paramGrid = new ParamGridBuilder().
	addGrid(classifier.maxMemoryInMB, Seq(1024)).
	addGrid(classifier.impurity, Seq("entropy")). //use both impurity measures
	addGrid(classifier.maxDepth, Seq(30)).	//depth of 1 to 20
	addGrid(classifier.maxBins, Seq(20, 40, 60, 80, 100)).	//40 to 300 values for rules
	addGrid(classifier.minInfoGain, Seq(0.0)).	//ranges from no changes to impurity to improve impurity by 0.05
	//addGrid(classifier.minInstancesPerNode, Seq(500, 1000, 2000))
	//the first time I ran this, I tried to use 500, 1000, 2000 for minInstance, but ended up drastically dropping the success of the function
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

val start = Calendar.getInstance()
val sh = start.get(Calendar.HOUR)
val sm = start.get(Calendar.MINUTE)
val ss = start.get(Calendar.SECOND)
printf("Start Time: %02d:%02d:%02d\n", sh, sm, ss);

val validatorModel = validator.fit(unencTrainData)	//returns best overall pipeline

val end = Calendar.getInstance()
val eh = end.get(Calendar.HOUR)
val em = end.get(Calendar.MINUTE)
val es = end.get(Calendar.SECOND)
printf("End Time: %02d:%02d:%02d\n", eh, em, es);

val dh = eh-sh
val dm = em-sm
val ds = es-ss
printf("Total Time: %02d:%02d:%02d\n", dh, dm, ds);

//ITERATION 4
print("ITERATION 4\n")
val paramGrid = new ParamGridBuilder().
	addGrid(classifier.maxMemoryInMB, Seq(2048)).
	addGrid(classifier.impurity, Seq("entropy")). //use both impurity measures
	addGrid(classifier.maxDepth, Seq(30)).	//depth of 1 to 20
	addGrid(classifier.maxBins, Seq(20, 40, 60, 80, 100)).	//40 to 300 values for rules
	addGrid(classifier.minInfoGain, Seq(0.0)).	//ranges from no changes to impurity to improve impurity by 0.05
	//addGrid(classifier.minInstancesPerNode, Seq(500, 1000, 2000))
	//the first time I ran this, I tried to use 500, 1000, 2000 for minInstance, but ended up drastically dropping the success of the function
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

val start = Calendar.getInstance()
val sh = start.get(Calendar.HOUR)
val sm = start.get(Calendar.MINUTE)
val ss = start.get(Calendar.SECOND)
printf("Start Time: %02d:%02d:%02d\n", sh, sm, ss);

val validatorModel = validator.fit(unencTrainData)	//returns best overall pipeline

val end = Calendar.getInstance()
val eh = end.get(Calendar.HOUR)
val em = end.get(Calendar.MINUTE)
val es = end.get(Calendar.SECOND)
printf("End Time: %02d:%02d:%02d\n", eh, em, es);

val dh = eh-sh
val dm = em-sm
val ds = es-ss
printf("Total Time: %02d:%02d:%02d\n", dh, dm, ds);

//ITERATION 4
print("ITERATION 4\n")
val paramGrid = new ParamGridBuilder().
	addGrid(classifier.maxMemoryInMB, Seq(4096)).
	addGrid(classifier.impurity, Seq("entropy")). //use both impurity measures
	addGrid(classifier.maxDepth, Seq(30)).	//depth of 1 to 20
	addGrid(classifier.maxBins, Seq(20, 40, 60, 80, 100)).	//40 to 300 values for rules
	addGrid(classifier.minInfoGain, Seq(0.0)).	//ranges from no changes to impurity to improve impurity by 0.05
	//addGrid(classifier.minInstancesPerNode, Seq(500, 1000, 2000))
	//the first time I ran this, I tried to use 500, 1000, 2000 for minInstance, but ended up drastically dropping the success of the function
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

val start = Calendar.getInstance()
val sh = start.get(Calendar.HOUR)
val sm = start.get(Calendar.MINUTE)
val ss = start.get(Calendar.SECOND)
printf("Start Time: %02d:%02d:%02d\n", sh, sm, ss);

val validatorModel = validator.fit(unencTrainData)	//returns best overall pipeline

val end = Calendar.getInstance()
val eh = end.get(Calendar.HOUR)
val em = end.get(Calendar.MINUTE)
val es = end.get(Calendar.SECOND)
printf("End Time: %02d:%02d:%02d\n", eh, em, es);

val dh = eh-sh
val dm = em-sm
val ds = es-ss
printf("Total Time: %02d:%02d:%02d\n", dh, dm, ds);
