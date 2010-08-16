
ifeq ($(USER), vthierry)

all :
	@$(MAKE) -C ../../.. jar
	@cd ../tst ; java -jar  ../www/javascool.jar
#	@cd ../tst ; java -cp ../www/javascool.jar org.javascool.MainV2 edit ingredients Test0.jvs

else

all : 
	@$(MAKE) -C ../../.. jar
	@cd ../tst ; java -jar  ../www/javascool.jar

endif
