tst : index.html
	firefox index.html

#
# Compile all proglets 
#

PROGLETS = konsol dicho smiley

index.html : $(wildcard */*/*.java */*/*.xml) makefile
	echo '<html><body><h2>Proglets disponibles</h2><table>' > $@
	for p in $(PROGLETS) ; do pushd org.javascool.$$p/src > /dev/null ; make all clean ; popd > /dev/null ;\
	 echo "<td><td><b>$$p:</b></td><td><a href='org.javascool.$$p/src/jtest.html'>test</a></td><td><a href='`ls org.javascool.$$p/src/[KDS]*.html`'>doc</a></td><td><a href='org.javascool.$$p/src/sujet.html'>sujet</a></td><td><a href='org.javascool.$$p/src/correction.html'>correction</a></td><td><a href='org.javascool.$$p/src/more.html'>more</a></td></tr>" >> $@ ;\
	done
	echo '</table></body></html>' >> $@

#
# Compile/Run Hamdi classes
#

run :
	cd LyceeGamlet/bin ; java compiler/AppletFrame

cmp :
	javac -nowarn -classpath /usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0/lib/tools.jar -d bin LyceeGamlet/src/compiler/*.java

