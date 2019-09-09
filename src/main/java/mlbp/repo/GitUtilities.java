package mlbp.repo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.repodriller.domain.Commit;

public class GitUtilities {
    Repository repository;
    Git git;
    List<String> indicators = Arrays.asList("bug", "fix", "defect", "patch", "error");

    public GitUtilities(String projectPath) throws IOException {
        super();
        this.git = Git.open(new File(projectPath + ".git"));
        this.repository = git.getRepository();
    }

    public String ReadFileFromCommit(String commitHash, String filePath) {
        // find the HEAD
        try {
            ObjectId lastCommitId = repository.resolve(commitHash);
            if (lastCommitId == null)
                return null;

            // a RevWalk allows to walk over commits based on some filtering that is defined
            RevWalk revWalk = new RevWalk(repository);
            RevCommit commit = revWalk.parseCommit(lastCommitId);
            // and using commit's tree find the path
            RevTree tree = commit.getTree();

            // now try to find a specific file
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            treeWalk.setFilter(PathFilter.create(filePath));
            if (!treeWalk.next()) {
                revWalk.close();
                treeWalk.close();
                throw new IllegalStateException("Did not find expected file " + filePath);
            }

            ObjectId objectId = treeWalk.getObjectId(0);
            ObjectLoader loader = repository.open(objectId);

            String newData = readStream(loader.openStream());
            // and then one can the loader to read the file

            revWalk.dispose();
            return newData;
        } catch (RevisionSyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    static String readStream(InputStream iStream) throws IOException {
        // build a Stream Reader, it can read char by char
        InputStreamReader iStreamReader = new InputStreamReader(iStream);
        // build a buffered Reader, so that i can read whole line at once
        BufferedReader bReader = new BufferedReader(iStreamReader);
        String line = null;
        StringBuilder builder = new StringBuilder();
        while ((line = bReader.readLine()) != null) { // Read till end
            builder.append(line + '\n');
        }
        bReader.close(); // close all opened stuff
        iStreamReader.close();
        iStream.close();
        return builder.toString();
    }

    public boolean isBugFixingCommit(Commit commit) {
        return indicators.stream().anyMatch(commit.getMsg().toLowerCase()::contains);
    }
}
