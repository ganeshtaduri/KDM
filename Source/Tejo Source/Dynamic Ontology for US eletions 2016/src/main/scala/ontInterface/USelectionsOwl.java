package ontInterface;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class USelectionsOwl {
    OWLOntology ont;
    PrefixManager pm;
    OWLOntologyManager manager;
    OWLDataFactory df;

    public USelectionsOwl() {
        try {
            pm = new DefaultPrefixManager(null, null, "https://www.kdm.com/OWL/USelections#");
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
        OWLObjectProperty isIngredientOf = df.getOWLObjectProperty(propertyName, pm);
        OWLObjectPropertyRangeAxiom rangeAxiom = df.getOWLObjectPropertyRangeAxiom(isIngredientOf, rangeC);
        OWLObjectPropertyDomainAxiom domainAxiom = df.getOWLObjectPropertyDomainAxiom(isIngredientOf, domainC);
        manager.addAxiom(ont, rangeAxiom);
        manager.addAxiom(ont, domainAxiom);

    }

    public OWLOntology initialzation() throws Exception {
        //creating ontology manager
        //In order to create objects that represent entities we need a

        ont = manager.createOntology(IRI.create("https://www.kdm.com/OWL/", "USelections"));
        OWLClass Election = df.getOWLClass(":Election", pm);

        OWLDeclarationAxiom declarationAxiomElection = df.getOWLDeclarationAxiom(Election);

        manager.addAxiom(ont, declarationAxiomElection);

        //Making all classes Disjoint to each other

        //Creating Subclasses for Election class
        OWLClass CandidateElection = df.getOWLClass(":CandidateElection", pm);
        OWLClass PresidentElection = df.getOWLClass(":PresidentElection", pm);
        OWLClass VicePresidentElection = df.getOWLClass(":VicePresidentElection", pm);
        OWLSubClassOfAxiom declarationAxiomCandidateElection = df.getOWLSubClassOfAxiom(CandidateElection, Election);
        OWLSubClassOfAxiom declarationAxiomCandidatePresidentElection = df.getOWLSubClassOfAxiom(PresidentElection, Election);
        OWLSubClassOfAxiom declarationAxiomCandidateVicePresidentElection = df.getOWLSubClassOfAxiom(VicePresidentElection, Election);
        manager.addAxiom(ont, declarationAxiomCandidateElection);
        manager.addAxiom(ont, declarationAxiomCandidatePresidentElection);
        manager.addAxiom(ont, declarationAxiomCandidateVicePresidentElection);

        OWLDisjointClassesAxiom disjointClassesAxiom = df.getOWLDisjointClassesAxiom(CandidateElection, PresidentElection, VicePresidentElection);
        manager.addAxiom(ont, disjointClassesAxiom);



        //Creating Subclasses for CandidateElection class
        OWLClass DemocratCandidates = df.getOWLClass(":DemocratCandidates", pm);
        OWLClass RepublicanCandidates = df.getOWLClass(":RepublicanCandidates", pm);
        OWLSubClassOfAxiom declarationAxiomDemocratCandidates = df.getOWLSubClassOfAxiom(DemocratCandidates, CandidateElection);
        OWLSubClassOfAxiom declarationAxiomRepublicanCandidates = df.getOWLSubClassOfAxiom(RepublicanCandidates, CandidateElection);
        manager.addAxiom(ont, declarationAxiomDemocratCandidates);
        manager.addAxiom(ont, declarationAxiomRepublicanCandidates);

        //Creating Subclasses for DemocratCandidates class
        OWLClass HilaryClinton = df.getOWLClass(":HilaryClinton", pm);
        OWLClass BernieSanders = df.getOWLClass(":BernieSanders", pm);
        OWLSubClassOfAxiom declarationAxiomHilaryClinton = df.getOWLSubClassOfAxiom(HilaryClinton, DemocratCandidates);
        OWLSubClassOfAxiom declarationAxiomBernieSanders = df.getOWLSubClassOfAxiom(BernieSanders, DemocratCandidates);
        manager.addAxiom(ont, declarationAxiomHilaryClinton);
        manager.addAxiom(ont, declarationAxiomBernieSanders);


        //Creating Subclasses for RepublicanCandidates class
        OWLClass DonaldTrump = df.getOWLClass(":DonaldTrump", pm);
        OWLSubClassOfAxiom declarationAxiomDonaldTrump = df.getOWLSubClassOfAxiom(DonaldTrump, RepublicanCandidates);
        manager.addAxiom(ont, declarationAxiomDonaldTrump);

        //Creating Subclasses for PresidentElection class
        OWLClass Candidates = df.getOWLClass(":Candidates", pm);
        OWLClass Electors = df.getOWLClass(":Electors", pm);
        OWLSubClassOfAxiom declarationAxiomRedCandidates = df.getOWLSubClassOfAxiom(Candidates, PresidentElection);
        OWLSubClassOfAxiom declarationAxiomElectors = df.getOWLSubClassOfAxiom(Electors, PresidentElection);
        manager.addAxiom(ont, declarationAxiomRedCandidates);
        manager.addAxiom(ont, declarationAxiomElectors);

        //Creating ElectoralCollege
        OWLClass ElectoralCollegeMembers = df.getOWLClass(":ElectoralCollegeMembers", pm);
        OWLDeclarationAxiom declarationAxiomElectoralCollegeMembers= df.getOWLDeclarationAxiom(ElectoralCollegeMembers);
        manager.addAxiom(ont, declarationAxiomElectoralCollegeMembers);

        //Creating Object Properties
        OWLObjectProperty belongsTo = df.getOWLObjectProperty(":belongsTo", pm);
        OWLObjectPropertyRangeAxiom rangeAxiombelongsTo = df.getOWLObjectPropertyRangeAxiom(belongsTo, ElectoralCollegeMembers);
        OWLObjectPropertyDomainAxiom domainAxiombelongsTo = df.getOWLObjectPropertyDomainAxiom(belongsTo, ElectoralCollegeMembers);
        manager.addAxiom(ont, rangeAxiombelongsTo);
        manager.addAxiom(ont, domainAxiombelongsTo);

        OWLObjectProperty hasCandidates = df.getOWLObjectProperty(":hasCandidates", pm);
        OWLObjectPropertyRangeAxiom rangeAxiomhasCandidates = df.getOWLObjectPropertyRangeAxiom(hasCandidates,DemocratCandidates);
        OWLObjectPropertyDomainAxiom domainAxiomhasCandidates = df.getOWLObjectPropertyDomainAxiom(hasCandidates, DemocratCandidates);
        manager.addAxiom(ont, rangeAxiomhasCandidates);
        manager.addAxiom(ont, domainAxiomhasCandidates);

        //Making belongsTo and hasCandidates inverse properties
        manager.addAxiom(ont, df.getOWLInverseObjectPropertiesAxiom(belongsTo, hasCandidates));


        return ont;
    }

    public void saveOntology() {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OutputStream os = new FileOutputStream("data/USelection.owl");
            OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
            manager.saveOntology(ont, owlxmlFormat, os);
            System.out.println("Ontology Created");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
