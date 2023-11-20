import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fr94_parser {

    public static void main(String[] args) {
        try {
            String[][] documentsArray = parseDocuments();
            for (String[] document : documentsArray) {
                System.out.println("Document: " + document[0]); 
                System.out.println("Title: " + document[1]); 
                System.out.println("Content: " + document[2]); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[][] parseDocuments() throws IOException {
        File[] files = new File("fr94/").listFiles();
        if (files == null) {
            throw new IOException("Directory not found or is empty");
        }

        ArrayList<String[]> documentsData = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    try {
                        Document doc = Jsoup.parse(f, "UTF-8", "");
                        Elements docs = doc.select("doc");

                        for (Element e : docs) {
                            String docNo = e.getElementsByTag("Docno").text();
                            String title = e.getElementsByTag("USDEPT").text();
                            if (title.isEmpty()) {
                                title = e.getElementsByTag("USBUREAU").text();
                            }
                            String content = e.getElementsByTag("SUMMARY").text();
                            documentsData.add(new String[]{docNo, title, content});
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return documentsData.toArray(new String[0][]);
    }
}