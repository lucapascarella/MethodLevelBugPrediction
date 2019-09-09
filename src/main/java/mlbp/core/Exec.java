package mlbp.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

public class Exec {

    private String cmd;
    private File workingDirectoryDir;
    private Queue<Integer> output;
    private Queue<Integer> error;
    // private StringBuffer output;
    // private StringBuffer error;
    private Process proc;
    private ThreadedInputReader ir;
    private ThreadedInputReader er;

    private boolean printCommand;
    private boolean printOutput;
    private boolean printError;

    private String savePath;

    public Exec(String workingDirectory, String command, String output, String error) {
        workingDirectoryDir = new File(workingDirectory);
        printCommand = Boolean.parseBoolean(command);
        printOutput = Boolean.parseBoolean(output);
        printError = Boolean.parseBoolean(error);
        this.output = new LinkedList<Integer>();
        this.error = new LinkedList<Integer>();
    }

    public Exec(String workingDirectory, boolean command, boolean output, boolean error) {
        workingDirectoryDir = new File(workingDirectory);
        printCommand = command;
        printOutput = output;
        printError = error;
        this.output = new LinkedList<Integer>();
        this.error = new LinkedList<Integer>();
    }

    public void createCommand(String cmd, String savePath) {
        // Reset for a nuw command
        output.clear();
        error.clear();
        this.cmd = cmd;
        this.savePath = savePath;
        saveCommand();
    }

    public String getLastCommand() {
        return this.cmd;
    }

    public void appendToCommand(String str) {
        if (str.charAt(0) != ' ')
            this.cmd += " ";
        this.cmd += str;
    }

    public boolean execAsyncCommand() {
        return execCmmand(false);
    }

    public boolean execSyncCommand() {
        return execCmmand(true);
    }

    public boolean execCmmand(boolean waitExecuton) {

        try {
            if (printCommand)
                System.out.println("CMD: " + this.cmd);
            String args[] = new String[] { "sh", "-c", this.cmd };
            // proc = Runtime.getRuntime().exec(new String[] { "sh", "-c", this.cmd }, null,
            // this.workingDirectoryDir);

            ProcessBuilder builder = new ProcessBuilder(args);
            builder.directory(this.workingDirectoryDir);
            proc = builder.start();

            // proc.waitFor();
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            ir = new ThreadedInputReader(inputReader, output, this.printOutput);
            er = new ThreadedInputReader(errorReader, error, this.printError);
            ir.start();
            er.start();

            // Execute in synchronous way
            if (waitExecuton) {
                ir.join();
                er.join();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getReasult() {
        String res = getString(output);
        saveOutput(res);
        return res;
    }

    public String getError() {
        String res = getString(error);
        saveError(res);
        return res;
    }

    private String getString(Queue<Integer> queue) {
        StringBuilder str = new StringBuilder("");
        Integer i;
        while ((i = queue.poll()) != null)
            str.append(Character.toChars(i));
        return str.toString();
    }

    public boolean isAlive() {
        return proc.isAlive() | ir.isAlive() | er.isAlive();
    }

    private void saveOutput(String toSave) {
        if (savePath != null) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(savePath + "_out.txt");
                out.println(toSave);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        }
    }

    private void saveError(String toSave) {
        if (savePath != null) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(savePath + "_err.txt");
                out.println(toSave);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        }
    }

    private void saveCommand() {
        if (savePath != null) {
            PrintWriter out = null;
            try {
                savePath = savePath + "_cmd.txt";
                File f = new File(savePath);
                f.getParentFile().mkdirs();
                f.createNewFile();
                out = new PrintWriter(f);
                out.println(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        }
    }
    
    public void close() {
        // Eventually to implement
    }

    private class ThreadedInputReader extends Thread {

        private BufferedReader br;
        private Queue<Integer> out;
        private boolean print;

        public ThreadedInputReader(BufferedReader br, Queue<Integer> out, boolean print) {
            this.br = br;
            this.out = out;
            this.print = print;
        }

        public void run() {
            Integer i;
            try {
                if (print)
                    while ((i = br.read()) != -1) {
                        out.add(i);
                        System.out.print(Character.toChars(i));
                    }
                else
                    while ((i = br.read()) != -1) {
                        out.add(i);
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
