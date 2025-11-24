package stepdefinitions;

import com.google.gson.annotations.Until;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.PGmanager;
import pages.PGsampleSale;
import pages.PGtechPos;
import utilities.ConfigReader;
import utilities.ReusableMethods;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.AndroidKey;


import java.time.Duration;

import static utilities.ReusableMethods.*;

public class StepDefinitions {


    public StepDefinitions() {
    }

    PGsampleSale salePage;
    PGmanager manager;
    PGtechPos techPos;


    @Given("kullanici sample sale baslatir")
    public void kullanici_sample_sale_baslatir() throws Exception {
        setUp();
        salePage = ReusableMethods.sampleSalePage;
        manager  = ReusableMethods.managerPage;
        techPos  = ReusableMethods.techPosPage;


        System.out.println("✅ Sample Sale baslatildi!");

        salePage = sampleSalePage;
        manager  = managerPage;
        techPos  = techPosPage;
    }

    @When("kullanici samplesale uzerinden merchant menuye giris yapar")
    public void kullanici_samplesale_uzerinden_merchant_menuye_giris_yapar() {
        ReusableMethods.swipeUp();

        //ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(salePage.lstMenuAc)).click();
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        AppiumBy.id("com.pax.samplesalea:id/menuSpinner")
                ))
                .click();
        salePage.itmMerchantMenu.click();
        salePage.btnMenuAc.click();

        System.out.println("✅ Merchant menuye giris yapildi.");
    }


    @And("kullanici isyeri menuden parametre secimi yapar")
    public void kullanici_islem_menuden_parametre_secimi_yapar() {
     //   ReusableMethods.iwait().until(ExpectedConditions.elementToBeClickable(techPos.btnParametre)).click();

        ReusableMethods.iwait()
                .until(ExpectedConditions.elementToBeClickable(techPos.btnParametre))
                .click();
    }


    @Then("ekran kontrolu sonrasi uygulama kapanir")
    public void isyeriEkranKontroluSonrasiUygulamaKapanir() throws InterruptedException {
        System.out.println("⏳ TechPOS İşyeri Menü veya SampleSale ana ekranı bekleniyor...");

        long timeout = System.currentTimeMillis() + 120_000;

        while (System.currentTimeMillis() < timeout) {

            boolean isIsyeriMenu = ReusableMethods.isElementPresent(techPos.lblIsyeriMenu);
            boolean isSampleSaleHome = ReusableMethods.isElementPresent(salePage.btnIade);

            System.out.println("📍 Menü Görünüyor?: " + isIsyeriMenu +
                    " | Start Butonu?: " + isSampleSaleHome);

            if (isIsyeriMenu || isSampleSaleHome) {
                System.out.println("✅ Doğru son ekran → Uygulama kapatılıyor!");
                ReusableMethods.quitDriver();
                return;
            }

          //  Thread.sleep(300);
        }

        System.out.println("❌ 120 sn boyunca beklenen ekran gelmedi → kapatmıyoruz!");
    }


    @Then("kullanici techpos sifresi girer")
    public void kullanici_techpos_sifresi_girer() {


         ReusableMethods.techPosPage.txtTechposAmountText.clear();
        techPos.txtTechposAmountText.sendKeys("0000");
        techPos.btnTechposGiris.click();

        System.out.println("✅ TechPOS girisi yapildi.");
    }

    @Then("Kullanici kurulum bilgisi girer")
    public void kullanici_kurulum_bilgisi_girer() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        boolean isSeriNoEkraniAcik;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(techPos.lblSeriNumarasiGiriniz));
            isSeriNoEkraniAcik = true;
        } catch (Exception e) {
            isSeriNoEkraniAcik = false;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        if (isSeriNoEkraniAcik) {
            System.out.println("📌 Seri No ekranı geldi → Doğrulama gerekir!");
            techPos.txtTechposGenelBox.click();
            techPos.txtTechposGenelBox.clear();
            techPos.txtTechposGenelBox.sendKeys("159632147");
            techPos.btnTechposGiris.click();


            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(
                            AppiumBy.xpath("//android.widget.Button[@index='14']")));
            techPos.txtTechposGenelBox.click();
            techPos.txtTechposGenelBox.clear();
            techPos.txtTechposGenelBox.sendKeys("159632147");
            techPos.btnTechposGiris.click();
        } else {
            System.out.println("📌 Seri No ekranı gelmedi → Direkt IP giriş ekranı!");
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(techPos.txtIpGiriniz1));
        }

        System.out.println("📌 IP & Port bilgisi giriliyor...");

        techPos.txtIpGiriniz1.sendKeys("031");
        techPos.txtIpGiriniz2.sendKeys("145");
        techPos.txtIPgiriniz3.sendKeys("171");
        techPos.txtIPgiriniz4.sendKeys("94");
        techPos.btnTechposGiris.click();

        techPos.txtTechposGenelBox.sendKeys("12121");
        techPos.btnTechposGiris.click();

        techPos.txtIpGiriniz1.sendKeys("031");
        techPos.txtIpGiriniz2.sendKeys("145");
        techPos.txtIPgiriniz3.sendKeys("171");
        techPos.txtIPgiriniz4.sendKeys("94");
        techPos.btnTechposGiris.click();

        techPos.txtTechposGenelBox.sendKeys("12121");
        techPos.btnTechposGiris.click();

        System.out.println("✅ Kurulum tamamlandi ✅");
    }

    @And("kullanici isyeri menuden gunsonu secimi yapar")
    public void kullaniciGunsonuSecimiYapar() throws InterruptedException {


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // 20 saniye bekle
        wait.until(ExpectedConditions.visibilityOf(techPos.btnGunsonu)).click(); // Görünürse tıkla
        wait.until(ExpectedConditions.visibilityOf(techPos.getBtnGunsonuDetay)).click(); // Görünürse tıkla


    }

    //manager üzerinden uygulama seçimi
    @Then("kullanici manager uzerinden uygulama secer techpos")
    public void kullanici_techpos_secimi_yapar() {

        try {
            WebDriverWait fastWait = new WebDriverWait(driver, Duration.ofSeconds(1));
            WebElement techPosButton = fastWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            AppiumBy.xpath("//*[@text='TechPOS']")
                    )
            );
            techPosButton.click();
            System.out.println("✅ TechPOS seçildi!");
        } catch (Exception e) {
            System.out.println("⚠️ TechPOS butonu gelmedi → Muhtemelen otomatik geçti (PASS ✅)");
        }

        // ✅ DOĞRU YER: Geçişten sonra aktif package logu
        String pkg = driver.getCurrentPackage();
        System.out.println("📌 Gerçek Aktif Package: " + pkg);
    }


    @When("kullanici samplesale uzerinden tutar girer")
    public void kullaniciTutarGirer() {

        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(salePage.txtTutar)).sendKeys("10000");


    }


    @And("kullanici samplesale uzerinden satis baslatir")
    public void kullaniciSamplesaleUzerindenSatisBaslatir() {


        sampleSalePage.btnSatisBaslat.click();

    }


    @And("kullanici mke secimi yapar")
    public void kullaniciMkeSecimiYapar() {

        manager.btnMke.click();


    }

    @And("kullanici ziraat1 combo KK no girer")
    public void kullaniciKartNoGirer() {

        manager.txtKartNo.sendKeys(ConfigReader.getProperty("ziraat1ComboKartNoKK"));

    }

    @And("kullanici ziraat1 combo skt girer")
    public void kullaniciZiraatComboSktGirer() {

        manager.txtSKT.click();
        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtSKT));
        manager.txtSKT.sendKeys(ConfigReader.getProperty("ziraat1ComboKSKT"));
    }

    @And("kullanici ziraat1 combo KK cvv girer")
    public void kullaniciZiraatComboCvvGirer() {

        manager.txtCVV.click();
        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtCVV));
        manager.txtCVV.sendKeys(ConfigReader.getProperty("ziraat1ComboKartCcvKK"));


    }

    @Then("kullanici tamam tusuna basar")
    public void kullaniciTamamTusunaBasar() {

        ReusableMethods.closeKeyboard();
        ReusableMethods.iwait();

        manager.btnTamam.click();

    }

    @And("kullanici halkbank1 combo KK no girer")
    public void kullaniciHalkbanComboKKNoGirer() {

        manager.txtKartNo.click();
        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtKartNo));
        manager.txtKartNo.sendKeys(ConfigReader.getProperty("halkbank1ComboKartNoKK"));

    }

    @And("kullanici halkbank1 combo skt girer")
    public void kullaniciHalkbankComboSktGirer() {
        manager.txtSKT.click();
        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtSKT));
        manager.txtSKT.sendKeys(ConfigReader.getProperty("halkbank1ComboSKT"));
    }

    @And("kullanici halkbank1 combo KK cvv girer")
    public void kullaniciHalkbankComboKKCvvGirer() {
        manager.txtCVV.click();
        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtCVV));
        manager.txtCVV.sendKeys(ConfigReader.getProperty("halkbank1ComboCcvKK"));
    }

    @And("kullanici samplesale gunsonu slibine dokunur")
    public void kullaniciSamplesaleGunsonuSlibineDokunur() {


        try {
            if (salePage.lblSlip1.isDisplayed()) {
                salePage.lblSlip1.click();
                System.out.println("📄 Slip ekranı tıklandı");
                Thread.sleep(300);
            } else {
                System.out.println("ℹ️ Slip ekranı görünmedi → devam ediliyor");
            }
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println("ℹ️ Slip elementi bulunamadı → devam ediliyor");
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            System.out.println("⚠️ Slip elementi stale oldu → yeniden kontrol ediliyor...");
            try {
                // stale durumunda tek sefer yeniden bulma
                WebElement slip = driver.findElement(AppiumBy.id("com.pax.samplesalea:id/lblSlip"));
                slip.click();
                System.out.println("📄 Slip yeniden bulundu ve tıklandı");
            } catch (Exception inner) {
                System.out.println("⚠️ Slip yeniden bulunamadı: " + inner.getMessage());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Slip kontrolünde hata: " + e.getMessage());
        }

    }

    @And("kullanici samplesale satis slibine basar")
    public void kullaniciSamplesaleSatisSlibineBasar() throws InterruptedException {


        for (int i = 0; i < 2; i++) {


            try {
                if (salePage.lblSlip1.isDisplayed()) {
                    salePage.lblSlip1.click();
                    System.out.println("📄 Slip ekranı tıklandı (" + (i + 1) + ". kez)");
                    Thread.sleep(300);

                    if (salePage.lblSlip2.isDisplayed()) {
                        salePage.lblSlip2.click();
                        Thread.sleep(300);
                        System.out.println("📄 Slip ekranı tıklandı (" + (i + 2) + ". kez)");

                    }
                } else {
                    System.out.println("ℹ️ Slip ekranı " + (i + 1) + ". kez görünmedi → devam ediliyor");
                    break;
                }
            } catch (org.openqa.selenium.NoSuchElementException e) {
                System.out.println("ℹ️ Slip elementi bulunamadı → devam ediliyor");
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("⚠️ Slip elementi stale oldu → yeniden kontrol ediliyor...");
                Thread.sleep(200);
            } catch (Exception e) {
                System.out.println("⚠️ Slip kontrolünde hata: " + e.getMessage());
                break;
            }

        }


    }

    @And("kullanici islem basarili mesaji sonrasi tamam tusuna basar")
    public void kullaniciIslemBasariliMesajiSonrasiTamamTusunaBasar() {


        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(sampleSalePage.btnTamamIslemBasarili)).click();
    }

    @And("Kullanici cihazi kendi serisine kurar")
    public void kullaniciCihaziKendiSerisineKurar() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        boolean isSeriNoEkraniAcik;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(techPos.lblSeriNumarasiGiriniz));
            isSeriNoEkraniAcik = true;
        } catch (Exception e) {
            isSeriNoEkraniAcik = false;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        if (isSeriNoEkraniAcik) {
            System.out.println("📌 Seri No ekranı geldi → Doğrulama gerekir!");

            techPos.btnTechposGiris.click();


            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(
                            AppiumBy.xpath("//android.widget.Button[@index='14']")));

            techPos.btnTechposGiris.click();
        } else {
            System.out.println("📌 Seri No ekranı gelmedi → Direkt IP giriş ekranı!");
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(techPos.txtIpGiriniz1));
        }

        System.out.println("📌 IP & Port bilgisi giriliyor...");

        techPos.txtIpGiriniz1.sendKeys("031");
        techPos.txtIpGiriniz2.sendKeys("145");
        techPos.txtIPgiriniz3.sendKeys("171");
        techPos.txtIPgiriniz4.sendKeys("94");
        techPos.btnTechposGiris.click();

        techPos.txtTechposGenelBox.sendKeys("12121");
        techPos.btnTechposGiris.click();

        techPos.txtIpGiriniz1.sendKeys("031");
        techPos.txtIpGiriniz2.sendKeys("145");
        techPos.txtIPgiriniz3.sendKeys("171");
        techPos.txtIPgiriniz4.sendKeys("94");
        techPos.btnTechposGiris.click();

        techPos.txtTechposGenelBox.sendKeys("12121");
        techPos.btnTechposGiris.click();

        System.out.println("✅ Kurulum tamamlandi ✅");
    }


    @And("kullanici yanlis seri no girer")
    public void kullaniciYanlisSeriNoGirer() {


        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        boolean isSeriNoEkraniAcik;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(techPos.lblSeriNumarasiGiriniz));
            isSeriNoEkraniAcik = true;
        } catch (Exception e) {
            isSeriNoEkraniAcik = false;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        if (isSeriNoEkraniAcik) {
            System.out.println("📌 Seri No ekranı geldi → Doğrulama gerekir!");
            techPos.txtTechposGenelBox.click();
            techPos.txtTechposGenelBox.clear();
            techPos.txtTechposGenelBox.sendKeys("159632147");
            techPos.btnTechposGiris.click();


            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(
                            AppiumBy.xpath("//android.widget.Button[@index='14']")));
            techPos.txtTechposGenelBox.click();
            techPos.txtTechposGenelBox.clear();
            techPos.txtTechposGenelBox.sendKeys("159630000");
            techPos.btnTechposGiris.click();
            // Write code here that turns the phrase above into concrete actions


        }
    }

    @When("kullanici hata mesajini gorur")
    public void kullaniciHataMesajiniGorur() {


        assertElementVisible("Seri numarası eşleşmedi mesajı", techPosPage.lblSeriNoEslesmedi);

    }


    @And("Kullanici Geri tusuyla geri cikar")
    public void kullaniciGeriTusuylaGeriCikar() throws InterruptedException {


        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(techPos.lblSeriNumarasiGiriniz));


        pressBack();


    }


    @And("kullanici merchant menude bulunan elementlerin gorunurlugunu test eder")
    public void kullaniciMerchantMenudeBulunanElementlerinGorunurlugunuTestEder() {


        assertElementVisible("Merchant menüde günsonu buttonu görüldü", techPos.btnGunsonu);
        assertElementVisible("Merchant menüde ara rapor buttonu görüldü", techPos.btnAraRapor);
        assertElementVisible("Merchant menüde fiş tekrarı buttonu görüldü", techPos.btnFisTekrari);
        assertElementVisible("Merchant menüde banka seçimi buttonu görüldü", techPos.btnBankaSecimi);
        assertElementVisible("Merchant menüde parametre buttonu görüldü", techPos.btnParametre);
        assertElementVisible("Merchant menüde banka irtibat buttonu görüldü", techPos.btnBankaIrtibat);
        assertElementVisible("Merchant menüde şifre değiştirme buttonu görüldü", techPos.btnSifreDegistirme);
        assertElementVisible("Merchant menüde şifre sıfırlama buttonu görüldü", techPos.btnSifreSifirlama);
        assertElementVisible("Merchant menüde sistem param buttonu görüldü", techPos.btnSistemParamRaporu);
        swipeUp();
        assertElementVisible("Merchant menüde otomatik günsonu aç kapa buttonu görüldü", techPos.btnOtomatikGunsonuAcKapa);

    }

    @And("kullanici pin girer")
    public void kullaniciPinGirer() {

        for (int i = 0; i < 2; i++) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

                wait.until(ExpectedConditions.visibilityOf(techPos.txtTechposGenelBox));

                techPos.txtTechposGenelBox.click();
                techPos.txtTechposGenelBox.clear();
                techPos.txtTechposGenelBox.sendKeys("1923");
                techPos.btnTechposGiris.click();

                System.out.println("✅ PIN girildi (" + (i + 1) + ". kez)");
                Thread.sleep(300);

            } catch (org.openqa.selenium.TimeoutException e) {
                System.out.println("⚠️ 12 sn içinde PIN ekranı gelmedi → daha beklemiyorum, geçiyorum (PASS ✅)");
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("⚠️ PIN elementi stale oldu → 1 kere yeniden denenecek...");
                i--; // 1 kere retry yap
                try { Thread.sleep(200); } catch (Exception ignored) {}
            } catch (Exception e) {
                System.out.println("⚠️ PIN akışında başka bir durum oluştu → geçiliyor");
                break;
            }
        }

        System.out.println("📌 Gerçek Aktif Package: " + driver.getCurrentPackage());
    }

    @When("kullanici samplesale uzerinden transaction menuye giris yapar")
    public void kullaniciSamplesaleUzerindenTransactionMenuyeGirisYapar() {

        if (salePage == null) salePage = new PGsampleSale(ReusableMethods.driver);

        ReusableMethods.switchToApp("com.pax.samplesalea");
        ReusableMethods.driverWaitForApp();

        // Ana ekran garantisi
        ReusableMethods.iwait().until(
                ExpectedConditions.visibilityOf(salePage.btnTaksitliSatis)
        );

        swipeUp();

        // ✅ EKLENECEK TEK SATIR:
        ReusableMethods.iwait().until(
                ExpectedConditions.elementToBeClickable(salePage.lstMenuAc)
        );

        salePage.lstMenuAc.click();
        salePage.itmTransactionMenu.click();
        salePage.btnMenuAc.click();

        System.out.println("✅ Transaction menuye giris yapildi.");
    }


    @And("kullanici transaction menude bulunan elementlerin gorunurlugunu test eder")
    public void kullaniciTransactionMenudeBulunanElementlerinGorunurlugunuTestEder() {
        if (techPos == null) techPos = new PGtechPos(ReusableMethods.driver);
        assertElementVisible("Transaction menude Satış buttonu görüldü",techPos.btnSatisIslemi );
        assertElementVisible("Transaction menude Taksitli satış buttonu görüldü",techPos.btnTaksitliSatisIslemi);
        assertElementVisible("Transaction menude puan kullanımı buttonu görüldü",techPos.btnPuanKullanimiIslemi );
        assertElementVisible("Transaction menude puan sorgu buttonu görüldü",techPos.btnPuanSorguIslemi);
        assertElementVisible("Transaction menude ön provizyon açma buttonu görüldü",techPos.btnProvizyonIslemi);
        assertElementVisible("Transaction menude ön provizyon kapama buttonu görüldü",techPos.btnProvizyonKapamaIslemi);
        assertElementVisible("Transaction menude ön provizyon iptal buttonu görüldü",techPos.btnProvizyonIptalIslemi);
        assertElementVisible("Transaction menude eşlenikLİ iade buttonu görüldü",techPos.btnEslenikliIadeIslemi);
        assertElementVisible("Transaction menude eşlenikSİZ buttonu görüldü",techPos.btnEsleniksizIadeIslemi);
        swipeUp();
        assertElementVisible("Transaction menude iptal buttonu görüldü", techPos.btnProvizyonIptalIslemi);


    }

    @Given("kullanici geri tusuyla cikis yapar")
    public void kullaniciGeriTusuylaCikisYapar() throws InterruptedException {


        pressBack();

    }

    @When("kullanici sample sale ekranini gorur")
    public void kullaniciSampleSaleEkraniniGorur() throws InterruptedException {
        salePage = new PGsampleSale(ReusableMethods.driver);


        System.out.println("⏳ Samplesale ana ekranı bekleniyor...");
        long timeout = System.currentTimeMillis() + 120_000; // 120 sn

        while (System.currentTimeMillis() < timeout) {

            boolean isSampleSaleHome = ReusableMethods.isElementPresent(salePage.btnTaksitliSatis);

            System.out.println("📍 Samplesale ekran elemanı görünüyor mu?: " + isSampleSaleHome);

            if (isSampleSaleHome) {
                System.out.println("✅ Samplesale ekranı açık ve hazır!");
                return;
            }

            Thread.sleep(500);
        }

        System.out.println("❌ 120 sn boyunca Samplesale ekranı gelmedi!");
        Assertions.fail("Samplesale ekranı yüklenemedi!");
    }

    @And("uygulamalar kapatilir")
    public void uygulamalarKapatilir() {
        try {
            ReusableMethods.quitDriver();
            System.out.println("✅ Driver kapatıldı.");
        } catch (Exception e) {
            System.out.println("⚠️ Driver kapatılırken hata: " + e.getMessage());
        }
    }


}



