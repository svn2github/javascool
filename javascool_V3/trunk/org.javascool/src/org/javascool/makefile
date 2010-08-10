
ifeq ($(USER), vthierry)

all : 
	@$(MAKE) -C ../../.. jar
	@cd ../tst ; java -cp ../www/javascool.jar org.javascool.MainV2 edit ingredients Test0.jvs

else

all : 
	@$(MAKE) -C ../../.. v3

endif

clean :
	@$(MAKE) -C ../../.. clean

#####################################################################################################################################
#
# To Do liste a vthierry:
#
#
# - mettre le mp3 sur documents pour limiter la taille des files
# - retester mode applet
# - Swing, ss demo, avec doc et avec import filtres par translator
# - Ajouter algobox fct
# - Distinguer les types de données, structures de contrôle des autres mots du langage dans la documentation et la colorisation, avec des oranges différents <r mode=''>
# - Ajout qq sujets au fil de l'eau, selon les retours
# - Travailler sur les tutoriaux BML (voir makefile.bml)
#  - Voir pb des droits qd on rsync sur javascool
#  - Voir le pb du JSlider qui affiche sa valeur entière et perturbe la lecture.
#  - Voir comment remplacer l'usage de saxon:line-number() dans style_about2.xsl pour mise en ligne directe du xml/xsl
#  - Voir le pb du setLocation() en mode standalone qui affiche mal les deux fenetres edit et run
#  - Voir à permettre d'agrandir la fenêtre d'édition en standalone en largeur qd on passe en mode maximal size.
#  - Utiliser la methode POST dans InterfacePrincipale pour appeler http://facets.inria.fr/javascool et non GET
#  - Voir si on peut alleger la copie des img sur le site si Proglets.getIcon devient inutile http://javascool.gforge.inria.fr/proglet/img
#  - Voir à utiliser un algorithme public pour proglet.Translator.reformat plutôt que ce bricolage local
#  - La version web a qq soucis, certains exécutaient encore le programme d'un autre. Sous IE, la zone d'input ne marche pas bien, elle s'active mais impossible de taper dedans.
#
#####################################################################################################################################
