package utilities;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NextTcFinder {

    public static void main(String[] args) throws Exception {
        File dir = new File("src/test/resources");
        Pattern p = Pattern.compile("TC(\\d+)");
        int max = 0;

        for (File f : dir.listFiles((d, n) -> n.endsWith(".feature"))) {
            String content = Files.readString(f.toPath());
            Matcher m = p.matcher(content);
            while (m.find()) {
                max = Math.max(max, Integer.parseInt(m.group(1)));
            }
        }

        System.out.println("\n✅ Next Test Case: TC" + (max + 1));
    }
}
