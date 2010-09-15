
all : gui jar

P_HOME=/usr/java/processing-1.2.1
J_HOME=$(P_HOME)/java

export PATH:=$(J_HOME)/bin:$(PATH)
export CLASSPATH=$(shell echo '$(J_HOME)/lib/tools.jar $(J_HOME)/jre/lib/rt.jar $(wildcard $(P_HOME)/lib/*.jar)' | sed 's/ /:/g')

gui :
	rm -rf applet
	cd $(P_HOME) ; java processing.app.Base
	rm -f applet/*.java

tst :
	echo 'grant { permission java.security.AllPermission; };' > applet.policy
	appletviewer -J-Djava.security.policy=./applet.policy applet/index.html
	rm applet.policy

jar :
	cd applet ; rm -rf tmp ; mkdir tmp ; for f in *.jar ; do unzip -qo $$f -d tmp ; done ; rm -rf tmp/META-INF
	cd applet/tmp ; zip -9qr ../../lib/processing.zip *
	rm -rf applet

j3 :
	$(MAKE) -C ../.. j3

run :
	rm -rf applet
	cd $(P_HOME) ; java processing.app.Commander --build --sketch=$(PWD) --output=$(PWD)/applet
