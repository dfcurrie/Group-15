#! /bin/bash

./testcase.sh | java -cp bin/ `cat MAIN_CLASS` -f input.txt -o output.txt > STDOUT 2> STDERROR &
PID=$!
echo $PID
sleep 1200
kill $PID
if [ $? -ne 0 ] ; then
    echo "Killed `pwd`"
fi

