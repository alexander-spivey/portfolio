//**Shipping Code from the Client to the Cluster**
val noheader = rawblocks.filter(!isHeader(_)) //create an rdd from rawblocks that filters out the header
//noheader: org.apache.spark.rdd.RDD[String] = MapPartitionsRDD[23] at filter at p2.scala:31

noheader.take(10).foreach(println) //take the first 10 elements from noheader and print them
/*
607,53170,1,?,1,?,1,1,1,1,1,TRUE
88569,88592,1,?,1,?,1,1,1,1,1,TRUE
21282,26255,1,?,1,?,1,1,1,1,1,TRUE
20995,42541,1,?,1,?,1,1,1,1,1,TRUE
27989,34739,1,?,1,?,1,1,1,1,1,TRUE
32442,69159,1,?,1,?,1,1,1,1,1,TRUE
24738,29196,1,1,1,?,1,1,1,1,1,TRUE
9904,89061,1,?,1,?,1,1,1,1,1,TRUE
29926,36578,1,?,1,?,1,1,1,1,1,TRUE
27815,46246,1,?,1,?,1,1,1,1,1,TRUE
*/

noheader.first //return the first element in noheader
//res75: String = 607,53170,1,?,1,?,1,1,1,1,1,TRUE

val firstLine = noheader.first //saving the first element to its own value
//firstLine: String = 607,53170,1,?,1,?,1,1,1,1,1,TRUE

val firstLineArray = firstLine.split(',') //create an array from the firstLine that is split at each coma (CSV)
//firstLineArray: Array[String] = Array(607, 53170, 1, ?, 1, ?, 1, 1, 1, 1, 1, TRUE)

firstLineArray.foreach(println) //print each element in firstLineArray
/*
607
53170
1
?
1
?
1
1
1
1
1
TRUE
*/

val id1 = firstLineArray(0).toInt //saving the id1 of firstLine's element by accessing the 0th item (first item) in firstLineArray
//id1: Int = 607

val id2 = firstLineArray(1).toInt //saving the id2 of firstLine's element by accessing the 1th item (2nd item) in firstLineArray
//id2: Int = 53170

val matchedCase = firstLineArray(11).toBoolean //saving the matched boolean to a value by accessing the 11th item in firstLineArray
//matchedCase: Boolean = true
//no cap, its so easy that it auto infers best value type

val rawScoresFLA = firstLineArray.slice(2,11) //save the rawscores to an array by slicing items 2 through 11 from firstLineArray
//rawScoresFLA: Array[String] = Array(1, ?, 1, ?, 1, 1, 1, 1, 1)

//rawScoresFLA.map(s => s.toDouble) errors due to the '?' try to use prebuilt, wont work due to the ? marks

def toDouble(s: String) = { //our own modified version of toDouble
	if("?".equals(s)) Double.NaN //if the char is '?' then return Double.NaN
	else
	s.toDouble //use the string.toDouble function if normal
}
//toDouble: (s: String)Double

val scoresFLA = rawScoresFLA.map(toDouble) //remap the values from rawScoredFLA to double and saving it to scores FLA
//scoresFLA: Array[Double] = Array(1.0, NaN, 1.0, NaN, 1.0, 1.0, 1.0, 1.0, 1.0)

def parse(line:String) = { //creating our own parse method that returns the new created values from each line into a tuple
	val pieces = line.split(',') //this to split the line its on based on the ','
	val id1 = firstLineArray(0).toInt //snatches value and save to id1
	val id2 = firstLineArray(1).toInt //save value to id2
	val scores = pieces.slice(2,11).map(toDouble) //slice scores from line, which is first transitioned to double
	val matched = pieces(11).toBoolean //save matched case to value
	(id1, id2, scores, matched) //returns a tuple
}
//parse: (line: String)(Int, Int, Array[Double], Boolean)
     
val tup = parse(firstLine) //saves tuple of firstLine to tup
//tup: (Int, Int, Array[Double], Boolean) = (607,53170,Array(1.0, NaN, 1.0, NaN, 1.0, 1.0, 1.0, 1.0, 1.0),true).

tup._1 //one way of accessing the first element
//res78: Int = 607
//tup._2 is basically same as tup.productElement(1)
//res79: Int = 53170
//tup.productElement(0) 
val tupLength = tup.productArity //this is how to find length of tuple (# of elements)
//tupLength: Int = 4

case class MatchData(id1:Int, id2: Int, scores: Array[Double], matched: Boolean) //create our own class to hold the data instead tuple
//defined class MatchData

def parse(line:String) = { //same method as before, instead of returning a tuple, it returns an object of MatchData
	val pieces = line.split(',')
	val id1 = pieces(0).toInt
	val id2 = pieces(1).toInt
	val scores = pieces.slice(2,11).map(toDouble)
	val matched = pieces(11).toBoolean
	MatchData(id1, id2, scores, matched)
}
//parse: (line: String)MatchData

val md = parse(firstLine) //saving the MatchData being returned from using parse on firstLine
//md: MatchData = MatchData(607,53170,[D@63c471df,true)
//array doesn't print, don't have access (but will if we step down scope, proved in commands below)

md.matched //accessing the matched value of specific MatchData object
//res82: Boolean = true

md.scores //within proper scope and now can actually access values
//res83: Array[Double] = Array(1.0, NaN, 1.0, NaN, 1.0, 1.0, 1.0, 1.0, 1.0)

val mds = head.filter(!isHeader(_)).map(parse(_)) //create an array of MD that is from the head RDD that has been filter to contain anything that isn't the header, that is first parsed and then added to the array mds
//mds: Array[MatchData] = Array(MatchData(607,53170,[D@3ef8d2af,true), MatchData(88569,88592,[D@5606dd8b,true), MatchData(21282,26255,[D@7870188,true), MatchData(20995,42541,[D@1984a303,true), MatchData(27989,34739,[D@61117736,true), MatchData(32442,69159,[D@11b141a,true), MatchData(24738,29196,[D@3eafcef0,true), MatchData(9904,89061,[D@28ffe9d,true), MatchData(29926,36578,[D@68cd913d,true))

val parsed = noheader.map(line => parse(line)) //basically same list due to it being from noheader, but parse each line of noheader
//parsed: org.apache.spark.rdd.RDD[MatchData] = MapPartitionsRDD[24] at map at p2.scala:31
