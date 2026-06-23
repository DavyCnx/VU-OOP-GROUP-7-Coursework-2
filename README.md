# VU-OOP-GROUP-7-Coursework-2

FIRST BANK UGANDA - ACCOUNT OPENING APP

This is a Java Swing desktop app for the OOP coursework.

The Java app creates FirstBankUganda.accdb here when UCanAccess jars are present.
If the jars are not present, it saves to FirstBankUganda_records.csv instead.

FILES
- src/*.java: Java source code.
- database/: the Access database will be made here when UCanAccess is used.
- lib/: put UCanAccess jar files here if you want MS Access save mode.
- run_windows.bat: compile and run on Windows.
- run_linux_mac.sh: compile and run on Linux or macOS.

HOW TO RUN WITHOUT MS ACCESS JARS
1. Open a terminal in this folder.
2. Run:
   javac -d out src/*.java
   java -cp out BankApplicationForm
3. The app runs and saves records to database/FirstBankUganda_records.csv.

HOW TO RUN WITH MS ACCESS DATABASE
1. Download UCanAccess and copy all jar files into the lib folder.
2. Keep the folder structure as it is.
3. On Windows, double-click run_windows.bat.
4. The app will create database/FirstBankUganda.accdb if it does not exist.
5. It will save account records into the Accounts table.

VALID TEST DATA
First Name: Allan
Last Name: Okello
NIN: CM1234567890AB
Email: okello.allan@firstbank.co.ug
Confirm Email: okello.allan@firstbank.co.ug
Phone: +256772123456
PIN: 1234
Confirm PIN: 1234
DOB: 2004 February 29
Account Type: Savings
Branch: Kampala
Opening Deposit: 50000

NOTES
- Joint accounts need a second NIN.
- Student accounts only allow age 18 to 25.
- The day list changes with the month and leap year.
