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
    private final static boolean REMAKE_INDEX = true;
    private final static String EVAL_DIR = "./target/evaluations";
    private final static String REPORT_DIR = "./target/reports";

    public static Similarity[] similarities = {
//        new ClassicSimilarity(),
        new BM25Similarity(),
//        new LMDirichletSimilarity(),
        new LMJelinekMercerSimilarity(0.5f),
        new BooleanSimilarity(),
//        new AxiomaticF1EXP(),
//        new AxiomaticF1LOG(),
//        new IndriDirichletSimilarity()
    };

    public static Analyzer[] analyzers = {
//        new StopAnalyzer(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET),
//        new SimpleAnalyzer(),
//        new EnglishAnalyzer(),
//        new KeywordAnalyzer(),
//        new WhitespaceAnalyzer(),
//        new StandardAnalyzer(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET)
        new YuanpeiCustomAnalyzer()
    };

    public static void main(String[] args) throws Exception {
//        Utils.emptyDirectory(EVAL_DIR);
//        Utils.emptyDirectory(REPORT_DIR);

        for (Analyzer analyzer : analyzers)
        {
            for (Similarity similarity : similarities)
            {
                System.out.println("\nAnalyzer: " + analyzer.getClass().getSimpleName() + ", Similarity: " + similarity.getClass().getSimpleName());

                if(REMAKE_INDEX)
                    IndexCreator.createIndex(analyzer, similarity);

                Searcher.rankSearch(analyzer, similarity);

                TrecEvalRunner.runTrecEval(analyzer, similarity);
            }
        }
    }
}
