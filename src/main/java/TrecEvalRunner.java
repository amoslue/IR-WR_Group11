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
        try {
//            ./trec_eval cranqry eval
            ProcessBuilder processBuilder = new ProcessBuilder(TREC_EVAL_PATH, QREL_PATH, EVAL_DIR);

            processBuilder.redirectOutput(new File(REPORT_DIR));
            Process process = processBuilder.start();
            process.waitFor();

            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
                runTrecEval();
    }
}
