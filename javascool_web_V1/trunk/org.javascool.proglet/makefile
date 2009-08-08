
#####################################################################################################################################

# Travail de vthierry apres Hamdi sur les proglets 

# Intégration de InterfacePrincipale
#  - le code source a été mise en page et mis en conformite avec -Xlint
#  - les petites classes FileChooser et TextAreaOutputStream mis dans le code
#  - le code a été intégré au package proglet, 
#  - le file-previwer a été desactivé : utile ici ?
#  - les icones utilisent Proglet.getIcon() pour se charger et la methode setProglet() a ete ajoutee pour les tests en tant que standalone
#  - le bouton stop a été ajouté

# 

dft : tst2

#####################################################################################################################################
##      Generation des fichiers javascool'proglet, contacts Hamdi.Ben_Abdallah@inria.fr Thierry.Vieville@sophia.inria.fr           ##
#####################################################################################################################################

all : cmp doc

clean :
	rm -rf proglet.jar proglet.crt proglet/*.class index.html api doc/*.htm

# Parametres pour la signature

vendor=hamdi.ben_abdallah@inria.fr, ou=interstices.fr, o=inria.fr, c=fr
public-key=hello-world
private-key=mer,d,azof
version=0.0

# Compilation du code

cmp : proglet.jar proglet.crt

proglet.jar proglet.crt : makefile $(wildcard proglet/*.java)
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
	jarsigner -verify proglet.jar
	@#Cleanup
	rm -rf /tmp/manisfest.mf /tmp/keystore /tmp/tmp.jar proglet/*.class

# Génération des pages du site web

doc : index.html api/index.html $(patsubst %.xml,%.htm,$(wildcard doc/*.xml)) doc/img/style.css doc/img/help_banner.jpg

# - génération des fichiers de documentation

XML2XML = /usr/java/jdk1.5.0_13/bin/java -classpath /usr/java/saxon6-5-5/saxon.jar com.icl.saxon.StyleSheet -l

doc/about-%.htm : doc/about-%.xml
	$(XML2XML) -o $@ $^  ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_about2.xsl

doc/proglet-%.htm : doc/proglet-%.xml
	$(XML2XML) -o $@ $^  ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_about2.xsl

doc/sujet-%.htm : doc/sujet-%.xml
	$(XML2XML) -o $@ $^  ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_sujet2.xsl

doc/correction-%.htm : doc/correction-%.xml
	$(XML2XML) -o $@ $^  ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_correction2.xsl

doc/img/% : ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/%
	cp $^ $@

api/index.html : $(wildcard proglet/*.java)
	javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d api proglet/*.java

index.html : makefile
	echo '<script>location.replace("doc/about-all.htm");</script>' >$@	

# Publication sur le site externe

pub : cmp doc
	echo 'svnci ..' ; cd ../../.. ; svn up ; svn ci -m ''
	echo 'rsync ..' ; rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ ./* loria.loria.fr:/local/web-homepages/vthierry/proglet
	firefox http://www.loria.fr/~vthierry/proglet/index.html

#####################################################################################################################################

tst1 : cmp
	java -cp proglet.jar proglet.Proglet Konsol

tst2 : cmp $(patsubst %.xml,%.htm,$(wildcard doc/proglet-*.xml)) 
	firefox doc/proglet-konsol.htm 

#####################################################################################################################################
