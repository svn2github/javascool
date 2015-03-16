---
layout: default
title: Ce qui change avec Java'sCool 4.9
---

## Ce qui change entre Java's Cool 4 et Java's Cool 4.9

* Chaque proglet et chaque programme javascool est un grain logiciel indépendant qui se lance sous forme de jar.

* Les classes partagées par plusieurs proglets ont été mises dans le core de javascool.

* Le [mécanisme de construction](developper/index.html) d'une proglet a été refondu et le [constructeur de proglet](../wproglets/javascool-core.jar) a été optimisé.

* La [spécification des proglets](developper/jvs4tojvs5.html) a été simplifiée. 

* L'intégration de proglets [processing](http://www.openprocessing.org) se fait hors javascool, dans processing.

## Ce qui ne change *pas* entre Java's Cool 4 et Java's Cool 4.9

* Les contenus et tous les programmes javascool sont stictement compatibles (seuls les localisation des classes peut changer).

* Les classes `org.javascool.{core|macros|tools|widgets}.*` qui forment le noyau de javascool sont préservées.

## Ce qui est en projet ou à l'étude pour la suite

* Automatiser les installations des librairies dynamiques des proglets.

* Proposer un éditeur HTML5/JS et un déploiement par [JNLP](http://en.wikipedia.org/wiki/Java_Web_Start) pour utiliser les proglets comme des grains du Web, avec le [wrapper web](../wproglets/javascool-core-api/org/javascool/core/MainWrapper.html)

* Étudier le déploiement sur Android à travers un middleware comme JBED ou JAD

