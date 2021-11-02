//--Document-Document Relevance--
/*
To find the similarity between 2 docs, comput CS between u1Ts and u2Ts where ui is the row in U corresponding to doc i.
To find one doc vs all, compuite normalize(US)u_t
*/
queryEngine.printTopDocsForDoc("Yoo-hoo")	//NO YOOHOO WHYYYYYY
queryEngine.printTopDocsForDoc("Cheddar Cheese")	//Kinda too specific I guess
queryEngine.printTopDocsForDoc("Wine")
/*
(Wine,1.0000000000000009), (Winemaker,0.8965201831052058), (Wine auction,0.8903566971796215), 
(Wine tasting descriptors,0.8775102691693857), (Wine law,0.8751851408414633), (Wine and food matching,0.8719598374411215), 
(Wine for the Confused,0.8668480858989095), (André Jullien,0.865373232160856), (Wine fault,0.8609460307620234), 
(Wine tasting,0.8585223710035453)
*/

//--Document-Term Relevance--
/*
 - This is equal to udT S vt, where ud is the row in U corresponding to the document, and vt is the row in V corresponding to the term. 
 - similarity between a term and every document is equivalent to US vt
 - similarity between a document and every term comes from udT SV
*/
queryEngine.printTopDocsForTerm("beer")
/*
(World Beer Cup,1677.542939316098), (Beer,798.7440445136723), (Beer by region,691.5097257567146), 
(Beer festival,439.079028078366), (Low-alcohol beer,346.4795868033664), (Brewing,326.70313247688756),
(Microbrewery,300.20738619575474), (List of drinks,279.2270927188251), 
(List of alcohol laws of the United States,242.72705639915338), (Pub,207.7323304673555)
*/

//--Multiple-Term Queries--
/*
Finding doc relevant to single term by selecting row corresponding to query in V
Equivallent to multplying V by term vector (nonzero entry)
Multiple terms: multiplying V by term vector for multiply terms???
*/

//To maintin weight scheme, set val for each term to inverse
val termIdfs = idfModel.idf.toArray
queryEngine.printTopDocsForTermQuery(Seq("cheese", "beer"))
/*
(World Beer Cup,4843.644573090004), (List of cheeses,2535.2993404319186), (Beer,2306.257447652742),
 (Beer by region,1996.1447443405243), (Cheese,1293.794986106358), (Beer festival,1268.212951393017),
 (Low-alcohol beer,1000.6471198980646), (Brewing,943.4903139353289), (Microbrewery,867.230443921393),
 (List of drinks,806.3654642144587)
*/