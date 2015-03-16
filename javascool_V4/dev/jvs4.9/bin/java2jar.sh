
#
# Effectue la compilation de sources java dans un jar
#

# Analyse de la ligne de commande
cp="."
for f in $* ; do e="`echo $f | sed 's/.*\.//'`"
 case "$f" in 
  -static ) static=1;;
  -main=* ) main="`echo $f | sed 's/-main=//'`";;
  * ) 
    if [ -z "$jar" ] ; then 
      if [ "$e" = "jar" ] ; then jar="`pwd`/$f" ; else echo "Invalid extension for the target jar-file $f" ; exit -1; fi
    else 
      if [ \! -f "$f" ] ; then echo "File not found $f"; exit -1 ; fi
       case "$e" in
         java ) src="$src $f";;
         jar  ) cp="$cp:$f" ; jrc="$jrc $f";;
         *    ) res="$res $f";;
        esac
    fi;;
  esac
done

# Usage 
if [ -z "$jar" -o -a "$src" ] ; then cat <<EOF
Usage : $0 [-static] [-main=<nom-du-main>] <jar-file> <java-files|jar-files> 
 Effectue la compilation, dans une jarre, de sources java complétés de jar externes.
 -static si les jars externes sont recopiées dans la jarre à la produire (sinon elles ne sont utilisées que dans le class-path)
 -main=<nom-du-main> si un manifeste est à ajouté avec le main de lancement
EOF
exit -1
fi

# Répertoire de travail
d="tmp-$$" ; /bin/rm -rf $d ; mkdir $d

# Inclut les sources et fichiers de ressources
for f in $res $src ; do t=$d/`echo $f | sed 's/.*src[^\/]*\///'`; mkdir -p `dirname $t` ; cp $f $t ;  done

# Inclut les jarres du classpath en mode static
if [ "$static" = 1 ] ; then for j in $jrc ; do unzip -d $d -oq $j ; done ; fi

# Compilation
rm -f $jar
if javac -Xlint:unchecked -d $d -cp $cp $src
then pushd $d > /dev/null
  if [ \! -z "$main" ]
  then (echo "Manifest-Version: 1.0" ; echo "Main-Class: $main" ; echo "Implementation-URL: http://javascool.gforge.inria.fr") > "META-INF/MANIFEST.MF"
  fi
  jar cfM $jar .
popd > /dev/null ; fi

/bin/rm -rf $d


