val rdd = sc.parallelize(Array(1, 2, 2, 4), 4)
val rawblocks = sc.textFile("linkage") ///csv
rawblocks.first
val head = rawblocks.take(10)
rdd.count()
rdd.collect()
head.foreach(println)

//def isHeader(line: String) = line.contains("id_1")
def isHeader(line: String): Boolean = {
line.contains("id_1")
}
head.filter(isHeader).foreach(println)
head.filterNot(isHeader).length
head.filter(x => !isHeader(x)).length
head.filter(!isHeader(_)).length

val noheader = rawblocks.filter(x => !isHeader(x))
noheader.first

spark
spark.sparkContext
val prev = spark.read.csv("linkage") ///csv
prev.show()

val parsed = spark.read.
	option("header", "true").
	option("nullValue", "?").
	option("inferSchema", "true").
	csv("linkage") ///csv
parsed.printSchema()
