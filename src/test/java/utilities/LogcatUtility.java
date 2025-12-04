package utilities;

import java.io.*;

public class LogcatUtility {

    private static Process logcatProcess;
    private static String currentRawLogPath;

    /**
     * Şu anki RAW log dosyasının yolu (null olabilir)
     */
    public static String getCurrentRawLogPath() {
        return currentRawLogPath;
    }

    /**
     * Senaryo bazlı RAW log başlat
     * Örnek komut:
     *   adb logcat -b all -v threadtime
     */
    public static void startLogcat(String scenarioName) throws Exception {

        // Her ihtimale karşı eski process'i kapat
        stopIfRunning();

        String logDir = "logs/raw";
        File dir = new File(logDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String safeName = scenarioName.replaceAll("[^a-zA-Z0-9._-]", "_");
        currentRawLogPath = logDir + "/" + safeName + "_" + System.currentTimeMillis() + ".log";

        ProcessBuilder pb = new ProcessBuilder(
                "adb", "logcat",
                "-b", "all",        // 🔥 Tüm buffer'lar
                "-v", "threadtime"  // 🔥 Android Studio formatına yakın
        );
        pb.redirectErrorStream(true);
        logcatProcess = pb.start();

        Thread t = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(logcatProcess.getInputStream()));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(currentRawLogPath))) {

                String line;
                while ((line = br.readLine()) != null) {
                    // RAW → HİÇBİR FİLTRE YOK
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException ignored) {
            }
        });

        t.setDaemon(true);
        t.start();

        System.out.println("RAW LOGCAT BAŞLADI → " + currentRawLogPath);
    }

    /**
     * İçeriden çağırılan yardımcı:
     * logcat process çalışıyorsa zorla kapat.
     */
    private static void stopIfRunning() {
        if (logcatProcess != null && logcatProcess.isAlive()) {
            logcatProcess.destroyForcibly();
        }
    }

    /**
     * RAW logcat'i durdur ve RAW dosya yolunu döndür.
     * TestHooks içinde:
     *
     *   String rawPath = LogcatUtility.stopLogcat();
     *
     * diye kullanacaksın.
     */
    public static String stopLogcat() {
        stopIfRunning();
        System.out.println("RAW LOGCAT DURDU → " + currentRawLogPath);
        return currentRawLogPath;
    }
}
