package stepdefinitions;

import utilities.ConfigReader;
import utilities.ConfigReader;

public class Test {



        public static void main(String[] args) {

            String stanNo = ConfigReader.getProperty("sonIslemStanNo");
            String acqName = ConfigReader.getProperty("sonIslemAcqName");
            String tranNo = ConfigReader.getProperty("sonIslemTranNo");
            String rrn = ConfigReader.getProperty("sonIslemRrn");
            String bankaRef = ConfigReader.getProperty("sonIslemBankaReferansNo");

            System.out.println("===== CONFIGURATION.properties SON İŞLEM BİLGİLERİ =====");
            System.out.println("sonIslemStanNo         = " + stanNo);
            System.out.println("sonIslemTranNo         = " + tranNo);
            System.out.println("sonIslemAcqName        = " + acqName);
            System.out.println("sonIslemRrn            = " + rrn);
            System.out.println("sonIslemBankaReferansNo= " + bankaRef);
            System.out.println("========================================================");
        }
    }



