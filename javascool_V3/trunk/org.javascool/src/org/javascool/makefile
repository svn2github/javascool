J5=/usr/java/jdk1.5.0_13
CP=/usr/java/jdk1.5.0_13/lib/tools.jar

all :
	@$(J5)/bin/javac -Xlint -cp $(CP) *.java
	java -cp ../..:$(CP) org.javascool.Main

clean :
	rm *.class
