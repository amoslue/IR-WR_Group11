import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexCreator {
    private static final String INDEX_DIR = "target/indexes";

//    input: 4 String arrays, output: 1 String array
    public String[][] combineArrays(String[][] array1, String[][] array2, String[][] array3, String[][] array4) {
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

    public void creatIndex() throws IOException {
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIR + "/index"));

//        will customize our analyzer later
        Analyzer analyzer = new EnglishAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

//        customize similarity
        config.setSimilarity(new BM25Similarity());

        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter iwriter = new IndexWriter(directory, config);

//        get parsed documents
//        String[][] parsedDocuments_FBIS = FBIS_Parser.parse();
//        String[][] parsedDocuments_FR94 = FT94_Parser.parse();
//        String[][] parsedDocuments_FT = FT_Parser.parse();
//        String[][] parsedDocuments_LATIMES = LATIMES_Parser.parse();

//        combine all parsed documents
//        String[][] parsedDocuments = combineArrays(parsedDocuments_FBIS, parsedDocuments_FR94, parsedDocuments_FT, parsedDocuments_LATIMES);

//         add documents to index
//        for (int i = 0; i < parsedDocuments.length; i++) {
//            Document luceneDoc = new Document();
//
//            luceneDoc.add(new TextField("id", documentFields[i][0], Field.Store.YES));
//            luceneDoc.add(new TextField("title", documentFields[i][1], Field.Store.YES));
//            luceneDoc.add(new TextField("author", documentFields[i][2], Field.Store.YES));
//            luceneDoc.add(new TextField("text", documentFields[i][3], Field.Store.YES));
//
//            iwriter.addDocument(luceneDoc);
//        }

        iwriter.close();
        directory.close();
    }
}
