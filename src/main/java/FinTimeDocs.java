
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

public class FinTimeDocs extends Parser {

    private final int uniqueEntries = 3;
    private final String docTag = "DOC";
    private final String[] tagsToExtract = {"DOCNO", "HEADLINE", "TEXT", "PROFILE", "BYLINE"};
    private final String[] tagNames = {"docNo", "title", "content", "profile", "byline"};
    private final String[] tagsToUnwrap = {};
    private final String[] IGNORE_FILES = {"readfrcg", "readmeft"};

    private final String absFilePath = "./src/main/resources/Assignment Two/ft";

    FinTimeDocs() {
        setParserConfiguration(uniqueEntries, docTag, tagsToExtract, tagNames, tagsToUnwrap, IGNORE_FILES, absFilePath);
    }


}
