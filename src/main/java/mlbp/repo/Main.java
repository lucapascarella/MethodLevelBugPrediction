package mlbp.repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;
import mlbp.beans.Release;
import mlbp.core.GitWorker;
import mlbp.utils.Args;
import mlbp.utils.FilePathManager;
import mlbp.utils.Prop;
import mlbp.utils.ReleaseFiltering;

/**
 * Hello world!
 *
 */
public class Main {

    private static Prop prop;

    public static void main(String[] args) {
        long millis = System.currentTimeMillis();

        String startRelease = "", endRelease = "";

        // Load project properties using optional input arguments
        prop = new Prop(new Args(args));
        System.out.println("*** Program " + prop.getProperty(Prop.progNameKey) + " version: " + prop.getProperty(Prop.progVersionKey) + " ***\n");

        String filename = FilePathManager.getFullPath(prop, Prop.projectListKey);
        Vector<String> projects = readProjects(filename);
        for (String project : projects) {
            // Create git worker object
            GitWorker git = new GitWorker(prop, project);
            // Get all releases from git and apply Fabio's filter
            Vector<Release> releaseList = getReleaseList(project);

            // Start releases analysis
            for (int ir = 0; ir < releaseList.size(); ir++) {
                Release r = releaseList.get(ir);
                if (ir == 0) {
                    endRelease = r.getGitHash();
                } else {
                    endRelease = r.getGitHash();
                    System.out.println("\tWorking on release (" + ir + "/" + (releaseList.size() - 1) + "): " + r.getTextContent() + " From: " + startRelease + " => " + endRelease + git.getDateOfCommit(startRelease)
                            + " " + git.getDateOfCommit(endRelease));
                    String outputCsv = getFileOutput(ir, project, r, Prop.csvResultKey);
                    String outputSummary = getFileOutput(ir, project, r, Prop.summaryResultKey);
                    String gitWorkingDirecotry = prop.getProperty(Prop.workDirKey) + prop.getProperty(Prop.fileSepKey) + project + prop.getProperty(Prop.fileSepKey);
                    MyStudy ms = new MyStudy(ir, gitWorkingDirecotry, outputCsv, outputSummary, startRelease, endRelease);
                    ms.execute();
                }
                startRelease = endRelease;
            }
        }

        // Program finished
        prop.close();
        long secs = System.currentTimeMillis() - millis;
        System.out.println("\nMethodLevelBugPrediction ended in: " + (secs / 1000.0) + " seconds");
    }

    static private Vector<String> readProjects(String filename) {
        Vector<String> list = new Vector<String>();
        try {
            Scanner s = new Scanner(new File(filename));
            while (s.hasNext()) {
                String project = s.nextLine();
                if (!project.contains("//"))
                    list.add(project);
            }
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file " + filename + " does not exist.");
        }
        return list;
    }

    static private Vector<Release> getReleaseList(String project) {
        String branches = "";
        Map<Integer, Release> map = new HashMap<Integer, Release>();
        Vector<Release> list = new Vector<Release>();

        // Create git worker object
        GitWorker git = new GitWorker(prop, project);

        // Get list of branches or tags
        if (Boolean.parseBoolean(prop.getProperty(Prop.branchAnalysisKey)))
            branches += git.getBranchesList();
        if (Boolean.parseBoolean(prop.getProperty(Prop.tagAnalysisKey)))
            branches += git.getTagsList();
        // Split by row
        List<String> branchList = new ArrayList<String>(Arrays.asList(branches.split("\n")));

        // Apply the Fabio's filter
        System.out.println("Releases for " + project + ":");
        ReleaseFiltering filter = new ReleaseFiltering();
        for (String branch : branchList) {
            branch = branch.replace(" ", "");
            String type = filter.extractTypeOfRelease(branch);
            String releaseHash = git.getHashFromTag(branch);
            if (releaseHash != null) {
                releaseHash = releaseHash.replace("\n", "").replace("\r", "").replace("*", "");
                String gitHash = git.getGitHash(releaseHash);
                String gitDate = git.getDateOfCommit(gitHash);
                Release release = new Release(branch, type, releaseHash, gitHash, gitDate);
                map.put(Integer.parseInt(gitDate), release);
            }
        }
        // Add first commit like fake major-release
        String firstCommitHash = git.getFirstCommitHash();
        Release r = new Release("First commit", "major-release", firstCommitHash, firstCommitHash, git.getDateOfCommit(firstCommitHash));
        map.put(Integer.parseInt(r.getUnixDate()), r);

        // Order release in ascending order
        TreeMap<Integer, Release> tree = new TreeMap<Integer, Release>(map);
        for (Map.Entry<Integer, Release> entry : tree.entrySet()) {
            if (entry.getValue().getType().equals("major-release")) {
                Date date = Date.from(Instant.ofEpochSecond(entry.getKey()));
                System.out.println("Time: " + date.toString() + " Release: " + entry.getValue().getTextContent());
                list.add(entry.getValue());
            }
        }

        // Print project releases
        try {
            PrintWriter ps = new PrintWriter(prop.getProperty(Prop.outputDirKey) + prop.getProperty(Prop.fileSepKey) + project + "_releases.csv");
            ps.println("ID,ReleaseName,Type,UnixTime,GitHash");
            int i = 0;
            for (Release release : list)
                ps.println(i++ + "," + release.getTextContent() + "," + release.getType() + "," + release.getUnixDate() + "," + release.getGitHash());
            ps.close();
        } catch (FileNotFoundException e) {
            System.out.println("Can not write on the output file!");
        }

        System.out.println();
        git.close();

        return list;
    }

    static public String getFileOutput(int index, String project, Release release, String key) {

        String ps = prop.getProperty(Prop.fileSepKey);

        File releasePath = new File(prop.getProperty(Prop.outputDirKey) + ps + project);
        if (!releasePath.exists())
            releasePath.mkdirs();

        return releasePath + ps + index + "_" + prop.getProperty(key);
    }
}
