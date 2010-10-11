
# Configuration variables
P_HOME=/usr/java/processing-1.2.1
J_HOME=$(P_HOME)/java
export PATH:=$(J_HOME)/bin:$(PATH)
export CLASSPATH=$(shell echo '$(J_HOME)/lib/tools.jar $(J_HOME)/jre/lib/rt.jar $(wildcard $(P_HOME)/lib/*.jar)' | sed 's/ /:/g')

# Default rule
all : gui jar sgn

# Runs the processing interface GUI

gui :
	@rm -rf */applet
	@cd $(P_HOME) ; java processing.app.Base

# Generates the htm from xml

htm : $(patsubst %.xml,%.htm,$(shell find . -name '*.xml'))

%.htm : %.xml ../src/org/javascool/hdoc2htm.xslt
	@java -jar ../lib/saxon.jar -o $@ $^

# Generates the javascool jar include: processing.zip

jar :
	@rm -f ../lib/processing.zip
	@rm -rf ../tmp ; mkdir ../tmp; for j in */applet/*.jar ; do unzip -qo $$j -d ../tmp ; done ; rm -rf ../tmp/META-INF
	@cd ../tmp ; zip -9qr ../lib/processing.zip *
	@rm -rf ../tmp

# Signs the generated jars and cp in www/applets dir

sgn :
	@for d in * ; do if [ -f $$d/applet/$$d.jar ] ; then $(MAKE) $$d/applet/$$d.key ; fi ; done
	@for d in * ; do if [ -f $$d/applet/index.html ] ; then egrep -v 'Source code:|Built with' < $$d/applet/index.html > $$d/applet/$$d.html ; rm $$d/applet/index.html ; fi ; done
	@mkdir -p ../www/applets ; cp -u */applet/*.jar */applet/*.html ../www/applets ; svn -q add ../www/applets/*.*

vendor=cecile.picard@inria.fr, ou=javascool.gforge.inria.fr, o=inria.fr, c=fr
public-key=hello-world
private-key=mer,d,azof

%.key : %.jar
	@echo "sign $*"
	@rm -f $@ ; mv $*.jar $*-tmp.jar
	@keytool -genkey -alias javascool -validity 3000 -dname cn='$(vendor)' -storepass '$(public-key)' -keypass '$(private-key)' -keystore $*.key
	@jarsigner -storepass '$(public-key)' -keypass '$(private-key)' -keystore $*.key -signedjar $*.jar $*-tmp.jar javascool
	@rm $*-tmp.jar $*.key

# Runs the javasccol interface

j3 :
	$(MAKE) -C .. j3

#################################################################################################################

# Exports the applets without using the GUI (NOT YET AVAILABLE)

APPS = ExplorationSonore CryptageRSA  

export :
	@rm -rf */applet
	@for f in $(APPS) ; do cd $(P_HOME) ; java processing.app.Commander --build --sketch=$(PWD)/$$f --output=$(PWD)/$$f/applet ; done

#See http://wiki.inria.fr/sciencinfolycee/JavaScool:ProcessingLinux for more infos
