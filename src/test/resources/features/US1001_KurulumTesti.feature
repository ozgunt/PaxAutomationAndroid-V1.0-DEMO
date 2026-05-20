@US1001
Feature: US1001 kullanici samplesale uzerinden kurulum parametre gunsonu yapar

  @US1001 @TC1 @ignore
  Scenario:kullanici samplesale uzerinden yanlis seri no girer
    Given kullanici sample sale baslatir
    When kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici isyeri menuden parametre secimi yapar
    And kullanici yanlis seri no girer
    When kullanici hata mesajini gorur
    And uygulamalar kapatilir

  @US1001 @TC2 @ignore
  Scenario:kullanici samplesale uzerinden basarili kurulum yapabilmeli
    Given kullanici sample sale baslatir
    When kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici isyeri menuden parametre secimi yapar
    And Kullanici kurulum bilgisi girer
    Then ekran kontrolu sonrasi uygulama kapanir

  @US1001 @TC3 @ignore
  Scenario:kullanici samplesale uzerinden cihazin serisine basarili kurulum yapabilmeli
    Given kullanici sample sale baslatir
    When kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici isyeri menuden parametre secimi yapar
    And Kullanici cihazi kendi serisine kurar
    Then ekran kontrolu sonrasi uygulama kapanir

  @US1001 @TC4 @ignore
  Scenario:kullanici parametre alir
    Given kullanici sample sale baslatir
    When kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici isyeri menuden parametre secimi yapar
    Then ekran kontrolu sonrasi uygulama kapanir

  @US1001 @TC5 @ignore
  Scenario:kullanici gunsonu yapar
    Given kullanici sample sale baslatir
    When kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici isyeri menuden gunsonu secimi yapar
    And kullanici samplesale gunsonu slibine dokunur
    Then uygulamalar kapatilir
