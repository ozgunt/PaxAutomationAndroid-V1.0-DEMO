package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {

    private LoggerUtil() {
        // Prevent instantiation
    }

    public static Logger getLogger() {
        String callerClassName = "automation";
        try {
            callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        } catch (Exception ignored) {}
        return LogManager.getLogger(callerClassName);
    }
}
