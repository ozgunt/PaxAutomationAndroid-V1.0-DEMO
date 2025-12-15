package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

import static utilities.ReusableMethods.driver;

public class PGsampleSale {
    private AndroidDriver driver;

    // Constructor: PageFactory başlatılır
    public PGsampleSale(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    // ----------- BUTTON ELEMENTLERİ -----------

    @AndroidFindBy(id = "com.pax.samplesalea:id/button")
    public WebElement btnSatisBaslat;

    @AndroidFindBy(id = "com.pax.samplesalea:id/buttonNull")
    public WebElement btnNullBaslat;

    @AndroidFindBy(id = "com.pax.samplesalea:id/menuPuanKullanı")
    public WebElement btnPuanKullanim;

    @AndroidFindBy(id = "com.pax.samplesalea:id/menuIptal")
    public WebElement btnIptal;

    @AndroidFindBy(id = "com.pax.samplesalea:id/menuReversal")
    public WebElement btnReversal;

    @AndroidFindBy(id = "com.pax.samplesalea:id/menuIade")
    public WebElement btnIade;

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"com.pax.samplesalea:id/menuTaksitliSatis\"]")
    public WebElement btnTaksitliSatis;

    @AndroidFindBy(id = "com.pax.samplesalea:id/paymentCancelButton")
    public WebElement btnIptalListesi;

    @AndroidFindBy(id = "com.pax.samplesalea:id/paymentRefundButton")
    public WebElement btnIadeListesi;

    @AndroidFindBy(id = "com.pax.samplesalea:id/menuOnProvizyonAcma")
    public WebElement btnOnProvizyonAcma;

    @AndroidFindBy(id = "com.pax.samplesalea:id/menuOnProvizyonKapama")
    public WebElement btnOnProvizyonKapama;

    @AndroidFindBy(id = "com.pax.samplesalea:id/buttonEOD")
    public WebElement btnGunsonu;

    @AndroidFindBy(id = "com.pax.samplesalea:id/menuButton")
    public WebElement btnMenuAc;

    @AndroidFindBy(id = "com.pax.samplesalea:id/reportButton")
    public WebElement btnRapor;

    //slip bastıktan sonra çıkan işlem başarılıda görünen tamam buttonu

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"android:id/button1\"]")
    public WebElement btnTamamIslemBasarili;

    @AndroidFindBy (id = "com.pax.samplesalea:id/iptalBtn")
    public WebElement BtnIptal;

    // ----------- TEXTBOX ELEMENTLERİ -----------

  //  @FindBy(id = "com.pax.samplesalea:id/amountEt")
    //public WebElement txtTutar;

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"com.pax.samplesalea:id/amountEt\"]")
    public WebElement txtTutar;
    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"com.pax.samplesalea:id/tutarEt\"]")
    public WebElement txtIptalTutar;


    @AndroidFindBy(id = "com.pax.samplesalea:id/stanNoEt")
    public WebElement txtStanNo;




    @AndroidFindBy(xpath = "//android.widget.Spinner[@resource-id=\"com.pax.samplesalea:id/menuSpinner\"]\n")
    public WebElement lstMenuAc ;

    // ----------- Label ELEMENTLERİ -----------

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='android:id/text1' and @text='MERCHANT_MENU']")
    public WebElement itmMerchantMenu;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"TRANSACTION_MENU\"]")
    public WebElement itmTransactionMenu;


    @AndroidFindBy(xpath = "//android.widget.ImageView[@resource-id=\"com.pax.samplesalea:id/slip1ImageView\"]")
    public WebElement lblSlip1 ;

    @AndroidFindBy(xpath = "//android.widget.ImageView[@resource-id=\"com.pax.samplesalea:id/slip2ImageView\"]")
    public WebElement lblSlip2 ;

}

