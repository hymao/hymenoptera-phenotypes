package org.hymao

import java.io.File
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat
import org.semanticweb.owlapi.model.IRI
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper
import scala.collection.JavaConversions._
import org.semanticweb.owlapi.model.AxiomType
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat
import org.phenoscape.scowl.OWL._
import org.semanticweb.owlapi.model.OWLClassExpression
import com.hp.hpl.jena.rdf.model.ModelFactory
import org.semanticweb.HermiT.Reasoner.ReasonerFactory
import org.phenoscape.owlet.SPARQLComposer._
import org.phenoscape.owlet.OwletManchesterSyntaxDataType.SerializableClassExpression
import org.hymao.Vocab._
import org.phenoscape.owlet.Owlet
import com.hp.hpl.jena.query.QueryExecutionFactory
import org.phenoscape.owl.util.ExpressionUtil
import org.semanticweb.owlapi.model.OWLAxiom

object RunQueries extends App {

  val dataFiles = "https://github.com/hymao/hymao-data/raw/master/miko2014_trissevania.ttl" ::
    "https://github.com/hymao/hymao-data/raw/master/balhoff2013_new_caledonia.ttl" ::
    "https://github.com/hymao/hymao-data/raw/master/mullins2012_evaniscus.ttl" :: Nil

  val queries = (has_part some (part_of some Abdomen)) ::
    (has_part some (bearer_of some Color)) ::
    (has_part some (part_of some Genitalia)) ::
    (has_part some (part_of some Head)) ::
    (has_part some (part_of some Integument)) ::
    (has_part some (part_of some Mesosoma)) ::
    (has_part some (part_of some Metasoma)) ::
    (has_part some (part_of some Mouthparts)) ::
    (has_part some (part_of some SenseOrganOfAppendage)) ::
    (has_part some (bearer_of some Shape)) ::
    (has_part some (bearer_of some Size)) ::
    (has_part some (bearer_of some Texture)) :: Nil

  def charactersQuery(term: OWLClassExpression) = select_distinct('character, 'char_label) where (
    bgp(
      t('character, rdfsLabel, 'char_label),
      t('character, can_have_state, 'state),
      t('state, rdfType, 'restriction),
      t('restriction, owlOnProperty, denotes),
      t('restriction, owlAllValuesFrom, 'phenotype),
      t('phenotype, rdfsSubClassOf, term.asOMN)))

  val allCharactersQuery = select_distinct('character, 'char_label) where (
    bgp(
      t('character, rdfsLabel, 'char_label),
      t('character, can_have_state, 'state),
      t('state, rdfType, 'restriction),
      t('restriction, owlOnProperty, denotes),
      t('restriction, owlAllValuesFrom, 'phenotype)))

  val factory = OWLManager.getOWLDataFactory
  val manager = OWLManager.createOWLOntologyManager()
  val psVocab = manager.loadOntologyFromOntologyDocument(IRI.create("http://purl.org/phenoscape/vocab.owl"))
  val hao = manager.loadOntologyFromOntologyDocument(IRI.create("http://purl.obolibrary.org/obo/hao.owl"))
  val bspo = manager.loadOntologyFromOntologyDocument(IRI.create("http://purl.obolibrary.org/obo/bspo.owl"))
  val pato = manager.loadOntologyFromOntologyDocument(IRI.create("http://purl.obolibrary.org/obo/pato.owl"))
  val cdao = manager.loadOntologyFromOntologyDocument(IRI.create("http://purl.obolibrary.org/obo/cdao.owl"))
  val queryOnt = manager.loadOntologyFromOntologyDocument(IRI.create("https://github.com/hymao/hymao-data/raw/master/queries.owx"))
  val baseTBox = Set(psVocab, hao, bspo, pato, cdao, queryOnt).flatMap(ont => ont.getTBoxAxioms(false) ++ ont.getRBoxAxioms(false))
  val annotations = manager.createOntology(Set(psVocab, hao, bspo, pato, cdao, queryOnt).flatMap(_.getAxioms(AxiomType.ANNOTATION_ASSERTION, false).toSet[OWLAxiom]))
  val renderer = ExpressionUtil.createEntityRenderer(factory.getRDFSLabel, annotations)

  def runQueriesOnData(uri: String): Unit = {
    println(s"File: $uri")
    val ont = manager.loadOntologyFromOntologyDocument(IRI.create(uri))
    val tbox = manager.createOntology(ont.getTBoxAxioms(false) ++ baseTBox)
    val model = ModelFactory.createDefaultModel()
    model.read(uri)
    val totalCharacters = QueryExecutionFactory.create(allCharactersQuery, model).execSelect.size
    println(s"Total characters: $totalCharacters")
    val hermit = new ReasonerFactory().createReasoner(tbox)
    val owlet = new Owlet(hermit)
    queries.foreach { term =>
      val expandedQuery = owlet.expandQuery(charactersQuery(term))
      val count = QueryExecutionFactory.create(expandedQuery, model).execSelect.size
      val termLabel = renderer(term)
      println(s"$termLabel: $count")
    }
    hermit.dispose()
  }
  dataFiles.foreach(runQueriesOnData)

}