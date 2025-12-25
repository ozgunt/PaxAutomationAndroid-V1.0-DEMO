@echo off
chcp 1254 >nul
setlocal enabledelayedexpansion

rem === Script nerede duruyorsa oraya cd yap ===
cd /d "%~dp0"

rem ===== MAVEN KOMUTU =====
rem Burayı kendi makinene göre ayarladın: dokunmuyorum
set "MVN_EXE=%~dp0tools\apache-maven-3.9.12\bin\mvn.cmd"
rem Eger wrapper kullanacaksan:
rem set "MVN_EXE=%~dp0mvnw.cmd"

if not exist "%MVN_EXE%" (
    echo [ERROR] MVN_EXE bulunamadi: %MVN_EXE%
    exit /b 1
)

rem KAC KEZ TEKRAR EDECEK
set "ITER=100"

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

    rem === MAVEN CIKTISI: CANLI KONSOL + DOSYA (TEE) ===
    rem  -> adimlar ANLIK konsola akar
    rem  -> ayni anda !LOGFILE! icine yazilir
    powershell -Command " & '%MVN_EXE%' '-Dtest=Runner' '-Dcucumber.filter.tags=@PipeLineRun' '-Dcucumber.plugin=pretty' test 2>&1 | Tee-Object -FilePath '!LOGFILE!'; exit $LASTEXITCODE"
    set "EXIT_CODE=!ERRORLEVEL!"

    if !EXIT_CODE! NEQ 0 (
        set /a TOTAL_FAILS+=1
        call :LogFail %%i "!LOGFILE!"
    ) else (
        echo [OK]   ITER %%i PASS>> "logs\pipeline\summary.log"
    )

    echo [SOFAR] TOTAL_RUNS=!TOTAL_RUNS! TOTAL_FAILS=!TOTAL_FAILS!>> "logs\pipeline\summary.log"
)

echo.>> "logs\pipeline\summary.log"
echo ================= PIPELINE BITTI =================>> "logs\pipeline\summary.log"
echo Toplam Maven run: !TOTAL_RUNS!>> "logs\pipeline\summary.log"
echo Toplam FAIL:      !TOTAL_FAILS!>> "logs\pipeline\summary.log"

echo.
echo [%date% !time!] ===== PIPELINE OZET =====
echo [%date% !time!] Toplam Maven run: !TOTAL_RUNS!
echo [%date% !time!] Toplam FAIL:      !TOTAL_FAILS!
echo [%date% !time!] Detay icin: logs\pipeline\summary.log
echo.
echo [%date% !time!] ----- FAIL DETAYLARI -----
findstr /C:"[FAIL]" "logs\pipeline\summary.log" || echo [%date% !time!] (FAIL yok)

endlocal
goto :eof


:LogFail
rem %1 = iter, %2 = logfile
setlocal enabledelayedexpansion
set "ITER=%~1"
set "LOGFILE=%~2"
set "FAIL_LINE="
set "STEP_LINE="

rem 1) Logdan ilk "FAIL" iceren satiri cek
for /f "usebackq tokens=* delims=" %%L in ('findstr /c:"FAIL" "!LOGFILE!"') do (
    if not defined FAIL_LINE set "FAIL_LINE=%%L"
)

rem 2) Özellikle "Adim FAIL oldu" satirini bul (screenshot aliniyor mesaji)
for /f "usebackq tokens=* delims=" %%S in ('findstr /c:"Adim FAIL oldu" "!LOGFILE!"') do (
    if not defined STEP_LINE set "STEP_LINE=%%S"
)

if not defined FAIL_LINE (
    set "FAIL_LINE=(Detay icin: !LOGFILE!)"
)

(
    if defined STEP_LINE (
        rem Örnek:
        rem [FAIL] ITER 29 17:37:25 [main] WARN ... - ❌ Adim FAIL oldu → Screenshot aliniyor...
        echo [FAIL] ITER !ITER! !STEP_LINE!
    ) else (
        echo [FAIL] ITER !ITER!
    )
    echo        LOG : !FAIL_LINE!
    echo.
)>> "logs\pipeline\summary.log"

endlocal
goto :eof