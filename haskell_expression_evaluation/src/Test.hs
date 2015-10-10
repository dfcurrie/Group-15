import Expressions
import Data.Maybe
import Data.Function (on)
import Data.List (sortBy)

sortRes :: Ord b => (a, [(b,c)]) -> (a, [(b,c)])
sortRes (a, m) = (a, sortTuple m)

sortTuple :: Ord a => [(a,b)] -> [(a,b)]
sortTuple xs = sortBy (compare `on` fst) xs


check f arg expectation points text
  | sortRes expectation ==  sortRes (f arg)   = points ++ "\tpassed! " ++ text
  | otherwise                                 = "0\tfailed! " ++ text ++
                                              ". Expected  <" ++ (show (sortRes expectation)) ++
                                              "> Got <" ++ (show (sortRes (f arg))) ++ ">"

checkEval = check $ uncurry eval
checkPrgm = check $ uncurry run


tests :: [String]
tests = [
    "======== Checking Eval ===================================================="
  , checkEval ((ADD (CONST 2) (CONST 3)), []) (5, []) "5" "(2+3) [] -> (5,[])"
  , checkEval ((SUB (CONST 2) (CONST 3)), []) (-1, []) "5" "(2-3) [] -> (-1,[])"
  , checkEval ((MUL (CONST 2) (CONST 3)), []) (6, []) "5" "(2*3) [] -> (6,[])"
  , checkEval ((DIV (CONST 2) (CONST 3)), []) (0, []) "5" "(2/3) [] -> (0,[])"
  , checkEval ((VAR "x"), [("x",1)]) (1, [("x",1)]) "5" "(x) [(x=1)] -> (1, [(x,1)])"
  , checkEval ((ASN "x" (CONST 1)), []) (1, [("x",1)]) "5" "(x=1) [] -> (1, [(x,1)])"
  , checkEval ((ASN "x" (CONST 1)), [("y",2)]) (1,  [("x",1),("y",2)]) "5" "(x=1) [(y,2)] -> (1, [(x,1),(y,2)])"
  , checkEval ((ADD (ASN "x" (CONST 1)) (ASN "y" (CONST 2))), []) (3, [("x",1),("y",2)]) "5" "(x=1 + y=2) [] -> (3, [(x,1),(y,2)])"
  , checkEval ((ADD (MUL (CONST 2) (ASN "x" (CONST 2)) ) (SUB (CONST 3) (MUL (CONST 4) (ASN "y" (CONST 2))))), [("z",1)]) (-1, [("x",2),("y",2),("z",1)]) "10" "( (2 * x=2) + (3 - (4 * y=2) )  ) [(z,1)] -> (-1, [(x,2), (y,2), (z,1)]"
  , "======== Checking Run ===================================================="
  , checkPrgm ([(ADD (CONST 1) (CONST 2))], []) (3, [])  "5" "[1+2] [] -> (3, [])"
  , checkPrgm ([ADD (VAR "x") (CONST 2)], [("x",1)]) (3, [("x",1)])  "5" "[x+2] [(x,1)] -> (3, [(x,1)])"
  , checkPrgm ([ADD (CONST 2) (CONST 1), ADD (CONST 2) (CONST 3), ADD (CONST 3) (CONST 4), ADD (CONST 4) (CONST 5)], [("x",1)]) (9,[("x",1)])  "5" "[2+1, 2+3, 3+4, 4+5] [(x,1)] -> (9, [(x,1)])"
  , checkPrgm ([ADD (CONST 2) (CONST 1), ADD (CONST 2) (CONST 3), ADD (CONST 3) (CONST 4), ADD (CONST 4) (CONST 5)], []) (9,[])  "10" "[2+1, 2+3, 3+4, 4+5] [] -> (9, [])"
  , checkPrgm ([ASN "x" (CONST 2), ASN "y" (CONST 3), ASN "z" (CONST 4), ASN "w" (ADD (VAR "x") (MUL (VAR "y") (VAR "z")))], []) (14,[("x",2),("y",3),("z",4),("w",14)])  "10" "[x=2, y=3, z=4, w=(x+(y*z))] [] -> (14, [(x,2),(y,3),(z,4),(w,14)])"
  , "======== Bonus points ===================================================="
  , checkPrgm ([ASN "x" (CONST 2), ASN "x" (ADD (VAR "x") (CONST 3)), ASN "y" (MUL (VAR "x") (CONST 2)), ASN "z" (VAR "y")], []) (10,[("x",5),("y",10),("z",10)])  "10" "[x=2, x=(x+3), y=(x*2), z=y] [] -> (10, [(x,5),(y,10),(z,10)])"
  , "======== Your code should pass 'at most' one the following two ==========="
  , checkEval (ADD (ASN "x" (ADD (CONST 2) (VAR "x"))) (ASN "x" (SUB (VAR "x") (CONST 1))), [("x",10)]) (21, [("x",9)]) "15" "( (x=(2 + x)) + (x=(x - 1)) ) [(x,10)] -> (21, [(x,9)])"
  , checkEval (ADD (ASN "x" (ADD (CONST 2) (VAR "x"))) (ASN "x" (SUB (VAR "x") (CONST 1))), [("x",10)]) (23, [("x",11)]) "20" "( (x=(2 + x)) + (x=(x - 1)) ) [(x,10)] -> (23, [(x,11)])"
  ]
main :: IO ()
main = for each
  where
    for [] = return ()
    for (x:xs) = do
      _ <- putStrLn x
      for xs
    each = [ test | test <- tests ]
