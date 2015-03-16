
GFORGE_USER ?= $(shell whoami)

publish :
	rsync --rsh='ssh -C' -rl --delete-excluded --exclude .svn/ --exclude .git/ . $(GFORGE_USER)@scm.gforge.inria.fr:/home/groups/javascool/htdocs/v4
