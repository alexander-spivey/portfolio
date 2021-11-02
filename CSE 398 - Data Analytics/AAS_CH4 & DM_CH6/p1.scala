//--Covtype Data Set--
/*
Data set contains records the type of forest coverying parcels of land in Corlorado, USA. Each example contains
information describing each parcel of land. 54 total features. 581,012 examples in the data set. Not super big.
*/

//--Preparing the Data--
val dataWithoutHeaders = spark.read.
	option("inferschema", true).	//predict value type
	option("header", false).	//no header line
	csv("/proj/cse398-498/course/AAS_CH4/covtype.data")
//dataWithoutHeaders: org.apache.spark.sql.DataFrame = [_c0: int, _c1: int ... 53 more fields]	
//since the first line is not a header line, only have _c0 for column names and so on
dataWithoutHeaders.head
//res6: org.apache.spark.sql.Row = [2596,51,3,258,0,510,221,232,148,6279,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,..]

val colNames = Seq(	//initial column names
		"Elevation", "Aspect", "Slope",
		"Horizontal_Distance_To_Hydrology", "Vertical_Distance_To_Hydrology",
		"Horizontal_Distance_To_Roadways",
		"Hillshade_9am", "Hillshade_Noon", "Hillshade_3pm",
		"Horizontal_Distance_To_Fire_Points"
	) ++ (	//++ concatenates collections together
		(0 until 4).map(i => s"Wilderness_Area_$i")	//cool trick to label each column differently
	) ++ (
		(0 until 40).map(i => s"Soil_Type_$i")	//same incrementing trick here
	) ++ Seq("Cover_Type")	//save sequence as
val data = dataWithoutHeaders.toDF(colNames: _*).
	withColumn("Cover_Type", $"Cover_Type".cast("double")) //cast each column to double (MLlib API req) and save it under Cover_Type sequence
data.take(5).foreach(println)
/*
colNames: Seq[String] = List(Elevation, Aspect, Slope, Horizontal_Distance_To_Hydrology, Vertical_Distance_To_Hydrology,...)
[2596,51,3,258,0,510,221,232,148,6279,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,5.0]
[2590,56,2,212,-6,390,220,235,151,6225,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,5.0]
[2804,139,9,268,65,3180,234,238,135,6121,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2.0]
[2785,155,18,242,118,3090,238,238,122,6211,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,2.0]
[2595,45,2,153,-1,391,220,234,150,6172,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,5.0]
*/
