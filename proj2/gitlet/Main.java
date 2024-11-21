package gitlet;



/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author hu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            Utils.error("Must have at least one argument");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.gitletInit();
                break;
            case "add":
                if (args.length != 2) {
                    Utils.error("Must have two arguments");
                }
                String addFileName = args[1];
                Repository.setAddStage(addFileName);
                break;
            case "commit":
                if (args.length != 2) {
                    Utils.error("Must have two arguments");
                }
                String message = args[1];
                Repository.doCommit(message);
                break;
            case "rm":
                if (args.length != 2) {
                    Utils.error("Must have two arguments");
                }
                String deletFileName = args[1];
                Repository.setRemoveStage(deletFileName);
                break;
            case "log":
                if (args.length != 1) {
                    Utils.error("log command does not need argument");
                }
                Repository.printLog();
                break;
            case "global-log":
                if (args.length != 1) {
                    Utils.error("global-log command does not need argument");
                }
                Repository.printAllLog();
                break;
            case "find":
                if (args.length != 2) {
                    Utils.error("Must have two arguments");
                }
                String findMessage = args[1];
                Repository.doFind(findMessage);
                break;
            case "status":
                if (args.length != 1) {
                    Utils.error("status command does not need argument");
                }
                Repository.doStatus();
                break;
            case "checkout":
                if (args.length < 2 || args.length > 4) {
                    Utils.error("Must have arguments Such as checkout [commit id] -- [file name], checkout -- [file name], checkout [branch name]");
                }
                if (args.length == 3) {
                    if (!"--".equals(args[1])) {
                        Utils.error("Argument error");
                    } else {
                        String fileName = args[2];
                        Repository.doCheckOut(fileName);
                    }
                }
                if (args.length == 4) {
                    if (!"--".equals(args[2])) {
                        Utils.error("Argument error");
                    } else {
                        String commitId = args[1];
                        String fileName = args[3];
                        Repository.doCheckOut(commitId, fileName);
                    }
                }

                if (args.length == 2) {
                    String branchName = args[1];
                    Repository.doCheckOutChangeBranch(branchName);
                }
                break;
            case "branch":
                if (args.length != 2) {
                    Utils.error("Must have arguments branch name");
                }
                String branchName = args[1];
                Repository.doBranch(branchName);
                break;
        }
    }
}
