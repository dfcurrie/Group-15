module Expressions(Exp(..), Prg, Mem, eval, run) where

data Exp
  = CONST Int
  | VAR String
  | ADD Exp Exp
  | SUB Exp Exp
  | MUL Exp Exp
  | DIV Exp Exp
  | ASN String Exp
    deriving (Show, Eq)
    
type Prg = [Exp]
type Mem = [(String, Int)]

-- A function that evaluates an expression given a memory
-- returns the result of the computation and the updated memory
eval :: Exp -> Mem -> (Int, Mem)
eval (CONST cnst) mem = (cnst, mem)
eval (ADD ex y) mem = (fst(eval ex mem) + fst(eval y (snd(eval ex mem))), snd(eval y (snd(eval ex mem))))
eval (SUB ex y) mem = (fst(eval ex mem) - fst(eval y (snd(eval ex mem))), snd(eval y (snd(eval ex mem))))
eval (MUL ex y) mem = (fst(eval ex mem) * fst(eval y (snd(eval ex mem))), snd(eval y (snd(eval ex mem))))
eval (DIV ex y) mem = (fst(eval ex mem) `div` fst(eval y (snd(eval ex mem))), snd(eval y (snd(eval ex mem))))
eval (ASN strng ex) mem = (fst(eval ex mem), (replaceVar (strng, fst(eval ex mem)) mem))
eval (VAR strng) mem = (ridMaybeInt 0 (lookup strng mem), mem)


-- A function that converts a Maybe Int into an Int if the Maybe Int has an Int
-- else it returns a default provided by it's first argument
ridMaybeInt :: Int -> Maybe Int -> Int
ridMaybeInt a Nothing = a
ridMaybeInt a (Just b) = b


{- A function that checks the memory for any existing instances of the variable
and replaces them with the new instance, or just places it in the memory if it
doesn't already exist-}
replaceVar :: (String, Int) -> Mem -> Mem
replaceVar x [] = [x] 
replaceVar (str,y) (x:xs)
    | str == fst(x) = ((str, y):xs)
    | otherwise = x:(replaceVar (str, y) xs)


-- A function that runs a series of expressions (i.e. a program) given a memory
-- returns the result of the evaluation of the "last" expression and the final updated memory
run :: Prg -> Mem -> (Int, Mem)
run [] mem = (0, mem)
run (x:xs) mem
    | xs == [] = eval x mem
    | otherwise = run xs (snd(eval x mem))
--If the first item in prg is the same as the last, just add it to mem
--    | (head)prg == (last)prg = (eval((head)prg) mem)
--Otherwise, try to add the first element in prg into mem and run again
--with everything but the first item in prg, can't get this part to work
--    | otherwise = ((eval((head)prg) mem) + (run (tail)prg mem))
{-
run prg mem = (0, [])
run prg mem = (eval (fst(prg)) [])

type Prg = [Exp]
type Mem = [(String, Int)]
-}
