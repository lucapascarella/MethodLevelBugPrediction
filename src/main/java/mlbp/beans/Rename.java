package mlbp.beans;

public class Rename {

    private String value;
    private String oldFile;
    private String newFile;

    public Rename(String value, String oldFile, String newFile) {
        super();
        this.value = value;
        this.oldFile = oldFile;
        this.newFile = newFile;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOldFile() {
        return oldFile;
    }

    public void setOldFile(String oldFile) {
        this.oldFile = oldFile;
    }

    public String getNewFile() {
        return newFile;
    }

    public void setNewFile(String newFile) {
        this.newFile = newFile;
    }

}
