import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexCreator {
    private static final String INDEX_DIR = "target/index/";

    //    input: 4 String arrays, output: 1 String array
    public static String[][] combineArrays(String[][] array1, String[][] array2, String[][] array3, String[][] array4) {
        int totalRows = array1.length + array2.length + array3.length + array4.length;
        int totalCols = 0;

        if (array1.length > 0) {
            totalCols = array1[0].length;
        }

        String[][] combinedArray = new String[totalRows][totalCols];

//        Copy each array into the combined array
        int currentRow = 0;
        for (String[][] array : new String[][][]{array1, array2, array3, array4}) {
            for (String[] row : array) {
                System.arraycopy(row, 0, combinedArray[currentRow], 0, totalCols);
                currentRow++;
            }
        }

        return combinedArray;
    }

    public static void createIndex(Analyzer analyzer, Similarity similarity) throws Exception {
        String analyzerName = analyzer.getClass().getSimpleName();
        String similarityName = similarity.getClass().getSimpleName();

        String outputDir = INDEX_DIR + analyzerName + "_" + similarityName;
        Utils.makeDir(INDEX_DIR);
        Directory directory = FSDirectory.open(Paths.get(outputDir));

//        will customize our analyzer later
//        Analyzer analyzer = new EnglishAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

//        customize similarity
        config.setSimilarity(similarity);

        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter iwriter = new IndexWriter(directory, config);

        FbisParser fbisParser = new FbisParser();
        FinTimeDocs finTimeDocs = new FinTimeDocs();
        Fr94Parser fr94Parser = new Fr94Parser();
        LATimesParser laTimesParser = new LATimesParser();
//        get parsed documents
        System.out.println("Loading documents from fbis...");
        String[][] parsedDocuments_FBIS = fbisParser.loadDocs();
        System.out.println("Loading documents from fr94...");
        String[][] parsedDocuments_FR94 = finTimeDocs.loadDocs();
        System.out.println("Loading documents from ft...");
        String[][] parsedDocuments_FT = fr94Parser.loadDocs();
        System.out.println("Loading documents from latimes...");
        String[][] parsedDocuments_LATIMES = laTimesParser.loadDocs();

//        combine all parsed documents
        String[][] parsedDocuments = combineArrays(parsedDocuments_FBIS, parsedDocuments_FR94, parsedDocuments_FT, parsedDocuments_LATIMES);

//         add documents to index
        System.out.println("Adding documents to index...");
        for (int i = 0; i < parsedDocuments.length; i++) {
//            System.out.println("title: " + parsedDocuments[i][0]);
//            System.out.println("text: " + parsedDocuments[i][1]);
//            System.out.println("metadata: " + parsedDocuments[i][2]);

            Document luceneDoc = new Document();

            luceneDoc.add(new StringField("id", parsedDocuments[i][0], Field.Store.YES));
            luceneDoc.add(new TextField("title", parsedDocuments[i][1], Field.Store.YES));
            luceneDoc.add(new TextField("text", parsedDocuments[i][2], Field.Store.YES));
            luceneDoc.add(new TextField("metadata", parsedDocuments[i][3], Field.Store.YES));

            iwriter.addDocument(luceneDoc);
        }

        iwriter.close();
        directory.close();

        System.out.println("Index created successfully!");
    }

    public void printEntry(String entry[]) {
        System.out.println("docno: " + entry[0]);
        System.out.println("title: " + entry[1]);
        System.out.println("text: " + entry[2]);
        System.out.println("metadata: " + entry[3]);
    }

    public static void main(String[] args) throws Exception {
        IndexCreator.createIndex(new EnglishAnalyzer(), new BM25Similarity());
    }
}
