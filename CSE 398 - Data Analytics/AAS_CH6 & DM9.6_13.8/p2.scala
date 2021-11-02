//--Lemmatization--
/*
 - Turn into bag of terms
	- Remove dead words, that, is, etc
	- Words can have diff form, like monkey vs monkeys
		-Combining these different forms is called stemming or lemmatization
			- Stemming: heuristiscs based tech for chopping of chars
*/
import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer
import edu.stanford.nlp.pipeline._
import edu.stanford.nlp.ling.CoreAnnotations._
import java.util.Properties
import org.apache.spark.sql.Dataset

def createNLPPipeline(): StanfordCoreNLP = {
	val props = new Properties()
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

val stopWords = scala.io.Source.fromFile("/proj/cse398-498/course/aas/ch06-lsa/src/main/resources/stopwords.txt").getLines().toSet
val bStopWords = spark.sparkContext.broadcast(stopWords) //broadcast to save memory and not be able to overwrite it

val terms: Dataset[(String, Seq[String])] = docTexts.mapPartitions { iter =>
	val pipeline = createNLPPipeline()
	iter.map { case(title, contents) =>
		(title, plainTextToLemmas(contents, bStopWords.value, pipeline))
	}
} //Use mapPartitions so that we only initialize the NLP pipeline object once per partition instead of once per document.	