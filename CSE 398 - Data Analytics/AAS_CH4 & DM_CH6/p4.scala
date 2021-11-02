//--Categorical Features Revisited--
/*
As of now, all features have been thought as numerical, even when severy categories are binary. This isn't a issue, since 0 and 1, values for
yes and no, are between 0 and 1, values being compared to. 
This isn't that great since it forces the model to look at each underlying categorical feature individually

QOUTE FROM BOOK - PLEASE NO PUNISH - TOO GOOD OF AN EXPLANATION:
"For example, nine different soil types are actually part of the Leighcan family, and they may be related in ways that the decision tree can 
exploit. If soil type were encoded as a single categorical feature with 40 soil values, then the tree could express rules like “if the soil 
type is one of the nine Leighton family types” directly. However, when encoded as 40 features, the tree would have to learn a sequence of 
nine decisions on soil type to do the same, this expressiveness may lead to better decisions and more efficient trees."
Whereas right nowe we have 40 features represented by 40 one valued columns, which increase memory usage and slooooows the program down
*/

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

//LOAD p1.SCALA BEFORE RUNNING BELOW!@!
val data = dataWithoutHeaders.toDF(colNames: _*).
	withColumn("Cover_Type", $"Cover_Type".cast("double")) //cast each column to double (MLlib API req) and save it under Cover_Type sequence
val Array(trainData, testData) = data.randomSplit(Array(0.9,0.1))
trainData.cache()
testData.cache()

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


val unencTrainData = unencodeOneHot(trainData)
val unencTestData = unencodeOneHot(testData)
/*
THIS DOESN'T MAKE THE TWO NEW COLUMNS INDICATE THAT THEY ARE SPECIAL. WE NEED TO HAVE SPARK INTERPRET IT DIFF OR ELSE FAILURE

Spark can add metadat to each column. In order to do so, we need to put data through VectorIndexer(turn inpput into properly label
categorical feature columns. HAVE TO ADD THIS STAGE TO THE PIPELINE*/

val inputCols = unencTrainData.columns.filter(_ != "Cover_Type")
val assembler = new VectorAssembler().
	setInputCols(inputCols).
	setOutputCol("featureVector")

val indexer = new VectorIndexer().
	setMaxCategories(40). //set to 40 because soil has 40 values
	setInputCol("featureVector").
	setOutputCol("indexedVector")
//this works because all values form 0-40 is present, but in scarce data set, may need to use VectorIndexerModel to manually map
val classifier = new DecisionTreeClassifier().
	setSeed(Random.nextLong()).
	setLabelCol("Cover_Type").
	setFeaturesCol("indexedVector").
	setPredictionCol("prediction")
val pipeline = new Pipeline().setStages(Array(assembler, indexer, classifier))

//-BELOW THIS IS TO TEST THE PIPELINE-
val paramGrid = new ParamGridBuilder().
	addGrid(classifier.impurity, Seq("gini", "entropy")). //use both impurity measures
	addGrid(classifier.maxDepth, Seq(1, 20)).	//depth of 1 to 20
	addGrid(classifier.maxBins, Seq(40, 300)).	//40 to 300 values for rules
	addGrid(classifier.minInfoGain, Seq(0.0, 0.05)).	//ranges from no changes to impurity to improve impurity by 0.05
	build()	//this builds 16 different models
val multiclassEval = new MulticlassClassificationEvaluator().
	setLabelCol("Cover_Type").
	setPredictionCol("prediction").
	setMetricName("accuracy")	//kinda self explanotary what happening here
	
import org.apache.spark.ml.tuning.TrainValidationSplit	//can use CrossValidator here, but will coost k times more expensive & doesnt add much
val validator = new TrainValidationSplit().
	setSeed(Random.nextLong()).
	setEstimator(pipeline).
	setEvaluator(multiclassEval).
	setEstimatorParamMaps(paramGrid). //HYPER PARAMETERS CAN STILL OVER FIT
	setTrainRatio(0.9)	//take another 10 percent and set it aside
//the left out 10% is used as a crossvalidation set (evaluate parameters that fit to training set)
//the original 10% left out to evaluate hyperparameters that fit the CV^^ (examples that arent in CV but has not been trained on [real world data])

val validatorModel = validator.fit(unencTrainData) //returns best overall pipeline
val bestModel = validatorModel.bestModel
println(bestModel.asInstanceOf[PipelineModel].stages.last.extractParamMap)
val testAccuracy = multiclassEval.evaluate(bestModel.transform(unencTestData))
/*
validatorModel: org.apache.spark.ml.tuning.TrainValidationSplitModel = tvs_57824b5a24d2
bestModel: org.apache.spark.ml.Model[_] = pipeline_4f8064e18313
{
        dtc_6bfd33904b24-cacheNodeIds: false,
        dtc_6bfd33904b24-checkpointInterval: 10,
        dtc_6bfd33904b24-featuresCol: indexedVector,
        dtc_6bfd33904b24-impurity: gini,
        dtc_6bfd33904b24-labelCol: Cover_Type,
        dtc_6bfd33904b24-maxBins: 300,
        dtc_6bfd33904b24-maxDepth: 20,
        dtc_6bfd33904b24-maxMemoryInMB: 256,
        dtc_6bfd33904b24-minInfoGain: 0.0,
        dtc_6bfd33904b24-minInstancesPerNode: 1,
        dtc_6bfd33904b24-predictionCol: prediction,
        dtc_6bfd33904b24-probabilityCol: probability,
        dtc_6bfd33904b24-rawPredictionCol: rawPrediction,
        dtc_6bfd33904b24-seed: -4035647379714359620
}
testAccuracy: Double = 0.9245599764628512
*/