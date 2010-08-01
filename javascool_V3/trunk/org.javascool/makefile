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
	@rm -rf `find . -name '*.class'` tmp lib/manifest.mf www/api www/javascool.jar

test: www/javascool.jar
	cd tst ; java -cp lib/saxon.jar -jar ../www/javascool.jar

svn: clean
	svn up ; svn ci -m '' ; svn status

doc: www/api/index.html
	@firefox www/api/org/javascool/package-summary.html

pub: www/api/index.html www/javascool.jar
	@php -l www/index.php ; rm -f www/index.php~
	@-rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ www/* scm.gforge.inria.fr:/home/groups/javascool/htdocs/v3 2>&1 | grep -v '^rsync: failed to set'
	@echo 'Si www/index.php a change la page http://javascool.gforge.inria.fr/v3 sera accessible dans environ une heure'

web:
	@firefox http://javascool.gforge.inria.fr/v3

#####################################################################################################################################

# Creation of a signed jar for standalone applications integrating the jdk5 classes

J5=/usr/java/jdk1.5.0_13

# Jar meta-data

vendor=thierry.vieville@sophia.inria.fr, ou=javascool.gforge.inria.fr, o=inria.fr, c=fr
version=3.0
public-key=hello-world
private-key=mer,d,azof

SRC=$(shell find . -name '*.java')

www/javascool.jar : makefile lib/tools.jar lib/saxon.jar lib/manifest.mf $(SRC)
	@echo "make $@"
	@#Compile the java classes
	@$(J5)/bin/javac -Xlint -cp lib/tools.jar $(SRC)
	@#Collects the files in a tmp directiry
	@rm -rf tmp ; mkdir tmp
	@# -1/3 All javascool classes
	@mkdir -p tmp/org/javascool/old ; cp src/org/javascool/old/*.class tmp/org/javascool/old
	@# -2.1/3 The jdk5 compiler classes
	@unzip -qod tmp lib/tools.jar 
	@# -2.2/3 The saxon transformer classes
	@unzip -qod tmp lib/saxon.jar
	@# -3/3 Images and icons
	@svn -q export old/img tmp/img
	@#Build the raw jar
	@cd tmp ; jar cfm ../tmp.jar ../lib/manifest.mf org/javascool com/sun/tools/javac sun/tools/javac com/icl/saxon org/w3c/xsl META-INF/services img
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

CP=/usr/java/jdk1.5.0_13/lib/tools.jar

lib/tools.jar : $(CP)
	@cp $^ $@

SX=/home/vthierry/bin/saxon.jar

lib/saxon.jar : $(SX)
	@cp $^ $@

#####################################################################################################################################

# Java doc generation

SRC2=$(wildcard src/org/javascool/*.java)

www/api/index.html : src/org/javascool/package.html $(SRC2)
	@echo "make $(@D)"
	@mkdir -p $(@D)
	@for f in $(SRC2) ; do mv $$f $$f.bak ; sed 's/\/\*\*\/ *public/\/\*public\*\//g' < $$f.bak > $$f ; touch -r $$f.bak $$f ; done
	@javadoc -quiet -public -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -author -use -link http://download.oracle.com/javase/6/docs/api \
	  -windowtitle "Java's Cool v3" -d $(@D) $(SRC2)
	@for f in $(SRC2) ; do mv $$f $$f.bak ; sed 's/\/\*public\*\//\/\*\*\/public/g' < $$f.bak > $$f ; touch -r $$f.bak $$f ; rm $$f.bak ; done
	@for f in $(SRC2) ; do cp $$f `echo $$f | sed 's/^src/$(subst /,\\/,$(@D))/'` ; done

#####################################################################################################################################
