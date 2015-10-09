#! /bin/bash

./testcase.sh | java -cp bin/ `cat MAIN_CLASS` -f input.txt -o output.txt &
PID=$!
echo $PID
sleep 1200
kill $PID

echo "DONE\n"
