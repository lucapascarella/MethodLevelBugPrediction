package mlbp.beans;

public class Release {
    
    private String textContent;
    private String type;
    private String releaseHash;
    private String gitHash;
    private String unixDate;
    
    
    
    
    public Release(String textContent, String type, String releaseHash, String gitHash, String unixDate) {
        super();
        this.textContent = textContent;
        this.setReleaseHash(releaseHash);
        this.gitHash = gitHash;
        this.type = type;
        this.unixDate = unixDate;
    }
    
    public String getTextContent() {
        return textContent;
    }
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
    public String getGitHash() {
        return gitHash;
    }
    public void setGitHash(String gitHash) {
        this.gitHash = gitHash;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getReleaseHash() {
        return releaseHash;
    }

    public void setReleaseHash(String releaseHash) {
        this.releaseHash = releaseHash;
    }

    public String getUnixDate() {
        return unixDate;
    }

    public void setUnixDate(String unixDate) {
        this.unixDate = unixDate;
    }

}
