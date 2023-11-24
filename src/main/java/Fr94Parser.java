import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

public class Fr94Parser extends Parser {
    private final int uniqueEntries = 3;
    private final String docTag = "doc";
    private final String[] tagsToExtract = {"docno", "usdept", "summary"};
    private final String[] tagNames = {"docNo", "title", "content"};
    private final String[] tagsToUnwrap = {"readchg", "readmefr"};

    private final String absFilePath = "./src/main/resources/Assignment Two/fr94/";


    Fr94Parser() {
        setParserConfiguration(uniqueEntries, docTag, tagsToExtract, tagNames, tagsToUnwrap, IGNORE_FILES, absFilePath);
    }

    @Override
    protected Document parseDoc(File file) throws IOException {
        return Jsoup.parse(file, "UTF-8", "");
    }

    @Override
    protected String[] getEntries(Element doc) {
        String result[] = new String[tagsToExtract.length];
        for (int i = 0; i < tagsToExtract.length; i++) {
            if (i == 1) {
                if ((doc.select(tagsToExtract[i]).text()).isEmpty()) {
                    result[i] = (doc.select("usbureau").text());
                }
            }
            result[i] = (doc.select(tagsToExtract[i]).text());
        }
        return result;
    }
}