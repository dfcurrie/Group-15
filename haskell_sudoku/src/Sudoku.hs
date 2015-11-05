module Sudoku where

import Data.List
import Data.Maybe
import Data.Char
import Data.List.Split
import Control.Monad

-------------------------------------------------------------------------

data Sudoku = Sudoku [[Maybe Int]]
 deriving ( Eq )

--instance Show Sudoku where
-- show (Sudoku ls) = show [ [ f m | m <- row] | row <- ls]
--    where
--      f m = fromMaybe "_" $ fmap (show) m

rows :: Sudoku -> [[Maybe Int]]
rows (Sudoku rs) = rs

-- allBlankSudoku is a sudoku with just blanks
allBlankSudoku :: Sudoku
allBlankSudoku = Sudoku (replicate 9 (replicate 9 Nothing))

-- isSudoku sud checks if sud is really a valid representation of a sudoku
-- puzzle
isSudoku :: Sudoku -> Bool
isSudoku sud  = (length grid == 9) && 
                    (length (transpose grid) == 9) && 
                        (and (map (all isSudokuValue) grid)) 
    where
        grid = rows sud

-- Checks if value at array is a valid value for a Sudoku (nothing, or 1-9)
isSudokuValue :: Maybe Int -> Bool
isSudokuValue Nothing = True
isSudokuValue (Just x)
    | x < 1 = False
    | x > 9 = False
    | otherwise = True

-- isSolved sud checks if sud is already solved, i.e. there are no blanks
isSolved :: Sudoku -> Bool
isSolved sud = (and (map (all notBlank) grid))
    where
        grid = rows sud

notBlank :: Maybe Int -> Bool
notBlank Nothing = False
notBlank _ = True

checkSolveInput :: [Maybe Int] -> Bool
checkSolveInput [] = True
checkSolveInput (Nothing:xs) = False
checkSolveInput ((Just x):xs)
    | x < 1 = False
    | x > 9 = False 
    | otherwise = checkSolveInput xs
-------------------------------------------------------------------------

-- printSudoku sud prints a representation of the sudoku sud on the screen
printSudoku :: Sudoku -> IO ()
printSudoku sud = 
    do
        putStr (sudToString (rows sud)) 

sudToString :: [[Maybe Int]] -> String
sudToString [] = "\n"
sudToString (x:xs) = (rowToString x) ++ (sudToString xs) 

rowToString :: [Maybe Int] -> String
rowToString [] = "\n"
rowToString (Nothing:xs) = '.':(rowToString xs)
rowToString ((Just a):xs) = (chr (a + 48)):(rowToString xs)

-- readSudoku file reads from the file, and either delivers it, or stops
-- if the file did not contain a sudoku
readSudoku :: FilePath -> IO Sudoku
readSudoku file = 
    do
        str <- readFile file
        let rows = lines str
            sud = Sudoku (makeSud rows)
        return (sud)

makeSud :: [String] -> [[Maybe Int]]
makeSud a = map makeRow a

makeRow :: String -> [Maybe Int]
makeRow a = map charToMaybeInt a

charToMaybeInt :: Char -> Maybe Int
charToMaybeInt '.' = Nothing
charToMaybeInt a = Just (digitToInt a)


-------------------------------------------------------------------------

type Block = [Maybe Int]
isOkayBlock :: Block -> Bool
isOkayBlock [] = True
isOkayBlock (x@(Just a):xs) = not (elem x xs) && isOkayBlock xs
isOkayBlock (Nothing:xs) = isOkayBlock xs

rowBlocks :: [[Maybe Int]] -> [[Maybe Int]]
rowBlocks rs = rs

colBlocks :: [[Maybe Int]] -> [[Maybe Int]]
colBlocks rs = (map reverse . transpose) rs

squareBlocks :: [[Maybe Int]] -> [[Maybe Int]]
squareBlocks rs =  concat [ 
                            [concat ( map (take 3. drop d) (take 3 . drop d' $ rs)) | d <- [0, 3, 6] ]
                            |
                            d' <- [0, 3, 6]
                          ]

blocks :: Sudoku -> [Block]
blocks (Sudoku rs) = (rowBlocks rs) ++ (colBlocks rs) ++ (squareBlocks rs)


isOkay :: Sudoku -> Bool
isOkay sud = and [ isOkayBlock block | block <- blocks sud]


type Pos = (Int,Int)

--Checks for the next blank spot in the sudoku
blank :: Sudoku -> Pos
blank (Sudoku []) = (0,0)
blank (Sudoku a)
    | (isSolved (Sudoku a)) == True = error "Sudoku is already solved!"
    | otherwise = (isBlank a 0)

isBlank :: [[Maybe Int]] -> Int -> Pos
isBlank (x:xs) n
    | (checkBlankInRow x) == True = (n, getBlankLoc x 0)
    | otherwise = isBlank xs (n+1)

--Checks the row to see if a blank exists
checkBlankInRow :: [Maybe Int] -> Bool
checkBlankInRow [] = False
checkBlankInRow (Nothing:xs) = True
checkBlankInRow ((Just x):xs) = checkBlankInRow xs

--Returns the location of the blank within the row
getBlankLoc :: [Maybe Int] -> Int -> Int
getBlankLoc (Nothing:t) n = n
getBlankLoc ((Just h):t) n = getBlankLoc t (n+1)


--Given a list and a tuple containing an index in the list and a new value
--updates the given list with the new value at the given index
(!!=) :: [a] -> (Int,a) -> [a]
(!!=) (h:t) (n,m)
    | fst(n,m) /= 0 = h:(!!=) t ((fst(n,m) - 1),m)
    | otherwise = snd(n,m):t
    

--given a Sudoku, position, and new cell value, update the given sudoku
update :: Sudoku -> Pos -> Maybe Int -> Sudoku
update (Sudoku a) (n,m) (i) = (Sudoku (getRow a (n,m) i))

--Finds the correct row that needs updating, then calls for Column
getRow :: [[Maybe Int]] -> Pos -> Maybe Int -> [[Maybe Int]]
getRow (x:xs) (n,m) (i)
    | (fst(n,m) == 0) && (snd(n,m) == 0) = (i:(tail x)):xs
    | fst(n,m) /= 0 = x : getRow (xs) (n-1,m) (i)
    | snd(n,m) /= 0 = (getCol (x) (m) (i)) : xs
    | otherwise = x:xs

--Finds the correct column that needs updating
getCol :: [Maybe Int] -> Int -> Maybe Int-> [Maybe Int]
getCol (h:t) n i
    | n /= 0 = h : getCol t (n-1) i
    | otherwise = i:t
    

{-solve the sudoku, returning "Nothing" if impossible
--and returning the solved sudoku if it is possible



First guard = given solved sudoku
Second guard = sudoku is full but violates constraints
Third guard = if given a sudoku that is not full
-}
type Choice a = [a]

choose :: [a] -> Choice a
choose xs = xs

solve :: Sudoku -> [Maybe Sudoku]
solve sud
    | (((isSudoku sud)&&(isSolved sud)) && (isOkay sud)) == True = [Just sud]
    | (((isSudoku sud)&&(isSolved sud)) == True) && (isOkay sud == False) = [Nothing]
    | ((isSudoku sud)==True)&&((isSolved sud)==False) =
        do
            let pos = blank sud
            solve (help sud pos False 1)
            
--solveConstraint


--call update, see if it works with new input
--if not, then increment input and call update again until it works
--if it doesn't work for all 9 inputs, return Error "Nothing"
--if it does work, call solve again on the next blank
            

--take a sudoku and a counter
--first guard = n is out of bounds
--second guard = n is out of bounds
--helper :: Sudoku -> Pos -> Bool -> Int -> Sudoku
--helper sud pos check n
--    | (n > 9) || (n < 1) || (check == True) = sud
--    | ((n > 0) && (n < 10)) && (check == False) = 
--        do
--            let newSud = (update sud pos (Just n))
--            helper newSud pos (isOkay newSud) (n+1)
    
        
    --check isOkay = true, then call solve again recursively
help :: Sudoku -> Pos -> Bool -> Sudoku
help sud pos check = do
    x <- choose [Just 1,Just 2,Just 3,Just 4,Just 5,Just 6,Just 7,Just 8,Just 9]
    guard (isOkay (update sud pos (x)) == True)
    return (update sud pos (x))
    
    
    
--testUpdate :: Sudoku -> Pos -> Maybe Int -> Sudoku
--testUpdate sud pos i = update sud pos i


{-Read a sudoku from a file, solve it and print the answer
    If impossible to solve, print "No Solution!"
    Otherwise, print every possible solution-}
readAndSolve :: FilePath -> IO ()
readAndSolve filePath = 
    do  
        sud <- readSudoku filePath
        printSudoku sud
-- eventually printSudoku (solve sud)

example :: Sudoku
example =
    Sudoku
      [ [Just 3, Just 6, Nothing,Nothing,Just 7, Just 1, Just 2, Nothing,Nothing]
      , [Nothing,Just 5, Nothing,Nothing,Nothing,Nothing,Just 1, Just 8, Nothing]
      , [Nothing,Nothing,Just 9, Just 2, Nothing,Just 4, Just 7, Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Just 1, Just 3, Nothing,Just 2, Just 8]
      , [Just 4, Nothing,Nothing,Just 5, Nothing,Just 2, Nothing,Nothing,Just 9]
      , [Just 2, Just 7, Nothing,Just 4, Just 6, Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Just 5, Just 3, Nothing,Just 8, Just 9, Nothing,Nothing]
      , [Nothing,Just 8, Just 3, Nothing,Nothing,Nothing,Nothing,Just 6, Nothing]
      , [Nothing,Nothing,Just 7, Just 6, Just 9, Nothing,Nothing,Just 4, Just 3]
      ]
      
example2 :: Sudoku
example2 =
    Sudoku
      [ [Just 3,Just 7,Just 8,Just 5,Just 4,Just 2,Just 6,Just 9,Just 1]
      , [Just 6,Just 5,Just 1,Just 9,Just 3,Just 7,Just 4,Just 8,Just 2]
      , [Just 4,Just 2,Just 9,Just 6,Just 1,Just 8,Just 5,Just 3,Just 7]
      , [Just 8,Just 9,Just 2,Just 7,Just 5,Just 4,Just 3,Just 1,Just 6]
      , [Just 7,Just 3,Just 5,Just 1,Just 8,Just 6,Just 2,Just 4,Nothing]
      , [Just 1,Just 6,Just 4,Just 3,Just 2,Just 9,Just 8,Just 7,Just 5]
      , [Just 2,Just 1,Just 7,Just 4,Just 6,Just 3,Just 9,Just 5,Nothing]
      , [Just 9,Just 8,Just 3,Just 2,Just 7,Just 5,Just 1,Just 6,Nothing]
      , [Just 5,Just 4,Just 6,Just 8,Just 9,Just 1,Just 7,Just 2,Nothing]
      ]
 --last unfinished sudoku column 127695843
      
badExample :: Sudoku
badExample =
    Sudoku
      [ [Just 3,Just 7,Just 8,Just 5,Just 4,Just 2,Just 6,Just 9,Just 1]
      , [Just 3,Just 7,Just 8,Just 5,Just 4,Just 2,Just 6,Just 9,Just 1]
      , [Just 3,Just 7,Just 8,Just 5,Just 4,Just 2,Just 6,Just 9,Just 1]
      , [Just 8,Just 9,Just 2,Just 7,Just 5,Just 4,Just 3,Just 1,Just 6]
      , [Just 3,Just 7,Just 8,Just 5,Just 4,Just 2,Just 6,Just 9,Just 1]
      , [Just 3,Just 7,Just 8,Just 5,Just 4,Just 2,Just 6,Just 9,Just 1]
      , [Just 3,Just 7,Just 8,Just 5,Just 4,Just 2,Just 6,Just 9,Just 1]
      , [Just 3,Just 7,Just 8,Just 5,Just 4,Just 2,Just 6,Just 9,Just 1]
      , [Just 3,Just 7,Just 8,Just 5,Just 4,Just 2,Just 6,Just 9,Just 1]
      ]
      
      
complete :: Sudoku
complete =
    Sudoku
      [ [Just 3, Just 6, Just 4, Just 8, Just 7, Just 1, Just 2, Just 9, Just 5]
      , [Just 7, Just 5, Just 2, Just 9, Just 3, Just 6, Just 1, Just 8, Just 4]
      , [Just 8, Just 1, Just 9, Just 2, Just 5, Just 4, Just 7, Just 3, Just 6]
      , [Just 5, Just 9, Just 6, Just 7, Just 1, Just 3, Just 4, Just 2, Just 8]
      , [Just 4, Just 3, Just 1, Just 5, Just 8, Just 2, Just 6, Just 7, Just 9]
      , [Just 2, Just 7, Just 8, Just 4, Just 6, Just 9, Just 3, Just 5, Just 1]
      , [Just 6, Just 4, Just 5, Just 3, Just 2, Just 8, Just 9, Just 1, Just 7]
      , [Just 9, Just 8, Just 3, Just 1, Just 4, Just 7, Just 5, Just 6, Just 2]
      , [Just 1, Just 2, Just 7, Just 6, Just 9, Just 5, Just 8, Just 4, Just 3]
      ]
