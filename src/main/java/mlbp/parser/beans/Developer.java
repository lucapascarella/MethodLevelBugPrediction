/**
 * 
 */
package mlbp.parser.beans;

/**
 * @author Tufano Michele - tufanomichele89@gmail.com
 *
 * GitDM - Git Data Mining
 */
public class Developer implements java.io.Serializable{

	private String name;
	private String email;
	
	
	public Developer(String name, String email) {
        super();
        this.name = name;
        this.email = email;
    }
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	/*
	@Override
	public boolean equals(Object obj) {
		Developer dev = (Developer) obj;
		if(dev.getEmail().equalsIgnoreCase(email)){
			return true;
		} else {
			return false;
		}
	}
	*/
}
