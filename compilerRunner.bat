javac -source 1.7 -target 1.7 *.java
IF EXIST Memory.jar DEL /F Memory.jar
jar cfm Memory.jar manifest.txt *.class
echo off
DEL /F *.class
DEL /F *.ctxt
cls
java -jar Memory.jar