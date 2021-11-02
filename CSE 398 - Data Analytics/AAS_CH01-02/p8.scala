//**Preparing Models for Production Enviroments
case class MatchData( //THIS IS WHAT THE BOOK WANTS ME TO DO, BUT THE PARSED INFERDSCHEMA SET MOST INTS TO STRINGS!!!
  id_1: String, //SUPPOSED TO BE INT BUT FOR SOME REASON IT WONT DO IT!
  id_2: Int,
  cmp_fname_c1: Option[Double], //using the Option[T] type to represent fields where may be null
  cmp_fname_c2: Option[Double],
  cmp_lname_c1: Option[Double],
  cmp_lname_c2: Option[Double],
  cmp_sex: Option[Int],
  cmp_bd: Option[Int],
  cmp_bm: Option[Int],
  cmp_by: Option[Int],
  cmp_plz: Option[Int],
  is_match: Boolean
)
//defined class MatchData

/* Back for when I was working on my own system 
case class MatchData( //this is my matchdata that works due to inferdschema for parsed
  id_1: String,
  id_2: String,
  cmp_fname_c1: Option[String], //using the Option[T] type to represent fields where may be null
  cmp_fname_c2: Option[String],
  cmp_lname_c1: Option[String],
  cmp_lname_c2: Option[String],
  cmp_sex: Option[String],
  cmp_bd: Option[String],
  cmp_bm: Option[String],
  cmp_by: Option[Int],
  cmp_plz: Option[Int],
  is_match: Boolean
)
*/

val matchData = parsed.as[MatchData]
matchData.show()

case class Score(value: Double) {
	def +(oi: Option[Int]) = { 
		Score(value + oi.getOrElse(0)) //return the value or 0 if it is missing
	}
}

def scoreMatchData(md: MatchData): Double = { //calculate a scoring for each 
	(Score(md.cmp_lname_c1.getOrElse(0.0)) + 
	md.cmp_plz + md.cmp_bd + md.cmp_bm).value
}

val scored = matchData.map { md => //for each matchdata
	(scoreMatchData(md), md.is_match) //calculate the matchdata and return it as a tuple with the md's is_match
	}.toDF("score", "is_match") //and convert it to a DataFrame with the two columns named respectively
/*
+-----+--------+
|score|is_match|
+-----+--------+
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
|  3.0|    true|
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
|  3.0|    true|
|  4.0|    true|
|  4.0|    true|
|  4.0|    true|
+-----+--------+
only showing top 20 rows
*/

/** AFTER THIS COMMENT, I TRY TO WORK WITH THE DIFFERENT DATA TYPE, BACK WHEN I WAS WORKING ON MY SYSTEM
/* Sad attempt to reparse data so string will be converted to doubles to work with the class
def toDouble(s: String) = { //our own modified version of toDouble
	if(null.equals(s)) Double.NaN //if the char is '?' then return Double.NaN
	else
	s.toDouble //use the string.toDouble function if normal
}
//toDouble: (s: String)Double

val dblParsed = parsed.flatMap(row => {
	val id_1 = row.getString(0).toInt
	val id_2 = row.getString(1).toInt
	(2 until row.size-1).map(i =>
	{(id_1, id_2, row.getString(i).toDouble)})
	})
dblParsed.show()
*/


case class Score(value: Double) {  //this is a modified version in hopes to get it work for strings
	def +(oi: String) = {
		if("null".equals(oi)) Score(Double.NaN)
		else
		Score(value.toDouble)
	}
}
**/
