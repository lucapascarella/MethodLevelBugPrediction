package mlbp.repo.beans;

public class ChangeMetricsBean {

    private int linesAdded = 0;
    private int linesDeleted = 0;
    private int decl = 0;
    private int cond = 0;
    private int elseAdded = 0;
    private int elseDeleted = 0;

    public int getLinesAdded() {
        return linesAdded;
    }

    public void setLinesAdded(int linesAdded) {
        this.linesAdded = linesAdded;
    }

    public int getLinesDeleted() {
        return linesDeleted;
    }

    public void setLinesDeleted(int linesDeleted) {
        this.linesDeleted = linesDeleted;
    }

    public int getDecl() {
        return decl;
    }

    public void setDecl(int decl) {
        this.decl = decl;
    }

    public int getCond() {
        return cond;
    }

    public void setCond(int cond) {
        this.cond = cond;
    }

    public int getElseAdded() {
        return elseAdded;
    }

    public void setElseAdded(int elseAdded) {
        this.elseAdded = elseAdded;
    }

    public int getElseDeleted() {
        return elseDeleted;
    }

    public void setElseDeleted(int elseDeleted) {
        this.elseDeleted = elseDeleted;
    }

}
