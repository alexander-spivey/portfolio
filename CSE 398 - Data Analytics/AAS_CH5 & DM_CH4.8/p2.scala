//--Choosing K--
/*
It is clear there are 23 distint types in our data, but k may be greater than 23. How do we find K? A clustering is considered good if its
data in the cluster is near its centroid, with closer the mean euclidian distance the better. Since there is no evaluator for K mean, we can use
kmeansModel computeCost to "computes the sum of squared distances and can easily be used to compute the mean squared distance."
*/
import org.apache.spark.sql.DataFrame
import scala.util.Random


def clusteringScore0(data: DataFrame, k: Int): Double = {	//input is DF and int for k, returns a double
	val assembler = new VectorAssembler().
		setInputCols(data.columns.filter(_ != "label")).
		setOutputCol("featureVector")	//create a vector including every column but label
	val kmeans = new KMeans().
		setSeed(Random.nextLong()).
		setK(k).	//expected number of clusters
		setPredictionCol("cluster").
		setFeaturesCol("featureVector")
	val pipeline = new Pipeline().setStages(Array(assembler, kmeans))
	val kmeansModel = pipeline.fit(data).stages.last.asInstanceOf[KMeansModel]		//grab our model
	kmeansModel.computeCost(assembler.transform(data)) / data.count() //"Compute mean from total squared distance (“cost”)"
}

(20 to 100 by 20).map(k => (k, clusteringScore0(numericData, k))).
//first we have a loop that goes from 20 to 100 by 20, then the value of k is set to the loop val, and then printed out as a tuple (k, score)
	foreach(println)
/*
(20,1.077001256461918E8)
(40,1.749847326308563E7)
(60,1.1806382683844185E7)
(80,1.5515384120060476E7)
(100,4691964.790611011)
*/
/*
as more clusters increase, we can have the data break down more, meaning each cluster increase will decrease the distance.
if we want to give as many clusters as data, we will have a distance of 0!!
Another thing to notice is that, from 60 to 80, distance increased. Kmeans is not necessarily able to find best cluster given k. 
Better versions: Kmeans++ and Kmeans||. Cant gurantee optimalness given the randomness. 
For k=80, the program may have been just a suboptimal cluster, or stopped early before maximum
We can improve everything by increasing iterations, by decreasing the setTol value, which allows the centroid to move longer
*/

def clusteringScore1(data: DataFrame, k: Int): Double = {	//input is DF and int for k, returns a double
	val assembler = new VectorAssembler().
		setInputCols(data.columns.filter(_ != "label")).
		setOutputCol("featureVector")	//create a vector including every column but label
	val kmeans = new KMeans().
		setSeed(Random.nextLong()).
		setK(k).	//expected number of clusters
		setMaxIter(40).	//increase from default 20 to 40
		setTol(1.0e-5).	//decease from deafult 1.0e-4
		setPredictionCol("cluster").
		setFeaturesCol("featureVector")
	val pipeline = new Pipeline().setStages(Array(assembler, kmeans))
	val kmeansModel = pipeline.fit(data).stages.last.asInstanceOf[KMeansModel]		//grab our model
	kmeansModel.computeCost(assembler.transform(data)) / data.count() //"Compute mean from total squared distance (“cost”)"
}
(20 to 100 by 20).map(k => (k, clusteringScore1(numericData, k))).
//first we have a loop that goes from 20 to 100 by 20, then the value of k is set to the loop val, and then printed out as a tuple (k, score)
	foreach(println)
/*
(20,5.454092597347078E7)
(40,4.566135854151461E7)
(60,3.787964426066939E7)
(80,9618261.718023658)
(100,9925572.773724418)
*/
/*
we can find the best k value for cluster when we see the decrease is no longer as exponential and more flat, which seems to show that the right 
value of k is most likely past 100
*/