#!/bin/sh

# Usage of this script
if [ -z "$1" -o "$1" = "-h" -o "$1" = "--help" ] 
then cat <<EOD
Usage: $0 <main> [arguments]
 Runs one of the org.javascool main 
Usage: $0 sax -o <out-file> <xml-file> <xsl-file>
 Runs a XML to XML translation usng a XSL
EOD
exit 0
fi

# Retrieve javascool root directory and main class
if echo $0 | grep '^/' ; then root="$0" ; else root="`pwd`/$0" ; fi ; root=`echo $root | sed 's/\/lib\/run.sh$//' | sed 's/\/lib\/\.\/run.sh$//'`
main=`echo $1 | sed 's/^org\.javascool\.//'` ; shift

# Running the saxon translator
if [ "$main" = sax ] ; then java -jar $root/lib/saxon.jar $* ; exit $? ; fi

# Running the class
make -s -C $root cmp ; java -cp $root/src:$root/lib/tools.jar:$root/lib/saxon.jar org.javascool.$main $*


