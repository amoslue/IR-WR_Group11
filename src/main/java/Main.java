import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import java.io.IOException;

public class Main {
    public static boolean REMAKE_INDEX = false;
    public static boolean GENERATE_QRELS = true;
    public static boolean GENERATE_TREC_EVAL = true;
    public static String EVAL_DIR = "target/evaluation";
    public static String REPORT_DIR = "target/reports";

    public static Similarity[] similarityModels = {
        new ClassicSimilarity(),
        new BM25Similarity(),
        new LMDirichletSimilarity(),
        new LMJelinekMercerSimilarity(0.5f),
        new BooleanSimilarity(),
        new AxiomaticF1EXP(),
        new AxiomaticF1LOG(),
        new IndriDirichletSimilarity()
    };

    public static Analyzer[] analyzers = {
        new StopAnalyzer(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET),
        new SimpleAnalyzer(),
        new EnglishAnalyzer(),
        new KeywordAnalyzer(),
        new WhitespaceAnalyzer(),
        new StandardAnalyzer(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET)
    };

    public static void main(String[] args) throws Exception {
        Utils.makeDir(EVAL_DIR);
        Utils.makeDir(REPORT_DIR);
        Utils.emptyDirectory(EVAL_DIR);
        Utils.emptyDirectory(REPORT_DIR);

        if(REMAKE_INDEX){
            System.out.println("Running index creator...");
            IndexCreator indexCreator = new IndexCreator();
            indexCreator.createIndex();
            System.out.println("Running queryer...");
        }

        if(GENERATE_QRELS){
            Search search = new Search();
            search.rankSearch(analyzers, similarityModels);
        }

        if(GENERATE_TREC_EVAL){
            TrecEvalRunner.runTrecEval();
            TrecEvalRunner.rankQrels();
        }
    }
}
