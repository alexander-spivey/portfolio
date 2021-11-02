//**Aggregations**
val grouped = mds.groupBy(md => md.matched) //we are aggregating over our mds array and using groupby to create a Scale Map[bool, arr] (look like weird dictionary), key is based on matched field
/*
grouped: scala.collection.immutable.Map[Boolean,Array[MatchData]] = Map(true -> Array(MatchData(607,53170,[D@187d8ae9,true), MatchData(88569,88592,[D@13d75a86,true), MatchData(21282,26255,[D@3af3c661,true), MatchData(20995,42541,[D@127f90eb,true), MatchData(27989,34739,[D@66a17a8f,true), MatchData(32442,69159,[D@4f8b0fb7,true), MatchData(24738,29196,[D@6b6f18e9,true), MatchData(9904,89061,[D@57935a7,true), MatchData(29926,36578,[D@68e14285,true)))
*/

grouped.mapValues(x => x.size).foreach(println) //print out how many were matched in a tuple
//(true,9)
//when performing aggregations, information split across, has to transfer (ser, deser), take time, so instead we need to filter more before agg.

//**Creating Histograms**
val matchCounts = parsed.map(md => md.matched).countByValue() //creates a simple histogram that counts matched values through parsed
//nice function exists, return value to client
//okay as of 8/26 10:30pm, when I try to use count or countbyvalue, it dies because it cant find special partitions of the rdd im trying to access... don't know if this just me or everyone
//Block rdd_6_5 could not be removed as it was not found on disk or in memory
