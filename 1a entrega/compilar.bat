@echo off
set SOURCEDIR=FONTS

if /I %1 == CsvUtilsDriver goto :CsvUtilsDriver
if /I %1 == FileIODriver goto :FileIODriver
if /I %1 == CtrlPersistenciaDriver goto :CtrlPersistenciaDriver
if /I %1 == DistanciaDriver goto :DistanciaDriver
if /I %1 == AtributDriver goto :AtributDriver
if /I %1 == CtrlItemsDriver goto :CtrlItemsDriver
if /I %1 == ItemDriver goto :ItemDriver
if /I %1 == TipusItemDriver goto :TipusItemDriver
if /I %1 == ConjuntRecomanacionsDriver goto :ConjuntRecomanacionsDriver
if /I %1 == CtrlRecomanacioDriver goto :CtrlRecomanacioDriver
if /I %1 == RecomanacioDriver goto :RecomanacioDriver
if /I %1 == SistemaRecomanadorDriver goto :CtrlItemsDriver
if /I %1 == CtrlUsuariDriver goto :CtrlUsuariDriver
if /I %1 == CtrlValoracioDriver goto :CtrlItemsDriver
if /I %1 == ValoracioDriver goto :CtrlItemsDriver
if /I %1 == clean goto :clean

goto :bad

:CsvUtilsDriver
javac -sourcepath %SOURCEDIR% .\FONTS\utils\CsvUtilsDriver.java
goto :eof

:FileIODriver
javac -sourcepath %SOURCEDIR% .\FONTS\persistencia\FileIODriver.java
goto :eof

:CtrlPersistenciaDriver
javac -sourcepath %SOURCEDIR% .\FONTS\persistencia\CtrlPersistenciaDriver.java
goto :eof

:DistanciaDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\distancia\DistanciaDriver.java
goto :eof

:AtributDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\item\atributs\AtributDriver.java
goto :eof

:CtrlItemsDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\item\CtrlItemsDriver.java
goto :eof

:ItemDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\item\ItemDriver.java
goto :eof

:TipusItemDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\item\TipusItemDriver.java
goto :eof

:ConjuntRecomanacionsDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\recomanacio\ConjuntRecomanacionsDriver.java
goto :eof

:CtrlRecomanacioDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\recomanacio\CtrlRecomanacioDriver.java
goto :eof

:RecomanacioDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\recomanacio\RecomanacioDriver.java
goto :eof

:SistemaRecomanadorDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\sistemarecomanador\SistemaRecomanadorDriver.java
goto :eof

:CtrlUsuariDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\usuari\CtrlUsuariDriver.java
goto :eof

:CtrlValoracioDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\valoracio\CtrlValoracioDriver.java
goto :eof

:ValoracioDriver
javac -sourcepath %SOURCEDIR% .\FONTS\domini\valoracio\ValoracioDriver.java
goto :eof

:clean
del /s /q *.class
goto :eof

:bad
echo No rule to target
goto :eof