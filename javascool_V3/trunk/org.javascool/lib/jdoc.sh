#!/bin/sh

# Usage of this script
if [ "$1" = "-h" -o "$1" = "--help" ] 
then cat <<EOD
Usage: $0 [JDOCLOCATION]
  JDoc generator
EOD
exit 0
fi

# Defines the jdoc target directory (writable by all tu update the APIJVS)
if [ -z "$1" ] ; then jdoc=/tmp/javascool/jdoc ; else jdoc=$1 ; fi
mkdir -p $jdoc/api $jdoc/APIJVS
chmod a+w $jdoc/APIJVS

echo "             JDoc Generator and Viewer                     "
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
echo "> jdoc is in $jdoc"
echo "> Evaluate and build dependances ..."
make -s -f - $jdoc/api/doc.tar.gz $jdoc/base.tar.gz <<EOF

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
make -s -C $jroot JDOC=$jdoc api
echo "Fine, JDoc is generated. Its size is `du -s -B 1048576 $jdoc | awk '{print $1}'`Mbytes"

echo "Let us open JDoc, open in your browser: Read The Fine Manuel (RTFM)"
firefox $jdoc/index.html 
