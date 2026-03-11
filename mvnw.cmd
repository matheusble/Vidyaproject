@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements. See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership. The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied. See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM Maven Wrapper startup script for Windows
@REM Required ENV: JAVA_HOME - location of JDK home

@echo off
title %0
if "%MAVEN_BATCH_ECHO%" == "on" echo %MAVEN_BATCH_ECHO%

if "%HOME%" == "" set "HOME=%HOMEDRIVE%%HOMEPATH%"

set ERROR_CODE=0
setlocal

if "%JAVA_HOME%" == "" (
  echo.
  echo ERROR: JAVA_HOME nao encontrado. Defina JAVA_HOME na variavel de ambiente
  echo apontando para o diretorio do JDK 17.
  echo.
  set ERROR_CODE=1
  goto end
)
if not exist "%JAVA_HOME%\bin\java.exe" (
  echo.
  echo ERROR: JAVA_HOME aponta para diretorio invalido: %JAVA_HOME%
  echo.
  set ERROR_CODE=1
  goto end
)

set "MAVEN_PROJECTBASEDIR=%~dp0"
cd /d "%MAVEN_PROJECTBASEDIR%"
set "MAVEN_PROJECTBASEDIR=%CD%"

set "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

if not exist "%WRAPPER_JAR%" (
  echo Baixando Maven Wrapper...
  powershell -NoProfile -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar', '%WRAPPER_JAR%')"
  if errorlevel 1 (
    echo Falha ao baixar maven-wrapper.jar
    set ERROR_CODE=1
    goto end
  )
)

"%JAVA_HOME%\bin\java.exe" -classpath "%WRAPPER_JAR%" -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" "%WRAPPER_LAUNCHER%" %*
set ERROR_CODE=%ERRORLEVEL%

:end
endlocal
exit /B %ERROR_CODE%
