#####################################################################################################################################
##  Hamdi.Ben_Abdallah@inria.fr et Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.                     ##
#####################################################################################################################################

# Travail de Thierry en cours:
# - Ajout qq sujets au fil de l'eau . . 
# - Voir algo des cercles à entrer: demander autour de nous

# Pour Hamdi: nouvelle fonctionnalité à développer:
#  - Ajouter un éditeur couleur dans l'applet, 
#       donc reprendre proglet.SourceEditor avec un javax.swing.JTexEditorPane de type text/rtf ou text/html; au lieu d'un TextArea,
#       avec le pb de gérer les numéros de ligne avec un texte de structure de telle structure

#
# Détails d'implémentation non critique, mais qui gagneraient à être revus sur le principe:
#
#  - Voir pb des droits qd on rsync sur javascool
#  - Voir le pb du JSlider qui affiche sa valeur entière et perturbe la lecture.
#  - Voir comment remplacer l'usage de saxon:line-number() dans style_about2.xsl pour mise en ligne directe du xml/xsl
#  - Voir le pb du setLocation() en mode standalone qui affiche mal les deux fenetres edit et run
#  - Voir à permettre d'agrandir la fenêtre d'édition en standalone en largeur qd on passe en mode maximal size.
#  - Utiliser la methode POST dans InterfacePrincipale pour appeler http://facets.inria.fr/javascool et non GET
#  - Voir si on peut alleger la copie des img sur le site si Proglets.getIcon devient inutile http://javascool.gforge.inria.fr/proglet/img
#  - Voir à utiliser un algorithme public pour proglet.Translator.reformat plutôt que ce bricolage local
#  - La version web a qq soucis, certains exécutaient encore le programme d'un autre. Sous IE, la zone d'input ne marche pas bien, elle s'active mais impossible de taper dedans.

#####################################################################################################################################

# Environnement utilisé:

JAVA=/usr/java/jdk1.5.0_13

CP=/usr/java/jdk1.5.0_13/lib/tools.jar
# .. cette référence est nécessaire au mode standalone pour la classe Compiler

#####################################################################################################################################

# Commande de test par défaut

dft : tst1

# Test en standalone (pour générer la doc)

tst0 : doc
	firefox doc/about-all.htm

# Test en standalone (pour débugger une proglet)

tst1 : wproglet.jar 
	cd tst ; /usr/bin/java -jar ../$^ edit Konsol Test0.jvs

# Test du service web (pour vérifier la demo web)

tst2 : scp
	cd tst ; firefox http://facets.inria.fr/javascool

# Test du site web (pour vérifier la génération des docs)

tst3 : pub
	cd tst ; firefox http://javascool.gforge.inria.fr/proglet

#####################################################################################################################################
##      Generation des fichiers javascool'proglet, contacts Hamdi.Ben_Abdallah@inria.fr Thierry.Vieville@sophia.inria.fr           ##
#####################################################################################################################################

all : scp pub
	@$(MAKE) clean
	@cd ../../.. ; svn up ; svn ci -m '' ; svn status

clean :
	@rm -rf proglet.jar proglet.zip wproglet.jar *.class */*.class index.html api doc/*.htm tmp.*

# Parametres pour la signature

vendor=hamdi.ben_abdallah@inria.fr, ou=interstices.fr, o=inria.fr, c=fr
public-key=hello-world
private-key=mer,d,azof
version=0.0

# Compilation du code et creation d'une jar certifiée

proglet.jar : makefile $(wildcard proglet/*.java) $(wildcard img/*.*)
	@rm -f $@
	@#Delete an eclipse file
	@rm -f proglet/Activator.java
	@#Compile the sources
	$(JAVA)/bin/javac -Xlint -cp $(CP) proglet/*.java
	@# Generates the manifest file
	(echo 'Summary: javascool proglets'; echo 'Created-By: $(version) (interstices.fr © creative-commons)' ; echo "Manifest-version: `date '+%Y:%m:%d'`") > tmp.mf
	(echo 'Main-Class: proglet.Proglets') > tmp.mf
	(echo 'Implementation-Vendor: $(vendor)'; echo 'Implementation-URL: http://javascool.gforge.inria.fr/proglet' ; echo 'Implementation-Version: $(version)') >> tmp.mf
	@#Build the raw jar
	$(JAVA)/bin/jar cfm tmp.jar tmp.mf proglet/*.class img/*.*
	@#Sign the jar file
	$(JAVA)/bin/keytool -genkey -alias proglet -validity 3000 -dname cn='$(vendor)' -storepass '$(public-key)' -keypass '$(private-key)' -keystore tmp.key
	$(JAVA)/bin/jarsigner -storepass '$(public-key)' -keypass '$(private-key)' -keystore tmp.key -signedjar proglet.jar tmp.jar proglet
	@##keytool -export -alias proglet -storepass '$(public-key)' -keystore tmp.key -file proglet.crt
	@##jarsigner -verify -verbose -certs proglet.jar
	@#Cleanup
	rm -rf tmp.mf tmp.key tmp.jar proglet/*.class

# Creation d'une jar en standalone avec les classes du compilateur jdk5 intégrées

wproglet.jar : proglet.jar $(CP)
	@rm -rf tmp ; mkdir tmp
	@unzip -qod tmp $(CP)
	@unzip -qod tmp proglet.jar
	@cd tmp ; $(JAVA)/bin/jar cfm ../$@ META-INF/MANIFEST.MF proglet img com/sun/tools/javac sun/tools/javac
	@rm -rf tmp

# Copie des fichiers pour le service web

scp : proglet.jar
	@echo sync on facets.inria.fr
	@-rsync --rsh='ssh -C' --archive --delete-excluded img/home.png img/doisneaubuffon.jpg proglet/index.php proglet.jar facets.inria.fr:javascool

# Génération des pages du site web

doc : index.html wproglet.jar api/index.html $(patsubst %.xml,%.htm,$(wildcard doc/*.xml)) $(patsubst %.fig,%.png,$(wildcard doc/img/*.fig)) doc/img/style.css
	@rm -f proglet.zip ; $(MAKE) proglet.zip

# Copie des fichiers de documentation sur le site web

pub : doc
	@echo sync on scm.gforge.inria.fr
	@-rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ index.html doc api img wproglet.jar proglet.zip \
	 scm.gforge.inria.fr:/home/groups/javascool/htdocs/proglet 2>&1 | egrep -v '^(rsync: failed to set permissions|rsync error: some files/attrs were not transferred)'
	@-rsync index.html scm.gforge.inria.fr:/home/groups/javascool/htdocs/proglets 

ftp :
	sftp scm.gforge.inria.fr:/home/groups/javascool/htdocs/proglet

# - génération des fichiers de documentation

XML2XML = /usr/java/jdk1.5.0_13/bin/java -classpath /usr/java/saxon6-5-5/saxon.jar com.icl.saxon.StyleSheet -l

doc/about-%.htm : doc/about-%.xml
	@$(XML2XML) -o $@ $^  ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_about2.xsl

doc/sujet-%.htm : doc/sujet-%.xml
	@$(XML2XML) -o $@ $^  ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_sujet2.xsl

doc/img/%.png : doc/img/%.fig
	fig2dev -L png $^ > $@

doc/img/% : ../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/%
	@cp $^ $@

api/index.html : proglet/package.html $(wildcard proglet/*.java)
	@javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d api proglet/*.java
	@cp proglet/*.java api/proglet

index.html : makefile
	@echo '<script>location.replace("../proglet/doc/about-all.htm");</script>' >$@	

proglet.zip : 
	@rm -f $@
	@cd .. ; zip -9qr org.javascool.proglet/$@ org.javascool.proglet/index.html org.javascool.proglet/doc org.javascool.proglet/api org.javascool.proglet/wproglet.jar \
	 -x \*/.svn/\* \*/\*/.svn/\*\*/.svn/\*/\* \*/\*/.svn/\*/\* 

#####################################################################################################################################
