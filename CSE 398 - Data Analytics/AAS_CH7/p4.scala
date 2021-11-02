//--Constructing a Co-Occurrence Network with GraphX--//
/*
The standard tools we use aren't enough to give insight. Raw counts is not 
enough, and the most co-occuring pairs are not of interest. 
Need to analyze the co-occurence network: topics as vertices, and existence
of a cite that has both pairs as an edge btwn the 2. Then compute network-centric
stats, that help us understand the structure and investigate outlier vertices.
Use co-occurence network to find relationships btwn entities worth investigating.
https://bit.ly/3AdxDUj -> Figure 7-1

Similar to how MLLib provides algo for creating ML models in Spark, GraphX is
used to anaylze networks. GraphX is built ontop of Spark, capable of multi-
computer processing. 
 1. move from writing data-parallel ETL routines against RDDs to executing
	graph-parallel algorithms against a graph
 2. to analyzing and summarizing the output of the graph computation in a 
	data-parallel fashion again
GraphX built on RDD, specifically VertexRDD[VD] (each type per instance is long
	for every vertex, while VD can be any other [vertex attribute], built on
	RDD[(VertexId, VD)]), and EdgeRDD[ED] (Edge is a case class that contains two
	VertexId values and an edge attribute of type ED, built on RDD[Edge[ED]]).
Both aforementioned have internal indices within each partition designed to
	facilitate fast joins and attribute updates. Given both  a VertexRDD and an
	associated EdgeRDD, create Graph class, do a bunch of methods efficently
First requirement in creating graph is to have Long val used as identifier for
	each vertex in graph. All topics rn is string. Need way to create unique
	64-bit for each topic as an ID, need to be done in a distributed fasion,
	done quickly for large dataset.
Use hashCode method, generate a 32-bit for any Scala object. In our case,
	13000 vertices, no problem. What if tens of million? Hash code collision prob
	occur. Due to this, we copy Google’s Guava Library to create a unique 64-bit
	identifier for each topic using MD5 hashing algorithm
*/
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

def hashId(str: String): Long = {
	val bytes = MessageDigest.getInstance("MD5").
		digest(str.getBytes(StandardCharsets.UTF_8))
		(bytes(0) & 0xFFL) |
		((bytes(1) & 0xFFL) << 8) |
		((bytes(2) & 0xFFL) << 16) |
		((bytes(3) & 0xFFL) << 24) |
		((bytes(4) & 0xFFL) << 32) |
		((bytes(5) & 0xFFL) << 40) |
		((bytes(6) & 0xFFL) << 48) |
		((bytes(7) & 0xFFL) << 56)
}
/*
Apply hashing func to data to generate DF for basis to set of vertices in our
	co-occurence graph. Also double check uniquness
*/
import org.apache.spark.sql.Row
val vertices = topics.map{ case Row(topic: String) =>
	(hashId(topic), topic) }.toDF("hash", "topic")
val uniqueHashes = vertices.agg(countDistinct("hash")).take(1)


/*
Generate edges for graph from unique ID (hashing function to map each topic
	name to its corresponding vertex ID). Good habit: left side VertexId (src)
	is less than right side VertexId (dst). Most algto in GraphX don't assume
	relationship btwn src and dst, only few do. Good idea to implement patten
	early, dont forget
*/
import org.apache.spark.graphx._

val edges = cooccurs.map{ case Row(topics: Seq[_], cnt: Long) =>
	val ids = topics.map(_.toString).map(hashId).sorted
	Edge(ids(0), ids(1), cnt)
}

//Have both vertices and edges, create Graph and cache
val vertexRDD = vertices.rdd.map{
	case Row(hash: Long, topic: String) => (hash, topic)
}
val topicGraph = Graph(vertexRDD, edges.rdd)
topicGraph.cache()
/*
The vertexRDD and edges used were regulear RDD. Didn't deduplicate. Graph API
	does it for us. Converting them to VertexRDD and EdgeRDD, vertex all unique
*/
vertexRDD.count()
//Long = 280464
topicGraph.vertices.count()
//res54: Long = 14548
/*
Will not deduplicate EdgeRDD, GraphX create multigraphs, multiple edges with
	diff values btwn same vertices. Usefull aplplcaition where verices are rich
	objects (people - friends, collegues, etc) that have many diff relationships.
	Allows us to treat edges as either direct or undirect (depending context)
*/