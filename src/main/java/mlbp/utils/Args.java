package mlbp.utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Args {

    public static String propKey = "prop";
    public static String clientKey = "client";

    CommandLine cmd;

    public Args(String[] args) {
        Options options = new Options();

        // Example of file prop.conf given by input
        Option prop = new Option("p", propKey, true, "Full path of properties file. If not provided '" + PropDefault.defaultFilePropName + "' in the working directory will be used");
        prop.setRequired(false);
        options.addOption(prop);

        Option client = new Option("c", clientKey, true, "Full path of client's list file. Default file defined in '" + PropDefault.defaultFilePropName + "'");
        client.setRequired(false);
        options.addOption(client);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("java - jar -p prop.conf -c cli.conf ", options);

            System.exit(1);
            return;
        }

    }

    public String getArg(String key, String def) {
        return cmd.getOptionValue(key, def);
    }

}
