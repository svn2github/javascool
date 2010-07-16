#####################################################################################################################################
##  Philoumailabo@gmail.com and Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved.                        ##
#####################################################################################################################################

usage :
	@echo 'Usage:'
	@echo ' make clean        : nettoie les fichiers intermédiares'
	@echo ' make test         : lance la vieille version des proglets'
	@echo ' make doc          : crée la doc des classes java'
	@echo ' make pub          : publie les fichiers du site web'


clean:
	@rm -rf `find . -name '*.class'` tmp lib/manifest.mf lib/tools.jar

pub:
	@rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ www/* scm.gforge.inria.fr:/home/groups/javascool/htdocs/v3
	@firefox http://javascool.gforge.inria.fr/v3

doc: www/api/index.html
	@firefox $^

#####################################################################################################################################

# Commande de test par défaut pour monter la v3

test: www/javascool.jar
	cd tst ; java -jar ../www/javascool.jar

# Creation d'un jar en standalone avec les classes du compilateur jdk5 intégrées

J5=/usr/java/jdk1.5.0_13
CP=/usr/java/jdk1.5.0_13/lib/tools.jar
# .. cette référence est nécessaire au mode standalone pour la classe Compiler

www/javascool.jar : makefile lib/tools.jar lib/manifest.mf $(wildcard src/org/javascool/*.java)
	@$(J5)/bin/javac -Xlint -cp $(CP) src/org/javascool/*.java
	@rm -rf tmp ; mkdir tmp
	@mkdir -p tmp/org/javascool ; cp src/org/javascool/*.class tmp/org/javascool 
	@unzip -qod tmp lib/tools.jar
	@svn export old/img tmp/img
	@cd tmp ; jar cfm ../$@ ../lib/manifest.mf org/javascool com/sun/tools/javac sun/tools/javac img

vendor=philoumailabo@gmail.com,thierry.vieville@sophia.inria.fr, ou=javascool.gforge.inria.fr, o=inria.fr, c=fr
version=3.0

lib/manifest.mf : makefile
	@(echo 'Summary: javascool v3'; echo 'Created-By: $(version) (javascool.gforge.inria.fr © creative-commons)' ; echo "Manifest-version: `date '+%Y:%m:%d'`") > $@
	@(echo 'Main-Class: org.javascool.Proglets') >> $@
	@(echo 'Implementation-Vendor: $(vendor)'; echo 'Implementation-URL: http://javascool.gforge.inria.fr' ; echo 'Implementation-Version: $(version)') >> $@

lib/tools.jar : $(CP)
	@cp $^ $@

#####################################################################################################################################

www/api/index.html : src/org/javascool/package.html $(wildcard src/org/javascool/*.java)
	@mkdir -p $(@D)
	@javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d $(@D) src/org/javascool/*.java
	@cp src/org/javascool/*.java $(@D)

#####################################################################################################################################
