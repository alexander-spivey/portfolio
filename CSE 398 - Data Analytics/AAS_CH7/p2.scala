//--Parsing XML Documents with Scala’s XML Library--//
/*
Scala has treated XML as first-class data type since 1.2. Folllowing is valid:
import scala.xml._
val cit = <MedlineCitation>data</MedlineCitation>
Support for XML literals is unusual. As of Scala 2.11, scalaxml no longer part
of core Scala libraries. Must explicitly include scala-xml to use. 
*/
//Start by taking unparsed first citation
import scala.xml._
val rawXml = medlineRaw.take(1)(0)
val elem = XML.loadString(rawXml)	//elem variable instance of scala.XML.Elem

//used to represent individual node, have lots of builtin funcs
elem.label
/*res18: String = MedlineCitation*/
elem.attributes
/*res20: scala.xml.MetaData =  Status="MEDLINE" Owner="NLM"*/

//has operators to find children of node
elem \ "MeshHeadingList" //only get node direct children 
/*
<MeshHeading>
<DescriptorName UI="D020816" MajorTopicYN="N">Amino Acid Motifs</DescriptorName>
</MeshHeading>
<MeshHeading>
<DescriptorName UI="D000595" MajorTopicYN="N">Amino Acid Sequence</DescriptorName>
</MeshHeading>
<MeshHeading>
<DescriptorName UI="D017124" MajorTopicYN="N">Conserved Sequence</DescriptorName>
</MeshHeading>
<MeshHeading>
<DescriptorName UI="D004268" MajorTopicYN="N">DNA-Binding Proteins</DescriptorName>
<QualifierName UI="Q000737" MajorTopicYN="N">chemistry</QualifierName>
<QualifierName UI="Q000378" MajorTopicYN="N">metabolism</QualifierName>
</MeshHeading>
<MeshHeading>
<DescriptorName UI="D003903" MajorTopicYN="N">Deuterium</DescriptorName>
<QualifierName UI="Q000378" MajorTopicYN="N">metabolism</QualifierNa...
*/

//extract nondirect children using "//"
elem \\ "MeshHeading"
/*
res22: scala.xml.NodeSeq =
NodeSeq(<MeshHeading>
<DescriptorName UI="D020816" MajorTopicYN="N">Amino Acid Motifs</DescriptorName>
</MeshHeading>, <MeshHeading>
<DescriptorName UI="D000595" MajorTopicYN="N">Amino Acid Sequence</DescriptorName>
</MeshHeading>, <MeshHeading>
<DescriptorName UI="D017124" MajorTopicYN="N">Conserved Sequence</DescriptorName>
</MeshHeading>, <MeshHeading>
<DescriptorName UI="D004268" MajorTopicYN="N">DNA-Binding Proteins</DescriptorName>
<QualifierName UI="Q000737" MajorTopicYN="N">chemistry</QualifierName>
<QualifierName UI="Q000378" MajorTopicYN="N">metabolism</QualifierName>
</MeshHeading>, <MeshHeading>
<DescriptorName UI="D003903" MajorTopicYN="N">Deuterium</DescriptorName>
<QualifierName UI="Q000378" MajorTopicYN="N">metabolism</QualifierName>
</MeshHead...
*/

//use "\\" to get DescriptorName directly, and retrieve MESH tag within node
(elem \\ "DescriptorName").map(_.text)

//return the name of major Mesh tags for each article
def majorTopics(record: String): Seq[String] = {
  val elem = XML.loadString(record)
  val dn = elem \\ "DescriptorName"
  val mt = dn.filter(n => (n \ "@MajorTopicYN").text == "Y")
  mt.map(n => n.text)
}
val elem = XML.loadString(rawXml)
val tempElem = elem.toString
majorTopics(tempElem)
/*res25: Seq[String] = List(Nuclear Magnetic Resonance, Biomolecular)*/

//now it parsing locally, apply to parse Mesh into our RDD
val medline = medlineRaw.map(majorTopics)
medline.cache()
medline.take(1)(0)
/*res27: Seq[String] = List(Nuclear Magnetic Resonance, Biomolecular)*/