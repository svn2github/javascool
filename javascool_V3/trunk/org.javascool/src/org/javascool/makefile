
CLASS_PATH=../..:../../../lib/tools.jar:../../../lib/saxon.jar

all :
	@javac -Xlint -cp $(CLASS_PATH) *.java
	@java -cp $(CLASS_PATH) org.javascool.Text ../../../old/doc/about-bml.xml

clean :
	rm *.class
