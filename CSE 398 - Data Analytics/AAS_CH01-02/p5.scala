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
