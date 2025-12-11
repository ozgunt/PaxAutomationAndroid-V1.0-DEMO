# run-pipeline.ps1
$ErrorActionPreference = "Stop"

# Script'in bulundugu klasoru baz al
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

# .bat'i CMD uzerinden calistir
cmd /c ".\run-pipeline.bat"
