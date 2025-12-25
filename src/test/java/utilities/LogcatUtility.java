package utilities;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class LogcatUtility {

    private static String currentRawLogPath;

    public static String getCurrentRawLogPath() {
        return currentRawLogPath;
    }

    public static synchronized void startLogcat(String scenarioName) throws Exception {

        String logDir = "logs/raw";
        File dir = new File(logDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String safeName = scenarioName.replaceAll("[^a-zA-Z0-9._-]", "_");
        currentRawLogPath = logDir + "/" + safeName + "_" + System.currentTimeMillis() + ".log";

        // Clear logcat buffer to start fresh for this scenario
        ProcessBuilder pbClear = new ProcessBuilder("adb", "logcat", "-c");
        Process clearProcess = pbClear.start();
        boolean cleared = clearProcess.waitFor(5, TimeUnit.SECONDS);
        if (!cleared) {
            clearProcess.destroyForcibly();
        }

        System.out.println("LOGCAT CLEARED AND READY → " + currentRawLogPath);
    }

    public static synchronized String stopLogcat() {

        if (currentRawLogPath == null) {
            System.out.println("No logcat path set, skipping dump.");
            return null;
        }

        System.out.println("DUMPING LOGCAT → " + currentRawLogPath);

        try {
            ProcessBuilder pbDump = new ProcessBuilder(
                    "adb", "logcat",
                    "-d", "-b", "all",
                    "-v", "threadtime"
            );
            pbDump.redirectErrorStream(true);
            pbDump.redirectOutput(new File(currentRawLogPath));

            Process dumpProcess = pbDump.start();
            boolean dumped = dumpProcess.waitFor(10, TimeUnit.SECONDS);
            if (!dumped) {
                dumpProcess.destroyForcibly();
            }
        } catch (Exception e) {
            System.err.println("Error during logcat dump: " + e.getMessage());
        }

        System.out.println("LOGCAT DUMP COMPLETED → " + currentRawLogPath);
        return currentRawLogPath;
    }
}