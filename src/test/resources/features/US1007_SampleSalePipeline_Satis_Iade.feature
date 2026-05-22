@US1006
Feature: US1006 SampleSale satis gunsonu iade islemleri

  @TC1 @ignore
  Scenario: kullanici samplesale uzerinden garanti1 kart ile MKE satis islemi gecer
    Given kullanici sample sale baslatir
    When  kullanici samplesale uzerinden 10000 tutar girer
    And  kullanici samplesale uzerinden satis baslatir
    And  kullanici mke secimi yapar
    And  kullanici manager "vakif1" kart no girer
    And  kullanici manager "vakif1" skt girer
    And  kullanici manager "vakif1" cvv girer
    Then kullanici manager tamam tusuna basar
    # And kullanici puan 1 girisi yapar
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar

  @TC2 @ignore
  Scenario: kullanici gunsonu yapar
    When kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici isyeri menuden gunsonu secimi yapar
    And kullanici samplesale gunsonu slibine dokunur

  @TC3 @ignore
  Scenario: kullanici iade islemi yapar
    Given kullanici iade secimi yapar
    When kullanici samplesale uzerinden 10000 tutar girer (iptal)
    And kullanici son stan no bilgisi girer
    And kullanici son islem referans numarasi girer
    And kullanici klavyeyi kapatir
    And kullanici iade tusuna basar
    And kullanici "vakif" banka secimi yapar
    And kullanici techpos mke secimi yapar
    And kullanici techpos "vakif1" kart no girer
    Then kullanici techpos giris tusuna basar
    And kullanici techpos "vakif1" skt girer
    Then kullanici techpos giris tusuna basar
    And kullanici techpos "vakif1" cvv girer
    And  kullanici techpos giris tusuna basar
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar
    And uygulamalar kapatilir
