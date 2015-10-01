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
eval (ADD ex y) mem = (fst(eval ex mem) + fst(eval y mem), mem)
eval (SUB ex y) mem = (fst(eval ex mem) - fst(eval y mem), mem)
eval (MUL ex y) mem = (fst(eval ex mem) * fst(eval y mem), mem)
eval (DIV ex y) mem = (fst(eval ex mem) `div` fst(eval y mem), mem)
eval (ASN strng ex) mem = (fst(eval ex mem), (strng, fst(eval ex mem)):mem)
eval (VAR strng) mem = (ridMaybeInt 0 (lookup strng mem), mem)
		
		
-- A function that converts a Maybe Int into an Int if the Maybe Int has an Int
-- else it returns a default provided by it's first argument
ridMaybeInt :: Int -> Maybe Int -> Int
ridMaybeInt a Nothing = a
ridMaybeInt a (Just b) = b


-- A function that runs a series of expressions (i.e. a program) given a memory
-- returns the result of the evaluation of the "last" expression and the final updated memory
run :: Prg -> Mem -> (Int, Mem)
run prg mem = (0, [])
