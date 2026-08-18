$ErrorActionPreference = "Stop"

function Invoke-WithRetry {
    param(
        [scriptblock]$Action,
        [int]$Retries = 20,
        [int]$DelaySeconds = 2
    )

    for ($i = 1; $i -le $Retries; $i++) {
        try {
            return & $Action
        } catch {
            if ($i -eq $Retries) {
                throw
            }
            Start-Sleep -Seconds $DelaySeconds
        }
    }
}

Push-Location "C:\Users\nicop\Documents\Programming\discord-micro-services-platform"
$env:JAVA_HOME = "C:\Users\nicop\.jdks\openjdk-24.0.1"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "[smoke] build required jars"
.\gradlew.bat :projects:bot-api:bootJar :projects:bot-template-standard:bootJar | Out-Host

Push-Location "infrastructure\docker"
Write-Host "[smoke] reset compose environment"
docker compose down -v | Out-Host

Write-Host "[smoke] start compose services"
docker compose up -d --build postgres rabbitmq bot-api bot-template-standard control-panel | Out-Host

Write-Host "[smoke] verify frontend availability"
$rootStatus = Invoke-WithRetry -Action { (Invoke-WebRequest -Uri "http://localhost:3000" -UseBasicParsing -TimeoutSec 5).StatusCode }
if ($rootStatus -ne 200) { throw "Frontend root returned $rootStatus" }

Write-Host "[smoke] verify bot list endpoint"
$bots = Invoke-WithRetry -Action { Invoke-RestMethod -Uri "http://localhost:3000/api/bots" -Method Get -TimeoutSec 10 }
if (-not ($bots | Where-Object { $_.botId -eq "bot-template-standard" })) {
    throw "bot-template-standard not found in bot list"
}

Write-Host "[smoke] trigger START lifecycle"
$payload = '{"action":"START","profile":"MEDIUM","requestedBy":"smoke-test"}'
$startResponse = Invoke-WithRetry -Action {
    Invoke-RestMethod -Uri "http://localhost:3000/api/bots/bot-template-standard/lifecycle" -Method Post -ContentType "application/json" -Body $payload -TimeoutSec 10
} -Retries 20 -DelaySeconds 2
if ($startResponse.status -ne "accepted") {
    throw "Lifecycle START was not accepted"
}

Write-Host "[smoke] wait for status propagation"
$statusReached = $false
for ($i = 1; $i -le 20; $i++) {
    $bots = Invoke-RestMethod -Uri "http://localhost:3000/api/bots" -Method Get -TimeoutSec 10
    $bot = $bots | Where-Object { $_.botId -eq "bot-template-standard" }
    if ($null -ne $bot -and $bot.liveStatus -eq "START") {
        $statusReached = $true
        break
    }
    Start-Sleep -Seconds 2
}

if (-not $statusReached) {
    throw "Live status START was not observed for bot-template-standard"
}

Write-Host "[smoke] SUCCESS"
Pop-Location
Pop-Location


