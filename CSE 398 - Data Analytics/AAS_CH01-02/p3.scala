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

//**FROM RDDS to Data Frames
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


