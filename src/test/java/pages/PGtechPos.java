package pages;


import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.xml.xpath.XPath;

public class PGtechPos {
    private final AndroidDriver driver;

    public PGtechPos(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }

    // ----------- BUTTON ELEMENTLERİ -----------
//gunsonu buttonu

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"DETAY RAPOR\"]")
    public WebElement getBtnGunsonuDetay;
    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ÖZET RAPOR\"]")
    public WebElement getBtnGunsonuOzet;




    //techpos genel giriş buttonu locate
    @FindBy(xpath = "//android.widget.Button[@index='14']")
    public WebElement  btnTechposGiris;
    // Techpos merchant menü elementleri

    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.pax.techpos:id/grid_text' and @text='GÜNSONU']")
    public WebElement btnGunsonu;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ARA RAPOR\"]")
    public WebElement btnAraRapor;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"FİŞ TEKRARI\"]")
    public WebElement btnFisTekrari;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"BANKA SEÇİMİ\"]")
    public WebElement btnBankaSecimi;

    @FindBy(xpath = "//android.view.ViewGroup[@index='4']")
    public WebElement btnParametre;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"BANKA İRTİBAT\"]")
    public WebElement btnBankaIrtibat;

    @FindBy(xpath = "//android.widget.ListView[@resource-id=\"com.pax.techpos:id/list\"]/android.view.ViewGroup[7]")
    public WebElement btnSifreDegistirme;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ŞİFRE SIFIRLAMA\"]")
    public WebElement btnSifreSifirlama;

    //ekranın altında kalanlar

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"SİSTEM PARAM. RAPORU\"]")
    public WebElement btnSistemParamRaporu;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"OTOMATİK GÜNSONU AÇ/KAPAT\"]")
    public WebElement btnOtomatikGunsonuAcKapa;


    //Techpos transaction menü elementleri


    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"SATIŞ\"]")
    public WebElement btnSatisIslemi;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"TAKSİTLİ SATIŞ\"]")
    public  WebElement btnTaksitliSatisIslemi;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"PUAN KULLANIM\"]")
    public  WebElement btnPuanKullanimiIslemi;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"PUAN SORGU\"]")
    public  WebElement btnPuanSorguIslemi;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ÖN PROVİZYON AÇMA\"]")
    public  WebElement btnProvizyonIslemi;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ÖN PROVİZYON KAPAMA\"]")
    public  WebElement btnProvizyonKapamaIslemi;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"ÖN PROVİZYON İPTAL\"]")
    public  WebElement btnProvizyonIptalIslemi;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"EŞLENİKLİ İADE\"]")
    public  WebElement btnEslenikliIadeIslemi;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"EŞLENİKLİ İADE\"]")
    public  WebElement btnEsleniksizIadeIslemi;

    // Ekranın altında kalanlar

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/grid_text\" and @text=\"İPTAL\"]")
    public  WebElement btnPtalIslemi;






    // ----------- TEXTBOX ELEMENTLERİ -----------
    @FindBy(id = "com.pax.techpos:id/amount_edtext")
    public WebElement txtTechposAmountText;

    // ✅ kurulum için IP giriş yapacağımız kutucuklar
    @FindBy(id = "com.pax.techpos:id/customEditText2")
    public WebElement txtIpGiriniz1;

    @FindBy(id = "com.pax.techpos:id/customEditText7")
    public WebElement txtIpGiriniz2;

    @FindBy(id = "com.pax.techpos:id/customEditText8")
    public WebElement txtIPgiriniz3;

    @FindBy(id = "com.pax.techpos:id/customEditText9")
    public WebElement txtIPgiriniz4;

    @FindBy(id = "com.pax.techpos:id/amount_edtext")
    public WebElement txtTechposGenelBox;

    @FindBy(id = "com.pax.techpos:id/amount_edtext")
    public WebElement txtSeriNo;

    // ----------- Label ELEMENTLERİ -----------
    //seri numarası giriniz yazısı
    @FindBy(xpath = "//*[@resource-id='com.pax.techpos:id/header' and @text='SERİ NUMARASI GİRİNİZ']")
    public WebElement lblSeriNumarasiGiriniz;

    @FindBy(xpath = "//android.widget.TextView[@text=\"İŞYERİ MENÜSÜ\"]")
    public WebElement lblIsyeriMenu;

    @FindBy(xpath = "//android.widget.TextView[@resource-id=\"com.pax.techpos:id/tv_title\"]")
    public WebElement lblSeriNoEslesmedi ;



    }



   







