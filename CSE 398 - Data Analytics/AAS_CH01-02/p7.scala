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
|     cmp_plz|5736289.0|  0.9563812499852176|	//very good value!
|cmp_lname_c2|   2464.0|  0.8064147192926264|	//good data for mean diff, but almost no values
|      cmp_by|5748337.0|  0.7762059675300512|	//good data
|      cmp_bd|5748337.0|   0.775442311783404|	//gopd
|cmp_lname_c1|5749132.0|  0.6838772482590526|	//good
|      cmp_bm|5748337.0|  0.5109496938298685|	//good
|cmp_fname_c1|5748125.0|  0.2854529057460786| //not good, too low diff
|cmp_fname_c2| 103698.0| 0.09104268062280008| //LMAO WHAT, no mean diff and so little amount
|     cmp_sex|5749132.0|0.032408185250332844| //the mean diff, where is it? not found here for sure
+------------+---------+--------------------+
*/
