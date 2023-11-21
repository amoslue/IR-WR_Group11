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

        for(Element doc : docs){
            String title, desc, narr;
            title = (doc.select("title").text());
            desc = (doc.select("desc").text());
            narr = (doc.select("narr").text());
            queryDocList.add(new String[]{title, desc, narr});
        }

        String[][] queryTopicArray = new String[queryDocList.size()][];
        queryTopicArray = queryDocList.toArray(queryTopicArray);

        return queryTopicArray;
    }
}
