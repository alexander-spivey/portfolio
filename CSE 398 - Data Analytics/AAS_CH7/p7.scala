//--Small-World Networks--//
/*
 “Collective Dynamics of ‘Small-World’ Networks” outlined the first mathematical mode
 to generate graphs that has “small-world” properties in real-world graphs:
 1. most nodes have small degree and belongs to dense cluster
 2. can travel from any node to another from sets of edges
 Researchers defined metric based on how strong these properties are true
*/


//-Cliques and Clustering Coefficients-//
/*
Complete if every vertex is connected. In graph, may be subset that are complete,
called clique. Presence of many large cliques similar to locally dense real
small-world networks. Finding cliques is difficult and expensive. 

One option is to use triangle count.  "A triangle is a complete graph on three vertices,
 and the triangle count at a vertex V is simply the number of triangles that contain V.
 The triangle count is a measure of how many neighbors of V are also connected to each other."
 
New metric: local clustering coefficent: undirected graph, 
local clustering coefficient C for a vertex that has k neighbors and t triangles is:
C = 2t / k(k-1)
*/
val triCountGraph = interesting.triangleCount()
triCountGraph.vertices.map(x => x._2).stats()
//(count: 14548, mean: 74.660159, stdev: 295.327094, max: 11023.000000, min: 0.000000)

//normalize triangle count
val maxTrisGraph = interestingDegrees.mapValues(d => d * (d - 1) / 2.0)

//join triangle count and normaliztion terms
val clusterCoef = triCountGraph.vertices.
  innerJoin(maxTrisGraph) { (vertexId, triCount, maxTris) => {
    if (maxTris == 0) 0 else triCount / maxTris //avoid dividing by 0
  }
}

clusterCoef.map(_._2).sum() / interesting.vertices.count() //network average clustering coefficient
//0.30624625605188605
