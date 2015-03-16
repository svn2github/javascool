
# vthierry's ongoing tasks, . . .

dft : tst1

export JAVA_HOME=/usr/java/jdk1.6.0_25
#export JAVA_HOME=/usr/java/jdk1.7.0_71
#export JAVA_HOME=/usr/java/jdk1.8.0_31

export PATH := $(JAVA_HOME)/bin:$(PATH)

export JRE_HOME=/usr/java/jre1.7.0_60
#export JRE_HOME=/usr/java/jre1.8.0_31

www :
	cd .. ; ant web

jvs :
	ant javascool-proglets.jar

tst1 : 
	echo $(PATH)
	which java
	java -version
	ant clean
	ant javascool-proglets.jar
	$(JRE_HOME)/bin/java -jar dist/javascool-proglets.jar

tst2 :
	ant clean
	cd ../web ; ant build
	ls -l ./dist

clean :
	ant clean
	rm -f tst*.jvs
	svn status

#################################################

enc :
	for f in `find ../sketchbook/plurialgo \( -name '*.java' \)` ;\
	do mv $$f $$f~ ; iconv -f ISO8859-1 -t UTF-8 $$f~ > $$f ;\
	done

ant :
	ant prun

nbe :
	sh -c '/usr/java/netbeans-7.3/bin/netbeans &'

jdoc :
	ant javadoc ; firefox ./dist/javadoc/overview-summary.html

sos8 : # À utiliser avec la JDK 7 ou JDK 8
	ant javascool-proglets.jar
	rsync --rsh='ssh -C' -rl ../work/dist/javascool-proglets.jar scm.gforge.inria.fr:/home/groups/javascool/htdocs/v4/javascool-proglets-8.jar
	ant javascool-builder.jar
	rsync --rsh='ssh -C' -rl ../work/dist/javascool-builder.jar scm.gforge.inria.fr:/home/groups/javascool/htdocs/v4/javascool-builder-8.jar

jbl :
	ant javascool-builder.jar ; cd ../sketchbook ; java -jar ../work/dist/javascool-builder.jar

jpr : 
	ant javascool-proglets.jar ; javac -cp dist/javascool-proglets.jar Main.java ; java -cp .:dist/javascool-proglets.jar Main

pgl :
	cd ../web ; ant build
	$(JRE_HOME)/bin/java -jar ../web/proglets/codagePixels/javascool-proglet-codagePixels.jar 

prg : 
	ant javascool-base.jar ; java -cp dist/javascool-base.jar org.javascool.gui2.TextFilesEditor;

php : 
	cd ../web ; ant local

usb :
	ant javascool-proglets.jar
	cd dist/javascool-proglets.jar lib/javascool.bat /home/vthierry/Desktop

###################################

