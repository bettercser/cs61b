package gitlet;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Blob implements Serializable {
    private String content;
    private String hash;
    private File filePath;

    public Blob(String content, String hash) {
        this.content = content;
        this.hash = hash;
        this.filePath = Utils.join(Repository.BLOB, hash);
    }

    public File getFilePath() {
        return filePath;
    }

    /**
     * save blob
     */
    public void saveBlob() {
        if (!filePath.exists()) {
            try {
                filePath.createNewFile();
                Utils.writeContents(filePath, content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getBlobContents(String hashName) {
        String content = "";
        File file = new File(Repository.BLOB, hashName);
        if (file.exists()) {
            content = Utils.readContentsAsString(file);
        }
        return content;
    }


}
