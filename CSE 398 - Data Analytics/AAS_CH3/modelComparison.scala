import org.apache.spark.sql._
import org.apache.spark.broadcast._
import org.apache.spark.ml.recommendation._
import scala.util.Random

def buildCounts(
	rawUserArtistData: Dataset[String],	//input is a datset of strings
    bArtistAlias: Broadcast[Map[Int,Int]]): DataFrame = {	//return a dataframe
		rawUserArtistData.map { line =>	//for each line of rawUserArtistData
			val Array(userID, artistID, count) = line.split(' ').map(_.toInt)	//split each line at the space and map em to int
			val finalArtistID = 
				bArtistAlias.value.getOrElse(artistID, artistID) //Get artist’s alias if it exists, otherwise get original artist.
			(userID, finalArtistID, count)	//return the following tupple
		}.toDF("user", "artist", "count")	//name the following columns as such
	}

val bArtistAlias = spark.sparkContext.broadcast(artistAlias) //Broadcast variables allow the programmer to keep a read-only variable cached on each machine rather than shipping a copy of it with tasks
	//If we just use artistAlias directly, it takes up 15 mg of storage, so instead use broadcast so it hold in memory a copy for each executor
val trainData = buildCounts(rawUserArtistData, bArtistAlias) //THIS IS THE RESULTING DATASET
trainData.cache() //suggest to spark to store and keep in memory in cluster

val userID = 2093760	//given by the textbook to work with
val existingArtistIDS = trainData.
	filter($"user" === userID).	//filter lines where user is 2093760 (WHY 3 EQUALS SIGNS)
	select("artist").as[Int].collect()	//collect all artist the user listens to as ints
artistByID.filter($"id" isin (existingArtistIDS: _*)).show()	//filter those artists out!

val model = new ALS(). //construct model as an ALSModel
    setSeed(Random.nextLong()). //use a random seed
    setImplicitPrefs(true). 
    setRank(10).
    setRegParam(0.01).
    setAlpha(1.0).
    setMaxIter(5).
    setUserCol("user").
    setItemCol("artist").
    setRatingCol("count").
    setPredictionCol("prediction").
    fit(trainData)

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
val topRecommendations = makeRecommendations(model, userID, 5)
val recommendedArtistIDs = topRecommendations.select("artist").as[Int].collect() //from recommendedAristIDS steal the column artist and save
artistByID.filter($"id" isin (recommendedArtistIDs:_*)).show()

val allData = buildCounts(rawUserArtistData, bArtistAlias)	//create an array with alias fixed
val Array(trainData2, cvData) = allData.randomSplit(Array(0.9, 0.1))	//split the array, 90% into trainData and 10% cvData
trainData2.cache()	//store to memory
val model2 = new ALS().
    setSeed(Random.nextLong()).
    setImplicitPrefs(true).
    setRank(30).setRegParam(4.00).setAlpha(40.0).setMaxIter(5).
    setUserCol("user").setItemCol("artist").
    setRatingCol("count").setPredictionCol("prediction").
    fit(trainData2)
val topRecommendations2 = makeRecommendations(model2, userID, 5)
val recommendedArtistIDs2 = topRecommendations2.select("artist").as[Int].collect()
artistByID.filter($"id" isin (recommendedArtistIDs2:_*)).show()