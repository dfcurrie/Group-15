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
eval ex mem = (0, [])

-- A function that runs a series of expressions (i.e. a program) given a memory
-- returns the result of the evaluation of the "last" expression and the final updated memory
run :: Prg -> Mem -> (Int, Mem)
run prg mem = (0, [])
