if [ "$#" = 0 -o "$1" = -h -o "$1" = -help ] ; then cat <<EOF
 Usage : $0 <program>.js
   Construit un jar à partir du fichier et de tous les fichiers du répertoire courant:
    ./<program>.jar pour lancer l´application
    ./<program>.html pour lancer l´applet
    ./<program>.zip qui contient les sources
EOF
exit
fi

# Archive de javascool
jvs_jar=`dirname $0`/../dist/javascool-proglets.jar

# Positionnement dans le répertoire de travail
jvs_name=`basename $1 | sed 's/\.jvs$//'` ; cd `dirname $1`

echo "build `pwd` / $jvs_name.{zip|jar|html}"

# Nettoyage préalable
/bin/rm -rf *~ tmp-$$ ./$jvs_name.jar ./$jvs_name.html ./$jvs_name.zip ; mkdir tmp-$$

# Création de l'archive des sources
zip -9q ./$jvs_name.zip *.*

# Compilation et construction du jar
if java -cp $jvs_jar org.javascool.core.Jvs2Java org.javascool.proglets.javaProg $jvs_name.jvs tmp-$$/JvsToJavaTranslated1.java
then 
  echo 'public class Main { public static void main(String[] usage) { org.javascool.widgets.PanelApplet.main(new String[] { "org.javascool.proglets.javaProg.Panel", "JvsToJavaTranslated1" }); }}' > tmp-$$/Main.java
  if javac -cp $jvs_jar -Xlint -d tmp-$$ tmp-$$/*.java
  then 
    unzip -oqd tmp-$$ $jvs_jar
    for f in *.* ; do if [ "$f" \!= $jvs_name.zip ] ; then cp $f tmp-$$ ; fi ; done
    echo 'Main-Class: Main' > tmp-$$/manifest.mf
    cd tmp-$$ ; jar cfm ../$jvs_name.jar manifest.mf com gnu org sun *.* ; cd ..
    echo "<html><head><title>$jvs_name</title></head><body bgcolor='#eeeeee'><center><applet width='800' height='600' code='org.javascool.widgets.PanelApplet' archive='./$jvs_name.jar'><param name='panel' value='org.javascool.proglets.javaProg.Panel'/><param name='program' value='JvsToJavaTranslated1'/><param name='manual-start' value='false'/><pre>Impossible de lancer $jvs_name : Java n'est pas installé ou mal configuré</pre></applet></center></body></html>" > ./$jvs_name.html
  fi
fi

/bin/rm -rf tmp-$$


