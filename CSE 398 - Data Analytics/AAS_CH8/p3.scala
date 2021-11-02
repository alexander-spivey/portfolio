//--Sessionization in Spark--//
/*
Its been a while, but if we can recall, the point of this chapter was to find the
utilization of taxis in NY. For each driver, we need to sort each drive they had
per shift. This is called sessionization: the analysis on a single entity as it 
executes a series of events over time. 

Google does this for autocorrect. It tracks when an user types a query, stops, 
goes back and types a modified version of the original query, and then googled it,
clicked on a new link, and didn't return back. This lets us build a better
autocorrect than any dictionary could ever. It can even idenitify mistakes even
when everything is spelt correctly: untied stats -> united states. This idea
is used for query suggestions to OneBox (so no clicks needed)
*/

//-Building Sessions: Secondary Sorts in Spark-//
/*
The naive approach would be to use groupBy to create a session and then shift
the events around. This is not practical as it would require every entry to 
be in memory and will not scale. 

"In MapReduce, we can build sessions by performing a secondary sort, where we 
create a composite key made up of an identifier and a timestamp value, sort all 
of the records on the composite key, and then use a custom partitioner and grouping 
function to ensure that all of the records for the same identifier appear in the 
same output partition."
*/
val sessions = taxiDone.
	repartition($"license").	//make sure they have same license
	sortWithinPartitions($"license", $"pickupTime") //then sort by pickupTime
sessions.cache()
//When working with large sets like this, it is useful to cache/export out

/*
Create a method to calculate the amount of time from pickup to dropoff
and time inbetween to get next fare
*/
case class Trip(
  license: String,
  pickupTime: Long,
  dropoffTime: Long,
  pickupX: Double,
  pickupY: Double,
  dropoffX: Double,
  dropoffY: Double)

def boroughDuration(t1: Trip, t2: Trip): (String, Long) = {	
  val b = bLookup(t1.dropoffX, t1.dropoffY)
  val d = (t2.pickupTime - t1.dropoffTime) / 1000
  (b, d)
}

//Instead of using a loop to apply method to all sequential pairs, use sliding
val boroughDurations: DataFrame =
      sessions.mapPartitions(trips => {
        val iter: Iterator[Seq[Trip]] = trips.sliding(2)
        val viter = iter.
			filter(_.size == 2). //ignore if there is only 2 trips
			filter(p => p(0).license == p(1).license) //ignore if license not same
        viter.map(p => boroughDuration(p(0), p(1)))
      }).toDF("borough", "seconds") //returns as DF
boroughDurations.
    where("seconds > 0").
    groupBy("borough").
    agg(avg("seconds"), stddev("seconds")).
    show()
/*
+-----+--------+
|hours|   count|
+-----+--------+
|   -3|       2|
|   -2|      16|
|   -1|    4253|
|    0|13359033|
|    1|  347634|
|    2|   76286|
|    3|   24812|
|    4|   10026|
|    5|    4789|
*/

//Show average and standard deviation of the pickup times by borough
boroughDurations.
  where("seconds > 0 AND seconds < 60*60*4").
  groupBy("borough").
  agg(avg("seconds"), stddev("seconds")).
  show()
/*
+-------------+------------------+--------------------+
|      borough|      avg(seconds)|stddev_samp(seconds)|
+-------------+------------------+--------------------+
|       Queens|2380.6603554494727|  2206.6572799118035|
|           NA|  2006.53571169866|  1997.0891370324784|
|     Brooklyn| 1365.394576250576|  1612.9921698951398|
|Staten Island|         2723.5625|  2395.7745475546385|
|    Manhattan| 631.8473780726746|   1042.919915477234|
|        Bronx|1975.9209786770646|   1704.006452085683|
+-------------+------------------+--------------------+
*/
/*
As we expected Manhattan has the shortest time and somewhere
as far as Queens or Staten Island, have way long time. 
*/
