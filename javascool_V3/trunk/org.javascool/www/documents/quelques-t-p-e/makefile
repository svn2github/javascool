
xml : $(patsubst %.xml,%.html,$(wildcard *.xml))

%.html : %.xml index.xsl
	java -jar ../../../lib/saxon.jar -o $@ $^
