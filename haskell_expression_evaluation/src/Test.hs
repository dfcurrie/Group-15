module Main where
import Expressions
import Data.Maybe

check :: (Show a, Eq a, Show b) => (b -> a) -> a -> b -> Maybe String
check f expectation arg 
  | expectation == (f arg) = Nothing
  | otherwise              = Just $ "for < " ++ (show arg)
                             ++ " > expected  < " ++ (show expectation)
                             ++ " > got < " ++ (show (f arg)) ++ " >"

checkEval = check $ uncurry eval
checkPrgm = check $ uncurry run

arg :: Int
arg = 10
prgm_1 = (ASN "x" (CONST 0)) : [(ASN "x" (ADD (VAR "x") (CONST n))) | n <- [0..arg]]
expect_1 = truncate (toRational arg*(toRational (arg-1))/2.0)

tests :: [Maybe String]
tests = [
  checkEval (5, []) ((ADD (CONST 2) (CONST 3)), [])
  , checkEval (1, []) ((SUB (CONST 3) (CONST 2)), [])
  , checkEval (4, []) ((MUL (CONST 2) (CONST 2)), [])
  , checkEval (3, []) ((DIV (CONST 9) (CONST 3)), [])
  , checkEval (2, [("x", 2)]) ((VAR "x"), [("x", 2)])
  , checkEval (2, [("x", 2)]) ((ASN "x" (CONST 2)), [])
  , checkEval (3, [("x", 3)]) ((ASN "x" (CONST 3)), [("x", 2)])

  , checkPrgm (expect_1, [("x", expect_1)]) (prgm_1, [("arg_1", arg)])
  ]
main :: IO ()
main = for each
  where
    for [] = return ()
    for (x:xs) = do
      _ <- putStrLn x
      for xs
    each = [ result | test <- tests, result <- maybeToList test ]
