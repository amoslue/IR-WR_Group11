
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.IntPoint;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FinTimeDocs {

    public static String[][] loadFinTimesDocs(String absPathToFinTimes) throws IOException {

        List<String[]> finTimesDocList = new ArrayList<>();

        File[] directories = new File(absPathToFinTimes).listFiles(File::isDirectory);

        for (File directory : directories) {
            File[] listOfFiles = directory.listFiles();
            for(File file : listOfFiles){
                Document finTimesContent = Jsoup.parse(file, null, "");


                Elements docs = finTimesContent.select("DOC");

                for(Element doc: docs) {
                    String docNo, headline, text,profile,byline;
                    docNo = (doc.select("DOCNO").text());
                    headline = (doc.select("HEADLINE").text());
                    profile =(doc.select("PROFILE").text());
                    byline =(doc.select("BYLINE").text());
                    text = (doc.select("TEXT").text());
                    String metadata = String.format("\"docNo\":\"%s\", \"profile\":\"%s\", \"byLine\":\"%s\"", docNo, profile, byline);
                    finTimesDocList.add(new String[]{headline,text,metadata});
                }
            }
        }

        // Convert the list to a 2D array
        String[][] finTimesDocArray = new String[finTimesDocList.size()][];
        finTimesDocArray = finTimesDocList.toArray(finTimesDocArray);

        return finTimesDocArray;
    }


}
