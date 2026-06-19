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

        // GÜVENLİK DUVARI 1: Cihazın log buffer boyutunu sessizce büyüt
        try {
            Process pG = new ProcessBuilder("adb", "logcat", "-G", "16M").start();
            pG.waitFor(800, TimeUnit.MILLISECONDS);
            pG.destroyForcibly();
        } catch (Exception ignored) {}

        // GÜVENLİK DUVARI 2: 'adb logcat -c' komutunun testi sonsuza kadar kilitlemesini KESİN olarak engelliyoruz
        try {
            ProcessBuilder pbClear = new ProcessBuilder("adb", "logcat", "-c");
            Process clearProcess = pbClear.start();

            // Cihaza logları temizlemesi için maksimum 1 saniye veriyoruz.
            // Pax cihazı kilitlendiyse veya yanıt vermiyorsa beklemeyi bırakıp zorla kapatıyoruz.
            boolean cleared = clearProcess.waitFor(1000, TimeUnit.MILLISECONDS);
            if (!cleared) {
                clearProcess.destroyForcibly();
                System.out.println("⚠️ UYARI: Pax cihazı meşgul, log temizleme limitini aştığı için es geçildi.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ UYARI: Log temizleme sırasında hata yok sayıldı: " + e.getMessage());
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
            // Sadece gerekli buffer'ları çekerek cihazın işlemcisini yormuyoruz
            ProcessBuilder pbDump = new ProcessBuilder(
                    "adb", "logcat",
                    "-d",
                    "-b", "main", "-b", "events",
                    "-v", "threadtime"
            );
            pbDump.redirectErrorStream(true);
            pbDump.redirectOutput(new File(currentRawLogPath));

            Process dumpProcess = pbDump.start();
            boolean dumped = dumpProcess.waitFor(4, TimeUnit.SECONDS);
            if (!dumped) {
                dumpProcess.destroyForcibly();
                System.out.println("⚠️ UYARI: Log dökümü zaman aşımına uğradı, süreç sonlandırıldı.");
            }
        } catch (Exception e) {
            System.err.println("Error during logcat dump: " + e.getMessage());
        }

        System.out.println("LOGCAT DUMP COMPLETED → " + currentRawLogPath);
        return currentRawLogPath;
    }
}