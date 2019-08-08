javac *.java
IF EXIST App.jar DEL /F App.jar
jar cfm App.jar manifest.txt *.class
java -jar App.jar
