package helper;

import java.io.*;
import java.io.PrintWriter;

/**
 * contains the method to record login data
 */
public class ActivityLog {

    /**
     * keeps a record of all login activity
     *
     * @param s string to append to the file "login_activity.txt"
     * @throws IOException
     */
    public static void activityLog(String s) throws IOException {
        String fileName = "login_activity.txt";

        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(fileName, true));
            printWriter.append(s);
            printWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
