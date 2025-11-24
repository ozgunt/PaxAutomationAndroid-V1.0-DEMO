Feature: US1001 kullanici samplesale uzerinden kurulum parametre günsonu yapar
  @ignore
  Scenario: TC905 kullanici samplesale uzerinden yanlis seri no girer.
    Given kullanici sample sale baslatir
    When kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici isyeri menuden parametre secimi yapar
    And kullanici yanlis seri no girer
    When kullanici hata mesajini gorur
    And uygulamalar kapatilir

  @ignore
  Scenario: TC901 kullanici samplesale uzerinden basarili kurulum yapabilmeli
    Given kullanici sample sale baslatir
    When kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici isyeri menuden parametre secimi yapar
    And Kullanici kurulum bilgisi girer
    Then ekran kontrolu sonrasi uygulama kapanir

  @ignore
  Scenario: TC904  kullanici samplesale uzerinden cihazın serisine basarili kurulum yapabilmeli
    Given kullanici sample sale baslatir
    When kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici isyeri menuden parametre secimi yapar
    And Kullanici cihazi kendi serisine kurar
    Then ekran kontrolu sonrasi uygulama kapanir
@wip
  Scenario: TC902 kullanici parametre alir
    Given kullanici sample sale baslatir
    When kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici isyeri menuden parametre secimi yapar
    Then ekran kontrolu sonrasi uygulama kapanir
@wip
    Scenario: TC903 kullanici gunsonu yapar
      Given kullanici sample sale baslatir
      When kullanici samplesale uzerinden merchant menuye giris yapar
      And kullanici manager uzerinden uygulama secer techpos
      And kullanici techpos sifresi girer
      And kullanici isyeri menuden gunsonu secimi yapar
      And kullanici samplesale gunsonu slibine dokunur
      Then uygulamalar kapatilir














