#####################################################################################################################################
##  Philoumailabo@gmail.com and Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved.                        ##
#####################################################################################################################################

# Environnement utilisé:

JAVA=/usr/java/jdk1.5.0_13

CP=/usr/java/jdk1.5.0_13/lib/tools.jar
# .. cette référence est nécessaire au mode standalone pour la classe Compiler

#####################################################################################################################################

# Commande de test par défaut pour monter la v3

tst: javascool.jar
	cd tst ; java -jar ../javascool.jar

cmp:
	$(JAVA)/bin/javac -Xlint -cp $(CP) src/org/javascool/*.java

clean:
	rm -rf `find . -name '*.class'` tmp javascool.jar lib/manifest.mf lib/tools.jar

# Creation d'un jar en standalone avec les classes du compilateur jdk5 intégrées

javascool.jar : lib/tools.jar lib/manifest.mf $(wildcard src/org/javascool/*.java)
	$(JAVA)/bin/javac -Xlint -cp $(CP) src/org/javascool/*.java
	@rm -rf tmp ; mkdir tmp
	@mkdir -p tmp/org/javascool ; cp src/org/javascool/*.class tmp/org/javascool 
	@unzip -qod tmp lib/tools.jar
	@svn export old/img tmp/img
	@cd tmp ; $(JAVA)/bin/jar cfm ../$@ ../lib/manifest.mf org/javascool com/sun/tools/javac sun/tools/javac img

vendor=philoumailabo@gmail.com,thierry.vieville@sophia.inria.fr, ou=javascool.gforge.inria.fr, o=inria.fr, c=fr
version=3.0

lib/manifest.mf : makefile
	@(echo 'Summary: javascool v3'; echo 'Created-By: $(version) (javascool.gforge.inria.fr © creative-commons)' ; echo "Manifest-version: `date '+%Y:%m:%d'`") > $@
	@(echo 'Main-Class: org.javascool.Proglets') >> $@
	@(echo 'Implementation-Vendor: $(vendor)'; echo 'Implementation-URL: http://javascool.gforge.inria.fr' ; echo 'Implementation-Version: $(version)') >> $@

lib/tools.jar : $(CP)
	cp $^ $@
