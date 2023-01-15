@echo off
REM ############################################
REM # XDO Client Batch Program
REM #############################################

java.exe -cp ^
.\bin;^
.\xdoclient-@build.version@.jar ^
-Dfile.encoding=UTF8 ^
symbolthree.oracle.xdo.XDOAction %*
