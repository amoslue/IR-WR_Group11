import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FbisParser {
    private final static String[] IGNORE_FILES = {"readchg.txt", "readmefb.txt"};
    public static String[][] loadFBISDocs() throws IOException {
        final String absPathToFBIS = "./src/main/resources/Assignment Two/fbis/";

        List<String[]> fbisDocList = new ArrayList<>();

        File[] files = new File(absPathToFBIS).listFiles(File::isFile);

        if(files == null){
            throw new IOException("FBIS parser error: No files at path");
        }

        for(File file : files){
            for(String ignoreFileName : IGNORE_FILES){
                if(file.getName().equalsIgnoreCase(ignoreFileName)){
                    continue;
                }
            }
            Document fbisContent = Jsoup.parse(file, null, "");

            Elements docs = fbisContent.select("DOC");

            for(Element doc: docs) {
                doc.select("H1, H2, H3, H4, H5, H6, H7, H8, FIG, TXT5, TR").unwrap();
                String title, text, date, docNo, collectionID, docID, cat1, cat2, cat3, info, lan, src;
                title        = (doc.select("TI").text());
                text         = (doc.select("TEXT").text());
                date         = (doc.select("DATE1").text());
                docNo        = (doc.select("DOCNO").text());
                collectionID = (doc.select("HT").text());
                docID        = (doc.select("AU").text());
                cat1         = (doc.select("F[P=100]").text());
                cat2         = (doc.select("F[P=101]").text());
                cat3         = (doc.select("ABS").text());
                info         = (doc.select("F[P=102]").text());
                lan          = (doc.select("F[P=105]").text());
                src          = (doc.select("F[P=106]").text());

                String metadata = String.format("\"date\":\"%s\", \"docNo\":\"%s\", \"collection ID\":\"%s\", \"document ID\":\"%s\", \"category 1\":\"%s\", \"category 2\":\"%s\", \"category 3\":\"%s\", \"additional information\":\"%s\", \"language\":\"%s\", \"source\":\"%s\"", date, docNo, collectionID, docID, cat1, cat2, cat3, info, lan, src);
                fbisDocList.add(new String[]{title,text,metadata});
            }
        }

        // Convert the list to a 2D array
        String[][] fbisDocArray = new String[fbisDocList.size()][];
        fbisDocArray = fbisDocList.toArray(fbisDocArray);

        return fbisDocArray;
    }
}
