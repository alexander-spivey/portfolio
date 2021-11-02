//--Computing Average Path Length with Pregel--//
/*
Calculating path length between vertices is iterative. Each phase, vertices will
remember how many it knows and how fat away. Then it will communicate with neighboors
to add new vertices it didn't know. Keep doing this until no new info in list.


1. Figure out what state we need to keep track of at each vertex.
2. Write a function that takes the current state into account and evaluates each 
pair of linked vertices to determine which messages to send at the next phase.
3. Write a function that merges the messages from all of the different vertices 
before we pass the output of the function to the vertex for updating.

Store this list of collection of veritxes inside a Map[VertexId, Int] (int is ID)
for each vertex. The messages passed need to be in similar lookup table as well.

Need to create 2 functions:
1. merge the infrom from new messages
2. update function with state and message
*/
def mergeMaps(m1: Map[VertexId, Int], m2: Map[VertexId, Int])
  : Map[VertexId, Int] = {
  def minThatExists(k: VertexId): Int = {
    math.min(
      m1.getOrElse(k, Int.MaxValue),	//make sre to get a return regardleess
      m2.getOrElse(k, Int.MaxValue))
  }

  (m1.keySet ++ m2.keySet).map {
    k => (k, minThatExists(k))
  }.toMap
}

def update(
    id: VertexId,
    state: Map[VertexId, Int],
    msg: Map[VertexId, Int]) = {
  mergeMaps(state, msg)
}

/*
Final step to send messages to each vertex with neighhooring info each iteration
Each vertex increment its key by one, combine the values with neighbhoors using
mergeMaps, and send result to neighboor if it differs from their internal map.
*/
def checkIncrement(
    a: Map[VertexId, Int],
    b: Map[VertexId, Int],
    bid: VertexId) = {
  val aplus = a.map { case (v, d) => v -> (d + 1) }
  if (b != mergeMaps(aplus, b)) {
    Iterator((bid, aplus))
  } else {
    Iterator.empty
  }
}

//iterate both dst and src
def iterate(e: EdgeTriplet[Map[VertexId, Int], _]) = {
  checkIncrement(e.srcAttr, e.dstAttr, e.dstId) ++ 
  checkIncrement(e.dstAttr, e.srcAttr, e.srcId)
}

//Prepare data for BSP run
val fraction = 0.005
val replacement = false
val sample = interesting.vertices.map(v => v._1).
  sample(replacement, fraction, 1729L)
val ids = sample.collect().toSet
/*
ids: scala.collection.immutable.Set[Long] = Set(4362101753053852075, -7060386309451085362, 
1561067361034022813, 8423631057606637304, -8735611893949641035, -2994881691215263051,...
*/

//Create new graph object with vertex map, nonempty if vertiex is member of sampled IDS
val mapGraph = interesting.mapVertices((id, _) => {
  if (ids.contains(id)) {
    Map(id -> 0)		
  } else {
    Map[VertexId, Int]()
  }
})

//Initial message to send
val start = Map[VertexId, Int]()
val res = mapGraph.pregel(start)(update, iterate, mergeMaps)

//flatmap vertices to exract tuples of (VertexId, VertexId, Int) values that represent the unique path lengths that were computed:
val paths = res.vertices.flatMap { case (id, m) =>
  m.map { case (k, v) =>
    if (id < k) {
      (id, k, v)
    } else {
      (k, id, v)
    }
  }
}.distinct()
paths.cache()

//compute summary stats and histogram of paht lengths
paths.map(_._3).filter(_ > 0).stats()
//(count: 774117, mean: 3.495234, stdev: 0.762609, max: 8.000000, min: 1.000000)
//we ended up with shorter mean than textbook, ty new versions

val hist = paths.map(_._3).countByValue()
hist.toSeq.sorted.foreach(println)
/*
(0,60)
(1,2084)
(2,54329)
(3,340159)
(4,316531)
(5,57841)
(6,3043)
(7,126)
(8,4)
*/
/*
The MEDLINE citation showed very similar ranges and avg path length with other
small-world networks; however very high clustering, but low avg path length.
"In general, real-world graphs should exhibit the small-world property", if not
may be evidence of issue present. 
*/