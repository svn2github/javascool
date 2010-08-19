#!/bin/sh

# Usage of this script
if [ -z "$1" -o "$1" = "-h" -o "$1" = "--help" ] 
then cat <<EOD
Usage: $0 
  JDoc generator
EOD
exit 0
fi

# Defines the jdoc target directory
jdoc=/tmp/javascool/jdoc
#jdoc=/usr/local/javascool/jdoc
echo "                    JDoc Generator                      "
echo "  Welcome to the generator of the bigest Java Doc"
echo ""
echo " --------------------------------------------------------"
echo " | Copyright 2010 INRIA for JavaScool Doc               |"
echo " | Copyright 1993-2010 Oracle for Java API Doc          |"
echo " --------------------------------------------------------"
echo " "
echo " Distributed on GNU GPL V3                      "
echo " For more information : http://javascool.gforge.inria.fr"
echo " ________________________________________________________"
echo " "
echo "> Evaluate and build dependances ..."
mkdir -p $jdoc/api $jdoc/APIJVS
chmod a+w $jdoc/APIJVS # pour pouvoir le faire en tant que user mmm si root
make -f - $jdoc/api/doc.tar.gz $jdoc/base.tar.gz <<EOF

$jdoc/api/doc.tar.gz:
	@echo "You need to build the Java Api Doc :  (This can take some time)"
	@echo "> Dowloading of the Java api doc ..."
	@wget http://www.philien.fr/jdoc/java_api_doc.tar.gz -O \$@ -q
	@echo "> Uncompressing the Java api doc ..."
	@cd $jdoc/api/ ; tar xzf ./doc.tar.gz;
	@echo "The Java api doc is build"

$jdoc/base.tar.gz:
	@echo "You need to build the Doc Base : (This can take some time)"
	@echo "> Dowloading of the doc base ..."
	@wget http://www.philien.fr/jdoc/usefull.tar.gz -O \$@ -q
	@echo "> Uncompressing the doc base ..."
	@cd $jdoc/ ; tar xzf ./base.tar.gz;
	@echo "The doc base is build"
EOF
echo "Good, dependences are ok and we now build the Javascool API Private Doc"
echo "> Generating the JavaScool API Private Doc ..."
if echo $0 | grep '^/' ; then jroot="$0" ; else jroot="`pwd`/$0" ; fi ; jroot=`echo $jroot | sed 's/\/lib\/jdoc.sh$//' | sed 's/\/lib\/\.\/jdoc.sh$//'`
make -f - $jdoc/APIJVS <<EOF
SRC = $(shell find $jroot/src -name '*.java')

$jdoc/APIJVS : \$(SRC)
	@echo "> Coping doc-file to the JavaScool API Doc ..."
	@$(MAKE) -C $jroot api
	@cp -r www/api/* \$@
	@ echo "> Generating the JavaScool API Private Doc ..."
	@javadoc -quiet -private -nohelp -nodeprecated -notree -noindex -author -charset "UTF-8" -use -linkoffline ../api/ $jdoc/api/ -windowtitle "Java's Cool v3" -d $jdoc/APIJVS \$(SRC)
EOF
echo "Fine, JDoc is generated. Its size is `du -s -B 1048576 $jdoc | awk '{print $$1}'`Mbytes"

echo "Let us open JDoc, open in your browser: Read The Fine Manuel (RTFM)"
firefox $jdoc/index.html 
