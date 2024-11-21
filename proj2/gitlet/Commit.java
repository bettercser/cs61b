package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author hu
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    private Date date;
    /** The message of this Commit. */
    private String message;
    private String directParentHash;
    private String otherParentHash;
    private HashMap<String, String> blobsMap;
    /* TODO: fill in the rest of this class. */

    public Commit(Date date, String message, String directParentHash, String blobFilename, String blobHas) {
        this.date = date;
        this.message = message;
        this.directParentHash = directParentHash;
        this.blobsMap = new HashMap<>();
        if(blobFilename != null || blobHas != null) {
            blobsMap.put(blobFilename, blobHas);
        }
    }

    public Commit(Commit directParentCommit) {
        this.date = directParentCommit.date;
        this.message = directParentCommit.message;
        this.directParentHash = directParentCommit.directParentHash;
        this.blobsMap = new HashMap<>(directParentCommit.blobsMap);

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDirectParentHash() {
        return directParentHash;
    }

    public void setDirectParentHash(String directParentHash) {
        this.directParentHash = directParentHash;
    }

    public String getOtherParentHash() {
        return otherParentHash;
    }

    public void setOtherParentHash(String otherParentHash) {
        this.otherParentHash = otherParentHash;
    }

    public void setBlobsMap(HashMap<String, String> blobsMap) {
        this.blobsMap = blobsMap;
    }

    public static String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    public String getHash() {
        return Utils.sha1(this.message, this.directParentHash, dateToString(this.date));
    }

    public HashMap<String, String> getBlobsMap() {
        return blobsMap;
    }

    public void saveCommit() {
        File commitFile = Utils.join(Repository.COMMIT, getHash());
        try {
            if (!commitFile.exists()) {
                commitFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeObject(commitFile, this);
    }

    public void saveBlob (String blobFilename, String blobHas) {
        this.blobsMap.put(blobFilename, blobHas);
    }

    public void removeBlob(String blobFilename) {
        this.blobsMap.remove(blobFilename);
    }

    public static String getHeadCommitHash() {
        String branchName = Branch.getCurBranch();
        String commitHash = Utils.readContentsAsString(Utils.join(Repository.REFS_HEADs, branchName));
        return commitHash;
    }
    public static Commit getHeadCommit() {
        String headCommitHash = getHeadCommitHash();
        return getCommitFromHash(headCommitHash);
    }

    public static Commit getCommitFromHash(String hash) {
        File commitFile = Utils.join(Repository.COMMIT, hash);
        Commit commit = Utils.readObject(commitFile, Commit.class);
        return commit;
    }

    public static Commit getCommitFromBranch(String branchName) {
        String commitHash = Utils.readContentsAsString(Utils.join(Repository.REFS_HEADs, branchName));
        return getCommitFromHash(commitHash);
    }
}
