//--A First Decision Tree--
val Array(trainData, testData) = data.randomSplit(Array(0.9,0.1))
trainData.cache()
testData.cache()

//Spark MLlib requires all inputs to be vectors (arrays of doubles)
import org.apache.spark.ml.feature.VectorAssembler
val inputCols = trainData.columns.filter(_ != "Cover_Type") 	//all columns except the target are input features
val assembler = new VectorAssembler().	//Transformer
	setInputCols(inputCols).	//input columns
	setOutputCol("featureVector")	//output column
val assembledTrainData = assembler.transform(trainData) //transform the data
assembledTrainData.select("featureVector").show(truncate = false) //select the output column and don't truncate
/*
assembledTrainData: org.apache.spark.sql.DataFrame = [Elevation: int, Aspect: int ... 54 more fields]
+-----------------------------------------------------------------------------------------------------+
|featureVector                                                                                        |
+-----------------------------------------------------------------------------------------------------+
|(54,[0,1,2,3,4,5,6,7,8,9,13,15],[1863.0,37.0,17.0,120.0,18.0,90.0,217.0,202.0,115.0,769.0,1.0,1.0])  |
|(54,[0,1,2,5,6,7,8,9,13,18],[1874.0,18.0,14.0,90.0,208.0,209.0,135.0,793.0,1.0,1.0])                 |
|(54,[0,1,2,3,4,5,6,7,8,9,13,18],[1879.0,28.0,19.0,30.0,12.0,95.0,209.0,196.0,117.0,778.0,1.0,1.0])   |
|(54,[0,1,2,3,4,5,6,7,8,9,13,15],[1888.0,33.0,22.0,150.0,46.0,108.0,209.0,185.0,103.0,735.0,1.0,1.0]) |
|(54,[0,1,2,3,4,5,6,7,8,9,13,14],[1889.0,28.0,22.0,150.0,23.0,120.0,205.0,185.0,108.0,759.0,1.0,1.0]) |
|(54,[0,1,2,3,4,5,6,7,8,9,13,18],[1889.0,353.0,30.0,95.0,39.0,67.0,153.0,172.0,146.0,600.0,1.0,1.0])  |
|(54,[0,1,2,3,4,5,6,7,8,9,13,18],[1896.0,337.0,12.0,30.0,6.0,175.0,195.0,224.0,168.0,732.0,1.0,1.0])  |
|(54,[0,1,2,3,4,5,6,7,8,9,13,15],[1898.0,34.0,23.0,175.0,56.0,134.0,210.0,184.0,99.0,765.0,1.0,1.0])  |
|(54,[0,1,2,3,4,5,6,7,8,9,13,18],[1899.0,355.0,22.0,153.0,43.0,124.0,178.0,195.0,151.0,819.0,1.0,1.0])|
|(54,[0,1,2,3,4,5,6,7,8,9,13,14],[1901.0,311.0,9.0,30.0,2.0,190.0,195.0,234.0,179.0,726.0,1.0,1.0])   |
|(54,[0,1,2,3,4,5,6,7,8,9,13,14],[1903.0,5.0,13.0,42.0,4.0,201.0,203.0,214.0,148.0,708.0,1.0,1.0])    |
|(54,[0,1,2,3,4,5,6,7,8,9,13,16],[1903.0,67.0,16.0,108.0,36.0,120.0,234.0,207.0,100.0,969.0,1.0,1.0]) |
|(54,[0,1,2,3,4,5,6,7,8,9,13,14],[1904.0,51.0,26.0,67.0,30.0,162.0,222.0,175.0,72.0,711.0,1.0,1.0])   |
|(54,[0,1,2,3,4,5,6,7,8,9,13,18],[1905.0,19.0,27.0,134.0,58.0,120.0,188.0,171.0,108.0,636.0,1.0,1.0]) |
|(54,[0,1,2,3,4,5,6,7,8,9,13,14],[1905.0,33.0,27.0,90.0,46.0,150.0,204.0,171.0,89.0,725.0,1.0,1.0])   |
|(54,[0,1,2,3,4,5,6,7,8,9,13,15],[1906.0,356.0,20.0,150.0,55.0,120.0,184.0,201.0,151.0,726.0,1.0,1.0])|
|(54,[0,1,2,3,4,5,6,7,8,9,13,18],[1908.0,323.0,32.0,150.0,52.0,120.0,125.0,190.0,196.0,765.0,1.0,1.0])|
|(54,[0,1,2,3,4,5,6,7,8,9,13,18],[1916.0,320.0,24.0,190.0,60.0,162.0,151.0,210.0,195.0,832.0,1.0,1.0])|
|(54,[0,1,2,3,4,5,6,7,8,9,13,23],[1918.0,321.0,28.0,42.0,17.0,85.0,139.0,201.0,196.0,402.0,1.0,1.0])  |
|(54,[0,1,2,3,4,5,6,7,8,9,13,18],[1919.0,44.0,26.0,162.0,68.0,150.0,216.0,173.0,77.0,706.0,1.0,1.0])  |
+-----------------------------------------------------------------------------------------------------+
only showing top 20 rows
*/
//the output looks different than normal because this is represennted as a SparseVector(stores nonzero values and indices)

import org.apache.spark.ml.classification.DecisionTreeClassifier
import scala.util.Random
val classifier = new DecisionTreeClassifier().
	setSeed(Random.nextLong()).	//use random seed
	setLabelCol("Cover_Type").	//label columns by Cover_Type
	setFeaturesCol("featureVector").	//label as featureVector
	setPredictionCol("prediction")	//self explanatory
val model = classifier.fit(assembledTrainData)	//use classifier on assembledTrainData
println(model.toDebugString)	//print model
/*
import org.apache.spark.ml.classification.DecisionTreeClassifier
import scala.util.Random
classifier: org.apache.spark.ml.classification.DecisionTreeClassifier = dtc_f89d84fc3a06
model: org.apache.spark.ml.classification.DecisionTreeClassificationModel = DecisionTreeClassificationModel (uid=dtc_f89d84fc3a06) of depth 5 with 63 nodes
DecisionTreeClassificationModel (uid=dtc_f89d84fc3a06) of depth 5 with 63 nodes
  If (feature 0 <= 3034.0)
   If (feature 0 <= 2476.0)
    If (feature 3 <= 0.0)
     If (feature 13 <= 0.0)
      If (feature 17 <= 0.0)
       Predict: 6.0
      Else (feature 17 > 0.0)
       Predict: 6.0
     Else (feature 13 > 0.0)
      If (feature 23 <= 0.0)
       Predict: 4.0
      Else (feature 23 > 0.0)
       Predict: 3.0
    Else (feature 3 > 0.0)
     If (feature 16 <= 0.0)
      If (feature 9 <= 577.0)
       Predict: 3.0
      Else (feature 9 > 577.0)
       Predict: 3.0
     Else (feature 16 > 0.0)
      If (feature 9 <= 1279.0)
       Predict: 3.0
      Else (feature 9 > 1279.0)
       Predict: 4.0
   Else (feature 0 > 2476.0)
    If (feature 17 <= 0.0)
     If (feature 15 <= 0.0)
      If (feature 0 <= 2924.0)
       Predict: 2.0
      Else (feature 0 > 2924.0)
       Predict: 2.0
     Else (feature 15 > 0.0)
      If (feature 9 <= 1370.0)
       Predict: 3.0
      Else (feature 9 > 1370.0)
       Predict: 3.0
    Else (feature 17 > 0.0)
     If (feature 0 <= 2694.0)
      If (feature 0 <= 2639.0)
       Predict: 3.0
      Else (feature 0 > 2639.0)
       Predict: 3.0
     Else (feature 0 > 2694.0)
      If (feature 5 <= 1200.0)
       Predict: 5.0
      Else (feature 5 > 1200.0)
       Predict: 2.0
  Else (feature 0 > 3034.0)
   If (feature 0 <= 3313.0)
    If (feature 7 <= 239.0)
     If (feature 0 <= 3100.0)
      If (feature 3 <= 162.0)
       Predict: 1.0
      Else (feature 3 > 162.0)
       Predict: 2.0
     Else (feature 0 > 3100.0)
      If (feature 5 <= 1022.0)
       Predict: 1.0
      Else (feature 5 > 1022.0)
       Predict: 1.0
    Else (feature 7 > 239.0)
     If (feature 3 <= 313.0)
      If (feature 0 <= 3186.0)
       Predict: 1.0
      Else (feature 0 > 3186.0)
       Predict: 1.0
     Else (feature 3 > 313.0)
      If (feature 0 <= 3208.0)
       Predict: 2.0
      Else (feature 0 > 3208.0)
       Predict: 1.0
   Else (feature 0 > 3313.0)
    If (feature 12 <= 0.0)
     If (feature 3 <= 277.0)
      If (feature 6 <= 206.0)
       Predict: 1.0
      Else (feature 6 > 206.0)
       Predict: 7.0
     Else (feature 3 > 277.0)
      If (feature 10 <= 0.0)
       Predict: 1.0
      Else (feature 10 > 0.0)
       Predict: 1.0
    Else (feature 12 > 0.0)
     If (feature 45 <= 0.0)
      If (feature 0 <= 3370.0)
       Predict: 7.0
      Else (feature 0 > 3370.0)
       Predict: 7.0
     Else (feature 45 > 0.0)
      If (feature 5 <= 914.0)
       Predict: 7.0
      Else (feature 5 > 914.0)
       Predict: 1.0
*/ 
// "Here, for historical reasons, the features are only referred to by number, not name, unfortunately" - MAD DUMB AND STUPID AND DUMB >:(

model.featureImportances.toArray.zip(inputCols).	//zip up a list of input columns based on its input feature contribution
	sorted.reverse.foreach(println)	//print it descending
/*
(0.8128454136618612,Elevation)
(0.03861086575747634,Soil_Type_3)
(0.030084100586133485,Soil_Type_1)
(0.029800200066844286,Horizontal_Distance_To_Hydrology)
(0.025883165249676293,Hillshade_Noon)
(0.017179393227441293,Soil_Type_31)
(0.01479539080531332,Horizontal_Distance_To_Roadways)
(0.011681578518113746,Wilderness_Area_2)
(0.006427023563056407,Horizontal_Distance_To_Fire_Points)
(0.003490393305697935,Soil_Type_2)
(0.0030626806129993253,Wilderness_Area_0)
(0.002706494899578449,Wilderness_Area_3)
(0.0025150989332858712,Hillshade_9am)
(9.182008125223037E-4,Soil_Type_9)
(0.0,Wilderness_Area_1)
(0.0,Vertical_Distance_To_Hydrology)
(0.0,Soil_Type_8)
(0.0,Soil_Type_7)
(0.0,Soil_Type_6)
(0.0,Soil_Type_5)
(0.0,Soil_Type_4)
(0.0,Soil_Type_39)
(0.0,Soil_Type_38)
(0.0,Soil_Type_37)
(0.0,Soil_Type_36)
(0.0,Soil_Type_35)
(0.0,Soil_Type_34)
(0.0,Soil_Type_33)
(0.0,Soil_Type_32)
(0.0,Soil_Type_30)
(0.0,Soil_Type_29)
(0.0,Soil_Type_28)
(0.0,Soil_Type_27)
(0.0,Soil_Type_26)
(0.0,Soil_Type_25)
(0.0,Soil_Type_24)
(0.0,Soil_Type_23)
(0.0,Soil_Type_22)
(0.0,Soil_Type_21)
(0.0,Soil_Type_20)
(0.0,Soil_Type_19)
(0.0,Soil_Type_18)
(0.0,Soil_Type_17)
(0.0,Soil_Type_16)
(0.0,Soil_Type_15)
(0.0,Soil_Type_14)
(0.0,Soil_Type_13)
(0.0,Soil_Type_12)
(0.0,Soil_Type_11)
(0.0,Soil_Type_10)
(0.0,Soil_Type_0)
(0.0,Slope)
(0.0,Hillshade_3pm)
(0.0,Aspect)
*/
//Most features (other than elevation) have no importance when predicting le cover type, which makes sense
//plant no care if soil best or not, just care if temp and water is good and matches, if not, lmao die

//The DecisionTreeClassificationModel we made is a transformer since we can transform a DF with vectors into a DF with predictions
val predictions = model.transform(assembledTrainData)
predictions.select("Cover_Type", "prediction", "probability").
	show(truncate = false)
/*
+----------+----------+----------------------------------------------------------------------------------------------------------------+
|Cover_Type|prediction|probability                                                                                                     |
+----------+----------+----------------------------------------------------------------------------------------------------------------+
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |4.0       |[0.0,0.0,0.024926686217008796,0.24853372434017595,0.6356304985337243,0.0,0.09090909090909091,0.0]               |
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|3.0       |3.0       |[0.0,0.0,0.007609668755595345,0.6777081468218442,0.2493285586392122,0.0,0.06535362578334826,0.0]                |
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|3.0       |3.0       |[0.0,0.0,0.007609668755595345,0.6777081468218442,0.2493285586392122,0.0,0.06535362578334826,0.0]                |
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
|6.0       |3.0       |[0.0,5.295768680824022E-5,0.05587035958269343,0.563257956892443,0.024731239739448182,0.0,0.3560874860986072,0.0]|
+----------+----------+----------------------------------------------------------------------------------------------------------------+
only showing top 20 rows
*/
//probability is a vector (aka chance for each cover type) [ignore the first index, 0, since it is always 0]

import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
val evaluator = new MulticlassClassificationEvaluator().
	setLabelCol("Cover_Type").
	setPredictionCol("prediction")
evaluator.setMetricName("accuracy").evaluate(predictions)
//res31: Double = 0.7025081046797959	//accuracy of classifier
evaluator.setMetricName("f1").evaluate(predictions)
//res32: Double = 0.6856366486945126	//The F1 score is the harmonic mean of the precision and recall

import org.apache.spark.mllib.evaluation.MulticlassMetrics
val predictionRDD = predictions.
	select("prediction", "Cover_Type").
	as[(Double, Double)].	//convert to dataset
	rdd	//convert to RDD
val multiclassMetrics = new MulticlassMetrics(predictionRDD)
multiclassMetrics.confusionMatrix
/*
122203.0  63458.0   81.0     0.0     41.0   3.0    5121.0
44925.0   206294.0  2783.0   34.0    341.0  38.0   745.0
0.0       4684.0    26996.0  354.0   38.0   102.0  0.0
0.0       10.0      1311.0   1133.0  0.0    0.0    0.0			
0.0       7864.0    274.0    0.0     419.0  0.0    0.0
0.0       5058.0    10069.0  124.0   9.0    412.0  0.0
8181.0    78.0      0.0      0.0     0.0    0.0    10280.0
*/
//the correct predictions are the counts along the diagonal and the predictions are everything else
	
//how to do confusionmatrix using dataframe api
val confusionMatrix = predictions.
	groupBy("Cover_Type").
	pivot("prediction", (1 to 7)).
	count().
	na.fill(0.0). //replace null with 0.0
	orderBy("Cover_Type")
confusionMatrix.show()
/*
+----------+------+------+-----+----+---+---+-----+
|Cover_Type|     1|     2|    3|   4|  5|  6|    7|
+----------+------+------+-----+----+---+---+-----+
|       1.0|122203| 63458|   81|   0| 41|  3| 5121|
|       2.0| 44925|206294| 2783|  34|341| 38|  745|
|       3.0|     0|  4684|26996| 354| 38|102|    0|
|       4.0|     0|    10| 1311|1133|  0|  0|    0|	
|       5.0|     0|  7864|  274|   0|419|  0|    0|
|       6.0|     0|  5058|10069| 124|  9|412|    0|
|       7.0|  8181|    78|    0|   0|  0|  0|10280|
+----------+------+------+-----+----+---+---+-----+
*/
//pretty asf!

//Create a random classifier based on cover_type probability distrubition
import org.apache.spark.sql.DataFrame
def classProbabilities(data: DataFrame): Array[Double] = {	//return an array double
		val total = data.count()
		data.groupBy("Cover_Type").count().	//count by the category
			orderBy("Cover_Type").	//order by category
			select("count").as[Double].	//convert to data set
			map(_ / total).
			collect()
	}
val trainPriorProbabilities = classProbabilities(trainData)
val testPriorProbabilities = classProbabilities(testData)
trainPriorProbabilities.zip(testPriorProbabilities).map { //Sum products of pairs in training test set
		case (trainProb, cvProb) => trainProb * cvProb		//where does cvProb comes from???
	}.sum
//res36: Double = 0.3772220889658342