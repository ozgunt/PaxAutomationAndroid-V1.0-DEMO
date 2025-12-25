package utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class LogFilter {

    private static final Logger logger = LoggerUtil.getLogger();

    /**
     * RAW log dosyasından:
     *  - crash bloklarını (beginning of crash / FATAL EXCEPTION)
     *  - Exception satırlarını
     *  - JSON benzeri satırları
     * ayrı dosyalara çıkarır.
     *
     * Ek olarak:
     *  - RAW log içinden EN SON geçen Slip JSON'u (mercSlipName içeren) yakalar
     *  - Configuration.properties içine sonIslem* alanlarını yazar
     */
    public static void processRawLog(String rawPath, String scenarioName, boolean failed) {
        if (rawPath == null || rawPath.isEmpty()) {
            logger.warn("LogFilter: rawPath boş, işlem yapılmadı.");
            return;
        }

        Path rawFile = Paths.get(rawPath);
        if (!Files.exists(rawFile)) {
            logger.warn("LogFilter: RAW dosya bulunamadı: {}", rawFile);
            return;
        }

        try {
            List<String> lines = Files.readAllLines(rawFile, StandardCharsets.UTF_8);
            if (lines.isEmpty()) {
                logger.info("LogFilter: RAW dosya boş, filtrelenecek satır yok.");
                return;
            }

            // klasörleri hazırla
            Path crashDir     = Paths.get("logs", "crash");
            Path exceptionDir = Paths.get("logs", "exception");
            Path jsonDir      = Paths.get("logs", "jsonSlip");

            Files.createDirectories(crashDir);
            Files.createDirectories(exceptionDir);
            Files.createDirectories(jsonDir);

            String safeScenario = scenarioName.replaceAll("[^a-zA-Z0-9._-]", "_");

            Path crashFile     = crashDir.resolve(safeScenario + ".log");
            Path exceptionFile = exceptionDir.resolve(safeScenario + ".log");
            Path jsonFile      = jsonDir.resolve(safeScenario + ".log");

            List<String> crashOut     = new ArrayList<>();
            List<String> exceptionOut = new ArrayList<>();
            List<String> jsonOut      = new ArrayList<>();

            boolean inCrashBlock = false;
            int crashLinesRemaining = 0; // crash başlangıcından sonra kaç satır daha alalım

            for (String line : lines) {

                // Crash başlangıcını yakala
                if (line.contains("beginning of crash") || line.contains("FATAL EXCEPTION")) {
                    inCrashBlock = true;
                    crashLinesRemaining = 50; // başlangıç + 50 satır context yeter
                }

                if (inCrashBlock) {
                    crashOut.add(line);
                    crashLinesRemaining--;
                    if (crashLinesRemaining <= 0) {
                        inCrashBlock = false;
                    }
                }

                // Genel exception satırları
                if (line.contains("Exception")) {
                    exceptionOut.add(line);
                }

                // JSON benzeri satırlar (genel dump)
                if (line.contains("{") && line.contains("}")) {
                    jsonOut.add(line);
                }
            }

            // ✅ SON İŞLEM SLIP JSON → Configuration.properties'e yaz
            String lastSlipJson = extractLastSlipJson(lines);

            // ✅ Pipeline/loop'ta slip JSON logu gecikmeli gelebiliyor.
            // RAW dosyaya düşene kadar bekle (maks 8 sn) ve tekrar oku.
            long deadline = System.currentTimeMillis() + 8000;
            while ((lastSlipJson == null || lastSlipJson.isBlank()) && System.currentTimeMillis() < deadline) {
                try {
                    Thread.sleep(500); // 0.5 sn bekle
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }

                try {
                    List<String> retryLines = Files.readAllLines(rawFile, StandardCharsets.UTF_8);
                    lastSlipJson = extractLastSlipJson(retryLines);
                } catch (IOException ignore) {
                    break;
                }
            }

            if (lastSlipJson != null && !lastSlipJson.isBlank()) {
                jsonOut.add("=== LAST SLIP JSON (picked for config update) ===");
                jsonOut.add(lastSlipJson);

                try {
                    JsonObject obj = JsonParser.parseString(lastSlipJson).getAsJsonObject();

                    String rrn       = obj.has("rrn")       ? obj.get("rrn").getAsString() : null;
                    String bankRefNo = obj.has("bankRefNo") ? obj.get("bankRefNo").getAsString() : null;
                    String acqName   = obj.has("acqName")   ? obj.get("acqName").getAsString() : null;
                    String tranNo    = obj.has("tranNo")    ? String.valueOf(obj.get("tranNo").getAsInt()) : null;
                    String stanNo    = obj.has("stanNo")    ? String.valueOf(obj.get("stanNo").getAsInt()) : null;

                    if (rrn != null)       ConfigReader.setProperty("sonIslemRrn", rrn);
                    if (bankRefNo != null) ConfigReader.setProperty("sonIslemBankaReferansNo", bankRefNo);
                    if (acqName != null)   ConfigReader.setProperty("sonIslemAcqName", acqName);
                    if (tranNo != null)    ConfigReader.setProperty("sonIslemTranNo", tranNo);
                    if (stanNo != null)    ConfigReader.setProperty("sonIslemStanNo", stanNo);

                    logger.info("LogFilter: sonIslem* alanları Configuration.properties'e yazıldı. (rrn={}, stanNo={})", rrn, stanNo);

                } catch (Exception ex) {
                    logger.error("LogFilter: Slip JSON parse edilemedi → Configuration.properties güncellenmedi.", ex);
                }

            } else {
                logger.warn("LogFilter: Slip JSON bulunamadı → Configuration.properties güncellenmedi.");
            }

            if (!crashOut.isEmpty()) {
                Files.write(crashFile, crashOut, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                logger.info("LogFilter: Crash log yazıldı → {}", crashFile);
            }

            if (!exceptionOut.isEmpty()) {
                Files.write(exceptionFile, exceptionOut, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                logger.info("LogFilter: Exception log yazıldı → {}", exceptionFile);
            }

            if (!jsonOut.isEmpty()) {
                Files.write(jsonFile, jsonOut, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                logger.info("LogFilter: JSON benzeri satırlar yazıldı → {}", jsonFile);
            }

            logger.info("LogFilter: RAW log işlendi (senaryo: {}).", scenarioName);

        } catch (IOException e) {
            logger.error("LogFilter: RAW dosya okunurken/yazılırken IO hatası oluştu", e);
        } catch (Exception e) {
            logger.error("LogFilter: processRawLog çalışırken beklenmeyen hata oluştu", e);
        }
    }

    // ✅ RAW log içinde en sonda geçen "MainActivity: { ... mercSlipName ... }" satırını yakala
    private static String extractLastSlipJson(List<String> lines) {

        for (int i = lines.size() - 1; i >= 0; i--) {
            String line = lines.get(i);

            // En sağlam hedef: direkt slip json satırı
            if (line.contains("MainActivity:") && line.contains("{") && line.contains("}")
                    && line.contains("\"mercSlipName\"")) {

                int s = line.indexOf('{');
                int e = line.lastIndexOf('}');
                if (s >= 0 && e > s) {
                    return line.substring(s, e + 1);
                }
            }

            // Fallback: "Slip Json:" satırından sonra birkaç satır içinde json olabilir
            if (line.contains("Slip Json:")) {
                for (int j = i + 1; j < Math.min(i + 6, lines.size()); j++) {
                    String nxt = lines.get(j);
                    if (nxt.contains("{") && nxt.contains("}") && nxt.contains("\"mercSlipName\"")) {
                        int s = nxt.indexOf('{');
                        int e = nxt.lastIndexOf('}');
                        if (s >= 0 && e > s) {
                            return nxt.substring(s, e + 1);
                        }
                    }
                }
            }
        }

        return null;
    }
}
