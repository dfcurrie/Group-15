module Sudoku where

import Data.List
import Data.Maybe
import Data.Char
import Data.List.Split

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
isSudoku (Sudoku []) = False
isSudoku (Sudoku a)  
    | (numRowsIs9 a) && (numColumnsIs9 a) && (applyToRow a) = True
    | otherwise = False

-- numRowsIs9 rows checks if there are 9 rows in the sudoku
numRowsIs9 ::[[Maybe Int]] -> Bool
numRowsIs9 x
    | (length x) == 9 = True
    | otherwise = False

-- numColumnsIs9 columns checks if there are 9 columns in the sudoku
numColumnsIs9 :: [[Maybe Int]] -> Bool
numColumnsIs9 [] = False
numColumnsIs9 (x:xs)
    | (length x) /= 9 = False
    | xs == [] = True
    | otherwise = numColumnsIs9 xs
    
-- Two functions that make sure everything in puzzle is a num between 1-9 or nothing
applyToRow :: [[Maybe Int]] -> Bool
applyToRow [] = True
applyToRow (x:xs) = (checkValidInput x) && (applyToRow xs)
    
checkValidInput :: [Maybe Int] -> Bool
checkValidInput [] = True
checkValidInput (Nothing:xs) = checkValidInput xs
checkValidInput ((Just x):xs)
    | x < 1 = False
    | x > 9 = False 
    | otherwise = checkValidInput xs


-- isSolved sud checks if sud is already solved, i.e. there are no blanks
isSolved :: Sudoku -> Bool
isSolved (Sudoku []) = False
isSolved (Sudoku a)
    | (sudNotBlank a) = True
    | otherwise = False

sudNotBlank :: [[Maybe Int]] -> Bool
sudNotBlank [] = True
sudNotBlank (x:xs) = (checkSolveInput x) && (sudNotBlank xs)

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
printSudoku (Sudoku sud) = 
    do
        putStr (rowsToString sud) 

rowsToString :: [[Maybe Int]] -> String
rowsToString [] = "\n"
rowsToString (x:xs) = (rowToString x) ++ (rowsToString xs) 

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
        let columns = lines str
        printSudoku (Sudoku (makeColumns columns))
        return (Sudoku (makeColumns columns))

makeColumns :: [String] -> [[Maybe Int]]
makeColumns a = map makeRow a

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
    


update :: Sudoku -> Pos -> Maybe Int -> Sudoku
update = undefined
--update (Sudoku a) (n,m) (Just x/Nothing)


solve :: Sudoku -> [Maybe Sudoku]
solve = undefined

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
