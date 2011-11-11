:: Setup:
    :: 
    :: 1. Change the constants to match your paths
    :: 2. Put this clj.bat file on your PATH
    ::
    :: Usage:
    ::
    :: clj                           # Starts REPL
    :: clj my_script.clj             # Runs the script
    :: clj my_script.clj arg1 arg2   # Runs the script with arguments
 
    @echo off

    :: Change the following to match your paths
    set CLOJURE_DIR=D:\Development\Clojure
    set CLOJURE_JAR=%CLOJURE_DIR%\clojure-1.3.0\clojure-1.3.0.jar
    set CONTRIB_JAR=%CLOJURE_DIR%\clojure-contrib-1.2.0\clojure-contrib-1.2.0.jar
    set JLINE_JAR=%CLOJURE_DIR%\jline-1.0\jline-1.0.jar
 
    if (%1) == () (
        :: Start REPL
        java -cp ..\src;.;%JLINE_JAR%;%CLOJURE_JAR%;%CONTRIB_JAR% jline.ConsoleRunner clojure.main
    ) else (
        :: Start some_script.clj
        java -cp ..\src;.;%CLOJURE_JAR%;%CONTRIB_JAR% clojure.main %1 -- %*
    )