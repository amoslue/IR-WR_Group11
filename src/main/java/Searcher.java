import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Objects;

public class Searcher {
    private static final int MAX_RESULTS = 1000;

    private static final String INDEX_DIR = "target/index/";

    private static final String EVAL_DIR = "target/evaluations/";

    public static void rankSearch(Analyzer analyzer, Similarity similarity) throws Exception {
//        Objects.requireNonNull(analyzer, "Analyzer cannot be null");
//        Objects.requireNonNull(similarity, "Similarity cannot be null");

//        Searcher searcher = new Searcher(analyzer, similarity);
//        searcher.runSearch(EVAL_DIR + analyzer.toString() + "_" + similarity.toString());

        String analyzerName = analyzer.getClass().getSimpleName();
        String similarityName = similarity.getClass().getSimpleName();

        System.out.println("Running search for " + analyzerName + " and " + similarityName + "...");

        Directory directory = FSDirectory.open(Paths.get(INDEX_DIR + analyzerName + "_" + similarityName));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        indexSearcher.setSimilarity(similarity);
        //String[] fields = {"title", "text", "metadata"};

        String[] fields = {"title^4", "text"}; //Modify 1
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
        String[][] topics = TopicParser.loadQueries();

        String evalFileName = EVAL_DIR + analyzerName + "_" + similarityName;

        for (String[] topic : topics) {
//            TopDocs results = getQueryResults(topic);
//            saveToEval(results.scoreDocs, evalFileName, topic[0]);
            //System.out.println(topic);
            //String topicString = topic[1] + " " + topic[2] + " " + topic[3];
            String topicString = topic[1] + " " + topic[2]; // Modify 2
            //System.out.println(topicString);

            Query query = parser.parse(QueryParser.escape(topicString));
            TopDocs results = indexSearcher.search(query, MAX_RESULTS);

            Utils.makeDir(EVAL_DIR);

            try (BufferedWriter evalWriter = new BufferedWriter(new FileWriter(evalFileName, true))) {
                int i = 0;
                for (ScoreDoc scoreDoc : results.scoreDocs) {
                    Document doc = indexSearcher.storedFields().document(scoreDoc.doc);
                    float score = scoreDoc.score;
                    String docIndex = doc.get("id");
                    evalWriter.write(topic[0] + " 0 " + docIndex + " " + i + " " + score + " STANDARD\n");
                    i++;
                }
            }

        }
    }

    public static void main(String[] args) throws Exception {
        Searcher.rankSearch(new EnglishAnalyzer(), new BM25Similarity());
    }
}
