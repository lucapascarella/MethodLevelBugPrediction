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
public class Change implements java.io.Serializable{
	private FileBean file;
	private List<Integer> addedlines;
	private List<Integer> removedlines;
	private List<Integer> modifiedlines;
	
	
	public FileBean getFile() {
		return file;
	}
	public void setFile(FileBean file) {
		this.file = file;
	}
	public List<Integer> getAddedlines() {
		return addedlines;
	}
	public void setAddedlines(List<Integer> addedlines) {
		this.addedlines = addedlines;
	}
	public List<Integer> getRemovedlines() {
		return removedlines;
	}
	public void setRemovedlines(List<Integer> removedlines) {
		this.removedlines = removedlines;
	}
	public List<Integer> getModifiedlines() {
		return modifiedlines;
	}
	public void setModifiedlines(List<Integer> modifiedlines) {
		this.modifiedlines = modifiedlines;
	}

}
