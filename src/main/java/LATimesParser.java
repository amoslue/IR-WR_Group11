
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LATimesParser {

    public static String[][] loadLaTimesDocs() throws IOException {
        final String pathToLATimesRegister = "./src/main/resources/Assignment Two/latimes/";

        List<String[]> parsedLADocsList = new ArrayList<>();

        File folder = new File(pathToLATimesRegister);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {

            Document laTimesContent = Jsoup.parse(file, null, "");

            Elements docs = laTimesContent.select("DOC");

            for(Element doc: docs) {
                String docNo, headline, text, docID;
                docNo = (doc.select("DOCNO").text());
                docID = (doc.select("DOCID").text());

                String metadata = String.format("\"docNo\":\"%s\", \"docID\":\"%s\"", docNo, docID);

                headline = (doc.select("HEADLINE").select("P").text());
                text = (doc.select("TEXT").select("P").text());
                parsedLADocsList.add(new String[]{metadata,headline, text});
            }
        }
        String[][] LATimeDocArray = new String[parsedLADocsList.size()][];
        LATimeDocArray = parsedLADocsList.toArray(LATimeDocArray);
        return LATimeDocArray;
    }

    private static org.apache.lucene.document.Document createDocument(
            String docNo, String docID, String headline,String text) {
        org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
        document.add(new StringField("docID", docID, Field.Store.YES));
        document.add(new StringField("docno", docNo, Field.Store.YES));
        document.add(new TextField("headline", headline, Field.Store.YES) );
        document.add(new TextField("text", text, Field.Store.YES) );
        return document;
    }
}


