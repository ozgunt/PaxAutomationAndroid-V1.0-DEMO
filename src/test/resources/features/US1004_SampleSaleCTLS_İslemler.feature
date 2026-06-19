@US1004
Feature: US1004 SampleSale CTLS Islemler

  @TC1 @ignore
  Scenario: kullanici samplesale uzerinden CTLS satis islemi gecer
   # Given kullanici sample sale baslatir
    When  kullanici samplesale uzerinden 10000 tutar girer
    And  kullanici samplesale uzerinden satis baslatir
 #   And kullanici pin girer
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar
    #And ekran kontrolu sonrasi uygulama kapanir
   # Then kullanici geri tusuyla cikis yapar
