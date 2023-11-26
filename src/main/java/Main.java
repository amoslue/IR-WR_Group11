import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.similarities.BM25Similarity;

import java.io.IOException;

public class Main {
    public static boolean REMAKE_INDEX = false;
    public static String EVAL_DIR = "target/eval";
    public static void main(String[] args) throws IOException, ParseException {
        if(REMAKE_INDEX){
            System.out.println("Running index creator...");
            IndexCreator indexCreator = new IndexCreator();
            indexCreator.createIndex();
            System.out.println("Running queryer...");
        }
        Search search = new Search();
        search.setSimilarity(new BM25Similarity());
        search.setAnalyzer(new EnglishAnalyzer());
        search.runSearch(EVAL_DIR);
    }
}
