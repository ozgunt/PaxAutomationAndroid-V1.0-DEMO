Feature: US1003_Menulerin_Gorununrlugu_Testi
  @ignore
  Scenario: TC908 kullanici techpos merchant menude gorunmesi gereken secimleri test eder
  Given kullanici sample sale baslatir
  When  kullanici samplesale uzerinden merchant menuye giris yapar
  And kullanici manager uzerinden uygulama secer techpos
  And kullanici techpos sifresi girer
  And kullanici merchant menude bulunan elementlerin gorunurlugunu test eder
  And kullanici geri tusuyla cikis yapar

  @ignore
  Scenario: TC910 kullanici techpos transaction menude gorunmesi gereken secimleri test eder

    When kullanici samplesale uzerinden transaction menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici transaction menude bulunan elementlerin gorunurlugunu test eder
    And uygulamalar kapatilir




