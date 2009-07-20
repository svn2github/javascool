
PROGLETS = Konsol Dicho Scope Tortue Smiley 
PROGLET = $(lastword $(PROGLETS))

test : build
#	java proglet.Proglet $(PROGLET)
#	appletviewer applet$(PROGLET).html 
	firefox index.html

build :
	echo 'javac ..' ; javac src/*.java 
	echo 'jadoc ..' ; javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d api src/*.java
	echo 'index ..' ; mkdir -p proglet ; mv src/*.class proglet ; $(MAKE) doc index.html 

publish : build
	echo 'svnci ..' ; cd ../../.. ; svn up ; svn ci -m ''
	echo 'rsync ..' ; rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ --exclude src ./* loria.loria.fr:/local/web-homepages/vthierry/proglet
	firefox http://www.loria.fr/~vthierry/proglet/index.html

clean :
	rm -rf *~ proglet api doc/*.htm *.html

#

doc : $(patsubst %.xml,%.htm,$(wildcard doc/about-*.xml doc/sujet-*.xml doc/correction-*.xml))

XML2XML = /usr/java/jdk1.5.0_13/bin/java -classpath /usr/java/saxon6-5-5/saxon.jar com.icl.saxon.StyleSheet

doc/about-%.htm : doc/about-%.xml
	(echo '<html><body>[<a href="../index.html"><img src="../img/home.png"/></a>]<hr/>' ; cat $^ ; echo '</body></html>') > $@

doc/sujet-%.htm : doc/sujet-%.xml
	$(XML2XML) -o $@ $^  ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_sujet.xsl

doc/correction-%.htm : doc/correction-%.xml
	$(XML2XML) -o $@ $^  ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_correction.xsl

#

index.html : doc/header.xml $(patsubst %,applet%.html,$(PROGLETS))	
	(echo '<html><body><h2>Proglets disponibles</h2>'; cat doc/header.xml ; echo '<table border="1" width="80%" align="center">') > $@	
	for f in $(PROGLETS) ; do echo "<tr><td valign='middle' width='75%'><h4>$$f</h4></td><td valign='middle' align='center'><h4><a href='applet$$f.html'>TEST</a></h4></td><td valign='middle' align='center'><h4><a href='api/proglet/$$f.html'>DOC</a></h4></td></tr>" ; done >> $@
	echo '</table></body></html>' >> $@

applet%.html : makefile
	echo '<html><body><applet code="proglet.Proglet$$Test.class" width="550" height="550"><param name="proglet" value="$*"/></applet></body></html>' > $@

#
# Run Hamdi classes
#

run : 
	cd ../LyceeGamlet/bin ; java compiler.AppletFrame

# -> cd ../LyceeGamlet/bin ; firefox test.html

../LyceeGamlet/bin/test.html : makefile
	echo '<html><body><applet code="compiler.InterfacePrincipale.class" width="800" height="600"><param name="proglet" value="$(PROGLET)"/></applet></body></html>' > $@

# -> javac -nowarn -classpath /usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0/lib/tools.jar -d bin LyceeGamlet/src/compiler/*.java


