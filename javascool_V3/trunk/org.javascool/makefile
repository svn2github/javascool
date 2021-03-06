#####################################################################################################################################
##  Philoumailabo@gmail.com and Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved.                        ##
#####################################################################################################################################

# Used to use sun sun-jdk instead of open-jdk because of processing and windows platforms
export PATH:=/usr/java/jdk1.6.0_22/bin:$(PATH)

# Defines the beta-relase jar temporary suffix under development (e.g., = -3-3)
JAR =

default : run

dft : jar
	javac  -cp ./www/javascool$(JAR).jar Tst.java
	java -cp ./www/javascool$(JAR).jar:. Tst

# General commands

usage :
	@grep '^#::' makefile | sed 's/^#:://'

what :
	@grep '^#:' makefile | sed 's/^#://'

run : jar
	@cd tst ; java -jar ../www/javascool$(JAR).jar

doc : api
	firefox file:www/api/org/javascool/package-summary.html

svn : clean
	@svn up ; svn ci -m '' ; svn status

clean :
	@rm -rf `find . -name '*.class'` tmp tmp.* tst/tmp.* tst/*.java lib/js-manifest.mf lib/sketchbook.zip www/api www/javascool$(JAR).jar www/sources.zip
	@$(MAKE) -C sketchbook clean

#####################################################################################################################################

# Creation of a signed jar for standalone applications integrating the jdk classes

jar : htm www/javascool$(JAR).jar

# Jar parameters, files and meta-data

J6=/usr/java/jdk1.6.0_22

vendor=thierry.vieville@inria.fr, ou=javascool.gforge.inria.fr, o=inria.fr, c=fr
version=3.2
revision=$(shell svn info | grep '^R.vision *:' | sed 's/^R.vision *: *//')
public-key=hello-world
private-key=mer,d,azof

SRC = \
 $(shell find src -name '*.java') \
 src/org/javascool/proglets.java  $(patsubst %.xslt,%.java,$(shell find src -name '*.xslt'))

XSL = \
 $(shell find src -name '*.xslt')

DOC = \
 $(patsubst %.fig,%.png,$(shell find src -name '*.fig')) $(patsubst %.xml,%.htm,$(shell find src -name '*.xml' \( \! -name 'about-pro-proglets.xml' \))) $(patsubst %.xml,%.htm,$(shell find sketchbook -name '*.xml'))

JVS = \
  src/proglet/javaprog/FleurEnRythme.jvs

CP=src:lib/tools.zip:lib/saxon.jar

# Compiles the java classes using the default jdk and builds the certified jar

www/javascool$(JAR).jar : makefile lib/tools.zip lib/saxon.jar lib/sketchbook.zip lib/js-manifest.mf $(SRC) $(JVS) htm
	@echo "make $@"
	@#[1] Compile the java classes
	@$(J6)/bin/javac -Xlint -cp $(CP) $(SRC)
	@#[2] Collects the files in a tmp directory
	@rm -rf tmp ; svn -q export src tmp 
	@for f in `find src -name '*.class'` ; do cp $$f `echo $$f | sed 's/^src/tmp/'` ; done
	@cp lib/js-manifest.mf tmp/org/javascool
	@# + The jdk compiler classes
	@unzip -qod tmp lib/tools.zip 
	@# + The saxon transformer classes
	@unzip -qod tmp lib/saxon.jar
	@# + The processing jars and docs
	@if [ -f lib/processing.zip ] ; then echo '  + processing ..' ; unzip -qod tmp lib/processing.zip ; fi
	@unzip -qod tmp lib/sketchbook.zip
	@#Build the raw jar
	@cd tmp ; jar cfm ../tmp.jar ../lib/js-manifest.mf *
	@#Sign the jar file
	@keytool -genkey -alias javascool -validity 3000 -dname cn='$(vendor)' -storepass '$(public-key)' -keypass '$(private-key)' -keystore tmp.key
	@jarsigner -storepass '$(public-key)' -keypass '$(private-key)' -keystore tmp.key -signedjar $@ tmp.jar javascool
	@#Optional verification commands
	@#-keytool -export -alias proglet -storepass '$(public-key)' -keystore tmp.key -file javascool.crt
	@#-jarsigner -verify -verbose -certs $@
	@#Cleanup
	@rm -rf tmp tmp.zip tmp.key tmp.jar javascool.crt 
	@echo ".. done: size = `du -s -B 1024 $@ | awk '{print $$1}'`Kbytes revision = `grep REVISION lib/js-manifest.mf | sed 's/.*REVISION//'`"

lib/js-manifest.mf : .svn makefile
	@echo 'Summary: javascool v$(version)' > $@
	@echo "Manifest-version: $(version) on `date '+%Y:%m:%d'` REVISION #$(revision)" >> $@
	@echo 'Created-By: inria.fr (javascool.gforge.inria.fr) ©INRIA: CeCILL V2 + CreativeCommons BY-NC-ND V2' >> $@
	@echo 'Implementation-URL: http://javascool.gforge.inria.fr'  >> $@
	@echo 'Implementation-Vendor: $(vendor)' >> $@
	@echo 'Implementation-Version: $(version)' >> $@
	@echo 'Main-Class: org.javascool.JsMain' >> $@

# Compiles the xml doc files

htm : $(DOC) 

%.htm : %.xml src/org/javascool/hdoc2htm.xslt
	@java -jar lib/saxon.jar -o $@ $^

src/org/javascool/hdoc2htm.xslt : src/org/javascool/hml2htm.xslt
	@touch $@

lib/sketchbook.zip : $(DOC)
	@rm -f $@; zip -9qr $@ `find sketchbook -name '*.htm' -o -name '*.xml' -o -name '*.pde' -o -name '*.png' -o -name '*.jvs'`

%.png : %.fig
	@fig2dev -L png $^ > $@

# Compiles the java classes using the default jdk 

cmp : $(SRC)
	@rm -f $@; javac -Xlint -cp $(CP) $(SRC)

# Automatically generated java files

%.java : %.jvs
	@echo "jvs2java $*"
	@javac -Xlint -cp $(CP) $(shell find src/org/javascool -name '*.java') $(shell find src/proglet/ingredients -name '*.java') 
	@java -cp $(CP) org.javascool.Jvs2Java $*

%.java : %.xslt
	@echo "<div/>" > /tmp/null.xml ; java -jar lib/saxon.jar -o /tmp/null.htm /tmp/null.xml $^ ; rm -f /tmp/null.xml /tmp/null.htm
	@echo '/* Automatically generated by the makefile: DO NOT EDIT !*/ package org.javascool; class $(*F) { static String xsl = ' > $@
	@cat $^ | sed 's/"/\\"/g' | sed 's/\(.*\)/"\1\\n"+/' >> $@
	@echo '"";}' >> $@ 	

src/org/javascool/proglets.java : $(shell find src/proglet -name '*.java')
	@echo '/* Automatically generated by the makefile: DO NOT EDIT !*/ package org.javascool; class proglets { static String proglets[] = {' > $@
	@for f in `egrep -l 'implements[ \t]+(org.javascool.)?Proglet' $^` ; do echo $$f | sed 's/^src\/\(.*\)\.java$$/"\1",/' | sed 's/\//./g' ; done >> $@
	@echo '""}; }' >> $@

# Installation of used jars

ifeq ($(USER), vthierry)

TL=$(J6)/lib/tools.jar

lib/tools.zip : $(TL)
	@echo "cp $@ .."
	@rm -rf tmp $@ ; mkdir tmp ; unzip -qod tmp $^
	@cd tmp ; jar cf ../$@ com/sun/tools/javac sun/tools/javac com/sun/source/tree com/sun/source/util
	@rm -rf tmp

SX=/home/vthierry/bin/saxon.jar

lib/saxon.jar : $(SX)
	@echo "cp $@ .."
	@cp $^ $@

endif

#####################################################################################################################################

# Source file beautifier

bty :
	for f in `find src -name '*.java' -o -name '*.jvs'` ; do uncrustify -q -c lib/uncrustify.cfg --replace $$f ; done
	for f in `find www -name '*.java' -o -name '*.jvs'` ; do uncrustify -q -c lib/uncrustify.cfg --replace $$f ; done

#####################################################################################################################################

# JavaScool doc generation

JDOC_OPTION = -quiet -nohelp -nodeprecated -nonavbar -nosince -notree -noindex -author -use -charset "UTF-8" -windowtitle "Java's Cool v3" 

JDOC_DEP    = $(SRC) $(DOC) $(JVS) $(shell find src -name package.html) lib/sketchbook.zip lib/js-manifest.mf makefile 

ifdef JDOC
JDOC_OPTION+= -private -linkoffline $(JDOC)/api/ $(JDOC)/api/ 

api: htm $(JDOC)/APIJVS

$(JDOC)/APIJVS : $(JDOC_DEP)
else
JDOC_OPTION+= -public -link http://download.oracle.com/javase/6/docs/api

api : htm www/api 

www/api : $(JDOC_DEP)
endif
	@rm -rf $@ ; mkdir -p $@
	@echo "make $@"
	@# Generates the javadoc
	@for f in $(SRC) ; do mv $$f $$f.bak ; sed 's/\/\*\*\/ *public/\/\*public\*\//g' < $$f.bak > $$f ; touch -r $$f.bak $$f ; done
	@javadoc $(JDOC_OPTION) $(SRC) -d $@
	@$(MAKE) aps
	@# Copies all complementary files
	@cd src ; cp --parents $(patsubst src/%,%,$(shell find src -name '*.java') $(JVS) $(XSL)) ../$@
	@for d in $(shell find src -name doc-files) ; do svn -q --force export $$d www/api/`echo $$d | sed 's/src\///'` ; done
	@unzip -qod $@ lib/sketchbook.zip
	@cp lib/js-manifest.mf $@/js-manifest.txt
	@#Generates the html files from the source files
	@for f in `find www -name '*.java' -o -name '*.pde' -o -name '*.jvs'` ; do cp $$f $$f.java ; java -jar lib/java2html.jar -srcfile $$f.java ; rm $$f.java ; done
	@#Generates the source archive
	@cd www ; rm -f sources.zip ; zip -9qr sources.zip api
	@#Concludes
	@echo ".. done: size = `du -s -B 1024 $@ | awk '{print $$1}'`Kbytes"

aps :
	@for f in $(SRC) ; do mv $$f $$f.bak ; sed 's/\/\*public\*\//\/\*\*\/public/g' < $$f.bak > $$f ; touch -r $$f.bak $$f ; rm $$f.bak ; done

#####################################################################################################################################

# Web site files publication

wpub : jar api 
	@$(MAKE) -C sketchbook publish
	@php -l www/index.php ; rm -f www/*~
	@-rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ --exclude index.php --exclude javascool.jar www/* scm.gforge.inria.fr:/home/groups/javascool/htdocs/v3 \
	 2>&1 | grep -v '^rsync: failed to set'
	@-rsync --rsh='ssh -C' www/index.php scm.gforge.inria.fr:/home/groups/javascool/htdocs/v3/.htindex.php
	@ssh scm.gforge.inria.fr 'sh -c "cd /home/groups/javascool/htdocs/v3 ; cat .htindex.php > index.php ; rm .htindex.php"'

# Jar file publication: WARNING this disseminates your new release all around the world !!!

jpub : jar
	@echo 'Ok to change the official javascool.jar ? CTRL+C to cancel, Enter to confirm: ' ; read cont
	@-rsync --rsh='ssh -C' www/javascool.jar scm.gforge.inria.fr:/home/groups/javascool/htdocs/v3/javascool.jar

# Check the web site links 

wref : wpub
	@lib/run.sh org.javascool.LinkCheck -recursive http://javascool.gforge.inria.fr/v3/ | tee brokenlinks.txt

# Clean the web cache
# NE PLUS UTILISER !!!
#wraz : 
#	@wget -q -O - http://javascool.gforge.inria.fr?kezako=niquelekacheux

# Copy the files on a USB key of name /media/javascool

USBROOT=/media/javascool

wusb : jar
	@cp lib/javascool.bat www/javascool*.jar $(USBROOT)
	@rm -rf $(USBROOT)/javascool.dir ; mkdir $(USBROOT)/javascool.dir ;\
	 for d in www/documents www/sketchbook ; do rsync --rsh='ssh -C' --archive --delete-excluded --exclude .svn/ $$d $(USBROOT)/javascool.dir ; done
	@unzip -d $(USBROOT)/opengl.dir www/sketchbook/EnVoiture/opengl-windows.zip ; cp sketchbook/EnVoiture/about-opengl.htm $(USBROOT)/opengl.dir
	@du -s $(USBROOT)/javascool*.*
	@umount $(USBROOT)

#####################################################################################################################################

# Useful links

jweb :
	@firefox http://javascool.gforge.inria.fr/v3

wiki :
	@firefox http://wiki.inria.fr/sciencinfolycee/JavaScool:ToDoList

glog :
	@firefox https://www.google.com/analytics

gref :
	@firefox https://www.google.com/webmasters/tools/dashboard

#####################################################################################################################################

#                                       System operation commands: TO BE USED WITH CARE !!!!                                        #

# This is used to access to web-site files

ssh :
	@gnome-terminal -e "ssh scm.gforge.inria.fr 'cd /home/groups/javascool/htdocs ; bash -i'" 


# This is used to install the javadoc bundle in /usr/local

jinstall :
	@sudo sh -c 'mkdir -p /usr/local/javascool/jdoc ; chmod a+rwx /usr/local/javascool/jdoc'

# Check the site apache log

wlog :
	@firefox http://javascool.gforge.inria.fr/log

ifeq ($(USER), vthierry)

# This is a remainder about what has been used to set the web site on v3

wv3 :
	@(echo '<script>' ; echo 'location.replace("./v3/index.php");' ; echo '</script>') > tmp.htm
	@rsync --rsh='ssh -C' tmp.htm scm.gforge.inria.fr:/home/groups/javascool/htdocs/index.html
	@firefox http://javascool.gforge.inria.fr
	@(echo '<script>' ; echo 'location.replace("../v3/index.php");' ; echo '</script>') > tmp.htm
	@rsync --rsh='ssh -C' tmp.htm scm.gforge.inria.fr:/home/groups/javascool/htdocs/proglet/index.html 
	@rsync --rsh='ssh -C' tmp.htm scm.gforge.inria.fr:/home/groups/javascool/htdocs/proglets/index.html 
	@firefox http://javascool.gforge.inria.fr
	@ssh facets.inria.fr '/bin/rm -rf /home/vthierry/javascool/* /home/vthierry/javascool/.[a-zA-Z0-1]*'
	@echo '<?php /* No web service in V3 ! */ header("Location:http://javascool.gforge.inria.fr"); ?>' > tmp.php	
	@rsync --rsh='ssh -C' tmp.php facets.inria.fr:javascool/index.php ; rm tmp.php
	@firefox http://facets.inria.fr/javascool


wlocal : 
	@/bin/rm -rf /usr/local/javascool/www
	@echo wget -q -nH -r -l 0 -k -p -P /usr/local/javascool/www http://javascool.gforge.inria.fr


endif

# Useful insuperable commands (to be used with MUCH care):
#
# [1] resolve conflicts in favor of MY working version
# - svn up ; svn resolve --accept working . 
# [2] remove files marked as deleted
# - svn rm `svn status | grep '^!' | awk '{print $2}'` 
#

# MakeToolBar commands

please : cmp
	@lib/run.sh  org.javascool.MakeToolBar

done : 
	@echo '..done (Enter to close)' ; read cont

#####################################################################################################################################

#: ********************************************************************************
#: *****                 JavaScool developer's readme                         *****
#: ********************************************************************************
#:
#: Welcome to the JavaScool V3 development bundle :
#:  please contact: philoumailabo@gmail.com,thierry.vieville@inria.fr for any info/request/..
#:
#:
#: [1] The javascool/javascool_V3/trunk/org.javascool file bundle description.
#: 
#: [1.1] Source files:
#: ./makefile                 : contains all compilation/publication/test commands
#: ./src/org/javascool/*.java : contains the javascool core and widget classes and doc-files
#: ./src/org/javascool/*.xslt : contains the javascool XSLT rules.
#: ./src/proglet/*/*.java     : contains the proglets classes and doc-files
#: ./sketchbook/*/*.pde       : contains the processing proglet extensions
#:
#: [1.2] Web site files
#: src/.../doc-files          : contains all javascool and proglets corresponding resources (documentation, icons, ..)
#: www/index.php              : is the web site wrapper (unique http://javascool.gforge.inria.fr entry point)
#: www/documents              : contains extra documents for the web site
#: www/images                 : contains the web site icons
#: www/styles/*.css           : contains the web site CSS styles
#: www/documents              : contains extra documents for the web site and/or not included in the JAR file
#: www/api                    : is the makefile generated javadoc files, deleted on clean: do NOT edit
#: www/javascool*.jar         : is the makefile generated javascool JAR, deleted on clean: do NOT edit
#:
#: [1.3] Lib files:
#: ./lib/run.sh               : simplifies the local use of saxon and javascool (Java main + performs xml2xml translation)
#: ./lib/*.jar                : are used jar for the present development
#: ./lib/js-manifest.mf       : is the makefile generated JAR manifest file: do NOT edit
#: 
#: [1.4] Other files
#: ./tst/*                    : contains local test files
#: ./javascool                : convenient link to ./src/org/javascool
#: ./proglet                  : convenient link to ./src/proglet.
#: ./jdoc                     : convenient Java and JavaScool doc bundle for local use (make jdoc to generate it).
#:
#: [2] Makefile usage.
#:
#::Usage:
#::Development commands:
#:: make run          : Run javascool'v3 from the constructed jar
#:: make doc          : Show the javadoc local bundle
#:: make bty          : Beautify all source files (requires the uncrustify) software
#:: make svn          : Synchronize with the svn repository (update + commit)
#:: make clean        , Clean all intermediate files
#:: make usage        ; Show the makefile usage
#:: make what         ; Show the README information contains in this makefile
#:: make please       ; Show a toolbar to run make actions
#::System commands:
#:: make cmp          : Build the java classes
#:: make htm          : Build the documentation files
#:: make jar          : Build the java jar
#:: make api          : Build the java doc
#:: make aps          : Rebuild source files after a java doc build failure
#:: make wpub         : Publish the present document release on the web site
#:: make jpub         : Publish the present javascool.jar release on the web site
#:: make wref         : Checks the web site broken links
#:: make wusb	      : Copy the files on a USB key of name /media/javascool
#::Useful links:
#:: make jweb         , Opens the JavaScool web site
#:: make wiki         , Opens the JavaScool wiki
#:: make glog         , Connects to google analytics
#:: make gref         , Connects to google dashboard
# note: This makefile usage is fragible !! Take care. (here: ',' means run in background ':' run in external windows ';' do not run in the "please" toolbar)
#:
#: [3] Development tools used.
#:
#: [3.1] Usual linux commands:  cp du echo cat fig2dev firefox grep make php rm rsync sed ssh svn touch unzip
#: Installing these commands:
#: > (apt-get|yum) install bash coreutils firefox grep make openssh perl php-cli rsync sed transfig subversion unzip 
#:  while fig2dev (from transfig)is only used to generate .fig images, firefox
#:  while perl    (from perl)    is only used for lib/linkcheck
#:  while php     (from php-cli) is only used to check the php syntax
#:  while firefox                can be replaced by any other open-source convenient browser
#:
#: [3.2] Java development tools: java javac jarsigner javadoc keytool
#: > (apt-get|yum) java-1.6.0-openjdk-devel java-1.6.0-openjdk-javadoc
#:
#####################################################################################################################################
