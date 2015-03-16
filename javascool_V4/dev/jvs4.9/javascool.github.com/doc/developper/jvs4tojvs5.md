---
layout: default
title: Développer autour de Java's Cool 4.9
---

## Quelles simplifications au niveau des proglets entre Java's Cool 4 et Java's Cool 4.9

### Utilisation des proglets

* Chaque proglet est utilisée de manière indépendante

* La proglet peut-être utilisée hors de la plateforme de Java's Cool 

  * en compilant directement un fichier .jvs sous forme de .jar

  * en réutilisant le code dans un autre environnement

### Spécification des proglets

La [spécification d'une proglet](./specification.html) est allégée.

* Le fichier de [description](./proglet-json.html) est au format standard [json](http://www.json.org) (et non plus en format "Pml").

* Les fichiers de documentation sont directement écrits en HTML (et non plus dans un format XML spécifique).

* Le fichier de  *[completion](./completion-json.html)* de l'éditeur au format standard [json](http://www.json.org) (et non plus en XML).

* Il n'y a plus la notion de "code de démonstration" car chaque fichier .jvs du répertoire constitue une démonstration et est compilé sous forme de .jar.

le reste restant inchangé.
