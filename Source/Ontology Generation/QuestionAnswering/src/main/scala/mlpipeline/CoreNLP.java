package mlpipeline;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.Quadruple;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Mayanka on 27-Jun-16.
 */
public class CoreNLP {
    public static String returnLemma(String sentence) {

        Document doc = new Document(sentence);
        String lemma = "";
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences

            List<String> l = sent.lemmas();
            for (int i = 0; i < l.size(); i++) {
                lemma += l.get(i) + " ";
            }
            //   System.out.println(lemma);
        }

        return lemma;
    }

    public static String[] returnOpenIE(String sentence) {

        Document doc = new Document(sentence);
        String[] lemma = new String[doc.sentences().size()];
        int i = 0;
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences

            Iterator<Quadruple<String, String, String, Double>> l = sent.openie().iterator();
            if (l.hasNext()) {
                Quadruple<String, String, String, Double> ll = l.next();
                lemma[i] = ll.first + ";" + ll.second + ";" + ll.third;
                i++;
            }
        }

        return lemma;
    }

    public static String returnNER(String sentence) {

        Document doc = new Document(sentence);
        String lemma = "";
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences

            List<String> l = sent.nerTags();
            for (int i = 0; i < l.size(); i++) {
                lemma += l.get(i) + " ";
            }
            //   System.out.println(lemma);
        }

        return lemma;
    }


}
