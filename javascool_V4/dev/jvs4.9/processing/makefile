#
# Compilation scriptee des proglets
#

PHOME=/usr/java/processing-2.0b8

all : clean build

build : $(patsubst sketchbook/%,outputbook/%,$(wildcard sketchbook/*))

outputbook/% : sketchbook/%
	echo build $*
	mkdir -p $(@D)
	$(PHOME)/processing-java --output=$@ --sketch=$^ --force --build 
	rm -f $@/*.class
#	$(PHOME)/processing-java --output=$@/linux --sketch=$^ --export --bits 64 #--platform linux --bits 64

clean : 
	rm -rf outputbook/*
