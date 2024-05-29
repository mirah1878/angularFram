set tomcatPath="D:\Eriq_RohWeltall\Hub\apache-tomcat-10.1.15\webapps"

dir src /a-d /b /s *.java > sources.txt
javac -cp .;lib/* -d bin @sources.txt
del sources.txt

cd bin
jar -cvf ../web/WEB-INF/lib/[projectNameMaj].jar *

xcopy "*" "../web/WEB-INF/classes" /E /Y /I

cd ../web
jar -cvf "%tomcatPath%/[projectNameMaj].war" *