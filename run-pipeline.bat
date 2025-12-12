@echo off
chcp 1254 >nul
setlocal enabledelayedexpansion
rem === Script nerede duruyorsa oraya cd yap ===
cd /d "%~dp0"
rem ===== MAVEN KOMUTU =====
rem Burayı kendi makinene göre ayarladın: dokunmuyorum
set "MVN_EXE=C:\apache-maven-3.9.11\bin\mvn.cmd"
rem Eger wrapper kullanacaksan:
rem set "MVN_EXE=%~dp0mvnw.cmd"
if not exist "%MVN_EXE%" (
    echo [ERROR] MVN_EXE bulunamadi: %MVN_EXE%
    exit /b 1
)
rem KAC KEZ TEKRAR EDECEK
set "ITER=3"
rem Log klasoru
mkdir "logs\pipeline" 2>nul
rem Sayaçlar
set /a TOTAL_RUNS=0
set /a TOTAL_FAILS=0
rem Eski summary'i sil
del /q "logs\pipeline\summary.log" 2>nul
echo ================= PIPELINE BASLADI =================> "logs\pipeline\summary.log"
echo ITER=%ITER%>> "logs\pipeline\summary.log"
echo.>> "logs\pipeline\summary.log"
for /L %%i in (1,1,%ITER%) do (
    set /a TOTAL_RUNS+=1
    set "LOGFILE=logs\pipeline\iter_%%i.log"
    echo.
    echo [%date% !time!] ##########################################################
    echo [%date% !time!] ITERATION %%i : @run senaryolari calisiyor
    echo [%date% !time!] ##########################################################
    echo ---------------------------------------------------------->> "logs\pipeline\summary.log"
    echo ITERATION %%i basladi>> "logs\pipeline\summary.log"
    del /q "!LOGFILE!" 2>nul
    rem === MAVEN CIKTISI: CANLI KONSOL + DOSYA (TEE) ===
    rem -> adimlar ANLIK konsola akar
    rem -> ayni anda !LOGFILE! icine yazilir
    powershell -Command " & '%MVN_EXE%' '-Dtest=Runner' '-Dcucumber.filter.tags=@run' '-Dcucumber.plugin=pretty' test 2>&1 | ForEach-Object { $_ | Out-File -FilePath '!LOGFILE!' -Append -Encoding utf8; $_ }; exit $LASTEXITCODE"
    set "EXIT_CODE=!ERRORLEVEL!"
    if !EXIT_CODE! NEQ 0 (
        set /a TOTAL_FAILS+=1
        call :LogFail %%i "!LOGFILE!"
    ) else (
        echo [OK] ITER %%i PASS>> "logs\pipeline\summary.log"
    )
    echo [SOFAR] TOTAL_RUNS=!TOTAL_RUNS! TOTAL_FAILS=!TOTAL_FAILS!>> "logs\pipeline\summary.log"
)
echo.>> "logs\pipeline\summary.log"
echo ================= PIPELINE BITTI =================>> "logs\pipeline\summary.log"
echo Toplam Maven run: !TOTAL_RUNS!>> "logs\pipeline\summary.log"
echo Toplam FAIL: !TOTAL_FAILS!>> "logs\pipeline\summary.log"
echo.
echo [%date% !time!] ===== PIPELINE OZET =====
echo [%date% !time!] Toplam Maven run: !TOTAL_RUNS!
echo [%date% !time!] Toplam FAIL: !TOTAL_FAILS!
echo [%date% !time!] Detay icin: logs\pipeline\summary.log
echo.
echo [%date% !time!] ----- FAIL DETAYLARI -----
findstr /C:"FAIL " "logs\pipeline\summary.log" || echo [%date% !time!] (FAIL yok)
endlocal
goto :eof
:LogFail
rem %1 = iter, %2 = logfile
setlocal enabledelayedexpansion
set "ITER=%~1"
set "LOGFILE=%~2"
set "FAIL_LINE="
set "STEP_NAME="
rem 1) Logdan ilk "FAIL" iceren satiri cek (ASCII icin)
for /f "usebackq tokens=* delims=" %%L in (`findstr /c:"FAIL" "!LOGFILE!"`) do (
    if not defined FAIL_LINE set "FAIL_LINE=%%L"
)
rem 2) Failing step'i bul ve extract et - PowerShell ile Unicode destekli
for /f "usebackq tokens=* delims=" %%S in (`powershell -Command "$content = Get-Content '!LOGFILE!' -Encoding UTF8; $line = $content | Where-Object {$_ -match 'at [✗?]\.'} | Select-Object -First 1; if ($line) { if ($line -match 'at [✗?]\.(.*)\(') { $matches[1].Trim() } else { $line.Trim() } } else { '' }"`) do (
    set "STEP_NAME=%%S"
)
if not defined STEP_NAME set "STEP_NAME=(Cucumber adimi bulunamadi)"
set "SHORT_DATE=%date:~4,2%.%date:~7,2%.%date:~12,2%"
set "SHORT_TIME=%time:~0,5%"
(
    echo !SHORT_DATE! !SHORT_TIME! Iter!ITER! FAIL !STEP_NAME!
    echo.
)>> "logs\pipeline\summary.log"
endlocal
goto :eof