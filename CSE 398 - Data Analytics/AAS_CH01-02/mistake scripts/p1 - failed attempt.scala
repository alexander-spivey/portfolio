/* Okay, so this was my failed attempt at writing a script. Didn't realize I didn't need to define it as an object */
import org.apache.spark.util.StatCounter
import org.apache.spark.sql._

object p1 extends Serializable {
  def main(args: Array[String]): Unit = {
    val rdd = sc.parallelize(Array(1, 2, 2, 4), 4)
    val rawblocks = sc.textFile("linkage")   
    //rawblocks = sc.textFile("linkage")
    //this will cause an error due to val is not reassignable
    //var varblocks = sc.textFile("linkage")
    //varblocks = sc.textFile("linkage")
    //no error for var cause reassinable
    
    /*IF THIS WAS CONSOLE*/
    //val rawblocks = sc.textFile("linkage") 
    //val rawblocks = sc.textFile("linkage") 
    //allowed to redeclare the same immutable variable in SHELL ONLY
    rawblocks.first
  }
}


