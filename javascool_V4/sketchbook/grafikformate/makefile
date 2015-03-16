#
# French version of http://www.swisseduc.ch/informatik/anwendungen/grafikformate_applet/applet.html
#
# - ISO8859-1 files have been converted to UTF-8
# - icons acces have been patched to be read in the Jar file
# - change variable from enum to enumeration since enum is now a Java keyword
# 
# * The French version is available at https://wiki.inria.fr/sciencinfolycee/Javascoolgrafikformate
# 

all :
	javac *.java
	rm -f grafikformate.jar; jar cf grafikformate.jar *.class *.java makefile icons/*.* ; rm -f *.class
	appletviewer grafikformate.html

pub :
	firefox https://wiki.inria.fr/sciencinfolycee/Javascoolgrafikformate

#cnv :
#	for f in *.java ; do mv $$f $$f.save ; iconv -f ISO8859-1 -t UTF-8 $$f.save > $$f ; done

