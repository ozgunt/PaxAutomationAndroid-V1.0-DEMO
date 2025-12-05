@echo off
setlocal enabledelayedexpansion

rem === Script nerede duruyorsa oraya cd yap (user adi, disk onemsiz) ===
cd /d "%~dp0"

rem KAC KEZ TEKRAR EDECEK
set ITER=10

for /L %%i in (1,1,%ITER%) do (
    echo.
    echo ##########################################################
    echo ===== ITERATION %%i : TC907 calisiyor =====
    echo ##########################################################
    mvn test -Dcucumber.filter.name="TC907" -Dcucumber.plugin=pretty
    echo [INFO] TC907 bitti, exit code: !ERRORLEVEL!
    if errorlevel 1 (
        echo [FAIL] TC907 iterasyon %%i
        goto :end
    )

    echo.
    echo ##########################################################
    echo ===== ITERATION %%i : TC906 calisiyor =====
    echo ##########################################################
    mvn test -Dcucumber.filter.name="TC906" -Dcucumber.plugin=pretty
    echo [INFO] TC906 bitti, exit code: !ERRORLEVEL!
    if errorlevel 1 (
        echo [FAIL] TC906 iterasyon %%i
        goto :end
    )
)

:end
echo.
echo Pipeline bitti.
endlocal
