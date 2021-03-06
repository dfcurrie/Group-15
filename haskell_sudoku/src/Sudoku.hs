module Sudoku where

import Data.List
import Data.Maybe
import Data.Char
import Data.List.Split
import Control.Monad

-------------------------------------------------------------------------

data Sudoku = Sudoku [[Maybe Int]]
 deriving ( Eq )

instance Show Sudoku where
 show (Sudoku ls) = show [ [ f m | m <- row] | row <- ls]
    where
      f m = fromMaybe "_" $ fmap (show) m

rows :: Sudoku -> [[Maybe Int]]
rows (Sudoku rs) = rs

-- allBlankSudoku is a sudoku with just blanks
allBlankSudoku :: Sudoku
allBlankSudoku = Sudoku (replicate 9 (replicate 9 Nothing))

-- isSudoku sud checks if sud is really a valid representation of a sudoku
-- puzzle. (Is 9 by 9)
isSudoku :: Sudoku -> Bool
isSudoku sud  = (lengthNine grid) && 
                    (and (map lengthNine grid)) && 
                        (and (map (all isSudokuValue) grid)) 
    where
        grid = rows sud
        
--Checks that the length given is 9.
lengthNine :: [a] -> Bool
lengthNine list
    | (length list) == 9 = True
    | otherwise = False

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

--Checks if the value given is Nothing, i.e. a blank
notBlank :: Maybe Int -> Bool
notBlank Nothing = False
notBlank _ = True
-------------------------------------------------------------------------

-- printSudoku sud prints a representation of the sudoku sud on the screen
printSudoku :: Sudoku -> IO ()
printSudoku sud = 
    do
        putStr (sudToString (rows sud)) 

{-sudToString and rowToString work together to change a given sudoku
into a printable version, changing Just values to the value as is
and changing Nothing values into .-}
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
        if not(isSudoku sud)
            then ioError (userError "Not a Sudoku!")
            else return sud
        return sud

{-Essentially does the reverse of sudToString and rowToString
by converting a visually appealling sudoku into a readable
sudoku -}
makeSud :: [String] -> [[Maybe Int]]
makeSud a = map makeRow a

makeRow :: String -> [Maybe Int]
makeRow a = map charToMaybeInt a

charToMaybeInt :: Char -> Maybe Int
charToMaybeInt '.' = Nothing
charToMaybeInt a 
    | isHexDigit a = Just (digitToInt a)
    | otherwise = Just 0


-------------------------------------------------------------------------

--isOkay checks every row/column/block for any repeats in numbers.
--The rest of these functions are helper functions
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
   

--Solve every possibility of the given sudoku
solve :: Sudoku -> [Maybe Sudoku]
solve sud 
        |(isSudoku sud == False) = [Nothing]
        | solved == [] = [Nothing]
        | otherwise = solved
            where solved = recurseSolve sud []

{-checks to see if the given sudoku is solved
returns a list of solved sudokus if it is,
otherwise, continues trying to solve-}
recurseSolve :: Sudoku -> [Maybe Sudoku] -> [Maybe Sudoku]
recurseSolve sud suds
    | not (isOkay sud) = suds
    | isSolved sud = (Just sud):suds
    | otherwise = allNum 1 sud

--try adding in 1-9 recursively into the sudoku and checking if it works
allNum :: Int -> Sudoku -> [Maybe Sudoku]
allNum 10 _ = []
allNum x sud = (recurseSolve (update sud (blank sud) (Just x)) []) ++ allNum (x+1) sud


--Print the first sudoku in the list of solved sudokus and then
--"All Donsies" or print "No solution" if no solution
printAll :: [Maybe Sudoku] -> IO()
printAll [Nothing] = putStrLn "No solution!"
printAll (Nothing:xs) = printAll xs
printAll [(Just x)] = do 
                            printSudoku x 
                            putStrLn "All Donesies"
printAll ((Just x):xs) = do 
                            printSudoku x 
                            printAll xs


{-Read a sudoku from a file, solve it and print the answer
    If impossible to solve, print "No Solution!"
    Otherwise, print every possible solution-}
readAndSolve :: FilePath -> IO ()
readAndSolve filePath = 
    do  
        sud <- readSudoku filePath
        printAll (solve sud)

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