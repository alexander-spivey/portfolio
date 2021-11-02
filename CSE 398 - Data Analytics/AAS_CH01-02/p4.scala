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



