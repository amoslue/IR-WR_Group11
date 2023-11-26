import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<File> listFiles(String directory) {
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

        //System.out.println("Found " + fileList.size() + " files in directory " + directory);
        return fileList;
    }

    public static void makeDir(String dirName) throws Exception{
        String directoryPath = dirName;
        File directory = new File(directoryPath);
        directory.mkdirs();
        if(!directory.exists()){
            throw new Exception("Directory " + dirName + " could not be created");
        }
    }

    public static void emptyDirectory(String dirName) {
        // Create a File object for the directory
        File directory = new File(dirName);

        // Check if the directory exists
        if (directory.exists() && directory.isDirectory()) {
            // List all files in the directory
            File[] files = directory.listFiles();

            // Delete each file
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            System.out.println("Deleted file: " + file.getAbsolutePath());
                        } else {
                            System.out.println("Failed to delete file: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
            System.out.println("Directory does not exist or is not a directory: " + directory.getAbsolutePath());
        }
    }
}
