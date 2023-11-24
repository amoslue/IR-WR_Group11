import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class Parser {
    protected int uniqueEntries;
    protected String docTag;
    protected String[] tagsToExtract;
    protected String[] tagNames;
    protected String[] tagsToUnwrap;
    protected String[] IGNORE_FILES;

    protected String absFilePath;

    public String[][] loadDocs() throws IOException {
        List<String[]> docList = new ArrayList<>();
        List<File> files = listFiles(absFilePath);

        for (File file : files) {
            if (IGNORE_FILES != null) {
                if (isInIgnoreFiles(file)) {
                    continue;
                }
            }
            Document content = parseDoc(file);
            Elements docs = content.select(docTag);

            for (Element doc : docs) {
                if (tagsToUnwrap.length != 0) {
                    String unwrapTags = String.join(", ", tagsToUnwrap);
                    doc.select(unwrapTags).unwrap();
                }
                String entries[] = getEntries(doc);
                docList.add(getEntryArray(entries));
            }
        }
        String[][] docArray = new String[docList.size()][];
        docArray = docList.toArray(docArray);

        return docArray;
    }

    protected Document parseDoc(File file) throws IOException {
        return Jsoup.parse(file, null, "");
    }

    protected void setParserConfiguration(int uniqueEntries, String docTag, String[] tagsToExtract, String[] tagNames, String[] tagsToUnwrap, String[] IGNORE_FILES, String absFilePath) {
        this.uniqueEntries = uniqueEntries;
        this.docTag = docTag;
        this.tagsToExtract = tagsToExtract;
        this.tagNames = tagNames;
        this.tagsToUnwrap = tagsToUnwrap;
        this.IGNORE_FILES = IGNORE_FILES;
        this.absFilePath = absFilePath;
    }

    protected String[] getEntries(Element doc) {
        String result[] = new String[tagsToExtract.length];
        for (int i = 0; i < tagsToExtract.length; i++) {
            result[i] = (doc.select(tagsToExtract[i]).text());
        }
        return result;
    }

    private List<File> listFiles(String directory) {
        List<File> fileList = new ArrayList<>();
        File dir = new File(directory);

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileList.add(file);
                    } else if (file.isDirectory()) {
                        fileList.addAll(listFiles(file.getAbsolutePath()));
                    }
                }
            }
        }

        return fileList;
    }

    private boolean isInIgnoreFiles(File file) {
        for (String ignoreFileName : IGNORE_FILES) {
            if (file.getName().equalsIgnoreCase(ignoreFileName)) {
                return true;
            }
        }
        return false;
    }

    private String[] getEntryArray(String[] entries) {
        String entryArray[] = new String[uniqueEntries + 1];

        for (int i = 0; i < uniqueEntries; i++) {
            entryArray[i] = entries[i];
        }
        String metadata = "";
        for (int i = uniqueEntries; i < tagsToExtract.length; i++) {
            metadata += "\"" + tagNames[i] + "\":\"" + entries[i] + "\"";
        }
        entryArray[uniqueEntries] = metadata;
        return entryArray;
    }
}
