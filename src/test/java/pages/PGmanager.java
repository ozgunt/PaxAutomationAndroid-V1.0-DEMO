package pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.xml.xpath.XPath;
import java.security.cert.X509Certificate;
import java.time.Duration;


public class PGmanager {

    private AndroidDriver driver;


    // Constructor: PageFactory başlatılır
    public PGmanager(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    // ----------- BUTTON ELEMENTLERİ -----------
    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"com.pax.mainapp:id/btn_ok\"]")
    public WebElement btnTamam;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='TechPOS']/parent::*")
    public WebElement btnApkSecimTechPos;

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"com.pax.mainapp:id/button4\"]")
    public WebElement btnMke;

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"com.pax.mainapp:id/cardno\"]")
    public WebElement txtKartNo;

    // ----------- TEXTBOX ELEMENTLERİ -----------

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"com.pax.mainapp:id/exdata\"]")
    public WebElement txtSKT;

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"com.pax.mainapp:id/cvv\"]")
    public WebElement txtCVV;

    // ----------- Label ELEMENTLERİ -----------

}
