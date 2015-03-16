dft : wun

wun : ./index.html
	firefox $^

run : ./notrejeu.jar
	java -jar $^

./index.html : makefile ./notrejeu.html $(patsubst %.html,%.htm,$(shell find -name '*.html' \! -name 'index.html')) $(patsubst %.jvs,%.jvs.htm,$(wildcard *.jvs))
	echo '<script>location.replace("apropos.htm");</script>' > $@

./%.html ./%.jar ./%.zip : %.jvs 
#	$(MAKE) -C /user/vthierry/home/Work/culsci/jvs4/work jvs
	../../../../work/lib/jvs2jar.sh $*.jvs
	zip $*.jar -dq clbustos.wav ; zip $*.zip -dq clbustos.wav

./%.htm : assets/header.html.inc ./%.html assets/trailer.html.inc 
	cat $^ > $@

./%.jvs.html : ./%.jvs
	(echo '<pre class="prettyprint linenums">'; cat $^ ; echo '</pre>') > $@

where = /net/servers/www-sop/websites/science-participative/bahhz
wpage = http://www-sop.inria.fr/science-participative/bahhz

pub : assets $(wildcard *.*)
	echo 'rsync $(wpage) ..'
	rsync --rsh='ssh -C' --archive --delete-excluded $^ srv-dias.inria.fr:$(where)
	firefox $(wpage)
