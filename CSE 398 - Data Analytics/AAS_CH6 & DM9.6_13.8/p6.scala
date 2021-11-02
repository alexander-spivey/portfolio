//--Querying and Scoring with a Low-Dimensional Representation--
/*
How relevant were our results? Use Cosine Similarity (CS) to mesaure angle btwn 2 vectors. 
	- Vector in same direction = relevant
	- computed as dot product of vectors divided by length
	- relevance score between term and doc simplt be element in matrix at intersection!!!
	- however this method is a bit shallow, based entirely on frequency. 
"For example, if the term “artillery” appears nowhere in a document on the “Normandy landings” article but it 
mentions “howitzer” frequently, the LSA representation may be able to recover the relation between “artillery” 
and the article based on the co-occurrence of “artillery” and “howitzer” in other documents." <- TOO GOOD EXAMPLE, SORRY FOR QUOTING
*/

//--Term-Term Relevance--
/*
 What LSA offer
	- Accounting for synonymy by condensing related terms
	- Accounting for polysemy by placing less weight on terms that have multiple meanings
	- Throwing out noise
 Need to discover cosing similarity 
	- Linear algerbra proves that CS between 2 columns in the reconstructed matrix = CS between columns in SV^T
	- Finding CS btwn a term(querry) and all others
		- normalizing each row in VS to length 1
		- multiply the row corresponding to term
		- Each element in result vector rtepresent a similarityh btwn term and querry term
*/
import breeze.linalg.{DenseMatrix => BDenseMatrix}
import com.cloudera.datascience.lsa.LSAQueryEngine

val termIdfs = idfModel.idf.toArray
val queryEngine = new LSAQueryEngine(svd, termIds, docIds, termIdfs)
queryEngine.printTopTermsForTerm("cheese")
queryEngine.printTopTermsForTerm("rice")
queryEngine.printTopTermsForTerm("yoo-hoo")
/*
(cheese,0.9999999999999987), (description,0.5087984242519732), (slightly,0.4201983808537367), (hard,0.4156986159998422), (texture,0.3917936414457268), (image,0.36087570804269453), (region,0.33069519644851925), (originate,0.31620751438226624), (similar,0.31370774567535686), (make,0.3113790958611898)
(rice,1.0000000000000009), (coconut,0.4220940870605958), (mix,0.40757177022896), (cook,0.3997445630210344), (dish,0.37193569104631014), (mixed,0.35279727095906804), (sesame,0.35117687090832833), (wrap,0.3476769455099212), (thailand,0.33834983289671694), (consist,0.33721780539766544)
java.util.NoSuchElementException: key not found: yoo-hoo
  at scala.collection.MapLike$class.default(MapLike.scala:228)
  at scala.collection.AbstractMap.default(Map.scala:59)
  at scala.collection.MapLike$class.apply(MapLike.scala:141)
  at scala.collection.AbstractMap.apply(Map.scala:59)
  at com.cloudera.datascience.lsa.LSAQueryEngine.printTopTermsForTerm(RunLSA.scala:250)
  ... 83 elided
*/
//why is there no term called yoo-hoo? its literally one of the best drinks ever... oh wait, it probably got filtered out xD