############################################################################################################################
#		Makefile for the Java's Cool Web Site
############################################################################################################################
## Usage :
##  make sync          : génère les pages dynamiques du site
##  make push          : effectue une mise à jour pour voir le site en ligne

GIT_COMMIT_MESSAGE ?= mise à jour du site Web

push : sync
	git commit -a -m '$(GIT_COMMIT_MESSAGE)' ; git push -q
#	firefox http://javascool.org

sync :
	javac -cp wproglets/javascool-core.jar _layouts/html_builder.java 
	java  -cp wproglets/javascool-core.jar:_layouts html_builder
	rm -f _layouts/*.class

clean :
	git status
