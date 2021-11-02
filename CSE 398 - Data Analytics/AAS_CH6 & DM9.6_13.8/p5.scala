//--Finding Important Concepts--
/*
SVD output a shit ton of numbers. V matrix represents concepts through the terms that are important to em.
V col for each concept, row for each term
Position relevate to importance. 
*/
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
/*
Concept terms: ale, usa, brewing, bronze, silver, gold, brewery, lager, stout, beer
Concept docs: World Beer Cup, GABS Hottest 100 Aussie Craft Beers of the Year, 
List of microbreweries, Beer, Champion Beer of Britain, Microbrewery, Brewing, List of breweries in British Columbia, 
Alewife (trade), Beer by region

Concept terms: ale, usa, bronze, brewing, silver, lager, gold, stout, pale, brewery
Concept docs: World Beer Cup, Boiling (brewing), Oshi sabo, Endicott Pear, Gallo Salame, 
Shi Youzhen, Pre-charged pneumatic (PCP), Bob Taco, Tempe bongkrek, Lentinula edodes

Concept terms: soil, clay, organic, plant, water, mineral, nitrogen, nutrient, matter, material
Concept docs: Soil, Soil compaction (agriculture), Base-cation saturation ratio, Organic farming, Soil pH, 
Crop rotation, List of universities with soil science curriculum, Agricultural soil science, No-till farming, Plough

Concept terms: file, gardens, garden, jpg, private, soil, house, park, heritage, rose
Concept docs: Heritage gardens in Australia, Soil, History of gardening, Garden design, Japanese rock garden, 
Community gardening in the United States, Garden roses, Gardening, Community gardening, List of New York state parks
*/
//LOOK THEY ARE ACTUALLY ALL SUPER RELEVANT

//In the textbook, they kept getting image based results, but not an issue here! nonetheless, we fix wikiXMLToPlainText
/*
def wikiXmlToPlainText(xml: String): Option[(String, String)] = {
  ...
  if (page.isEmpty || !page.isArticle || page.isRedirect ||
      page.getTitle.contains("(disambiguation)")) {
    None
  } else {
    Some((page.getTitle, page.getContent))
  }
}
*/