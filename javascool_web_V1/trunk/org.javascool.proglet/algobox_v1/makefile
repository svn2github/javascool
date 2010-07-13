all :
	find . -name '*.class' -exec /bin/rm {} \;
	rm -rf bin/proglet
	javac -d bin src/proglet/*.java
	java -classpath bin proglet/Arbre4
