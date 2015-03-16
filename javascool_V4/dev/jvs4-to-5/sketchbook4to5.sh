
#
# Effectue la convertion des proglets jvs4 en jvs5
#   ce script est un mecanisme temporaire et local à l'environnement de vthierry, il n'a pas vocation à être utilisé après le basculement v4->v5
#

# Positionnement à la racine du svn
cd /home/vthierry/Work/culsci/jvs

# Définit le répertoire source et cibles et les proglets à convertir

srcDir="`pwd`/sketchbook"
dstDir="`pwd`/jvs4-to-5/sketchbook"
prcDir="`pwd`/jvs4-to-5/processing"
srcProglets="`find $srcDir -mindepth 1 -maxdepth 1 -type d -exec basename {} \; | egrep -v 'jeux2D|sampleCode|grafikformate|\.build'`"

mkdir -p $prcDir/{libraries,tools,modes,sketchbook}

echo "Conversion de «" $srcProglets "» de '$srcDir' à '$dstDir'"

# Référence les librairies à utiliser
jvs="`pwd`/work/dist/javascool-builder.jar" 
jsx="`pwd`/work/lib/saxon.jar" 
xslt="`pwd`/work/jsrc/builder/hdoc2htm.xslt" 

pushd work > /dev/null ; ant javascool-builder.jar ; popd > /dev/null

# proglet.pml -> proglet.json
rm -rf /tmp/pml2json ; mkdir /tmp/pml2json
cat > /tmp/pml2json/pml2json.java <<EOF
import org.javascool.tools.Pml;
public class pml2json { public static void main(String usage[]) {
  Pml pml = new Pml().load(usage[0]);
  pml.set("name", usage[2]);
  String who = pml.getString("author");
  pml.set("author", who.replaceFirst("<[^>]*>", "").trim());
  pml.set("email", who.replaceFirst("[^<]*<([^>]*)>.*", "\$1").trim());
  pml.save(usage[1], "json");
}}
EOF
pushd /tmp/pml2json > /dev/null ; javac -cp $jvs pml2json.java ; popd > /dev/null

# Boucle sur les proglets

for p in $srcProglets
do s="$srcDir/$p" ; d="$dstDir/$p" ; echo "Translate $p"

  # Copie en miroir des fichiers
  /bin/rm -rf $d ; mkdir -p `dirname $d` ; svn -q export --force $s $d
  
  # Translation des fichiers de ressource
  pushd $d > /dev/null
  if [ -f proglet.pml ] ; then java -cp $jvs:/tmp/pml2json pml2json proglet.pml proglet.json $p ; rm proglet.pml ; else echo "Erreur : y'a pas de proglet.pml !" ; fi
  if [ -f completion.xml ] ; then java -cp $jvs org.javascool.tools.Pml completion.xml completion.json ; rm completion.xml ; fi
  # Translation des fichiers *.html
  if ls *.xml > /dev/null ; then for f in *.xml ; do n=`echo $f | sed s'/.xml$//'` ; java -jar $jsx -o $n.html $n.xml $xslt ; rm -f $f ; done ; fi
    for f in *.html ; do cp $f $f~ ; cat $f~ |\
	sed 's/http:..javascool.gforge.inria.fr.[?]page=proglets&amp;action=show&amp;id=\([^&]*\)&amp;helpFile=\([^"]*\)/http:\/\/javascool.github.com\/wproglets\/javascool-proglet-\1-html\/\2/g' |\
	sed 's/http:..javascool.gforge.inria.fr.index\.php[?]page=api/http:\/\/javascool.github.com\/wproglets\/javascool-core-api/' |\
	sed 's/.htm"/.html"/g' > $f ; done
  #
  if [ -d applet ] ; then
    # Basculement des fichiers processing 
    t=../../processing/sketchbook/$p
    mkdir -p $t
    rm -rf applet $p.jar
    (echo "package org.javascool.proglets.$p;" ; sed "s/${p}Functions/Functions/" < ${p}Functions.java) > Functions.java ; rm ${p}Functions.java
    (echo "package org.javascool.proglets.$p;" ; echo "public class Panel extends $p {}") > Panel.java
    #if [ $p = 'cryptageRSA' ] ; then echo "public class BigInteger extends java.math.BigInteger {}") > BigInteger.java ; fi
    cp *.* $t
    cd .. ; rm -rf $p
  else
    # Manipulation des fichiers java
    mkdir -p $d/java
    mv $d/*.java $d/java
  fi
  rm -f *~
  popd > /dev/null

done

svn status .
