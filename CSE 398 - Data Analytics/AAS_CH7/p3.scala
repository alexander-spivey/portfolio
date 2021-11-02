//--Analyzing the MeSH Major Topics and Their Co-Occurrences--//
//Do some basic mathematical calculations to get feel of data
medline.count()
val topics = medline.flatMap(mesh => mesh).toDF("topic")
topics.createOrReplaceTempView("topics")
val topicDist = spark.sql("""
  SELECT topic, COUNT(*) cnt
  FROM topics
  GROUP BY topic
  ORDER BY cnt DESC""")
topicDist.count()
//res60: Long = 14548
topicDist.show()
/*
+--------------------+----+
|               topic| cnt|
+--------------------+----+
|            Research|1649|
|             Disease|1349|
|           Neoplasms|1123|
|        Tuberculosis|1066|
|       Public Policy| 816|
|       Jurisprudence| 796|
|          Demography| 763|
| Population Dynamics| 753|
|           Economics| 690|
|            Medicine| 682|
|Socioeconomic Fac...| 655|
|               Blood| 631|
|            Politics| 631|
|Emigration and Im...| 601|
|       Social Change| 577|
|          Physicians| 560|
|            Mutation| 542|
|   Abortion, Induced| 503|
|          Anesthesia| 483|
|       Public Health| 479|
+--------------------+----+
*/
/*
The more general the topic, the more often it appears/ More than 13,000 different
topics in dataset. Biggest topic is only (1,649/240,000 ~ 0.7%). Relatively
long tail for overall distribution
*/

//Test above theory
topicDist.createOrReplaceTempView("topic_dist")
spark.sql("""
  SELECT cnt, COUNT(*) dist
  FROM topic_dist
  GROUP BY cnt
  ORDER BY dist DESC
  LIMIT 10""").show()
/*
+---+----+
|cnt|dist|
+---+----+
|  1|3106|
|  2|1699|
|  3|1207|
|  4| 902|
|  5| 680|
|  6| 571|
|  7| 490|
|  8| 380|
|  9| 356|
| 10| 296|
+---+----+
*/

/*
Primary interest is co-occuring MeSH topics. Each entry in medline is a set
of strings that are namnes of topics. To get co-occurances, need to generate
all of the 2-element subsets of this list of strings. 
combinations to make generating these sublists extremely easy. combinations
returns an Iterator, meaning that the combinations need not all be held in memory

val list = List(1, 2, 3)
val combs = list.combinations(2)
combs.foreach(println)

-Results-
List(1, 2)
List(1, 3)
List(2, 3)

However, before we make a list of all combinations, we need to sort the data.
List(3, 2) == List(2, 3)

val combs = list.reverse.combinations(2)
combs.foreach(println)

-Results-
List(3, 2)
List(3, 1)
List(2, 1)
*/

//Sort then generate two-element sublist
val topicPairs = medline.flatMap(t => {
	t.sorted.combinations(2)
}).toDF("pairs") //create a sorted df of pairs
topicPairs.createOrReplaceTempView("topic_pairs")
val cooccurs =  spark.sql("""
	SELECT pairs, COUNT(*) cnt
	FROM topic_pairs
	GROUP BY pairs""")
cooccurs.cache()
cooccurs.count()
//res68: Long = 213745

cooccurs.createOrReplaceTempView("cooccurs")
spark.sql("""
	SELECT pairs, cnt
	FROM cooccurs
	ORDER BY cnt DESC
	LIMIT 10""").collect().foreach(println)
/*
[WrappedArray(Demography, Population Dynamics),288]
[WrappedArray(Government Regulation, Social Control, Formal),254]
[WrappedArray(Emigration and Immigration, Population Dynamics),230]
[WrappedArray(Acquired Immunodeficiency Syndrome, HIV Infections),220]
[WrappedArray(Antibiotics, Antitubercular, Dermatologic Agents),205]
[WrappedArray(Analgesia, Anesthesia),183]
[WrappedArray(Economics, Population Dynamics),181]
[WrappedArray(Analgesia, Anesthesia and Analgesia),179]
[WrappedArray(Anesthesia, Anesthesia and Analgesia),177]
[WrappedArray(Population Dynamics, Population Growth),174]
*/
/*
Nothing too interesting above. The top results are combinations of large topics
or are just words that are nearly the same like Analgesia and Anesthesia.
*/