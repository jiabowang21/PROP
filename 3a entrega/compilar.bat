@echo off
set SOURCEDIR=FONTS

if /I [%1] == [all] goto :all
if /I [%1] == [run] goto :run
if /I [%1] == [clean] goto :clean
if /I [%1] == [] goto :all

goto :bad

:all
javac -sourcepath %SOURCEDIR% -cp "%SOURCEDIR%\flatlaf-1.6.4.jar;%SOURCEDIR%\flatlaf-intellij-themes-1.6.4.jar;%SOURCEDIR%\forms_rt.jar;" %SOURCEDIR%\SistemaRecomanadorPROP.java
goto :eof

:run
cd %SOURCEDIR%
java -cp "flatlaf-1.6.4.jar;flatlaf-intellij-themes-1.6.4.jar;forms_rt.jar;" SistemaRecomanadorPROP
goto :eof

:clean
del /s /q %SOURCEDIR%\*.class
goto :eof

:bad
echo No rule to target
goto :eof