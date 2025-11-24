package pages;


import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.xml.xpath.XPath;
import java.time.Duration;

public class PGtechPos {
    private final AndroidDriver driver;

    public PGtechPos(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    // ----------- BUTTON ELEMENTLERİ -----------
//gunsonu buttonu

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"DETAY RAPOR\"]")
    public WebElement getBtnGunsonuDetay;
    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ÖZET RAPOR\"]")
    public WebElement getBtnGunsonuOzet;




    //techpos genel giriş buttonu locate
    @AndroidFindBy(xpath = "//android.widget.Button[@index='14']")
    public WebElement  btnTechposGiris;
    // Techpos merchant menü elementleri

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='com.pax.techpos:id/grid_text' and @text='GÜNSONU']")
    public WebElement btnGunsonu;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ARA RAPOR\"]")
    public WebElement btnAraRapor;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"FİŞ TEKRARI\"]")
    public WebElement btnFisTekrari;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"BANKA SEÇİMİ\"]")
    public WebElement btnBankaSecimi;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@index='4']")
    public WebElement btnParametre;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"BANKA İRTİBAT\"]")
    public WebElement btnBankaIrtibat;

    @AndroidFindBy(xpath = "//android.widget.ListView[@resource-id=\"com.pax.techpos:id/list\"]/android.view.ViewGroup[7]")
    public WebElement btnSifreDegistirme;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ŞİFRE SIFIRLAMA\"]")
    public WebElement btnSifreSifirlama;

    //ekranın altında kalanlar

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"SİSTEM PARAM. RAPORU\"]")
    public WebElement btnSistemParamRaporu;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"OTOMATİK GÜNSONU AÇ/KAPAT\"]")
    public WebElement btnOtomatikGunsonuAcKapa;


    //Techpos transaction menü elementleri


    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"SATIŞ\"]")
    public WebElement btnSatisIslemi;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"TAKSİTLİ SATIŞ\"]")
    public  WebElement btnTaksitliSatisIslemi;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"PUAN KULLANIM\"]")
    public  WebElement btnPuanKullanimiIslemi;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"PUAN SORGU\"]")
    public  WebElement btnPuanSorguIslemi;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ÖN PROVİZYON AÇMA\"]")
    public  WebElement btnProvizyonIslemi;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ÖN PROVİZYON KAPAMA\"]")
    public  WebElement btnProvizyonKapamaIslemi;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ÖN PROVİZYON İPTAL\"]")
    public  WebElement btnProvizyonIptalIslemi;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"EŞLENİKLİ İADE\"]")
    public  WebElement btnEslenikliIadeIslemi;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"EŞLENİKLİ İADE\"]")
    public  WebElement btnEsleniksizIadeIslemi;

    // Ekranın altında kalanlar

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"İPTAL\"]")
    public  WebElement btnPtalIslemi;






    // ----------- TEXTBOX ELEMENTLERİ -----------
    @AndroidFindBy(id = "com.pax.techpos:id/amount_edtext")
    public WebElement txtTechposAmountText;

    // ✅ kurulum için IP giriş yapacağımız kutucuklar
    @AndroidFindBy(id = "com.pax.techpos:id/customEditText2")
    public WebElement txtIpGiriniz1;

    @AndroidFindBy(id = "com.pax.techpos:id/customEditText7")
    public WebElement txtIpGiriniz2;

    @AndroidFindBy(id = "com.pax.techpos:id/customEditText8")
    public WebElement txtIPgiriniz3;

    @AndroidFindBy(id = "com.pax.techpos:id/customEditText9")
    public WebElement txtIPgiriniz4;

    @AndroidFindBy(id = "com.pax.techpos:id/amount_edtext")
    public WebElement txtTechposGenelBox;

    @AndroidFindBy(id = "com.pax.techpos:id/amount_edtext")
    public WebElement txtSeriNo;

    // ----------- Label ELEMENTLERİ -----------
    //seri numarası giriniz yazısı
    @AndroidFindBy(xpath = "//*[@resource-id='com.pax.techpos:id/header' and @text='SERİ NUMARASI GİRİNİZ']")
    public WebElement lblSeriNumarasiGiriniz;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"İŞYERİ MENÜSÜ\")")
    public WebElement lblIsyeriMenu;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/tv_title\"]")
    public WebElement lblSeriNoEslesmedi ;



    }



   







