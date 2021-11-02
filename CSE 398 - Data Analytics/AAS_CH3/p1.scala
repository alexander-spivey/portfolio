//--Preparing the Data--
val rawUserArtistData = //reading user_artist_data into rawUserArtistData
  spark.read.textFile("/proj/cse398-498/course/AAS_CH3/profiledata_06-May-2005/user_artist_data.txt")
rawUserArtistData.take(5).foreach(println) //take the top 5 and println each
/*
rawUserArtistData: org.apache.spark.sql.Dataset[String] = [value: string]
1000002 1 55
1000002 1000006 33
1000002 1000007 8
1000002 1000009 144
1000002 1000010 314
*/

val userArtistDF = rawUserArtistData.map { line => //for each line of rawUserArtistData
	val Array(user, artist, _*) = line.split(' ')  //create an array with user, artist, and unspecified by splitting line at space
	(user.toInt, artist.toInt) //convert user and artist id to int
	}.toDF("user", "artist") //save as a dataframe, with those two names respectively as columns
userArtistDF.agg( //do the following aggregations
	min("user"), max("user"), min("artist"), max("artist")).show()
/*
userArtistDF: org.apache.spark.sql.DataFrame = [user: int, artist: int]
+---------+---------+-----------+-----------+
|min(user)|max(user)|min(artist)|max(artist)|
+---------+---------+-----------+-----------+
|       90|  2443548|          1|   10794401| //values are small enough to leave as ints! (since max int value = 2147483647)
+---------+---------+-----------+-----------+
*/

val rawArtistData = spark.read.textFile("/proj/cse398-498/course/AAS_CH3/profiledata_06-May-2005/artist_data.txt")
/* This will error!
rawArtistData.map { line =>
  val (id, name) = line.span(_ != '\T') //split line at the very first tab
  (id.toInt, name.trim) //convert id to int and trim name
}.count() //this breaks when we call count, since some lines are corrupted (no tab or newline character)
*/
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
/*
artistByID: org.apache.spark.sql.DataFrame = [id: int, name: string]
[1134999,06Crazy Life]
[6821360,Pang Nakarin]
[10113088,Terfel, Bartoli- Mozart: Don]
[10151459,The Flaming Sidebur]
[6826647,Bodenstandig 3000]
*/

val rawArtistAlias = spark.read.textFile("/proj/cse398-498/course/AAS_CH3/profiledata_06-May-2005/artist_alias.txt")
val artistAlias = rawArtistAlias.flatMap { line => 	//use flatMap instead since it "flattens" collection or results into one dataset!	
		val Array(artist, alias) = line.split('\t')	//split line at the very first tab
		if (artist.isEmpty) {	//return none if artist id missing
			None
		} else { 
			Some((artist.toInt, alias.toInt))	//try to make le value
		}
	}.collect().toMap	//collect all since it doesnt specify and convert to map
artistAlias.head
/*
artistAlias: scala.collection.immutable.Map[Int,Int] = Map(1208690 -> 1003926, 2012757 -> 4569, 6949139 -> 1085752, 1109727 -> 1239120, 6772751 -> 1244705, 2070533 -> 1021544, 1157679 -> 2194, 9969617 -> 5630, 2034496 -> 1116214, 6764342 -> 40, 1272489 -> 1278238, 2108744 -> 1009267, 10349857 -> 1000052, 2145319 -> 1020463, 2126338 -> 2717, 10165456 -> 1001169, 6779368 -> 1239506, 10278137 -> 1001523, 9939075 -> 1329390, 2037201 -> 1274155, 1248585 -> 2885, 1106945 -> 1399, 6811322 -> 1019016, 9978396 -> 1784, 6676961 -> 1086433, 2117821 -> 2611, 6863616 -> 1277013, 6895480 -> 1000993, 6831632 -> 1246136, 1001719 -> 1009727, 10135633 -> 4250, 7029291 -> 1034635, 6967939 -> 1002734, 6864694 -> 1017311, 1237279 -> 1029752, 6793956 -> 1283231, 1208609 -> 1000699, 6693428 -> 1100258, 685174...res66: (Int, Int) = (1208690,1003926)
*/

artistByID.filter($"id" isin (1208690, 1003926)).show() //filter through id and show these two values
/*
+-------+----------------+
|     id|            name|
+-------+----------------+
|1208690|Collective Souls|
|1003926| Collective Soul|
+-------+----------------+
*/