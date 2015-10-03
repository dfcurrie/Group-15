#! /bin/bash


sleep 2

# =========================================================
# simple
echo "CASE 1:"
echo "(1,2,1,1,1,2)"
echo ""
sleep 20


# =========================================================
# wait in air
echo "CASE 2:"
echo "(1,10,1,1,1,1)"
echo "(2,10,1,1,1,1)"
echo "(3,10,1,1,1,1)"
echo ""
sleep 20


# =========================================================
# no wait (long enough delay)
echo "CASE 3:"
echo "(1,10,1,1,1,4)"
sleep 22
echo "(2,10,1,1,1,1)"
sleep 20
echo "(3,10,1,1,1,1)"
echo ""
sleep 20


# =========================================================
# no wait (very close delay)
echo "CASE 4:"
echo "(1,10,1,1,1,4)"
sleep 6
echo "(2,10,1,1,1,5)"
sleep 4
echo "(3,10,1,1,3,2)"
echo ""
sleep 30


# =========================================================
# more in each time unit (beginning & end)
echo "CASE 5:"
sleep 1
echo "(1,10,1,1,1,1)"
sleep 2
echo "(2,10,1,1,1,1)"
echo ""
sleep 20


# =========================================================
# fuel fraction success
echo "CASE 6:"
echo "(1,10,3,1,1,4)"
echo ""
sleep 20


# =========================================================
# fuel fraction crash
echo "CASE 7:"
echo "(1,5,3,1,1,4)"
sleep 10
echo "(2,10,1,1,1,1)"
echo ""
sleep 5


# =========================================================
# fuel 0
echo "CASE 8:"
echo "(1,0,1,1,1,4)"
echo ""
sleep 5


# =========================================================
#parking crash
echo "CASE 9:"
echo "(1,41,2,1,5,3)"
echo "(2,41,2,1,5,3)"
echo "(3,90,1,1,1,10)"
echo ""
sleep 52


# =========================================================
#crash in air
echo "CASE 10:"
echo "(1,10,1,1,1,1)"
echo "(2,10,1,1,1,1)"
echo "(3,1,1,1,1,1)"
echo ""
sleep 52


echo "END"
