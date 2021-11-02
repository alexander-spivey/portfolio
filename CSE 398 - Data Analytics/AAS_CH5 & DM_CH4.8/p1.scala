//--Anomaly Detection--
/*
Anomaly detection is used to find fraud and other type of attacks. Since if a normal users actions would be classified, but these attacks, 
would be an unusual interaction, which would classify it outside of most clusters. This is known as unsupervised learning, since we don't known
if an instance is anomaly or not. 
*/
//--K-means Clustering--
/*
Clustering is one of the most common unsupervised learning alg. We have to define how many clusters, k, we want (IMPORTANT HYPERPARAMETER)
We find the centroid of each cluster, aka the means of each instance, and if the distance from the centroid is little, then it is a 'like'
instance (this is within Euclidean space). 
*/
//--Network Intrusion--
/*
While it may be easy to determine most common attacks, like accessing every port when its only normal for consumers to acces one or two, but 
still find a needle in a haystack of thousands network requests. Most mallicious attacks have some known patterns, but what do we do when it
is a new type of attack that has never been seen before? "Part of detecting potential network intrusions is detecting anomalies. 
These are connections that aren’t known to be attacks but do not resemble connections that have been observed in the past." 
All items within the cluuster are normal connections, anything outside the clusters are unusual and could be an anomaly.
*/
//--KDD Cup 1999 Data Set--
/*
Check out kaggle, seems supppper cool!!! Data set is 708mb, with 5 million connections. Each connection is one line of CSV with 38 attributes.
Example:
0,tcp,http,SF,215,45076,
0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1,
0.00,0.00,0.00,0.00,1.00,0.00,0.00,0,0,0.00,
0.00,0.00,0.00,0.00,0.00,0.00,0.00,normal.
Most of these categorical features are 0 or 1, so we can convert ot binary. In the example, we see that it is a normal connection, and some
other instances will even mention what attack this is, but not point of project. We will be setting aside that label so we can focus on working
on distiguishing anomaly
*/
//--A First Take on Clustering--
val dataWithoutHeader = spark.read.	//reading the data in, but lacks header
	option("inferSchema", true).
	option("header", false).
	csv("/proj/cse398-498/course/AAS_CH5/kddcup.data")
dataWithoutHeader.head()

val data = dataWithoutHeader.toDF(
	"duration", "protocol_type", "service", "flag",
	"src_bytes", "dst_bytes", "land", "wrong_fragment", "urgent",
	"hot", "num_failed_logins", "logged_in", "num_compromised",
	"root_shell", "su_attempted", "num_root", "num_file_creations",
	"num_shells", "num_access_files", "num_outbound_cmds",
	"is_host_login", "is_guest_login", "count", "srv_count",
	"serror_rate", "srv_serror_rate", "rerror_rate", "srv_rerror_rate",
	"same_srv_rate", "diff_srv_rate", "srv_diff_host_rate",
	"dst_host_count", "dst_host_srv_count",
	"dst_host_same_srv_rate", "dst_host_diff_srv_rate",
	"dst_host_same_src_port_rate", "dst_host_srv_diff_host_rate",
	"dst_host_serror_rate", "dst_host_srv_serror_rate",
	"dst_host_rerror_rate", "dst_host_srv_rerror_rate",
	"label")	//adding our column labels to our dataWithoutHeader

data.select("label").groupBy("label").count().orderBy($"count".desc).show(25) //select count of labels and order by descending, showing top 25
/*
+----------------+-------+
|           label|  count|
+----------------+-------+
|          smurf.|2807886|
|        neptune.|1072017|
|         normal.| 972781|
|          satan.|  15892|
|        ipsweep.|  12481|
|      portsweep.|  10413|
|           nmap.|   2316|
|           back.|   2203|
|    warezclient.|   1020|
|       teardrop.|    979|
|            pod.|    264|
|   guess_passwd.|     53|
|buffer_overflow.|     30|
|           land.|     21|
|    warezmaster.|     20|
|           imap.|     12|
|        rootkit.|     10|
|     loadmodule.|      9|
|      ftp_write.|      8|
|       multihop.|      7|
|            phf.|      4|
|           perl.|      3|
|            spy.|      2|
+----------------+-------+
*/
//23 total labels

//As of right now, the second column may be nonnumeric (tcp, udp, icmp) and same label column. K-mean clustering need numerical, so ignore those
//"A VectorAssembler creates a feature vector, a KMeans implementation creates a model off feature vectors, and a Pipeline stitches it all together"
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.ml.feature.VectorAssembler

val numericData = data.drop("protocol_type", "service", "flag").cache()

val assembler = new VectorAssembler().
	setInputCols(numericData.columns.filter(_ != "label")).
	setOutputCol("featureVector")

val kmeans = new KMeans().
	setPredictionCol("cluster").
	setFeaturesCol("featureVector")
	
val pipeline = new Pipeline().setStages(Array(assembler, kmeans))
val pipelineModel = pipeline.fit(numericData)

val kmeansModel = pipelineModel.stages.last.asInstanceOf[KMeansModel]
kmeansModel.clusterCenters.foreach(println)
/*
[48.34019491959669,1834.6215497618625,826.2031900016945,5.7161172049003456E-6,6.487793027561892E-4,7.961734678254053E-6,0.012437658596734055,3.205108575604837E-5,0.14352904910348827,0.00808830584493399,6.818511237273984E-5,3.6746467745787934E-5,0.012934960793560386,0.0011887482315762398,7.430952366370449E-5,0.0010211435092468404,0.0,4.082940860643104E-7,8.351655530445469E-4,334.9735084506668,295.26714620807076,0.17797031701994256,0.1780369894027269,0.05766489875327379,0.05772990937912744,0.7898841322630906,0.02117961060991097,0.028260810096297884,232.98107822302248,189.21428335201279,0.7537133898007772,0.03071097882384052,0.605051930924901,0.006464107887636894,0.1780911843182284,0.1778858981346887,0.05792761150001272,0.05765922142401037]
[10999.0,0.0,1.309937401E9,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,1.0,0.0,0.0,1.0,1.0,1.0,0.0,0.0,255.0,1.0,0.0,0.65,1.0,0.0,0.0,0.0,1.0,1.0]
*/
/*
The values are the coordinates of the centroid with respect to input features.
It printed of 2 vectors, meaning that it found k-2 clusters to the data. This nowhere near the 23 distinct types of connections
*/

val withCluster = pipelineModel.transform(numericData)

withCluster.select("cluster", "label").
	groupBy("cluster", "label").count().
	orderBy($"cluster", $"count".desc).
	show(25)
/*
+-------+----------------+-------+
|cluster|           label|  count|
+-------+----------------+-------+
|      0|          smurf.|2807886|
|      0|        neptune.|1072017|
|      0|         normal.| 972781|
|      0|          satan.|  15892|
|      0|        ipsweep.|  12481|
|      0|      portsweep.|  10412|
|      0|           nmap.|   2316|
|      0|           back.|   2203|
|      0|    warezclient.|   1020|
|      0|       teardrop.|    979|
|      0|            pod.|    264|
|      0|   guess_passwd.|     53|
|      0|buffer_overflow.|     30|
|      0|           land.|     21|
|      0|    warezmaster.|     20|
|      0|           imap.|     12|
|      0|        rootkit.|     10|
|      0|     loadmodule.|      9|
|      0|      ftp_write.|      8|
|      0|       multihop.|      7|
|      0|            phf.|      4|
|      0|           perl.|      3|
|      0|            spy.|      2|
|      1|      portsweep.|      1|	//only one data point in cluster 1
+-------+----------------+-------+	//no other data points PERIOD
*/