package pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.cucumber.java.en.And;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import javax.xml.xpath.XPath;
import java.time.Duration;

import java.time.Duration;

public class PGtosla {



    private AndroidDriver driver;


    // Constructor: PageFactory başlatılır
    public PGtosla(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(2)), this);


    }



    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"com.akbank.akode:id/buttonSale\"]")
    public WebElement btnSatisBaslat;

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id=\"com.akbank.akode:id/refundButton\"]")
    public  WebElement btnIptalIade;

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"com.akbank.akode:id/bankRefNoEt\"]")
    public  WebElement txtReferans;

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"com.akbank.akode:id/stanNoEt\"]")
    public  WebElement txtStan;

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"com.akbank.akode:id/amountEt\"]")
    public  WebElement txtTutar;






}
