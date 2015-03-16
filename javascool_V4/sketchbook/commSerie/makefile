#
# Pb de lock sous linux:
#  Once: s/uucp:x:14:uucp,vthierry/uucp:x:14:uucp,vthierry/ s/lock:x:54:/lock:x:54:vthierry/  in /etc/group
#  After reboot : chgrp lock /var/lock ; chmod 775 /var/lock
#

all : ../../documents/sketchbook/commSerie/terminalSerie.jar 
	java -jar $^

export LD_LIBRARY_PATH=../../documents/sketchbook/commSerie/lib/amd64

SRC=Panel.java Functions.java ConvertisseurPanel.java SerialInterfacePanel.java SerialInterface.java

../../documents/sketchbook/commSerie/terminalSerie.jar : makefile RXTXcomm.jar ../../work/dist/javascool-base.jar $(SRC)
	/bin/rm -rf tmp $@ ; mkdir tmp
	javac -cp RXTXcomm.jar:../../work/dist/javascool-base.jar -Xlint -d tmp $(SRC)
	unzip -oqd tmp RXTXcomm.jar
	unzip -oqd tmp ../../work/dist/javascool-base.jar
	echo 'Main-Class: org.javascool.proglets.commSerie/SerialInterfacePanel' > tmp/manifest.mf
	cd tmp ; jar cfm ../$@ manifest.mf com gnu org sun 
	/bin/rm -rf tmp

clean :
	/bin/rm -rf tmp


