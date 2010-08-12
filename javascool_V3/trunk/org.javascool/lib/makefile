
f=about-proglet.new

one : 
	rm -f $(f).xml $(f).htm
	$(MAKE) $(f).xml $(f).htm
#	firefox $(f).xml
	firefox http://javascool.gforge.inria.fr/proglet/doc/sujet-hello-world.htm
	firefox $(f).htm

all : $(patsubst %.xml,%.new.htm,$(shell find ../src -name '*.xml.org'))

clean :
	rm -f `find ../src -name '*.new.xml' -o -name '*.xml.org' -o -name '*.new.htm'`

%.new.xml : %.xml org2xml.xsl
	@echo org2xml
	@java -jar saxon.jar -o $@ $^

%.new.htm : %.new.xml dml2htm.xsl
	@echo dml2htm
	@java -jar saxon.jar -o $@ $^
