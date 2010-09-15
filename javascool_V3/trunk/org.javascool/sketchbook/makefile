
# Configuration variables
P_HOME=/usr/java/processing-1.2.1
J_HOME=$(P_HOME)/java
export PATH:=$(J_HOME)/bin:$(PATH)
export CLASSPATH=$(shell echo '$(J_HOME)/lib/tools.jar $(J_HOME)/jre/lib/rt.jar $(wildcard $(P_HOME)/lib/*.jar)' | sed 's/ /:/g')

# Default rule
all : gui jar

# Runs the processing interface

gui :
	@rm -rf */applet
	@cd $(P_HOME) ; java processing.app.Base

# Generates the javascool jar include

jar :
	@rm -f ../lib/processing.zip
	@rm -rf tmp ; mkdir tmp; for j in */applet/*.jar ; do unzip -qo $$j -d tmp ; done ; rm -rf tmp/META-INF
	@cd tmp ; zip -9qr ../../lib/processing.zip *
	rm -rf tmp */applet

# Runs the javasccol interface
j3 :
	$(MAKE) -C .. j3

#################################################################################################################

# Tests the applet export

APP = ExplorationSonore

tst :
	echo 'grant { permission java.security.AllPermission; };' > applet.policy
	appletviewer -J-Djava.security.policy=./applet.policy $(APP)/applet/index.html
	rm applet.policy

# Exports the applet (NOT YET AVAILABLE)

APPS = CryptageRSA  ExplorationSonore

export :
	rm -rf */applet
	for f in $(APPS) ; do cd $(P_HOME) ; java processing.app.Commander --build --sketch=$(PWD)/$$f --output=$(PWD)/$$f/applet ; done
