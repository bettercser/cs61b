package gitlet;

import jdk.jshell.execution.Util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author hu
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     *
     *
     * ├── .gitlet
     * │         ├── objects
     * │                    ├── commit
     * │                    ├── blob
     * │         ├── refs
     * │                    ├── heads
     * │         ├── HEAD
     * │         ├── stages
     * │                    ├── add
     * │                    ├── removal
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File STAGES_DIR = join(GITLET_DIR, "stages");
    /* TODO: fill in the rest of this class. */
    public static final File REFS_HEADs = join(REFS_DIR, "heads");

    public static final File ADD_STAGE = join(STAGES_DIR, "add");
    public static final File REMOVE_STAGE = join(STAGES_DIR, "removal");
    public static final File COMMIT = join(OBJECTS_DIR, "commit");
    public static final File BLOB = join(OBJECTS_DIR, "blob");
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    public static void removeFromStage(File stageFolder, String fileName) {
        if (stageFolder.isDirectory()) {
            File filePath = Utils.join(stageFolder, fileName);
            filePath.delete();
        }
    }

    /**
     * add file to add or removal stage, but if you want to use this function to save any file in any fucking folder .it's ok
     * @param stageFolder
     * @param fileName
     * @param content
     */
    public static void addToStage(File stageFolder, String fileName, String content) {
        File file = Utils.join(stageFolder, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Utils.writeContents(file, content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void gitletInit() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        setupPersistence();

        Date newDate = new Date(0);

        Commit initCommit = new Commit(newDate, "initial commit", "", null, null);
        String commitHash = initCommit.getHash();
        initCommit.saveCommit();
        Branch.doAllSave("master", initCommit);
    }



    public static void setupPersistence() {
        try {
            GITLET_DIR.mkdirs();
            OBJECTS_DIR.mkdirs();
            REFS_DIR.mkdirs();
            STAGES_DIR.mkdirs();
            REFS_HEADs.mkdirs();
            ADD_STAGE.mkdirs();
            REMOVE_STAGE.mkdirs();
            COMMIT.mkdirs();
            BLOB.mkdirs();
            HEAD.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * the file whether in commit
     * @param fileName
     */
    public static void setAddStage(String fileName) {

        File addFile = Utils.join(CWD, fileName);
        if(!addFile.exists()) {
            System.out.println("File does not exist: " + addFile.getAbsolutePath());
            System.exit(0);
        }
        Commit headCommit = Commit.getHeadCommit();
        HashMap<String, String> blobHashMap = headCommit.getBlobsMap();
        String fileContent = Utils.readContentsAsString(addFile);
        String fileHash = Utils.sha1(fileContent);
        /**
         * commit has file record
         */
        if (blobHashMap.containsKey(fileName)) {
            String blobHash = blobHashMap.get(fileName);
            if (blobHash.equals(fileHash)) {
                /**
                 * if commit has same file and stage has file
                 */
                List<String> stagingAddFile = Utils.plainFilenamesIn(ADD_STAGE);
                List<String> stagingRemoveFile = Utils.plainFilenamesIn(REMOVE_STAGE);
                if (stagingAddFile.contains(fileName)) {
                    removeFromStage(ADD_STAGE, fileName);
                }
                if (stagingRemoveFile.contains(fileName)) {
                    removeFromStage(REMOVE_STAGE, fileName);
                }
                return;
            }
        }

        Blob blob = new Blob(fileContent, fileHash);
        blob.saveBlob();
        addToStage(ADD_STAGE, fileName, fileHash);

    }

    /**
     * do commit ,current head is parent of current commit
     * @param message
     */
    public static void doCommit(String message) {

        List<String> stagingAddFile = Utils.plainFilenamesIn(ADD_STAGE);
        List<String> stagingRemoveFile = Utils.plainFilenamesIn(REMOVE_STAGE);
        if (stagingAddFile.isEmpty() && stagingRemoveFile.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        Commit parentCommit = Commit.getHeadCommit();
        Commit curCommit = new Commit(parentCommit);
        curCommit.setDate(new Date());
        curCommit.setMessage(message);
        curCommit.setDirectParentHash(parentCommit.getHash());
        HashMap<String, String> curCommitHashMap = curCommit.getBlobsMap();

        for (String fileName : stagingAddFile) {
            String blobHash = Utils.readContentsAsString(Utils.join(ADD_STAGE, fileName));
            curCommitHashMap.put(fileName, blobHash);
            File addFile = Utils.join(ADD_STAGE, fileName);
            addFile.delete();
        }
        for (String fileName : stagingRemoveFile) {
            curCommitHashMap.remove(fileName);
            File removeFile = Utils.join(REMOVE_STAGE, fileName);
            removeFile.delete();
        }

        /**
         * after commit, we need delete stage change the HEAD
         */

        curCommit.saveCommit();
        Branch.doAllSave(Branch.getCurBranch(), curCommit);
    }

    public static void setRemoveStage(String fileName) {
        /**
         * get add stage file, and current commit whether trace the file
         */
        List<String> stagingAddFile = Utils.plainFilenamesIn(ADD_STAGE);
        Commit parentCommit = Commit.getHeadCommit();
        HashMap<String, String> curCommitHashMap = parentCommit.getBlobsMap();
        /**
         * if not in add stage, and not be traced by commit
         */
        if (!curCommitHashMap.containsKey(fileName) && !stagingAddFile.contains(fileName)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }

        /**
         * if file in add stage
         */
        if (stagingAddFile.contains(fileName)) {
            removeFromStage(ADD_STAGE, fileName);
        }

        /**
         * if file be traced by commit
         */
        if (curCommitHashMap.containsKey(fileName)) {
            addToStage(REMOVE_STAGE, fileName, curCommitHashMap.get(fileName));
            File workFile = Utils.join(CWD, fileName);
            if (workFile.exists()) {
                workFile.delete();
            }
        }

    }

    public static void printAllLog() {
        List<String> commitHashList = Utils.plainFilenamesIn(COMMIT);
        for (String commitHash : commitHashList) {
            Commit commit = Commit.getCommitFromHash(commitHash);
            printCommit(commit);
        }
    }

    public static void printLog() {
        Commit headCommit = Commit.getHeadCommit();
        do {
            printCommit(headCommit);
            if (!"".equals(headCommit.getDirectParentHash())) {
                headCommit = Commit.getCommitFromHash(headCommit.getDirectParentHash());
            } else {
                System.exit(0);
            }

        } while (!"".equals(headCommit.getHash()));
    }



    private static void printCommit(Commit commit) {
        System.out.println("===");
        System.out.println("commit " + commit.getHash());
        if (commit.getOtherParentHash() != null) {
            System.out.println("Merge: " + commit.getDirectParentHash().substring(0, 8) + " " + commit.getOtherParentHash().substring(0, 8));
        }
        System.out.println("Date: " + Commit.dateToString(commit.getDate()));
        System.out.println(commit.getMessage());
        System.out.println();
    }

    public static void doFind(String message) {
        List<String> commitHashList = Utils.plainFilenamesIn(COMMIT);
        for (String commitHash : commitHashList) {
            Commit commit = Commit.getCommitFromHash(commitHash);
            String commitMessage = commit.getMessage();
            if (commitMessage.toLowerCase().contains(message.toLowerCase())) {
                System.out.println(commitHash);
            }
        }
    }

    /**
     * such as
     * === Branches ===
     * *master
     * other-branch
     *
     * === Staged Files ===
     * wug.txt
     * wug2.txt
     *
     * === Removed Files ===
     * goodbye.txt
     *
     * === Modifications Not Staged For Commit ===
     * junk.txt (deleted)
     * wug3.txt (modified)
     *
     * === Untracked Files ===
     * random.stuff
     */
    public static void doStatus() {
        System.out.println("=== Branches ===");
        String curBranch = Branch.getCurBranch();
        List<String> allBranches = Branch.getAllBranches();
        for (String branch : allBranches) {
            if (branch.equals(curBranch)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }

        System.out.println();
        System.out.println("=== Staged Files ===");
        List<String> addStagingFile = Utils.plainFilenamesIn(ADD_STAGE);
        for (String fileName : addStagingFile) {
            System.out.println(fileName);
        }

        System.out.println();
        System.out.println("=== Removed Files ===");
        List<String> removeStagingFile = Utils.plainFilenamesIn(REMOVE_STAGE);
        for (String fileName : removeStagingFile) {
            System.out.println(fileName);
        }

        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        Commit curCommit = Commit.getHeadCommit();
        HashMap<String, String> blobsMap = curCommit.getBlobsMap();
        List<String> workAllFile = Utils.plainFilenamesIn(CWD);
        for (String fileName : blobsMap.keySet()) {
            if (workAllFile.contains(fileName)) {
                String blobHash = blobsMap.get(fileName);
                String blobContent = Blob.getBlobContents(blobHash);
                String curFileContent = Utils.readContentsAsString(Utils.join(CWD, fileName));
                if (!curFileContent.equals(blobContent) && !addStagingFile.contains(fileName)) {
                    System.out.println(fileName + " " + "(modified)");
                }
            } else {
                if (!removeStagingFile.contains(fileName)) {
                    System.out.println(fileName + " " + "(deleted)");
                }
            }
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
        for (String fileName : workAllFile) {
            if (!removeStagingFile.contains(fileName) && !addStagingFile.contains(fileName) && !blobsMap.containsKey(fileName)) {
                System.out.println(fileName);
            }
        }
    }

    public static void doCheckOut(String fileName) {
        doCheckOut(Commit.getHeadCommitHash(), fileName);
    }

    public static void doCheckOut(String commitHash, String fileName) {
        Commit changeCommit = Commit.getCommitFromHash(commitHash);
        if (changeCommit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        HashMap<String, String> changeCommitHashMap = changeCommit.getBlobsMap();
        if (!changeCommitHashMap.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        File workFile = Utils.join(CWD, fileName);
        if (!workFile.exists()) {
            try {
                workFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String blobHash = changeCommitHashMap.get(fileName);
        String blobContent = Blob.getBlobContents(blobHash);
        Utils.writeContents(workFile, blobContent);
    }

    public static void doCheckOutChangeBranch(String branchName) {
        String curBranch = Branch.getCurBranch();

        if (curBranch.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        List<String> allBranches = Branch.getAllBranches();

        if (!allBranches.contains(curBranch)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }

        List<String> curWorkFile = Utils.plainFilenamesIn(CWD);
        List<String> addStagingFile = Utils.plainFilenamesIn(ADD_STAGE);
        List<String> removeStagingFile = Utils.plainFilenamesIn(REMOVE_STAGE);
        Commit curCommit = Commit.getHeadCommit();
        HashMap<String, String> curBlobsMap = curCommit.getBlobsMap();
        for (String fileName : curWorkFile) {
            if (!removeStagingFile.contains(fileName) && !addStagingFile.contains(fileName) && !curBlobsMap.containsKey(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        Commit changeCommit = Commit.getCommitFromBranch(branchName);
        HashMap<String, String> changeCommitBlobsMap = changeCommit.getBlobsMap();

        /**
         * delete stage files
         */
        for (String addStageFileName : addStagingFile) {
            File addFile = Utils.join(ADD_STAGE, addStageFileName);
            addFile.delete();
        }

        for (String removeStageFileName : removeStagingFile) {
            File removeFile = Utils.join(REMOVE_STAGE, removeStageFileName);
            removeFile.delete();
        }

        for (String blobFileName : changeCommitBlobsMap.keySet()) {
            String blobContent = Blob.getBlobContents(changeCommitBlobsMap.get(blobFileName));
            File workFile = Utils.join(CWD, blobFileName);
            if (!workFile.exists()) {
                try {
                    workFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Utils.writeContents(workFile, blobContent);
        }

        List<String> newWorkFiles = Utils.plainFilenamesIn(CWD);
        for (String fileName : newWorkFiles) {
            if (!changeCommitBlobsMap.containsKey(fileName)) {
                File workFile = Utils.join(CWD, fileName);
                workFile.delete();
            }
        }
    }

    public static void doBranch(String branchName) {
        List<String> allBranches = Branch.getAllBranches();
        if (allBranches.contains(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        String curCommitHash = Commit.getHeadCommitHash();
        File branchFile = Utils.join(REFS_HEADs, branchName);
        try {
            branchFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeContents(branchFile, curCommitHash);
    }


    public static void main(String[] args) {
        System.out.println(new Date(0));
    }
}
