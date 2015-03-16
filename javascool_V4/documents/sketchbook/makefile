all : $(patsubst %.xml,%.htm,$(wildcard */*.xml))

%.htm : %.xml
	java -jar  ../../work/lib/saxon.jar -o $@ $^ ../../work/src/org/javascool/builder/hdoc2htm.xslt 

%/activites-unplug.xml : ../../sketchbook/%/tutoriel.xml
	cp $^ $@
