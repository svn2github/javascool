tst : $(HOME)/.hotjava/properties
	appletviewer index.html 

run:
	java compiler/ChargeClasse

cmp : 
	javac compiler/*.java
	mv compiler/*.class ../img/compiler

$(HOME)/.hotjava/properties : makefile
	@mkdir -p $(@D) ; rm -f $@
	@echo 'acl.read.default=~' >>$@
	@echo 'acl.write.default=~' >>$@
	@echo 'appletviewer.security.mode=unrestricted' >> $@
