// src/test/java/runners/PipeLineRunner.java
package runners;

import java.io.File;

public class PipeLineRunner {

    public static void main(String[] args) {
        try {
            // Proje kökü
            String projectDir = System.getProperty("user.dir");
            String scriptPath = projectDir + File.separator + "run-pipeline.bat";

            System.out.println("Proje dizini : " + projectDir);
            System.out.println("Çalışacak script: " + scriptPath);

            // cmd.exe /c run-pipeline.bat
            ProcessBuilder pb = new ProcessBuilder(
                    "C:\\Windows\\System32\\cmd.exe",
                    "/c",
                    scriptPath
            );

            // Çalışma dizini proje kökü olsun
            pb.directory(new File(projectDir));

            // Maven çıktısı doğrudan IntelliJ konsoluna aksın
            pb.inheritIO();

            Process process = pb.start();
            int exitCode = process.waitFor();

            System.out.println("Pipeline bitti. Exit code = " + exitCode);

            // IDE'de kırmızı/yeşil görünsün
            if (exitCode != 0) {
                System.exit(exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
