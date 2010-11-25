#!/bin/sh

# Used to use sun jdk instead of open jdk
export PATH=/usr/java/jdk1.6.0_22/bin:$PATH

# Usage of this script
if [ -z "$1" -o "$1" = "-h" -o "$1" = "--help" ] 
then cat <<EOD
Usage: $0 <main> [arguments]
 Runs one of the main (ex: org.javascool.Main)
Usage: $0 sax -o <out-file> <xml-file> <xsl-file>
 Runs a XML to XML translation usng a XSL
- This scripts simplifies the local use of saxon and javascool
EOD
exit 0
fi

# Retrieve javascool root directory and main class
if echo $0 | grep '^/' ; then root="$0" ; else root="`pwd`/$0" ; fi ; root=`echo $root | sed 's/\/lib\/run.sh$//' | sed 's/\/lib\/\.\/run.sh$//'`
main="$1" ; shift

# Running the saxon translator
if [ "$main" = sax ] ; then java -jar $root/lib/saxon.jar $* ; exit $? ; fi

# Running the class after compilation
make -s -f - <<EOF
all :
	\$(MAKE) -C $root jar
	java -cp $root/www/javascool.jar $main $*
EOF



