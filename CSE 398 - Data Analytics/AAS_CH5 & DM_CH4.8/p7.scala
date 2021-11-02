//--Clustering in Action--
/*
Since from p6 we saw that 180 was the best choice based of entropy, we 
are going to rebuild it with 180 = k and show each label goes under what cluster
*/
val pipelineModel = fitPipeline4(data, 180)
val countByClusterLabel = pipelineModel.transform(data).
	select("cluster", "label").
	groupBy("cluster", "label").count().
	orderBy("cluster", "label")
countByClusterLabel.show()
/*
+-------+----------+-------+
|cluster|     label|  count|
+-------+----------+-------+
|      0|  ipsweep.|     40|
|      0|     nmap.|      6|
|      0|   normal.|   3374|
|      0|portsweep.|      2|
|      0|    satan.|      7|
|      0|    smurf.|2807852|
|      1|  neptune.|      3|
|      1|   normal.|  22641|
|      2|  neptune.|   1034|
|      2|   normal.|      4|
|      2|portsweep.|      7|
|      2|    satan.|      4|
|      3|  ipsweep.|     13|
|      3|  neptune.|   1044|
|      3|portsweep.|     14|
|      3|    satan.|      3|
|      4|  ipsweep.|     13|
|      4|  neptune.|   1038|
|      4|portsweep.|     14|
|      4|    satan.|      3|
+-------+----------+-------+
*/

val kMeansModel = pipelineModel.stages.last.asInstanceOf[KMeansModel] //grab the best kMeansmodel
val centroids = kMeansModel.clusterCenters	//save to centroids

val clustered = pipelineModel.transform(data)
val threshold = clustered.
	select("cluster", "scaledFeatureVector").as[(Int, Vector)].
	map { case (cluster, vec) => Vectors.sqdist(centroids(cluster), vec) }.
	orderBy($"value".desc).take(100).last //Single output implicitly named “value"
//the threshold would be the 100th farthest item within the cluster

val originalCols = data.columns
val anomalies = clustered.filter { row =>
		val cluster = row.getAs[Int]("cluster")	//grab the cluster we are workign with
		val vec = row.getAs[Vector]("scaledFeatureVector")	//grab scaled feature vector
		Vectors.sqdist(centroids(cluster), vec) >= threshold	//any that are above the threshold
	}.select(originalCols.head, originalCols.tail:_*)

anomalies.first()
/*
[9,tcp,telnet,SF,307,2374,0,0,1,0,0,1,0,1,0,1,3,1,0,0,0,0,1,1,0.0,0.0,0.0,0.0,1.0,0.0,0.0,69,4,0.03,0.0,4,0.01,0.75,0.0,0.0,0.0,0.0,normal.]
*/