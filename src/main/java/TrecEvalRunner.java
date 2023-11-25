import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TrecEvalRunner {

    private static final String TREC_EVAL_PATH = "./src/main/resources/trec_eval-9.0.7/trec_eval";
    private static final String QREL_PATH = "./src/main/resources/qrels.assignment2.part1";
    private static final String EVAL_DIR = "./target/evaluation";
    private static final String REPORT_DIR = "./target/reports";
    private static void runTrecEval() {
        Bash(TREC_EVAL_PATH + " " + QREL_PATH + " " + EVAL_DIR, REPORT_DIR); // Run trec_eval on the similarity scores
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

    public static void main(String[] args) throws IOException {
                runTrecEval();
    }
}
