//--Understanding Wikipedia with Latent Semantic Analysis--
/*
Most of work for data nerds is assembling data into a searchable format. Use SQL for tables. When not in table, or in nice order, hard for humans
to use. For humans it is not as much assembly but "indexing" or "coercion" when ugly. Standard search index let us find documents based off given
term. However, we may want to find words that relate to the word, but we don't give them the term. These kinda results are rare as most standard
search inderx fail to cpture latent structure. 
Latent semantic analysis(LSA) is a way to better understand a corpus of documents and the realtionip between the words in those docs. Attempts to 
break the corpus into relevant concepts. 3 main attributes:
 a level of affinity for each document in the corpus
 a level of affinity for each term in the corpus
 an importance score reflecting how useful the concept is in describing variance in the data set.
By selecting concepts with high affinity, LSA throw away irrelevant noise and merge "co-occuring strands."
This can provide scores of similarity between words, terms, and docs. Base score deper than occurance by "encapsulating the patterns of variance 
in the corpus." Perfect for finding query terms, grouping docs, finding related words. 

LSA does this through lower-dimension representation through techniqe: SVD - more proweful version of ALS factoraiztion (AAS_CH3)
1. document-term matrix: genrated counting word occurances
	- each doc is a column
	- each row is a word
	- each element represents word importance
2. SVD factorize matrix into 3 matrices
	- terms
	- docs
	- importance
3. remove columns and rows corresponding to least important concepts: low-rank approximation
	-multiple approximation iwht some matrix and produce matrix close ot original, but approx decreases for each concept removed

uses for svd: detect climate trends, face reg, and image compression
*/

//--The Document-Term Matrix--
/*
Before anaylsis, LSA need rawtext of corpus to become document-term matrix. to an extent, the position of term in text relate to importance.
Most common weighting system: TF-IDF - term frequency * inverse document frequency
	- Tells 2 things
		- More often the word is repeated in document, more important
		- More important to meet a rare word when looking through entire corpus
	- Issues
		- Frequency of words in corpus is distributed exponentially
			- common word appears 10x more than mildy common
			- common appear 100x more than rare
		- Due to distriibution, rare words would take all importance
		- Use log of inverse document frequency instead
	- Treats docs as "bag of words"
		- No attention to order, structure, etc
		- Problem with polysemy: "radiohead" and "band" vs "broke the band" vs "rubber band"
	- 10 million docs in corpus, vs a million terms in english
		- Generate the document term matrix as a row matrix (collection of sparse vectors) for each doc
	-Turn wiki-formatting to plain text
		- Plain text split into tokens into root term: lemmatization
		- use token to compute term and document
		- all steps ecampsulated in Spark's AssembleDocumentTermMatrix class
*/

//--Getting the Data--
/*
$ curl -s -L https://dumps.wikimedia.org/enwiki/latest/\
$ enwiki-latest-pages-articles-multistream.xml.bz2 \
$   | bzip2 -cd \
$   | hadoop fs -put - wikidump.xml
*/

//--Parsing and Preparing the Data--
/*
Need to work with some dependencies. We need to strip out formtatting and grab content. Start by creating a tuple dataset (title, doc content).
Cloud9 gives a bunch of APIs. 
*/
import edu.umd.cloud9.collection.XMLInputFormat
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io._

val path = "/proj/cse398-498/course/AAS_CH6/food_and_drink/wikipedia_category_food_and_drink.xml"
@transient val conf = new Configuration() //what is this? most of this has no notes, so must guess, configuring how we deparse?
conf.set(XMLInputFormat.START_TAG_KEY, "<page>")	//formtatting for start of each doc
conf.set(XMLInputFormat.END_TAG_KEY, "</page>")		//formatting for end of each doc
val kvs = spark.sparkContext.newAPIHadoopFile(path, classOf[XMLInputFormat],
	classOf[LongWritable], classOf[Text], conf)
val rawXmls = kvs.map(_._2.toString).toDS()

import edu.umd.cloud9.collection.wikipedia.language._
import edu.umd.cloud9.collection.wikipedia._

def wikiXmlToPlainText(pageXml: String): Option[(String, String)] = {
  // Wikipedia has updated their dumps slightly since Cloud9 was written,
  // so this hacky replacement is sometimes required to get parsing to work.
  val hackedPageXml = pageXml.replaceFirst(
    "<text xml:space=\"preserve\" bytes=\"\\d+\">",
    "<text xml:space=\"preserve\">")

  val page = new EnglishWikipediaPage()
  WikipediaPage.readPage(page, hackedPageXml)
  if (page.isEmpty || !page.isArticle || page.isRedirect ||
      page.getTitle.contains("(disambiguation)")) {
    None
  } else {
    Some((page.getTitle, page.getContent))
  }
  //if (page.isEmpty) None
  //else Some((page.getTitle, page.getContent))
}

val docTexts = rawXmls.filter(_ != null).flatMap(wikiXmlToPlainText)