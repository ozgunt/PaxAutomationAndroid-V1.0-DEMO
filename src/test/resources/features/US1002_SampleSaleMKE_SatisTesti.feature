@US1002
Feature: US1002 SampleSaleMKE SatisTesti

  @US1002 @TC1 @ignore
  Scenario: kullanici samplesale uzerinden halkbank combo kart ile MKE satis islemi gecer
    Given kullanici sample sale baslatir
    When  kullanici samplesale uzerinden 10000 tutar girer
    And  kullanici samplesale uzerinden satis baslatir
    And  kullanici mke secimi yapar
    And  kullanici halkbank1 combo KK no girer
    And  kullanici halkbank1 combo skt girer
    And  kullanici halkbank1 combo KK cvv girer
    Then kullanici manager tamam tusuna basar
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar
    Then uygulamalar kapatilir

  @US1002 @TC2 @ignore
  Scenario:kullanici samplesale uzerinden ziraat combo kart ile MKE satis islemi gecer
    Given kullanici sample sale baslatir
    When  kullanici samplesale uzerinden 10000 tutar girer
    And  kullanici samplesale uzerinden satis baslatir
    And  kullanici mke secimi yapar
    And  kullanici ziraat1 combo KK no girer
    And  kullanici ziraat1 combo skt girer
    And  kullanici ziraat1 combo KK cvv girer
    Then kullanici manager tamam tusuna basar
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar
    Then uygulamalar kapatilir
