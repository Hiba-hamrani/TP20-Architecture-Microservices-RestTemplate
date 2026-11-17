@echo off
cd /d "%~dp0"
mvn clean package -DskipTests
pause
