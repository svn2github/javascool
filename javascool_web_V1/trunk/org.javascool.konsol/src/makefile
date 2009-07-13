
##########################################################################################################################

SOURCE=$(patsubst %.java,%,$(wildcard [A-Z]*.java))

all :  $(SOURCE).class $(SOURCE).html sujet.html correction.html jtest.html jtest.jar

##########################################################################################################################

%.class : %.java
	javac $^

tst : test.class $(SOURCE).class
	java test
	rm test.java test.class

test.java : makefile
	echo 'import javax.swing.JFrame; public class test { public static void main(String args[]) { JFrame f = new JFrame(); f.setSize(550, 500); f.getContentPane().add($(SOURCE).getPanel()); f.pack(); f.setVisible(true); $(SOURCE).test(); }}' >$@

jtest.html : makefile
	echo '<html><body><applet archive="jtest.jar" code="jtest.class" width="550" height="500"></applet></body></html>' > $@

jtest.jar : $(SOURCE).java makefile
	$(MAKE) $(SOURCE).class jtest.class
	rm -f $@ ; jar cfM $@ $(SOURCE)*.class jtest*.class
	rm jtest.java *.class

jtest.java : makefile
	echo 'import javax.swing.*; import java.awt.*; import java.awt.event.*; public class jtest extends JApplet { public void init() { getContentPane().setLayout(new BorderLayout()); JButton t = new JButton("test"); getContentPane().add(t, BorderLayout.NORTH); t.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { $(SOURCE).test(); }}); getContentPane().add($(SOURCE).getPanel()); }}' > $@

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
