package mlbp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mlbp.parser.beans.Commit2;

public class CommitGoal {

    public static void tagCommit(Commit2 pCommit) {

        if (CommitGoal.isBugFixing(pCommit)) {
            pCommit.setBugFix(true);
        }
    }

    private static boolean isEnhancement(Commit2 pCommit) {
        String commitMessage = pCommit.getSubject() + " " + pCommit.getBody();

        if ((commitMessage.toLowerCase().contains("updat")) || (commitMessage.toLowerCase().contains("modif")) || (commitMessage.toLowerCase().contains("upgrad")) || (commitMessage.toLowerCase().contains("export"))
                || (commitMessage.toLowerCase().contains("remov")) || (commitMessage.toLowerCase().contains("integrat")) || (commitMessage.toLowerCase().contains("support"))
                || (commitMessage.toLowerCase().contains("enhancement")) || (commitMessage.toLowerCase().contains("replac")) || (commitMessage.toLowerCase().contains("includ"))
                || (commitMessage.toLowerCase().contains("expos")) || (commitMessage.toLowerCase().contains("better")) || (commitMessage.toLowerCase().contains("svn"))
                || (commitMessage.toLowerCase().contains("generate"))) {
            return true;
        }

        return false;
    }

    private static boolean isNewFeature(Commit2 pCommit) {
        String commitMessage = pCommit.getSubject() + " " + pCommit.getBody();

        if ((commitMessage.toLowerCase().contains("new")) || (commitMessage.toLowerCase().contains("feature")) || (commitMessage.toLowerCase().contains("add")) || (commitMessage.toLowerCase().contains("create"))
                || (commitMessage.toLowerCase().contains("introduc")) || (commitMessage.toLowerCase().contains("migrat"))) {
            return true;
        }

        return false;
    }

    private static boolean isBugFixing(Commit2 pCommit) {
        String commitMessage = pCommit.getSubject() + " " + pCommit.getBody();

        // if ((commitMessage.toLowerCase().contains("fix")) || (commitMessage.toLowerCase().contains("repair")) || (commitMessage.toLowerCase().contains("error")) ||
        // (commitMessage.toLowerCase().contains("avoid"))
        // || (commitMessage.toLowerCase().contains("bug ")) || (commitMessage.toLowerCase().contains("issue ")) || (commitMessage.toLowerCase().contains("#"))
        // || (commitMessage.toLowerCase().contains("exception"))) {

        Pattern pattern = Pattern.compile("(?i)fix|repair|error|bug|issue|#[0-9]+");
        Matcher matcher = pattern.matcher(commitMessage);
        if (matcher.find())
            // System.out.println(commitMessage);
            return true;
        // }

        return false;
    }

    private static boolean isRefactoring(Commit2 pCommit) {
        String commitMessage = pCommit.getSubject() + " " + pCommit.getBody();

        if ((commitMessage.toLowerCase().contains("renam")) || (commitMessage.toLowerCase().contains("reorganiz")) || (commitMessage.toLowerCase().contains("refactor")) || (commitMessage.toLowerCase().contains("clean"))
                || (commitMessage.toLowerCase().contains("polish")) || (commitMessage.toLowerCase().contains("typo")) || (commitMessage.toLowerCase().contains("move")) || (commitMessage.toLowerCase().contains("extract"))
                || (commitMessage.toLowerCase().contains("reorder")) || (commitMessage.toLowerCase().contains("re-order"))) {
            return true;
        }

        return false;
    }

    private static boolean isMerge(Commit2 pCommit) {
        String commitMessage = pCommit.getSubject() + " " + pCommit.getBody();

        if ((commitMessage.toLowerCase().contains("merge"))) {
            return true;
        }

        return false;
    }

    private static boolean isPorting(Commit2 pCommit) {
        String commitMessage = pCommit.getSubject() + " " + pCommit.getBody();

        if ((commitMessage.toLowerCase().contains("initial "))) {
            return true;
        }

        return false;
    }
}