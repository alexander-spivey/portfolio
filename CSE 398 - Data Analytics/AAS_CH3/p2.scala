//--Building a First Model--
import org.apache.spark.sql._
import org.apache.spark.broadcast._


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
trainData.take(5).foreach(println)
/*
import org.apache.spark.sql._
import org.apache.spark.broadcast._
buildCounts: (rawUserArtistData: org.apache.spark.sql.Dataset[String], bArtistAlias: org.apache.spark.broadcast.Broadcast[Map[Int,Int]])org.apache.spark.sql.DataFrame
bArtistAlias: org.apache.spark.broadcast.Broadcast[scala.collection.immutable.Map[Int,Int]] = Broadcast(43)
trainData: org.apache.spark.sql.DataFrame = [user: int, artist: int ... 1 more field]
res71: trainData.type = [user: int, artist: int ... 1 more field]
[1000002,1,55]
[1000002,1000006,33]
[1000002,1000007,8]
[1000002,1000009,144]
[1000002,1000010,314]
*/

import org.apache.spark.ml.recommendation._
import scala.util.Random
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
/*
21/09/09 22:37:43 WARN BLAS: Failed to load implementation from: com.github.fommil.netlib.NativeSystemBLAS
21/09/09 22:37:43 WARN BLAS: Failed to load implementation from: com.github.fommil.netlib.NativeRefBLAS
21/09/09 22:37:43 WARN LAPACK: Failed to load implementation from: com.github.fommil.netlib.NativeSystemLAPACK
21/09/09 22:37:43 WARN LAPACK: Failed to load implementation from: com.github.fommil.netlib.NativeRefLAPACK
model: org.apache.spark.ml.recommendation.ALSModel = als_9a0e5c0e9737
*/
model.userFactors.show(1, truncate = false)
/*
+---+---------------------------------------------------------------------------------------------------------------------------+
|id |features                                                                                                                   |
+---+---------------------------------------------------------------------------------------------------------------------------+
|90 |[1.0062273, -0.055548448, -0.53881216, -0.08377492, -0.29344943, -0.10351685, -0.5451834, 0.25785673, 0.6842964, 0.7220503]|
+---+---------------------------------------------------------------------------------------------------------------------------+
only showing top 1 row

vs

scala> model.userFactors.show(1, truncate = true)
+---+--------------------+
| id|            features|
+---+--------------------+
| 90|[1.0062273, -0.05...|
+---+--------------------+
only showing top 1 row
*/
