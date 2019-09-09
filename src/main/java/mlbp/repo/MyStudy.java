package mlbp.repo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class MyStudy implements Study {

    private static String headerCM = "methodHistories,authors,stmtAdded,maxStmtAdded,avgStmtAdded,stmtDeleted,maxStmtDeleted,avgStmtDeleted,churn,maxChurn,avgChurn,decl,cond,elseAdded,elseDeleted,buggy";

    private int index;
    private String repopath;
    private String outputCsv;
    private String outputSummary;
    private String start;
    private String end;
    private GitUtilities gu;

    public MyStudy(int index, String repopath, String outputCsv, String outputSummary, String start, String end) {
        super();
        this.index = index;
        this.repopath = repopath;
        this.outputCsv = outputCsv;
        this.outputSummary = outputSummary;
        this.start = start;
        this.end = end;
        try {
            this.gu = new GitUtilities(repopath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void execute() {
        MLDPVisitor mldpvisitor = new MLDPVisitor(gu);
        new RepositoryMining().in(GitRepository.singleProject(repopath))
                //.through(Commits.single("13e52da2394b1608864ead72f72feeb1852039a5"))
                .through(Commits.range(start, end))
                // .filters(new Filter())
                .process(mldpvisitor, new CSVFile(outputCsv)).mine();

        HashMap<String, MLDP> methods = mldpvisitor.getMethods();
        
        saveResults(methods, mldpvisitor.getNumberOfCommits(), mldpvisitor.getNumberOfBuggyCommits(), mldpvisitor.getNumberOfFiles(), mldpvisitor.getNumberOfBuggyFiles());
    }

    private void saveResults(HashMap<String, MLDP> methods, int commits, int buggyCommits, int files, int buggyFiles) {

        try {
            PrintWriter ps = new PrintWriter(outputCsv);
            ps.print("method,");
            ps.println(headerCM);
            for (String method : methods.keySet()) {
                MLDP m = methods.get(method);
                // Method name
                ps.print(method + ",");
                // CM
                ps.print(m.getNumChanges() + ",");
                ps.print(m.getNumAuthors() + ",");
                ps.print(m.getSumLinesAdded() + ",");
                ps.print(m.getMaxLinesAdded() + ",");
                ps.print(m.getAvgLinesAdded() + ",");
                ps.print(m.getSumLinesDeleted() + ",");
                ps.print(m.getMaxLinesDeleted() + ",");
                ps.print(m.getAvgLinesDeleted() + ",");
                ps.print(m.getChurn() + ",");
                ps.print(m.getMaxChurn() + ",");
                ps.print(m.getAvgChurn() + ",");
                ps.print(m.getDecl() + ",");
                ps.print(m.getCond() + ",");
                ps.print(m.getSumElseAdded() + ",");
                ps.print(m.getSumElseDeleted() + ",");
                // Buggy
                ps.print(m.isBuggy());
                ps.println();
                ps.flush();
            }
            ps.close();
        } catch (FileNotFoundException e) {
            System.out.println("Can not write on the output file!");
        }

        try {
            PrintWriter ps = new PrintWriter(outputSummary);
            ps.println("Release index: " + index);
            ps.println("Release git start hash: " + start);
            ps.println("Release git end hash: " + end);
            ps.println("Commits: " + commits);
            ps.println("Buggy commits: " + buggyCommits);
            ps.println("Files: " + files);
            ps.println("Buggy files: " + buggyFiles);
            ps.close();
        } catch (FileNotFoundException e) {
            System.out.println("Can not write on the output file!");
        }

    }

}
