#!/bin/sh
mkdir -p out
javac -d out src/*.java || exit 1
java -cp "out:lib/*" BankApplicationForm
