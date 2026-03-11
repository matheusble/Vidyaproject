# Maven Wrapper - PowerShell
# Uso: .\mvnw.ps1 spring-boot:run
# Defina JAVA_HOME se necessario: $env:JAVA_HOME = "C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\jbr"

$ErrorActionPreference = "Stop"
$projectDir = $PSScriptRoot
if (-not $projectDir) { $projectDir = Get-Location }

if (-not $env:JAVA_HOME) {
    Write-Host ""
    Write-Host "ERRO: JAVA_HOME nao definido. Exemplo:" -ForegroundColor Red
    Write-Host '  $env:JAVA_HOME = "C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\jbr"' -ForegroundColor Yellow
    Write-Host ""
    exit 1
}

$javaExe = Join-Path $env:JAVA_HOME "bin\java.exe"
if (-not (Test-Path $javaExe)) {
    Write-Host "ERRO: java.exe nao encontrado em $env:JAVA_HOME" -ForegroundColor Red
    exit 1
}

$wrapperJar = Join-Path $projectDir ".mvn\wrapper\maven-wrapper.jar"
if (-not (Test-Path $wrapperJar)) {
    Write-Host "Baixando Maven Wrapper..."
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    $wrapperUrl = "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    (New-Object Net.WebClient).DownloadFile($wrapperUrl, $wrapperJar)
}

$args = @(
    "-classpath", $wrapperJar,
    "-Dmaven.multiModuleProjectDirectory=$projectDir",
    "org.apache.maven.wrapper.MavenWrapperMain"
) + $args

& $javaExe @args
exit $LASTEXITCODE
