//--Compute the TF-IDFS--
/*
As of rn term is a datset of sequences of terms, each relating to a single doc. Next need to count occurance. Use Estimator and Transformer.
*/
val termsDF = terms.toDF("title", "terms")		//add column titles
termsDF.show()
/*
+--------------------+--------------------+
|               title|               terms|
+--------------------+--------------------+
|Agricultural science|[agricultural, sc...|
|         Agriculture|[agriculture, agr...|
|        Annual plant|[annual, plant, a...|
|         Arable land|[arable, land, ar...|
|              Almond|[almond, the, alm...|
| Agrippina the Elder|[agrippina, elder...|
|         Aquaculture|[aquaculture, aqu...|
|          Anaxagoras|[anaxagoras, anax...|
|             Archery|[archery, archery...|
|Action Against Hu...|[action, hunger, ...|
|          Alcoholism|[alcoholism, alco...|
|                Beer|[beer, beer, one,...|
|          Bubble tea|[bubble, tea, bob...|
|           Boomerang|[boomerang, boome...|
|              Beagle|[beagle, the, bea...|
|   Bubble and squeak|[bubble, squeak, ...|
|             Brewing|[brewing, brewing...|
|                Bean|[bean, bean, seed...|
| Bacterial vaginosis|[bacterial, vagin...|
|     Body mass index|[body, mass, inde...|
+--------------------+--------------------+
*/
val filtered = termsDF.where(size($"terms") > 1)	//remove all docs that have less than 1 word

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