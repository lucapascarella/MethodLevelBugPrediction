package mlbp.repo;

import java.util.ArrayList;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class MLDP {

    boolean isBuggy;
    int numChanges;
    ArrayList<String> authors;

    ArrayList<Integer> linesAdded;
    ArrayList<Integer> linesDeleted;

    ArrayList<Integer> decl;
    int cond;
    ArrayList<Integer> elseAdded;
    ArrayList<Integer> elseDeleted;

    public MLDP() {
        this.isBuggy = false;
        this.numChanges = 0;
        this.authors = new ArrayList<>();

        this.linesAdded = new ArrayList<>();
        this.linesDeleted = new ArrayList<>();
        this.decl = new ArrayList<>();
        this.elseAdded = new ArrayList<>();
        this.elseDeleted = new ArrayList<>();
    }

    public int getNumChanges() {
        return numChanges;
    }

    public int getNumAuthors() {
        return authors.stream().distinct().collect(Collectors.toList()).size();
    }

    // Added
    public Double getSumLinesAdded() {
        double sum = linesAdded.stream().mapToInt(i -> i.intValue()).sum();
        return sum;
    }

    public Double getMaxLinesAdded() {
        OptionalDouble max = linesAdded.stream().mapToDouble(a -> a).max();
        return max.isPresent() ? max.getAsDouble() : 0;
    }

    public Double getAvgLinesAdded() {
        OptionalDouble average = linesAdded.stream().mapToDouble(a -> a).average();
        return average.isPresent() ? average.getAsDouble() : 0;
    }

    // Deleted
    public Double getSumLinesDeleted() {
        double sum = linesDeleted.stream().mapToInt(i -> i.intValue()).sum();
        return sum;
    }

    public double getMaxLinesDeleted() {
        OptionalDouble max = linesDeleted.stream().mapToDouble(a -> a).max();
        return max.isPresent() ? max.getAsDouble() : 0;
    }

    public double getAvgLinesDeleted() {
        OptionalDouble average = linesDeleted.stream().mapToDouble(a -> a).average();
        return average.isPresent() ? average.getAsDouble() : 0;
    }
    
    // Churn
    public double getChurn() {
        double sum = getSumLinesAdded() - getSumLinesDeleted();
        return sum;
    }
    // Max Churn
    public double getMaxChurn() {
        double max = getMaxLinesAdded() - getMaxLinesDeleted();
        return max;
    }
    // Avg Churn
    public double getAvgChurn() {
        double max = getAvgLinesAdded() - getAvgLinesDeleted();
        return max;
    }
    
    public double getDecl() {
        return -1;
    }
    
    public double getCond() {
        return cond;
    }
    
    // Else
    public Double getSumElseAdded() {
        double sum = elseDeleted.stream().mapToInt(i -> i.intValue()).sum();
        return sum;
    }

    public Double getSumElseDeleted() {
        double sum = elseDeleted.stream().mapToInt(i -> i.intValue()).sum();
        return sum;
    }
    
    
    
    public boolean isBuggy() {
        return isBuggy;
    }

}
