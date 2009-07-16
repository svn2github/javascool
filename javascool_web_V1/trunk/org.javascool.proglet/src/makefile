
PROGLETS = Konsol Dicho Smiley Scope
PROGLET = $(lastword $(PROGLETS))

test : build
#	cd .. ; java Proglet $(PROGLET)
#	cd .. ; appletviewer applet$(PROGLET).html 
#	cd .. ; firefox index.html
	cd .. ; firefox http://www.loria.fr/~vthierry/proglet/index.html

build :
	echo 'javac ..' ; javac *.java ; mv *.class ..
	echo 'jadoc ..' ; javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d ../api *.java
	echo 'index ..' ; $(MAKE) ../index.html
	echo 'rsunc ..' ; rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ ../* loria.loria.fr:/local/web-homepages/vthierry/proglet

clean :
	rm -rf *~ ../*.class ../api ../*.html

#

../index.html : $(patsubst %,../applet%.html,$(PROGLETS))	
	echo '<html><body><h2>Proglets disponibles</h2><table border="1" width="80%" align="center">' > $@	
	for f in $(PROGLETS) ; do echo "<tr><td valign='middle' width='75%'><h4>$$f</h4></td><td valign='middle' align='center'><h4><a href='applet$$f.html'>TEST</a></h4></td><td valign='middle' align='center'><h4><a href='api/$$f.html'>DOC</a></h4></td></tr>" ; done >> $@
	echo '</table></body></html>' >> $@

../applet%.html : makefile
	echo '<html><body><applet code="Proglet$$Test.class" width="550" height="550"><param name="proglet" value="$*"/></applet></body></html>' > $@

#

XML2XML = /usr/java/jdk1.5.0_13/bin/java -classpath /usr/java/saxon6-5-5/saxon.jar com.icl.saxon.StyleSheet

../sujet-%.html : ../doc/sujet-%.xml
	$(XML2XML) -o $@ $^ ../../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_sujet.xsl

../correction-%.html : ../doc/correction-%.xml
	$(XML2XML) -o $@ $^ ../../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_correction.xsl


#
# Run Hamdi classes
#

run :
	cd ../../LyceeGamlet/bin ; java compiler/AppletFrame

# -> javac -nowarn -classpath /usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0/lib/tools.jar -d bin LyceeGamlet/src/compiler/*.java


