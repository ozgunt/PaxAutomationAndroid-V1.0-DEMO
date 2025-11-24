# === OTOMASYON İÇİN TECHPOS LOG ALICI ===
# Test çalışırken logu alır → proje klasörüne kaydeder

# 1. Proje klasörünü al (scriptin bulunduğu klasör)
$projectDir = $PSScriptRoot
if (-not $projectDir) { $projectDir = (Get-Location).Path }

# 2. ADB yolunu otomatik bul
$adbPath = $null
$possiblePaths = @(
    "$env:ANDROID_HOME\platform-tools\adb.exe",
    "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe",
    "C:\Android\platform-tools\adb.exe"
)

foreach ($path in $possiblePaths) {
    if (Test-Path $path) { $adbPath = $path; break }
}

if (-not $adbPath) {
    Write-Host "ADB BULUNAMADI!" -ForegroundColor Red
    exit 1
}

$env:Path += ";$(Split-Path $adbPath)"

# 3. Cihazı al
$deviceLine = & adb devices | Select-String "device$" | Select-Object -First 1
if (-not $deviceLine) {
    Write-Host "CİHAZ BAĞLI DEĞİL!" -ForegroundColor Red
    exit 1
}

$device = $deviceLine.ToString().Split()[0]
$date = Get-Date -Format "yyyyMMdd_HHmmss"
$logFile = Join-Path $projectDir "logs\techpos_log_${device}_$date.txt"

# 4. logs klasörü yoksa oluştur
$logDir = Join-Path $projectDir "logs"
if (-not (Test-Path $logDir)) { New-Item -ItemType Directory -Path $logDir -Force | Out-Null }

# 5. Logu al ve kaydet
& adb -s $device logcat -d | Select-String "(com\.pax\.techpos|com\.pax\.mainapp)" | Out-File -FilePath $logFile -Encoding UTF8

Write-Host "LOG KAYDEDİLDİ: $logFile" -ForegroundColor Green