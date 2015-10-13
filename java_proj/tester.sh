#! /bin/bash

$1 | java -cp bin/ `cat MAIN_CLASS` -f input.txt -o output.txt  > STDOUT 2> STDERROR &
PID=$!
echo $PID
sleep $2
kill $PID
if [ $? -ne 0 ] ; then
    echo "Killed `pwd`"
fi

