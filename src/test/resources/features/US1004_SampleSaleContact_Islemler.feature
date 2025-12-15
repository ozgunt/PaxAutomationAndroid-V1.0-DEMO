Feature: US1004 cipli satis islemi yapilir
  @ignore
  Scenario: TC911 cipli dusuk tutar islem yapilir
    Given kullanici sample sale baslatir
    When  kullanici samplesale uzerinden 1 tutar girer
    And  kullanici samplesale uzerinden satis baslatir
    And kullanici ofline pin girer
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar
    And ekran kontrolu sonrasi uygulama kapanir
