#####################################################################################################################################

index.html : doc/header.xml $(patsubst %,applet%.html,$(PROGLETS))	
	(echo '<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></head><body><h2>Proglets disponibles</h2>'; cat doc/header.xml ; echo '<table border="1" width="80%" align="center">') > $@	
	for f in $(PROGLETS) ; do echo "<tr><td valign='middle' width='75%'><h4>$$f</h4></td><td valign='middle' align='center'><h4><a href='applet$$f.html'>TEST</a></h4></td><td valign='middle' align='center'><h4><a href='api/proglet/$$f.html'>DOC</a></h4></td></tr>" ; done >> $@
	echo '</table></body></html>' >> $@

applet%.html : makefile
	echo '<html><body><applet code="proglet.Proglet$$Test.class" width="550" height="550"><param name="proglet" value="$*"/></applet></body></html>' > $@

#####################################################################################################################################

# Lancer la page sur interstices 

interstices : clean
	firefox http://interstices.info/jcms/c_45621/gamletlycee?preview=true

# Qq autre test avec les classes eclipes

EJARS=/home/vthierry/doc/pres/JavasCool/plugins
ECP=$(shell echo '$(wildcard $(EJARS)/*.jar)' | sed 's/ /:/g'):../../../javascool_V2/trunk/javascool_2/org.javascool.core/src:.

test2 : build
	javac -classpath $(ECP) Test.java 
	java -classpath $(ECP) Test

clean2 :
	rm -f `svn status ../../../javascool_V2/trunk | grep '^\?.*\.class' | awk '{print $2}'`

#####################################################################################################################################

