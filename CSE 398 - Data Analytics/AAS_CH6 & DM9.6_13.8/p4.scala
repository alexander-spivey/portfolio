//--Singular Value Decomposition--
/*
 SVD takes a m*n and return 3 matrixes
	- M = U S V ^T
		- U = m * k: columns orthonormal basis for the document space
		- S = k * k: each entries correspond to strength of concept
		- V^T = k * n: columns form orthonormal basis for the term space
	- In LSA case, m = #docs & n = #terms & k =#concepts to keep, 
	- when k = n, product of factor matrixes = original matrix exactly
	- k < n, low rank approx: typical case
	- as of spark2.2, no SVD for DF but for RDD, so must convert
*/
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
//SingularValueDecomposition(org.apache.spark.mllib.linalg.distributed.RowMatrix@a779a2f,[11727.069069516834,4602.546620412862,3224.7304432149404,..

/*
 Review
	- term space vector: weight on er term
	- document space vector: weight on er doc
	- concept space vec: weight on er concept
Each item^ defines an axis, and weight means length along axis
	- V: matpping between term space and concept space
	- U: row is doc, coluimn is concept, doc and concept space
	- S:each diagonal erlement correpsond to a sincle concept (column in V & U)
		- Magnitude relate to importance
		- Shitty SVD: throw away (n-k) that has least weight until n = k
		- Also happenes to be sqroots of eigenvalues of MM^T
*/