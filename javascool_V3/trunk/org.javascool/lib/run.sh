#!/bin/sh

# Used to use sun jdk instead of open jdk
export PATH=/usr/java/jdk1.6.0_22/bin:$PATH

# Usage of this script
if [ -z "$1" -o "$1" = "-h" -o "$1" = "--help" ] 
then cat <<EOD
Usage: $0 [-rejar] <main> [arguments]
 Runs one of the main (ex: org.javascool.JsMain)
  -rejar Recompile the jar before running.

Usage: $0 sax -o <out-file> <xml-file> <xsl-file>
 Runs a XML to XML translation using a XSL

- This scripts simplifies the local use of saxon and javascool
EOD
exit 0
fi

# Retrieve javascool root directory and main class
if echo $0 | grep '^/' ; then root="$0" ; else root="`pwd`/$0" ; fi ; root=`echo $root | sed 's/\/lib\/run.sh$//' | sed 's/\/lib\/\.\/run.sh$//'`

# Running the saxon translator
if [ "$1" = sax ] ; then shift ; java -jar $root/lib/saxon.jar $* ; exit $? ; fi

# Recompile if required
if [ "$1" = -rejar ] ; then shift ; make -C $root jar ; if [ $? != 0 ] ; then exit $? ; fi ; fi

# Running the class fromn the main jar
JAR=`grep 'JAR *= *' $root/makefile | sed 's/.*= *\([^ ]*\) */\1/'`
if [ -z "$1" ] 
then java -jar $root/www/javascool$JAR.jar
else java -cp $root/www/javascool$JAR.jar $*
fi


