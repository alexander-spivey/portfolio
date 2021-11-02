/*
All imports
*/
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.functions._

import com.esri.core.geometry.Point
import spray.json._
import com.cloudera.datascience.geotime._
import GeoJsonProtocol._

val spark = SparkSession.builder().getOrCreate()
import spark.implicits._



//--Preparing the New York City Taxi Trip Data--//
val taxiRaw = spark.read.option("header", "true").csv("/proj/cse398-498/course/AAS_CH8/taxidata")
taxiRaw.show()
/*
When using the automatic converter, it takes 2 folds. Which is incredibly
ineffecient and will be wasted even more when attributes are dropped for
analysis. DO custom conversion ourselves. If we want to take advantage
of the speed and processing power of the Dataset class, we must stick to 
small data types (int, string, double, long).
*/
case class Trip(
	license: String,
	pickupTime: Long,
	dropoffTime: Long,
	pickupX: Double,
	pickupY: Double,
	dropoffX: Double,
	dropoffY: Double
)
//As of now, time is long due to Unix epoch, and x&y will become a Point

//Create a method to parse information if null
class RichRow(row: org.apache.spark.sql.Row) {
	def getAs[T](field: String): Option[T] = {	//returns an Option[T] to
		if (row.isNullAt(row.fieldIndex(field))) {	//explicitly handle nulls
			None
		} else {
			Some(row.getAs[T](field))
		}
	}
}

//Parse string to get time in miliseconds
def parseTaxiTime(rr: RichRow, timeField: String): Long = {
 val formatter = new SimpleDateFormat(
     "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
 val optDt = rr.getAs[String](timeField)
 optDt.map(dt => formatter.parse(dt).getTime).getOrElse(0L)
}

//Convert pickup/dropoff locations from string to Doubles using implicit method
def parseTaxiLoc(rr: RichRow, locField: String): Double = {
	rr.getAs[String](locField).map(_.toDouble).getOrElse(0.0) //return 0 if null
}

//Combining all 3 methods into one:
def parse(row: org.apache.spark.sql.Row): Trip = {
val rr = new RichRow(row)
	Trip(
		license = rr.getAs[String]("hack_license").orNull,
		pickupTime = parseTaxiTime(rr, "pickup_datetime"),
		dropoffTime = parseTaxiTime(rr, "dropoff_datetime"),
		pickupX = parseTaxiLoc(rr, "pickup_longitude"),
		pickupY = parseTaxiLoc(rr, "pickup_latitude"),
		dropoffX = parseTaxiLoc(rr, "dropoff_longitude"),
		dropoffY = parseTaxiLoc(rr, "dropoff_latitude")
	)
}

//-Handling Invalid Records at Scale-//
/*
Many failures within the pipeline is due to data that doesn't confirm to
standard. Typically, this is a game of whack-a-mole; where the developer 
is fixing one error and getting another one, and so on. One way is to use 
try-catch blocks, and skip the handful of mistakes. In Spark, we can 
adapt our parsing to even work with invalid entries. 

There is 2 possible outcomes, success parsing or failure. If it is failure, 
we want to catch the entrie and exception. Whenever an operation has 2 
outcomes, we can use 
Either[L (success), R (failure, a tuple of entrie and excpetion)]
*/
def safe[S, T](f: S => T): S => Either[T, (S, Exception)] = {
	new Function[S, Either[T, (S, Exception)]] with Serializable {
		def apply(s: S): Either[T, (S, Exception)] = {
			try {
				Left(f(s))
			} catch {
				case e: Exception => Right((s, e))
			}
		}
	}
}

//Apply safe wrapper to parser to prevent parsing issues
val safeParse = safe(parse)
val taxiParsed = taxiRaw.rdd.map(safeParse) //no direct due to Either not in Dataset API
taxiParsed.map(_.isLeft).	//print number correctly parsed
	countByValue().
	foreach(println)
//(true,14776615)

//Since none failed, convert parsed to Dataset
val taxiGood = taxiParsed.map(_.left.get).toDS
taxiGood.cache()

/*
Just because everything parsed properly doesnt mean there are discrpencies within the
data. One of the top of the head, is if dropoff time is earlier than pickup.
*/
//Create a method to convert miliseconds to hours
val hours = (pickup: Long, dropoff: Long) => { 
	TimeUnit.HOURS.convert(dropoff - pickup, TimeUnit.MILLISECONDS)
}

//Wrap the hours in a UDF (UserDefinedFunction) to apply to both time columns
import org.apache.spark.sql.functions.udf
val hoursUDF = udf(hours)
taxiGood.
	groupBy(hoursUDF($"pickupTime", $"dropoffTime").as("h")).
	count().
	sort("h").
	show()	
	//returns a histogram of time and count (perfect use of DataSetAPI/SparkSQL)
/*
+---+--------+
|  h|   count|
+---+--------+
| -8|       1|	//one instance of negative 8 hours
|  0|14752326|
|  1|   22934|
|  2|     843|
|  3|     197|
|  4|      86|
|  5|      55|
|  6|      42|
|  7|      33|
|  8|      17|
|  9|       9|
| 10|      11|
| 11|      13|
| 12|       7|
| 13|       5|
| 14|       5|
| 15|       3|
| 16|       5|
| 17|       4|
| 19|       3|
+---+--------+
*/

//Analyze odd instance
taxiGood.
	where(hoursUDF($"pickupTime", $"dropoffTime") < 0).
	collect().
	foreach(println)
//Trip(4669D6DB6D5B6739B9194E999D907924,1359155305000,1359125716000,-73.952911,40.748318,-73.952835,40.748287)


//Analyzing histogram shows that most rides are no longer than 3 hours
spark.udf.register("hours", hours)	//registering our hours function as an SparkSql function
val taxiClean = taxiGood.where(
	"hours(pickupTime, dropoffTime) BETWEEN 0 AND 3"
)
//MAIN IDEA: Use Scala's Option[T] to deal with nulls and clean data using Sql

//-Geospatial Analysis-//
/*
Another instance we clean from the data are checking to see if trips start and end 
long/lat are within NY Broughs.
*/
//Read in our GeoJson file using the source class from scala.io
val geojson = scala.io.Source.
	fromFile("/proj/cse398-498/course/aas/ch08-geotime/src/main/resources/nyc-boroughs.geojson").
	mkString

//Using Esri and Spray to parse geojson to FeatureCollection
import com.cloudera.datascience.geotime._
import GeoJsonProtocol._
import spray.json._

val features = geojson.parseJson.convertTo[FeatureCollection]

//Lets try to test some random point and see where it may be
import com.esri.core.geometry.Point
val p = new Point(-73.994499, 40.75066)
val borough = features.find(f => f.geometry.contains(p))
// Some(Feature(Some(72),Map(boroughCode -> 1, borough -> "Manhattan", @id -> ...

/*
To increase time efficency, we are going to take the boroughs that are largest
and move them to the top, that way, statistically, our most common points
will load faster since they are higher up the hierachy.
*/
val areaSortedFeatures = features.sortBy(f => {
  val borough = f("boroughCode").convertTo[Int] //switch boroughs to #1-5
  (borough, -f.geometry.area2D())//based of 2d area
}) //scala auto sorts ascending order

//Write a function to to find which borough trips ended in
val bFeatures = sc.broadcast(areaSortedFeatures) //create copy to mess with

val bLookup = (x: Double, y: Double) => {
  val feature: Option[Feature] = bFeatures.value.find(f => {
    f.geometry.contains(new Point(x, y))
  })
  feature.map(f => {
    f("borough").convertTo[String]
  }).getOrElse("NA")
}
val boroughUDF = udf(bLookup) //convert to udf

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
/*
Most are typically in Manhattan, which is not a suprised, but what is suprising
is the number of NA counts.	
*/

//Print out these NA points
taxiClean.
  where(boroughUDF($"dropoffX", $"dropoffY") === "NA").
  show()
/*
|559F071794B721398...|1357615159000|1357616342000|       0.0|      0.0|       0.0|      0.0|
|A4B0B563E94A1C3AD...|1357593562000|1357593853000|       0.0|      0.0|       0.0|      0.0|
|559F071794B721398...|1357602162000|1357602593000|       0.0|      0.0|       0.0|      0.0|
|559F071794B721398...|1357607962000|1357608190000|       0.0|      0.0|       0.0|      0.0|
|4103ADCF50D18CFE2...|1358069880000|1358070240000|       0.0|      0.0|       0.0|      0.0|
|59BFB5C9B1E404F09...|1358093160000|1358093700000|       0.0|      0.0|       0.0|      0.0|
*/

//Filter out all cases where start and stop are 0.0 using SparkSql
val taxiDone = taxiClean.where(
  "dropoffX != 0 and dropoffY != 0 and pickupX != 0 and pickupY != 0"
).cache()

//Rerun histogram to show changes
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
//Cut most of NA down, and some others entries in other boroughs