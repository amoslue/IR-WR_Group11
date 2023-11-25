import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TopicParser {
    public static String[][] loadQueries() throws IOException{
        final String absQueryPath = "./src/main/resources/topics";

        List<String[]> queryDocList = new ArrayList<>();

        File topicFile = new File(absQueryPath);

        if(topicFile == null){
            throw new IOException("Query Parser Error: File not found");
        }

        Document queryContent = Jsoup.parse(topicFile, null, "");

        Elements docs = queryContent.select("top");

        // extract fields from that wierd ahh format with no closing DOMs :/
        for(Element doc : docs){
            String docText = doc.text();
            String title = doc.select("title").text().trim();
            String number = extractContent(docText, "Number:", title);
            String description = extractContent(docText, "Description:", "Narrative:");
            String narrative = doc.select("narr").text().replace("Narrative:", "").trim();
            queryDocList.add(new String[]{number, title, description, narrative});
        }

        String[][] queryTopicArray = new String[queryDocList.size()][];
        queryTopicArray = queryDocList.toArray(queryTopicArray);

        return queryTopicArray;
    }

    private static String extractContent(String text, String startTag, String endTag) {
        int start = text.indexOf(startTag) + startTag.length();
        int end = text.indexOf(endTag, start);
        if (start < startTag.length() || end == -1) {
            return "";
        }
        return text.substring(start, end).trim();
    }
}
