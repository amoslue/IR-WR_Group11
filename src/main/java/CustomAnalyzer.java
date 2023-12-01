import org.apache.lucene.analysis.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.core.FlattenGraphFilter;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;
import org.tartarus.snowball.ext.EnglishStemmer;


public class CustomAnalyzer extends StopwordAnalyzerBase {
    private final Path currentRelativePath = Paths.get("").toAbsolutePath();

    @Override
    protected TokenStreamComponents createComponents(String s) {
        final StandardTokenizer tokenizer = new StandardTokenizer();

        TokenStream tokenStream = tokenizer;
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new EnglishPossessiveFilter(tokenStream);

        tokenStream = new TrimFilter(tokenStream);

        //tokenStream = new EnglishMinimalStemFilter(tokenStream);
        tokenStream = new KStemFilter(tokenStream);
        tokenStream = new PorterStemFilter(tokenStream);

        tokenStream = new FlattenGraphFilter(new WordDelimiterGraphFilter(tokenStream,
                WordDelimiterGraphFilter.SPLIT_ON_NUMERICS |
                        WordDelimiterGraphFilter.GENERATE_WORD_PARTS |
                        WordDelimiterGraphFilter.GENERATE_NUMBER_PARTS |
                        WordDelimiterGraphFilter.PRESERVE_ORIGINAL , null));


        tokenStream = new StopFilter(tokenStream, StopFilter.makeStopSet(createStopWordList(),true));
        tokenStream = new SnowballFilter(tokenStream, new EnglishStemmer());

        return new TokenStreamComponents(tokenizer, tokenStream);
    }

    private SynonymMap createSynonymMap() {
        SynonymMap synMap = new SynonymMap(null, null, 0);
        try {
            BufferedReader countries = new BufferedReader(new FileReader(currentRelativePath + "/src/main/resources/Assignment Two/countries.txt"));

            final SynonymMap.Builder builder = new SynonymMap.Builder(true);
            String country = countries.readLine();

            while(country != null) {
                builder.add(new CharsRef(country), new CharsRef("country"), true);
                builder.add(new CharsRef(country), new CharsRef("countries"), true);
                country = countries.readLine();
            }

            synMap = builder.build();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getLocalizedMessage() + "occurred when trying to create synonym map");
        }
        return synMap;
    }

    private List<String> createStopWordList()
    {
        ArrayList<String> stopWordList = new ArrayList<>();
        try {
            BufferedReader stopwords = new BufferedReader(new FileReader(currentRelativePath + "/src/main/resources/Assignment Two/stop_words_english.txt"));
            String word = stopwords.readLine();
            while(word != null) {
                stopWordList.add(word);
                word = stopwords.readLine();
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getLocalizedMessage() + "occurred when trying to create stopword list");
        }
        return stopWordList;
    }


}