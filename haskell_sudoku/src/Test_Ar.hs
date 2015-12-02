module Main where
import Sudoku
import Data.Maybe
import Data.List 
import System.Console.ANSI
import System.IO


check f arg expectation points text
  | expectation == (f arg) = points ++ "\tpassed! " ++ text
  | otherwise              = "0\tfailed!( !!!! original marks = " ++ points ++ " !!!!)"  ++ text ++ ". Expected <" ++ (show expectation)
                             ++ " > got  < " ++ (show (f arg))

--------------------------------------------------------------------------------------

checkNoArg f expectation points text
  | expectation == f       = points ++ "\tpassed! " ++ text
  | otherwise              = "0\tfailed! " ++ text ++ ". Expected <" ++ (show expectation)
                             ++ " > got  < " ++ (show f)


checkAllBlank = checkNoArg allBlankSudoku allBlank "2" "list of list of Nothings"




allBlank :: Sudoku
allBlank =
    Sudoku
      [ [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      ]

--------------------------------------------------------------------------------------
problemInRow1 :: Sudoku
problemInRow1 =
    Sudoku
      [ [Nothing,Nothing,Just 5 ,Just 5,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      ]

problemInRow2 :: Sudoku
problemInRow2 =
    Sudoku
      [ [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Just 1 ,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Just 2 ]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      ]

problemInCol1 :: Sudoku
problemInCol1 =
    Sudoku
      [ [Just 1 ,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Just 10,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      ]

problemInCol2 :: Sudoku
problemInCol2 =
    Sudoku
      [ [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Just 6 ,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Just 16,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      ]

impossible :: Sudoku
impossible =
    Sudoku
      [ [Just 4, Just 6, Just 4, Just 8, Just 7, Just 1, Just 2, Just 9, Just 5]
      , [Just 7, Just 5, Just 2, Just 9, Just 3, Just 6, Just 1, Just 8, Just 4]
      , [Just 8, Just 1, Just 9, Just 2, Just 5, Just 4, Just 7, Just 3, Just 6]
      , [Just 5, Just 9, Just 6, Just 7, Just 1, Just 3, Just 4, Just 2, Just 8]
      , [Just 4, Just 3, Just 1, Just 5, Just 8, Just 2, Just 6, Just 7, Just 9]
      , [Just 2, Just 7, Just 8, Just 4, Just 6, Just 9, Just 3, Just 5, Just 1]
      , [Just 6, Just 4, Just 5, Just 3, Just 2, Just 8, Just 9, Just 1, Just 7]
      , [Just 9, Just 8, Just 3, Just 1, Just 4, Just 7, Just 5, Just 6, Just 2]
      , [Just 1, Just 2, Just 7, Just 6, Just 9, Just 5, Just 8, Just 4, Just 3]
      ]

checkIsSudoku1 = check isSudoku example True "0.5" "The example is a Sudoku"
checkIsSudoku2 = check isSudoku allBlank True "0.5" "The all blank is a Sudoku"
checkIsSudoku3 = check isSudoku impossible False "1" "Should not have repeated numbers"
checkIsSudoku7 = check isSudoku problemInRow1 False "1" "The should have exactly 9 rows is a Sudoku"
checkIsSudoku4 = check isSudoku problemInRow2 False "0.5" "The should have exactly 9 rows is a Sudoku"
checkIsSudoku5 = check isSudoku problemInCol1 False "1" "The should have exactly 9 columns is a Sudoku"
checkIsSudoku6 = check isSudoku problemInCol2 False "0.5" "The should have exactly 9 columns is a Sudoku"

checkIsSudoku = [checkIsSudoku1,checkIsSudoku2,checkIsSudoku3,checkIsSudoku4,checkIsSudoku5,checkIsSudoku6]
--------------------------------------------------------------------------------------
oneBlank :: Sudoku
oneBlank =
    Sudoku
      [ [Nothing, Just 6, Just 4, Just 8, Just 7, Just 1, Just 2, Just 9, Just 5]
      , [Just 7, Just 5, Just 2, Just 9, Just 3, Just 6, Just 1, Just 8, Just 4]
      , [Just 8, Just 1, Just 9, Just 2, Just 5, Just 4, Just 7, Just 3, Just 6]
      , [Just 5, Just 9, Just 6, Just 7, Just 1, Just 3, Just 4, Just 2, Just 8]
      , [Just 4, Just 3, Just 1, Just 5, Just 8, Just 2, Just 6, Just 7, Just 9]
      , [Just 2, Just 7, Just 8, Just 4, Just 6, Just 9, Just 3, Just 5, Just 1]
      , [Just 6, Just 4, Just 5, Just 3, Just 2, Just 8, Just 9, Just 1, Just 7]
      , [Just 9, Just 8, Just 3, Just 1, Just 4, Just 7, Just 5, Just 6, Just 2]
      , [Just 1, Just 2, Just 7, Just 6, Just 9, Just 5, Just 8, Just 4, Just 3]
      ]
solved :: Sudoku
solved =
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

checkIsSolved1 = check isSolved allBlank False "1" "all blank is not solved"
checkIsSolved2 = check isSolved allBlank False "1" "example is not solved"
checkIsSolved3 = check isSolved impossible True "1" "impossible is solved"
checkIsSolved4 = check isSolved solved True "2" "solved is solved"
checkIsSolved = [checkIsSolved1,checkIsSolved2,checkIsSolved3,checkIsSolved4]
--------------------------------------------------------------------------------------
{-
-- Test printSudoku manually
-- each of the following is worth 5 points
-- if the function is printing sudoku with rows and columns revered, they should get 3 out of 5
-- printSudoku allBlank
-- printSudoku oneBlank
--------------------------------------------------------------------------------------
-- check readSoduku manually
-- readSudoku "invalid"
   --> if they return any sort of custom made error or exception  -> 3
   --> if the the custom error or message is "Not a Sudoku!"      -> 2
-- readSudoku "valid sudoku"
   --> if they return a value of type Sudoku  -> 3
   --> if the value is the correct value      -> 2

-}
--------------------------------------------------------------------------------------
firstBlank :: Sudoku
firstBlank =
    Sudoku
      [ [Nothing, Just 6, Just 4, Just 8, Just 7, Just 1, Just 2, Just 9, Just 5]
      , [Just 7, Just 5, Just 2, Just 9, Just 3, Just 6, Just 1, Just 8, Just 4]
      , [Just 8, Just 1, Just 9, Just 2, Just 5, Just 4, Just 7, Just 3, Just 6]
      , [Just 5, Just 9, Just 6, Just 7, Just 1, Just 3, Just 4, Just 2, Just 8]
      , [Just 4, Just 3, Just 1, Just 5, Just 8, Just 2, Just 6, Just 7, Just 9]
      , [Just 2, Just 7, Just 8, Just 4, Just 6, Just 9, Just 3, Just 5, Just 1]
      , [Just 6, Just 4, Just 5, Just 3, Just 2, Just 8, Just 9, Just 1, Just 7]
      , [Just 9, Just 8, Just 3, Just 1, Just 4, Just 7, Just 5, Just 6, Just 2]
      , [Just 1, Just 2, Just 7, Just 6, Just 9, Just 5, Just 8, Just 4, Just 3]
      ]
oneBlankRC :: Sudoku
oneBlankRC =
    Sudoku
      [ [Just 3, Nothing, Just 4, Just 8, Just 7, Just 1, Just 2, Just 9, Just 5]
      , [Just 7, Just 5, Just 2, Just 9, Just 3, Just 6, Just 1, Just 8, Just 4]
      , [Just 8, Just 1, Just 9, Just 2, Just 5, Just 4, Just 7, Just 3, Just 6]
      , [Just 5, Just 9, Just 6, Just 7, Just 1, Just 3, Just 4, Just 2, Just 8]
      , [Just 4, Just 3, Just 1, Just 5, Just 8, Just 2, Just 6, Just 7, Just 9]
      , [Just 2, Just 7, Just 8, Just 4, Just 6, Just 9, Just 3, Just 5, Just 1]
      , [Just 6, Just 4, Just 5, Just 3, Just 2, Just 8, Just 9, Just 1, Just 7]
      , [Just 9, Just 8, Just 3, Just 1, Just 4, Just 7, Just 5, Just 6, Just 2]
      , [Just 1, Just 2, Just 7, Just 6, Just 9, Just 5, Just 8, Just 4, Just 3]
      ]
twoBlanks :: Sudoku
twoBlanks =
    Sudoku
      [ [Nothing, Just 6, Just 4, Just 8, Just 7, Just 1, Just 2, Just 9, Just 5]
      , [Just 7, Just 5, Just 2, Just 9, Just 3, Just 6, Just 1, Just 8, Just 4]
      , [Just 8, Just 1, Just 9, Just 2, Just 5, Just 4, Just 7, Just 3, Just 6]
      , [Just 5, Just 9, Just 6, Just 7, Just 1, Just 3, Just 4, Just 2, Just 8]
      , [Just 4, Just 3, Just 1, Just 5, Just 8, Just 2, Just 6, Just 7, Just 9]
      , [Just 2, Just 7, Just 8, Just 4, Just 6, Just 9, Just 3, Just 5, Just 1]
      , [Just 6, Just 4, Just 5, Just 3, Just 2, Just 8, Just 9, Just 1, Just 7]
      , [Just 9, Just 8, Just 3, Just 1, Just 4, Just 7, Just 5, Just 6, Just 2]
      , [Just 1, Just 2, Just 7, Just 6, Just 9, Just 5, Just 8, Just 4, Nothing]
      ]

removeones :: Sudoku
removeones =
    Sudoku
      [ [Just 3 , Just 6, Just 4, Just 8, Just 7, Nothing, Just 2, Just 9, Just 5]
      , [Just 7, Just 5, Just 2, Just 9, Just 3, Just 6, Nothing, Just 8, Just 4]
      , [Just 8, Nothing, Just 9, Just 2, Just 5, Just 4, Just 7, Just 3, Just 6]
      , [Just 5, Just 9, Just 6, Just 7, Nothing, Just 3, Just 4, Just 2, Just 8]
      , [Just 4, Just 3, Nothing, Just 5, Just 8, Just 2, Just 6, Just 7, Just 9]
      , [Just 2, Just 7, Just 8, Just 4, Just 6, Just 9, Just 3, Just 5, Nothing]
      , [Just 6, Just 4, Just 5, Just 3, Just 2, Just 8, Just 9, Nothing, Just 7]
      , [Just 9, Just 8, Just 3, Nothing, Just 4, Just 7, Just 5, Just 6, Just 2]
      , [Nothing, Just 2, Just 7, Just 6, Just 9, Just 5, Just 8, Just 4, Just 3]
      ]




checkBlank1    = check blank oneBlankRC (0, 1) "3" "One blank. You should pass either this or the next. Checks for correct identification of rows and columns"
checkBlank2    = check blank oneBlank (1, 0) "1" "One blank. read the previous description"
checkBlank3    = check blank twoBlanks (0, 0) "2" "Two blanks. You should pass either this one or the next"
checkBlank4    = check blank twoBlanks (8, 8) "2" "Two blanks. read the previous description"
checkBlank     = [checkBlank1,checkBlank2,checkBlank3,checkBlank4] 
--------------------------------------------------------------------------------------
checkReplace1  = check (uncurry (!!=)) (["a","b","c","d"], (1, "apa"))  ["a","apa","c","d"] "2" "original example!"
checkReplace2  = check (uncurry (!!=)) (["a"], (0, "apa"))  ["apa"] "2" "edge case"
checkReplace3  = check (uncurry (!!=)) ([Just 6, Nothing], (1, Just 3))  [Just 6, Just 3] "1" "another edge case"
checkReplace   = [checkReplace1,checkReplace2,checkReplace3]
--------------------------------------------------------------------------------------
firstFilled :: Sudoku
firstFilled   =
    Sudoku
      [ [Nothing,Just 2,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      , [Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing,Nothing]
      ]

uncurry3 f    = \(a,b,c) -> f a b c
checkUpdate1   = check (uncurry3 update) (allBlank,(0,1),(Just 2)) firstFilled "5" "original example!"
checkUpdate2   = check (uncurry3 update) (solved,(0,0),(Just 3)) solved "5" "testing to see if you keep the rest of sudoku unchanged!"
checkUpdate    = [checkUpdate1,checkUpdate2] 

checkEverything = checkUpdate ++ checkReplace ++ checkBlank ++ checkIsSolved ++ checkIsSudoku ++ [checkAllBlank]
--------------------------------------------------------------------------------------
-- I am working on writing the test function for solve
-- checkSolve = check $ solve
--------------------------------------------------------------------------------------

checkSolve _ _ _ _ [] = True                                                
checkSolve orig history limit count (x:xs)                                      
        | limit == count = True                                                      
        | otherwise      = (isSudoku1 x &&
                           isSolved1 x &&
                           matchesOrig orig x  &&
                           notDup history x ) &&
                           checkSolve orig (x:history) limit (count+1) xs

isSudoku1 (Sudoku rs) = length rs == 9 && and (map (\r -> length r == 9) rs)   

isSolved1 (Sudoku rs) = and (map allJusts rs)

allJusts [] = True
allJusts ((Just a):xs) = allJusts xs                                            
allJusts (Nothing:xs) = False            

matchesOrig (Sudoku orig) (Sudoku sol) = and [matchRow (orig !! i) (sol !! i)| i <- [0..8] ]

notDup [] _ = True                                                               
notDup (x:xs) sol = x /= sol && notDup xs sol 


matchRow [] [] = True                                                           
matchRow ((Just a):xs) ((Just b):ys) = a == b && matchRow xs ys                 
matchRow ((Nothing):xs) _ = True       

-- checking a few simple sudokus
--checkSudList = [firstBlank,removeones]

chsols1 = (map fromJust (solve  removeones))
chsols2 = (map fromJust (solve  firstBlank))

simpleTest :: String 
simpleTest   
   | bool1 == True = "Successfully tested  for very simple Sudokus"
   | otherwise     = "Program not solving simple sudokus"
   where 
    bool1 = chsols2 == chsols1 

-- This function generates multiple solutions 
-- and checks whether they are unique or not
checkMulSolutions :: Sudoku -> String -> String
checkMulSolutions sud str
     | checkSolve sud [] 20 0 (map fromJust (solve  sud)) == True = "Checked successfully for " ++ str ++ ": multiple solutions are unique\n"
     | otherwise                                                  = "Failed :for " ++ str ++ "  Duplicate solutions for the sudoku \n"

checkmulsols1 = checkMulSolutions allBlankSudoku "allBlankSudoku"
checkmulsols2 = checkMulSolutions example "example"

checkmulsols_final = checkmulsols1 ++ checkmulsols2

--checksols_allBlank = checkSolve allBlankSudoku [] 10 0 (map fromJust (solve  allBlankSudoku))
--checksols_example  =  checkSolve example [] 10 0 (map fromJust (solve example))


tests :: [String]
tests = checkEverything

printLines :: Color -> IO ()
printLines color  = do
            hSetSGR stdout [(SetColor Foreground Vivid Blue),(SetColor Background Dull Yellow)]
            putStrLn $ concat $  replicate 3  divider
            hSetSGR stdout [(SetColor Foreground Dull color),(SetColor Background Dull White)]
      where 
        divider = "//////////////////////////////////////////////////////////////////////////////////\n"

printStar :: Int ->  IO ()
printStar n = do
  putStrLn stars
        where 
          stars = concat $ replicate n "*************************************\n"

main :: IO ()
main = do
   printLines Black
   putStrLn "Total Marks =  34" 
   printStar 2 
   mapM_ putStrLn checkEverything
   printLines Red
   putStrLn "Checking printSudoku Function"
   printStar 2
   putStrLn "Total Marks =  10" 
   printStar 2 
   printSudoku allBlank
   printStar 2 
   putStrLn "Sudoku to be printed"
   printStar 1 
   putStrLn $ show oneBlank 
   printStar 1
   putStrLn "Printed Sudoku"
   printStar 1 
   printSudoku oneBlank 
   printStar 1    
   printLines Red 
   putStrLn "Total Marks =  15" 
   printStar 2 
   putStrLn "checking solve function (for multiple solutions) "   
   putStrLn checkmulsols_final
   printLines Black
   putStrLn "Total Marks =  6" 
   printStar 2 
   putStrLn "checking solve function (for simple sudokus) "  
   putStrLn simpleTest
   printLines Black
   putStrLn "Checking readSudoku Function"
   printStar 2
   putStrLn "Total Marks =  10" 
   printStar 2 
   right <- readSudoku "example.sud"
   putStrLn "Expecting a properly printed sudoku(as this is a valid sudoku)"
   printStar 1 
   printSudoku right
   printStar 1
   putStrLn "Expecting an error or exception"
   wrong <- readSudoku "example2.sud"
   printStar 1 
   printSudoku wrong 







