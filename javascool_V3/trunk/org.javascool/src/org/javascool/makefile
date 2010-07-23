J5=/usr/java/jdk1.5.0_13
CP=/usr/java/jdk1.5.0_13/lib/tools.jar
SX=/home/vthierry/bin/saxon.jar

all :
	@$(J5)/bin/javac -Xlint -cp $(CP) *.java
	java -cp ../..:$(SX):$(CP) org.javascool.Text ../../../old/doc/about-bml.xml

clean :
	rm *.class
