@US1003
Feature: US1003 Menulerin Gorunurlugu Testi

  @US1003 @TC1 @ignore
  Scenario: kullanici techpos merchant menude gorunmesi gereken secimleri test eder
    Given kullanici sample sale baslatir
    When  kullanici samplesale uzerinden merchant menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici techpos sifresi girer
    And kullanici merchant menude bulunan elementlerin gorunurlugunu test eder
    And kullanici geri tusuyla cikis yapar

  @US1003 @TC2 @ignore
  Scenario: kullanici techpos transaction menude gorunmesi gereken secimleri test eder
    When kullanici samplesale uzerinden transaction menuye giris yapar
    And kullanici manager uzerinden uygulama secer techpos
    And kullanici transaction menude bulunan elementlerin gorunurlugunu test eder
    And uygulamalar kapatilir
