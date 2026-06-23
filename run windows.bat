@echo off
if not exist out mkdir out
javac -d out src\*.java
if errorlevel 1 pause
java -cp "out;lib\*" BankApplicationForm
pause
