
PROGLETS = Konsol Dicho Scope Tortue Smiley 
PROGLET = $(lastword $(PROGLETS))

test : build
#	cd .. ; java proglet.Proglet $(PROGLET)
#	cd .. ; appletviewer applet$(PROGLET).html 
	cd .. ; firefox index.html

build :
	echo 'javac ..' ; javac *.java 
	echo 'jadoc ..' ; javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d ../api *.java
	echo 'index ..' ; mkdir -p ../proglet ; mv *.class ../proglet ; $(MAKE) -C ../doc ; $(MAKE) ../index.html 

publish : build
	echo 'svnci ..' ; cd .. ; svn up ; svn ci -m ''
	echo 'rsync ..' ; rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ ../* loria.loria.fr:/local/web-homepages/vthierry/proglet
	firefox http://www.loria.fr/~vthierry/proglet/index.html

clean :
	rm -rf *~ ../proglet ../api ../doc/*.htm ../index.html

#

../index.html : $(patsubst %,../applet%.html,$(PROGLETS))	
	(echo '<html><body><h2>Proglets disponibles</h2>'; cat ../doc/header.xml ; echo '<table border="1" width="80%" align="center">') > $@	
	for f in $(PROGLETS) ; do echo "<tr><td valign='middle' width='75%'><h4>$$f</h4></td><td valign='middle' align='center'><h4><a href='applet$$f.html'>TEST</a></h4></td><td valign='middle' align='center'><h4><a href='api/$$f.html'>DOC</a></h4></td></tr>" ; done >> $@
	echo '</table></body></html>' >> $@

../applet%.html : makefile
	echo '<html><body><applet code="proglet.Proglet$$Test.class" width="550" height="550"><param name="proglet" value="$*"/></applet></body></html>' > $@

#
# Run Hamdi classes
#

run :
	cd ../../LyceeGamlet/bin ; java compiler/AppletFrame

# -> javac -nowarn -classpath /usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0/lib/tools.jar -d bin LyceeGamlet/src/compiler/*.java


