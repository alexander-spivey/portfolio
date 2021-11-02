//**Bringing Data from the Cluster to the Client
rawblocks.first //prints the first item/element
//res67: String = "id_1","id_2","cmp_fname_c1","cmp_fname_c2","cmp_lname_c1","cmp_lname_c2","cmp_sex","cmp_bd","cmp_bm","cmp_by","cmp_plz","is_match"

val head = rawblocks.take(10) //snatching the first 10 elements
//head: Array[String] = Array("id_1","id_2","cmp_fname_c1","cmp_fname_c2","cmp_lname_c1","cmp_lname_c2","cmp_sex","cmp_bd","cmp_bm","cmp_by","cmp_plz","is_match", 607,53170,1,?,1,?,1,1,1,1,1,TRUE, 88569,88592,1,?,1,?,1,1,1,1,1,TRUE, 21282,26255,1,?,1,?,1,1,1,1,1,TRUE, 20995,42541,1,?,1,?,1,1,1,1,1,TRUE, 27989,34739,1,?,1,?,1,1,1,1,1,TRUE, 32442,69159,1,?,1,?,1,1,1,1,1,TRUE, 24738,29196,1,1,1,?,1,1,1,1,1,TRUE, 9904,89061,1,?,1,?,1,1,1,1,1,TRUE, 29926,36578,1,?,1,?,1,1,1,1,1,TRUE)

head.foreach(println) //for each item in head, print it on a new line
/*
"id_1","id_2","cmp_fname_c1","cmp_fname_c2","cmp_lname_c1","cmp_lname_c2","cmp_sex","cmp_bd","cmp_bm","cmp_by","cmp_plz","is_match"
607,53170,1,?,1,?,1,1,1,1,1,TRUE
88569,88592,1,?,1,?,1,1,1,1,1,TRUE
21282,26255,1,?,1,?,1,1,1,1,1,TRUE
20995,42541,1,?,1,?,1,1,1,1,1,TRUE
27989,34739,1,?,1,?,1,1,1,1,1,TRUE
32442,69159,1,?,1,?,1,1,1,1,1,TRUE
24738,29196,1,1,1,?,1,1,1,1,1,TRUE
9904,89061,1,?,1,?,1,1,1,1,1,TRUE
29926,36578,1,?,1,?,1,1,1,1,1,TRUE
*/

def isHeader(line: String) = line.contains("id_1") //method to return boolean based on if it has string "id_1"
//isHeader: (line: String)Boolean

/*different method declaration style*/
//def isHeader(line: String): Boolean = {line.contains("id_1")} fancy version where you dictate return type

head.filter(isHeader).foreach(println) //within the head, for each item that is true for isheader, print on a new line
//"id_1","id_2","cmp_fname_c1","cmp_fname_c2","cmp_lname_c1","cmp_lname_c2","cmp_sex","cmp_bd","cmp_bm","cmp_by","cmp_plz","is_match"

head.filterNot(isHeader).foreach(println) //within the head rdd, for each item that is not true for isheader, print out on a new line
/*
607,53170,1,?,1,?,1,1,1,1,1,TRUE
88569,88592,1,?,1,?,1,1,1,1,1,TRUE
21282,26255,1,?,1,?,1,1,1,1,1,TRUE
20995,42541,1,?,1,?,1,1,1,1,1,TRUE
27989,34739,1,?,1,?,1,1,1,1,1,TRUE
32442,69159,1,?,1,?,1,1,1,1,1,TRUE
24738,29196,1,1,1,?,1,1,1,1,1,TRUE
9904,89061,1,?,1,?,1,1,1,1,1,TRUE
29926,36578,1,?,1,?,1,1,1,1,1,TRUE
*/
//head.filter(!isHeader(_)).foreach(println) another version using anonymous functions

head.filter(x => !isHeader(x)).length //head filter anything that is a header, and count the remainder
//res71: Int = 9
//head.filter(!isHeader(_)).length another version using anonymous functions
