
#####################################################################################################################################
#
# Travail de vthierry apres Hamdi sur les proglets 
#
# Intégration de InterfacePrincipale
#  - le code source a été mise en page, les petites classes FileChooser et TextAreaOutputStream mis dans le code, et le code a été intégré au package proglet, mis en conformite avec -Xlint
#  - le file-previwer a été desactivé : utile ici ?
#  - les icones utilisent Proglet.getIcon() pour se charger et la methode setProglet() a ete ajoutee pour les tests en tant que standalone
#  - le bouton stop a été ajouté

#

############ HEADER -> about-all 

#####################################################################################################################################

# Parametres pour la signature

vendor=hamdi.ben_abdallah@inria.fr, ou=interstices.fr, o=inria.fr, c=fr
public-key=hello-world
private-key=mer,d,azof
version=0.0

# Compilation du code

proglet.jar proglet.crt : makefile $(wildcard *.java)
	@rm -f proglet.jar proglet.crt
	@#Compile the sources
	javac -Xlint proglet/*.java
	@# Generates the manifest file
	(echo 'Summary: proglet'; echo 'Created-By: $(version) (interstices.fr © creative-commons)' ; echo "Manifest-version: `date '+%Y:%m:%d'`") > /tmp/tmp.mf
	(echo 'Implementation-Vendor: $(vendor)'; echo 'Implementation-URL: http://javascool.gforge.inria.fr' ; echo 'Implementation-Version: $(version)') >> /tmp/tmp.mf
	@#Build the raw jar
	jar cfm /tmp/tmp.jar /tmp/tmp.mf proglet
	@#Sign the jar file
	keytool -genkey -alias proglet -dname cn='$(vendor)' -storepass '$(public-key)' -keypass '$(private-key)' -keystore /tmp/keystore
	jarsigner -storepass '$(public-key)' -keypass '$(private-key)' -keystore /tmp/keystore -signedjar proglet.jar /tmp/tmp.jar proglet
	keytool -export -alias proglet -storepass '$(public-key)' -keystore /tmp/keystore -file proglet.crt
	@#Cleanup
	rm -rf /tmp/manisfest.mf /tmp/keystore /tmp/tmp.jar proglet/*.class

# Génération des pages du site web



#####################################################################################################################################

PROGLETS = Dicho Scope Tortue Smiley Konsol  
PROGLET = $(lastword $(PROGLETS))

test1 : build
#	java proglet.Proglet $(PROGLET)
	appletviewer applet$(PROGLET).html 
#	firefox index.html

build :
	echo 'jadoc ..' ; javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d api src/*.java
	echo 'index ..' ; $(MAKE) doc index.html 

publish : build
	echo 'svnci ..' ; cd ../../.. ; svn up ; svn ci -m ''
	echo 'rsync ..' ; rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ --exclude src ./* loria.loria.fr:/local/web-homepages/vthierry/proglet
	firefox http://www.loria.fr/~vthierry/proglet/index.html

clean :
	rm -f `svn status ../../../javascool_V2/trunk | grep '^\?.*\.class' | awk '{print $2}'`
	rm -f proglet.jar proglet.crt

#<applet  code="compiler.InterfacePrincipale.class" id="applet1" archive="http://interstices.info/upload/hamdi/testJavaWeb73.jar" height="720" width="920">
#<param name="cache_archive" value="http://interstices.info/upload/hamdi/tools.jar">
#</applet>

# Génération des fichiers de documentation

doc : index.html $(patsubst %.xml,%.htm,$(wildcard doc/about-*.xml doc/sujet-*.xml doc/correction-*.xml)) doc/img/style.css doc/img/help_banner.jpg

XML2XML = /usr/java/jdk1.5.0_13/bin/java -classpath /usr/java/saxon6-5-5/saxon.jar com.icl.saxon.StyleSheet -l

doc/about-%.htm : doc/about-%.xml
	$(XML2XML) -o $@ $^  ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_about2.xsl

doc/sujet-%.htm : doc/sujet-%.xml
	$(XML2XML) -o $@ $^  ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_sujet2.xsl

doc/correction-%.htm : doc/correction-%.xml
	$(XML2XML) -o $@ $^  ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_correction2.xsl

doc/img/% : ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/%
	cp $^ $@

# 

index.html : doc/header.xml $(patsubst %,applet%.html,$(PROGLETS))	
	(echo '<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></head><body><h2>Proglets disponibles</h2>'; cat doc/header.xml ; echo '<table border="1" width="80%" align="center">') > $@	
	for f in $(PROGLETS) ; do echo "<tr><td valign='middle' width='75%'><h4>$$f</h4></td><td valign='middle' align='center'><h4><a href='applet$$f.html'>TEST</a></h4></td><td valign='middle' align='center'><h4><a href='api/proglet/$$f.html'>DOC</a></h4></td></tr>" ; done >> $@
	echo '</table></body></html>' >> $@

applet%.html : makefile
	echo '<html><body><applet code="proglet.Proglet$$Test.class" width="550" height="550"><param name="proglet" value="$*"/></applet></body></html>' > $@

#####################################################################################################################################

# Lancer la page sur interstices 

interstices : clean
	firefox http://interstices.info/jcms/c_45621/gamletlycee?preview=true

# Qq autre test avec les classes eclipes

EJARS=/home/vthierry/doc/pres/JavasCool/plugins
ECP=$(shell echo '$(wildcard $(EJARS)/*.jar)' | sed 's/ /:/g'):../../../javascool_V2/trunk/javascool_2/org.javascool.core/src:.

test2 : build
	javac -classpath $(ECP) Test.java 
	java -classpath $(ECP) Test

#####################################################################################################################################

