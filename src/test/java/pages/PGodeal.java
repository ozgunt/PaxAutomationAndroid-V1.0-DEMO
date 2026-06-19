package pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class PGodeal {


    private AndroidDriver driver;


    // Constructor: PageFactory başlatılır
    public PGodeal(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(2)), this);


    }
    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"com.telera.merchant.stage.debug:id/btn_new_transaction\"]")
    public WebElement yeniIslem ;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.telera.merchant.stage.debug:id/name\" and @text=\"Kalem\"]")
    public WebElement kalem ;

    @AndroidFindBy(id = "com.telera.merchant.stage.debug:id/ok")
    public WebElement tamam ;

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"com.telera.merchant.stage.debug:id/next_nihai\"]")
    public WebElement nihai ;

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"com.telera.merchant.stage.debug:id/btnInstalmantSingle\"]")
    public WebElement tekCekim ;

    @AndroidFindBy(id = "android:id/button2")
    public WebElement hayir;
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Kredi/Banka Kartı ile ödeme\"]")
    public WebElement KKode;


}
