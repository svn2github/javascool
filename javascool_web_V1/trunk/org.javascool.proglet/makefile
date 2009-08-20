#####################################################################################################################################
##  Hamdi.Ben_Abdallah@inria.fr et Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.                     ##
#####################################################################################################################################

# Rapport sur le travail de vthierry en aout apres Hamdi
# > Intégration de InterfacePrincipale
#  - le code source a été mise en page et mis en conformite avec -Xlint
#  - les petites classes FileChooser et TextAreaOutputStream sont mis dans le code
#  - le code a été intégré au package proglet, 
#  - le file-previwer a été desactivé : utile ici ?
#  - les icones utilisent Proglet.getIcon() pour se charger indiferrement du jar our de repertoire locaux
#  - la methode setProglet() a ete ajoutee (utile pour les tests en standalone)
#  - le bouton nouveau a ete enlevé (trop de risque détruire le source !!!) 
#  - la dependence au repertoire work et au fichier Ctest a ete enlevee
#  - la compilation du jar est entirement automatisee dans ce makefile (voir ci dessous)
# > Ajout de fonctionnalité
#  + le bouton stop a été ajouté et validé
#  + la console gère les accents en UTF-8 et des tags html pour une meilleure lisibilité
#  + les actions doCompile() et doRun() sont correctement codées avec sauvegarde de fichier etc.. 
#  + un bouton démo a été ajouté
#  + un bouton de choix dynamique de la proglet a ete ajoute
#  + lors de la compilation le Translator définit un serialVersionUID différent pour entraîner la mise à jour de la classe et ne pas ré-exécuter une classe compilée précédemment
#  ! L'applet a ete separee en deux modes d'affichage [edit/compil] et [execute] pour un meilleur graphisme et un usage a travers le service web
#  ! Du coup de tout ces pbs, la compilation a été refaite en jdk5 pour tourner sur MacOS aussi.
# > Bugs, sans solution, rencontres en mode applet avec compilateur intégré en jdk6
#  ? Exceptions de sécurité à la pelle, malgré la certification: lors du rechargement, selon la version du compilateur utilisé, etc...
#  ? Lors du chargement de la classe compilee, non certifiée, il y a de fait une exception de sécurite à l'exécution puisque dès qu'il y a un accès à des ressources certifiées
#  ? Lors du chargement de la classe compilee, si les expections de sécurité ne se produisent pas, elle s'exécute parfois ``dans le vide´´ sans accéder à la proglet affichée
#  -> Donc abandon de la solution et passage a une solution en mode serveur

# Travail de Thierry en cours:
# - Ajout les sujets annoncés en-cours etc...
# - Voir le 27, ok Gilles et soumission à )i( ; comm a animatestroumpf et infolycee + lycee-inria + yannick + AurélienLaurents, avec cc rev-com/dir-sophia.

# Détails d'implémentation à revoir:
#  - Voir le pb de l'acces aux icones à partir du jar qui nécessite une copie sur le site.
#  - Voir le pb du JSlider qui affiche sa valeur entière et perturbe la lecture.
#  - Voir le pb du setLocation() en mode standalone qui affiche mal les deux fenetres edit et run
#  - Voir pb des droits qd on rsync sur javascool
#  - Voir comment remplacer l'usage de saxon:line-number() dans style_about2.xsl pour mise en ligne directe du xml/xsl

# Nouvelles fonctionnalités à développer:
#  - Refaire des tests sous windows/macos/etc..
#  - Ajouter un éditeur couleur dans l'applet

# Environnement utilisé:

JAVA=/usr/java/jdk1.5.0_13

CP=/usr/java/jdk1.5.0_13/lib/tools.jar
# .. en fait, cette référence est uniquement nécessaire au mode standalone pour la classe Compiler, inutile en mode serveur

#####################################################################################################################################

# Commande de test par défaut

dft : tst1

# Test en standalone (pour débugger une proglet)

tst1 : wproglet.jar
	cd tst ; /usr/bin/java -jar ../$^ run Conva

# Test du service web (pour vérifier l'intégration)

tst2 : scp
	cd tst ; firefox http://facets.inria.fr/javascool

# Test du site web (pour vérifier la génération des docs)

tst0 : pub
	cd tst ; firefox http://javascool.gforge.inria.fr/proglet

#####################################################################################################################################
##      Generation des fichiers javascool'proglet, contacts Hamdi.Ben_Abdallah@inria.fr Thierry.Vieville@sophia.inria.fr           ##
#####################################################################################################################################

all : scp pub
	@$(MAKE) clean
	@cd ../../.. ; svn up ; svn ci -m '' ; svn status

clean :
	@rm -rf proglet.jar wproglet.jar *.class */*.class index.html api doc/*.htm

# Parametres pour la signature

vendor=hamdi.ben_abdallah@inria.fr, ou=interstices.fr, o=inria.fr, c=fr
public-key=hello-world
private-key=mer,d,azof
version=0.0

# Compilation du code et creation d'une jar certifiée

proglet.jar : makefile $(wildcard proglet/*.java) $(wildcard img/*.*)
	@rm -f $@
	@#Compile the sources
	$(JAVA)/bin/javac -Xlint -cp $(CP) proglet/*.java
	@# Generates the manifest file
	(echo 'Summary: javascool proglets'; echo 'Created-By: $(version) (interstices.fr © creative-commons)' ; echo "Manifest-version: `date '+%Y:%m:%d'`") > tmp.mf
	(echo 'Main-Class: proglet.Proglet') > tmp.mf
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
	@rsync --rsh='ssh -C' --archive --delete-excluded img/home.png proglet/index.php proglet.jar facets.inria.fr:javascool

# Génération des pages du site web

doc : index.html wproglet.jar api/index.html $(patsubst %.xml,%.htm,$(wildcard doc/*.xml)) $(patsubst %.fig,%.png,$(wildcard doc/img/*.fig)) doc/img/style.css

# Copie des fichiers de documentation sur le site web

pub : doc
	@-rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ index.html doc api img wproglet.jar scm.gforge.inria.fr:/home/groups/javascool/htdocs/proglet \
	 2>&1 | egrep -v '^(rsync: failed to set permissions|rsync error: some files/attrs were not transferred)'

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
	@echo '<script>location.replace("doc/about-all.htm");</script>' >$@	

#####################################################################################################################################
