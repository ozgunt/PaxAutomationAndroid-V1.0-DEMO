package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static utilities.ReusableMethods.driver;

public class PGsampleSale {
    private AndroidDriver driver;

    // Constructor: PageFactory başlatılır
    public PGsampleSale(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ----------- BUTTON ELEMENTLERİ -----------

    @FindBy(id = "com.pax.samplesalea:id/button")
    public WebElement btnSatisBaslat;

    @FindBy(id = "com.pax.samplesalea:id/buttonNull")
    public WebElement btnNullBaslat;

    @FindBy(id = "com.pax.samplesalea:id/menuPuanKullanı")
    public WebElement btnPuanKullanim;

    @FindBy(id = "com.pax.samplesalea:id/menuIptal")
    public WebElement btnIptal;

    @FindBy(id = "com.pax.samplesalea:id/menuReversal")
    public WebElement btnReversal;

    @FindBy(id = "com.pax.samplesalea:id/menuIade")
    public WebElement btnIade;

    @FindBy(xpath = "//android.widget.Button[@resource-id=\"com.pax.samplesalea:id/menuTaksitliSatis\"]")
    public WebElement btnTaksitliSatis;

    @FindBy(id = "com.pax.samplesalea:id/paymentCancelButton")
    public WebElement btnIptalListesi;

    @FindBy(id = "com.pax.samplesalea:id/paymentRefundButton")
    public WebElement btnIadeListesi;

    @FindBy(id = "com.pax.samplesalea:id/menuOnProvizyonAcma")
    public WebElement btnOnProvizyonAcma;

    @FindBy(id = "com.pax.samplesalea:id/menuOnProvizyonKapama")
    public WebElement btnOnProvizyonKapama;

    @FindBy(id = "com.pax.samplesalea:id/buttonEOD")
    public WebElement btnGunsonu;

    @FindBy(id = "com.pax.samplesalea:id/menuButton")
    public WebElement btnMenuAc;

    @FindBy(id = "com.pax.samplesalea:id/reportButton")
    public WebElement btnRapor;

    //slip bastıktan sonra çıkan işlem başarılıda görünen tamam buttonu

    @FindBy(xpath = "//android.widget.Button[@resource-id=\"android:id/button1\"]")
    public WebElement btnTamamIslemBasarili;

    // ----------- TEXTBOX ELEMENTLERİ -----------

  //  @FindBy(id = "com.pax.samplesalea:id/amountEt")
    //public WebElement txtTutar;

    @FindBy(xpath = "//android.widget.EditText[@resource-id=\"com.pax.samplesalea:id/amountEt\"]")
    public WebElement txtTutar;


    @FindBy(xpath = "//android.widget.Spinner[@resource-id=\"com.pax.samplesalea:id/menuSpinner\"]\n")
    public WebElement lstMenuAc ;

    // ----------- Label ELEMENTLERİ -----------

    @FindBy(xpath = "//android.widget.TextView[@resource-id='android:id/text1' and @text='MERCHANT_MENU']")
    public WebElement itmMerchantMenu;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"TRANSACTION_MENU\"]")
    public WebElement itmTransactionMenu;


    @FindBy(xpath = "//android.widget.ImageView[@resource-id=\"com.pax.samplesalea:id/slip1ImageView\"]")
    public WebElement lblSlip1 ;

    @FindBy(xpath = "//android.widget.ImageView[@resource-id=\"com.pax.samplesalea:id/slip2ImageView\"]")
    public WebElement lblSlip2 ;

}

