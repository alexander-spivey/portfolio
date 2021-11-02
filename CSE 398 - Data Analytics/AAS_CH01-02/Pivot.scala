import org.apache.spark.sql.DataFrame //import DataFrame
import org.apache.spark.sql.functions.first //import first

def pivotSummary(desc: DataFrame): DataFrame = { //returns a type dataframe
  val schema = desc.schema //create a schema of the input df (desc)
  import desc.sparkSession.implicits._ //erm another import??

  val lf = desc.flatMap(row => { //longform flat map by 
    val metric = row.getString(0) //grabbing metric from first row
    (1 until row.size).map(i => { //from rest on, create a tuple
      (metric, schema(i).name, row.getString(i).toDouble)
    })
  }).toDF("metric", "field", "value") //convert to data frame with these 3 columns

  lf.groupBy("field"). //group by field
    pivot("metric", Seq("count", "mean", "stddev", "min", "max")). //pivot around metric
    agg(first("value")) //does the first group of value
}
