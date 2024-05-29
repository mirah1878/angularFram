@echo off
setlocal

set SRC_DIR=src
set WEB_INF_CLASS_DIR=web\WEB-INF\classes
set LIB_DIR=lib
set WEB_DIR=web
set WAR_FILE=web.war
set JAVA_FILES=%SRC_DIR%\*.java

echo Compiling Java files...

:: Construit une chaîne de chemin pour les fichiers .jar dans le dossier lib
set CLASSPATH=%LIB_DIR%\*;

:: Assurez-vous que JAVA_HOME pointe vers JDK 8
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_91\bin

:: Utilisez javac de JDK 8 pour la compilation

for /R %SRC_DIR% %%f in (*.java) do (
    echo Compiling %%f
    javac -d %WEB_INF_CLASS_DIR% -sourcepath %SRC_DIR% -classpath %CLASSPATH% "%%f"
)

echo Compilation complete.

:: Copie les fichiers .java dans le dossier class de WEB-INF
echo Copying .java files to %WEB_INF_CLASS_DIR%...
xcopy /s /y "%SRC_DIR%\*.java" "%WEB_INF_CLASS_DIR%\"

echo Copy complete.

:: Création du fichier .war
echo Creating WAR file...
jar cvf %WAR_FILE% -C %WEB_DIR% .

echo WAR file created successfully.

endlocal

pause
