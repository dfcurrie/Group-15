module Paths_Sudoku (
    version,
    getBinDir, getLibDir, getDataDir, getLibexecDir,
    getDataFileName, getSysconfDir
  ) where

import qualified Control.Exception as Exception
import Data.Version (Version(..))
import System.Environment (getEnv)
import Prelude

catchIO :: IO a -> (Exception.IOException -> IO a) -> IO a
catchIO = Exception.catch


version :: Version
version = Version {versionBranch = [0,1,0,0], versionTags = []}
bindir, libdir, datadir, libexecdir, sysconfdir :: FilePath

bindir     = "/home/grads/pkumar/Desktop/Cpsc_449_haskell2/group-15/haskell_sudoku/.cabal-sandbox/bin"
libdir     = "/home/grads/pkumar/Desktop/Cpsc_449_haskell2/group-15/haskell_sudoku/.cabal-sandbox/lib/x86_64-linux-ghc-7.8.4/Sudoku-0.1.0.0"
datadir    = "/home/grads/pkumar/Desktop/Cpsc_449_haskell2/group-15/haskell_sudoku/.cabal-sandbox/share/x86_64-linux-ghc-7.8.4/Sudoku-0.1.0.0"
libexecdir = "/home/grads/pkumar/Desktop/Cpsc_449_haskell2/group-15/haskell_sudoku/.cabal-sandbox/libexec"
sysconfdir = "/home/grads/pkumar/Desktop/Cpsc_449_haskell2/group-15/haskell_sudoku/.cabal-sandbox/etc"

getBinDir, getLibDir, getDataDir, getLibexecDir, getSysconfDir :: IO FilePath
getBinDir = catchIO (getEnv "Sudoku_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "Sudoku_libdir") (\_ -> return libdir)
getDataDir = catchIO (getEnv "Sudoku_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "Sudoku_libexecdir") (\_ -> return libexecdir)
getSysconfDir = catchIO (getEnv "Sudoku_sysconfdir") (\_ -> return sysconfdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "/" ++ name)
