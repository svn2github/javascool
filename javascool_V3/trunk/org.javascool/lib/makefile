
ALL = $(patsubst %.xml,%.dml,$(shell find ../src -name '*.xml')) $(patsubst %.dml,%.htm,$(shell find .. -name '*.dml'))

all :
	$(MAKE) $(ALL)

org2xml.xsl : ../src/org/javascool/htm2hml.xslt
	@touch $@

dml2htm.xsl : ../src/org/javascool/hml2htm.xslt
	@touch $@

%.dml : %.xml org2xml.xsl
	@echo "org2xml $*"
	@java -jar saxon.jar -o $@ $^

%.htm : %.dml dml2htm.xsl
	@echo "dml2htm $*"
	@java -jar saxon.jar -o $@ $^
