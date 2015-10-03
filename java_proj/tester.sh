#! /bin/bash

./testcase.sh | java -cp bin/ `cat MAIN_CLASS` -f input.txt -o output.txt
