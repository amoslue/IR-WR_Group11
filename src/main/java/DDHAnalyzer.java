import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.TokenStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DDHAnalyzer extends Analyzer {

    private final Set<String> customStopwords;

    private final String stopwordFilePath = ".src/main/resources/stopwords.txt";

    public DDHAnalyzer() throws IOException {
        // Initialize a HashSet for custom stopwords
        customStopwords = loadCustomStopwords(stopwordFilePath);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        // Use the StandardTokenizer for tokenization
        Tokenizer tokenizer = new StandardTokenizer();

        // Apply custom filters
        TokenStream tokenStream = new LowerCaseFilter(tokenizer);

        // Add SynonymGraphFilter for synonym handling (you need to implement your synonym map)
        tokenStream = new SynonymGraphFilter(tokenStream, createSynonymMap(), true);

        // Lowercase filter
        tokenStream = new LowerCaseFilter(tokenStream);

        // Stopword filter using custom stopwords
        tokenStream = new StopFilter(tokenStream, new CharArraySet(customStopwords, true));

        // Optionally, add additional filters for stemming or other custom transformations

        return new TokenStreamComponents(tokenizer, tokenStream);
    }

    // Implement your custom synonym map
    private SynonymMap createSynonymMap() {
        // Implement your synonym map creation logic here
        // Return a SynonymMap instance
        // Example:
        // SynonymMap.Builder builder = new SynonymMap.Builder(true);
        // builder.add(new CharsRef("word1"), new CharsRef("word2"), true);
        // return builder.build();
        return null; // Replace with your implementation
    }

    // Load custom stopwords from a file
    private Set<String> loadCustomStopwords(String stopwordFilePath) throws IOException {
        Set<String> stopwords = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(stopwordFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopwords.add(line.trim());
            }
        }
        return stopwords;
    }
}
