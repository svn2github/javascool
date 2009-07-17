
all : $(patsubst %.xml,%.htm,$(wildcard about-*.xml sujet-*.xml correction-*.xml))

XML2XML = /usr/java/jdk1.5.0_13/bin/java -classpath /usr/java/saxon6-5-5/saxon.jar com.icl.saxon.StyleSheet

about-%.htm : about-%.xml
	(echo '<html><body>[<a href="../index.html"><img src="../img/home.png"/></a>]<hr/>' ; cat $^ ; echo '</body></html>') > $@

sujet-%.htm : sujet-%.xml
	$(XML2XML) -o $@ $^ ../../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_sujet.xsl

correction-%.htm : correction-%.xml
	$(XML2XML) -o $@ $^ ../../../../javascool_V2/trunk/javascool_2/org.javascool.help.tutorials/doc/style_correction.xsl
