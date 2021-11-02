//--Hyperparameter Selection
/* THIS IS TAKEN DIRECTLY FROM THE TEXTBOOK [NO MISTAKES IN MIS UNDERSTANDING]
setRank(10)
The number of latent factors in the model, or equivalently, the number of columns k in the user-feature and product-feature matrices. 
In nontrivial cases, this is also their rank.

setMaxIter(5)
The number of iterations that the factorization runs. More iterations take more time but may produce a better factorization.

setRegParam(0.01)
A standard overfitting parameter, also usually called lambda. Higher values resist overfitting, but values that are too high hurt the 
factorization’s accuracy.

setAlpha(1.0)
Controls the relative weight of observed versus unobserved user-product interactions in the factorization.
*/

val evaluations =
	for (rank     <- Seq(5,  30);
		regParam <- Seq(4.0, 0.0001);
		alpha    <- Seq(1.0, 40.0)) //triple nested loop (different value for each)
    yield {
		val model = new ALS().
			setSeed(Random.nextLong()).
			setImplicitPrefs(true).
			setRank(rank).setRegParam(regParam).	//use test values instead!
			setAlpha(alpha).setMaxIter(20).
			setUserCol("user").setItemCol("artist").
			setRatingCol("count").setPredictionCol("prediction").
			fit(trainData)
		val auc = areaUnderCurve(cvData, bAllArtistIDs, model.transform)
		model.userFactors.unpersist()	//Free up model resources immediately
		model.itemFactors.unpersist()	//Free up model resources immediately
		(auc, (rank, regParam, alpha))
    }
evaluations.sorted.reverse.foreach(println)	//have it printed descending order
/*
evaluations: Seq[(Double, (Int, Double, Double))] = List((0.9201206231476234,(5,4.0,1.0)), (0.9191436348025518,(5,4.0,40.0)), (0.9132892036807542,(5,1.0E-4,1.0)), (0.9188247842998887,(5,1.0E-4,40.0)), (0.9225502446940053,(30,4.0,1.0)), (0.9234664132404989,(30,4.0,40.0)), (0.9082654521690375,(30,1.0E-4,1.0)), (0.9222690272202381,(30,1.0E-4,40.0)))
(0.9234664132404989,(30,4.0,40.0))
(0.9225502446940053,(30,4.0,1.0))
(0.9222690272202381,(30,1.0E-4,40.0))
(0.9201206231476234,(5,4.0,1.0))
(0.9191436348025518,(5,4.0,40.0))
(0.9188247842998887,(5,1.0E-4,40.0))
(0.9132892036807542,(5,1.0E-4,1.0))
(0.9082654521690375,(30,1.0E-4,1.0))
*/
//when alpha was 40, did significantly better (model is better of focusing on what the user listened to that not)
//higher regparam, because model may be overfitting