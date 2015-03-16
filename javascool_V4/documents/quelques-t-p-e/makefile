
all : $(patsubst %.xml,%.html,$(wildcard *.xml))

%.html : %.xml index.xsl
	java -jar  ../../work/lib/saxon.jar -o $@ $^
