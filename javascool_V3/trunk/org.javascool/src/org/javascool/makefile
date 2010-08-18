
ifeq ($(USER), vthierry)

all :
	@$(MAKE) -C ../../.. jar
	@cd ../tst ; java -jar  ../www/javascool.jar
#	@cd ../tst ; ../lib/run.sh org.javascool.ProgletApplet javaprog

else

all : 
	@$(MAKE) -C ../../.. jar
	@cd ../../../tst ; java -jar ../www/javascool.jar

endif
