all : $(patsubst %.xml,%.htm,$(wildcard *.xml)) $(patsubst %.jvs,%.htm,$(wildcard *.jvs))

memo-macros.xml : ../../work/src/org/javascool/macros/memo-macros.xml
	cp $^ $@

%.htm : %.xml
	java -jar  ../../work/lib/saxon.jar -o $@ $^ ../../work/src/org/javascool/builder/hdoc2htm.xslt 

%.htm : %.jvs
	java -jar ../../work/lib/java2html.jar -srcfile ./$^ -targetfile ./$@ 
 

