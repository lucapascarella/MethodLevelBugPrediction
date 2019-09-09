/**
 * 
 */
package mlbp.parser.beans;

import java.util.HashMap;
import java.util.List;

/**
 * @author Tufano Michele - tufanomichele89@gmail.com
 *
 * GitDM - Git Data Mining
 */
public class Commit2 implements java.io.Serializable{

	private String commitHash;
	private String abbreviateCommitHash;
	private Author author;
	private long authorTime;
	private Committer committer;
	private long committerTime;
	private String subject;
	private String body;
	private List<Change> changes;
	private boolean bug;
	private boolean fix;
	private Bug introducedBug;
	private Bug fixedBug;
	private boolean isBugFix = false;
	
	public String getCommitHash() {
		return commitHash;
	}
	public void setCommitHash(String commitHash) {
		this.commitHash = commitHash;
	}
	public String getAbbreviateCommitHash() {
		return abbreviateCommitHash;
	}
	public void setAbbreviateCommitHash(String abbreviateCommitHash) {
		this.abbreviateCommitHash = abbreviateCommitHash;
	}
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public long getAuthorTime() {
		return authorTime;
	}
	public void setAuthorTime(long authorTime) {
		this.authorTime = authorTime;
	}
	public Committer getCommitter() {
		return committer;
	}
	public void setCommitter(Committer committer) {
		this.committer = committer;
	}
	public long getCommitterTime() {
		return committerTime;
	}
	public void setCommitterTime(long committerTime) {
		this.committerTime = committerTime;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public List<Change> getChanges() {
		return changes;
	}
	public void setChanges(List<Change> changes) {
		this.changes = changes;
	}
	public boolean isBug() {
		return bug;
	}
	public void setBug(boolean bug) {
		this.bug = bug;
	}
	public boolean isFix() {
		return fix;
	}
	public void setFix(boolean fix) {
		this.fix = fix;
	}
	public Bug getIntroducedBug() {
		return introducedBug;
	}
	public void setIntroducedBug(Bug introducedBug) {
		this.introducedBug = introducedBug;
	}
	public Bug getFixedBug() {
		return fixedBug;
	}
	public void setFixedBug(Bug fixedBug) {
		this.fixedBug = fixedBug;
	}
    public boolean isBugFix() {
        return isBugFix;
    }
    public void setBugFix(boolean tag) {
        this.isBugFix = tag;
    }
}
