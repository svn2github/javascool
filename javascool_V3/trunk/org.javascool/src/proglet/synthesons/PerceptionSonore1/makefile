
P_HOME=/usr/java/processing-1.2.1
J_HOME=$(P_HOME)/java

export PATH:=$(J_HOME)/bin:$(PATH)
export CLASSPATH=$(shell echo '$(J_HOME)/lib/tools.jar $(J_HOME)/jre/lib/rt.jar $(wildcard $(P_HOME)/lib/*.jar)' | sed 's/ /:/g')

run :
	cd $(P_HOME) ; java processing.app.Commander --build --sketch=$(PWD) --output=$(PWD)/applet

pre :
	cd $(P_HOME) ; java processing.app.Base

tst :
	echo 'grant { permission java.security.AllPermission; };' > applet.policy
	appletviewer -J-Djava.security.policy=./applet.policy applet/index.html
	rm applet.policy
