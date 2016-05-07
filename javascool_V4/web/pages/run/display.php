<?php 
    showBrowser(
        array(
            array("Java's Cool","index.php"),
            array("Lancement","")
        ),
        array(
            array("Licence","?page=run&action=licence"),
            array("Usage","?page=run&action=screenshot")
        )
    );
?>

<div class="display">

<p>Pour lancer Java's Cool, rien à installer, hormis Java. <br/><table border="1"><tr><td>
Il suffit de le télécharger <b><a href="javascool-proglets.jar">ICI</a></b> ou , en choisissant l'option «Ouvrir» ou «Ouvrir avec Java».
</td></tr></table><br/></p>

    <p>Pour une version antérieure java 1.6 essayer la version <tt>javascool-proglets-6.jar</tt> disponible en <b><a href="http://javascool.gforge.inria.fr/v4/javascool-proglets-6.jar">cliquant ici</a></b></p>

    <p>ATTENTION: sous Windows10, télécharger la nouvelle version de java (au moins 8 update 91) et télécharger JarFix que vous pourrez retrouver <a href="http://johann.loefflmann.net/en/software/jarfix/index.html#Download">via ce lien</a> puis exécuter <tt>javascool-proglets.jar</tt> en faisant clic droit --> "ouvrir avec..." et ouvrir avec <tt>Javaw.exe</tt> (un immense merci à Rémy Chardon pour cette solution).</p>

<br/>

    <p>- Si le lancement échoue, vous devrez probablement <a href="http://www.java.com/fr/download">installer Java<a/>.</p>

<p>- Ci dessous, nous donnons des indications pour vous aider plus si besoin, sous <a href="#windows">windows</a>, <a href="#macos">Mac OS</a>, et <a href="#linux">Linux</a>.</p>
<p>- Si vous avez des doutes sur l'installation java essayer ce <a href="http://javascool.gforge.inria.fr/documents/test-java/test.jar">programme minimal</a>, si il ne se lance pas c'est probablement un souci d'installation java, il y a de <a href="https://www.java.com/fr/download/help/troubleshoot_java.xml">l'aide ici</a.</p>

<p>- En cas d'échec <a href="?page=contact">contactez-nous</a> : nous vous aiderons.</p>

<p>Pour en savoir plus sur l'utilisation de l'interface se reporter à sa <a href="?page=run&action=screenshot">description picturale</a>.

<hr/><div class="label2"><a name="windows">Windows</a></div>

<p>Téléchargez Java's Cool au format <tt>JAR</tt> exécutable <b><a href="javascool-proglets.jar">ici</a></b>, en choisissant l'option «Ouvrir» ou «Ouvrir avec Java»:</p>
<table border ="1" align="center"><tr><td><img src="images/screen3-1.png" alt="screenshot"/></td></tr></table>

<p>- Si l'option  «Ouvrir ...» n'est pas présente, il suffit de <ul>
  <li>télécharger le fichier <tt>javascool-proglets.jar</tt> sur le bureau,</li>
  <li>puis de cliquer dessus pour le lancer.</li>
</ul></p>
<table align="center"><tr><td>(1) télécharger sur le bureau</td><td></td><td>(2) cliquer pour le lancer et accepter le lancement</td></tr><tr>
<td valign="top"><table border ="1" align="center"><tr><td><img src="images/screen4-1.png" alt="screenshot"/></td></tr></table></td>
<td valign="top"><span class="label-arrow" /></td>
<td valign="top"><table border ="1" align="center"><tr><td><img src="images/screen56-1.png" alt="screenshot"/></td></tr></table></td>
</tr></table>

<p>- Si le lancement échoue encore, vous devez probablement <a href="http://www.java.com/fr/download">installer Java<a/> (version au moins 1.6).</p>

<hr/><div class="label2"><a name="macos">Mac OS</a></div>

<p>Téléchargez Java's Cool au format <tt>JAR</tt> xécutable <b><a href="javascool-proglets.jar">ici</a></b>, et l'ouvrir : ce sera immédiat.</p>
<table border ="1" align="center"><tr><td><img src="images/screen7.png" alt="screenshot"/></td></tr></table>

<p>- Si l'option «Java» n'est pas présente, vous devez probablement <a href="http://www.java.com/fr/download">installer Java<a/> (version au moins 1.6).</p>

<p>- Si votre MAC OS X est un peu ancien, vous pouvez <a href="#Installation_avec_un_Mac_OS_X_un_peu_ancien">activer Java 1.6</a>.</p>

<hr/><div class="label2"><a name="linux">Linux</a></div>

<p>Téléchargez Java's Cool au format <tt>JAR</tt> <b><a href="javascool-proglets.jar">ici</a></b>, en choisissant l'option «Ouvrir» ou «Ouvrir avec Java»:</p>

<p>- Si l'option est absente, suivez les instructions ci-dessous :</p> 
<table border ="1" align="center"><tr><td><table><tr>
 <td><img src="images/screen1.png" alt="screenshot" style="width: 300px; height: auto;"/></td>
 <td><span class="label-arrow" /></td>
 <td><img src="images/screen2.png" alt="screenshot" style="width: 300px; height: auto;"/></td></tr>
</table></td></tr></table>
                
<p>- On peut aussi choisir, pour avoir le logiciel hors-réseau, de le télécharger en cliquant sur: <ul
 ><li>«Sauver sous» puis </li><li>«Ouvrir avec . . <i>java</i>».</li></ul></p>

<p>- On peut aussi lancer, une fois le logiciel téléchargé, à partir d'une ligne de commande en tapant: <ul><li><tt><tt>java -jar javascool-proglets.jar</tt> .</li></ul></p>
    

</div>
