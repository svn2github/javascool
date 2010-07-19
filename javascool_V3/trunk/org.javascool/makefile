#####################################################################################################################################
##  Philoumailabo@gmail.com and Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved.                        ##
#####################################################################################################################################

usage :
	@echo 'Usage:'
	@echo ' make clean        : nettoie les fichiers intermédiares'
	@echo ' make test         : lance la vieille version des proglets'
	@echo ' make svn          : synchronize avec le svn'
	@echo ' make doc          : cree la doc des classes java'
	@echo ' make pub          : publie les fichiers du site web'

clean:
	@rm -rf `find . -name '*.class'` tmp lib/manifest.mf lib/tools.jar

test: www/javascool.jar
	cd tst ; java -jar ../www/javascool.jar

svn: clean
	rm -rf www/api www/avascool.jar
	svn up ; svn ci -m '' ; svn status

doc: www/api/index.html
	@firefox www/api/org/javascool/package-summary.html

pub: www/api/index.html www/javascool.jar
	@-rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ www/* scm.gforge.inria.fr:/home/groups/javascool/htdocs/v3

web:
	@firefox http://javascool.gforge.inria.fr/v3

#####################################################################################################################################

# Creation of a signed jar for standalone applications integrating the jdk5 classes

J5=/usr/java/jdk1.5.0_13
CP=/usr/java/jdk1.5.0_13/lib/tools.jar

# Jar meta-data

vendor=thierry.vieville@sophia.inria.fr, ou=javascool.gforge.inria.fr, o=inria.fr, c=fr
version=3.0
public-key=hello-world
private-key=mer,d,azof

www/javascool.jar : makefile lib/tools.jar lib/manifest.mf $(wildcard src/org/javascool/old/*.java)
	@#Compile the java classes
	@$(J5)/bin/javac -Xlint -cp $(CP) src/org/javascool/old/*.java
	@#Collects the files in a tmp directiry
	@rm -rf tmp ; mkdir tmp
	@# -1/3 All javascool classes
	@mkdir -p tmp/org/javascool/old ; cp src/org/javascool/old/*.class tmp/org/javascool/old
	@# -2/3 The jdk5 compiler classes
	@unzip -qod tmp lib/tools.jar
	@# -3/3 Images and icons
	@svn export old/img tmp/img
	@#Build the raw jar
	@cd tmp ; jar cfm ../tmp.jar ../lib/manifest.mf org/javascool com/sun/tools/javac sun/tools/javac img
	@#Sign the jar file
	@keytool -genkey -alias javascool -validity 3000 -dname cn='$(vendor)' -storepass '$(public-key)' -keypass '$(private-key)' -keystore tmp.key
	@jarsigner -storepass '$(public-key)' -keypass '$(private-key)' -keystore tmp.key -signedjar $@ tmp.jar javascool
	@#Optional verification commands
	@#-keytool -export -alias proglet -storepass '$(public-key)' -keystore tmp.key -file javascool.crt
	@#-jarsigner -verify -verbose -certs $@
	@#Cleanup
	rm -rf tmp tmp.key tmp.jar javascool.crt 

lib/manifest.mf : makefile
	@(echo 'Summary: javascool v3'; echo 'Created-By: $(version) (javascool.gforge.inria.fr © creative-commons)' ; echo "Manifest-version: `date '+%Y:%m:%d'`") > $@
	@(echo 'Main-Class: org.javascool.old.Proglets') >> $@
	@(echo 'Implementation-Vendor: $(vendor)'; echo 'Implementation-URL: http://javascool.gforge.inria.fr' ; echo 'Implementation-Version: $(version)') >> $@

lib/tools.jar : $(CP)
	@cp $^ $@

#####################################################################################################################################

# Java doc generation

www/api/index.html : src/org/javascool/package.html $(wildcard src/org/javascool/*.java)
	@mkdir -p $(@D)
	@javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -d $(@D) src/org/javascool/*.java
	@cp src/org/javascool/*.java $(@D)

#####################################################################################################################################
