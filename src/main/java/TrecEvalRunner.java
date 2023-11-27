import jdk.jshell.execution.Util;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TrecEvalRunner {
    private static final String TREC_EVAL_PATH = "./src/main/resources/trec_eval-9.0.7/trec_eval";
    private static final String QREL_PATH = "./src/main/resources/qrels.assignment2.part1";
    private static final String EVAL_DIR = "./target/evaluations/";
    private static final String REPORT_DIR = "./target/reports/";
    public static void runTrecEval(Analyzer analyzer, Similarity similarity) throws Exception {
        Utils.makeDir(REPORT_DIR);
        String analyzerName = analyzer.getClass().getSimpleName();
        String similarityName = similarity.getClass().getSimpleName();
        Bash(TREC_EVAL_PATH + " " + QREL_PATH + " " + EVAL_DIR + analyzerName + "_" + similarityName, REPORT_DIR + analyzerName + "_" + similarityName);
    }

    // Execute bash command and record its inputs into file
    public static void Bash(String command, String outputFilePath) {
        System.out.println("Executing " + command + " ...");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            File outputFile = new File(outputFilePath);
            processBuilder.redirectOutput(outputFile);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Test executed successfully (" + outputFilePath + ")");
            } else {
                System.out.println("Test failed with exit code " + exitCode + " (" + outputFilePath + ")");
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        runTrecEval(new EnglishAnalyzer(), new BM25Similarity());
    }
}
