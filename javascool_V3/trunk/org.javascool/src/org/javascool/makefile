
# A remettre sur le site
#### <html><center> - '''Pour télécharger JavaScool [http://javascool.gforge.inria.fr/v3/javascool.jar cliquer ici]''' </center> et mettre lle lien Tele->J.jar

ifeq ($(USER), vthierry)

all :
	@$(MAKE) -C ../../.. jar
	@cd ../tst ; java -jar  ../www/javascool.jar
#	@cd ../tst ; java -cp ../www/javascool.jar org.javascool.MainV2 edit ingredients Test0.jvs

else

all : 
	@$(MAKE) -C ../../.. jar
	@cd ../../../tst ; java -jar  ../www/javascool.jar

endif
