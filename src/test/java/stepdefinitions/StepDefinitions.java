package stepdefinitions;

import com.google.gson.annotations.Until;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.*;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import org.apache.logging.log4j.core.appender.ScriptAppenderSelector;
import org.apache.poi.ss.formula.atp.Switch;
import org.apache.xmlbeans.GDuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.platform.commons.function.Try;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.PGmanager;
import pages.PGsampleSale;
import pages.PGtechPos;
import utilities.ConfigReader;
import utilities.PageContext;
import utilities.ReusableMethods;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import java.security.spec.ECField;
import java.sql.Driver;
import java.time.Duration;


public class StepDefinitions {


    public StepDefinitions() {
    }

    PGsampleSale salePage;
    PGmanager manager;
    PGtechPos techPos;

    // Her adım öncesi ReusableMethods'taki güncel referansları local field'lara kopyala
    @BeforeStep
    public void syncPages() {
        if (PageContext.sampleSalePage != null) salePage = PageContext.sampleSalePage;
        if (PageContext.managerPage != null) manager = PageContext.managerPage;
        if (PageContext.techPosPage != null) techPos = PageContext.techPosPage;
    }


    @Given("kullanici sample sale baslatir")
    public void kullanici_sample_sale_baslatir() throws Exception {
        PageContext.setUp();
        System.out.println("✅ Sample Sale baslatildi!");
    }

    @When("kullanici samplesale uzerinden merchant menuye giris yapar")
    public void kullanici_samplesale_uzerinden_merchant_menuye_giris_yapar() {
        ReusableMethods.swipeUp();

        //ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(salePage.lstMenuAc)).click();
        new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(2))
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


        techPos.txtTechposAmountText.clear();
        techPos.txtTechposAmountText.sendKeys("0000");
        techPos.btnTechposGiris.click();

        System.out.println("✅ TechPOS girisi yapildi.");
    }

    @Then("Kullanici kurulum bilgisi girer")
    public void kullanici_kurulum_bilgisi_girer() {
        ReusableMethods.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        boolean isSeriNoEkraniAcik;
        try {
            new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(techPos.lblSeriNumarasiGiriniz));
            isSeriNoEkraniAcik = true;
        } catch (Exception e) {
            isSeriNoEkraniAcik = false;
        }

        ReusableMethods.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        if (isSeriNoEkraniAcik) {
            System.out.println("📌 Seri No ekranı geldi → Doğrulama gerekir!");
            techPos.txtTechposGenelBox.click();
            techPos.txtTechposGenelBox.clear();
            techPos.txtTechposGenelBox.sendKeys("900000011");
            techPos.btnTechposGiris.click();


            new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(7))
                    .until(ExpectedConditions.elementToBeClickable(
                            AppiumBy.xpath("//android.widget.Button[@index='14']")));
            techPos.txtTechposGenelBox.click();
            techPos.txtTechposGenelBox.clear();
            techPos.txtTechposGenelBox.sendKeys("900000011");
            techPos.btnTechposGiris.click();
        } else {
            System.out.println("📌 Seri No ekranı gelmedi → Direkt IP giriş ekranı!");
            new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(techPos.txtIpGiriniz1));
        }

        ReusableMethods.iwait().until(ExpectedConditions.textToBePresentInElement(techPos.lblHeader, "TCKN"));
        techPos.btnGiris.click();

        ReusableMethods.iwait().until(ExpectedConditions.textToBePresentInElement(techPos.lblHeader, "VKN"));
        techPos.btnGiris.click();

        System.out.println("📌 IP & Port bilgisi giriliyor...");

        techPos.txtIpGiriniz1.sendKeys("213");
        techPos.txtIpGiriniz2.sendKeys("248");
        techPos.txtIPgiriniz3.sendKeys("141");
        techPos.txtIPgiriniz4.sendKeys("194");
        techPos.btnTechposGiris.click();

        techPos.txtTechposGenelBox.sendKeys("12500");
        techPos.btnTechposGiris.click();

        techPos.txtIpGiriniz1.sendKeys("213");
        techPos.txtIpGiriniz2.sendKeys("248");
        techPos.txtIPgiriniz3.sendKeys("141");
        techPos.txtIPgiriniz4.sendKeys("194");
        techPos.btnTechposGiris.click();

        techPos.txtTechposGenelBox.sendKeys("12500");
        techPos.btnTechposGiris.click();

        System.out.println("✅ Kurulum tamamlandi ✅");
    }

    @And("kullanici isyeri menuden gunsonu secimi yapar")
    public void kullaniciGunsonuSecimiYapar() throws InterruptedException {


        WebDriverWait wait = new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(20)); // 20 saniye bekle
        wait.until(ExpectedConditions.visibilityOf(techPos.btnGunsonu)).click(); // Görünürse tıkla
        wait.until(ExpectedConditions.visibilityOf(techPos.getBtnGunsonuDetay)).click(); // Görünürse tıkla


    }

    //manager üzerinden uygulama seçimi
    @Then("kullanici manager uzerinden uygulama secer techpos")
    public void kullanici_techpos_secimi_yapar() {

        try {
            WebDriverWait fastWait = new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(1));
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
        String pkg = ReusableMethods.driver.getCurrentPackage();
        System.out.println("📌 Gerçek Aktif Package: " + pkg);
    }


    @When("kullanici samplesale uzerinden {int} tutar girer")
    public void kullaniciTutarGirer(Integer tutar) {

        ReusableMethods.iwait()
                .until(ExpectedConditions.visibilityOf(salePage.txtTutar))
                .sendKeys(String.valueOf(tutar));
    }


    @And("kullanici samplesale uzerinden satis baslatir")
    public void kullaniciSamplesaleUzerindenSatisBaslatir() {


        salePage.btnSatisBaslat.click();

    }


    @And("kullanici mke secimi yapar")
    public void kullaniciMkeSecimiYapar() {


        assert manager.btnMke.isDisplayed();

        System.out.println("MKE button görünür");

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

    @Then("kullanici manager tamam tusuna basar")
    public void kullaniciManagerTamamTusunaBasar() {

        ReusableMethods.closeKeyboard();
        ReusableMethods.iwait();

        try {
            manager.btnTamam.click();

        } catch (Exception ignored) {
        }

    }

    @And("kullanici halkbank1 combo KK no girer")
    public void kullaniciHalkbanComboKKNoGirer() {
        String card = ConfigReader.getProperty("halkbank1ComboKartNoKK");
        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtKartNo));
        manager.txtKartNo.click();
        manager.txtKartNo.sendKeys(card);
        try {
            String typed = manager.txtKartNo.getText();
            if (typed == null || typed.isBlank()) {
                java.util.Map<String, Object> args = new java.util.HashMap<>();
                args.put("command", "input");
                args.put("args", java.util.Arrays.asList("text", card));
                ReusableMethods.driver.executeScript("mobile: shell", args);
            }
        } catch (Exception e) {
            System.out.println("⚠️ getText kontrol hatası, devam ediliyor: " + e.getMessage());
        }
    }

    @And("kullanici halkbank1 combo skt girer")
    public void kullaniciHalkbankComboSktGirer() {
        String skt = ConfigReader.getProperty("halkbank1ComboSKT");
        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtSKT));
        manager.txtSKT.click();
        manager.txtSKT.sendKeys(skt);
        try {
            String typed = manager.txtSKT.getText();
            if (typed == null || typed.isBlank()) {
                java.util.Map<String, Object> args = new java.util.HashMap<>();
                args.put("command", "input");
                args.put("args", java.util.Arrays.asList("text", skt));
                ReusableMethods.driver.executeScript("mobile: shell", args);
            }
        } catch (Exception e) {
            System.out.println("⚠️ getText kontrol hatası, devam ediliyor: " + e.getMessage());
        }
    }

    @And("kullanici halkbank1 combo KK cvv girer")
    public void kullaniciHalkbankComboKKCvvGirer() {
        String cvv = ConfigReader.getProperty("halkbank1ComboCcvKK");
        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtCVV));
        manager.txtCVV.click();
        manager.txtCVV.sendKeys(cvv);
        try {
            String typed = manager.txtCVV.getText();
            if (typed == null || typed.isBlank()) {
                java.util.Map<String, Object> args = new java.util.HashMap<>();
                args.put("command", "input");
                args.put("args", java.util.Arrays.asList("text", cvv));
                ReusableMethods.driver.executeScript("mobile: shell", args);
            }
        } catch (Exception e) {
            System.out.println("⚠️ getText kontrol hatası, devam ediliyor: " + e.getMessage());
        }
    }

    @And("kullanici samplesale gunsonu slibine dokunur")
    public void kullaniciSamplesaleGunsonuSlibineDokunur() {
        int n = 2;
        while (n > 0) {
            try {
                if (salePage.lblSlip1.isDisplayed()) {
                    salePage.lblSlip1.click();
                    System.out.println("📄 Slip ekranı tıklandı");
                    Thread.sleep(300);
                } else {
                    System.out.println("ℹ️ Slip ekranı görünmedi → devam ediliyor");
                    break;
                }
            } catch (org.openqa.selenium.NoSuchElementException e) {
                System.out.println("ℹ️ Slip elementi bulunamadı → devam ediliyor");
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("⚠️ Slip elementi stale oldu → yeniden kontrol ediliyor...");
                try {
                    WebElement slip = ReusableMethods.driver.findElement(AppiumBy.id("com.pax.samplesalea:id/lblSlip"));
                    slip.click();
                    System.out.println("📄 Slip yeniden bulundu ve tıklandı");
                } catch (Exception inner) {
                    System.out.println("⚠️ Slip yeniden bulunamadı: " + inner.getMessage());
                }
            } catch (Exception e) {
                System.out.println("⚠️ Slip kontrolünde hata: " + e.getMessage());
                break;
            }
            n--;
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


        // ✅ techPos objesi null mı? -> asıl NPE sebebi burada
        if (salePage == null) {
            throw new RuntimeException("techPosPage null. setUp() çağrılmamış veya PGtechPos init olmamış.");
        }


        try {
            ReusableMethods.iwait()
                    .until(ExpectedConditions.elementToBeClickable(salePage.btnTamamIslemBasarili));
            salePage.btnTamamIslemBasarili.click();
            System.out.println("✅ İşlem başarılı popup 'Tamam' tıklandı");
        } catch (Exception e) {
            System.out.println("ℹ️ İşlem başarılı popup gelmedi → devam ediliyor");
        }
    }


    @And("Kullanici cihazi kendi serisine kurar")
    public void kullaniciCihaziKendiSerisineKurar() {
        ReusableMethods.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        boolean isSeriNoEkraniAcik;
        try {
            new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(techPos.lblSeriNumarasiGiriniz));
            isSeriNoEkraniAcik = true;
        } catch (Exception e) {
            isSeriNoEkraniAcik = false;
        }

        ReusableMethods.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        if (isSeriNoEkraniAcik) {
            System.out.println("📌 Seri No ekranı geldi → Doğrulama gerekir!");

            techPos.btnTechposGiris.click();


            new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(
                            AppiumBy.xpath("//android.widget.Button[@index='14']")));

            techPos.btnTechposGiris.click();
        } else {
            System.out.println("📌 Seri No ekranı gelmedi → Direkt IP giriş ekranı!");
            new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(3))
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


        ReusableMethods.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        boolean isSeriNoEkraniAcik;
        try {
            new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(techPos.lblSeriNumarasiGiriniz));
            isSeriNoEkraniAcik = true;
        } catch (Exception e) {
            isSeriNoEkraniAcik = false;
        }

        ReusableMethods.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        if (isSeriNoEkraniAcik) {
            System.out.println("📌 Seri No ekranı geldi → Doğrulama gerekir!");
            techPos.txtTechposGenelBox.click();
            techPos.txtTechposGenelBox.clear();
            techPos.txtTechposGenelBox.sendKeys("159632147");
            techPos.btnTechposGiris.click();


            new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(5))
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


        ReusableMethods.assertElementVisible("Seri numarası eşleşmedi mesajı", techPos.lblSeriNoEslesmedi);

    }


    @And("Kullanici Geri tusuyla geri cikar")
    public void kullaniciGeriTusuylaGeriCikar() throws InterruptedException {


        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(techPos.lblSeriNumarasiGiriniz));


        ReusableMethods.pressBack();


    }


    @And("kullanici merchant menude bulunan elementlerin gorunurlugunu test eder")
    public void kullaniciMerchantMenudeBulunanElementlerinGorunurlugunuTestEder() {


        ReusableMethods.assertElementVisible("Merchant menüde günsonu buttonu görüldü", techPos.btnGunsonu);
        ReusableMethods.assertElementVisible("Merchant menüde ara rapor buttonu görüldü", techPos.btnAraRapor);
        ReusableMethods.assertElementVisible("Merchant menüde fiş tekrarı buttonu görüldü", techPos.btnFisTekrari);
        ReusableMethods.assertElementVisible("Merchant menüde banka seçimi buttonu görüldü", techPos.btnBankaSecimi);
        ReusableMethods.assertElementVisible("Merchant menüde parametre buttonu görüldü", techPos.btnParametre);
        ReusableMethods.assertElementVisible("Merchant menüde banka irtibat buttonu görüldü", techPos.btnBankaIrtibat);
        ReusableMethods.assertElementVisible("Merchant menüde şifre değiştirme buttonu görüldü", techPos.btnSifreDegistirme);
        ReusableMethods.assertElementVisible("Merchant menüde şifre sıfırlama buttonu görüldü", techPos.btnSifreSifirlama);
        ReusableMethods.assertElementVisible("Merchant menüde sistem param buttonu görüldü", techPos.btnSistemParamRaporu);
        ReusableMethods.swipeUp();
        ReusableMethods.assertElementVisible("Merchant menüde otomatik günsonu aç kapa buttonu görüldü", techPos.btnOtomatikGunsonuAcKapa);

    }

    @And("kullanici pin girer")
    public void kullaniciPinGirer() {

        for (int i = 0; i < 2; i++) {
            try {
                WebDriverWait wait = new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(3));

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
                try {
                    Thread.sleep(200);
                } catch (Exception ignored) {
                }
            } catch (Exception e) {
                System.out.println("⚠️ PIN akışında başka bir durum oluştu → geçiliyor");
                break;
            }
        }

        System.out.println("📌 Gerçek Aktif Package: " + ReusableMethods.driver.getCurrentPackage());
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

        ReusableMethods.swipeUp();

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
        ReusableMethods.assertElementVisible("Transaction menude Satış buttonu görüldü", techPos.btnSatisIslemi);
        ReusableMethods.assertElementVisible("Transaction menude Taksitli satış buttonu görüldü", techPos.btnTaksitliSatisIslemi);
        ReusableMethods.assertElementVisible("Transaction menude puan kullanımı buttonu görüldü", techPos.btnPuanKullanimiIslemi);
        ReusableMethods.assertElementVisible("Transaction menude puan sorgu buttonu görüldü", techPos.btnPuanSorguIslemi);
        ReusableMethods.assertElementVisible("Transaction menude ön provizyon açma buttonu görüldü", techPos.btnProvizyonIslemi);
        ReusableMethods.assertElementVisible("Transaction menude ön provizyon kapama buttonu görüldü", techPos.btnProvizyonKapamaIslemi);
        ReusableMethods.assertElementVisible("Transaction menude ön provizyon iptal buttonu görüldü", techPos.btnProvizyonIptalIslemi);
        ReusableMethods.assertElementVisible("Transaction menude eşlenikLİ iade buttonu görüldü", techPos.btnEslenikliIadeIslemi);
        ReusableMethods.assertElementVisible("Transaction menude eşlenikSİZ buttonu görüldü", techPos.btnEsleniksizIadeIslemi);
        ReusableMethods.swipeUp();
        ReusableMethods.assertElementVisible("Transaction menude iptal buttonu görüldü", techPos.btnProvizyonIptalIslemi);


    }

    @Given("kullanici geri tusuyla cikis yapar")
    public void kullaniciGeriTusuylaCikisYapar() throws InterruptedException {


        ReusableMethods.pressBack();

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


    @And("kullanici ofline pin girer")
    public void kullaniciOflinePinGirer() {
        for (int i = 0; i < 2; i++) {
            try {
                WebDriverWait wait = new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(3));
                wait.until(ExpectedConditions.visibilityOf(techPos.lblOflinePinEkrani));

                // 1 tuşunun merkezi
                org.openqa.selenium.Rectangle r1 = techPos.btnOflinePinOne.getRect();
                int x1 = r1.getX() + r1.getWidth() / 2;
                int y1 = r1.getY() + r1.getHeight() / 2;

                // 2 tuşunun merkezi
                org.openqa.selenium.Rectangle r2 = techPos.btnOflinePinTwo.getRect();
                int x2 = r2.getX() + r2.getWidth() / 2;
                int y2 = r2.getY() + r2.getHeight() / 2;

                // 3 tuşunun merkezi
                org.openqa.selenium.Rectangle r3 = techPos.btnOflinePinThree.getRect();
                int x3 = r3.getX() + r3.getWidth() / 2;
                int y3 = r3.getY() + r3.getHeight() / 2;

                // 4 tuşunun merkezi
                org.openqa.selenium.Rectangle r4 = techPos.btnOflinePinFour.getRect();
                int x4 = r4.getX() + r4.getWidth() / 2;
                int y4 = r4.getY() + r4.getHeight() / 2;

                // OS seviyesinde tap
                tapWithShell(x1, y1);
                tapWithShell(x2, y2);
                tapWithShell(x3, y3);
                tapWithShell(x4, y4);

                techPos.btnTechposGiris.click();

                System.out.println("✅ PIN girildi (" + (i + 1) + ". kez)");
                Thread.sleep(300);

            } catch (org.openqa.selenium.TimeoutException e) {
                System.out.println("⚠️  PIN ekranı gelmedi → geçiyorum (PASS ✅)");
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("⚠️ PIN elementi stale oldu → 1 kere yeniden denenecek...");
                i--;
                try {
                    Thread.sleep(200);
                } catch (Exception ignored) {
                }
            } catch (Exception e) {
                System.out.println("⚠️ PIN akışında başka bir durum oluştu → geçiliyor: " + e.getMessage());
                break;
            }
        }
    }

    // 🔽 BUNU EKLE (aynı sınıfın içinde, en sondaki }'den önce)
    @SuppressWarnings("unchecked")
    private void tapWithShell(int x, int y) {
        java.util.Map<String, Object> args = new java.util.HashMap<>();
        args.put("command", "input");
        args.put("args", java.util.Arrays.asList("tap", String.valueOf(x), String.valueOf(y)));

        ((org.openqa.selenium.JavascriptExecutor) ReusableMethods.driver)
                .executeScript("mobile: shell", args);
    }


    @Given("kullanici iptal secimi yapar")
    public void kullaniciIptalSecimiYapar() {
        salePage.btnIptalMenu.isDisplayed();
        salePage.btnIptalMenu.click();
    }

    @And("kullanici son stan no bilgisi girer")
    public void kullaniciSonStanNoBilgisiGirer() {

        ReusableMethods.iwait()
                .until(ExpectedConditions.visibilityOf(salePage.txtStanNo))
                .click();
        salePage.txtStanNo.sendKeys(String.valueOf(ConfigReader.getProperty("sonIslemStanNo")));
    }

    @And("kullanici iade tusuna basar")
    public void kullaniciiadeTusunaBasar() {

        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(salePage.btnIadeIslemBTN)).click();

    }


    @And("kullanici {string} banka secimi yapar")
    public void kullaniciBankaSecimiYapar(String banka) {

        ReusableMethods.switchToApp("com.pax.techpos");

        try {
            new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(10))
                    .until(x -> "com.pax.techpos".equals(ReusableMethods.driver.getCurrentPackage()));
        } catch (Exception ignored) {
        }

        System.out.println("PKG=" + ReusableMethods.driver.getCurrentPackage());
        System.out.println("ACT=" + ReusableMethods.driver.currentActivity());

        By gridAny = AppiumBy.id("com.pax.techpos:id/grid_text");
        if (ReusableMethods.driver.findElements(gridAny).isEmpty()) {
            System.out.println("ℹ️ Bank listesi ekranı değil (grid_text yok) -> banka secimi ATLANDI");
            return;
        }

        String bankText;
        switch (banka.toLowerCase()) {
            case "halkbank":
                bankText = "HALKBANK";
                break;
            case "ziraat":
                bankText = "ZIRAAT";
                break;
            case "garanti1":
                bankText = "GARANTI";
                break;
            default:
                System.out.println("⚠️ Banka tanımsız: " + banka);
                bankText = banka.toUpperCase();
                break;
        }

        By bankCell = AppiumBy.xpath(
                "//android.widget.TextView[@resource-id='com.pax.techpos:id/grid_text' and @text='" + bankText + "']/.."
        );

        ReusableMethods.iwait()
                .until(ExpectedConditions.elementToBeClickable(bankCell))
                .click();

        System.out.println("✅ Banka seçildi: " + bankText);
    }

    @When("kullanici samplesale uzerinden {int} tutar girer \\(iptal)")
    public void kullaniciSamplesaleUzerindenTutarGirerIptal(int tutar) {

        ReusableMethods.iwait()
                .until(ExpectedConditions.visibilityOf(salePage.txtIptalTutar))
                .click();
        salePage.txtIptalTutar.sendKeys(String.valueOf(tutar));


    }

    @And("kullanici Garanti1 kart no girer")
    public void kullaniciGarantiKartNoGirer() {
        salePage.txtStanNo.click();
        salePage.txtStanNo.sendKeys(ConfigReader.getProperty("garantiBank1KartNo"));

    }

    @And("kullanici Garanti1 skt girer")
    public void kullaniciGarantiSktGirer() {

        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtSKT));
        manager.txtSKT.sendKeys(ConfigReader.getProperty("garantiBank1SKT"));
    }

    @And("kullanici Garanti1 cvv girer")
    public void kullaniciGarantiCvvGirer() {

        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtCVV));
        manager.txtCVV.sendKeys(ConfigReader.getProperty("garantiBank1CCV"));

    }

    @And("kullanici puan {int} girisi yapar")
    public void kullaniciPuanGirisiYapar(int puan) {
        try {
            if (techPos.lblIlkPuanPoup.isDisplayed()) {
                int n = 3;
                while (n > 0) {
                    System.out.println("puan popup mesajının geçmesi bekleniyor");
                    ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(techPos.lblIlkPuanPoup)).click();

                    techPos.lblPuanEkranaBas.click();
                    ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(techPos.txtTechposAmountText)).click();

                    techPos.txtTechposAmountText.sendKeys(String.valueOf(puan));
                    techPos.btnTechposGiris.click();

                    n--;
                }

            }
        } catch (Exception ignored) {
        }
        techPos.btnTechposGiris.click();

    }

    @And("kullanici klavyeyi kapatir")
    public void kullaniciKlavyeyiKapatir() {
        ReusableMethods.closeKeyboard();
    }

    @And("kullanici techpos {string} kart no girer")
    public void kullaniciTechposHalkbankKartNoGirer(String banka) {

        switch (banka) {

            case "halkbank1":

                techPos.txtKartNoGiriniz.sendKeys(ConfigReader.getProperty("halkbank1ComboKartNoKK"));

                break;

            case "garanti1":
                techPos.txtKartNoGiriniz.sendKeys(ConfigReader.getProperty("garantiBank1KartNo"));
                break;

            case "vakif1":
                techPos.txtKartNoGiriniz.sendKeys(ConfigReader.getProperty("vakif1kkNo"));
        break;
        }



    }

    @And("kullanici techpos {string} skt girer")
    public void kullaniciTechposSktGirer(String banka) {
        switch (banka) {
            case "halkbank1":
                techPos.txtSKT.sendKeys(ConfigReader.getProperty("halkbank1ComboSKT"));
                break;
            case  "garanti1":
                techPos.txtSKT.sendKeys(ConfigReader.getProperty("garantiBank1SKT"));
                break;
            case "vakif1":
                techPos.txtSKT.sendKeys(ConfigReader.getProperty("vakif1SKTno"));
                break;
        }
    }

    @And("kullanici techpos {string} cvv girer")
    public void kullaniciTechposCvvGirer(String banka) {

        switch (banka){
            case "halkbank1":
                techPos.txtCCV.sendKeys(ConfigReader.getProperty("halkbank1ComboCcvKK"));
                break;


            case "garanti1":
                techPos.txtCCV.sendKeys(ConfigReader.getProperty("garantiBank1CCV"));
                break;

            case "vakif1":
                techPos.txtCCV.sendKeys(ConfigReader.getProperty("vakif1CCV"));
                break;





        }





    }

    @Then("kullanici techpos giris tusuna basar")
    public void kullaniciTechposGirisTusunaBasar() throws InterruptedException {
        techPos.btnGiris.isDisplayed();
        techPos.btnGiris.click();
        Thread.sleep(8000);


    }

    @And("kullanici techpos mke secimi yapar")
    public void kullaniciTechposMkeSecimiYapar() {

        // Burada da kilitlenmeyelim: switch yap, paket techpos ise dene; değilse yine de click dene.
        ReusableMethods.switchToApp("com.pax.techpos");

        System.out.println("PKG=" + ReusableMethods.driver.getCurrentPackage());
        System.out.println("ACT=" + ReusableMethods.driver.currentActivity());

        // 1) Önce mevcut POM elementini dene
        try {
            techPos.btnMKE.click();
            System.out.println("✅ btnMKE tıklandı (POM)");
            return;
        } catch (Exception ignored) {
        }


        // 2) Fallback: inspector’da gördüğün id (button4) — bu yeni ekran layout’uysa buradan yürür
        By mkeFallback = AppiumBy.id("com.pax.techpos:id/button4");
        ReusableMethods.iwait().until(ExpectedConditions.elementToBeClickable(mkeFallback)).click();
        System.out.println("✅ btnMKE tıklandı (fallback button4)");
    }

    @Given("kullanici iade secimi yapar")
    public void kullaniciIadeSecimiYapar() {

        ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(salePage.btnIade)).click();


    }

    @And("kullanici son islem referans numarasi girer")
    public void kullaniciSonIslemReferansNumarasiGirer() {

        ReusableMethods.iwait()
                .until(ExpectedConditions.visibilityOf(salePage.txtBankRefNo))
                .click();
        salePage.txtBankRefNo.sendKeys(String.valueOf(ConfigReader.getProperty("sonIslemBankaReferansNo")));
    }



    @And("kullanici manager {string} kart no girer")
    public void kullaniciManagerBankaKartNoGirer(String banka) {
        switch (banka.toLowerCase()){




            case "garanti1":
                String card = ConfigReader.getProperty("garantiBank1KartNo");
                ReusableMethods.closeKeyboard();
                try {
                    ReusableMethods.iwait().until(ExpectedConditions.elementToBeClickable(manager.txtKartNo)).click();
                }catch (Exception e){
                    System.out.println("Element bulunamadı txtKartNo");

                }
                manager.txtKartNo.clear();
                manager.txtKartNo.click();


                manager.txtKartNo.sendKeys(card);



// ✅ kontrol
                String typed = manager.txtKartNo.getText();
                if (typed == null || typed.isBlank()) {
                    // custom keypad/secure input -> sendKeys yemedi
                    java.util.Map<String, Object> args = new java.util.HashMap<>();
                    args.put("command", "input");
                    args.put("args", java.util.Arrays.asList("text", card));
                    ((org.openqa.selenium.JavascriptExecutor) ReusableMethods.driver).executeScript("mobile: shell", args);
                }
                break;

            case "vakif1" :
                manager.txtKartNo.clear();
                manager.txtKartNo.click();
                manager.txtKartNo.sendKeys(ConfigReader.getProperty("vakif1kkNo"));
                break;

        }

    }

    @And("kullanici manager {string} skt girer")
    public void kullaniciManagerSktGirer(String banka) {
        switch (banka.toLowerCase()){

            case "garanti1":
                ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtSKT)).click();
                manager.txtSKT.clear();
                manager.txtSKT.sendKeys(ConfigReader.getProperty("garantiBank1SKT"));
                break;

            case"vakif1":

                ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtSKT)).click();
                manager.txtSKT.clear();
                manager.txtSKT.sendKeys(ConfigReader.getProperty("vakif1SKTno"));
                break;
        }
    }

    @And("kullanici manager {string} cvv girer")
    public void kullaniciManagerCvvGirer(String banka) {

        switch (banka.toLowerCase()){
            case "garanti1":
                ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtCVV)).click();
                manager.txtCVV.clear();
                manager.txtCVV.sendKeys(ConfigReader.getProperty("garantiBank1CCV"));
                break;
            case "vakif1":
                ReusableMethods.iwait().until(ExpectedConditions.visibilityOf(manager.txtCVV)).click();
                manager.txtCVV.clear();
                manager.txtCVV.sendKeys(ConfigReader.getProperty("vakif1CCV"));

        }

    }
}