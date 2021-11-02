//--Decision Tree Hyperparameters--
/*
Moving forward, we will no longer us AUC to measure. Now we will be using multiclass accuracy. 
Hyperparameters:
1. Maximum Depth - limits the numbers of levels in decision tree (usefull to prevent overfitting)
2. Maximum Bins - Bins are really a set of values to plug in to the decision tree, more bins, more optiomal decision rule
3. Impurtiy Measure - It is the probability that a randomly chosen classification of randmonl;yu chosen example is incorrect
	I_g(p) = 1 - N(E)i=1[p^2_i]
	If only one class, the value is 0, since the subset is pure
4. Entropy - "captures how much uncertainty the collection of target values in the subset implies about 
	predictions for data that falls in that subset"
5. Minimum Information Gain - imposes a min infro gain, or decrease in impurity for the specified decision rules
	If the ruleset doesn't improve impurity, it gets rejected
	Lowers overfitting (if doesn't divide the training data, the rule not gonna do much in real world)
*/

//--Tuning Decision Trees--
//We are setting up a pipeline that does both steps above (vector assemble and decision tree classsiferier)
import org.apache.spark.ml.Pipeline
val inputCols = trainData.columns.filter(_ != "Cover_Type") //training data is made into input by taking out covertype columns
val assembler = new VectorAssembler().	//create assembler
	setInputCols(inputCols).	//using input cols
	setOutputCol("featureVector")	//save output columns as a featureVector
val classifier = new DecisionTreeClassifier().
	setSeed(Random.nextLong()). //set as a random seed
	setLabelCol("Cover_Type").
	setFeaturesCol("featureVector").
	setPredictionCol("prediction")
val pipeline = new Pipeline().setStages(Array(assembler, classifier)) //pipeline is multiprocess, use Array to order stages
/*
inputCols: Array[String] = Array(Elevation, Aspect, Slope, Horizontal_Distance_To_Hydrology, Vertical_Distance_To_Hydrology, ...
classifier: org.apache.spark.ml.classification.DecisionTreeClassifier = dtc_0d2c61bd71f9
pipeline: org.apache.spark.ml.Pipeline = pipeline_656d68e09702
import org.apache.spark.ml.tuning.ParamGridBuilder
*/

import org.apache.spark.ml.tuning.ParamGridBuilder	//to test hyperparameters
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
/*
paramGrid: Array[org.apache.spark.ml.param.ParamMap] =
Array({
        dtc_9f33733f0207-impurity: gini,
        dtc_9f33733f0207-maxBins: 40,
        dtc_9f33733f0207-maxDepth: 1,
        dtc_9f33733f0207-minInfoGain: 0.0
}, {
        dtc_9f33733f0207-impurity: gini,
        dtc_9f33733f0207-maxBins: 300,
        dtc_9f33733f0207-maxDepth: 1,
        dtc_9f33733f0207-minInfoGain: 0.0
}, {
        dtc_9f33733f0207-impurity: entropy,
        dtc_9f33733f0207-maxBins: 40,
        dtc_9f33733f0207-maxDepth: 1,
        dtc_9f33733f0207-minInfoGain: 0.0
}, {
        dtc_9f33733f0207-impurity: entropy,
        dtc_9f33733f0207-maxBins: 300,
        dtc_9f33733f0207-maxDepth: 1,
        dtc_9f33733f0207-minInfoGain: 0.0
}, {
        dtc_9f33733f0207-impurity: gini,
        dtc_9f33733f0207-maxBins: 40,
        dtc_9f33733f0207-maxDepth: 20,
        dtc_9f33733f0207-minInfoGain: 0.0
}, {
        dtc_9f33733f0207-impurity: gini,
        dtc_9f3373...multiclassEval: org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator = mcEval_c1532b2b620e
*/

import org.apache.spark.ml.tuning.TrainValidationSplit	//can use CrossValidator here, but will coost k times more expensive & doesnt add much
val validator = new TrainValidationSplit().
	setSeed(Random.nextLong()).
	setEstimator(pipeline).
	setEvaluator(multiclassEval).
	setEstimatorParamMaps(paramGrid). //HYPER PARAMETERS CAN STILL OVER FIT
	setTrainRatio(0.9)	//take another 10 percent and set it aside
//the left out 10% is used as a crossvalidation set (evaluate parameters that fit to training set)
//the original 10% left out to evaluate hyperparameters that fit the CV^^ (examples that arent in CV but has not been trained on [real world data])
val validatorModel = validator.fit(trainData)	//returns best overall pipeline
/*import org.apache.spark.ml.tuning.TrainValidationSplit
validator: org.apache.spark.ml.tuning.TrainValidationSplit = tvs_2ecb5852718b
21/09/16 12:40:08 WARN RandomForest: Tree learning is using approximately 268474176 bytes per iteration, which exceeds requested limit maxMemoryUsage=268435456. This allows splitting 1647 nodes in this iteration.
21/09/16 12:40:12 WARN RandomForest: Tree learning is using approximately 268474176 bytes per iteration, which exceeds requested limit maxMemoryUsage=268435456. This allows splitting 1647 nodes in this iteration.
21/09/16 12:40:16 WARN RandomForest: Tree learning is using approximately 268474176 bytes per iteration, which exceeds requested limit maxMemoryUsage=268435456. This allows splitting 1647 nodes in this iteration.
21/09/16 12:40:20 WARN RandomForest: Tree learning is using approximately 268474176 bytes per iteration, which exceeds requested limit maxMemoryUsage=268435456. This allows splitting 1647 nodes in this iteration.
21/09/16 12:41:05 WARN RandomForest: Tree learning is using approximately 268474176 bytes per iteration, which exceeds requested limit maxMemoryUsage=268435456. This allows splitting 1647 nodes in this iteration.
21/09/16 12:41:09 WARN RandomForest: Tree learning is using approximately 268474176 bytes per iteration, which exceeds requested limit maxMemoryUsage=268435456. This allows splitting 1647 nodes in this iteration.
21/09/16 12:41:13 WARN RandomForest: Tree learning is using approximately 268474176 bytes per iteration, which exceeds requested limit maxMemoryUsage=268435456. This allows splitting 1647 nodes in this iteration.
validatorModel: org.apache.spark.ml.tuning.TrainValidationSplitModel = tvs_2ecb5852718b
*/

import org.apache.spark.ml.PipelineModel
val bestModel = validatorModel.bestModel	//extract best model
bestModel.asInstanceOf[PipelineModel].stages.last.extractParamMap	//extract parameters
/*
scala> bestModel.asInstanceOf[PipelineModel].stages.last.extractParamMap
res87: org.apache.spark.ml.param.ParamMap =
{
        dtc_d529a29ca28a-cacheNodeIds: false,
        dtc_d529a29ca28a-checkpointInterval: 10,
        dtc_d529a29ca28a-featuresCol: featureVector,
        dtc_d529a29ca28a-impurity: entropy,	//entropy was better than impurity
        dtc_d529a29ca28a-labelCol: Cover_Type,
        dtc_d529a29ca28a-maxBins: 40,	//40 bins was more than enough, 300 is too much lol
        dtc_d529a29ca28a-maxDepth: 20,	//WOW WHO KNEW MORE LEAVES DO BETTER? (1 leaf vs 20)
        dtc_d529a29ca28a-maxMemoryInMB: 256,
        dtc_d529a29ca28a-minInfoGain: 0.0,
        dtc_d529a29ca28a-minInstancesPerNode: 1,
        dtc_d529a29ca28a-predictionCol: prediction,
        dtc_d529a29ca28a-probabilityCol: probability,
        dtc_d529a29ca28a-rawPredictionCol: rawPrediction,
        dtc_d529a29ca28a-seed: -6060068181667196680
}
*/

//Say we want to see the results and score of each hyperparameters
val validatorModel = validator.fit(trainData) //redefine for assurance
val paramAndMetrics = validatorModel.validationMetrics.	//accessing the the models metrics
	zip(validatorModel.getEstimatorParamMaps).sortBy(-_._1)	//zip up all models params, sort by descending
paramsAndMetrics.foreach { case (metric, params) =>
    println(metric)
    println(params)
    println()
}
/*
0.9117241114902427
{
        dtc_d529a29ca28a-impurity: entropy,
        dtc_d529a29ca28a-maxBins: 40,
        dtc_d529a29ca28a-maxDepth: 20,
        dtc_d529a29ca28a-minInfoGain: 0.0
}

0.9095579496223594
{
        dtc_d529a29ca28a-impurity: entropy,
        dtc_d529a29ca28a-maxBins: 300,
        dtc_d529a29ca28a-maxDepth: 20,
        dtc_d529a29ca28a-minInfoGain: 0.0
}

0.9067975309588621
{
        dtc_d529a29ca28a-impurity: gini,
        dtc_d529a29ca28a-maxBins: 300,
        dtc_d529a29ca28a-maxDepth: 20,
        dtc_d529a29ca28a-minInfoGain: 0.0
}

0.9044971820726143
{
        dtc_d529a29ca28a-impurity: gini,
        dtc_d529a29ca28a-maxBins: 40,
        dtc_d529a29ca28a-maxDepth: 20,
        dtc_d529a29ca28a-minInfoGain: 0.0
}

0.738124448874746
{
        dtc_d529a29ca28a-impurity: entropy,
        dtc_d529a29ca28a-maxBins: 40,
        dtc_d529a29ca28a-maxDepth: 20,
        dtc_d529a29ca28a-minInfoGain: 0.05
}

0.7280795920714642
{
        dtc_d529a29ca28a-impurity: entropy,
        dtc_d529a29ca28a-maxBins: 300,
        dtc_d529a29ca28a-maxDepth: 20,
        dtc_d529a29ca28a-minInfoGain: 0.05
}

0.6732737798566116
{
        dtc_d529a29ca28a-impurity: gini,
        dtc_d529a29ca28a-maxBins: 300,
        dtc_d529a29ca28a-maxDepth: 20,
        dtc_d529a29ca28a-minInfoGain: 0.05
}

0.6722386228578001
{
        dtc_d529a29ca28a-impurity: gini,
        dtc_d529a29ca28a-maxBins: 40,
        dtc_d529a29ca28a-maxDepth: 20,
        dtc_d529a29ca28a-minInfoGain: 0.05
}

0.6384426638040103
{
        dtc_d529a29ca28a-impurity: gini,
        dtc_d529a29ca28a-maxBins: 300,
        dtc_d529a29ca28a-maxDepth: 1,
        dtc_d529a29ca28a-minInfoGain: 0.0
}

0.6384426638040103
{
        dtc_d529a29ca28a-impurity: gini,
        dtc_d529a29ca28a-maxBins: 300,
        dtc_d529a29ca28a-maxDepth: 1,
        dtc_d529a29ca28a-minInfoGain: 0.05
}

0.6379059157305524
{
        dtc_d529a29ca28a-impurity: gini,
        dtc_d529a29ca28a-maxBins: 40,
        dtc_d529a29ca28a-maxDepth: 1,
        dtc_d529a29ca28a-minInfoGain: 0.0
}

0.6379059157305524
{
        dtc_d529a29ca28a-impurity: gini,
        dtc_d529a29ca28a-maxBins: 40,
        dtc_d529a29ca28a-maxDepth: 1,
        dtc_d529a29ca28a-minInfoGain: 0.05
}

0.48834489897634475
{
        dtc_d529a29ca28a-impurity: entropy,
        dtc_d529a29ca28a-maxBins: 40,
        dtc_d529a29ca28a-maxDepth: 1,
        dtc_d529a29ca28a-minInfoGain: 0.0
}

0.48834489897634475
{
        dtc_d529a29ca28a-impurity: entropy,
        dtc_d529a29ca28a-maxBins: 40,
        dtc_d529a29ca28a-maxDepth: 1,
        dtc_d529a29ca28a-minInfoGain: 0.05
}

0.48834489897634475
{
        dtc_d529a29ca28a-impurity: entropy,
        dtc_d529a29ca28a-maxBins: 300,
        dtc_d529a29ca28a-maxDepth: 1,
        dtc_d529a29ca28a-minInfoGain: 0.0
}

0.48834489897634475
{
        dtc_d529a29ca28a-impurity: entropy,
        dtc_d529a29ca28a-maxBins: 300,	//LMAO WHAT 300 bins and 1 leaf... that is erm, definetly an intresting 1R
        dtc_d529a29ca28a-maxDepth: 1,
        dtc_d529a29ca28a-minInfoGain: 0.05
}
*/ 
//it is kinda self explanotary what happens to the score as you read the parameters

//-How well did the data do on the cv and test-
validatorModel.validationMetrics.max
//res90: Double = 0.9117241114902427
multiclassEval.evaluate(bestModel.transform(testData))
//res91: Double = 0.9145611417005437
/*
IN THIS CASE, NUMBERS ARE CLOSE AND MATCH, NOT ALWAYS THE CASE!!!
Tree made may be so good and fit perfectly for specific model, but overfits when compared to real stuff
A good way to double check is to test on CV for model evaluation and then test best model on an unused set(neither for training nor evaluation)
*/
multiclassEval.evaluate(bestModel.transform(trainData))
//res94: Double = 0.9501868137964811
//The model seem to be a bit overfitted, better for data then real world, a lower maximum depth may help