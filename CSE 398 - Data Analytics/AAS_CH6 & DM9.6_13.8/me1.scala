//spark-shell --master local[*] --driver-memory 8g --jars /proj/cse398-498/course/aas/ch06-lsa/target/ch06-lsa-2.0.0-jar-with-dependencies.jar
import edu.umd.cloud9.collection.XMLInputFormat
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io._

val path = "/proj/cse398-498/aas423/AAS_CH6/machine-learning_artificial-intelligence.xml"	//words are concatenated with '-'
//val path = "/proj/cse398-498/aas423/AAS_CH6/ML_AI.xml"
@transient val conf = new Configuration() //what is this? most of this has no notes, so must guess, configuring how we deparse?
conf.set(XMLInputFormat.START_TAG_KEY, "<page>")	//formtatting for start of each doc
conf.set(XMLInputFormat.END_TAG_KEY, "</page>")		//formatting for end of each doc
val kvs = spark.sparkContext.newAPIHadoopFile(path, classOf[XMLInputFormat],
	classOf[LongWritable], classOf[Text], conf)
val rawXmls = kvs.map(_._2.toString).toDS()

import edu.umd.cloud9.collection.wikipedia.language._
import edu.umd.cloud9.collection.wikipedia._

def wikiXmlToPlainText(pageXml: String): Option[(String, String)] = {
  // updated parser for updated wiki dumps 2021
  val hackedPageXml = pageXml.replaceFirst(
    "<text bytes=\"\\d+\" xml:space=\"preserve\">",
	"<text xml:space=\"preserve\">")

  val page = new EnglishWikipediaPage()
  WikipediaPage.readPage(page, hackedPageXml)
  if (page.isEmpty || !page.isArticle || page.isRedirect ||
      page.getTitle.contains("(disambiguation)")) {
    None
  } else {
    Some((page.getTitle, page.getContent))
  }
}

val docTexts = rawXmls.filter(_ != null).flatMap(wikiXmlToPlainText)



import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer
import edu.stanford.nlp.pipeline._
import edu.stanford.nlp.ling.CoreAnnotations._
import java.util.Properties
import org.apache.spark.sql.Dataset

def createNLPPipeline(): StanfordCoreNLP = {
	val props = new Properties()
	// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
	//props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref")
	props.put("annotators", "tokenize, ssplit, pos, lemma")
	new StanfordCoreNLP(props)
}

def isOnlyLetters(str: String): Boolean = {	//returns true if every char in string is letters
	str.forall(c => Character.isLetter(c))
}

def plainTextToLemmas(text: String, stopWords: Set[String], pipeline: StanfordCoreNLP): Seq[String] = {
	val doc = new Annotation(text)	//the doc we are on 
	pipeline.annotate(doc)
	
	val lemmas = new ArrayBuffer[String]()
	val sentences = doc.get(classOf[SentencesAnnotation]) //convert doc to array of sentences
	for (sentence <- sentences.asScala;
       token <- sentence.get(classOf[TokensAnnotation]).asScala) {
			val lemma = token.get(classOf[LemmaAnnotation])
			if (lemma.length > 2 && !stopWords.contains(lemma) && isOnlyLetters(lemma)) { //Specify min req on lemmas to weed out dead words
			//must have length greather than 2, cant be in stop words, must be only letters
				lemmas += lemma.toLowerCase
			}
	}
	lemmas
}

val stopWords = scala.io.Source.fromFile("/proj/cse398-498/aas423/AAS_CH6/stopWords.txt").getLines().toSet
val bStopWords = spark.sparkContext.broadcast(stopWords) //broadcast to save memory and not be able to overwrite it

val terms: Dataset[(String, Seq[String])] = docTexts.mapPartitions { iter =>
	val pipeline = createNLPPipeline()
	iter.map { case(title, contents) =>
		(title, plainTextToLemmas(contents, bStopWords.value, pipeline))
	}
} //Use mapPartitions so that we only initialize the NLP pipeline object once per partition instead of once per document.
val termsDF = terms.toDF("title", "terms")		//add column titles
val filtered = termsDF.where(size($"terms") > 2) //machine learning and artificial intelligence are 2 words each
filtered.show()



/*
CountVectorizer is an Estimator we can use to calculate TF. Scans all data, builds a map, is a Transformer. Then is used to generate
frequency vector for each doc
*/
import org.apache.spark.ml.feature.CountVectorizer
val numTerms = 2000
val countVectorizer = new CountVectorizer().
	setInputCol("terms").
	setOutputCol("termFreqs").
	setVocabSize(numTerms)	//corpus has shit ton of words, putting a limit will give us only most freq words
val vocabModel = countVectorizer.fit(filtered)	//have to fit the transformer with a dataset then can transform the data
val docTermFreqs = vocabModel.transform(filtered)
docTermFreqs.cache()	//cached since used twice at least, calculate  IDFS and final document-term matrix

//counts the number of documents in which each term in the corpus appears and usescounts to compute the IDF scaling factor per term.
import org.apache.spark.ml.feature.IDF
val idf = new IDF().
	setInputCol("termFreqs").
	setOutputCol("tfidfVec")
val idfModel = idf.fit(docTermFreqs)
val docTermMatrix = idfModel.transform(docTermFreqs).select("title", "tfidfVec")

//as we move from DF to vector matries, we cant search by string. important to save mapping of pos in matrix to terms and docs. pos in
//term vectors are == to cols in document-term matrix
val termIds: Array[String] =  vocabModel.vocabulary

/*
row to document title harder. use zipWithUniqueId(). "it will assign the same unique IDs to the transformed rows as long as the 
transformations don’t change the number of rows or their partitioning
*/
val docIds = docTermFreqs.rdd.map(_.getString(0)).
	zipWithUniqueId().
	map(_.swap).
	collect().toMap



import org.apache.spark.mllib.linalg.{Vectors, Vector => MLLibVector}
import org.apache.spark.ml.linalg.{Vector => MLVector}

val vecRdd = docTermMatrix.select("tfidfVec").rdd.map { row =>
  Vectors.fromML(row.getAs[MLVector]("tfidfVec"))
}
//to calc SVD, wrarp RDD in rowmatrix
import org.apache.spark.mllib.linalg.distributed.RowMatrix

vecRdd.cache()	//cache to save O(nk) storage (n = storage, k = passes)
val mat = new RowMatrix(vecRdd)
val k = 1000
val svd = mat.computeSVD(k, computeU=true)




import org.apache.spark.mllib.linalg.{Matrix,
  SingularValueDecomposition}
import org.apache.spark.mllib.linalg.distributed.RowMatrix

def topTermsInTopConcepts(
    svd: SingularValueDecomposition[RowMatrix, Matrix],
    numConcepts: Int,
    numTerms: Int, termIds: Array[String]) : Seq[Seq[(String, Double)]] = {
		val v = svd.V
		val topTerms = new ArrayBuffer[Seq[(String, Double)]]()
		val arr = v.toArray
		for (i <- 0 until numConcepts) {
			val offs = i * v.numRows
			val termWeights = arr.slice(offs, offs + v.numRows).zipWithIndex
			val sorted = termWeights.sortBy(-_._1)
			topTerms += sorted.take(numTerms).map {	//take as many as requested
			  case (score, id) => (termIds(id), score)	//remap integer->term using termIDS
			}
		}
		topTerms
	}

//using almost same process we can get top docs, but U is in distrubtued space
def topDocsInTopConcepts(
	svd: SingularValueDecomposition[RowMatrix, Matrix],
	numConcepts: Int, numDocs: Int, docIds: Map[Long, String])
	: Seq[Seq[(String, Double)]] = {
		val u  = svd.U
		val topDocs = new ArrayBuffer[Seq[(String, Double)]]()
		for (i <- 0 until numConcepts) {
			val docWeights = u.rows.map(_.toArray(i)).zipWithUniqueId() //trick from last chapter to maintain continuit betweem rows in matrix 
			topDocs += docWeights.top(numDocs).map {					//rows where DF is derived
			  case (score, id) => (docIds(id), score)
			}
		}
		topDocs
	}
	
//Inspect top few concepts
val topConceptTerms = topTermsInTopConcepts(svd, 4, 10, termIds)
val topConceptDocs = topDocsInTopConcepts(svd, 4, 10, docIds)
for ((terms, docs) <- topConceptTerms.zip(topConceptDocs)) {
	println("Concept terms: " + terms.map(_._1).mkString(", "))
	println("Concept docs: " + docs.map(_._1).mkString(", "))
	println()
}


import breeze.linalg.{DenseMatrix => BDenseMatrix}
import com.cloudera.datascience.lsa.LSAQueryEngine

val termIdfs = idfModel.idf.toArray

val queryEngine = new LSAQueryEngine(svd, termIds, docIds, termIdfs)
println("Machine")
queryEngine.printTopTermsForTerm("machine")
println("Artificial")
queryEngine.printTopTermsForTerm("artificial")
println("Machine learning")
queryEngine.printTopDocsForTermQuery(Seq("machine", "learning"))
println("Artificial intelligence")
queryEngine.printTopDocsForTermQuery(Seq("artificial", "intelligence"))
println("Machine learning Artificial intelligence")
queryEngine.printTopDocsForTermQuery(Seq("artificial", "intelligence", "machine", "learning"))
/*
Original Dump
Machine
(machine,1.0000000000000018), (learning,0.7590785618323165), (learn,0.7159204719069167), (supervise,0.6025095960230853), (algorithm,0.5762248089544058), (svm,0.5497873966433428), (datum,0.541928582567879), (improve,0.5389858396421375), (research,0.5377524679275919), (perform,0.5353734823679331)

Artificial
(artificial,0.999999999999999), (aus,0.749740625771433), (research,0.6485758215679959), (intelligence,0.613955513412785), (human,0.6121735956569119), (superintelligence,0.6064729559611044), (intelligent,0.5876906850447825), (develop,0.5686512109120677), (technology,0.5566786994439001), (future,0.5545212273181872)

Machine learning
(Machine learning,64.06587924879922), (Outline of machine learning,38.019270405043585), (Deep learning,27.978329098941217), (Quantum machine learning,24.46067021373109), (Artificial intelligence,23.350181318303434), (Machine learning in bioinformatics,20.158358781214996), (Federated learning,19.446850253852983), (Machine learning in video games,14.921774612438062), (Supervised learning,14.243512748461708), (Semi-supervised learning,14.077272629054148)

Artificial intelligence
(Collective intelligence,70.97626783060878), (Outline of artificial intelligence,49.03109536113169), (Artificial intelligence,40.44251486098382), (Applications of artificial intelligence,35.308890217846674), (Regulation of artificial intelligence,24.938955736518853), (Artificial general intelligence,22.904570303826254), (Artificial intelligence arms race,15.756590575977643), (Computational intelligence,13.722205143285063), (Distributed artificial intelligence,12.51207201936948), (Artificial consciousness,12.0435363571586)

Machine learning Artificial intelligence
(Machine learning,74.45584407810814), (Collective intelligence,73.10747486858763), (Artificial intelligence,63.79269617928727), (Outline of artificial intelligence,56.61167473702667), (Outline of machine learning,47.11644726336468), (Applications of artificial intelligence,44.55520293688335), (Deep learning,33.703204947323876), (Artificial general intelligence,29.75701441942675), (Quantum machine learning,25.648239534517653), (Regulation of artificial intelligence,25.620542947081656)
*/
/*
Modified Dump
Machine
(machine,1.0), (artificial,0.5973960374140815), (research,0.5718084938517987), (computer,0.5396223887672426), (experience,0.511108482933532), (learn,0.5078156801277549), (learning,0.5053879556462173), (researcher,0.497650020553514), (human,0.4955690896447456), (aus,0.48729287882214994)

Artificial
(artificial,0.9999999999999979), (aus,0.6546660076178309), (machine,0.5973960374140815), (research,0.5789052009544935), (superintelligence,0.5686795374831048), (human,0.5503246275692328), (intelligent,0.5192582229639386), (fiction,0.4987576302767342), (include,0.49171264739018794), (ethic,0.49138284209145766)

Machine learning
(Machine learning,79.10730897555501), (Outline of machine-learning,65.08643299547283), (Artificial intelligence,55.25041099543391), (Deep learning,50.40735192410311), (Solomonoff's theory of inductive inference,34.313649107103416), (Artificial consciousness,29.05404357332539), (Supervised learning,27.726003573567084), (Federated learning,27.523545250934916), (Semi-supervised learning,27.15696674094668), (Concept learning,25.424872266855903)

Artificial intelligence
(Collective intelligence,182.3685222656075), (Outline of artificial-intelligence,84.63336314412014), (Artificial intelligence,75.80672205260252), (Regulation of artificial-intelligence,53.23833914340918), (Applications of artificial-intelligence,48.398490130372025), (Artificial general intelligence,39.461439022439656), (Computational intelligence,33.86136393961341), (Artificial consciousness,29.724068434891308), (Artificial imagination,26.700743307382464), (Artificial intelligence arms race,21.825735991051364)

Machine learning Artificial intelligence
(Collective intelligence,185.83271121378914), (Artificial intelligence,131.05713304803652), (Outline of artificial-intelligence,104.96361223548584), (Machine learning,97.81688897432961), (Outline of machine-learning,81.7938273791837), (Applications of artificial-intelligence,63.544947787580156), (Artificial general intelligence,60.36072494642577), (Deep learning,59.622895626338746), (Artificial consciousness,58.77811200821671), (Regulation of artificial-intelligence,53.959004175270756)
*/
/*
queryEngine.printTopDocsForDoc("Yoo-hoo")	//NO YOOHOO WHYYYYYY
queryEngine.printTopDocsForDoc("Cheddar Cheese")	//Kinda too specific I guess
queryEngine.printTopDocsForDoc("Wine")

queryEngine.printTopDocsForTerm("beer")

val termIdfs = idfModel.idf.toArray
queryEngine.printTopDocsForTermQuery(Seq("cheese", "beer"))
*/