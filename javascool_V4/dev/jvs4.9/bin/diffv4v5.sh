#
# Atelier de comparaision de jvs4 et jvs5
#

SRC4=/user/vthierry/home/Work/culsci/jvs4/work/src
SRC5=/user/vthierry/home/Work/culsci/old/jvs5/javascool-framework/src

for f in `find $SRC4 -name '*.java'` ; do g=`echo $f | sed 's/.*\/src//'` ; if [ \! -f "$SRC5$g" ] ; then echo "- $g" ; fi ; done 

for f in `find $SRC5 -name '*.java'` ; do g=`echo $f | sed 's/.*\/src//'` ; if [ \! -f "$SRC4$g" ] ; then echo "+ $g" ; fi ; done 

tmp=tmp-$$ ; /bin/rm -rf $tmp ; mkdir $tmp

unc=`dirname $0`/../../work/lib/uncrustify.cfg
jvs="`dirname $0`/../../work/dist/javascool-builder.jar" 

cat > $tmp/text2text.java <<EOF
import org.javascool.tools.FileManager;
public class text2text { public static void main(String usage[]) {
  String text = FileManager.load(usage[0]);
  text = text.replaceAll("\\t", " ");
  text = text.replaceAll("/[*][^\\t]*[*]/", "");
  text = text.replaceAll("//.*\n", "");
  FileManager.save(usage[0], text);
}}
EOF
javac -cp $jvs $tmp/text2text.java

for f5 in `find $SRC5 -name '*.java'`
do g=`echo $f5 | sed 's/.*\/src//'` ; b=`basename $g` ; f4="$SRC4$g" ; if [ -f "$f4" ] ; then
  cp $f4 $tmp/$b.4 ; java -cp $tmp:$jvs text2text $tmp/$b.4 ; uncrustify -q -c $unc --replace $tmp/$b.4 
  cp $f5 $tmp/$b.5 ; java -cp $tmp:$jvs text2text $tmp/$b.5 ; uncrustify -q -c $unc --replace $tmp/$b.5
  if diff $tmp/$b.4 $tmp/$b.5 > $tmp/$b.45 ; then ok= ; else
    echo "---------------------------------------------------------------------------------------------------------------------------------------"
    echo "<> $g"
    cat $tmp/$b.45
    echo "---------------------------------------------------------------------------------------------------------------------------------------"
  fi
fi ; done 

/bin/rm -rf $tmp


