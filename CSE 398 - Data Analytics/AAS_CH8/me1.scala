import scala.math.sqrt

//create a histogram of trips to borough
taxiClean.
	groupBy(boroughUDF($"dropoffX", $"dropoffY")).
	count().
	show()
/*
+-----------------------+--------+
|UDF(dropoffX, dropoffY)|   count|
+-----------------------+--------+
|                 Queens|  672192|
|                     NA|  339037|
|               Brooklyn|  715252|
|          Staten Island|    3338|
|              Manhattan|12979047|
|                  Bronx|   67434|
+-----------------------+--------+
*/
val distance = (pickupX: Double, pickupY: Double, dropoffX: Double, dropoffY: Double) => {
	sqrt((dropoffX - pickupX)*(dropoffX - pickupX) + (dropoffY - pickupY)*(dropoffY - pickupY))
}
spark.udf.register("distance", distance)	//registering our hours function as an SparkSql function
val distanceUDF = udf(distance)
taxiClean.groupBy(distanceUDF($"pickupX", $"pickupY", $"dropoffX", $"dropoffY").as("h")).count().sort("h").show()	
/*
+--------------------+------+
|                   h| count|
+--------------------+------+
|                 0.0|422606|
| 2.82842712474619E-6|     1|
|2.999999999531155...|   362|
|3.000000006636583E-6|    29|
|3.999999997006398E-6|   942|
|4.000000004111825...|   675|
|6.999999996537554E-6|   560|
|7.000000003642981E-6|   120|
|7.000000010748408E-6|   133|
|7.615773102496731...|    97|
|7.615773105295697E-6|     5|
|7.615773115558567E-6|    32|
|7.615773118357532E-6|     1|
|7.999999994012796E-6|   566|
|8.000000001118224E-6|   367|
|8.000000008223651E-6|   360|
|8.062257743807061E-6|   255|
|8.062257747332341E-6|   169|
| 8.06225775614554E-6|    78|
| 8.06225775967082E-6|    52|
+--------------------+------+
only showing top 20 rows
*/
//Before removing all NA instances, there is over 400,000 0 distance entries


taxiDone.groupBy(distanceUDF($"pickupX", $"pickupY", $"dropoffX", $"dropoffY").as("h")).count().sort("h").show()	
/*
+--------------------+------+
|                   h| count|
+--------------------+------+
|                 0.0|165564|
|2.999999999531155...|   361|
|3.000000006636583E-6|    29|
|3.999999997006398E-6|   940|
|4.000000004111825...|   673|
|6.999999996537554E-6|   560|
|7.000000003642981E-6|   119|
|7.000000010748408E-6|   133|
|7.615773102496731...|    97|
|7.615773105295697E-6|     5|
|7.615773115558567E-6|    32|
|7.615773118357532E-6|     1|
|7.999999994012796E-6|   566|
|8.000000001118224E-6|   365|
|8.000000008223651E-6|   360|
|8.062257743807061E-6|   255|
|8.062257747332341E-6|   169|
| 8.06225775614554E-6|    78|
| 8.06225775967082E-6|    52|
|8.544003739546916E-6|   120|
+--------------------+------+
*/
//There is still over 150,000 entries... Lets clean taxiDone

//Rerun histogram to show before
taxiDone.
  groupBy(boroughUDF($"dropoffX", $"dropoffY")).
  count().
  show()
/*
+-----------------------+--------+
|UDF(dropoffX, dropoffY)|   count|
+-----------------------+--------+
|                 Queens|  670912|
|                     NA|   62778|
|               Brooklyn|  714659|
|          Staten Island|    3333|
|              Manhattan|12971314|
|                  Bronx|   67333|
+-----------------------+--------+
*/

val taxiDone2 = taxiDone.where("distance(pickupX, pickupY, dropoffX, dropoffY) != 0")
taxiDone2.groupBy(boroughUDF($"dropoffX", $"dropoffY")).count().show().cache()
/*
+-----------------------+--------+
|UDF(dropoffX, dropoffY)|   count|
+-----------------------+--------+
|                 Queens|  644722|
|                     NA|   55003|
|               Brooklyn|  704557|
|          Staten Island|    2963|
|              Manhattan|12852982|
|                  Bronx|   64538|
+-----------------------+--------+
*/
/*
A bunch of entries dissapeared from all boroughs. Seems like just 
removing NA wasnt enough. Lets see if we can remove any instances
with less than one minute ride.
*/
taxiDone2.groupBy(distanceUDF($"pickupX", $"pickupY", $"dropoffX", $"dropoffY").as("h")).count().sort("h").show()
taxiDone2.where(distanceUDF($"pickupX", $"pickupY", $"dropoffX", $"dropoffY") < 3).take(10).foreach(println)
//Trip(BA96DE419E711691B9445D6A6307C170,1357071108000,1357071490000,-73.978165,40.757977,-73.989838,40.751171)
//3 distance is less than one km

	
//Create seconds function
val seconds = (pickup: Long, dropoff: Long) => { TimeUnit.HOURS.convert(dropoff - pickup, TimeUnit.SECONDS)}
val secondsUDF = udf(seconds)
spark.udf.register("seconds", seconds)	//registering our seconds function as an SparkSql function
taxiDone2.groupBy(secondsUDF($"pickupTime", $"dropoffTime").as("h")).count().sort("h").take(60).foreach(println)
/*
[1,3521]
[2,1849]
[3,2223]
[4,1617]
[5,1823]
[6,1714]
[7,1267]
[8,1906]
[9,1597]
[10,2170]
[11,2287]
[12,1832]
[13,2615]
[14,2207]
[15,3114]
[16,58919]
[17,2983]
[18,4408]
[19,3649]
[20,5243]
[21,5864]
[22,4679]
[23,7041]
[24,5457]
[25,8029]
[26,8890]
[27,6834]
[28,9914]
[29,8112]
[30,11188]
[31,12122]
[32,9528]
[33,190312]
[34,10333]
[35,14721]
[36,15347]
[37,11848]
[38,16682]
[39,12944]
[40,17812]
[41,18475]
[42,14459]
[43,19878]
[44,15489]
[45,21424]
[46,22059]
[47,16770]
[48,23180]
[49,17937]
[50,354006]
[51,25235]
[52,19492]
[53,26123]
[54,20239]
[55,27321]
[56,27703]
[57,21295]
[58,28741]
[59,22144]
[60,29942]
*/
val taxiDone3 = taxiDone2.where("seconds(pickupTime, dropoffTime) > 60 AND distance(pickupX, pickupY, dropoffX, dropoffY) < 3") //aproximately less than one minute and one km
taxiDone3.groupBy(boroughUDF($"dropoffX", $"dropoffY")).count().show
/*taxiDone3
+-----------------------+--------+
|UDF(dropoffX, dropoffY)|   count|
+-----------------------+--------+
|                 Queens|  619598|
|                     NA|   48708|
|               Brooklyn|  681154|
|          Staten Island|    2906|
|              Manhattan|11630506|
|                  Bronx|   63620|
+-----------------------+--------+
*/
/*
scala> taxiDone3.count()
res62: Long = 13046492

scala> taxiDone.count()
res61: Long = 14490329
*/
/*taxiDone
+-----------------------+--------+
|UDF(dropoffX, dropoffY)|   count|
+-----------------------+--------+
|                 Queens|  670912|
|                     NA|   62778|
|               Brooklyn|  714659|
|          Staten Island|    3333|
|              Manhattan|12971314|
|                  Bronx|   67333|
+-----------------------+--------+
*/

val sessions = taxiDone3.
repartition($"license").
sortWithinPartitions($"license", $"pickupTime").
cache()

def boroughDuration(t1: Trip, t2: Trip): (String, Long) = {
val b = bLookup(t1.dropoffX, t1.dropoffY)
val d = (t2.pickupTime - t1.dropoffTime) / 1000
(b, d)
}

val boroughDurations: DataFrame = sessions.mapPartitions(trips => {
val iter: Iterator[Seq[Trip]] = trips.sliding(2)
val viter = iter.filter(_.size == 2).filter(p => p(0).license == p(1).license)
viter.map(p => boroughDuration(p(0), p(1)))
}).toDF("borough", "seconds")

boroughDurations.
where("seconds > 0").
groupBy("borough").
agg(avg("seconds"), stddev("seconds")).
show()
/* taxidone (original)
+-------------+------------------+--------------------+
|      borough|      avg(seconds)|stddev_samp(seconds)|
+-------------+------------------+--------------------+
|       Queens| 15145.02921535893|   46184.65570022602|
|           NA| 11145.50690421012|   41062.38476837451|
|     Brooklyn|10924.258102953178|   40079.37390372924|
|Staten Island| 17012.34120171674|  41266.189555996105|
|    Manhattan| 3441.172764592876|   22029.98741240281|
|        Bronx|13846.641869522882|   41205.83813202717|
+-------------+------------------+--------------------+
*/
/* taxidone3
+-------------+------------------+--------------------+
|      borough|      avg(seconds)|stddev_samp(seconds)|
+-------------+------------------+--------------------+
|       Queens| 15987.87468371353|   46748.80682717808|
|           NA|12499.481820445773|   44664.82118171125|
|     Brooklyn|11703.938399555638|   41485.08013004814|
|Staten Island|18761.355064844025|  43003.091870988756|
|    Manhattan| 3847.903044809918|   23382.97123334362|
|        Bronx|14974.669308311672|  42677.088182751584|
+-------------+------------------+--------------------+
*/