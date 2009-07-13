#
# Compile proglet's files
# 

SOURCE=$(patsubst %.java,%,$(wildcard [A-Z]*.java))

ALL = $(SOURCE).class $(SOURCE).html sujet.html correction.html

all : $(ALL)

%.class : %.java
	javac $^

%.html : %.java
	javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d doc $*.java | grep -v 'Creating destination directory' ; ok=1
	cp doc/$*.html .
	rm -rf doc

XML2XML = /usr/java/jdk1.5.0_13/bin/java -classpath /usr/java/saxon6-5-5/saxon.jar com.icl.saxon.StyleSheet

%.html : %.xml
	$(XML2XML) -o $@ $^ ../../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_$*.xsl

#

test : test.class $(SOURCE).class
	java test
	rm test.java test.class

test.java : makefile
	echo 'import javax.swing.JFrame; public class test { public static void main(String args[]) { JFrame f = new JFrame(); f.setSize(550, 500); f.getContentPane().add($(SOURCE).getPanel(null)); f.pack(); f.setVisible(true); $(SOURCE).test(); }}' >$@

#

clean :
	rm -f *.class $(ALL)