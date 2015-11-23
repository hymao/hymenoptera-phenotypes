package org.hymao

import org.phenoscape.scowl.OWL._
import com.hp.hpl.jena.vocabulary.RDF
import com.hp.hpl.jena.vocabulary.RDFS
import com.hp.hpl.jena.vocabulary.OWL2
import com.hp.hpl.jena.graph.NodeFactory
import com.hp.hpl.jena.graph.Node
import org.semanticweb.owlapi.model.IRI

object Vocab {

  val bearer_of = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000053")
  val inheres_in = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000052")
  val has_part = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000051")
  val part_of = ObjectProperty("http://purl.obolibrary.org/obo/BFO_0000050")
  val denotes = ObjectProperty("http://purl.obolibrary.org/obo/IAO_0000219")
  val has_state = ObjectProperty("http://purl.obolibrary.org/obo/CDAO_0000184")
  val belongs_to_TU = ObjectProperty("http://purl.obolibrary.org/obo/CDAO_0000191")
  val in_subset = IRI.create("http://www.geneontology.org/formats/oboInOwl#inSubset")
  val attribute_slim = IRI.create("http://purl.obolibrary.org/obo/pato#attribute_slim")
  val may_have_state_value = ObjectProperty("http://purl.org/phenoscape/vocab.owl#may_have_state_value")
  val can_have_state = ObjectProperty("http://purl.oclc.org/NET/mx-database/can_have_state")

  val rdfType = RDF.`type`.asNode
  val rdfsSubClassOf = RDFS.subClassOf.asNode
  val rdfsLabel = RDFS.label.asNode
  val owlOnProperty = OWL2.onProperty.asNode
  val owlAllValuesFrom = OWL2.allValuesFrom.asNode

  val Character = Class("http://purl.obolibrary.org/obo/CDAO_0000075")
  val CharacterStateDomain = Class("http://purl.obolibrary.org/obo/CDAO_0000045")

  val AnatomicalEntity = Class("http://purl.obolibrary.org/obo/HAO_0000000")
  val Integument = Class("http://purl.obolibrary.org/obo/HAO_0000421")
  val Head = Class("http://purl.obolibrary.org/obo/HAO_0000397")
  val Mouthparts = Class("http://purl.obolibrary.org/obo/HAO_0000639")
  val Genitalia = Class("http://purl.obolibrary.org/obo/HAO_0000374")
  val Mesosoma = Class("http://purl.obolibrary.org/obo/HAO_0000576")
  val Metasoma = Class("http://purl.obolibrary.org/obo/HAO_0000626")
  val Abdomen = Class("http://purl.obolibrary.org/obo/HAO_0000015")
  val SenseOrganOfAppendage = Class("http://purl.obolibrary.org/obo/HAO_0000930") and (part_of some Class("http://purl.obolibrary.org/obo/HAO_0000144"))
  val Color = Class("http://purl.obolibrary.org/obo/PATO_0000014")
  val Shape = Class("http://purl.obolibrary.org/obo/PATO_0000052")
  val Size = Class("http://purl.obolibrary.org/obo/PATO_0000117")
  val Texture = Class("http://purl.obolibrary.org/obo/PATO_0000150")
}