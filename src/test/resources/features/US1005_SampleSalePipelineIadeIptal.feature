@PipeLineRun
Feature: US1005 satis iptal iade islemleri yapilir

  Scenario: TC912 kullanici samplesale uzerinden halkbank combo kart ile MKE satis islemi gecer
    Given kullanici sample sale baslatir
    When kullanici samplesale uzerinden 10000 tutar girer
    And kullanici samplesale uzerinden satis baslatir
    And kullanici mke secimi yapar
    And kullanici Garanti1 kart no girer
    And kullanici Garanti1 skt girer
    And kullanici Garanti1 cvv girer
    And kullanici tamam tusuna basar
    And kullanici puan 1000 girisi yapar
    Then kullanici tamam tusuna basar
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar


  Scenario: TC913 kullanici iptal islemi yapar
    Given kullanici iptal secimi yapar
    When kullanici samplesale uzerinden 10000 tutar girer (iptal)
    And kullanici son stan no bilgisi girer
    And kullanici iptal tusuna basar
    And kullanici banka secimi yapar
    And kullanici mke secimi yapar
    And  kullanici Garanti1 kart no girer
    And  kullanici Garanti1 skt girer
    And  kullanici Garanti1 cvv girer
    Then kullanici tamam tusuna basar
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar








