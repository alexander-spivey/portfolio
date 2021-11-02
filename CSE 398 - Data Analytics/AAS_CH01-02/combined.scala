val rdd = sc.parallelize(Array(1,2,3,4),4) //creating a rdd by using parallelize
rdd.count() //returns the number of objects in an RDD
rdd.collect() //returns all objects in RDD as an array thats saved to the local mem

val rawblocks = sc.textFile("/proj/cse398-498/course/AAS_CH2/linkage") //after seperating the cvs files into its own seperate folder, we finally were able to open it and save it as an RDD
rawblocks.first //prints the first item/element

val head = rawblocks.take(10) //snatching the first 10 elements
head.foreach(println) //for each item in head, print it on a new line

def isHeader(line: String) = line.contains("id_1") //method to return boolean based on if it has string "id_1"
head.filter(isHeader).foreach(println) //within the head, for each item that is true for isheader, print on a new line
head.filterNot(isHeader).foreach(println) //within the head rdd, for each item that is not true for isheader, print out on a new line
//head.filter(!isHeader(_)).foreach(println) another version using anonymous functions
head.filter(x => !isHeader(x)).length //head filter anything that is a header, and count the remainder
//head.filter(!isHeader(_)).length another version using anonymous functions

val noheader = rawblocks.filter(!isHeader(_)) //create an rdd from rawblocks that filters out the header
noheader.take(10).foreach(println) //take the first 10 elements from noheader and print them
noheader.first //return the first element in noheader

spark //auto created spark session object
spark.sparkContext //spark session is a wrapper for SparkContext object

val prev = spark.read.csv("/proj/cse398-498/course/AAS_CH2/linkage")
prev.show() //prints out first 20 rows

val parsed = spark.read. //parse the data directly from csv by following these options
	option("header", "true"). //take first line as header and save to columns
	option("nullValue", "?"). //change '?' to nullValue
	option("inferSchema", "true"). //this means infer type for column
	csv("/proj/cse398-498/course/AAS_CH2/linkage") //this be where it stored
parsed.printSchema() //print the infer type of columns

//val d1 = spark.read.format("json").load("file.json") how to read in a json file
//val d2 = spark.read.json("file.json")

//d1.write.format("parquet").save("file.parquet") to write out data for parquet file
//d1.write.parquet("file.parquet") 
//these will actually error out cuz we can't save data frame to a file that already exists

//d2.write.mode(SaveMode.Ignore).parquet("file.parquet") this lets us overwrite the file
//we can change SaveMode to Overwrite, Append, or Ignore

parsed.count() //counts all the data
parsed.cache() //saves object (cuz rn it a DataFrame, but work for RDD) to memory
parsed.take(10) //access cached elements instead of recomputing them

parsed.rdd. //write an inlined scala function to extact the value of is__match
	map(_.getAs[Boolean]("is_match")). //calling countByValue on the resulting RDD[boolean]
	countByValue()
//only use countByValue() when they are few distinct values in data set (few true in this case)
//if lot of distinct, use reduceByKey to not return result to client
//if we need result of countByValue for future computation, need to use parralllelize to ship data from client to cluster

//here comes the new and improved method, this message is sponsored by DataFrameAPI
parsed.groupBy("is_match"). //group our data based on is_match
	count(). //count what was grouped
	orderBy($"count".desc). //order the results descending based on the count
	show() //show the result to user
	
/*HOW WOULD WE GET MINS, MAXES, MEANS, SUM and STDEV??? use ur friend .agg!*/
parsed.agg(avg($"cmp_sex"), stddev($"cmp_sex")).
	show()
	
parsed.createOrReplaceTempView("linkage") //telling spark sql execution engine to associate with parsed, since name of variable isn't available to Spark

spark.sql(""" 
	SELECT is_match, COUNT(*) cnt
	FROM linkage
	GROUP BY is_match
	ORDER BY cnt DESC
	""").show() ////using sql syntax here to code, thank you for being redable <3
//cool how we can us sql here to do our work, use sql for familiar and expressive queery
//use DataFrame API when need complex, multistage analysis in a dynamic, readable and testable way

val summary = parsed.describe() //get commmon analysis values from numerical columns
summary.schema //see structure types
summary.show() //show em off girly! yas, slay queen of analysis
summary. //select the following columns summary and show em
	select("summary", "cmp_fname_c1", "cmp_fname_c2").
	show()
	
/*To create a useful classifier, we need to rely on variables that are almost always present in the data—unless their missingness indicates something meaningful about whether the record matches.*/
val matches = parsed.where("is_match = true") //sql syntax to find where is_match == true
val matchSummary = matches.describe() //use describe to do analysis
matches.show()

val misses = parsed.filter($"is_match" === false) //dataframe syntax using column object & filter
val missSummary = misses.describe() //use describe to do analysis
misses.show()

/*To transpose our stats, we need to convert our summaries to long form, where each row has one metirc and column of variables aka (METRIC, FIELD, VALUE) = {[count, id_1, 5749133]}
We use the hella goated function flatMap, which is a wraper for RDD. flatMap is a generalization of the map and filter transforms what we have used so far*/
summary.printSchema() //prints Schema of summary in pretty form
val schema = summary.schema //save schema to an actual value cuz rn it is in string form
val longForm = summary.flatMap(row => { //look below for explanation
	val metric = row.getString(0)
	(1 until row.size).map(i =>
	{(metric,schema(i).name,row.getString(i).toDouble)}) //blessed implicit types
	})

val longDF = longForm.toDF("metric", "field", "value") //convert back to DataFrame with the 3 col.
longDF.show()

val wideDF = longDF. //creating a wide DF from our Long by
	groupBy("field"). //grouping with field
	pivot("metric", Seq("count", "mean", "stddev", "min", "max")). //pivoting around metric
	//at each of the fields mentioned above
	agg(first("value")) //aggregate with respect to value
wideDF.select("field", "count", "mean").show() //show the select few

val matchSummaryT = pivotSummary(matchSummary)
matchSummaryT.show()
val missSummaryT = pivotSummary(missSummary)
missSummaryT.show()

matchSummaryT.createOrReplaceTempView("match_desc") //relate matchSummaryT to match_desc
missSummaryT.createOrReplaceTempView("miss_desc")
spark.sql(""" 
	SELECT a.field, a.count + b.count total, a.mean - b.mean delta
	FROM match_desc a INNER JOIN miss_desc b ON a.field = b.field
	WHERE a.field not in ("id_1", "id_2")
	ORDER BY delta DESC, total DESC
	""").show()
/*Okay the first line says, we are choose objects a's field, add a count and b count to a column called total, and one more column called delta to represent the diff in a and b fields mean.
The next line states that match_desc is a, and we are going to join a with miss_desc, which is respectively b on fields where a and b have the field name
HOWEVER, do not need id_1 or id_2 to be added to the resulting table
Order the table by delta desc, with total descinding as well*/












