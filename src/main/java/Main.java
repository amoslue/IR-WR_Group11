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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final static boolean REMAKE_INDEX = true;
    private final static String EVAL_DIR = "./target/evaluations";
    private final static String REPORT_DIR = "./target/reports";

    public static Similarity[] similarities = {
            new BM25Similarity(),
            new LMDirichletSimilarity(),
            new LMJelinekMercerSimilarity(0.5f),
            new AxiomaticF1EXP(),
            new AxiomaticF1LOG(),
            new IndriDirichletSimilarity(),
            new IBSimilarity(new DistributionSPL(), new LambdaDF(), new NormalizationH1()),
            new DFRSimilarity(new BasicModelIn(), new AfterEffectL(), new NormalizationH2())
    };

    public static Analyzer[] analyzers = {
            new StopAnalyzer(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET),
            new YuanpeiCustomAnalyzer(),
            new EnglishAnalyzer(),
            new KeywordAnalyzer(),
            new StandardAnalyzer()
    };

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        List<Analyzer> selectedAnalyzers = new ArrayList<>();
        List<Similarity> selectedSimilarities = new ArrayList<>();

        // Allow the user to select multiple analyzers
        System.out.println("Analyzers:");
        for (int i = 0; i < analyzers.length; i++) {
            System.out.println(i + ". " + analyzers[i].getClass().getSimpleName());
        }
        System.out.print("Select analyzers (Enter the numbers separated by spaces): ");
        String analyzerSelection = scanner.nextLine();
        String[] analyzerIndexes = analyzerSelection.split(" ");
        for (String index : analyzerIndexes) {
            int selectedAnalyzerIndex = Integer.parseInt(index);
            selectedAnalyzers.add(analyzers[selectedAnalyzerIndex]);
        }

        // Allow the user to select multiple similarities
        System.out.println("Similarities: ");
        for (int i = 0; i < similarities.length; i++) {
            System.out.println(i + ". " + similarities[i].getClass().getSimpleName());
        }
        System.out.print("Select Similarities (Enter the numbers separated by spaces): ");
        String similaritySelection = scanner.nextLine();
        String[] similarityIndices = similaritySelection.split(" ");
        for (String index : similarityIndices) {
            int selectedSimilarityIndex = Integer.parseInt(index);
            selectedSimilarities.add(similarities[selectedSimilarityIndex]);
        }

        for (Analyzer selectedAnalyzer : selectedAnalyzers) {
            for (Similarity selectedSimilarity : selectedSimilarities) {
                System.out.println("\nAnalyzer: " + selectedAnalyzer.getClass().getSimpleName() +
                        ", Similarity: " + selectedSimilarity.getClass().getSimpleName());

                if (REMAKE_INDEX)
                    IndexCreator.createIndex(selectedAnalyzer, selectedSimilarity);

                Searcher.rankSearch(selectedAnalyzer, selectedSimilarity);

                TrecEvalRunner.runTrecEval(selectedAnalyzer, selectedSimilarity);
            }
        }

        scanner.close();
    }
}
