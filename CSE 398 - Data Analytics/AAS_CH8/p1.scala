/*
• Start Spark shell like this:
spark-shell --master local[*] --driver-memory 4g --jars /proj/cse398-
498/course/aas/ch08-geotime/target/ch08-geotime-2.0.0-jar-with-dependencies.jar
• You’ll also need GEOJSON file for NYC boroughs:
/proj/cse398-498/course/AAS_CH8/nyc-boroughs.geojson
*/



//--Geospatial and Temporal Data Analysis on New York City Taxi Trip Data--//
/*
New york is widely known for its yellow taxis. The only consensus people say is
during 4 to 5, take the subway instead since thats the shift change. On March 4th
2014, New York City Taxi and Limousine Commission shared an infographic on its 
Twitter account that displayed the number of taxis on the road and the numbers
occupied at a given time. This information supported the earlier claim since during
the shift change, taxi drivers have to drive back to there center to return the
taxi for the next shift. Chris Wong noticed this release and followed up asking
for more data if they had. Taxi commision agreed if he turned in a Freedom of
Information Law request. He then released the two 500 gb hardrives publicly online.

One of the important statistics within the taxi field would be utilization, or
amount of time/duration that a taxi is being used. During rush hour, drivers may
earn fares nonstop, whereas at 2am, it may take some time. To sort data like this,
there exist temporal data, dates/time, and geospatial information, like points 
of longitutde and latitude, that will have to be analyzed. Sparks capability rose
from Spark 1 to 2 in terms of capability to manipulate temporal data due to the 
release of Java 8's java.time package and the use of UDFS from Apache Hive
proj in SparkSQL. Geospatial data is still hard to manipulate and will require
3rd party and our own custom UDFs
*/


//--Getting the Data--//
//For this chapter, we used the fare data from January 2013, 2.5 gb
/*
$ mkdir taxidata
$ cd taxidata
$ curl -O https://nyctaxitrips.blob.core.windows.net/data/trip_data_1.csv.zip
$ unzip trip_data_1.csv.zip
$ head -n 10 trip_data_1.csv
*/
/*
Each row of the file after the header represents a single taxi ride in CSV format. 
For each ride, we have some attributes of the cab (a hashed version of the medallion 
number) as well as the driver (a hashed version of the hack license, which is what 
licenses to drive taxis are called), some temporal information about when the trip 
started and ended, and the longitude/latitude coordinates for where the passenger(s) 
were picked up and dropped off

The first row is our header, which shows our attributes:
medallion
hack_license
vendor_id
rate_code
store_and_fwd_flag
pickup_datetime
dropoff_datetime
passenger_count
trip_time_in_secs
trip_distance
pickup_longitude
pickup_latitude	dropoff_longitude
dropoff_latitude
*/


//--Working with Third-Party Libraries in Spark--//
/*
One of the major benefits of Scala being built on Java is that Java is such a
widely known and used language, that most code you may need is already available
or even open-source. However, the quality of each library differs drastically.
The library we choose we want it to keep working with the Serializable inter-
face, or be serialized using Kyro. We also want to make sure the libraries we use
have the least amount of dependencies (due to size and differ Java variations).
Lastly, we don't want our APIs that extensivly use Java-oriented design patterns
like abstract factories and visitors. Some even have Scala wrappers to increase
scalability and reduce boilerplate code. 
*/


//--Geospatial Data with the Esri Geometry API and Spray--//
/*
When working with geospatial data, there is 2 major kinds: vector and raster. 
Within the dataset, we have longitude/latitude and vector data stored in GeoJSON
format, which represents boundaries of the different boroughs within New York.
Sadly, there is only a library to parse these GeoJSON into Java objects, but no
other library to do spatial analyzation. Due to this, we will be working with
ESRI Geometry API, but can only parse a subjset of GeoJSON (need to clean data).
However, we can make a new Scala function for parsing all of GeoJSON.
*/

//-Exploring the Esri Geometry API-//
/*
Core data type is Geometry, holds shape and geolocation. Esri library can compute
are of geometry, whether two overlap, compute the geometry of overlap. In our case,
the geometry objects will represent dropoffs and boroughs. We want to know if a 
point is in one of the boroughs. All these methods are within GeometryEngine,
inclduingcontains operation. The contains method takes three arguments: 
two Geometry objects, one instance of the SpatialReference class, the coordinate system.
The SpatialReference will be using is WKID 4326, coord system for GPS. 
Following the naming convention, we are going to add some helper methods.
*/
import com.esri.core.geometry.{GeometryEngine, SpatialReference, Geometry}
import scala.language.implicitConversions

class RichGeometry(val geometry: Geometry,
    val spatialReference: SpatialReference =
      SpatialReference.create(4326)) {
  def area2D() = geometry.calculateArea2D()

  def contains(other: Geometry): Boolean = {
    GeometryEngine.contains(geometry, other, spatialReference)
  }

  def distance(other: Geometry): Double = {
    GeometryEngine.distance(geometry, other, spatialReference)
  }
}
//make it so that it implicitly converts all Geometry to RichGeometry
object RichGeometry {
  implicit def wrapRichGeo(g: Geometry) = {
    new RichGeometry(g)
  }
}
//import this implicit function
import RichGeometry._

//-Intro to GeoJSON-//
/*
The boundaries for boroughs are in GeoJSON format. The core object in GeoJSON is called a
feature, which is made up of a geometry instance and a set of key-value pairs called 
properties.A set of features is called a FeatureCollection. Download the data:
$ curl -O https://nycdatastables.s3.amazonaws.com/2013-08-19T18:15:35.172Z/
  nyc-borough-boundaries-polygon.geojson
$ mv nyc-borough-boundaries-polygon.geojson nyc-boroughs.geojson

Esri will parse the Geometry objects, but no the id nor properites. Use Spray to convert
any Scala object to a corresponding JsValue by calling an implicit toJson method. 
Convert string to parseJson then converting it to scala. 

We need to create a class to hold GeoJSON features. Each JSON object references its own
attribute.
*/
//create a function to look up values within properites
import spray.json._

case class Feature(
    val id: Option[JsValue],
    val properties: Map[String, JsValue],
    val geometry: RichGeometry) {
  def apply(property: String) = properties(property)
  def get(property: String) = properties.get(property)
}

//We need to also make a corresponding class for GeoJSON FeatureCollection.
case class FeatureCollection(features: Array[Feature])
    extends IndexedSeq[Feature] {
  def apply(index: Int) = features(index)
  def length = features.length
}

/*
After creating our case classes, we need a way to help Spray convert RichGeometry, Feature, 
and FeatureCollection along with a JsValue. 
*/
implicit object FeatureJsonFormat extends
    RootJsonFormat[Feature] {
  def write(f: Feature) = {
    val buf = scala.collection.mutable.ArrayBuffer(
      "type" -> JsString("Feature"),
      "properties" -> JsObject(f.properties),
      "geometry" -> f.geometry.toJson)
    f.id.foreach(v => { buf += "id" -> v})
    JsObject(buf.toMap)
  }

  def read(value: JsValue) = {
    val jso = value.asJsObject
    val id = jso.fields.get("id")
    val properties = jso.fields("properties").asJsObject.fields
    val geometry = jso.fields("geometry").convertTo[RichGeometry]
    Feature(id, properties, geometry)
  }
}
//The rest of the code can be seen within the jar file we are using.