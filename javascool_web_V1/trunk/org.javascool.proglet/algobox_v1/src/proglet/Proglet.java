package proglet;
/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************
/



/** Indique que la classe est une proglet.
 *  <p>Par contrat, une proglet doit: <ul>
 *    <li>Appartenir au <tt>package proglet</tt>.</li>
 *    <li>D�finir un <tt>public static final JPanel panel;</tt> de taille maximale <tt>[540 x 580]</tt> qui est l'instanciation graphique de la proglet.</li>
 *    <li>D�finir une m�thode <tt>static void test()</tt> pour fournir un test/d�monstration de la proglet.</li>
 *    <li>D�finir toutes les m�thodes accessibles � l'utilisateur sous la forme de m�thode <tt>public static</tt> pr�fix�es par le nom de la proglet.</li>
 *    <li>Ne jamais [re]d�finir de m�thodes accessibles � l'utilisateur de nom: 
 *       <tt>clone</tt>, <tt>equals</tt>, <tt>finalize</tt>, <tt>getClass</tt>, <tt>hashCode</tt>, <tt>notify</tt>, <tt>run</tt>, <tt>toString</tt>, ou <tt>wait</tt>, 
 *       r�serv� pour les objets <tt>Runnable</tt> de <tt>Java</tt> utilis�s ici pour l'encapsulation.</li>
 *    <li>Avoir un constructeur <tt>private</tt> puisque c'est une factory avec uniquement des m�thodes statiques qui ne peut �tre construit.</li>
 *  </ul>
 *  Une bonne m�thode pour �crire une proglet est de partir d'un <a href="Smiley.java">exemple</a> et de l'adapter aux nouvelles fonctionnalit�s souhait�es.</p>
 *   Ces classes sont �crites pour permettre facilement � un enseignant ou un partenaire, qui programme en Java au niveau �l�mentaire d'adapter, enrichir ces �l�ments.
 *   Non sans les <a href="../../doc/about-all.htm#contact">partager</a> avec nous tout(te)s en nous contactant.
 */
public interface Proglet { }
