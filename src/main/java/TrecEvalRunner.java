import jdk.jshell.execution.Util;
import org.apache.lucene.search.similarities.Similarity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TrecEvalRunner {

    private static final String TREC_EVAL_PATH = "./src/main/resources/trec_eval-9.0.7/trec_eval";
    private static final String QREL_PATH = "./src/main/resources/qrels.assignment2.part1";
    private static final String EVAL_DIR = "./target/evaluation/";
    private static final String REPORT_DIR = "./target/reports/";
    public static void runTrecEval() {
        List<File> evalFiles = Utils.listFiles(EVAL_DIR);
        for(File file : evalFiles){
            String fileName = file.getName();
            Bash(TREC_EVAL_PATH + " " + QREL_PATH + " " + EVAL_DIR + fileName, REPORT_DIR + fileName);
        }
    }

    public static void rankQrels(){
        List<String> FileNames = new ArrayList<>();
        List<Float> MapScores = new ArrayList<>();

        List<File> qrels = Utils.listFiles(REPORT_DIR);

        for(File qrel : qrels){
            String qrelAbsolutePath = qrel.getAbsolutePath();
            String qrelName = qrel.getName();
            float mapValue = getMapValue(qrelAbsolutePath);

            int index = 0;
            for (int i = 0; i < MapScores.size(); i++) {
                if (mapValue < MapScores.get(i)) {
                    index = i + 1;
                } else {
                    break;
                }
            }
            MapScores.add(index, mapValue);
            FileNames.add(index, qrelName);
        }

        Float[] mapScoreArray = MapScores.toArray(new Float[qrels.size()]);
        String[] fileNamesArray = FileNames.toArray(new String[qrels.size()]);
        printRanking(mapScoreArray, fileNamesArray);
    }

    private static void printRanking(Float[] mapScores, String[] filenames){
        System.out.println("\n\nFINAL RANKINGS\n");
        System.out.println("Rank | Map Score | Name");
        for (int i = 0; i < mapScores.length; i++) {
            System.out.println((i + 1) + " | " + mapScores[i] + " | " + filenames[i]);
        }
    }

    public static float getMapValue(String filePath) {
        float mapValue = -1.0f;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("map")) {
                    int indexOfZero = line.indexOf("0");
                    String result = line.substring(indexOfZero);
                    mapValue = Float.parseFloat(result);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapValue;
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
