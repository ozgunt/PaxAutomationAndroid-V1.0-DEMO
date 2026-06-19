@echo off
chcp 1254 >nul
setlocal enabledelayedexpansion

cd /d "%~dp0"

set "MVN_EXE=C:\Program Files\JetBrains\IntelliJ IDEA 2025.3\plugins\maven\lib\maven3\bin\mvn.cmd"

if not exist "%MVN_EXE%" (
    echo [ERROR] MVN_EXE bulunamadi: %MVN_EXE%
    exit /b 1
)

set "ITER=600"
set "WAIT_BETWEEN=5"

mkdir "logs\pipeline" 2>nul

set /a TOTAL_RUNS=0
set /a TOTAL_FAILS=0

del /q "logs\pipeline\summary.log" 2>nul

echo ================= PIPELINE BASLADI =================> "logs\pipeline\summary.log"
echo ITER=%ITER%>> "logs\pipeline\summary.log"
echo.>> "logs\pipeline\summary.log"

for /L %%i in (1,1,%ITER%) do (
    set /a TOTAL_RUNS+=1
    set "LOGFILE=logs\pipeline\iter_%%i.log"

    echo.
    echo [%date% !time!] ##########################################################
    echo [%date% !time!] ITERATION %%i : @runepipeline senaryolari calisiyor
    echo [%date% !time!] ##########################################################
    echo ---------------------------------------------------------->> "logs\pipeline\summary.log"
    echo ITERATION %%i basladi>> "logs\pipeline\summary.log"

    set "EXIT_CODE_FILE=logs\pipeline\exit_%%i.tmp"

    rem Maven testi doğrudan çalıştırılır ve log dosyasına yazılır (IntelliJ inheritIO ile çakışmaz)
    call "%MVN_EXE%" "-Dtest=Runner" "-Dcucumber.filter.tags=@run" "-Dcucumber.plugin=pretty" test > "!LOGFILE!" 2>&1
    set "EXIT_CODE=!ERRORLEVEL!"
    echo !EXIT_CODE! > "!EXIT_CODE_FILE!"

    set /p EXIT_CODE=<"!EXIT_CODE_FILE!"
    del /q "!EXIT_CODE_FILE!" 2>nul

    if "!EXIT_CODE!" NEQ "0" (
        set /a TOTAL_FAILS+=1
        call :LogFail %%i "!LOGFILE!"
    ) else (
        echo [OK]   ITER %%i PASS>> "logs\pipeline\summary.log"
    )

    echo [SOFAR] TOTAL_RUNS=!TOTAL_RUNS! TOTAL_FAILS=!TOTAL_FAILS!>> "logs\pipeline\summary.log"

    rem Etkileşimsiz ortamda kilitlenmeyen ping tabanlı bekleme
    if %%i LSS %ITER% (
        ping 127.0.0.1 -n 6 >nul
    )
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
setlocal enabledelayedexpansion
set "ITER=%~1"
set "LOGFILE=%~2"
set "FAIL_LINE="
set "STEP_LINE="

for /f "usebackq tokens=* delims=" %%L in (findstr /c:"FAIL" "!LOGFILE!") do (
    if not defined FAIL_LINE set "FAIL_LINE=%%L"
)

for /f "usebackq tokens=* delims=" %%S in (findstr /c:"Adim FAIL oldu" "!LOGFILE!") do (
    if not defined STEP_LINE set "STEP_LINE=%%S"
)

if not defined FAIL_LINE set "FAIL_LINE=(Detay icin: !LOGFILE!)"

(
    if defined STEP_LINE (
        echo [FAIL] ITER !ITER! !STEP_LINE!
    ) else (
        echo [FAIL] ITER !ITER!
    )
    echo        LOG : !FAIL_LINE!
    echo.
)>> "logs\pipeline\summary.log"

endlocal
goto :eof