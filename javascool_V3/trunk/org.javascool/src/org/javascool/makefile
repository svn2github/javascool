
ifeq ($(USER), vthierry)

bt :
	uncrustify -q -c ../../../lib/uncrustify.cfg -f Pml.java -o Pml.java.ok

all : 
	@cd ../tst ; ../lib/run.sh org.javascool.JsMain basiqueproba.jvs 
#	$(MAKE) j3-2
#	@cd ../tst ; ../lib/run.sh org.javascool.Eml exp.jvs
#	@cd ../tst ; ../lib/run.sh org.javascool.AlgoEditor
#	@(echo "" | make -C ../../.. jpub) ; firefox http://javascool.gforge.inria.fr/?page=api:proglet/synthesons/doc-files/the-proglet.htm

endif

j3-2 :
	@$(MAKE) -C ../../.. j3-2

clean :
	@$(MAKE) -C ../../.. clean
