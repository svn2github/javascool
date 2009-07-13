#
# Builds sandbox files
# 

PROGLETS = konsol dicho smiley

build : clean
	for p in $(PROGLETS) ; do pushd ../org.javascool.$$p/src > /dev/null ;\
	 $(MAKE) -f ../../sandbox/makefile.inc ;\
	 for m in sujet correction ; do mv $$m.html ../../sandbox/$$p-$$m.html ; done ; mv *.class *.html ../../sandbox ; cp *.png ../../sandbox 2> /dev/null ; ok=1 ;\
	popd  > /dev/null ; done
	$(MAKE) index.html

#

index.html : applets makefile
	echo '<html><body><h2>Proglets disponibles</h2><table border="1" width="80%" align="center">' > $@	
	for f in $(patsubst %.html,%,$(wildcard [A-Z]*.html)) ; do echo "<tr><td valign='middle' width='75%'><h4>$$f</h4></td><td valign='middle' align='center'><h4><a href='applet$$f.html'>TEST</a></h4></td><td valign='middle' align='center'><h4><a href='$$f.html'>DOC</a></h4></td></tr>" ; done >> $@
	echo '</table></body></html>' >> $@

#

applets :
	for f in `ls [ABCDEFGHIJKLMNOPQRSTUVWXYZ]*.html | sed 's/.html//'` ; do $(MAKE) applet$$f.html applet$$f.class ; done


applet%.html : %.html makefile
	echo '<html><body><applet code="applet$*.class" width="550" height="500"></applet></body></html>' > $@

%.class : %.java
	javac $^

applet%.java : %.html makefile
	echo 'import javax.swing.*; import java.awt.*; import java.awt.event.*; public class applet$* extends JApplet { public void init() { getContentPane().setLayout(new BorderLayout()); JButton t = new JButton("test"); getContentPane().add(t, BorderLayout.NORTH); t.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { $*.test(); }}); getContentPane().add($*.getPanel(this)); }}' > $@

#

clean :
	rm -f *.class *.html *.png ; for p in $(PROGLETS) ; do $(MAKE) -C ../org.javascool.$$p/src -f ../../sandbox/makefile.inc clean ; done

#
# Run Hamdi classes
#

run :
	cd ../LyceeGamlet/bin ; java compiler/AppletFrame

# -> javac -nowarn -classpath /usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0/lib/tools.jar -d bin LyceeGamlet/src/compiler/*.java


