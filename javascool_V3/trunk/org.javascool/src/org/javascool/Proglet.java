/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

/** Indique que la classe est une proglet.
 *  <p>Par contrat, une proglet doit: <ul>
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
 */
public interface Proglet { }
