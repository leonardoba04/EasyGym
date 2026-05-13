@echo off
title EasyGym V2
color 0A

REM ── Define o JDK 21 diretamente ────────────────────────────────
set JAVA_HOME=%LOCALAPPDATA%\Programs\Eclipse Adoptium\jdk-21.0.11.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

REM ── Verifica se encontrou ───────────────────────────────────────
if not exist "%JAVA_HOME%\bin\javac.exe" (
    REM Tenta a versao 21.0.10 como fallback
    set JAVA_HOME=%LOCALAPPDATA%\Programs\Eclipse Adoptium\jdk-21.0.10.7-hotspot
    set PATH=%JAVA_HOME%\bin;%PATH%
)

if not exist "%JAVA_HOME%\bin\javac.exe" (
    echo.
    echo [ERRO] JDK 21 nao encontrado em:
    echo %LOCALAPPDATA%\Programs\Eclipse Adoptium\
    echo.
    echo Instale em: https://adoptium.net
    pause
    exit /b 1
)

echo.
echo  ===================================================
echo   EasyGym V2 - Plataforma de Gestao de Academias
echo  ===================================================
echo   JDK: %JAVA_HOME%
echo.

REM ── Maven ───────────────────────────────────────────────────────
set MAVEN_EXE=%USERPROFILE%\.easygym\maven\apache-maven-3.9.6\bin\mvn.cmd

if exist "%MAVEN_EXE%" goto :rodar

echo Baixando Maven pela primeira vez, aguarde...
mkdir "%USERPROFILE%\.easygym\maven" 2>nul
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip' -OutFile '%USERPROFILE%\.easygeh\maven\maven.zip'"
if %errorlevel% neq 0 ( echo [ERRO] Falha ao baixar Maven. & pause & exit /b 1 )
powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%USERPROFILE%\.easygym\maven\maven.zip' -DestinationPath '%USERPROFILE%\.easygym\maven' -Force"
del "%USERPROFILE%\.easygym\maven\maven.zip" 2>nul
echo Maven pronto!

:rodar
echo.
echo  Aguarde 20-30 segundos e acesse: http://localhost:8080
echo.
echo   EQUIPE
echo   Admin:       admin@fitzone.com  /  admin123
echo   Funcionario: joao@fitzone.com   /  func123
echo.
echo   ALUNOS
echo   MAT00001  /  123456
echo   MAT00002  /  123456
echo  ===================================================
echo.
call "%MAVEN_EXE%" spring-boot:run
pause
