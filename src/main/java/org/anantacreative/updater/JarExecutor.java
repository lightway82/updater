package org.anantacreative.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Исполняет jar файлы
 */
public class JarExecutor {

    private BufferedReader error;
    private BufferedReader op;
    private int exitVal;
    private String jarFilePath;
    private List<String> args;


    public JarExecutor(String jarFilePath, List<String> args) {
        this.jarFilePath = jarFilePath;
        this.args = args;
    }

    public void execute(boolean printLog) throws JarExecutorException {


        final List<String> actualArgs = new ArrayList<String>();
        actualArgs.add(0, "java");
        actualArgs.add(1, "-jar");
        actualArgs.add(2, jarFilePath);
        actualArgs.addAll(args);
        try {
            final Runtime re = Runtime.getRuntime();
            //final Process command = re.exec(cmdString, args.toArray(new String[0]));
            final Process command = re.exec(actualArgs.toArray(new String[0]));
            this.error = new BufferedReader(new InputStreamReader(command.getErrorStream()));
            this.op = new BufferedReader(new InputStreamReader(command.getInputStream()));
            // Wait for the application to Finish
            command.waitFor();
            this.exitVal = command.exitValue();
            if (this.exitVal != 0) {
                throw new IOException("Failed to execure jar, " + this.getExecutionLog());
            }
            if(printLog) getExecutionLog();

        } catch (final IOException | InterruptedException e) {
            throw new JarExecutorException(e.getMessage(), e);
        }
    }

    public String getExecutionLog() {
        String error = "";
        String line;
        try {
            while((line = this.error.readLine()) != null) {
                error = error + "\n" + line;
            }
        } catch (final IOException e) {
        }
        String output = "";
        try {
            while((line = this.op.readLine()) != null) {
                output = output + "\n" + line;
            }
        } catch (final IOException e) {
        }
        try {
            this.error.close();
            this.op.close();
        } catch (final IOException e) {
        }
        return "exitVal: " + this.exitVal + ", error: " + error + ", output: " + output;
    }

    public static class JarExecutorException extends Exception {
        public JarExecutorException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
