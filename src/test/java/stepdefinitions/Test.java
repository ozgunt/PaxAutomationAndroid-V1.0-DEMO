package stepdefinitions;

import utilities.ConfigReader;

public class Test {

    @org.junit.jupiter.api.Test
    public void main() {

        System.out.println(ConfigReader.getProperty("ziraat1ComboKartNoKK"));

    }

}
