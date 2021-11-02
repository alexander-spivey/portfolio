//--Analyzing Co-Ocurrence Networks with GraphX--//
/*
Data scientists nowadays come from all different types of fields. 
 1. The fields are often interested in relation between entities
 2. Explosion of digital data, more data then we know what to use for
	- Has forced these workers to pick up new skills to manage data
 - Realized methods to anaylze relationships applicable to other field
	- Network science is born!
		- Uses graph theory (study of pairwise relationships [edges]
		between set of entities [vertices])
			- Used everywhere, like CS for data structures
			- Big impact in business world as well. Major coprotations 
			abilities depend entirely on how they are able to anaylze
			- Facebook, friends, Linkedin, colleges, Amazon, prods, etc.
			- Companies like these drive creation of Distrubuted 
			Processing Framewroks like Mapreduce, and hire data scientists
				- First use of Mapreduce, to solve Pagerank issue
		- Over time, graph gotten much larger, need faster graph parrallel
		processing frameworks (Pregel - Google, Giraph - Yahoo, Graphlab)
			- "supported fault-tolerant, in-memory, iterative, and graph-
			centric processing, and were capable of performing" than Mapreduce
 - This chapter will introduce GraphX, let us use aforementioned frameworks ^^^
	- Since in spark, easier to integrate graphx for analyze network-centric data

	//-GraphX and GraphFrames--//
	Designed before DF in 1.3, to work with RDDs. Working in progress to port
	GraphX to new API in DF (GraphFrames). Rest of chapter uses GraphX since 
	GraphFrames isn't ready yet. But no worries, all ideas in GraphFrames present
	in GraphX.
*/

//--The MEDLINE Citation Index: A Network Analysis--//
/*
MEDLINE (Medical Literature Analysis and Retrieval System Online) database of
academic papers covering life science & medicine. Managed by US NLM. Citation index
trace back to 1879, available to med school in 1971, and public 1996. Updated 5 day
per week. Due to volumn of cits, updates, and research community developed an
extensive set of semantic tags, called MeSH (Medical Subject Headings). Tags provide
framework of relation. 2001: Pubgene released first text mining search engine to
match docs to terms.
Chapter is going to use Scale, Spark, and GraphX to analyze network of MeSH terms.
Idea is based off "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC4090190/" Use GraphX 
instead of R and C++.
To feel data out, we will
	1. Major topics and co-occurances (no GraphX)
	2. Look for connected components (follow one path of cites to another topic?)
	3. Degree distribution (how topics vary and connect similar)
	4. Clustering coeefient and Average Path Length [complicated]
After these steps, will allow us to understamd how similar citation graphs work
*/

//--Getting the Data--//
/*
//Grab sample of citation index
$ mkdir medline_data
$ cd medline_data
$ wget ftp://ftp.nlm.nih.gov/nlmdata/sample/medline/^.gz

//Uncompress data and examine
$ gunzip *.gz
$ ls -ltr
...
total 1814128
-rw-r--r--  1 jwills  staff  145188012 Dec  3  2015 medsamp2016h.xml
-rw-r--r--  1 jwills  staff  133663105 Dec  3  2015 medsamp2016g.xml
-rw-r--r--  1 jwills  staff  131298588 Dec  3  2015 medsamp2016f.xml
-rw-r--r--  1 jwills  staff  156910066 Dec  3  2015 medsamp2016e.xml
-rw-r--r--  1 jwills  staff  112711106 Dec  3  2015 medsamp2016d.xml
-rw-r--r--  1 jwills  staff  105189622 Dec  3  2015 medsamp2016c.xml
-rw-r--r--  1 jwills  staff   72705330 Dec  3  2015 medsamp2016b.xml
-rw-r--r--  1 jwills  staff   71147066 Dec  3  2015 medsamp2016a.xml

"Each entry in the sample files is a MedlineCitation record, which contains
information about the publication of an article in a biomedical journal, 
including the journal name, issue, publication date, the names of the authors, 
the abstract, and the set of MeSH keywords that are associated with the article"
Also each MESH keyword has attribute to tell if keyword major topic of doc.

//First citation record in medsamp2016a.xml
<MedlineCitation Owner="PIP" Status="MEDLINE">
<PMID Version="1">12255379</PMID>
<DateCreated>
  <Year>1980</Year>
  <Month>01</Month>
  <Day>03</Day>
</DateCreated>
...
<MeshHeadingList>
...
  <MeshHeading>
    <DescriptorName MajorTopicYN="N">Humans</DescriptorName>
  </MeshHeading>
  <MeshHeading>
    <DescriptorName MajorTopicYN="Y">Intelligence</DescriptorName>
  </MeshHeading>
  <MeshHeading>
    <DescriptorName MajorTopicYN="Y">Rorschach Test</DescriptorName>
  </MeshHeading>
...
</MeshHeadingList>
...
</MedlineCitation>
In last case Wikipedia, we wanted to extract the text. We actually here want to
extract the DescriptorName tags by parsing the XML. 

//To compile code into Jar
$ cd ch07-graph/
$ mvn package
$ cd target
$ spark-shell --jars /proj/cse398-498/course/aas/ch07-graph/target/ch07-graph-2.0.0-jar-with-dependencies.jar
*/
//Read XML MEDLINE data into shell
import edu.umd.cloud9.collection.XMLInputFormat
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.hadoop.io.{Text, LongWritable}
import org.apache.hadoop.conf.Configuration

def loadMedline(spark: SparkSession, path: String) = {
	import spark.implicits._
	@transient val conf = new Configuration()
	conf.set(XMLInputFormat.START_TAG_KEY, "<MedlineCitation ")
	conf.set(XMLInputFormat.END_TAG_KEY, "</MedlineCitation>")
	val sc = spark.sparkContext
	val in = sc.newAPIHadoopFile(path, classOf[XMLInputFormat],
	  classOf[LongWritable], classOf[Text], conf)
	in.map(line => line._2.toString).toDS()
}
val medlineRaw = loadMedline(spark, "/proj/cse398-498/course/AAS_CH7/medline_data")