package org.anantacreative.updater.Utilites;

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
    private BufferedReader input;
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
            final Process command = re.exec(actualArgs.toArray(new String[0]));
            error = new BufferedReader(new InputStreamReader(command.getErrorStream()));
            input = new BufferedReader(new InputStreamReader(command.getInputStream()));

            command.waitFor();
            this.exitVal = command.exitValue();
            if (this.exitVal != 0) {
                throw new IOException("Failed to execute jar, " + this.getExecutionLog());
            }
            if(printLog) getExecutionLog();

        } catch (final IOException | InterruptedException e) {
            throw new JarExecutorException(e.getMessage(), e);
        }catch (Exception e){
            throw new JarExecutorException(e.getMessage(), e);
        }finally {
            try {
                error.close();
                input.close();
            } catch (IOException e) {}
        }
    }

    public String getExecutionLog() throws IOException {
        String line;
        StringBuilder output = new StringBuilder("exitVal: " + this.exitVal);
        output.append(", error: ");
        while((line = error.readLine()) != null)  output.append("\n ").append(line);
        while((line = input.readLine()) != null) output.append("\n ").append(line);
        return output.toString();
    }

    public static class JarExecutorException extends Exception {
        public JarExecutorException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
