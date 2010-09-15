
# Configuration variables
P_HOME=/usr/java/processing-1.2.1
J_HOME=$(P_HOME)/java
export PATH:=$(J_HOME)/bin:$(PATH)
export CLASSPATH=$(shell echo '$(J_HOME)/lib/tools.jar $(J_HOME)/jre/lib/rt.jar $(wildcard $(P_HOME)/lib/*.jar)' | sed 's/ /:/g')

# Default rule
all : gui jar

# Runs the processing interface GUI

gui :
	@rm -rf */applet
	@cd $(P_HOME) ; java processing.app.Base

# Generates the javascool jar include: processing.zip

jar :
	@rm -f ../lib/processing.zip
	@rm -rf ../tmp ; mkdir ../tmp; for j in */applet/*.jar ; do unzip -qo $$j -d ../tmp ; done ; rm -rf ../tmp/META-INF
	@cd ../tmp ; zip -9qr ../lib/processing.zip *
	@rm -rf ../tmp

# Runs the javasccol interface

j3 :
	$(MAKE) -C .. j3

#################################################################################################################

# Tests the applet export

APP = ExplorationSonore

tst :
	@echo 'grant { permission java.security.AllPermission; };' > applet.policy
	@appletviewer -J-Djava.security.policy=./applet.policy $(APP)/applet/index.html
	@rm applet.policy

# Exports the applets without using the GUI (NOT YET AVAILABLE)

APPS = CryptageRSA  ExplorationSonore

export :
	@rm -rf */applet
	@for f in $(APPS) ; do cd $(P_HOME) ; java processing.app.Commander --build --sketch=$(PWD)/$$f --output=$(PWD)/$$f/applet ; done

# Notes: as root we have to :
# cp /usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0/jre/lib/i386/libpulse-java.so  /usr/java/jdk1.6.0_21/jre/lib/i386/
# cp /usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0/jre/lib/ext/pulse-java.jar /usr/java/jdk1.6.0_21/
