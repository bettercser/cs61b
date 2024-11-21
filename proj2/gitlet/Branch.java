package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Branch {

    /**
     * branch file ,it's content is current commit hash
     * @param branchName
     * @param commitHash
     */
    public static void saveBranch (String branchName, String commitHash) {
        File branchFile = Utils.join(Repository.REFS_HEADs, branchName);
        if (!branchFile.exists()) {
            mkBranch(branchName);
        }
        Utils.writeContents(branchFile, commitHash);
    }

    /**
     * mk branchFile
     * @param branchName
     */
    public static void mkBranch (String branchName) {
        File branchFile = Utils.join(Repository.REFS_HEADs, branchName);
        if (branchFile.exists()) {

            System.out.println("Branch " + branchName + " already exists.");
            System.exit(0);
        }
        try {
            branchFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * HEAD use for saving current branch
     * @param branchName
     */
    public static void saveHEAD(String branchName) {
        File HEAD = Utils.join(Repository.HEAD);
        Utils.writeContents(HEAD, branchName);
    }

    /**
     * edit current commit, and current HEAD ref
     * @param branchName
     * @param commit
     */
    public static void doAllSave(String branchName, Commit commit) {
        String commitHash = commit.getHash();
        saveBranch(branchName, commitHash);
        saveHEAD(branchName);
    }

    public static List<String> getAllBranches() {
        return Utils.plainFilenamesIn(Repository.REFS_HEADs);

    }

    public static String getCurBranch() {
        File HEAD = Utils.join(Repository.HEAD);
        return Utils.readContentsAsString(HEAD);
    }

}
