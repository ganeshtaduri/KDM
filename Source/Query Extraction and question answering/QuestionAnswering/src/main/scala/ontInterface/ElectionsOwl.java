package ontInterface;

/**
 * Created by Ganesh Taduri on 7/18/2016.
 */

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class ElectionsOwl {
    OWLOntology ont;
    PrefixManager pm;
    OWLOntologyManager manager;
    OWLDataFactory df;


    public ElectionsOwl(){
        try {
            pm = new DefaultPrefixManager(null, null, "https://www.kdm.com/OWL/elections2016#");
            manager = OWLManager.createOWLOntologyManager();
            df = manager.getOWLDataFactory();
            ont = initialzation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSubClass(String className, String subClassName) {
        OWLClass baseClass = df.getOWLClass(className, pm);
        OWLClass subClass = df.getOWLClass(subClassName, pm);
        OWLSubClassOfAxiom declarationSubClassAxiom = df.getOWLSubClassOfAxiom(subClass, baseClass);
        manager.addAxiom(ont, declarationSubClassAxiom);
    }

    public void createClass(String className) {

        OWLClass classN = df.getOWLClass(className, pm);
        OWLDeclarationAxiom declarationAxiom = df.getOWLDeclarationAxiom(classN);
        manager.addAxiom(ont, declarationAxiom);

    }

    public void createIndividual(String individualName, String className) {
        OWLClass classN = df.getOWLClass(className, pm);
        OWLNamedIndividual ind = df.getOWLNamedIndividual(individualName, pm);
        OWLClassAssertionAxiom classAssertion = df.getOWLClassAssertionAxiom(classN, ind);
        manager.addAxiom(ont, classAssertion);

    }

    public void createObjectProperty(String domain, String propertyName, String range) {

        OWLClass domainC = df.getOWLClass(domain, pm);
        OWLClass rangeC = df.getOWLClass(range, pm);
        OWLObjectProperty isMemberof = df.getOWLObjectProperty(propertyName, pm);
        OWLObjectPropertyRangeAxiom rangeAxiom = df.getOWLObjectPropertyRangeAxiom(isMemberof, rangeC);
        OWLObjectPropertyDomainAxiom domainAxiom = df.getOWLObjectPropertyDomainAxiom(isMemberof, domainC);
        manager.addAxiom(ont, rangeAxiom);
        manager.addAxiom(ont, domainAxiom);

    }


    public OWLOntology initialzation() throws Exception {

        ont = manager.createOntology(IRI.create("https://www.kdm.com/OWL/", "elections2016#"));

        //creating class political parties
        OWLClass PoliticalParties = df.getOWLClass(":PoliticalParties", pm);
        OWLDeclarationAxiom declarationAxiomPolicalParties = df.getOWLDeclarationAxiom(PoliticalParties);
        manager.addAxiom(ont, declarationAxiomPolicalParties);

        //creating class president
        OWLClass Members = df.getOWLClass("President", pm);
        OWLDeclarationAxiom declarationAxiomMembers = df.getOWLDeclarationAxiom(Members);
        manager.addAxiom(ont, declarationAxiomMembers);


        //creating subclasses for Political Parties
        OWLClass DemocraticParty = df.getOWLClass(":DemocraticParty", pm);
        OWLClass RepublicParty = df.getOWLClass(":RepublicParty", pm);
        OWLSubClassOfAxiom declarationAxiomDemocraticParty = df.getOWLSubClassOfAxiom(DemocraticParty, PoliticalParties);
        OWLSubClassOfAxiom declarationAxiomRepublicParty = df.getOWLSubClassOfAxiom(RepublicParty, PoliticalParties);
        manager.addAxiom(ont, declarationAxiomDemocraticParty);
        manager.addAxiom(ont, declarationAxiomRepublicParty);

        //Declaring Democratic and Republic parties as disjoint classes
        OWLDisjointClassesAxiom disjointClassesAxiom = df.getOWLDisjointClassesAxiom(DemocraticParty,RepublicParty);
        manager.addAxiom(ont, disjointClassesAxiom);


        OWLClass DemocraticMembers = df.getOWLClass(":DemocraticMembers",pm);
        OWLSubClassOfAxiom declarationAxiomDemocraticMembers = df.getOWLSubClassOfAxiom(DemocraticMembers, DemocraticParty);
        manager.addAxiom(ont, declarationAxiomDemocraticMembers);

        //creating subclasses for Republic party
        OWLClass RepublicMembers = df.getOWLClass(":RepublicMembers",pm);
        OWLSubClassOfAxiom declarationAxiomRepublicMembers = df.getOWLSubClassOfAxiom(RepublicMembers, RepublicParty);
        manager.addAxiom(ont, declarationAxiomRepublicMembers);
        //creating subclasses for presidential candidates for democratic party
        OWLNamedIndividual HilaryClinton = df.getOWLNamedIndividual(":HilaryClinton", pm);
        OWLNamedIndividual Donaldtrump = df.getOWLNamedIndividual(":DonaldTrump", pm);

        //Class Assertion specifying members of class
        OWLClassAssertionAxiom classAssertionHilaryClinton = df.getOWLClassAssertionAxiom(DemocraticMembers, HilaryClinton);
        OWLClassAssertionAxiom classAssertionDonaldTrump = df.getOWLClassAssertionAxiom(RepublicMembers, Donaldtrump);
        manager.addAxiom(ont, classAssertionHilaryClinton);
        manager.addAxiom(ont, classAssertionDonaldTrump);

        //Creating object properties for political parties and its subclasses
        OWLObjectProperty hasParty = df.getOWLObjectProperty(":hasParty", pm);
        OWLObjectPropertyRangeAxiom rangeAxiomhasParty = df.getOWLObjectPropertyRangeAxiom(hasParty,DemocraticParty);
        OWLObjectPropertyDomainAxiom domainAxiomhasParty1 = df.getOWLObjectPropertyDomainAxiom(hasParty, PoliticalParties);
        OWLObjectPropertyDomainAxiom domainAxiomhasParty2 = df.getOWLObjectPropertyDomainAxiom(hasParty, PoliticalParties);
        manager.addAxiom(ont, rangeAxiomhasParty);
        manager.addAxiom(ont, domainAxiomhasParty1);
        manager.addAxiom(ont, domainAxiomhasParty1);

        //creating oject properties for democratinc and republic parties
        OWLObjectProperty hasDemocraticNominie = df.getOWLObjectProperty(":hasDemocraticNominie", pm);
        OWLObjectPropertyRangeAxiom rangeAxiomhasDemocraticNominie = df.getOWLObjectPropertyRangeAxiom(hasDemocraticNominie,DemocraticParty);
        OWLObjectPropertyDomainAxiom domainAxiomhasDemocraticNominie = df.getOWLObjectPropertyDomainAxiom(hasDemocraticNominie, DemocraticMembers);

        OWLObjectProperty hasRepublicNominie = df.getOWLObjectProperty(":hasRepublicNominie", pm);
        OWLObjectPropertyRangeAxiom rangeAxiomhasRepblicNominie = df.getOWLObjectPropertyRangeAxiom(hasRepublicNominie,RepublicParty);
        OWLObjectPropertyDomainAxiom domainAxiomhasRepublicNominie = df.getOWLObjectPropertyDomainAxiom(hasRepublicNominie, RepublicMembers);


        return ont;
    }

    public void saveOntology() {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OutputStream os = new FileOutputStream("data/ElectionsOwl.owl");
            OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
            manager.saveOntology(ont, owlxmlFormat, os);
            System.out.println("Ontology Created");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
