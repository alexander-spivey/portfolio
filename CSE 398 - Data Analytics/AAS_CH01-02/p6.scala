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
		{				//are generating a sequence of tuples
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

//:load Pivot.scala
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
