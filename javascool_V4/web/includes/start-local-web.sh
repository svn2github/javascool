#!/bin/bash
#
# Lancement en local du site web, il faut apache et php installé
#

#
# Si ce script ne marche pas, configurer un serveur apache local et faire un lien symbolique
# du dossier web du serveur local (souvent /var/www ou /var/www/htdocs) vers le dossier web
# du svn. Exemple de commande : 
# sudo ln -s /var/www/jvs /home/utilisateur/javascool/jvs/web
#

# Tue tous les serveurs locaux

killall -KILL httpd 2> /dev/null

# Crée un http.root minimal adapteé à la config locale

/bin/rm -rf .http.root ; mkdir -p `pwd`/.http.root/conf `pwd`/.http.root/logs 

# Génère les fichiers de conf

cat > `pwd`/.http.root/conf/http.conf <<EOF
Listen		 1234
ServerName	 127.0.0.1
ServerRoot	 "`pwd`/.http.root"
DocumentRoot	 "`pwd`"
LoadModule	 alias_module 	`locate mod_alias.so|tail -n 1`
LoadModule	 mime_module 	`locate mod_mime.so|tail -n 1`
LoadModule	 php5_module 	`locate libphp5.so |tail -n 1`
AddHandler	 php5-script	.php
AddType		 text/css	.css
DefaultType 	 text/php
AddDefaultCharset UTF-8
LoadModule	 dir_module	`locate mod_dir.so |tail -n 1`
DirectoryIndex	 index.php
EOF

cat > `pwd`/.http.root/conf/mime.types <<EOF
text/css		.css
EOF

# Lance le serveur apache
if [ -f /usr/sbin/httpd ]
then HTTP=/usr/sbin/httpd 
elif [ -f /usr/sbin/apache2 ]
then HTTP=/usr/sbin/apache2 
else echo "Apache n'est pas installé : impossible de lancer le serveur http" ; exit -1
fi

$HTTP -f `pwd`/.http.root/conf/http.conf -k start

# Liste les logs si il y a un problème

sleep 1 ; cat `pwd`/.http.root/logs/error_log
