package mlbp.utils;

public final class PropDefault {
    // Program informations
    public static String progName = "MethodLevelBugPrediction";
    public static String defaultPropVersion = "0.1";
    // Preliminary settings
    public static String defaultFilePropName = "prop.conf";
    public static String defaultWorkingDirectory = "work";
    public static String defaultProjectsList = "projs.txt";
    public static String defaultCliList = "cli.txt";
    public static String defaultOutputDirectory = "out";
    public static String defaultCsvRes = "output.csv";
    public static String defaultSummaryRes = "summary.txt";
    public static String defaultPrintCommand = "false";
    public static String defaultPrintOutput = "false";
    public static String defaultPrintError = "false";
    // MySQL values
    public static String defaultMySQLAddress = "192.168.1.3";
    public static String defaultMySQLPort = "3306";
    public static String defaultMySQLUser = "luca";
    public static String defaultMySQLPassword = "master";
    public static String defaultMySQLDatabaseName = "test";
    // Machine settings
    public static String defaultLocalPort = "1234";
    
    // Program specific
    public static String defTagAnalysis = "true";
    public static String defBranchAnalysis = "false";

    public static char getFileSeparator() {
        String rtn = System.getProperty("file.separator");
        if (rtn.length() == 1)
            return rtn.charAt(0);
        return '\0';
    }

    public static char getPathSeparator() {
        String rtn = System.getProperty("path.separator");
        if (rtn.length() == 1)
            return rtn.charAt(0);
        return '\0';
    }
    
    // Program specific settings
    //public static String defaultFilePropName = "prop.conf";
}
