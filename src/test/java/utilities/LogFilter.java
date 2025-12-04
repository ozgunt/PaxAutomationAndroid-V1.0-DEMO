package utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * RAW log dosyasını (adb logcat -b all) analiz edip;
 *  - crash
 *  - exception
 *  - jsonSlip
 *  - test
 * klasörlerine ayrı loglar üretir.
 *
 * RAW dosyaya DOKUNMAZ, sadece okur.
 */
public class LogFilter {

    private static final String[] APP_PACKAGES = {
            "com.pax.samplesalea",
            "com.pax.techpos",
            "com.pax.mainapp",
            "com.pax.manager"
    };

    // Exception logunda görmek istemediğin Appium/Selenium satırları
    private static final String[] APPIUM_MARKERS = {
            "io.appium",
            "org.openqa.selenium",
            "selenium.",
            "org.apache.http",
            "sun.reflect.",
            "jdk.internal.reflect"
    };

    private static boolean containsAny(String line, String[] tokens) {
        for (String t : tokens) {
            if (line.contains(t)) return true;
        }
        return false;
    }

    private static boolean containsIgnoreCase(String line, String token) {
        return line.toLowerCase().contains(token.toLowerCase());
    }

    /**
     * @param rawLogPath   LogcatUtility.stopLogcat() ile dönen RAW log yolu
     * @param scenarioName Senaryo adı
     * @param failed       Senaryo FAIL ise true (test log için)
     */
    public static void processRawLog(String rawLogPath, String scenarioName, boolean failed) {

        if (rawLogPath == null) return;
        File rawFile = new File(rawLogPath);
        if (!rawFile.exists()) return;

        try {
            Path baseDir      = Path.of("logs");
            Path crashDir     = baseDir.resolve("crash");
            Path exceptionDir = baseDir.resolve("exception");
            Path jsonSlipDir  = baseDir.resolve("jsonSlip");
            Path testDir      = baseDir.resolve("test");

            Files.createDirectories(crashDir);
            Files.createDirectories(exceptionDir);
            Files.createDirectories(jsonSlipDir);
            Files.createDirectories(testDir);

            String safeScenario = scenarioName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String ts = new SimpleDateFormat("yyyyMMdd-HHmmssSSS")
                    .format(new Date(rawFile.lastModified()));

            File crashFile     = crashDir.resolve(safeScenario + "_" + ts + ".log").toFile();
            File exceptionFile = exceptionDir.resolve(safeScenario + "_" + ts + ".log").toFile();
            File jsonSlipFile  = jsonSlipDir.resolve(safeScenario + "_" + ts + ".log").toFile();
            File testFile      = testDir.resolve(safeScenario + "_" + ts + ".log").toFile();

            BufferedWriter crashWriter     = null;
            BufferedWriter exceptionWriter = null;
            BufferedWriter jsonSlipWriter  = null;
            BufferedWriter testWriter      = null;

            // 🔹 jsonSlip satırlarını önce buffer’da toplayacağız
            StringBuilder jsonSlipBuffer = new StringBuilder();

            // 🔹 Son slip bilgileri (her yeni slip geldiğinde override edilecek → son slip kalacak)
            String lastStanNo        = null;
            String lastTranNo        = null;
            String lastAcqName       = null;
            String lastRrn           = null;
            String lastBankaRefNo    = null;

            // 🔹 Crash bloğu için flag
            boolean inCrashBlock = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(rawFile))) {
                String line;

                while ((line = reader.readLine()) != null) {

                    boolean hasAppPackage = containsAny(line, APP_PACKAGES);

                    // ---------- CRASH ----------
                    // beginning of crash, FATAL EXCEPTION, CRASH içeren satırlar
                    boolean isCrashLine =
                            line.contains("beginning of crash") ||
                                    containsIgnoreCase(line, "FATAL EXCEPTION") ||
                                    line.contains(" CRASH ");

                    if (isCrashLine) {
                        if (crashWriter == null) {
                            crashWriter = new BufferedWriter(new FileWriter(crashFile, true));
                        }
                        inCrashBlock = true;
                    }

                    // crash bloğu açıksa satırları yaz
                    if (inCrashBlock && crashWriter != null) {
                        crashWriter.write(line);
                        crashWriter.newLine();
                    }

                    // Çok agresif bitiş kontrolüne girmiyorum, RAW sonuna kadar blok devam etsin.


                    // ---------- EXCEPTION (Appium hariç) ----------
                    boolean looksLikeException =
                            containsIgnoreCase(line, "Exception") ||
                                    containsIgnoreCase(line, "Caused by:") ||
                                    containsIgnoreCase(line, "java.lang.");

                    boolean isAppium = containsAny(line, APPIUM_MARKERS);

                    if (looksLikeException && !isAppium) {
                        if (exceptionWriter == null) {
                            exceptionWriter = new BufferedWriter(new FileWriter(exceptionFile, true));
                        }
                        exceptionWriter.write(line);
                        exceptionWriter.newLine();
                    }

                    // ---------- JSON SLIP TXT (sadece buffer'a yaz) ----------
                    if (line.contains("Slip Json")
                            || line.contains("slipLines[")
                            || line.contains("\"slipLines\"")
                            || line.contains("merchantSlip")
                            || line.contains("customerSlip")) {

                        jsonSlipBuffer.append(line).append(System.lineSeparator());
                    }

                    // 🔹 merchantSlip JSON'u yakala → stanNo / tranNo / acqName / rrn
                    if (line.contains("\"merchantSlip\"")) {
                        try {
                            int idx = line.indexOf("{\"merchantSlip\"");
                            if (idx >= 0) {
                                String jsonStr = line.substring(idx).trim();
                                JsonObject outer = JsonParser.parseString(jsonStr).getAsJsonObject();
                                if (outer.has("merchantSlip")) {
                                    String innerJsonStr = outer.get("merchantSlip").getAsString();
                                    JsonObject inner = JsonParser.parseString(innerJsonStr).getAsJsonObject();

                                    if (inner.has("stanNo")) {
                                        lastStanNo = String.valueOf(inner.get("stanNo").getAsInt());
                                    }
                                    if (inner.has("tranNo")) {
                                        lastTranNo = String.valueOf(inner.get("tranNo").getAsInt());
                                    }
                                    if (inner.has("acqName")) {
                                        lastAcqName = inner.get("acqName").getAsString();
                                    }
                                    if (inner.has("rrn")) {
                                        lastRrn = inner.get("rrn").getAsString();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // 🔹 slipLines satırlarından İŞLEM NO / STAN çek
                    if (line.contains("İŞLEM NO:") && line.contains("STAN:")) {
                        try {
                            String part = line.substring(line.indexOf("İŞLEM NO:") + "İŞLEM NO:".length());
                            part = part.replace("\"", "");
                            String[] tokens = part.trim().split("\\s+");
                            if (tokens.length > 0) {
                                lastTranNo = stripLeadingZeros(tokens[0]);
                            }
                            int idxStan = part.indexOf("STAN:");
                            if (idxStan >= 0) {
                                String stanPart = part.substring(idxStan + "STAN:".length());
                                String[] stanTokens = stanPart.trim().split("\\s+");
                                if (stanTokens.length > 0) {
                                    lastStanNo = stripLeadingZeros(stanTokens[0]);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // 🔹 slipLines satırından RRN çek
                    if (line.contains("RRN:")) {
                        try {
                            String marker = "RRN:";
                            int idxRrn = line.indexOf(marker);
                            if (idxRrn >= 0) {
                                String rest = line.substring(idxRrn + marker.length()).trim();
                                String[] parts = rest.split("\\s+");
                                if (parts.length > 0) {
                                    lastRrn = parts[0];
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // 🔹 slipLines satırından BANKA REFERANS: numarasını çek
                    if (line.contains("BANKA REFERANS:")) {
                        try {
                            String marker = "BANKA REFERANS:";
                            int idx = line.indexOf(marker);
                            if (idx >= 0) {
                                String rest = line.substring(idx + marker.length()).trim();
                                String[] parts = rest.split("\\s+");
                                if (parts.length > 0) {
                                    lastBankaRefNo = parts[0];
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // 🔹 slipLines içindeki t1'lerden acqName yakala (fallback)
                    if (line.contains("\"t1\":\"")) {
                        try {
                            int idx = line.indexOf("\"t1\":\"");
                            while (idx >= 0) {
                                int start = idx + "\"t1\":\"".length();
                                int end = line.indexOf("\"", start);
                                if (end > start) {
                                    String t1 = line.substring(start, end);
                                    String trimmed = t1.trim();

                                    if (!trimmed.startsWith("BANKA REFERANS")
                                            && trimmed.contains("BANKASI")) {
                                        lastAcqName = trimmed;
                                    }
                                }
                                idx = line.indexOf("\"t1\":\"", end);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // ---------- TEST LOG (yalnızca FAIL senaryolarda) ----------
                    if (failed && hasAppPackage) {
                        if (testWriter == null) {
                            testWriter = new BufferedWriter(new FileWriter(testFile, true));
                        }
                        testWriter.write(line);
                        testWriter.newLine();
                    }
                }
            } finally {

                // 🔚 jsonSlip buffer doluysa şimdi dosyaya yaz
                try {
                    if (jsonSlipBuffer.length() > 0) {
                        jsonSlipWriter = new BufferedWriter(new FileWriter(jsonSlipFile, true));
                        jsonSlipWriter.write(jsonSlipBuffer.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (jsonSlipWriter != null) {
                        try { jsonSlipWriter.close(); } catch (IOException ignored) {}
                    }
                }

                if (crashWriter != null)     crashWriter.close();
                if (exceptionWriter != null) exceptionWriter.close();
                if (testWriter != null)      testWriter.close();
            }

            // 🔚 Tüm raw log okundu → eldeki SON slip değerlerini Configuration.properties'e yaz
            saveLastTransactionToConfig(
                    lastStanNo,
                    lastAcqName,
                    lastTranNo,
                    lastRrn,
                    lastBankaRefNo
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveLastTransactionToConfig(
            String stanNo,
            String acqName,
            String tranNo,
            String rrn,
            String bankaRefNo
    ) {
        if (stanNo == null && acqName == null && tranNo == null && rrn == null && bankaRefNo == null) {
            return;
        }

        try {
            String configPath = System.getProperty("user.dir") + "/src/test/java/Configuration.properties";
            File configFile = new File(configPath);

            Properties props = new Properties();

            if (configFile.exists()) {
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    props.load(fis);
                }
            }

            if (stanNo != null) {
                props.setProperty("sonIslemStanNo", stanNo);
            }
            if (acqName != null) {
                props.setProperty("sonIslemAcqName", acqName);
            }
            if (tranNo != null) {
                props.setProperty("sonIslemTranNo", tranNo);
            }
            if (rrn != null) {
                props.setProperty("sonIslemRrn", rrn);
            }
            if (bankaRefNo != null) {
                props.setProperty("sonIslemBankaReferansNo", bankaRefNo);
            }

            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                props.store(fos, "Last transaction info updated by LogFilter");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // "000375" → "375"
    private static String stripLeadingZeros(String value) {
        if (value == null) return null;
        return value.replaceFirst("^0+(?!$)", "");
    }
}
