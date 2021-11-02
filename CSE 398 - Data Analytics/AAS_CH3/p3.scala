//--Spot Checking Recommendations
val userID = 2093760	//given by the textbook to work with
val existingArtistIDS = trainData.
	filter($"user" === userID).	//filter lines where user is 2093760 (WHY 3 EQUALS SIGNS)
	select("artist").as[Int].collect()	//collect all artist the user listens to as ints
artistByID.filter($"id" isin (existingArtistIDS: _*)).show()	//filter those artists out!
/*
userID: Int = 2093760
existingArtistIDS: Array[Int] = Array(1180, 1255340, 378, 813, 942)
+-------+---------------+
|     id|           name|
+-------+---------------+
|   1180|     David Gray|
|    378|  Blackalicious|
|    813|     Jurassic 5|
|1255340|The Saw Doctors|
|    942|         Xzibit|
+-------+---------------+
*/

def makeRecommendations(model: ALSModel, userID: Int, howMany: Int): DataFrame = //the inputs and outputs
{
	val toRecommend = model.itemFactors.
		select($"id".as("artist")).
		withColumn("user", lit(userID)) 	//select all artist ids and pair with target user id
	model.transform(toRecommend).
		select("artist", "prediction").
		orderBy($"prediction".desc).
		limit(howMany)	//score all artists, with top score descending, limiting amount to howMany
}
//makeRecommendations: (model: org.apache.spark.ml.recommendation.ALSModel, userID: Int, howMany: Int)org.apache.spark.sql.DataFrame

val topRecommendations = makeRecommendations(model, userID, 5)
topRecommendations.show()
/*
+-------+-----------+
| artist| prediction|
+-------+-----------+
|   2814|0.026949896|
|1001819|0.026780745|
|1300642|0.025676597|
|1007614|0.025646321|
|   4605|0.025559124|
+-------+-----------+
*/

val recommendedArtistIDs =
	topRecommendations.select("artist").as[Int].collect() //from recommendedAristIDS steal the column artist and save
artistByID.filter($"id" isin (recommendedArtistIDs:_*)).show()
/*
+-------+----------+
|     id|      name|
+-------+----------+
|   2814|   50 Cent|
|   4605|Snoop Dogg|
|1007614|     Jay-Z|	//not great recommendation, seems to be all popular general hiphop artists
|1001819|      2Pac|
|1300642|  The Game|
+-------+----------+
*/