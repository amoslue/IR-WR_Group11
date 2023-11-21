import org.apache.lucene.queryparser.classic.ParseException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Running index creator...");
        IndexCreator indexCreator = new IndexCreator();
        indexCreator.creatIndex();
        System.out.println("Running queryer...");
        Queryer.query();
    }
}
