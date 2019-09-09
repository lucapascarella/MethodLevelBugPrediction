package mlbp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class Prop {

    /*
     * How to add a property:
     * 
     * The properties could be of 3 types: (1) default, (2) loaded from file, and
     * (3) overwritten by shell arguments
     * 
     * First, create an unique static Key string in this file.
     * public static String LucaKey = "Luca";
     * 
     * Second, create an unique static Value in PropDefault file.
     * public static String LucaVal = "Luca";
     * 
     * Third, add the source line to generateDefaultPropFile() method.
     * ps.println(genLine(LucaKey, PropDefault.LucaVal));
     * 
     */

    // Program informations keys
    public static String progNameKey = "progName";
    public static String progVersionKey = "progVersion";
    // Preliminary settings keys
    public static String propFileNameKey = "propFileName";
    public static String workDirKey = "workDir";
    public static String projectListKey = "projsList";
    public static String cliListKey = "cliList";
    public static String outputDirKey = "outDir";
    public static String csvResultKey = "csvRes";
    public static String summaryResultKey = "sumRes";
    public static String printCommandKey = "command";
    public static String printOutputKey = "output";
    public static String printErrorKey = "error";

    public static String fileSepKey = "fileSep";
    public static String pathSepKey = "pathSep";

    // MySQL keys
    public static String MySQLAddressKey = "mySQLAddress";
    public static String MySQLPortKey = "MySQLPort";
    public static String MySQLUserKey = "MySQLUser";
    public static String MySQLPasswordKey = "MySQLPassword";
    public static String MySQLDatabaseNameKey = "MySQLTable";

    // Machine settings keys
    public static String localPortKey = "localPort";
    
    // Program specific
    public static String tagAnalysisKey = "tag";
    public static String branchAnalysisKey = "branches";

    private String propFileName;
    private Properties prop;
    private Args args;

    public Prop(Args a) {
        args = a;
        propFileName = args.getArg(Args.propKey, PropDefault.defaultFilePropName);
        checkAndLoadProp();
    }

    private void checkAndLoadProp() {
        prop = new Properties();
        File file = new File(propFileName);
        // Check file existence and generate it if required
        if (!file.exists())
            generateDefaultPropFile();
        loadProp();
    }

    private void loadProp() {
        try {
            // Open and load properties file in stream mode
            FileInputStream inStream = new FileInputStream(propFileName);
            prop.load(inStream);
            inStream.close();
            // Override properties with command line arguments
            prop.put(cliListKey, args.getArg(Args.clientKey, PropDefault.defaultCliList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateDefaultPropFile() {
        try {
            OutputStream output = new FileOutputStream(propFileName);
            PrintStream ps = new PrintStream(output);
            // Generate default properties template and fill it
            ps.println("# Auto generated properies file");
            ps.println("# Properties version: v" + PropDefault.defaultPropVersion);
            ps.println();
            ps.println("# File and path separators OS dependent");
            ps.println(genLine(fileSepKey, "" + PropDefault.getFileSeparator()));
            ps.println(genLine(pathSepKey, "" + PropDefault.getPathSeparator()));
            ps.println("# Working directory, projects and clients list");
            ps.println(genLine(workDirKey, PropDefault.defaultWorkingDirectory));
            ps.println(genLine(projectListKey, PropDefault.defaultProjectsList));
            ps.println(genLine(cliListKey, PropDefault.defaultCliList));
            ps.println(genLine(outputDirKey, PropDefault.defaultOutputDirectory));
            ps.println(genLine(csvResultKey, PropDefault.defaultCsvRes));
            ps.println(genLine(summaryResultKey, PropDefault.defaultSummaryRes));
            ps.println(genLine(printCommandKey, PropDefault.defaultPrintCommand));
            ps.println(genLine(printOutputKey, PropDefault.defaultPrintOutput));
            ps.println(genLine(printErrorKey, PropDefault.defaultPrintError));
            // MySQL properties
            ps.println(genLine(MySQLAddressKey, PropDefault.defaultMySQLAddress));
            ps.println(genLine(MySQLPortKey, PropDefault.defaultMySQLPort));
            ps.println(genLine(MySQLUserKey, PropDefault.defaultMySQLUser));
            ps.println(genLine(MySQLPasswordKey, PropDefault.defaultMySQLPassword));
            ps.println(genLine(MySQLDatabaseNameKey, PropDefault.defaultMySQLDatabaseName));
            // Machine settings
            ps.println(genLine(localPortKey, PropDefault.defaultLocalPort));
            // Program specific
            ps.println("# Tags and Branches selection");
            ps.println(genLine(tagAnalysisKey, PropDefault.defTagAnalysis));
            ps.println(genLine(branchAnalysisKey, PropDefault.defBranchAnalysis));
            
            // Flush and close file
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String genLine(String key, String val) {
        if (prop == null) {
            return key + "=" + val;
        } else {
            if (prop.containsKey(key)) {
                return key + "=" + prop.getProperty(key);
            } else {
                return key + "=" + val;
            }
        }
    }

    public void close() {
        // Eventually implemented
    }

    /*
     * Return property value or null if not found
     */
    public String getProperty(String key) {
        if (!prop.containsKey(key)) {
            if (key.equals(progNameKey)) {
                return PropDefault.progName;
            } else if (key.equals(progVersionKey)) {
                return PropDefault.defaultPropVersion;
            } else {
                generateDefaultPropFile();
                loadProp();
            }
        }
        return prop.getProperty(key);
    }

    /*
     * Return property value or default value if not found in loaded file
     */
    public String getProperty(String key, String def) {
        if (!prop.containsKey(key)) {
            if (key.equals(progNameKey)) {
                return PropDefault.progName;
            } else if (key.equals(progVersionKey)) {
                return PropDefault.defaultPropVersion;
            } else {
                prop.put(key, def);
                generateDefaultPropFile();
                loadProp();
            }
        }
        return prop.getProperty(key, def);
    }
}
