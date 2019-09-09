package mlbp.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.domain.ModificationType;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import info.debatty.java.stringsimilarity.Cosine;
import mlbp.parser.beans.Commit2;
import mlbp.repo.beans.ChangeMetricsBean;
import mlbp.repo.beans.ClassBean;
import mlbp.repo.beans.MethodBean;
import mlbp.utils.CommitGoal;
import mlbp.utils.Diff;

public class MLDPVisitor implements CommitVisitor {

    private static final double RENAME_ACCURACY = 0.05;

    private int numberOfCommits;
    private int numberOfBuggyCommits;
    private int numberOfFiles;
    private int numberOfBuggyFiles;
    HashMap<String, MLDP> methods = new HashMap<String, MLDP>();
    GitUtilities gu;

    public MLDPVisitor(GitUtilities gu) {
        this.gu = gu;
    }

    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {

        boolean isBuggy = false;
        int files = 0;

        System.out.println("Working on: " + repo.getPath() + " " + commit.getHash());
        for (Modification m : commit.getModifications()) {
            files++;
            HashMap<String, String> commitMethods = new HashMap<String, String>();

            // System.out.println(m.getSourceCode());
            if (!m.getFileName().contains(".java"))
                continue;
            if (m.getSourceCode().isEmpty())
                continue;

            if (!Pattern.compile("\\s*class\\s*.*\\{").matcher(m.getSourceCode()).find())
                continue;
            if (!Pattern.compile("\\s*package\\s*.*\\s*;").matcher(m.getSourceCode()).find())
                continue;

            System.out.println("\t" + files + "/" + commit.getModifications().size() + " " + m.getType() + " " + m.getFileName());
            // System.out.println(m.getSourceCode());
            isBuggy = tagCommit(commit);
            if (isBuggy)
                numberOfBuggyFiles++;

            ClassBean convertedClass = FolderToJavaProjectConverter.convert(m.getSourceCode());
            if (convertedClass == null)
                continue;
            saveMethods(convertedClass, commitMethods);

            HashMap<String, String> previousCommitMethods = new HashMap<String, String>();
            String sourceCodePreviousCommit = "";

            try {
                sourceCodePreviousCommit = gu.ReadFileFromCommit(commit.getHash() + "^", m.getType() == ModificationType.RENAME ? m.getOldPath() : m.getNewPath());
            } catch (Exception e) {
                // e.printStackTrace();
            }

            ClassBean convertedClassPreviousCommit = new ClassBean();
            if (m.getType() != ModificationType.ADD && m.getType() != ModificationType.COPY) {
                if (Pattern.compile("\\s*class\\s*.*\\{").matcher(m.getSourceCode()).find() && Pattern.compile("\\s*package\\s*.*\\s*;").matcher(sourceCodePreviousCommit).find()) {
                    convertedClassPreviousCommit = FolderToJavaProjectConverter.convert(sourceCodePreviousCommit);
                    saveMethods(convertedClassPreviousCommit, previousCommitMethods);
                }
            }

            if (m.getType() == ModificationType.RENAME) {
                updateAllOccurrences(convertedClassPreviousCommit.getBelongingPackage() + "." + convertedClassPreviousCommit.getName() + ":", convertedClass.getBelongingPackage() + "." + convertedClass.getName() + ":");
            }
            HashMap<String, ChangeMetricsBean> checkForChanges = checkForChanges(commitMethods, previousCommitMethods, convertedClass.getBelongingPackage(), convertedClass.getName(), m.getDiff());

            for (String methodName : checkForChanges.keySet()) {
                String formatName = formatName(convertedClass.getBelongingPackage(), convertedClass.getName(), methodName);
                if (methods.containsKey(formatName)) {
                    updateMetrics(methods.get(formatName), checkForChanges.get(methodName), commit.getAuthor().getName(), isBuggy);
                } else {
                    MLDP mldp = new MLDP();
                    methods.put(formatName, mldp);
                    updateMetrics(mldp, checkForChanges.get(methodName), commit.getAuthor().getName(), isBuggy);
                }
            }
            numberOfFiles++;
        }
        if (isBuggy)
            numberOfBuggyCommits++;
        numberOfCommits++;
    }

    private boolean tagCommit(Commit commit) {
        Commit2 c = new Commit2();
        c.setBody(commit.getMsg());
        CommitGoal.tagCommit(c);
        return c.isBugFix();
    }

    private void updateAllOccurrences(String oldPath, String newPath) {
        HashMap<String, MLDP> tmp = new HashMap<String, MLDP>(methods);
        for (String oldFilePath : tmp.keySet()) {
            if (oldFilePath.contains(oldPath)) {
                String newFilePath = oldFilePath.replace(oldPath, newPath);
                MLDP m = methods.getOrDefault(oldFilePath, new MLDP());
                methods.remove(oldFilePath);
                methods.put(newFilePath, m);
            }
        }
    }

    private void updateMetrics(MLDP mldp, ChangeMetricsBean changeMetrics, String author, boolean isBuggy) {
        mldp.numChanges = mldp.numChanges + 1;
        mldp.authors.add(author);
        mldp.linesAdded.add(changeMetrics.getLinesAdded());
        mldp.linesDeleted.add(changeMetrics.getLinesDeleted());
        mldp.decl.add(changeMetrics.getDecl());
        mldp.cond += changeMetrics.getCond();
        mldp.elseAdded.add(changeMetrics.getElseAdded());
        mldp.elseDeleted.add(changeMetrics.getElseDeleted());
        mldp.isBuggy = mldp.isBuggy | isBuggy;
    }

    private HashMap<String, ChangeMetricsBean> checkForChanges(HashMap<String, String> methodsContentCommit, HashMap<String, String> methodsContentPreviousCommit, String packageClass, String className, String diffStr) {
        HashMap<String, ChangeMetricsBean> res = new HashMap<String, ChangeMetricsBean>();
        ChangeMetricsBean cmb = new ChangeMetricsBean();
        boolean update;
        for (String methodName : methodsContentCommit.keySet()) {
            update = false;
            String newMethod = methodsContentCommit.get(methodName);
            String oldMethod = methodsContentPreviousCommit.get(methodName);
            if (methodsContentPreviousCommit.containsKey(methodName)) {
                if (!newMethod.equals(oldMethod)) {
                    //System.out.println(methodName + " in " + className + " changed!");
                    update = true;
                }
            } else {
                // method has been added or renamed
                update = true;
                methodName = checkForMethodRenaming(methodName, newMethod, packageClass, className, methodsContentPreviousCommit);
            }
            if (update) {
                int linesAdded = 0, linesDeleted = 0;
                String newCode, oldCode;
                newCode = newMethod.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
                if (oldMethod != null) {
                    oldCode = oldMethod.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");

                    Diff diff = new Diff();
                    List<Map<Integer, String>> resDiff = diff.diff(diffStr);
                    Map<Integer, String> addedLines = resDiff.get(0);
                    Map<Integer, String> deletedLines = resDiff.get(1);

                    // <LIST_DELETIONS>.stream().anyMatch(<STRING_METHOD>::contains);

                    linesAdded = 0;
                    for (String newLine : newCode.split("\n")) {
                        newLine = newLine.replaceAll("\\s+", "");
                        for (Entry<Integer, String> entry : addedLines.entrySet()) {
                            String s = entry.getValue().replaceAll("\\s+", "");
                            if (s.equals(newLine))
                                linesAdded++;
                        }
                    }

                    linesDeleted = 0;
                    for (String newLine : newCode.split("\n")) {
                        newLine = newLine.replaceAll("\\s+", "");
                        for (Entry<Integer, String> entry : deletedLines.entrySet()) {
                            String s = entry.getValue().replaceAll("\\s+", "");
                            if (s.equals(newLine))
                                linesDeleted++;
                        }
                    }

                } else {
                    linesAdded = StringUtils.countMatches(newCode, "\n");
                    oldCode = "";
                }

                cmb.setLinesAdded(linesAdded);
                cmb.setLinesDeleted(linesDeleted);
                cmb.setDecl(numberOfDecl(newCode, oldCode));
                cmb.setCond(numberOfCond(newCode, oldCode));
                cmb.setElseAdded(numberOfElseAdded(newCode, oldCode));
                cmb.setElseDeleted(cmb.getElseAdded() * -1);
                res.put(methodName, cmb);
            }
        }
        return res;
    }

    private int numberOfDecl(String newCode, String oldCode) {
        Pattern pattern = Pattern.compile("(public|protected|private|static|\\s) +[\\w\\<\\>\\[\\]]+\\s+(\\w+) *\\([^\\)]*\\) *(\\{?|[^;])\n");
        Matcher matcherNew, matcherOld;
        matcherNew = pattern.matcher(newCode);
        if (matcherNew.matches()) {
            matcherOld = pattern.matcher(oldCode);
            if (matcherOld.matches()) {
                if (!matcherNew.group().equals(matcherOld.group()))
                    return 1;
            }
        }
        return 0;
    }

    private int numberOfElseAdded(String newCode, String oldCode) {
        Pattern pattern = Pattern.compile("\\s?\\}?else\\s*\\{?");
        Matcher matcher = pattern.matcher(newCode);

        int countNew = 0;
        while (matcher.find())
            countNew++;

        int countOld = 0;
        matcher = pattern.matcher(oldCode);
        while (matcher.find())
            countOld++;

        return countNew - countOld;
    }

    private Integer numberOfCond(String newCode, String oldCode) {
        Pattern pattern = Pattern.compile("\\sif\\s*\\(");
        Matcher matcher = pattern.matcher(newCode);

        int countNew = 0;
        while (matcher.find())
            countNew++;

        int countOld = 0;
        matcher = pattern.matcher(oldCode);
        while (matcher.find())
            countOld++;

        return Math.abs(countNew - countOld);
    }

    private String checkForMethodRenaming(String methodName, String methodContent, String packageClass, String className, HashMap<String, String> methodsContentPreviousCommit) {
        Cosine c = new Cosine();
        for (String met : methodsContentPreviousCommit.keySet()) {
            double distance = c.distance(methodContent, methodsContentPreviousCommit.get(met));

            if (distance < RENAME_ACCURACY) {
                // System.out.println(methodName + " is a rename of " + met + " in class " + className);
                renameModification(methodName, met, packageClass, className);
                return methodName;
            }
        }
        return methodName;
    }

    private void renameModification(String methodName, String oldMethodName, String packageClass, String nameClass) {
        String name = formatName(packageClass, nameClass, oldMethodName);
        MLDP changes = methods.getOrDefault(name, new MLDP());

        methods.remove(name);

        name = name.replace(oldMethodName, methodName);
        methods.put(name, changes);
    }

    private String formatName(String packageClass, String nameClass, String methodName) {
        return packageClass + "." + nameClass + ":" + methodName;
    }

    public HashMap<String, MLDP> getMethods() {
        return this.methods;
    }

    private void saveMethods(ClassBean convertedClass, HashMap<String, String> methodsContentCommit) {
        for (MethodBean testCase : convertedClass.getMethods()) {
            methodsContentCommit.put(testCase.getName(), testCase.getTextContent());
        }
    }

    public int getNumberOfCommits() {
        return numberOfCommits;
    }

    public int getNumberOfBuggyCommits() {
        return numberOfBuggyCommits;
    }

    public int getNumberOfFiles() {
        return numberOfFiles;
    }

    public int getNumberOfBuggyFiles() {
        return numberOfBuggyFiles;
    }
}
