
all :
	javac Dicho.java	
	java Dicho

doc :  Dicho.html

%.html : %.java
	javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d doc $*.java
	cp doc/$*.html .
	rm -rf doc

clean :
	rm -rf *.class doc

flags :
	/usr/java/jdk1.5.0_13/bin/java -classpath /usr/java/saxon6-5-5/saxon.jar com.icl.saxon.StyleSheet -o flags.txt flags.xml flags.xsl

