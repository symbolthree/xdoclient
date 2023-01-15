@echo off
REM ############################################
REM # XDO Client Batch File
REM #############################################
SETLOCAL
set HOME_DIR=%~dp0
cd %HOME_DIR%

start javaw.exe -cp ^
.\bin;^
.\xdoclient-@build.version@.jar ^
-splash:splash.gif ^
-Dfile.encoding=UTF8 ^
symbolthree.oracle.xdo.XDOClient %*

