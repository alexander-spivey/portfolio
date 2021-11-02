/** ABOMNINATION**/
case class MatchData(id1:Int, id2: Int, scores: Array[Double], matched: Boolean) //create our own class to hold the data instead tuple

def isHeader(line: String) = line.contains("id_1")

def toDouble(s: String) = { //our own modified version of toDouble
     if("?".equals(s)) Double.NaN //if the char is '?' then return Double.NaN
     else
     s.toDouble //use the string.toDouble function if normal
}

//won't convert using the new defintion, so tinker around this
/*def parse(line:String) = { //same method as before, instead of returning a tuple, it returns an object of MatchData
	val pieces = line.split(',')
	val id1 = pieces(0).toInt
	val id2 = pieces(1).toInt
	val cmp_fname_c1 = pieces(2).map(toDouble)
	val cmp_fname_c2 = pieces(3).map(toDouble)
	val cmp_lname_c1 = pieces(4).map(toDouble)
	val cmp_lname_c2 = pieces(5).map(toDouble)
	val cmp_sex = pieces(6).map(toDouble)
	val cmp_bd = pieces(7).map(toDouble)
	val cmp_bm = pieces(8).map(toDouble)
	val cmp_by = pieces(9).map(toDouble)
	val cmp_plz = pieces(10).map(toDouble)
	//val scores = pieces.slice(2,11).map(toDouble)
	val scores = Array(cmp_fname_c1, cmp_fname_c2, cmp_lname_c1, cmp_lname_c2, cmp_sex, cmp_bd, cmp_bm, cmp_by, cmp_plz)
	val matched = pieces(11).toBoolean
	MatchData(id1, id2, scores, matched)
}*/

def parse(line:String) = { //same method as before, instead of returning a tuple, it returns an object of MatchData
	val pieces = line.split(',')
	val id1 = pieces(0).toInt
	val id2 = pieces(1).toInt
	val scores = pieces.slice(2,11).map(toDouble)
	val matched = pieces(11).toBoolean
	MatchData(id1, id2, scores, matched)
}

val rawItems = sc.textFile("linkage/csv")
val noHeader = rawItems.filter(!isHeader(_))

noHeader.take(10).foreach(println)
val mds = noHeader.map(line => parse(line))

val mdsDF = mds.toDF
mdsDF.show()
/*
scala> mdsDF.printSchema()
root
 |-- id1: integer (nullable = false)
 |-- id2: integer (nullable = false)
 |-- scores: array (nullable = true)
 |    |-- element: double (containsNull = false)
 |-- matched: boolean (nullable = false)
*/

val line = mdsDF.first
val arraything = 

case class Score(value: Double) {
	def +(oi: Option[Int]) = {
		Score(value + oi.getOrElse(0))
	}
}

def scoreMatchData(md: MatchData): Double = {
	(Score(md.cmp_lname_c1.getOrElse(0.0)) + 
	md.cmp_plz + md.cmp_bd + md.cmp_bm).value
}

val scored = matchData.map { md =>
	(scoreMatchData(md), md.is_match)
	}.toDF("score", "is_match")
