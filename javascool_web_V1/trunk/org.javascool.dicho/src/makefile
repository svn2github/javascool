all :
	#javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d doc Dicho.java
	javac Dicho.java 
	java Dicho

clean :
	rm -rf *.class doc

flags :
	/usr/java/jdk1.5.0_13/bin/java -classpath /usr/java/saxon6-5-5/saxon.jar com.icl.saxon.StyleSheet -o flags.txt flags.xml flags.xsl

