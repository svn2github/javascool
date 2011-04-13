/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
*******************************************************************************/

package org.javascool;

import javax.swing.JPanel;

/** Define a «proglet», i.e. a piece of code that can be integrated in JavaScool. 
 * - This specification is a draft for Javascool3.3 : DO NOT USE yet !!!
 */
public class JsProglet {
  /** Returns the «proglet» meta-data.
   * @return A String of the form :<tt>"{<br>
   *  title = '<i>the «proglet» title</i></i>'<br>
   *  description = '<i>a one/two line(s) description of what the «proglet» proposes</i>'<br>
   *  category = '<i>the «proglet» category (see below)</i>'<br>
   *  vendor = '<i>the contact email address or home page url</i>'<br>
   *  type = '<i>the «proglet» type</i>'<br>
   *  width = '<i>the «proglet» width, if constrainted</i>'<br>
   *  height = '<i>the «proglet» height, if constrainted</i>'<br>
   *  pages = {<br>
   *   { name = 'usage' file = 'proglet/<i>the-proglet-name</i>/doc-files/about-proglet.htm' } // Description of the « proglet » routines »<br>
   *   { name = 'activité' file = 'proglet/<i>the-proglet-name</i>/doc-files/<i>file-name</i>.htm' } // Description of some activity with the proglet<br>
   *   { name = '<i>the document page title</i>' file = '<i>the document page file relative location of the form: </i>proglet/<i>the-proglet-name</i>/doc-files/<i>the-file-name</i>.htm' }<br>
   *   // etc..<br>
   *  }<br>
   *  video = '<i>an URL where a video demo is available</i>'<br>
   *  applet = '<i>an URL where the «proglet» is available as a web applet</i>' // If not a standard page is generated ob the web site.<br>
   * }"</tt><br>
   * Remarks: <ul>
   * <li>The «proglet» name is the Java class name.</li>
   * <li>Standard «proglet» categories are:
   * <br> "Apprendre à programmer", "Algorithmes dichotomiques", "Aller plus loin en programmation", "Objet numérique: le son", "Objet numérique: l'image", "Objet numérique: le graphe".</li>
   * <li>The «proglet» type is either "processing" for «proglets»  built from the processing platform, "algobox" for proglets using the algo-editor, or "java" (default type).</li>
   * <li>Width and height must be specified for "processing" type «proglet».</li>
   * </ul>
   */
  static String progletProperties = "{}";
  /** References the panel corresponding to the «proglet» graphical interface if any (if only using the console, return null or undefined). */
  static final JPanel panel = null;
  /** Runs a test/demo of the «proglet» graphical interface if any (no demo if undefined). */
  static final void test() { }
}

/** Indique que la classe est une proglet.
 *  <p>Par contrat, une <a href="doc-files/about-proglets.htm">«proglet»</a> doit: <ul>
 *    <li>Implementer l'interface <tt>org.javascool.Proglet</tt>.</li>
 *    <li>Définir un <tt>public static final JPanel panel;</tt> qui est l'instanciation graphique de la proglet.</li>
 *    <li>Définir une méthode <tt>static void test()</tt> pour fournir un test/démonstration de la proglet.</li>
 *    <li>Définir toutes les méthodes accessibles à l'utilisateur sous la forme de méthode <tt>public static</tt> préfixées par le nom de la proglet.</li>
 *    <li>Ne jamais [re]définir de méthodes accessibles à l'utilisateur de nom:
 *       <tt>clone</tt>, <tt>equals</tt>, <tt>finalize</tt>, <tt>getClass</tt>, <tt>hashCode</tt>, <tt>notify</tt>, <tt>run</tt>, <tt>toString</tt>, ou <tt>wait</tt>,
 *       réservé pour les objets <tt>Runnable</tt> de <tt>Java</tt> utilisés ici pour l'encapsulation.</li>
 *    <li>Avoir un constructeur <tt>private</tt> puisque c'est une factory avec uniquement des méthodes statiques qui ne peut être construit.</li>
 *  </ul></p>
 *  <p>Une bonne méthode pour écrire une proglet est de partir d'un exemple et de l'adapter aux nouvelles fonctionnalités souhaitées.</p>
 *  <p>Ces <a href="doc-files/about-proglets.htm">«proglets»</a> sont écrites pour permettre facilement à un enseignant ou un partenaire,
 *    qui programme en Java au niveau élémentaire d'adapter, enrichir ces éléments.</p>
 *  <p>Non sans les <a href="http://javascool.gforge.inria.fr/?page=contact">partager</a> avec nous tout(te)s en nous contactant.</p>
 *
 * <hr/>Intégration de la proglet dans javascool:<ol>
 *  <li>Les fichiers sources sont dans un répertoire: <tt>src/proglet/<i>nom-de-la-proglet</i>/*.java</tt></li>
 *  <li>Les fichiers de documentation et de description des activités sont dans un répertoire: <tt>src/proglet/<i>nom-de-la-proglet</i>/doc-files/*.(xml|png|..)</tt>:<ul>
 *    <li>Les fichiers de documentation sont en XML avec les constructions de <a href="doc-files/about-hdoc.htm">Hdoc</a> et <a href="doc-files/about-hml.htm">Hml</a>.</li>
 *    <li>Un fichier de documentation de la proglet est créé en <tt>src/proglet/<i>nom-de-la-proglet</i>/doc-files/about-proglet.xml</tt>.</li>
 *    <li>Un fichier de lancement de la proglet est créé en <tt>src/proglet/<i>nom-de-la-proglet</i>/doc-files/the-proglet.xml</tt>:
 *      <li>Il contient la directive <tt>&lt;p>&lt;l class="proglet" link="<i>nom-de-la-proglet</i>"/>&lt;/p></tt>.</li>
 *   </ul></li>
 *  </ul></li>
 *  <li>Le fichier <tt>src/org/javascool/doc-files/about-proglets.xml</tt> contient la <a href="doc-files/about-proglets.htm">table de toutes les proglets</a>.</li>
 * </ol>
 */
/*

 - Ajouter un integrateur de proglet: compile les xml en htm, hurle si fichier kk, genere un jar signé.
 - Reconnaitre les proglets : par declaration, par lecture des fichiers du path ? de javascool.jar ?
 - Reunit les proglets/processing proglets en faisant un lien de sketchbook dans proglet
 - Ah que les prglet pas dans progtlet pour defalut et puis voila ?
 - Ah que Proglet si et seulement si a les fields progletProperties etc . . ?
 - Creer un org.javascool.core ? org.javascool.widget ?

*/
