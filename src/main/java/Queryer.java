import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Queryer {
    private static final int MAX_RESULTS = 50;
    private static final String INDEX_DIR = "target/index";
    private static final String OUTPUT_DIR = "target/query_results";
    private static final String EVAL_DIR = "target/evaluation";


    //    query with analyzer and score with similarities
    public static void query() throws IOException, ParseException {
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

//        create analyzer
        Analyzer analyzer = new EnglishAnalyzer();

        // MultiFieldQueryParser for querying multiple fields
        String[] fields = {"title", "text", "metadata"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);

//      get queries
        System.out.println("Loading queries...");
        String[][] queries = TopicParser.loadQueries();

        // write results to file
        try (BufferedWriter resultWriter = new BufferedWriter(new FileWriter(OUTPUT_DIR))) {

            int queryNumber = 1;  // for tracking query number

            System.out.println("Gnerating query results and evaluation...");

            for (String[] qry : queries) {
                String q = qry[1] + " " + qry[2] + " " + qry[3];

                Query query = parser.parse(QueryParser.escape(q));

                resultWriter.write("Query: " + query.toString() + "\n");


                searcher.setSimilarity(new BM25Similarity());
                TopDocs results = searcher.search(query, MAX_RESULTS);

                resultWriter.write("Total Hits: " + results.totalHits + "\n");
                resultWriter.write("Top " + MAX_RESULTS + " results:\n");

                String evalFilePath = EVAL_DIR;

                // renew file for the first write
                if (queryNumber == 1) {
                    Files.deleteIfExists(Paths.get(evalFilePath));
                }

                try (BufferedWriter evalWriter = new BufferedWriter(new FileWriter(evalFilePath, true))) {
                    int i = 0;
                    for(ScoreDoc scoreDoc : results.scoreDocs){
                        Document doc = searcher.storedFields().document(scoreDoc.doc);
                        float score = scoreDoc.score;
                        String docIndex = doc.get("id");
                        resultWriter.write("Doc ID: " + scoreDoc.doc + ", Score: " + score + "\n");
                        evalWriter.write( qry[0] + " 0 " + docIndex + " " +  i + " " + score + " Any\n");
                        i++;
                    }
                }
                resultWriter.write("\n");
                queryNumber++;  // increase query number
            }
        }

        reader.close();

        System.out.println("Querying finished!");
    }

    public static void main(String[] args) throws IOException, ParseException {
        Queryer.query();
    }
}