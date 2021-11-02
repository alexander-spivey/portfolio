//--Making Recommendations
//Testing with best set of Hyperparameters!
val allData = buildCounts(rawUserArtistData, bArtistAlias)	//create an array with alias fixed
val Array(trainData2, cvData) = allData.randomSplit(Array(0.9, 0.1))	//split the array, 90% into trainData and 10% cvData
trainData2.cache()	//store to memory
val model2 = new ALS().
    setSeed(Random.nextLong()).
    setImplicitPrefs(true).
    setRank(30).setRegParam(4.00).setAlpha(40.0).setMaxIter(5).	//using our new best values!
    setUserCol("user").setItemCol("artist").
    setRatingCol("count").setPredictionCol("prediction").
    fit(trainData2)
val topRecommendations2 = makeRecommendations(model2, userID, 5)
val recommendedArtistIDs2 = topRecommendations2.select("artist").as[Int].collect()
artistByID.filter($"id" isin (recommendedArtistIDs2:_*)).show()
/*
+-------+-----------+
|     id|       name|	//WAY BETTER SUGGESTIONS~!
+-------+-----------+
|1034635|  [unknown]|
|    930|     Eminem|
|1270639|The Killers|
|   4267|  Green Day|
|1001412|  blink-182|
+-------+-----------+
*/

val someUsers = allData.select("user").as[Int].distinct().take(100)	//snatch 100 distinct users
val someRecommendations = someUsers.map(userID => (userID, makeRecommendations(model, userID, 5)))	//for each user make a recommendation
someRecommendations.foreach { case (userID, recsDF) =>
  val recommendedArtists = recsDF.select("artist").as[Int].collect()	//grab list of artists as recommendedArtists
  println(s"$userID -> ${recommendedArtists.mkString(", ")}") 	//print with user id followed by recommnededArtists
}
/*
1000190 -> 1003853, 6694932, 1006134, 1006411, 1244362
1001043 -> 1274, 1854, 4267, 1034635, 1205
1001129 -> 1034635, 121, 352, 1307, 189
1001139 -> 15, 2, 313, 1000113, 352
1002431 -> 4267, 3327, 976, 1000139, 979
1002605 -> 1205, 1000113, 1034635, 1274, 1275996
1004666 -> 1034635, 733, 1003084, 1295531, 1247272
1005158 -> 4267, 1001412, 1026440, 1854, 5409
1005439 -> 1000481, 2003588, 4468, 1000183, 1004162
1005697 -> 2814, 1001819, 930, 1300642, 1003249
1005853 -> 1001907, 1233770, 1001531, 1193, 352
1007007 -> 1177, 1002061, 1000113, 4267, 1205
1007847 -> 1000113, 979, 1205, 1000139, 976
1008081 -> 1003965, 1004077, 1235066, 1011262, 1005975
1008233 -> 1300642, 1117311, 1084205, 1008984, 1003249
1008804 -> 1298659, 1105902, 478, 1285410, 1006371
...
*/

/*
By swapping the fields of the user and artist, we can actually see the best user for artist
rawArtistData.map { line =>
  val (id, name) = line.span(_ != '\t')
  (name.trim, id.int)
}
*/