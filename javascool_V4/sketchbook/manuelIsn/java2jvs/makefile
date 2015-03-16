#
# Mécanisme de conversion des sources Isn Java en code Jvs
# 

# Répertoire Isn de Gilles Dowek
ISN_SOURCES = /home/vthierry/Desktop/Isn

all : conv clean 

conv : cmp
	rm -f ../*.jvs
	if [ -f $(ISN_SOURCES)/Isn.java ] ; then mv -f $(ISN_SOURCES)/Isn.java $(ISN_SOURCES)/Isn.java.txt ; fi
	$(MAKE) $(patsubst $(ISN_SOURCES)/%.java,../%.jvs,$(wildcard $(ISN_SOURCES)/*.java))
	cd ../.. ; rm -f manuelIsn/IsnJvsExamples.zip ; zip -9qr manuelIsn/IsnJvsExamples.zip manuelIsn/*.jvs

# Conversion d'un fichier java -> jvs

../%.jvs : $(ISN_SOURCES)/%.java
	echo jvs2java $*
	uncrustify -q -c ../../../work/lib/uncrustify.cfg --replace $^
	java -cp ../../../work/dist/javascool-proglets.jar:. Java2Jvs $^ $@
	uncrustify -q -c ../../../work/lib/uncrustify.cfg --replace $@
	java -cp ../../../work/dist/javascool-proglets.jar org.javascool.core.Jvs2Java org.javascool.proglets.manuelIsn $@ JvsToJavaTranslated1.java
	javac -cp ../../../work/dist/javascool-proglets.jar JvsToJavaTranslated1.java
	rm JvsToJavaTranslated1.java

# Compilation du translateur

cmp :
	cp Java2Jvs.java.txt Java2Jvs.java
	javac -cp ../../../work/dist/javascool-proglets.jar Java2Jvs.java
	rm Java2Jvs.java

clean :
	rm -rf *.class Java2Jvs.java JvsToJavaTranslated1.java
	find .. -name '*~' -exec rm {} \;

# Ajout de la licence

copyright :
	cd .. ; for f in *.java *.jvs ; do grep -v "CeCILL-B" < $$f > $$f~ ; (echo "//  Ces programmes sont sous licence CeCILL-B V1." ; cat $$f~) > $$f ; done


