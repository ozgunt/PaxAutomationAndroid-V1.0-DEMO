@run
Feature: US1005 satis iptal iade islemleri yapilir

  Scenario: TC912 kullanici samplesale uzerinden halkbank combo kart ile MKE satis islemi gecer
    Given kullanici sample sale baslatir
    When kullanici samplesale uzerinden 10000 tutar girer
    And kullanici samplesale uzerinden satis baslatir
    And kullanici mke secimi yapar
    And kullanici halkbank1 combo KK no girer
    And kullanici halkbank1 combo skt girer
    And kullanici halkbank1 combo KK cvv girer
    And kullanici tamam tusuna basar
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar

  @run
  Scenario: TC913 kullanici iptal islemi yapar
    Given kullanici iptal secimi yapar
    When kullanici samplesale uzerinden 10000 tutar girer (iptal)
    And kullanici son stan no bilgisi girer
    And  kullanici klavyeyi kapatir
    And kullanici iptal tusuna basar
    And kullanici "halkbank" banka secimi yapar
    And kullanici techpos mke secimi yapar
    And kullanici techpos "halkbank1" kart no girer
    Then kullanici techpos giris tusuna basar
    And  kullanici techpos "halkbank1" skt girer
    Then kullanici techpos giris tusuna basar
    And  kullanici techpos "halkbank1" cvv girer
    #Then kullanici techpos giris tusuna basar
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar
    And uygulamalar kapatilir








