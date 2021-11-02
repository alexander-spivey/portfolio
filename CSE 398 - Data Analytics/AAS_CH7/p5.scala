//--Understanding the Structure of Networks--//
/*
When exploring the table, number of stats needed to be computed immediatly. 
	Same principle applies to graph, just diff stats. Easy due to Graph and RDD API
*/


//-Connected Components-//
/*
Most basic feature, if graph connected (travel from one vertex to any another).
	If not, may be full of subgraphs. Connectedness is fundamental, graphx includes
	function to do so. Number of spark jobs will appear when called, later result
*/
val connectedComponentGraph = topicGraph.connectedComponents()
/*
Type of object returned is another Graph class, but vertex attribute is VertexID,
	unique ID for component each vertex belongs to. To get # of connected comp,
	convert VertexRDD to df
*/
val componentDF = connectedComponentGraph.vertices.toDF("vid", "cid")
val componentCounts = componentDF.groupBy("cid").count()
componentCounts.count()
componentCounts.orderBy(desc("count")).show()
/*
Largest component had more than 90% of vertices, second is 4. Big difference.
	To see names associatated with smaller components, need to join the VertexRDD
	with vertices from our original concept graph. Innerjoin in graphx, faster than
	normal join. Returns a new data type for the resulting VertexRDD. In this case
	we want a DF with topic name and component ID.
*/
val topicComponentDF = topicGraph.vertices.innerJoin(
	connectedComponentGraph.vertices) {
	(topicId, name, componentId) => (name, componentId.toLong)
}.toDF("topic", "cid")
topicComponentDF.where("cid = -2062883918534425492").show()
//topicComponentDF.where(col("cid")==="-8193948242717911820")
//Doesn't print? Data type mismatch?
/*
"A bit of Google searching reveals that “Campylobacter” is a genus of bacteria that
 is one of the most common causes of food poisoning, and “serotyping” is a technique
 used for classifying bacteria based on their cell surface “antigens,” which is a
 toxin that induces an immune response in the body."
*/

//Find others that were related but were cut off.
val campy = spark.sql("""
	SELECT *
	FROM topic_dist
	WHERE topic LIKE '%ampylobacter%'""")
campy.show()
/*
+--------------------+---+
|               topic|cnt|
+--------------------+---+
|Campylobacter jejuni|  3|
|Campylobacter Inf...|  2|
|       Campylobacter|  1|
|  Campylobacter coli|  1|
| Campylobacter fetus|  1|
+--------------------+---+
*/
/*
Fetus seems similar, but not reallt, occurs in cattle and shepp, not humans. 
	The broader pattern in our network is more citations we add, the more connected 
	things become. 
connectedComponents method is doing iterative comps on graph to identify component
	that each vertex belongs to, taking advantage of unique VertexID. During each
	phase of comp, each vertex broadcast smallest VertexID value it has seen
	to each neighboor. During first run, this will be vertex onw Id, but will be
	generall updated in each run. Each vertex keep track of smallest IDs changes,
	and when none change, the connected component comp is done, each vertex
	assigned to when they were the smallest value for that component. 
*/


//--Degree Distribution--//
/*
Connected graphs differ drastically. Can be all points to one, but no other
connections. If delete center vertix, whole model breaks apart into indy vertex.
May have case where all connected to only 2 others, forming a big loop. 
Nice insight is finding degree of each vertex, how many edges vertex belong to.
Sum of degree, when not looped, will be equal to 2x # of edges, bc each edge 
connected to 2 vertices.
*/

//Find degree of vertex, returned as a VertexRDD of integers, degree of each vertex
val degrees: VertexRDD[Int] = topicGraph.degrees.cache()
degrees.map(_._2).stats()
/*
(count: 13721, mean: 31.155892, stdev: 65.497591, max: 2596.000000, min: 1.000000)
*/
/*
Something pecuilar, less degree than edges. That means some are not connected. Must
be due to some entries only having one main topic and does not divilge off.
*/

//Find instances of single topic 
val sing = medline.filter(x => x.size == 1)
sing.count()
//44509
val singTopic = sing.flatMap(topic => topic).distinct()
singTopic.count()
//8243
//There is 8243 distinct topics

//Remove instances within topic pair
val topic2 = topicPairs.flatMap(_.getAs[Seq[String]](0))
singTopic.except(topic2).count()
//827
/*
Lil math = 14,548–827 is 13,721, the number of entries in the degrees RDD.
Analyzing other stats, we see most aren't connected to much, mean is small fraction.
Lets analyze the high degree vertixes by innerJoin degrees VertexRDD to vertices
in concept graph. This will filter out non co-occuring concepts.
*/
val namesAndDegrees = degrees.innerJoin(topicGraph.vertices) {
  (topicId, degree, name) => (name, degree.toInt)
}.values.toDF("topic", "degree")
namesAndDegrees.orderBy(desc("degree")).show()
/*
+-------------------+------+
|              topic|degree|
+-------------------+------+
|           Research|  2596|
|            Disease|  1746|
|          Neoplasms|  1202|
|              Blood|   914|
|       Pharmacology|   882|
|       Tuberculosis|   815|
|         Toxicology|   694|
|       Drug Therapy|   678|
|      Jurisprudence|   661|
|Biomedical Research|   633|
|         Physicians|   625|
|      Public Policy|   601|
|           Medicine|   590|
|         Metabolism|   578|
|      Social Change|   570|
|Wounds and Injuries|   570|
|              Brain|   569|
|          Hospitals|   557|
|              Urine|   551|
|          Economics|   548|
+-------------------+------+
only showing top 20 rows
*/
/*
Unsuprising, the most common are most vague. Next section will use GraphX api and stats
to filter out less interesting
*/

