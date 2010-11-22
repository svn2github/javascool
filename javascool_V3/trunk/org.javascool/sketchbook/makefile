#####################################################################################################################################
##  Cecile.Picard@sophia.inria.fr and Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved.                  ##
#####################################################################################################################################

# Publishs the exported applets on the web site

publish : 
# export the applet to the www directory
	@if [ -f */applet ] ; then for d in */applet ; do mv $$d ../www/sketchbook/`echo $$d | sed 's/\/applet//'` ; done ; fi
# sign the exported jar
	@cd ../www/sketchbook ; for d in * ; do if [ -f $$d/$$d.jar ] ; then $(MAKE) -f ../../sketchbook/makefile $$d/$$d.key ; fi ; done
# copy the documentation files
	@cd ../www/sketchbook ; for d in * ; do if [ -f $$d/$$d.jar ] ; then rsync --archive --exclude .svn/ ../../sketchbook/$$d . ; fi ; done
# generates an index
	@cd ../www/sketchbook ; (echo "<h1>sketchbook files</h1>"; for f in */*.htm* ; do echo "<p><a href='$$f'>$$f</a></p>"; done) > index.html
# add new files to svn
	@cd ../www/sketchbook ; svn -q add */*.*

# Generates the javasccol processing.zip 

JARS=/usr/java/processing-1.2.1/libraries/opengl/library/jogl.jar /usr/java/processing-1.2.1/libraries/opengl/library/gluegen-rt.jar

jar : ../lib/processing.zip

../lib/processing.zip : $(wildcard ../www/sketchbook/*/*.jar)
	@rm -rf $@ tmp ; mkdir tmp 
	@for j in $(JARS) $^ ; do unzip -qo $$j -d tmp ; done ; rm -rf tmp/META-INF
	@cd tmp ; zip -9qr ../$@ *
	@rm -rf tmp

# Generates the htm from xml

htm : $(patsubst %.xml,%.htm,$(shell find . -name '*.xml'))

%.htm : %.xml ../src/org/javascool/hdoc2htm.xslt
	@java -jar ../lib/saxon.jar -o $@ $^

# Signs a jar

vendor=cecile.picard@inria.fr, ou=javascool.gforge.inria.fr, o=inria.fr, c=fr
public-key=hello-world
private-key=mer,d,azof

%.key : %.jar
	@echo "sign $*"
	@rm -f $@ ; mv $*.jar $*-tmp.jar
	@keytool -genkey -alias javascool -validity 3000 -dname cn='$(vendor)' -storepass '$(public-key)' -keypass '$(private-key)' -keystore $*.key
	@jarsigner -storepass '$(public-key)' -keypass '$(private-key)' -keystore $*.key -signedjar $*.jar $*-tmp.jar javascool
	@rm $*-tmp.jar

#################################################################################################################

# Runs the processing interface GUI under Linux

P_HOME=/usr/java/processing-1.2.1
J_HOME=$(P_HOME)/java
export PATH:=$(J_HOME)/bin:$(PATH)
export CLASSPATH=$(shell echo '$(J_HOME)/lib/tools.jar $(J_HOME)/jre/lib/rt.jar $(wildcard $(P_HOME)/lib/*.jar)' | sed 's/ /:/g')

gui :
	@rm -rf */applet
	@cd $(P_HOME) ; java processing.app.Base

#################################################################################################################

# Exports the applets without using the GUI (NOT YET AVAILABLE)

APPS = ExplorationSonore CryptageRSA  

export :
	@rm -rf */applet
	@for f in $(APPS) ; do cd $(P_HOME) ; java processing.app.Commander --build --sketch=$(PWD)/$$f --output=$(PWD)/$$f/applet ; done

#See http://wiki.inria.fr/sciencinfolycee/JavaScool:ProcessingLinux for more infos
