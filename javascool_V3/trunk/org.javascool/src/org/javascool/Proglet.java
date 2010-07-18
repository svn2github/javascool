/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

/** Indique que la classe est une proglet.
 *  <p>Par contrat, une proglet doit: <ul>
 *    <li>Appartenir au <tt>package org.javascool</tt>.</li>
 *    <li>Définir un <tt>public static final JPanel panel;</tt> qui est l'instanciation graphique de la proglet.</li>
 *    <li>Définir une méthode <tt>static void test()</tt> pour fournir un test/démonstration de la proglet.</li>
 *    <li>Définir toutes les méthodes accessibles à l'utilisateur sous la forme de méthode <tt>public static</tt> préfixées par le nom de la proglet.</li>
 *    <li>Ne jamais [re]définir de méthodes accessibles à l'utilisateur de nom: 
 *       <tt>clone</tt>, <tt>equals</tt>, <tt>finalize</tt>, <tt>getClass</tt>, <tt>hashCode</tt>, <tt>notify</tt>, <tt>run</tt>, <tt>toString</tt>, ou <tt>wait</tt>, 
 *       réservé pour les objets <tt>Runnable</tt> de <tt>Java</tt> utilisés ici pour l'encapsulation.</li>
 *    <li>Avoir un constructeur <tt>private</tt> puisque c'est une factory avec uniquement des méthodes statiques qui ne peut être construit.</li>
 *  </ul>
 *  Une bonne méthode pour écrire une proglet est de partir d'un <a href="Smiley.java">exemple</a> et de l'adapter aux nouvelles fonctionnalités souhaitées.</p>
 *   Ces classes sont écrites pour permettre facilement à un enseignant ou un partenaire, qui programme en Java au niveau élémentaire d'adapter, enrichir ces éléments.
 *   Non sans les <a href="http://javascool.gforge.inria.fr/?page=contactcontact">partager</a> avec nous tout(te)s en nous contactant.
 */
public interface Proglet { }
