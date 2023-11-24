public class LATimesParser extends Parser {

    private final int uniqueEntries = 3;
    private final String docTag = "DOC";
    private final String[] tagsToExtract = {"DOCNO", "HEADLINE", "TEXT", "DOCID"};
    private final String[] tagNames = {"docNo", "title", "content", "docID"};
    private final String[] tagsToUnwrap = {};
    private final String[] IGNORE_FILES = {"readchg.txt", "readmela.txt"};

    private final String absFilePath = "./src/main/resources/Assignment Two/latimes/";


    LATimesParser() {
        setParserConfiguration(uniqueEntries, docTag, tagsToExtract, tagNames, tagsToUnwrap, IGNORE_FILES, absFilePath);
    }
}


