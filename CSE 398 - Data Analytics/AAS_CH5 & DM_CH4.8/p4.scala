//--Feature Normalization--
/*
We can noemalize our attributes by converting it to standard score by taking the feature value and subtracting the feature means from it 
and dividing by standard deviation. 
normalized_i = (feature_i/u_i)/o_i
By standarizing the data, has no effect on clustering because it shifts all points and normalizes them. We used StandardScaler to do so.
*/
import org.apache.spark.ml.feature.StandardScaler
def clusteringScore2(data: DataFrame, k: Int): Double = {
	val assembler = new VectorAssembler().
		setInputCols(data.columns.filter(_ != "label")).
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

(60 to 270 by 30).map(k => (k, clusteringScore2(numericData, k))).
	foreach(println)

/*
(60,1.2085102766608882)
(90,0.7556338365580493)
(120,0.5066266588775019)
(150,0.4078229693345159)
(180,0.33735657183433865)
(210,0.2808956418075503)
(240,0.24121210670843768)
(270,0.22372480024764824)
*/