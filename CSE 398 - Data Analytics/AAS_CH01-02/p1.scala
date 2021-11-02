//**Getting Started**
val rawblocks = sc.textFile("linkage/csv") //after seperating the cvs files into its own seperate folder, we finally were able to open it and save it as an RDD
//rawblocks: org.apache.spark.rdd.RDD[String] = linkage/cvs MapPartitionsRDD[1] at textFile at p1.scala:24

/*we can redeclare our variables as often in shell but not in script*/
//val rawblocks = sc.textFile("linkage/csv") //finally getting it to access the proper files (create an rdd from txtfile)
//rawblocks: org.apache.spark.rdd.RDD[String] = linkage/csv MapPartitionsRDD[22] at textFile at p1v3.scala:28


val rdd = sc.parallelize(Array(1,2,3,4),4) //creating a rdd by using parallelize
//rdd: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[20] at parallelize at p1v3.scala:28

rdd.count() //returns the number of objects in an RDD
//res3: Long = 4

rdd.collect() //returns all objects in RDD as an array thats saved to the local mem
//res4: Array[Int] = Array(1, 2, 3, 4)

//rdd.saveAsTextFile("filename") saves the contents of rdd, each object is seperate txt file
//look at folder called simpleRDD result
