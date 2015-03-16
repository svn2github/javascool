# Script à faire lancer par les administrateurs de la forge si des problèmes de droit surgissent à la publication
find /home/groups/javascool/htdocs -type d -exec chmod g+s {} \;
chgrp -R javascool /home/groups/javascool/htdocs
chmod -R g+w /home/groups/javascool/htdocs

