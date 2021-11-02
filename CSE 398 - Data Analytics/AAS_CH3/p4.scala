//--Evaluating Recommendation Quality--
/*
The system thinks only the songs that user listens to are good, and all else are bad. So what if we exclude some of the artist plays?
That way, this held out data can be thought of some the best recommendations to the user. We can basically say, 1 is a perfect rec, 0.5
is like a randomly ranking artist, and 0 is worst score. This scoring system relates to a infro retrievel concept called ROC
(receiver operating characteristics) curve.  The metric mentioned ealier relates to the ROC's AUC (area under curve). AUC can be read
as the "randomly chosen good rec ranks above randomly chosen bad rec." When using AUC for recomendors, we compute AUC for each user and
find the mean. Another evlaution we can use (within RankingMetrics) is precision, recall, and mean average precision (MAP). 
Data is typically divided into 3 sets for machine learning: aining, cross-validation (CV), and test sets
*/

//--Computing AUC--
//PROVIDED BY SOURCE CODE
import scala.collection.mutable.ArrayBuffer
def areaUnderCurve( 
      positiveData: DataFrame,
      bAllArtistIDs: Broadcast[Array[Int]],
      predictFunction: (DataFrame => DataFrame)): Double = {

    // What this actually computes is AUC, per user. The result is actually something
    // that might be called "mean AUC".

    // Take held-out data as the "positive".
    // Make predictions for each of them, including a numeric score
    val positivePredictions = predictFunction(positiveData.select("user", "artist")).
      withColumnRenamed("prediction", "positivePrediction")

    // BinaryClassificationMetrics.areaUnderROC is not used here since there are really lots of
    // small AUC problems, and it would be inefficient, when a direct computation is available.

    // Create a set of "negative" products for each user. These are randomly chosen
    // from among all of the other artists, excluding those that are "positive" for the user.
    val negativeData = positiveData.select("user", "artist").as[(Int,Int)].
      groupByKey { case (user, _) => user }.
      flatMapGroups { case (userID, userIDAndPosArtistIDs) =>
        val random = new Random()
        val posItemIDSet = userIDAndPosArtistIDs.map { case (_, artist) => artist }.toSet
        val negative = new ArrayBuffer[Int]()
        val allArtistIDs = bAllArtistIDs.value
        var i = 0
        // Make at most one pass over all artists to avoid an infinite loop.
        // Also stop when number of negative equals positive set size
        while (i < allArtistIDs.length && negative.size < posItemIDSet.size) {
          val artistID = allArtistIDs(random.nextInt(allArtistIDs.length))
          // Only add new distinct IDs
          if (!posItemIDSet.contains(artistID)) {
            negative += artistID
          }
          i += 1
        }
        // Return the set with user ID added back
        negative.map(artistID => (userID, artistID))
      }.toDF("user", "artist")

    // Make predictions on the rest:
    val negativePredictions = predictFunction(negativeData).
      withColumnRenamed("prediction", "negativePrediction")

    // Join positive predictions to negative predictions by user, only.
    // This will result in a row for every possible pairing of positive and negative
    // predictions within each user.
    val joinedPredictions = positivePredictions.join(negativePredictions, "user").
      select("user", "positivePrediction", "negativePrediction").cache()

    // Count the number of pairs per user
    val allCounts = joinedPredictions.
      groupBy("user").agg(count(lit("1")).as("total")).
      select("user", "total")
    // Count the number of correctly ordered pairs per user
    val correctCounts = joinedPredictions.
      filter($"positivePrediction" > $"negativePrediction").
      groupBy("user").agg(count("user").as("correct")).
      select("user", "correct")

    // Combine these, compute their ratio, and average over all users
    val meanAUC = allCounts.join(correctCounts, "user").
      select($"user", ($"correct" / $"total").as("auc")).
      agg(mean("auc")).
      as[Double].first()

    joinedPredictions.unpersist()

    meanAUC
  }
//areaUnderCurve: (positiveData: org.apache.spark.sql.DataFrame, bAllArtistIDs: org.apache.spark.broadcast.Broadcast[Array[Int]], 
	//predictFunction: org.apache.spark.sql.DataFrame => org.apache.spark.sql.DataFrame)Double
	

val allData = buildCounts(rawUserArtistData, bArtistAlias)	//create an array with alias fixed
val Array(trainData, cvData) = allData.randomSplit(Array(0.9, 0.1))	//split the array, 90% into trainData and 10% cvData
trainData.cache()	//store to memory
cvData.cache()	//store to memory
/*
allData: org.apache.spark.sql.DataFrame = [user: int, artist: int ... 1 more field]
trainData: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [user: int, artist: int ... 1 more field]
cvData: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [user: int, artist: int ... 1 more field]
res32: trainData.type = [user: int, artist: int ... 1 more field]
res33: cvData.type = [user: int, artist: int ... 1 more field]

scala> allData.take(5).foreach(println)
[1000002,1,55]
[1000002,1000006,33]
[1000002,1000007,8]
[1000002,1000009,144]
[1000002,1000010,314]
*/

val allArtistIDs = allData.select("artist").as[Int].distinct().collect()	//collect all distinct artists ids
val bAllArtistIDs = spark.sparkContext.broadcast(allArtistIDs)	//create a read only
val model = new ALS().
    setSeed(Random.nextLong()).
    setImplicitPrefs(true).
    setRank(10).setRegParam(0.01).setAlpha(1.0).setMaxIter(5).
    setUserCol("user").setItemCol("artist").
    setRatingCol("count").setPredictionCol("prediction").
    fit(trainData)
areaUnderCurve(cvData, bAllArtistIDs, model.transform)
/*
model: org.apache.spark.ml.recommendation.ALSModel = als_c65315b0e83d
res38: Double = 0.912093562911161	//AYE WE GOT WAY HIGHER THAN BOOK
*/

def predictMostListened(train: DataFrame)(allData: DataFrame) = { //model of most listened artists
	val listenCounts = train.
		groupBy("artist").
		agg(sum("count").as("prediction")).
		select("artist", "prediction")
	allData.
		join(listenCounts, Seq("artist"), "left_outer").
		select("user", "artist", "prediction")
}
areaUnderCurve(cvData, bAllArtistIDs, predictMostListened(trainData))
/*
predictMostListened: (train: org.apache.spark.sql.DataFrame)(allData: org.apache.spark.sql.DataFrame)org.apache.spark.sql.DataFrame
res40: Double = 0.8857297321607271 //HAHAHA NONPERSONALIZED IS LESSS
*/"""Clearly, the model needs some tuning. Can it be made better?""" //HAHAHAHA NOPE, IM BETTER THAN U