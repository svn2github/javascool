
f=sujet-hello-world.new

one : $(f).xml $(f).htm
	firefox $(f).htm
#	firefox $(f).xml
	firefox http://javascool.gforge.inria.fr/proglet/doc/sujet-hello-world.htm

all : $(patsubst %.xml,%.new.htm,$(shell find ../src -name '*.xml.org'))

clean :
	rm -f `find ../src -name '*.new.xml' -o -name '*.xml.org' -o -name '*.new.htm'`

%.new.xml : %.xml org2xml.xsl
	@echo $^
	@java -jar saxon.jar -o $@ $^

%.new.htm : %.new.xml hml2htm.xslt
	@echo $^
	@java -jar saxon.jar -o $@ $^
