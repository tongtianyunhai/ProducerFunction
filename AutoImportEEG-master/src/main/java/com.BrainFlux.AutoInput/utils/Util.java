package utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Util {

    /**
     * Generate info inside a exception
     *
     * @return String
     */
    public static String stackTraceErrorToString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
