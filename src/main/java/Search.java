import org.apache.lucene.analysis.Analyzer;
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

public class Search {
    private static final int MAX_RESULTS = 1000;
    private static final String INDEX_DIR = "target/index";

    IndexReader indexReader;
    IndexSearcher indexSearcher;
    Analyzer analyzer;
    Similarity similarity;
    MultiFieldQueryParser parser;
    String[][] topics;


    Search() throws IOException {
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));
        indexReader = DirectoryReader.open(directory);
        indexSearcher = new IndexSearcher(indexReader);
        topics = TopicParser.loadQueries();
    }

    public void setSimilarity(Similarity _similarity) {
        similarity = _similarity;
        indexSearcher.setSimilarity(similarity);
    }

    public void setAnalyzer(Analyzer _analyzer) {
        analyzer = _analyzer;
        String[] fields = {"title", "text", "metadata"};
        parser = new MultiFieldQueryParser(fields, analyzer);
    }

    public void runSearch(String evalFileName) throws IOException, ParseException {
        for (String[] topic : topics) {
            TopDocs results = getQueryResults(topic);
            saveToEval(results.scoreDocs, evalFileName, topic[0]);
        }
    }

    public void rankSearch(Analyzer[] analyzers, Similarity[] similarities) throws IOException, ParseException{
        for(Analyzer analyzer : analyzers){
            for(Similarity similarity : similarities){
                String analyzerName = analyzer.toString().replace(" ", "").replaceAll("\\(.*?\\)", "");
                String similarityName = similarity.toString().replace(" ", "").replaceAll("\\(.*?\\)", "");
                setAnalyzer(analyzer);
                setSimilarity(similarity);
                System.out.println("Using " + similarityName + " and " + analyzerName);
                String evalPath = "./target/evaluation/" + analyzerName + similarityName;
                runSearch(evalPath);
            }
        }
    }

    private TopDocs getQueryResults(String[] topic) throws IOException, ParseException {
        String topicString = topic[1] + " " + topic[2] + " " + topic[3];
        Query query = parser.parse(QueryParser.escape(topicString));
        return indexSearcher.search(query, MAX_RESULTS);
    }

    private void saveToEval(ScoreDoc[] scoreDocs, String evalDir, String queryNumber) throws IOException{
        try (BufferedWriter evalWriter = new BufferedWriter(new FileWriter(evalDir, true))) {
            int i = 0;
            for(ScoreDoc scoreDoc : scoreDocs){
                Document doc = indexSearcher.storedFields().document(scoreDoc.doc);
                float score = scoreDoc.score;
                String docIndex = doc.get("id");
                evalWriter.write( queryNumber + " 0 " + docIndex + " " +  i + " " + score + " Any\n");
                i++;
            }
        }
    }
}
