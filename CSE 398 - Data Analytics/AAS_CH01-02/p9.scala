//**Model Evaluation**
//I do apologize for having a copy and paste of the textbook here, but since I couldn't get past scoring, I don't really understand fully what is going on here. Due to this, I decided it important for me to be able to reread this portion when needed.

/*
The final step in creating our scoring function is to decide on what threshold the score must exceed in order for us to predict that the two records represent a match. If we set the threshold too high, then we will incorrectly mark a matching record as a miss (called the false-negative rate), whereas if we set the threshold too low, we will incorrectly label misses as matches (the false-positive rate.) For any nontrivial problem, we always have to trade some false positives for some false negatives, and the question of what the threshold value should be usually comes down to the relative cost of the two kinds of errors in the situation to which the model is being applied.

To help us choose a threshold, it’s helpful to create a 2×2 contingency table (which is sometimes called a cross tabulation, or crosstab) that counts the number of records whose scores fall above/below the threshold value crossed with the number of records in each of those categories that were/were not matches. Since we don’t know what threshold value we’re going to use yet, let’s write a function that takes the scored DataFrame and the choice of threshold as parameters and computes the crosstabs using the DataFrame API:
*/

def crossTabs(scored: DataFrame, t: Double): DataFrame = { //input DataFrame, Double, ouput DataFrame
  scored.//using the referenced dataframe
    selectExpr(s"score >= $t as above", "is_match").
    //determine the value of the field named above based on the value of the t argument using Scala’s string interpolation syntax, which allows us to substitute variables by name if we preface the string literal with the letter s
    groupBy("above"). //once the above field is defined
    pivot("is_match", Seq("true", "false")). //we pivot around is_match
    count() //count it up!
} 

crossTabs(scored, 4.0).show()
/*
crossTabs: (scored: org.apache.spark.sql.DataFrame, t: Double)org.apache.spark.sql.DataFrame

scala> crossTabs(scored, 4.0).show()
[Stage 64:==================================>                      (6 + 4) / 10]21/08/30 00:22:01 ERROR Executor: Exception in task 9.0 in stage 64.0 (TID 2147)
java.lang.NullPointerException: Null value appeared in non-nullable field:
- field (class: "scala.Int", name: "id_2")
- root class: "$line102.$read.$iw.$iw.MatchData"
If the schema is inferred from a Scala tuple/case class, or a Java bean, please try to use scala.Option[_] or other nullable types (e.g. java.lang.Integer instead of int/scala.Int).
	at org.apache.spark.sql.catalyst.expressions.GeneratedClass$GeneratedIterator.agg_doAggregateWithKeys$(Unknown Source)
	at org.apache.spark.sql.catalyst.expressions.GeneratedClass$GeneratedIterator.processNext(Unknown Source)
	at org.apache.spark.sql.execution.BufferedRowIterator.hasNext(BufferedRowIterator.java:43)
	at org.apache.spark.sql.execution.WholeStageCodegenExec$$anonfun$8$$anon$1.hasNext(WholeStageCodegenExec.scala:395)
	at scala.collection.Iterator$$anon$11.hasNext(Iterator.scala:408)
	at org.apache.spark.shuffle.sort.BypassMergeSortShuffleWriter.write(BypassMergeSortShuffleWriter.java:125)
	at org.apache.spark.scheduler.ShuffleMapTask.runTask(ShuffleMapTask.scala:96)
	at org.apache.spark.scheduler.ShuffleMapTask.runTask(ShuffleMapTask.scala:53)
	at org.apache.spark.scheduler.Task.run(Task.scala:108)
	at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:335)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
*/
/* WHAT SHOULD HAVE HAPPENED
Applying a high threshold value of 4.0, meaning that the average of the five features is 0.8, we can filter out almost all of the nonmatches while keeping over 90% of the matches:
...
+-----+-----+-------+
|above| true|  false|
+-----+-----+-------+
| true|20871|    637|
|false|   60|5727564|
+-----+-----+-------+
*/


crossTabs(scored, 2.0).show()
//the actual result still errored, soo lets look at the fake one below!
/*Applying the lower threshold of 2.0, we can ensure that we capture all of the known matching records, but at a substantial cost in terms of false positive (top-right cell):
...
+-----+-----+-------+
|above| true|  false|
+-----+-----+-------+
| true|20931| 596414|
|false| null|5131787|
+-----+-----+-------+
*/
//see if you can find a way to use some of the other values from MatchData (both missing and not) to come up with a scoring function that successfully identifies every true match at the cost of less than 100 false positives.
//^^^ hahaha no, I can't even get past inferSchema
