TITLE: AAS_CH01 & 02 Homework Writeup 	AUTHOR: ALEXANDER SPIVEY	DATE:8/29/21
Chapter Code Writeup
	//**Getting Started**
		val rawblocks = sc.textFile("linkage/csv") //after seperating the cvs files into its own seperate folder, we finally were able to open it and save it as an RDD
		//rawblocks: org.apache.spark.rdd.RDD[String] = linkage/cvs MapPartitionsRDD[1] at textFile at p1.scala:24

		/*we can redeclare our variables as often in shell but not in script*/
		//val rawblocks = sc.textFile("linkage/csv") //finally getting it to access the proper files (create an rdd from txtfile)
		//rawblocks: org.apache.spark.rdd.RDD[String] = linkage/csv MapPartitionsRDD[22] at textFile at p1v3.scala:28


		val rdd = sc.parallelize(Array(1,2,3,4),4) //creating a rdd by using parallelize
		//rdd: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[20] at parallelize at p1v3.scala:28

		rdd.count() //returns the number of objects in an RDD
		//res3: Long = 4

		rdd.collect() //returns all objects in RDD as an array thats saved to the local mem
		//res4: Array[Int] = Array(1, 2, 3, 4)

		//rdd.saveAsTextFile("filename") saves the contents of rdd, each object is seperate txt file
		//look at folder called simpleRDD result

	//**Bringing Data from the Cluster to the Client
		rawblocks.first //prints the first item/element
		//res67: String = "id_1","id_2","cmp_fname_c1","cmp_fname_c2","cmp_lname_c1","cmp_lname_c2","cmp_sex","cmp_bd","cmp_bm","cmp_by","cmp_plz","is_match"

		val head = rawblocks.take(10) //snatching the first 10 elements
		//head: Array[String] = Array("id_1","id_2","cmp_fname_c1","cmp_fname_c2","cmp_lname_c1","cmp_lname_c2","cmp_sex","cmp_bd","cmp_bm","cmp_by","cmp_plz","is_match", 607,53170,1,?,1,?,1,1,1,1,1,TRUE, 88569,88592,1,?,1,?,1,1,1,1,1,TRUE, 21282,26255,1,?,1,?,1,1,1,1,1,TRUE, 20995,42541,1,?,1,?,1,1,1,1,1,TRUE, 27989,34739,1,?,1,?,1,1,1,1,1,TRUE, 32442,69159,1,?,1,?,1,1,1,1,1,TRUE, 24738,29196,1,1,1,?,1,1,1,1,1,TRUE, 9904,89061,1,?,1,?,1,1,1,1,1,TRUE, 29926,36578,1,?,1,?,1,1,1,1,1,TRUE)

		head.foreach(println) //for each item in head, print it on a new line
		/*
		"id_1","id_2","cmp_fname_c1","cmp_fname_c2","cmp_lname_c1","cmp_lname_c2","cmp_sex","cmp_bd","cmp_bm","cmp_by","cmp_plz","is_match"
		607,53170,1,?,1,?,1,1,1,1,1,TRUE
		88569,88592,1,?,1,?,1,1,1,1,1,TRUE
		21282,26255,1,?,1,?,1,1,1,1,1,TRUE
		20995,42541,1,?,1,?,1,1,1,1,1,TRUE
		27989,34739,1,?,1,?,1,1,1,1,1,TRUE
		32442,69159,1,?,1,?,1,1,1,1,1,TRUE
		24738,29196,1,1,1,?,1,1,1,1,1,TRUE
		9904,89061,1,?,1,?,1,1,1,1,1,TRUE
		29926,36578,1,?,1,?,1,1,1,1,1,TRUE
		*/

		def isHeader(line: String) = line.contains("id_1") //method to return boolean based on if it has string "id_1"
		//isHeader: (line: String)Boolean

		/*different method declaration style*/
		//def isHeader(line: String): Boolean = {line.contains("id_1")} fancy version where you dictate return type

		head.filter(isHeader).foreach(println) //within the head, for each item that is true for isheader, print on a new line
		//"id_1","id_2","cmp_fname_c1","cmp_fname_c2","cmp_lname_c1","cmp_lname_c2","cmp_sex","cmp_bd","cmp_bm","cmp_by","cmp_plz","is_match"

		head.filterNot(isHeader).foreach(println) //within the head rdd, for each item that is not true for isheader, print out on a new line
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
		*/
		//head.filter(!isHeader(_)).foreach(println) another version using anonymous functions

		head.filter(x => !isHeader(x)).length //head filter anything that is a header, and count the remainder
		//res71: Int = 9
		//head.filter(!isHeader(_)).length another version using anonymous functions

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

	//**From RDDS to Data Frames
		/* THIS IS WHERE I,  THE DUMBASS, WAS ON THE WRONG VERSION AND CONTINUED TO WORK WITH RDD'S INSTEAD OF MOVING ONTO THE GOD SEND THAT IS DATAFRAMES...
		DataFrames are pog because instead of using an RDD to go through our data and seperate it out, aka filtering/beautify, it just auto snacthes the strucutre of the existing data and parse it for immediate analysis*/

		spark //auto created spark session object
		//res8: org.apache.spark.sql.SparkSession = org.apache.spark.sql.SparkSession@355bffcf


		spark.sparkContext //spark session is a wrapper for SparkContext object
		//res9: org.apache.spark.SparkContext = org.apache.spark.SparkContext@5d42e865
		//this value is the same as the sc variable we have been using to create rdds

		val prev = spark.read.csv("/proj/cse398-498/course/AAS_CH2/linkage")
		//prev: org.apache.spark.sql.DataFrame = [_c0: string, _c1: string ... 10 more fields]

		prev.show() //prints out first 20 rows
		/*
		+-----+-----+------------+------------+------------+------------+-------+------+------+------+-------+--------+
		|  _c0|  _c1|         _c2|         _c3|         _c4|         _c5|    _c6|   _c7|   _c8|   _c9|   _c10|    _c11|
		+-----+-----+------------+------------+------------+------------+-------+------+------+------+-------+--------+
		| id_1| id_2|cmp_fname_c1|cmp_fname_c2|cmp_lname_c1|cmp_lname_c2|cmp_sex|cmp_bd|cmp_bm|cmp_by|cmp_plz|is_match|
		| 3148| 8326|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|14055|94934|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|33948|34740|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|  946|71870|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|64880|71676|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|25739|45991|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|62415|93584|           1|           ?|           1|           ?|      1|     1|     1|     1|      0|    TRUE|
		|27995|31399|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		| 4909|12238|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|15161|16743|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|31703|37310|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|30213|36558|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|56596|56630|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|16481|21174|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|32649|37094|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|34268|37260|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		|66117|69253|           1|           ?|           1|           ?|      1|     1|     1|     1|      0|    TRUE|
		| 2771|31982|           1|           ?|           1|           ?|      0|     1|     1|     1|      1|    TRUE|
		|23557|29673|           1|           ?|           1|           ?|      1|     1|     1|     1|      1|    TRUE|
		+-----+-----+------------+------------+------------+------------+-------+------+------+------+-------+--------+
		only showing top 20 rows
		*/

		val parsed = spark.read. //parse the data directly from csv by following these options
			option("header", "true"). //keep header
			option("nullValue", "?"). //change '?' to nullValue
			option("inferSchema", "true"). //this means infer type for column
			csv("/proj/cse398-498/course/AAS_CH2/linkage") //this be where it stored
		//parsed: org.apache.spark.sql.DataFrame = [id_1: string, id_2: string ... 10 more fields]

		parsed.printSchema() //print the infer type of columns
		/*
		root
		 |-- id_1: string (nullable = true)
		 |-- id_2: integer (nullable = true)
		 |-- cmp_fname_c1: double (nullable = true)
		 |-- cmp_fname_c2: double (nullable = true)
		 |-- cmp_lname_c1: double (nullable = true)
		 |-- cmp_lname_c2: double (nullable = true)
		 |-- cmp_sex: integer (nullable = true)
		 |-- cmp_bd: integer (nullable = true)
		 |-- cmp_bm: integer (nullable = true)
		 |-- cmp_by: integer (nullable = true)
		 |-- cmp_plz: integer (nullable = true)
		 |-- is_match: boolean (nullable = true)
		*/
		//this takes two passes. first pass to infer type, second pass to actually parse
		//if know schema b4, create instance of org.apache...StructType and pass it to Reader API via schema function (saves time for large dataset, wow really?? u mean pre filtering makes things faster??? oh my days!)
		//spark.read can do the following types: csv, json, parquet/orc, jdbc, libsvm, text

		//val d1 = spark.read.format("json").load("file.json") how to read in a json file
		//val d2 = spark.read.json("file.json")

		//d1.write.format("parquet").save("file.parquet") to write out data for parquet file
		//d1.write.parquet("file.parquet")
		//these will actually error out cuz we can't save data frame to a file that already exists

		//d2.write.mode(SaveMode.Ignore).parquet("file.parquet") this lets us overwrite the file
		//we can change SaveMode to Overwrite, Append, or Ignore

	//**Analyzing Data with the DataFrame API
		/* This is a short preface on why dataframe is better:
		Okay, so our basic RDDs, are dope as shit right? I mean for sure, they got the dope count and countByValue and stats to get (min, max, etc)
		BUT, DataFrame API is way faster (and significantly boosts python usage, cuz some methods werent available and they had to write their own methods, which take longer time than system intergrated commands) and has way more powerful commands.*/

		parsed.count() //counts all the data
		//res68: Long = 5749133  (THEY CALLED THIS A SMALL DATA SET, wtf????)
		//Up to this point, our system had to reparse every value when called to perform action

		parsed.cache() //saves object (cuz rn it a DataFrame, but work for RDD) to memory
		//res69: parsed.type = [id_1: string, id_2: string ... 10 more fields]


		//parsed.cache() //prevents reparsing the data every time
		//res85: parsed.type = MapPartitionsRDD[24] at map at p2.scala:31
		//this is shorthand for rdd.persist(StorageLevel.MEMORY)
		//has a MEMORY_SER storage level, serialize rdd contents into them (save 2-5x space)
		//for memory and memory_ser, if it full, it just recomputes instead of saving to space
		//for MEMORY_AND_DISK it will spill partitions into disk if not fit in mem

		parsed.take(10) //access cached elements instead of recomputing them
		/*
		es70: Array[org.apache.spark.sql.Row] = Array([3148,8326,1,null,1,null,1,1,1,1,1,true], [14055,94934,1,null,1,null,1,1,1,1,1,true], [33948,34740,1,null,1,null,1,1,1,1,1,true], [946,71870,1,null,1,null,1,1,1,1,1,true], [64880,71676,1,null,1,null,1,1,1,1,1,true], [25739,45991,1,null,1,null,1,1,1,1,1,true], [62415,93584,1,null,1,null,1,1,1,1,0,true], [27995,31399,1,null,1,null,1,1,1,1,1,true], [4909,12238,1,null,1,null,1,1,1,1,1,true], [15161,16743,1,null,1,null,1,1,1,1,1,true])
		*/

		//parsed.count() reran this after parsed.take(10) and got the following warning
		/*
		21/08/27 12:59:42 WARN CSVHeaderChecker: Number of column in CSV header is not equal to number of fields in the schema:
		 Header length: 9, schema size: 12
		CSV file: file:///home/aspiv/Classes/CSE398/AAS_CH01/linkage/csv/frequencies.csv
		res71: Long = 5749133
		*/
		//wait why is my count one bigger???

		parsed.rdd. //write an inlined scala function to extact the value of is__match
		    map(_.getAs[Boolean]("is_match")). //calling countByValue on the resulting RDD[boolean]
		    countByValue()
		//res72: scala.collection.Map[Boolean,Long] = Map(true -> 20931, false -> 5728202)
		//only use countByValue() when they are few distinct values in data set (few true in this case)
		//if lot of distinct, use reduceByKey to not return result to client
		//if we need result of countByValue for future computation, need to use parralllelize to ship data from client to cluster

		//here comes the new and improved method, this message is sponsored by DataFrameAPI
		parsed.
		    groupBy("is_match"). //group our data based on is_match
		    count(). //count what was grouped
		    orderBy($"count".desc). //order the results descending based on the count
		    show() //show the result to user
		/*
		+--------+-------+                                                              
		|is_match|  count|
		+--------+-------+
		|   false|5728201|
		|    true|  20931|
		|    null|      1|
		+--------+-------+
		*/
		//where the fuck the null coming from? KEEP AN EYE ON THIS NULL INDEX
		//two different ways of refer name of columns, use strings: groupBy("is_match") or use it as a Column object using: $"<colname>"
		//we use the special syntax, cuz object string does not have .desc, but object column has it!
		parsed.createOrReplaceTempView("linkage") //this worked? maybe?
		spark.sql("""
		    SELECT is_match, COUNT(*) cnt
		    FROM linkage
		    GROUP BY is_match
		    ORDER BY cnt DESC
		    """).show() ////using sql syntax here to code, thank you for being redable <3
		//cool how we can us sql here to do our work, use sql for familiar and expressive queery
		//use DataFrame API when need complex, multistage analysis in a dynamic, readable and testable way
		/*
		21/08/29 16:09:02 WARN CSVHeaderChecker: CSV header does not conform to the schema.
		 Header: ?
		 Schema: is_match
		Expected: is_match but found: ?
		CSV file: file:///home/aspiv/Classes/CSE398/AAS_CH01/linkage/csv/frequencies.csv
		+--------+-------+                                                              
		|is_match|    cnt|
		+--------+-------+
		|   false|5728201|
		|    true|  20931|
		|    null|      1|
		+--------+-------+
		*/
		 

		/*HOW WOULD WE GET MINS, MAXES, MEANS, SUM and STDEV??? use ur friend .agg!*/
		parsed.
		    agg(avg($"cmp_sex"), stddev($"cmp_sex")).
		    show()
		/*
		+------------------+--------------------+                                       
		|      avg(cmp_sex)|stddev_samp(cmp_sex)|
		+------------------+--------------------+
		|0.9550012294607436|  0.2073014119031251|
		+------------------+--------------------+
		*/

	//**Fast Summary Statistics**
		val summary = parsed.describe() //get commmon analysis values from numerical columns

		summary.schema //see structure types
		//res284: org.apache.spark.sql.types.StructType = StructType(StructField(summary,StringType,true), StructField(id_1,StringType,true), StructField(id_2,StringType,true), StructField(cmp_fname_c1,StringType,true), StructField(cmp_fname_c2,StringType,true), StructField(cmp_lname_c1,StringType,true), StructField(cmp_lname_c2,StringType,true), StructField(cmp_sex,StringType,true), StructField(cmp_bd,StringType,true), StructField(cmp_bm,StringType,true), StructField(cmp_by,StringType,true), StructField(cmp_plz,StringType,true))

		summary.show() //show em off girly! yas, slay queen of analysis
		/*
		+-------+--------------------+-------------------+--------------------+------------------+-------------------+------------------+------------------+-------------------+------------------+-------------------+-------------------+
		|summary|                id_1|               id_2|        cmp_fname_c1|      cmp_fname_c2|       cmp_lname_c1|      cmp_lname_c2|           cmp_sex|             cmp_bd|            cmp_bm|             cmp_by|            cmp_plz|
		+-------+--------------------+-------------------+--------------------+------------------+-------------------+------------------+------------------+-------------------+------------------+-------------------+-------------------+
		|  count|             5749133|            5749133|             5748126|            103699|            5749133|              2465|           5749133|            5748338|           5748338|            5748337|            5736289|
		|   mean|   33324.47979999771|  66587.42400114964|  0.7129023464248427|0.9000089989364238|0.31562785137797594|0.3182967444051654|0.9550012294607436|0.22446522967751065|0.4888552135282675| 0.2227485966810923|0.00552866147434343|
		| stddev|  23659.861398886704| 23620.501884381938| 0.38875843950829053|0.2713306768152374|0.33423361373861143|0.3685373395187368|0.2073014119031251|0.41722969573281293|0.4998758217099882|0.41609096298317344|0.07414914925420066|
		|    min|0.000235404896421846|0.00147710487444609|                   0|                 0|                  0|                 0|                 0|                  0|                 0|                  0|                  0|
		|    max|                9999|              99999|2.68694413843136e-05|                 1|                  1|                 1|                 1|                  1|                 1|                  1|                  1|
		+-------+--------------------+-------------------+--------------------+------------------+-------------------+------------------+------------------+-------------------+------------------+-------------------+-------------------+
		*/

		summary. //select the following columns summary and show em
		    select("summary", "cmp_fname_c1", "cmp_fname_c2").
		    show()
		/*
		+-------+--------------------+------------------+
		|summary|        cmp_fname_c1|      cmp_fname_c2|
		+-------+--------------------+------------------+
		|  count|             5748126|            103699|
		|   mean|  0.7129023464248427|0.9000089989364238|
		| stddev| 0.38875843950829053|0.2713306768152374|
		|    min|                   0|                 0|
		|    max|2.68694413843136e-05|                 1|
		+-------+--------------------+------------------+
		*/

		/*To create a useful classifier, we need to rely on variables that are almost always present in the data—unless their missingness indicates something meaningful about whether the record matches.*/

		val matches = parsed.where("is_match = true") //sql syntax to find where is_match == true
		val matchSummary = matches.describe() //use describe to do analysis
		matches.show()
		/*
		+-----+-----+------------+------------+------------+------------+-------+------+------+------+-------+--------+
		| id_1| id_2|cmp_fname_c1|cmp_fname_c2|cmp_lname_c1|cmp_lname_c2|cmp_sex|cmp_bd|cmp_bm|cmp_by|cmp_plz|is_match|
		+-----+-----+------------+------------+------------+------------+-------+------+------+------+-------+--------+
		| 3148| 8326|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|14055|94934|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|33948|34740|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|  946|71870|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|64880|71676|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|25739|45991|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|62415|93584|           1|        null|           1|        null|      1|     1|     1|     1|      0|    true|
		|27995|31399|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		| 4909|12238|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|15161|16743|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|31703|37310|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|30213|36558|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|56596|56630|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|16481|21174|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|32649|37094|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|34268|37260|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|66117|69253|           1|        null|           1|        null|      1|     1|     1|     1|      0|    true|
		| 2771|31982|           1|        null|           1|        null|      0|     1|     1|     1|      1|    true|
		|23557|29673|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		|37156|39557|           1|        null|           1|        null|      1|     1|     1|     1|      1|    true|
		+-----+-----+------------+------------+------------+------------+-------+------+------+------+-------+--------+
		only showing top 20 rows
		*/

		val misses = parsed.filter($"is_match" === false) //dataframe syntax using column object & filter
		val missSummary = misses.describe() //use describe to do analysis
		misses.show()
		/*
		+-----+-----+-----------------+-----------------+------------------+------------+-------+------+------+------+-------+--------+
		| id_1| id_2|     cmp_fname_c1|     cmp_fname_c2|      cmp_lname_c1|cmp_lname_c2|cmp_sex|cmp_bd|cmp_bm|cmp_by|cmp_plz|is_match|
		+-----+-----+-----------------+-----------------+------------------+------------+-------+------+------+------+-------+--------+
		|18206|58536|                1|             null|                 0|        null|      1|     0|     1|     0|      0|   false|
		| 6373|47769|            0.875|             null| 0.166666666666667|        null|      1|     1|     0|     0|      0|   false|
		|14095|79961|                1|             null|                 0|        null|      1|     0|     1|     0|      0|   false|
		|16247|63203|                1|             null| 0.142857142857143|        null|      1|     0|     0|     1|      0|   false|
		|15525|97448|                1|             null| 0.222222222222222|        null|      1|     0|     1|     1|      0|   false|
		| 3004|68135|                0|             null|                 1|        null|      1|     1|     0|     0|      0|   false|
		| 4331| 7693|            0.875|             null| 0.166666666666667|        null|      1|     1|     0|     0|      0|   false|
		|  263|14076|                1|             null|0.0909090909090909|        null|      1|     0|     0|     1|      0|   false|
		|61638|82485|                1|             null|                 0|        null|      1|     1|     0|     0|      0|   false|
		| 4295|89513|                1|             null| 0.111111111111111|        null|      1|     0|     1|     0|      0|   false|
		| 8810|77731|                1|             null| 0.222222222222222|        null|      1|     0|     1|     0|      0|   false|
		|55597|87589|                0|0.285714285714286|                 0|        null|      1|     1|     1|     1|      0|   false|
		| 9296|27119|                1|             null| 0.166666666666667|        null|      1|     0|     1|     0|      0|   false|
		|48870|78963|                1|             null|              0.25|        null|      1|     0|     1|     0|      0|   false|
		|40561|53170|                1|             null|             0.125|        null|      1|     0|     1|     0|      0|   false|
		|11741|88816|                1|             null|                 0|        null|      1|     0|     1|     0|      0|   false|
		|36252|48314|                1|             null|                 0|        null|      1|     0|     1|     1|      0|   false|
		|67307|98994|0.142857142857143|             null|             0.125|        null|      1|     1|     1|     1|      0|   false|
		| 7230|15748|0.166666666666667|             null|               0.6|        null|      1|     0|     0|     0|      0|   false|
		|51327|84236|              0.4|             null|                 0|        null|      1|     0|     1|     0|      0|   false|
		+-----+-----+-----------------+-----------------+------------------+------------+-------+------+------+------+-------+--------+
		only showing top 20 rows
		*/

	//**Pivoting and Reshaping DataFrames
		/*To transpose our stats, we need to convert our summaries to long form, where each row has one metirc and column of variables aka (METRIC, FIELD, VALUE) = {[count, id_1, 5749133]}
		We use the hella goated function flatMap, which is a wraper for RDD. flatMap is a generalization of the map and filter transforms what we have used so far*/

		summary.printSchema() //prints Schema of summary in pretty form
		/*
		root
		 |-- summary: string (nullable = true)
		 |-- id_1: string (nullable = true)
		 |-- id_2: string (nullable = true)
		 |-- cmp_fname_c1: string (nullable = true)
		 |-- cmp_fname_c2: string (nullable = true)
		 |-- cmp_lname_c1: string (nullable = true)
		 |-- cmp_lname_c2: string (nullable = true)
		 |-- cmp_sex: string (nullable = true)
		 |-- cmp_bd: string (nullable = true)
		 |-- cmp_bm: string (nullable = true)
		 |-- cmp_by: string (nullable = true)
		 |-- cmp_plz: string (nullable = true)
		*/

		val schema = summary.schema //save schema to an actual value cuz rn it is in string form
		val longForm = summary.flatMap(row => { //look below for explanation
		    val metric = row.getString(0)
		    (1 until row.size).map(i =>
		    {(metric,schema(i).name,row.getString(i).toDouble)}) //blessed implicit types
		    })
		//longform: org.apache.spark.sql.Dataset[(String, String, Double)] = [_1: string, _2: string ... 1 more field]
		//Dataset is just DataFrames alias, just version 2.0 of Spark, convert between the two types

		/*
		val longform = summary.flatMap(row => //for each row in summary, we are
		    {
			val metric = row.getString(0) //getting the name of the metric for that row
			(1 until row.size).map(i => //for other columnsin the row, from pos 1 till end, we
			{                //are generating a sequence of tuples
			    //val valStat = row.getString(i)
			    (metric, schema(i).name. row.getString(i).toDouble)
			})//(name of the metric, name of column, value of the stats which are now doubles
		    })
		/*
		schema: org.apache.spark.sql.types.StructType = StructType(StructField(summary,StringType,true), StructField(id_1,StringType,true), StructField(id_2,StringType,true), StructField(cmp_fname_c1,StringType,true), StructField(cmp_fname_c2,StringType,true), StructField(cmp_lname_c1,StringType,true), StructField(cmp_lname_c2,StringType,true), StructField(cmp_sex,StringType,true), StructField(cmp_bd,StringType,true), StructField(cmp_bm,StringType,true), StructField(cmp_by,StringType,true), StructField(cmp_plz,StringType,true))
		p6.scala:34: error: value row is not a member of String
			(metric, schema(i).name. row.getString(i).toDouble)
		*/
		*/

		val longDF = longForm.toDF("metric", "field", "value") //convert back to DataFrame with the 3 col.
		longDF.show()
		/*
		+------+------------+-------------------+
		|metric|       field|              value|
		+------+------------+-------------------+
		| count|        id_1|          5749133.0|
		| count|        id_2|          5749133.0|
		| count|cmp_fname_c1|          5748126.0|
		| count|cmp_fname_c2|           103699.0|
		| count|cmp_lname_c1|          5749133.0|
		| count|cmp_lname_c2|             2465.0|
		| count|     cmp_sex|          5749133.0|
		| count|      cmp_bd|          5748338.0|
		| count|      cmp_bm|          5748338.0|
		| count|      cmp_by|          5748337.0|
		| count|     cmp_plz|          5736289.0|
		|  mean|        id_1|  33324.47979999771|
		|  mean|        id_2|  66587.42400114964|
		|  mean|cmp_fname_c1| 0.7129023464248427|
		|  mean|cmp_fname_c2| 0.9000089989364238|
		|  mean|cmp_lname_c1|0.31562785137797594|
		|  mean|cmp_lname_c2| 0.3182967444051654|
		|  mean|     cmp_sex| 0.9550012294607436|
		|  mean|      cmp_bd|0.22446522967751065|
		|  mean|      cmp_bm| 0.4888552135282675|
		+------+------------+-------------------+
		only showing top 20 rows
		*/

		val wideDF = longDF. //creating a wide DF from our Long by
		    groupBy("field"). //grouping with field
		    pivot("metric", Seq("count", "mean", "stddev", "min", "max")). //pivoting around metric
		    //at each of the fields mentioned above
		    agg(first("value")) //aggregate with respect to value
		wideDF.select("field", "count", "mean").show() //show the select few
		/*
		+------------+---------+-------------------+                                    
		|       field|    count|               mean|
		+------------+---------+-------------------+
		|        id_2|5749133.0|  66587.42400114964|
		|     cmp_plz|5736289.0|0.00552866147434343|
		|cmp_lname_c1|5749133.0|0.31562785137797594|
		|cmp_lname_c2|   2465.0| 0.3182967444051654|
		|     cmp_sex|5749133.0| 0.9550012294607436|
		|      cmp_bm|5748338.0| 0.4888552135282675|
		|cmp_fname_c2| 103699.0| 0.9000089989364238|
		|cmp_fname_c1|5748126.0| 0.7129023464248427|
		|        id_1|5749133.0|  33324.47979999771|
		|      cmp_bd|5748338.0|0.22446522967751065|
		|      cmp_by|5748337.0| 0.2227485966810923|
		+------------+---------+-------------------+
		*/

		//wideDF.show()
		/*
		+------------+---------+-------------------+-------------------+-------------------+-------------------+
		|       field|    count|               mean|             stddev|                min|                max|
		+------------+---------+-------------------+-------------------+-------------------+-------------------+
		|        id_2|5749133.0|  66587.42400114964| 23620.501884381938|0.00147710487444609|            99999.0|
		|     cmp_plz|5736289.0|0.00552866147434343|0.07414914925420066|                0.0|                1.0|
		|cmp_lname_c1|5749133.0|0.31562785137797594|0.33423361373861143|                0.0|                1.0|
		|cmp_lname_c2|   2465.0| 0.3182967444051654| 0.3685373395187368|                0.0|                1.0|
		|     cmp_sex|5749133.0| 0.9550012294607436| 0.2073014119031251|                0.0|                1.0|
		|      cmp_bm|5748338.0| 0.4888552135282675| 0.4998758217099882|                0.0|                1.0|
		|cmp_fname_c2| 103699.0| 0.9000089989364238| 0.2713306768152374|                0.0|                1.0|
		|cmp_fname_c1|5748126.0| 0.7129023464248427|0.38875843950829053|                0.0|2.68694413843136E-5|
		|        id_1|5749133.0|  33324.47979999771| 23659.861398886704|2.35404896421846E-4|             9999.0|
		|      cmp_bd|5748338.0|0.22446522967751065|0.41722969573281293|                0.0|                1.0|
		|      cmp_by|5748337.0| 0.2227485966810923|0.41609096298317344|                0.0|                1.0|
		+------------+---------+-------------------+-------------------+-------------------+-------------------+
		*/

		//:load Pivot.scala before moving on or else crash
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

		val matchSummaryT = pivotSummary(matchSummary)
		/*
		+------------+-------+------------------+--------------------+-------+-------+  
		|       field|  count|              mean|              stddev|    min|    max|
		+------------+-------+------------------+--------------------+-------+-------+
		|        id_2|20931.0| 51259.95939037791|    24345.7334537752|10010.0|99996.0|
		|     cmp_plz|20902.0|0.9584250310975027| 0.19962063345931927|    0.0|    1.0|
		|cmp_lname_c1|20931.0|0.9970152595958817| 0.04311880753394512|    0.0|    1.0|
		|cmp_lname_c2|  475.0| 0.969370167843852| 0.15345280740388917|    0.0|    1.0|
		|     cmp_sex|20931.0| 0.987291577086618| 0.11201570591216432|    0.0|    1.0|
		|      cmp_bm|20925.0|0.9979450418160095| 0.04528612745217063|    0.0|    1.0|
		|cmp_fname_c2| 1333.0|0.9898900320318174| 0.08251973727615237|    0.0|    1.0|
		|cmp_fname_c1|20922.0|0.9973163859635038|0.036506675848336785|    0.0|    1.0|
		|        id_1|20931.0| 34575.72117911232|  21950.312851969127|10001.0|99946.0|
		|      cmp_bd|20925.0|0.9970848267622461| 0.05391487659807977|    0.0|    1.0|
		|      cmp_by|20925.0|0.9961290322580645|0.062098048567310576|    0.0|    1.0|
		+------------+-------+------------------+--------------------+-------+-------+
		*/
		val missSummaryT = pivotSummary(missSummary)
		/*
		+------------+---------+--------------------+-------------------+-------+-------+
		|       field|    count|                mean|             stddev|    min|    max|
		+------------+---------+--------------------+-------------------+-------+-------+
		|        id_2|5728201.0|   66643.44259218557| 23599.551728241124|10000.0|99999.0|
		|     cmp_plz|5715387.0|0.002043781112285135|0.04516197989362504|    0.0|    1.0|
		|cmp_lname_c1|5728201.0| 0.31313801133682906| 0.3322812130572706|    0.0|    1.0|
		|cmp_lname_c2|   1989.0| 0.16295544855122554|0.19302366635287027|    0.0|    1.0|
		|     cmp_sex|5728201.0|  0.9548833918362851|0.20755988859217656|    0.0|    1.0|
		|      cmp_bm|5727412.0|   0.486995347986141|   0.49983089404939|    0.0|    1.0|
		|cmp_fname_c2| 102365.0|  0.8988473514090173| 0.2727209029401023|    0.0|    1.0|
		|cmp_fname_c1|5727203.0|  0.7118634802174252|0.38908060096985714|    0.0|    1.0|
		|        id_1|5728201.0|  33319.913548075565| 23665.760130330764|    1.0| 9999.0|
		|      cmp_bd|5727412.0|  0.2216425149788421| 0.4153518275558737|    0.0|    1.0|
		|      cmp_by|5727412.0|  0.2199230647280133|0.41419432671429335|    0.0|    1.0|
		+------------+---------+--------------------+-------------------+-------+-------+
		*/

	//**Joining DataFrames and Selecting Features**
		/*Let’s create temporary views for the matchSummaryT and missSummaryT DataFrames, join them on the field column, and compute some simple summary statistics on the resulting rows:*/
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
		/*
		+------------+---------+--------------------+                                   
		|       field|    total|               delta|
		+------------+---------+--------------------+
		|     cmp_plz|5736289.0|  0.9563812499852176|    //very good value!
		|cmp_lname_c2|   2464.0|  0.8064147192926264|    //good data for mean diff, but almost no values
		|      cmp_by|5748337.0|  0.7762059675300512|    //good data
		|      cmp_bd|5748337.0|   0.775442311783404|    //gopd
		|cmp_lname_c1|5749132.0|  0.6838772482590526|    //good
		|      cmp_bm|5748337.0|  0.5109496938298685|    //good
		|cmp_fname_c1|5748125.0|  0.2854529057460786| //not good, too low diff
		|cmp_fname_c2| 103698.0| 0.09104268062280008| //LMAO WHAT, no mean diff and so little amount
		|     cmp_sex|5749132.0|0.032408185250332844| //the mean diff, where is it? not found here for sure
		+------------+---------+--------------------+
		*/

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

	//**Model Evaluation**
		//I do apologize for having a copy and paste of the textbook here, but since I couldn't get past scoring, I don't really understand fully what is going on here. Due to this, I decided it important for me to be able to reread this portion when needed.

		def crossTabs(scored: DataFrame, t: Double): DataFrame = { //input DataFrame, Double, ouput DataFrame
		  scored.//using the referenced dataframe
		    selectExpr(s"score >= $t as above", "is_match").
		    //determine the value of the field named above based on the value of the t argument using Scala’s string interpolation syntax, which allows us to substitute variables by name if we preface the string literal with the letter s
		    groupBy("above"). //once the above field is defined
		    pivot("is_match", Seq("true", "false")). //we pivot around is_match
		    count() //count it up!
		} 

		crossTabs(scored, 4.0).show()
		/*
		crossTabs: (scored: org.apache.spark.sql.DataFrame, t: Double)org.apache.spark.sql.DataFrame

		scala> crossTabs(scored, 4.0).show()
		[Stage 64:==================================>                      (6 + 4) / 10]21/08/30 00:22:01 ERROR Executor: Exception in task 9.0 in stage 64.0 (TID 2147)
		java.lang.NullPointerException: Null value appeared in non-nullable field:
		- field (class: "scala.Int", name: "id_2")
		- root class: "$line102.$read.$iw.$iw.MatchData"
		If the schema is inferred from a Scala tuple/case class, or a Java bean, please try to use scala.Option[_] or other nullable types (e.g. java.lang.Integer instead of int/scala.Int).
			at org.apache.spark.sql.catalyst.expressions.GeneratedClass$GeneratedIterator.agg_doAggregateWithKeys$(Unknown Source)
			at org.apache.spark.sql.catalyst.expressions.GeneratedClass$GeneratedIterator.processNext(Unknown Source)
			at org.apache.spark.sql.execution.BufferedRowIterator.hasNext(BufferedRowIterator.java:43)
			at org.apache.spark.sql.execution.WholeStageCodegenExec$$anonfun$8$$anon$1.hasNext(WholeStageCodegenExec.scala:395)
			at scala.collection.Iterator$$anon$11.hasNext(Iterator.scala:408)
			at org.apache.spark.shuffle.sort.BypassMergeSortShuffleWriter.write(BypassMergeSortShuffleWriter.java:125)
			at org.apache.spark.scheduler.ShuffleMapTask.runTask(ShuffleMapTask.scala:96)
			at org.apache.spark.scheduler.ShuffleMapTask.runTask(ShuffleMapTask.scala:53)
			at org.apache.spark.scheduler.Task.run(Task.scala:108)
			at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:335)
			at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
			at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
			at java.lang.Thread.run(Thread.java:748)
		*/
		/* WHAT SHOULD HAVE HAPPENED
		Applying a high threshold value of 4.0, meaning that the average of the five features is 0.8, we can filter out almost all of the nonmatches while keeping over 90% of the matches:
		...
		+-----+-----+-------+
		|above| true|  false|
		+-----+-----+-------+
		| true|20871|    637|
		|false|   60|5727564|
		+-----+-----+-------+
		*/


		crossTabs(scored, 2.0).show()
		//the actual result still errored, soo lets look at the fake one below!
		/*Applying the lower threshold of 2.0, we can ensure that we capture all of the known matching records, but at a substantial cost in terms of false positive (top-right cell):
		...
		+-----+-----+-------+
		|above| true|  false|
		+-----+-----+-------+
		| true|20931| 596414|
		|false| null|5131787|
		+-----+-----+-------+
		*/
		//see if you can find a way to use some of the other values from MatchData (both missing and not) to come up with a scoring function that successfully identifies every true match at the cost of less than 100 false positives.
		//^^^ hahaha no, I can't even get past crossTabs

My Extension Writeup
	//**This section I would like to call it hmmm, since my inferSchema gave everything String instead, what if I made a parser using edition 1 code, would that allow me to score my values? (answer: no, lol)
		case class MatchData(id1:Int, id2: Int, scores: Array[Double], matched: Boolean) //create our own class to hold the data instead tuple

		def isHeader(line: String) = line.contains("id_1")

		def toDouble(s: String) = { //our own modified version of toDouble
		     if("?".equals(s)) Double.NaN //if the char is '?' then return Double.NaN
		     else
		     s.toDouble //use the string.toDouble function if normal
		}

		//won't convert using the new defintion, so tinker around this
		//figured out this method is kinda pointless, so work with accessing the array instead
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
	//**Wrong Edition Code
		//**Defining Script as an Object
			/*Okay, so this was my failed attempt at writing a script. Didn't realize I didn't need to define it as an object */
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

		//**Aggregations**
			val grouped = mds.groupBy(md => md.matched) //we are aggregating over our mds array and using groupby to create a Scale Map[bool, arr] (look like weird dictionary), key is based on matched field
			/*
			grouped: scala.collection.immutable.Map[Boolean,Array[MatchData]] = Map(true -> Array(MatchData(607,53170,[D@187d8ae9,true), MatchData(88569,88592,[D@13d75a86,true), MatchData(21282,26255,[D@3af3c661,true), MatchData(20995,42541,[D@127f90eb,true), MatchData(27989,34739,[D@66a17a8f,true), MatchData(32442,69159,[D@4f8b0fb7,true), MatchData(24738,29196,[D@6b6f18e9,true), MatchData(9904,89061,[D@57935a7,true), MatchData(29926,36578,[D@68e14285,true)))
			*/

			grouped.mapValues(x => x.size).foreach(println) //print out how many were matched in a tuple
			//(true,9)
			//when performing aggregations, information split across, has to transfer (ser, deser), take time, so instead we need to filter more before agg.

		//**Creating Histograms**
			val matchCounts = parsed.map(md => md.matched).countByValue() //creates a simple histogram that counts matched values through parsed
			//nice function exists, return value to client
			//okay as of 8/26 10:30pm, when I try to use count or countbyvalue, it dies because it cant find special partitions of the rdd im trying to access... don't know if this just me or everyone
			//Block rdd_6_5 could not be removed as it was not found on disk or in memory
	//**Actual Extension Code
		/*
		Okay well the reason I choose to use cmp_by as my focus was because cmp_plz was the best value from the difference between missed and matched mean. I went to go look at what cmp_plz was in the documentation: cmp_plz: agreement of postal code, which kinda does make sense now that I am typing it out, but I was confused what they meant by postal code. So I decided to go with the next data set, that actually had enough values to work with (cough, cough, cmp_lname_c2).
		*/
		val parsed = spark.read. //parse the data directly from csv by following these options
			option("header", "true"). //take first line as header and save to columns
			option("nullValue", "?"). //change '?' to nullValue
			option("inferSchema", "true"). //this means infer type for column
			csv("/proj/cse398-498/course/AAS_CH2/linkage") //this be where it stored

		parsed.printSchema() //print the infer type of columns
		/*
		root
		 |-- id_1: string (nullable = true)
		 |-- id_2: integer (nullable = true)
		 |-- cmp_fname_c1: double (nullable = true)
		 |-- cmp_fname_c2: double (nullable = true)
		 |-- cmp_lname_c1: double (nullable = true)
		 |-- cmp_lname_c2: double (nullable = true)
		 |-- cmp_sex: integer (nullable = true)
		 |-- cmp_bd: integer (nullable = true)
		 |-- cmp_bm: integer (nullable = true)
		 |-- cmp_by: integer (nullable = true)
		 |-- cmp_plz: integer (nullable = true)
		 |-- is_match: boolean (nullable = true)
		*/
			
		parsed.
			groupBy("cmp_by"). //group our data based on cmp_by
			count(). //count what was grouped
			orderBy($"count".desc). //order the results descending based on the count
			show() //show the result to user
		/*
		CSV file: file:///home/aspiv/Classes/CSE398/AAS_CH01/linkage/csv/frequencies.csv
		+------+-------+                                                                
		|cmp_by|  count|
		+------+-------+
		|     0|4467903| //a lot not agreed on years
		|     1|1280434| //still not 0.27%ish is not bad for agreed
		|  null|    796| //boo, no entry, smh lazy data
		+------+-------+
		*/

		parsed. //needed info on what i was working with
			agg(avg($"cmp_by"), stddev($"cmp_by"), count($"cmp_by")). //do the following aggregations on cmp_by
			show()
		/*
		CSV file: file:///home/aspiv/Classes/CSE398/AAS_CH01/linkage/csv/frequencies.csv
		+------------------+-------------------+-------------+                          
		|       avg(cmp_by)|stddev_samp(cmp_by)|count(cmp_by)|
		+------------------+-------------------+-------------+
		|0.2227485966810923|0.41609096298317344|      5748337|
		+------------------+-------------------+-------------+
		*/

		val describe = parsed.describe() //creating a summary because I want to prove that method above actually succesfully calculated the section!
		describe.
			select("summary", "cmp_by").
			show()
		/*
		describe: org.apache.spark.sql.DataFrame = [summary: string, id_1: string ... 10 more fields]
		+-------+-------------------+
		|summary|             cmp_by|
		+-------+-------------------+
		|  count|            5748337|
		|   mean| 0.2227485966810923| //its the exact same nums!
		| stddev|0.41609096298317344|
		|    min|                  0|
		|    max|                  1|
		+-------+-------------------+
		*/

		val agreedbY = parsed.where("cmp_by = 1") //grab everything where the year was agreed = 1
		val agreedbYSummary = agreedbY.describe() //describe it for me!
		agreedbYSummary.show() //the below data represents all the entries aggs for when the year is agreed
		/*									most agree				most neutral									lol	most disagree							
		+-------+-----------------+------------------+------------------+-------------------+------------------+-------------------+-------------------+-------------------+-------------------+-------+-------------------+
		|summary|             id_1|              id_2|      cmp_fname_c1|       cmp_fname_c2|      cmp_lname_c1|       cmp_lname_c2|            cmp_sex|             cmp_bd|             cmp_bm| cmp_by|            cmp_plz|
		+-------+-----------------+------------------+------------------+-------------------+------------------+-------------------+-------------------+-------------------+-------------------+-------+-------------------+
		|  count|          1280434|           1280434|           1280119|              24816|           1280434|               1041|            1280434|            1280434|            1280434|1280434|            1277157|
		|   mean|33308.76097557547| 66360.17225331411|0.7372919356434241| 0.9302952908006101|0.1788615644656431| 0.5207967424826216| 0.8620959768328551|0.28099925494012185|0.31972752988439856|    1.0|0.01748179746107957|
		| stddev|23614.16864493855|23725.752837340064|0.3829100151312604|0.22895931839885514|0.1992904742543946|0.43479032503157633|0.34479935674263135|0.44948729843295465|0.46637110371920204|    0.0|0.13105799352723504|
		|    min|                1|             10000|                 0|                  0|                 0|                  0|                  0|                  0|                  0|      1|                  0|
		|    max|             9999|             99999|                 1|                  1|                 1|                  1|                  1|                  1|                  1|      1|                  1|
		+-------+-----------------+------------------+------------------+-------------------+------------------+-------------------+-------------------+-------------------+-------------------+-------+-------------------+
		*/
		agreedbYSummary.
			select("summary", "cmp_by").
			show()
		/*
		+-------+-------+
		|summary| cmp_by|
		+-------+-------+
		|  count|1280434|
		|   mean|    1.0|
		| stddev|    0.0|
		|    min|      1|
		|    max|      1|
		+-------+-------+
		*/

		parsed.rdd. //write an inlined scala function to extact the values of cmp_by
			map(_.getAs[Integer]("cmp_by")). //calling countByValue on the resulting RDD[Integer]
			countByValue()
		//res17: scala.collection.Map[Integer,Long] = Map(null -> 796, 0 -> 4467903, 1 -> 1280434) okay once again, the math checks out that there is only 1280434 agreed entries

		val disagreedbY = parsed.where("cmp_by = 0") //grab everything where the year was agreed = 0
		val disagreedbYSummary = disagreedbY.describe() //describe it for me!
		disagreedbYSummary.show() //the below data represents all the entries aggs for when the year is disagreed
		/*																	most agree				most neutral		most disagree
		+-------+------------------+------------------+------------------+------------------+-------------------+-------------------+------------------+-------------------+------------------+-------+--------------------+
		|summary|              id_1|              id_2|      cmp_fname_c1|      cmp_fname_c2|       cmp_lname_c1|       cmp_lname_c2|           cmp_sex|             cmp_bd|            cmp_bm| cmp_by|             cmp_plz|
		+-------+------------------+------------------+------------------+------------------+-------------------+-------------------+------------------+-------------------+------------------+-------+--------------------+
		|  count|           4467903|           4467903|           4467211|             78880|            4467903|               1423|           4467903|            4467903|           4467903|4467903|             4458337|
		|   mean|33327.110131083864| 66651.57652057352|0.7060074835868007|0.8905129048745861|0.35472863416035516|0.17035826280380054|0.9816186698771213|0.20826347393844494|0.5373247807752317|    0.0|0.002103699204434299|
		| stddev|23672.843843174862|23591.187350741508|0.3901011474044887| 0.282642467106024| 0.3541397426695683|0.21238511262874368|0.1343259500776211| 0.4060662954087105|0.4986049702750139|    0.0| 0.04581783631901901|
		|    min|                 1|             10000|                 0|                 0|                  0|                  0|                 0|                  0|                 0|      0|                   0|
		|    max|              9999|             99999|                 1|                 1|                  1|                  1|                 1|                  1|                 1|      0|                   1|
		+-------+------------------+------------------+------------------+------------------+-------------------+-------------------+------------------+-------------------+------------------+-------+--------------------+
		*/
		disagreedbYSummary.
			select("summary", "cmp_by"). 
			show()
		/*
		+-------+-------+
		|summary| cmp_by|
		+-------+-------+
		|  count|4467903|
		|   mean|    0.0|
		| stddev|    0.0|
		|    min|      0|
		|    max|      0|
		+-------+-------+
		*/

		val disagreedbYSummaryT = pivotSummary(disagreedbYSummary) //beautify the atrocity from above
		disagreedbYSummaryT.show()
		/*
		+------------+---------+--------------------+-------------------+-------+-------+
		|       field|    count|                mean|             stddev|    min|    max|
		+------------+---------+--------------------+-------------------+-------+-------+
		|        id_2|4467903.0|   66651.57652057352| 23591.187350741508|10000.0|99999.0|
		|     cmp_plz|4458337.0|0.002103699204434299|0.04581783631901901|    0.0|    1.0| most disagree
		|cmp_lname_c1|4467903.0| 0.35472863416035516| 0.3541397426695683|    0.0|    1.0|
		|cmp_lname_c2|   1423.0| 0.17035826280380054|0.21238511262874368|    0.0|    1.0|
		|     cmp_sex|4467903.0|  0.9816186698771213| 0.1343259500776211|    0.0|    1.0| most agree
		|      cmp_bm|4467903.0|  0.5373247807752317| 0.4986049702750139|    0.0|    1.0| most neutral/divided
		|cmp_fname_c2|  78880.0|  0.8905129048745861|  0.282642467106024|    0.0|    1.0|
		|cmp_fname_c1|4467211.0|  0.7060074835868007| 0.3901011474044887|    0.0|    1.0|
		|        id_1|4467903.0|  33327.110131083864| 23672.843843174862|    1.0| 9999.0|
		|      cmp_bd|4467903.0| 0.20826347393844494| 0.4060662954087105|    0.0|    1.0|
		|      cmp_by|4467903.0|                 0.0|                0.0|    0.0|    0.0|
		+------------+---------+--------------------+-------------------+-------+-------+
		*/

		val agreedbYSummaryT = pivotSummary(agreedbYSummary) //beautify the atrocity from above
		agreedbYSummaryT.show()
		/*
		+------------+---------+-------------------+-------------------+-------+-------+
		|       field|    count|               mean|             stddev|    min|    max|
		+------------+---------+-------------------+-------------------+-------+-------+
		|        id_2|1280434.0|  66360.17225331411| 23725.752837340064|10000.0|99999.0|
		|     cmp_plz|1277157.0|0.01748179746107957|0.13105799352723504|    0.0|    1.0| most disagree
		|cmp_lname_c1|1280434.0| 0.1788615644656431| 0.1992904742543946|    0.0|    1.0|
		|cmp_lname_c2|   1041.0| 0.5207967424826216|0.43479032503157633|    0.0|    1.0| most neutral/divided
		|     cmp_sex|1280434.0| 0.8620959768328551|0.34479935674263135|    0.0|    1.0|
		|      cmp_bm|1280434.0|0.31972752988439856|0.46637110371920204|    0.0|    1.0| 
		|cmp_fname_c2|  24816.0| 0.9302952908006101|0.22895931839885514|    0.0|    1.0| most agree
		|cmp_fname_c1|1280119.0| 0.7372919356434241| 0.3829100151312604|    0.0|    1.0|
		|        id_1|1280434.0|  33308.76097557547|  23614.16864493855|    1.0| 9999.0|
		|      cmp_bd|1280434.0|0.28099925494012185|0.44948729843295465|    0.0|    1.0|
		|      cmp_by|1280434.0|                1.0|                0.0|    1.0|    1.0|
		+------------+---------+-------------------+-------------------+-------+-------+
		*/

		disagreedbYSummaryT.createOrReplaceTempView("agree_year") //relate disagreedbYSummaryT to disagree_year
		agreedbYSummaryT.createOrReplaceTempView("agree_year") //relate agreedbYSummaryT to agree_year
		spark.sql(""" 
			SELECT a.field, a.count + b.count total, a.mean - b.mean delta
			FROM agree_year a INNER JOIN disagree_year b ON a.field = b.field
			WHERE a.field not in ("id_1", "id_2")
			ORDER BY delta DESC, total DESC
			""").show()
		/*
		+------------+---------+--------------------+                                   
		|       field|    total|               delta| 
		+------------+---------+--------------------+
		|      cmp_by|5748337.0|                 1.0|
		|cmp_lname_c2|   2464.0| 0.35043847967882114| //good data for mean diff, but almost no vals
		|      cmp_bd|5748337.0| 0.07273578100167691| 
		|cmp_fname_c2| 103696.0| 0.03978238592602401|	
		|cmp_fname_c1|5747330.0|0.031284452056623446|					just noticed there is no is_match here?
		|     cmp_plz|5735494.0| 0.01537809825664527|	least diff in terms of avg	whats the ratio for match for years agreed vs not
		|     cmp_sex|5748337.0|-0.11952269304426621|
		|cmp_lname_c1|5748337.0|-0.17586706969471205|
		|      cmp_bm|5748337.0|-0.21759725089083315| //technical winner off most difference! (that means a.mean < b.mean)
		+------------+---------+--------------------+
		*/


		/*Okay this coming section is when I noticed, hey!, when we use describe it cuts of is_match (guessing cuz you cant really do the mean with Boolean, need to be either int) so I decided to find the number of cases for the following*/
		/*Match Found or Not*/
		val agreedbYMatch = parsed.where("cmp_by = 1").where("is_match = true").agg(count($"is_match")) //where year is agreed and matched is true
		agreedbYMatch.show()
		/*
		+---------------+
		|count(is_match)|
		+---------------+
		|          20844| //woah for sure can say, if year is matched, significantly higher percentage that its matched 
		+---------------+
		*/

		val agreedbYMiss = parsed.where("cmp_by = 1").where("is_match = false").agg(count($"is_match")) //where year is agreed and matched is false
		agreedbYMiss.show()
		/*
		+---------------+
		|count(is_match)|
		+---------------+
		|        1259590|
		+---------------+
		*/

		val disagreedbYMatch = parsed.where("cmp_by = 0").where("is_match = true").agg(count($"is_match")) //where year is disagreed and matched is true
		disagreedbYMatch.show()
		/*
		+---------------+
		|count(is_match)|
		+---------------+
		|             81| //almost no entries where its matched and the year is not agreed uponed, which makes a lot of sense
		+---------------+
		*/

		val disagreedbYMiss = parsed.where("cmp_by = 0").where("is_match = false").agg(count($"is_match")) //where year is disagreed and matched is false
		disagreedbYMiss.show()
		/*
		+---------------+
		|count(is_match)|
		+---------------+
		|        4467822|
		+---------------+
		*/
		/*Math portion: 
		agreedYMatched% = 20844/1259590 = 0.01654824188 = 1.654824188%
		disagreedYMatched% = 81/4467822 = 0.00001812963 = 0.001812963%
		relativechange = (agreedYMatched - disagreedYMatched)/disagreedYMatched = 91,177.3282191% MORE LIKELY TO GET MATCHED WHEN YEAR IS ALSO AGREED ON vs NOT
		*/


		/*Okay this coming section is focused on postal due to the fact, it was the most disagreed entry in all data (period) so I wanted to see the potential*/
		/*Postal Agreed vs Not*/
		val agreedbYMatchPLZ = parsed.where("cmp_by = 1").where("cmp_plz = 1").agg(count($"cmp_plz")) //where year is agreed and postal code is agreed
		agreedbYMatchPLZ.show()
		/*
		+--------------+
		|count(cmp_plz)|
		+--------------+
		|         22327| //wow not much better
		+--------------+
		*/

		val agreedbYMissPLZ = parsed.where("cmp_by = 1").where("cmp_plz = 0").agg(count($"cmp_plz")) //where year is agreed and postal code is disagreed
		agreedbYMissPLZ.show()
		/*
		+--------------+
		|count(cmp_plz)|
		+--------------+
		|       1254830| //about same amount that is not matched 
		+--------------+
		*/

		val disagreedbYMatchPLZ = parsed.where("cmp_by = 0").where("cmp_plz = 1").agg(count($"cmp_plz")) //where year is disagreed and postal code is agreed
		disagreedbYMatchPLZ.show()
		/*
		+--------------+
		|count(cmp_plz)|
		+--------------+
		|          9379| //it has a bit more likelyness to be agreed when year is disagreed
		+--------------+
		*/

		val disagreedbYMissPLZ = parsed.where("cmp_by = 0").where("cmp_plz = 0").agg(count($"cmp_plz")) //where year is disagreed and postal code is agreed
		disagreedbYMissPLZ.show()
		/*
		+--------------+
		|count(cmp_plz)|
		+--------------+
		|       4448958|
		+--------------+
		*/
		/*Math portion:
		agreedYMatchPLZ% = 22327/1254830 = 0.01779284843 = 1.779284843%
		disagreedYMatchPLZ% = 9379/4448958 = 0.00210813408 = 0.210813408%
		relativechange = (agreedYMatched - disagreedYMatched)/disagreedYMatched = 744.009335023% more likely for postal code to be agreed when year is also agreed
		*/

