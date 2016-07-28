import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.Quadruple;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Naga on 7/20/2016.
 */
public class CoreNLP {

    public static String returnTriplets(String sentence) {

        CoreNLP c = new CoreNLP();
        Document doc = new Document(sentence);
        String output = null;
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            String subject, predicate, object;

            Collection<Quadruple<String, String, String, Double>> l=sent.openie();

            Iterator<Quadruple<String, String, String, Double>> p = l.iterator();
            Quadruple<String, String, String, Double> s = p.next();
            String preSubject = s.first();
            String prePredicate = s.second();
            String preObject = s.third();
            String query;
            String originalPrefix = "http://www.kdm.com/OWL/elections2016#";
            String prefix = "PREFIX x: <" + originalPrefix + "> ";
            if(preSubject.equals("WR")){
                subject = "where";
                predicate = "currentLocation";
                object = c.returnLemma(preObject);
                query = prefix + "SELECT ?place WHERE { x:"+ object + " x:currentLocation ?place } ";
                output = c.QueryExecution(query, "place");
            }
            else if(preSubject.equals("WH")){
                subject = "who";
                predicate = "name";
                object = c.returnLemma(preObject);

                query = prefix + "SELECT ?name WHERE { x:"+ preObject + " x:name ?name } ";
                output = c.QueryExecution(query, "name");
//                query = prefix + "SELECT ?person WHERE { ?person x:isPresident x:" + object + " } ";
            }
            else if(preSubject.equals("YT")){
                subject = "what";
//                predicate
                object = c.returnLemma(preObject);
                query = prefix + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "Select ?type { x:" + object + " rdf:type ?type}";
                String output1 = c.QueryExecution(query, "type");
                String[] temp = output1.split(" ");
                for(int j=0; j<temp.length;j++){
                    if(temp[j].contains(originalPrefix)){
                        output = temp[j].replaceAll(originalPrefix, "");
                    }
                }
            }else if(preSubject.equals("ZT")) {
                subject = "when";
                predicate = "hasTimeDate";
                object = c.returnLemma(preObject).trim().replaceAll(" ", "_");
                query = prefix + "SELECT ?time WHERE { x:" + object + " x:hasTimeDate ?time } ";
                output = c.QueryExecution(query, "time");
            }
                else if(preSubject.equals("AS")){
                    subject = "what";
                    predicate = "asset";
                    //object = c.returnLemma(preObject).trim().replaceAll(" ", "_");
                    query = prefix + "SELECT ?asset WHERE { x:"+ preObject + " x:asset ?asset } ";
                    output = c.QueryExecution(query, "time");
            }else{
                //Ask Question
                subject = c.returnLemma(preSubject);
                object = c.returnLemma(preObject);
                query = prefix + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  ASK { x:" + subject  + " rdf:type x:" + object + " }";
                output = c.QueryExecution(query, "ASK");
            }
        }

        return output;
    }

    public String returnLemma(String sentence) {

        Document doc = new Document(sentence);
        String lemma="";
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences

            List<String> l=sent.lemmas();
            for (int i = 0; i < l.size() ; i++) {
                lemma+= l.get(i) +" ";
            }
            //   System.out.println(lemma);
        }

        return lemma;
    }

    public String QueryExecution(String queryString, String variable){
        OntModel ontoModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        InputStream in = FileManager.get().open("data/usaelection1.owl");
        ontoModel.read(in, null);
        Query query = QueryFactory.create(queryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, ontoModel);
        StringBuilder sb = new StringBuilder();
        if(variable.equals("ASK")){
            Boolean res = qexec.execAsk();
            sb.append(res);
        }else{
            ResultSet results = qexec.execSelect() ;
            if(results.hasNext()){
                for ( ; results.hasNext() ; ){
                    QuerySolution soln = results.nextSolution() ;
                    if(soln.get(variable)!=null){
                        sb.append(soln.get(variable));
                    }
                    else{
                        sb.append(soln.toString());
                    }
                    sb.append(" ");
                }
            }else{
                sb.append("null");
            }

        }
        return sb.toString();
    }

}
