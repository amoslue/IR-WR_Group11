import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FbisParser extends Parser {
    private final int uniqueEntries = 3;
    private final String docTag = "DOC";
    private final String[] tagsToExtract = {"DOCNO", "TI", "TEXT", "DATE1", "HT", "AU", "F[P=100]", "F[P=101]", "ABS", "F[P=102]", "F[P=105]", "F[P=106]"};
    private final String[] tagNames = {"docNo", "title", "content", "date", "collection ID", "document ID", "category 1", "category 2", "category 3", "additional information", "language", "source"};
    private final String[] tagsToUnwrap = {"H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8", "FIG", "TXT5", "TR"};
    private final String[] IGNORE_FILES = {"readchg.txt", "readmefb.txt"};

    private final String absFilePath = "./src/main/resources/Assignment Two/fbis/";


    FbisParser() {
        setParserConfiguration(uniqueEntries, docTag, tagsToExtract, tagNames, tagsToUnwrap, IGNORE_FILES, absFilePath);
    }
}
