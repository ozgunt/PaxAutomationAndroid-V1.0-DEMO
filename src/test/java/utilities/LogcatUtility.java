package utilities;

import java.io.*;

public class LogcatUtility {

    private static Process logcatProcess;
    private static String currentLogFilePath;

    // ============================================================
    //  LOGCAT BAŞLAT (PID Tagging + Full Real Log)
    // ============================================================
    public static void startLogcat(String testName) throws Exception {

        String logDir = "logs";
        File dir = new File(logDir);
        if (!dir.exists()) dir.mkdirs();

        currentLogFilePath = logDir + "/" + testName + "_" + System.currentTimeMillis() + ".txt";

        // ---- PID GATHER ----
        String techPosPid    = getPid("com.pax.techpos");
        String sampleSalePid = getPid("com.pax.samplesalea");
        String mainAppPid    = getPid("com.pax.mainapp");

        System.out.println("📌 PID listesi:");
        System.out.println("➡ com.pax.techpos     → " + techPosPid);
        System.out.println("➡ com.pax.samplesalea → " + sampleSalePid);
        System.out.println("➡ com.pax.mainapp     → " + mainAppPid);

        // ---- LOGCAT PROCESS ----
        ProcessBuilder pb = new ProcessBuilder("adb", "logcat", "-v", "time");
        logcatProcess = pb.start();

        Thread t = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(logcatProcess.getInputStream()));
                 FileWriter fw = new FileWriter(currentLogFilePath))
            {
                String line;
                while ((line = br.readLine()) != null) {

                    String prefix = "";

                    // PID’ye göre paket adını otomatik ekle
                    if (techPosPid != null && line.contains(" " + techPosPid + " "))
                        prefix = "[com.pax.techpos] ";
                    else if (sampleSalePid != null && line.contains(" " + sampleSalePid + " "))
                        prefix = "[com.pax.samplesalea] ";
                    else if (mainAppPid != null && line.contains(" " + mainAppPid + " "))
                        prefix = "[com.pax.mainapp] ";

                    fw.write(prefix + line + System.lineSeparator());
                }

            } catch (Exception e) {
                System.out.println("⚠️ Logcat thread hatası → " + e.getMessage());
            }
        });
        t.setDaemon(true);
        t.start();

        System.out.println("📄 Logcat kayıt başladı → " + currentLogFilePath);
    }

    // ============================================================
    //  LOGCAT DURDUR
    // ============================================================
    public static void stopLogcat() {
        try {
            if (logcatProcess != null) {
                logcatProcess.destroy();
                System.out.println("🛑 Logcat kayıt durduruldu.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Logcat durdurulamadı → " + e.getMessage());
        }
    }

    // ============================================================
    //  PAX LOG KLASÖRLERİNİ ÇEK
    // ============================================================
    public static void pullPaxLogs(String testName) {
        try {
            String outputDir = "logs/pax_" + testName + "_" + System.currentTimeMillis();
            File dir = new File(outputDir);
            dir.mkdirs();

            String[] paths = {
                    "/sdcard/pax/log/",
                    "/sdcard/Pax/log/",
                    "/sdcard/log/",
                    "/data/local/tmp/log/"
            };

            for (String path : paths) {

                Process p = new ProcessBuilder("adb", "shell", "ls", path).start();
                int exit = p.waitFor();

                if (exit == 0) {
                    System.out.println("📁 PAX log klasörü bulundu: " + path);

                    new ProcessBuilder("adb", "pull", path, outputDir)
                            .start()
                            .waitFor();

                    System.out.println("📥 PAX logları çekildi → " + outputDir);
                    return;
                }
            }

            System.out.println("ℹ️ Bilinen PAX log klasörleri bulunamadı.");

        } catch (Exception e) {
            System.out.println("⚠️ PAX logları alınamadı → " + e.getMessage());
        }
    }

    // ============================================================
    //  PID ÇEKME (com.pax.* Uygulamaları için)
    // ============================================================
    private static String getPid(String packageName) {
        try {
            Process p = new ProcessBuilder("adb", "shell", "pidof", packageName).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String pid = br.readLine();

            if (pid != null && !pid.trim().isEmpty()) {
                return pid.trim();
            }

        } catch (Exception ignored) {}

        return null;
    }
}
