/**
 * 
 */
package mlbp.parser.beans;

import java.util.List;

/**
 * @author Tufano Michele - tufanomichele89@gmail.com
 *
 * GitDM - Git Data Mining
 */
public class Bug implements java.io.Serializable{

	private String ID;
	private String subject;
	private long reportedTime;
	private long lastChangedTime;
	private String body;
	private Commit2 fix;
	private String product;
	private String component;
	private String assignedTo;
	private String status;
	private String resolution;
	private List<Commit2> fixInducingChanges;
	
	public String toString(){
		String tostring="";
		tostring += "\nBug ID: "+ID;
		tostring += "\nProduct: "+product;
		tostring += "\nComponent: "+component;
		tostring += "\nReported Time: "+reportedTime;
		tostring += "\nLast Changed Time: "+lastChangedTime;
		tostring += "\nStatus: "+status;
		tostring += "\nResolution: "+resolution;
		tostring += "\nSubject: "+subject;
		//tostring += "\nFix Commit: "+fix.getCommitHash();

		return tostring;
	}
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
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
	public Commit2 getFix() {
		return fix;
	}
	public void setFix(Commit2 fix) {
		this.fix = fix;
	}
	public List<Commit2> getFixInducingChanges() {
		return fixInducingChanges;
	}
	public void setFixInducingChanges(List<Commit2> inducingFixChanges) {
		this.fixInducingChanges = inducingFixChanges;
	}
	public long getReportedTime() {
		return reportedTime;
	}
	public void setReportedTime(long reportedTime) {
		this.reportedTime = reportedTime;
	}
	public long getLastChangedTime() {
		return lastChangedTime;
	}
	public void setLastChangedTime(long lastChangedTime) {
		this.lastChangedTime = lastChangedTime;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
}
