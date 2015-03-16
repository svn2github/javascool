all : $(patsubst %.tex,%.pdf,$(wildcard *.tex)) ls.html
	firefox ls.html

%.pdf : %.tex
	-pdflatex $*

ls.html : $(shell ls -1 | egrep -v ls.html)
	@echo '<h4>David ISN extra files</h4><ul>' > $@
	@ls -1 | egrep -v '(ls.html|makefile)' | sed 's/\(.*\)/<li><a href="\1">\1<\/a><\/li>/g' >> $@
	@echo '</ul>' >> $@
