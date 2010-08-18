
ifeq ($(USER), vthierry)

all :
	@cd ../tst ; ../lib/run.sh org.javascool.ProgletApplet javaprog
#	@$(MAKE) -C ../../.. jar
#	@cd ../tst ; java -jar  ../www/javascool.jar

else

all : 
	@$(MAKE) -C ../../.. jar
	@cd ../../../tst ; java -jar ../www/javascool.jar

endif
