//--My Extension Part 1--
import org.apache.spark.sql._
import org.apache.spark.broadcast._
import org.apache.spark.ml.recommendation._
import scala.util.Random
import scala.collection.mutable.ArrayBuffer
import java.util.Calendar;

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

val rawUserArtistData = //reading user_artist_data into rawUserArtistData
  spark.read.textFile("/proj/cse398-498/course/AAS_CH3/profiledata_06-May-2005/user_artist_data.txt")
 val userArtistDF = rawUserArtistData.map { line => //for each line of rawUserArtistData
	val Array(user, artist, _*) = line.split(' ')  //create an array with user, artist, and unspecified by splitting line at space
	(user.toInt, artist.toInt) //convert user and artist id to int
	}.toDF("user", "artist") //save as a dataframe, with those two names respectively as columns
	
val rawArtistData = spark.read.textFile("/proj/cse398-498/course/AAS_CH3/profiledata_06-May-2005/artist_data.txt")
val artistByID = rawArtistData.flatMap { line => //use flatMap instead since it "flattens" collection or results into one dataset!
		val (id, name) = line.span(_ != '\t') //split line at the very first tab
		if (name.isEmpty) {
			None //return none if name empty
		} else {
			try { //try to convert!!!
				Some((id.toInt, name.trim)) //using the option class, we can use some to simplify instead returning empty element
			} catch { //catch le error
			case _: NumberFormatException => None //if can't convert due to corruption, return none
			}
		}
	}.toDF("id", "name") //save column names as

val rawArtistAlias = spark.read.textFile("/proj/cse398-498/course/AAS_CH3/profiledata_06-May-2005/artist_alias.txt")
val artistAlias = rawArtistAlias.flatMap { line => 	//use flatMap instead since it "flattens" collection or results into one dataset!	
		val Array(artist, alias) = line.split('\t')	//split line at the very first tab
		if (artist.isEmpty) {	//return none if artist id missing
			None
		} else { 
			Some((artist.toInt, alias.toInt))	//try to make le value
		}
	}.collect().toMap	//collect all since it doesnt specify and convert to map
	
val bArtistAlias = spark.sparkContext.broadcast(artistAlias) 
//Broadcast variables allow the programmer to keep a read-only variable cached on each machine rather than shipping a copy of it with tasks
val allData = buildCounts(rawUserArtistData, bArtistAlias)	//create an array with alias fixed
var foo  = new ArrayBuffer[Int]()

val start = Calendar.getInstance()
val sh = start.get(Calendar.HOUR)
val sm = start.get(Calendar.MINUTE)
val ss = start.get(Calendar.SECOND)
printf("Start Time: %02d:%02d:%02d\n", sh, sm, ss);

for(i <- 0 to 10) 	//loop 11 times since unknown will most likely arrive multiple times
{
	val Array(trainData2, cvData) = allData.randomSplit(Array(0.9, 0.1))	//split the array, 90% into trainData and 10% cvData
	trainData2.cache()	//store to memory
	val model2 = new ALS().
		setSeed(Random.nextLong()).
		setImplicitPrefs(true).
		setRank(30).setRegParam(4.00).setAlpha(40.0).setMaxIter(5).
		setUserCol("user").setItemCol("artist").
		setRatingCol("count").setPredictionCol("prediction").
		fit(trainData2)
	val userID = 2093760
	val topRecommendations2 = makeRecommendations(model2, userID, 5)
	val recommendedArtistIDs2 = topRecommendations2.select("artist").as[Int].collect()
	artistByID.filter($"id" isin (recommendedArtistIDs2:_*)).show()
	recommendedArtistIDs2.foreach(foo += _)
}
/*
+-------+---------------+	//run 1 results
|     id|           name|
+-------+---------------+
|1034635|      [unknown]|
|   1784|Black Eyed Peas|
|   1786|     Bob Marley|
|    930|         Eminem|
|1001048|  Avril Lavigne|
+-------+---------------+

+-------+------------+	//run 2 results
|     id|        name|
+-------+------------+
|1034635|   [unknown]|
|    930|      Eminem|
|   1205|          U2|
|    250|     Outkast|
|    121|Beastie Boys|
+-------+------------+

+-------+------------+	//run 3 results
|     id|        name|
+-------+------------+
|   2814|     50 Cent|
|1034635|   [unknown]|
|    930|      Eminem|
|   4267|   Green Day|
|1058104|Gwen Stefani|
+-------+------------+

+-------+---------------+	//run 4 results
|     id|           name|
+-------+---------------+
|   2814|        50 Cent|
|1034635|      [unknown]|
|   1784|Black Eyed Peas|
|    930|         Eminem|
|    250|        Outkast|
+-------+---------------+

+-------+---------+	//run 5 results
|     id|     name|
+-------+---------+
|   2814|  50 Cent|
|1034635|[unknown]|
|    930|   Eminem|
|    250|  Outkast|
|   4267|Green Day|
+-------+---------+

+-------+------------+	//run 6 results
|     id|        name|
+-------+------------+
|1034635|   [unknown]|
|    930|      Eminem|
|   4267|   Green Day|
|    121|Beastie Boys|
|1001412|   blink-182|
+-------+------------+

+-------+------------+	//run 7 results
|     id|        name|
+-------+------------+
|1034635|   [unknown]|
|   1786|  Bob Marley|
|    930|      Eminem|
|   4267|   Green Day|
|    121|Beastie Boys|
+-------+------------+

+-------+------------+	//run 8 results
|     id|        name|
+-------+------------+
|   2814|     50 Cent|
|1034635|   [unknown]|
|    930|      Eminem|
|    250|     Outkast|
|1058104|Gwen Stefani|
+-------+------------+

+-------+------------+	//run 9 results
|     id|        name|
+-------+------------+
|   2814|     50 Cent|
|1034635|   [unknown]|
|    930|      Eminem|
|    250|     Outkast|
|    121|Beastie Boys|
+-------+------------+

+-------+------------+ 	//run 10 results
|     id|        name|
+-------+------------+
|   2814|     50 Cent|
|1034635|   [unknown]|
|    930|      Eminem|
|   1854| Linkin Park|
|1058104|Gwen Stefani|
+-------+------------+

+-------+------------+ 	//run 11 results
|     id|        name|
+-------+------------+
|   2814|     50 Cent|
|1034635|   [unknown]|
|    930|      Eminem|
|    250|     Outkast|
|1058104|Gwen Stefani|
+-------+------------+
*/
val end = Calendar.getInstance()
val eh = end.get(Calendar.HOUR)
val em = end.get(Calendar.MINUTE)
val es = end.get(Calendar.SECOND)
printf("End Time: %02d:%02d:%02d\n", eh, em, es);

val dh = eh-sh
val dm = em-sm
val ds = es-ss
printf("Total Time: %02d:%02d:%02d\n", dh, dm, ds);

import scala.collection.immutable.ListMap
val hoo = foo.groupBy(identity).mapValues(_.size) //convert the array to a map, containing each instance and occurances
/*
(1205,1)
(930,11)
(2814,7)
(121,4)
(1001048,1)
(1058104,4)
(1034635,11)
(1784,2)
(1786,2)
(4267,4)
(250,6)
(1001412,1)
(1854,1)
*/
val goo = ListMap(hoo.toSeq.sortWith(_._2 > _._2):_*) //flip the list over (sort by value)
/*
(930,11)
(1034635,11)
(2814,7)
(250,6)
(121,4)
(1058104,4)
(4267,4)
(1784,2)
(1786,2)
(1205,1)
(1001048,1)
(1001412,1)
(1854,1)
*/

var topRecs  = new ArrayBuffer[Int]()
goo.foreach(topRecs += _._1)	//add each key(artistID) to find artist names
/*930
1034635
2814
250
121
1058104
4267
1784
1786
1205
1001048
1001412
1854
*/

artistByID.filter($"id" isin (topRecs:_*)).show()	//show names of artists from topRecs
/*
+-------+---------------+
|     id|           name|
+-------+---------------+
|   2814|        50 Cent|	//3
|1034635|      [unknown]|	//2
|   1784|Black Eyed Peas|	//8
|   1786|     Bob Marley|	//9
|    930|         Eminem|	//1
|1001048|  Avril Lavigne|	//11
|   1205|             U2|	//10
|    250|        Outkast|	//4
|   1854|    Linkin Park|	//13
|   4267|      Green Day|	//7
|1058104|   Gwen Stefani|	//6
|    121|   Beastie Boys|	//5
|1001412|      blink-182|	//12
+-------+---------------+
*/


