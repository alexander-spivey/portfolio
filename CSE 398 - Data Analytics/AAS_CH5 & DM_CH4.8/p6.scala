//--Using Labels with Entropy--
/*
We have been using the given label for each instance to doubel check the quality of clustering. This practice can be nromalized more.
We want all instances to pretty match up with the human based labels given, and not to put different labeled instances near one another.
In Chapter 4 we used Gini Impurity and Entropy. 
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
    pipeline.fit(data)
  }
  
def clusteringScore4(data: DataFrame, k: Int): Double = {
	val pipelineModel = fitPipeline4(data, k)

	// Predict cluster for each datum
	val clusterLabel = pipelineModel.transform(data).
		select("cluster", "label").as[(Int, String)]
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

(60 to 270 by 30).map(k => (k, clusteringScore4(data, k))).
	foreach(println)
