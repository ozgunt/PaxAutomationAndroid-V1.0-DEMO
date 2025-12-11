package utilities;

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
     *  - (istersen) ileride JSON slip satırlarını
     * ayrı dosyalara çıkarır.
     *
     * Bu metot TEK PASSTA okuyup biter; blokta kalmaz, thread açmaz.
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

            boolean inCrashBlock      = false;
            int crashLinesRemaining   = 0;   // crash başlangıcından sonra kaç satır daha alalım

            for (String line : lines) {

                // Crash başlangıcını yakala
                if (line.contains("beginning of crash") || line.contains("FATAL EXCEPTION")) {
                    inCrashBlock = true;
                    crashLinesRemaining = 50;   // başlangıç + 50 satır context yeter
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

                // JSON slip satırı (şimdilik basit: içinde { ve } geçen satırlar)
                // Burayı istersen ileride net pattern'e göre daraltırız.
                if (line.contains("{") && line.contains("}")) {
                    jsonOut.add(line);
                }
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
}
