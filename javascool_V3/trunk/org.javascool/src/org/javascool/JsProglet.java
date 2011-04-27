/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
*******************************************************************************/

package org.javascool;

import javax.swing.JPanel;

/** Définit une «proglet», c'est à dire un objet numérique programmaable qui, ici, s'intègre dans JavaScool.
 * <pre>- This specification is a draft for Javascool3.3 : DO NOT USE YET !!!</pre>
 * <ul>
 * <li><b>Spécification d'une «proglet».</b><ul>
 *   <li>Chaque «proglet» doit avoir un <i>nom</i> différent: <ul>
 *      <li>c'est celui de la classe Java qui définit la «proglet».</li>
 *      <li>c'est aussi celui du répertoire qui contient les éléments qui constituent la «proglet».</li>
 * </ul></li>
 *  <li>La classe Java qui définit la «proglet» doit: <ol>
 *    <li>Définir la variable statique <a href="#progletProperties"><tt>public static String progletProperties;</tt></a> qui contient les méta-données permettant d'intégrer la  «proglet». <ul>
 *       <li>C'est ce qui indique que cette classe définit une  «proglet».</li>
 *    </ul></li>
 *    <li>Définir la variable statique <tt>public static javax.swing.JPanel panel</tt> si l'objet numérique s'incarne dans un objet graohique.</li>
 *    <li>Définir la méthode statique <tt>public static void test();</tt> si l'objet numérique a un petit code de démo de son fonctionnement.</li>
 * </ol></li>
 * <li>Les éléments qui définissent une «proglet»: <ul>
 *   <li> Sont les fichiers de code, documentation, multimédia (image, son, etc..), regroupés dans un répertoire qui porte le nom de la «proglet».</li>
 *   <li> Les fichiers de documents XML utilisent les constructions de <a href="doc-files/about-hdoc.htm">Hdoc</a> et <a href="doc-files/about-hml.htm">Hml</a>.</li>
 *   <li> Les très gros fichiers sont à poser sur le Web sans imposer de les charger comme ressources de la «proglet».</li>
 *   <li> Le répertoire peut contenu d'autres fichiers JAR si des librairies externes sont utilisées.</li>
 *  </ul></li>
 * </ul></li>
 * <li><b>Manipulation de l'objet numérique programmable.</b><ul>
 *  <li>Toutes les méthodes accessibles à l'utilisateur pour manipuler l'objet numérique sont définies par des méthodes <tt>public static</tt>,
 *    qui seront appellés en les préfixant  du nom de la «proglet» (par exemple <tt>Scope.clear();</tt> pour effacer l'oscilloscope.</li>
 *  <li>Il ne faut jamais [re]définir de méthodes accessibles à l'utilisateur de nom: 
 *     <tt>clone</tt>, <tt>equals</tt>, <tt>finalize</tt>, <tt>getClass</tt>, <tt>hashCode</tt>, <tt>notify</tt>, <tt>run</tt>, <tt>toString</tt>, ou <tt>wait</tt>.</li>
 *  <li>Une bonne méthode pour écrire une proglet est de partir d'un exemple et de l'adapter aux nouvelles fonctionnalités souhaitées.</li>
 * </ul></li>
 * <li><b>Intégration automatique d'une proglet dans JavaScool.</b> Lors de son lancement, JavaScool: <ul>
 *   <li>regarde dans les sous-répertoires de son répertoire de lancement les fichiers Java qui contiennent des classes avec <tt>public static String progletProperties;</tt>,
 *      donc des  «proglet».</li>
 *   <li>compile les fichiers Java et XML de ce répertoire et: <ol>
 *     <li>soit rapporte les erreurs de compilation ou d'intégration si il y en a</li>
 *     <li>soit génère un fichier JAR avec tous les éléments de la  «proglet».</li>
 *   </ol></li>
 *   <li>charge ensuite toutes les  «proglet» visibles dans son environnement.</li>
 * </ul>
 *
 * <pre>Notes critiques de vthierry:
 * - c'est lourd de définir progletProperties; comme une string multi-ligne en java (faut mettre plein de ""+\n""+ . . ) on pourrait mettre la string dans la doc mais c'est intrduire une bricole/verrue
 * - En JsProglet sert a rien puisque c'est juste un "exemple vide" 
 * - La mecanique de chargement dynaamique de proglet est pas gagnee . . a revoir 
 * </pre>
 */
public class JsProglet {
  private JsProglet() {}
  /** Définit les méta-donnęes de la «proglet».
   * C'est une String de syntaxe :<tt><b>"{<br>
   *  &nbsp;&nbsp; title = '<i>le titre de la «proglet»</i></i>'<br>
   *  &nbsp;&nbsp; description = '<i>une à deux lignes de description de ce que la «proglet» propose</i>'<br>
   *  &nbsp;&nbsp; category = '<i>la catégorie pédagogique de la «proglet» (voir ci-dessous)</i>'<br>
   *  &nbsp;&nbsp; vendor = '<i>le contact email ou la page web pour contacter l'auteur de la proglet</i>'<br>
   *  &nbsp;&nbsp; type = '<i>le type de «proglet»  (voir ci-dessous)</i>'<br>
   *  &nbsp;&nbsp; width = '<i>la largeur en pixel de la «proglet», si la valeur est contrainte</i>'<br>
   *  &nbsp;&nbsp; height = '<i>la hauteur en pixel de la «proglet», si la valeur est contrainte</i>'<br>
   *  &nbsp;&nbsp; pages = { // Documentation de la «proglet»<<br>
   *  &nbsp;&nbsp;&nbsp;&nbsp; { name = 'usage' file = '<i>the-proglet-name</i>/doc-files/about-proglet.htm' } // Description des routines de la «proglet»<br>
   *  &nbsp;&nbsp;&nbsp;&nbsp; { name = 'activité' file = '<i>the-proglet-name</i>/doc-files/<i>file-name</i>.htm' } // Description de l'activité réalisée avec la «proglet»<br>
   *  &nbsp;&nbsp;&nbsp;&nbsp; { name = '<i>the document page title</i>' file = '<i>the-proglet-name</i>/doc-files/<i>the-file-name</i>.htm' } // Autres documents relatifs à la «proglet»<br>
   *  &nbsp;&nbsp; &nbsp;&nbsp;// etc..<br>
   *  &nbsp;&nbsp; }<br>
   *  &nbsp;&nbsp; video = '<i>une URL où une vidéo de démonstration est disponible</i>'<br>
   *  &nbsp;&nbsp; applet = '<i>une URL où la «proglet» focntionne en "educlet" sur le Web</i>'<br>
   * }"</b></tt><br>
   * Remarques: <ul>
   * <li>Le nom de la  «proglet» c'est pas redonnée ici car c'est le nom de la classe Java et du répertoire des ressources.</li>
   * <li>Les catégories pédagogiques standards sont:
   * <br> "Apprendre à programmer", "Algorithmes dichotomiques", "Aller plus loin en programmation", "Objet numérique: le son", "Objet numérique: l'image", "Objet numérique: le graphe".</li>
   * <li>Le type d'une «proglet» est soit "processing" pour des «proglets» construites à partir de processing, "algobox" pour des «proglets» utilisant l'"algo-editor", ou "java" (type par défaut).</li>
   * <li>Les width et height sont à spécifier pour des «proglet» de type "processing".</li>
   * </ul>
   */
  public static String progletProperties = "{}";
  /** Référence le panel correspondant à l'interface graphique de la «proglet», si il existe (si seule la console est utilisée, le champ peut rester indéfini). */
  public static final JPanel panel = null;
  /** Lance un test/demo sur l'interface graphique de la «proglet», si définit (si il n'y a pas de démo, la méthode peut rester indéfinie). */
  public static final void test() { }
}
