
all : $(patsubst %.xml,%.html,$(wildcard *.xml))

pub : all
	$(MAKE) -C ../../.. wpub

%.html : %.xml index.xsl
	java -jar ../../../lib/saxon.jar -o $@ $^
