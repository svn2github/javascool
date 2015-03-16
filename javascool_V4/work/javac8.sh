javac -g \
  -cp ./lib/autocomplete.jar:./lib/RSyntaxTextArea.jar:./lib/json.jar:./lib/jvs2html.jar:./lib/javadoc.jar:./lib/jarsigner.jar \
  -d ./build/classes \
  `find src -name '*.java'`


