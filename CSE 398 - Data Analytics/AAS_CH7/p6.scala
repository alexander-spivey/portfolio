//--Filtering Out Noisy Edges--//
/*
As of right now, edges are weighted on count of how often a pair is. This doesnt mean
there is a semantic relationship present. Need new edge weighting scheme that
checks interest a pair concept is, given "overall prevalence of those concepts in 
the data. We will use Pearson’s chi-squared test to calculate whether the occurrence 
of a particular concept is independent from the occurrence of another concept"

With a pair of concepts A and B, create a 2x2 contigency table. The entries YY, YN,
& NN is raw count presence of A and B.  The entries YA and NA are the row sums for
concept A, YB and NB are the column sums for concept B, and the value T is the 
total number of documents. 
		yes b	no b 	a total
yes a	yy		yn		ya
no a	ny		nn		na
b total	yb		nb		t

x^2 = (T(|YY * YN -YN * NY| - T/2)^2)/(YA * NA * YB * NB)
-T/2 is Yates continuity correlation
Higher the value, the more pair concept is interesting. We will calc this per pair
*/


//-Processing EdgeTriplets-//
//Easiest to calculate, is T, total number of docs under consideration
val T = medline.count()
//240000

//Calculate how many documents feature each concept
val topicDistRdd = topicDist.map{ //Done earlier, but redoing as hashed version
  case Row(topic: String, cnt: Long) => (hashId(topic), cnt)
}.rdd

//New graph
val topicDistGraph = Graph(topicDistRdd, topicGraph.edges)

/*
To do calc, need to combine vertices - how often each concept appears in a document
& edges - how often each pair of concepts occurs in the same document. GraphX 
support this operation with EdgeTriplet[VD,ED]
*/
def chiSq(YY: Long, YB: Long, YA: Long, T: Long): Double = {
  val NB = T - YB
  val NA = T - YA
  val YN = YA - YY
  val NY = YB - YY
  val NN = T - NY - YN - YY
  val inner = math.abs(YY * NN - YN * NY) - T / 2.0
  T * math.pow(inner, 2) / (YA * NA * YB * NB)
}
val chiSquaredGraph = topicDistGraph.mapTriplets(triplet => {
  chiSq(triplet.attr, triplet.srcAttr, triplet.dstAttr, T)
})

chiSquaredGraph.edges.map(x => x.attr).stats()
/*
(count: 213745, mean: 877.956648, stdev: 5094.935171, max: 198668.408387, min: 0.000000)
*/
/*
Range here is huge, STD proves this. Use aggresive thresholding/filtering. 
"The 99.999th percentile of the chi-squared distribution with one degree
 of freedom is approximately 19.5" - Use this to thresholding/filtering
*/

//Threholding the 99.99th percentile
val interesting = chiSquaredGraph.subgraph(
  triplet => triplet.attr > 19.5)
interesting.edges.count
/*
res85: Long = 140575	//removed about a 1/3 edges
*/


//-Analyzing the Filtered Graph-//
val interestingComponentGraph = interesting.connectedComponents()
val icDF = interestingComponentGraph.vertices.toDF("vid", "cid")
val icCountDF = icDF.groupBy("cid").count()
icCountDF.count()
//878

icCountDF.orderBy(desc("count")).show()
/*
+--------------------+-----+
|                 cid|count|
+--------------------+-----+
|-9218306090261648869|13610|
|-8193948242717911820|    5|
|-2062883918534425492|    4|
|-7016546051037489808|    3|
| 2742772755763603550|    3|
|-8679136035911620397|    3|
|-7685954109876710390|    3|
| -784187332742198415|    3|
| 1765411469112156596|    3|
|-9211944049288765106|    2|
|-4895960388347845016|    2|
|-2317423407077322989|    2|
|-3299226677350014771|    2|
|-6541817437503372447|    2|
|-5362458719777034637|    2|
|-3191983795676547449|    2|
| -697775734067750523|    2|
|-3467839743215210439|    2|
|-4717785562675251817|    2|
|-1046815223728304871|    2|
+--------------------+-----+
only showing top 20 rows
*/
//Removing a third of data didn't didnt impact connectdness. Everything remainds same

val interestingDegrees = interesting.degrees.cache()
interestingDegrees.map(_._2).stats()
//(count: 13721, mean: 20.490489, stdev: 29.864223, max: 863.000000, min: 1.000000)
//The mean has fallen down a bit

interestingDegrees.innerJoin(topicGraph.vertices) {
  (topicId, degree, name) => (name, degree)
}.values.toDF("topic", "degree").orderBy(desc("degree")).show()
/*
+--------------------+------+
|               topic|degree|
+--------------------+------+
|            Research|   863|
|             Disease|   637|
|        Pharmacology|   509|
|           Neoplasms|   453|
|          Toxicology|   381|
|          Metabolism|   321|
|        Drug Therapy|   304|
|               Blood|   302|
|       Public Policy|   279|
|Evaluation Studie...|   277|
|       Social Change|   277|
|       Jurisprudence|   253|
|       Contraception|   245|
|Socioeconomic Fac...|   245|
|Family Planning S...|   244|
|              Plants|   239|
|           Economics|   237|
|Diagnosis, Differ...|   234|
|               Brain|   229|
|           Diagnosis|   227|
+--------------------+------+
only showing top 20 rows
*/
//Look it effects of chi square ended up working. Removed generics and left related!