Feature: US1006_SampleSale satış günsonu iade işlemleri
  @TestRun
  Scenario: TC914 kullanici samplesale uzerinden halkbank combo kart ile MKE satis islemi gecer
    Given kullanici sample sale baslatir
    When  kullanici samplesale uzerinden 10000 tutar girer
    And  kullanici samplesale uzerinden satis baslatir
    And  kullanici mke secimi yapar
    And  kullanici manager "garanti1" kart no girer
    And  kullanici manager "garanti1" skt girer
    And  kullanici manager "garanti1" cvv girer
    Then kullanici tamam tusuna basar
    And kullanici islem basarili mesaji sonrasi tamam tusuna basar
    And kullanici samplesale satis slibine basar




  @TestRun
  Scenario: TC915 kullanici gunsonu yapar
    When kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici isyeri menuden gunsonu secimi yapar
    And kullanici samplesale gunsonu slibine dokunur



@TestRun
  Scenario: TC916 kullanici iade islemi yapar
  Given kullanici iade secimi yapar
  When kullanici samplesale uzerinden 10000 tutar girer (iptal)
  And kullanici son stan no bilgisi girer
  And kullanici son islem referans numarasi girer
  And  kullanici klavyeyi kapatir
  And kullanici iptal tusuna basar
  And kullanici "garanti1" banka secimi yapar
  And kullanici techpos mke secimi yapar
  And kullanici techpos "garanti1" kart no girer
  Then kullanici techpos giris tusuna basar
  And  kullanici techpos "garanti1" skt girer
  Then kullanici techpos giris tusuna basar
  And  kullanici techpos "garanti1" cvv girer
    #Then kullanici techpos giris tusuna basar
  And kullanici islem basarili mesaji sonrasi tamam tusuna basar
  And kullanici samplesale satis slibine basar
  And uygulamalar kapatilir





