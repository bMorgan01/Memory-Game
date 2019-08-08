"C:\Program Files\Java\jdk1.8.0_172\bin\javac" *.java
IF EXIST App.jar DEL /F App.jar
"C:\Program Files\Java\jdk1.8.0_172\bin\jar" cfm App.jar manifest.txt *.class
echo off
cls
"C:\Program Files\Java\jdk1.8.0_172\bin\java" -jar App.jar
