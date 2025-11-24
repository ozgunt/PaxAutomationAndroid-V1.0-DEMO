Feature: US1003_SampleSaleCTLS_İslemler
  @ignore
  Scenario: TC909 kullanici samplesale uzerinden CTLS satis islemi gecer
    Given kullanici sample sale baslatir
    When  kullanici samplesale uzerinden tutar girer
    And  kullanici samplesale uzerinden satis baslatir
    And kullanici pin girer
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar
    And ekran kontrolu sonrasi uygulama kapanir
