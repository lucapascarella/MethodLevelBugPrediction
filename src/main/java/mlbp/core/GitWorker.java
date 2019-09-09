package mlbp.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import mlbp.beans.Release;
import mlbp.beans.Rename;
import mlbp.utils.Prop;

public class GitWorker {

    private Exec exec;
    private Prop prop;

    public GitWorker(Prop prop, String project) {
        this.prop = prop;
        String workingDirecotry = prop.getProperty(Prop.workDirKey) + prop.getProperty(Prop.fileSepKey) + project;
        this.exec = new Exec(workingDirecotry, prop.getProperty(Prop.printCommandKey), prop.getProperty(Prop.printOutputKey), prop.getProperty(Prop.printErrorKey));
    }

    public void close() {
        this.exec.close();
    }

    public String getBranchesList() {
        exec.createCommand("git branch -a --sort version:refname", null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty())
                return result;
        }
        return null;
    }

    public String getTagsList() {
        exec.createCommand("git tag --sort=-taggerdate", null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (!result.isEmpty())
                return result;
        }
        return null;
    }

    public String getHashFromTag(String tag) {
        exec.createCommand("git rev-parse " + tag, null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty())
                return result;
        }
        return null;
    }

    public boolean gitCheckout(String gitHash) {
        exec.createCommand("git checkout " + gitHash, null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.contains("HEAD is now") && result.isEmpty())
                return true;
        }
        return false;
    }

    public Vector<Rename> trackRename(String gitHash) {
        Vector<Rename> v = new Vector<Rename>();
        exec.createCommand("git log --name-status -1 --pretty='' " + gitHash, null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty()) {
                for (String s : result.split("\n")) {
                    List<String> files = new ArrayList<String>(Arrays.asList(s.split("\t")));
                    if (files.size() == 3 && files.get(0).charAt(0) == 'R') {
                        Rename rename = new Rename(files.get(0), files.get(1), files.get(2));
                        v.add(rename);
                    }
                }
                // if (v != null && v.size() > 0)
                // System.out.println("Commit with renames (" + v.size() + "): " + gitHash);
                return v;
            }
        }
        return null;
    }

    public String getDateOfCommit(String gitHash) {
        exec.createCommand("git log -1 -s --format=%ct " + gitHash, null); // Unix format
        //exec.createCommand("git log -1 -s --format=%ad " + gitHash, null); // Human format
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty())
                return result.replaceAll("\\r|\\n", "");
        }
        return null;
    }

    public String getPreviousCommit(String gitID) {
        exec.createCommand("git log " + gitID + "~ --pretty=format:'%H' -1", null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty())
                return result;
        }
        return null;
    }

    public String getCommitMessage(String gitID) {
        exec.createCommand("git log " + gitID + " --pretty=format:'%s' -1", null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty())
                return result;
        }
        return null;
    }

    public String getCommitBody(String gitID) {
        exec.createCommand("git log " + gitID + " --pretty=format:'%b' -1", null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty())
                return result;
        }
        return null;
    }

    public String getAuthorName(String gitID) {
        exec.createCommand("git log " + gitID + " --pretty=format:'%an' -1", null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty())
                return result;
        }
        return null;
    }

    public String getAuthorEmail(String gitID) {
        exec.createCommand("git log " + gitID + " --pretty=format:'%ae' -1", null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty())
                return result;
        }
        return null;
    }

    public String[] getListOfFiles(String gitID) {
        exec.createCommand("git show " + gitID + " --pretty=format:'' --name-only", null);

        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty()) {
                String textStr[] = result.split("\\r\\n|\\n|\\r");
                return textStr;
            }
        }
        return null;
    }

    public boolean saveDiff(String gitID, String file, String destDir) {
        if (gitID != null) {
            exec.createCommand("git show " + gitID + "~ " + gitID + " -- " + file + " > " + destDir, null);
            if (exec.execSyncCommand()) {
                // String result = exec.getReasult();
                String error = exec.getError();
                if (error.isEmpty())
                    return true;
            }
        }
        return false;
    }

    public String getFirstCommitHash() {
        exec.createCommand("git rev-list --max-parents=0 HEAD -1", null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty())
                return result.replaceAll("\\r|\\n", "");
        }
        return null;
    }

    public String getGitHash(String gitHash) {
        exec.createCommand("git log " + gitHash + " --pretty=format:'%H' -1", null);
        if (exec.execSyncCommand()) {
            String result = exec.getReasult();
            String error = exec.getError();
            if (error.isEmpty())
                return result.replaceAll("\\r|\\n", "");
        }
        return null;
    }
}
