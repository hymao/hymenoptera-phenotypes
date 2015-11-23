package org.hymao

import java.io.File
import scala.collection.JavaConversions._
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.IRI
import org.semanticweb.owlapi.model.OWLClassExpression
import org.semanticweb.owlapi.model.OWLIndividual
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom
import org.semanticweb.owlapi.model.OWLOntology
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOWLObjectRendererImpl
import org.semanticweb.owlapi.model.OWLNamedIndividual
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary
import org.semanticweb.owlapi.util.AnnotationValueShortFormProvider
import java.util.HashMap
import org.semanticweb.owlapi.model.AxiomType
import org.semanticweb.owlapi.util.ShortFormProvider
import org.semanticweb.owlapi.model.OWLEntity
import org.semanticweb.owlapi.model.OWLClass
import org.hymao.Vocab._
import scala.xml.PrettyPrinter

object CreatePhenotypeTable extends App {

  val inputFile = args(0)
  val factory = OWLManager.getOWLDataFactory()
  val label = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI)
  val manchester = new ManchesterOWLSyntaxOWLObjectRendererImpl()
  val manager = OWLManager.createOWLOntologyManager()
  manchester.setShortFormProvider(new HTMLShortFormProvider(new AnnotationValueShortFormProvider(List(label), new HashMap(), manager)))
  val ontology = manager.loadOntologyFromOntologyDocument(new File(inputFile))
  val table =
    <table>
      <thead>
        <tr>
          <th>Character</th><th>State</th><th>Semantic Phenotype</th>
        </tr>
      </thead>
      <tbody>
        {
          for {
            character <- getCharacters(ontology)
            state <- getStates(character, ontology)
          } yield {
            <tr>
              <td> { label(character, ontology) }</td>
              <td> { label(state, ontology) }</td>
              <td> { translate(getPhenotype(state, ontology)) }</td>
            </tr>
          }
        }
      </tbody>
    </table>
  val prettyPrinter = new PrettyPrinter(9999, 2)
  println(prettyPrinter.format(table))

  def getCharacters(ontology: OWLOntology): Iterable[OWLIndividual] = Character.getIndividuals(ontology.getImportsClosure)

  def getStates(character: OWLIndividual, ontology: OWLOntology): Iterable[OWLIndividual] = ontology.getImportsClosure.flatMap(ont =>
    character.getObjectPropertyValues(can_have_state, ont) ++ character.getObjectPropertyValues(may_have_state_value, ont))

  def getPhenotype(state: OWLIndividual, ontology: OWLOntology): OWLClassExpression = {
    val allValuesFroms = state.getTypes(ontology.getImportsClosure).filter(_.isInstanceOf[OWLObjectAllValuesFrom]).map(_.asInstanceOf[OWLObjectAllValuesFrom]).filter(_.getProperty == denotes)
    val equivClasses = allValuesFroms.map(_.getFiller).map(_.asOWLClass).map(_.getSuperClasses(ontology.getImportsClosure)).flatten
    if (equivClasses.isEmpty) OWLManager.getOWLDataFactory.getOWLThing
    else equivClasses.head
  }

  def translate(phenotype: OWLClassExpression): String = manchester.render(phenotype).replaceAll("\n", "").replaceAll("\\s+", " ")

  def label(item: OWLIndividual, ontology: OWLOntology): String = item match {
    case named: OWLNamedIndividual => {
      val labelAnnotations = ontology.getAxioms(AxiomType.ANNOTATION_ASSERTION, true).filter(_.getSubject == named.getIRI).filter(_.getProperty.getIRI == label)
      labelAnnotations.headOption.map(_.getValue.toString).getOrElse(item.toString)
    }
    case _ => item.toString
  }

  class HTMLShortFormProvider(labelProvider: ShortFormProvider) extends ShortFormProvider {

    override def dispose(): Unit = {
      labelProvider.dispose()
    }

    def getShortForm(entity: OWLEntity): String =
      s"""<a href="${entity.getIRI.toString}" class="${entity.getEntityType.getName}">${labelProvider.getShortForm(entity)}</a>"""

  }

}