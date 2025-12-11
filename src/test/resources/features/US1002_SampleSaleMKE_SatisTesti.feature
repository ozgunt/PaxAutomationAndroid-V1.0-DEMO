Feature: US1002_SampleSaleMKE_SatisTesti
@ignore
  Scenario:TC906 kullanici samplesale uzerinden halkbank combo kart ile MKE satis islemi gecer
    Given kullanici sample sale baslatir
    When  kullanici samplesale uzerinden 10000 tutar girer
    And  kullanici samplesale uzerinden satis baslatir
    And  kullanici mke secimi yapar
    And  kullanici halkbank1 combo KK no girer
    And  kullanici halkbank1 combo skt girer
    And  kullanici halkbank1 combo KK cvv girer
    Then kullanici tamam tusuna basar
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar
    Then uygulamalar kapatilir


  @ignore
  Scenario:TC907 kullanici samplesale uzerinden ziraat combo kart ile MKE satis islemi gecer
    Given kullanici sample sale baslatir
    When  kullanici samplesale uzerinden 10000 tutar girer
    And  kullanici samplesale uzerinden satis baslatir
    And  kullanici mke secimi yapar
    And  kullanici ziraat1 combo KK no girer
    And  kullanici ziraat1 combo skt girer
    And  kullanici ziraat1 combo KK cvv girer
    Then kullanici tamam tusuna basar
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar
    Then uygulamalar kapatilir
