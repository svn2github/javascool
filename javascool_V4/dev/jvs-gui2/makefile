cmp :
	for f in `find -name '*.java'` ; do if [ -f ../../work/$$f ] ; then if diff $$f ../../work/$$f > /dev/null ; then ok=1; else echo ; echo ">>> $$f" ; echo ; diff $$f ../../work/$$f ; fi ; fi ; done > diff.txt ; ok=1



#Historique des fichiers modifies dans la version courante de jvs:
#
#    U work/src/org/javascool/tools/UserConfig.java
#    U work/src/org/javascool/tools/FileManager.java
#    U work/src/org/javascool/macros/Stdout.java
#    U work/src/org/javascool/widgets/ToolBar.java
#    U work/src/org/javascool/widgets/TabbedPane.java
#    U work/src/org/javascool/core/ProgletEngine.java
#    U work/src/org/javascool/About.java
#
#Releve des principales versions svn
# -r 1112 dernieres version avant tes modifs
# -r 1124 derniere version apres tes modifs
# -r 1129 version avec les sources de la -r1112 beautifies
