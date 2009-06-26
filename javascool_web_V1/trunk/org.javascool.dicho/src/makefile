
all : doc
	javac *.java	
	java Dicho

doc :  Dicho.html sujet.html correction.html

##########################################################################################################################

XML2XML = /usr/java/jdk1.5.0_13/bin/java -classpath /usr/java/saxon6-5-5/saxon.jar com.icl.saxon.StyleSheet

%.html : %.java
	javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d doc $*.java
	cp doc/$*.html .
	rm -rf doc

%.html : %.xml
	$(XML2XML) -o $@ $^ ../../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_$*.xsl

clean :
	rm -rf *.class doc

flags :
	$(XML2XML) -o flags.txt flags.xml flags.xsl

##########################################################################################################################
