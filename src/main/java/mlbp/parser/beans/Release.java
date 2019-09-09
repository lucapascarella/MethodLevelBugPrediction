package mlbp.parser.beans;

import java.util.Date;

public class Release {

	private String releaseID;
	private String type;
	private String commitID;
	private long timestamp;
	private Date date;
	
	public String getReleaseID() {
		return releaseID;
	}
	public void setReleaseID(String releaseID) {
		this.releaseID = releaseID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCommitID() {
		return commitID;
	}
	public void setCommitID(String commitID) {
		this.commitID = commitID;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	

}
