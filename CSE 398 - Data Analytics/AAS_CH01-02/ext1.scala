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
