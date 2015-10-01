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
--eval ex mem = (0, [])
eval (CONST cnst) mem = (cnst, [])
eval (ADD ex y) mem = (fst(eval ex []) + fst(eval y []), [])
eval (SUB ex y) mem = (fst(eval ex []) - fst(eval y []), [])
eval (MUL ex y) mem = (fst(eval ex []) * fst(eval y []), [])
eval (DIV ex y) mem = (fst(eval ex []) `div` fst(eval y []), [])
eval (VAR strng) mem = return value in var strng
eval (ASN strng ex) mem = put ex in var strng


-- A function that runs a series of expressions (i.e. a program) given a memory
-- returns the result of the evaluation of the "last" expression and the final updated memory
run :: Prg -> Mem -> (Int, Mem)
run prg mem = (0, [])


-- A function to help with evaluating functions that don't require looking into Mem
-- returns the result of the function. Essentially a simpler version of eval
eval_1 :: Exp -> Int 
eval_1 (CONST x) = x
eval_1 (ADD x y) = (eval_1 x) + (eval_1 y)
eval_1 (SUB x y) = (eval_1 x) - (eval_1 y)
eval_1 (MUL x y) = (eval_1 x) * (eval_1 y)
eval_1 (DIV x y) = (eval_1 x) `div` (eval_1 y)