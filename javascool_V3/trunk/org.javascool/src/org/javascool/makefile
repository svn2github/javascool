J5=/usr/java/jdk1.5.0_13
CP=/usr/java/jdk1.5.0_13/lib/tools.jar
SX=/home/vthierry/bin/saxon.jar

all :
	@$(J5)/bin/javac -Xlint -cp $(CP) *.java
	java -cp ../..:$(SX):$(CP) org.javascool.Pml /home/vthierry/Work/culsci/javascool/javascool_web_V1/trunk/org.javascool.proglet/doc/about-bml.xml

clean :
	rm *.class
