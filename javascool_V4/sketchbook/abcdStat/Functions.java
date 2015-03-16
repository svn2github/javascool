package org.javascool.proglets.abcdStat;

import static org.javascool.macros.Macros.*;

import org.javascool.gui.Desktop;
import org.javascool.widgets.Console;

/** Définit les fonctions de la proglet (dont celles de la proglet AlgoDeMath)
 * @see <a href="Functions.java.html">source code</a>
 * @serial exclude
 */
public class Functions {
	private static final long serialVersionUID = 1L;
	private Functions() {}

	// --------------------------------------------------------------
	// Les fonctions de la proglet algoDeMath
	// --------------------------------------------------------------

	private static Panel getPane() {
		return getProgletPane();
	}
	
	/** Initialise le tracé.
	 * @param Xmin Echelle minimale horizontale, l'abscisse sera tracée dans [-Xmin, Xmax], par défaut [-1, 1].
	 * @param Xmax Echelle maximale horizontale, l'abscisse sera tracée dans [-Xmin, Xmax], par défaut [-1, 1].
	 * @param Ymin Echelle minimale verticale, l'abscisse sera tracée dans [-Ymin, Ymay], par défaut [-1, 1].
	 * @param Ymax Echelle maximale verticale, l'abscisse sera tracée dans [-Ymin, Ymax], par défaut [-1, 1].
	 */
	public static void reset(double Xmin, double Xmax, double Ymin, double Ymax) {
		getPane().inputX.setScale(Xmin, Xmax, 0.001);
		getPane().inputY.setScale(Ymin, Ymax, 0.001);
		getPane().scope.reset((Xmin + Xmax) / 2, (Ymin + Ymax) / 2, (Xmax - Xmin) / 2, (Ymax - Ymin) / 2);
		getPane().runnable = null;
		org.javascool.gui.Desktop.getInstance().focusOnProgletPanel();
	}
	
	/** Ajoute une chaîne de caractères au tracé.
	 * @param x Abcisse du coin inférieur gauche de la chaîne, dans [-X, X], par défaut [-1, 1].
	 * @param y Ordonnée du coin inférieur gauche de la chaîne, dans [-Y, Y], par défaut [-1, 1].
	 * @param s Valeur de la chaîne de caractères.
	 * @param c Couleur du point: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
	 * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
	 */
	public static Object addString(double x, double y, String s, int c) {
		return getPane().scope.add(x, y, s, c);
	}
	
	/*
	 * @see #addString(double, double, double, String, int)
	 */
	public static Object addString(double x, double y, String s) {
		return addString(x, y, s, 0);
	}
	
	/** Trace un rectangle.
	 * @param xmin Abcisse inférieure gauche, dans [-X, X], par défaut [-1, 1].
	 * @param ymin Ordonnée inférieure gauche, dans [-Y, Y], par défaut [-1, 1].
	 * @param xmax Abcisse supérieure droite, dans [-X, X], par défaut [-1, 1].
	 * @param ymax Ordonnée supérieure droite, dans [-Y, Y], par défaut [-1, 1].
	 * @param c Numéro de la courbe: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
	 * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
	 */
	public static Object addRectangle(double xmin, double ymin, double xmax, double ymax, int c) {
		return getPane().scope.addRectangle(xmin, ymin, xmax, ymax, c);
	}
	
	/*
	 * @see #addRectangle(double, double, double, double, int)
	 */
	public static Object addRectangle(double xmin, double ymin, double xmax, double ymax) {
		return addRectangle(xmin, ymin, xmax, ymax, 0);
	}
	
	/** Trace un block rectangulaire.
	 * @param xmin Abcisse inférieure gauche, dans [-X, X], par défaut [-1, 1].
	 * @param ymin Ordonnée inférieure gauche, dans [-Y, Y], par défaut [-1, 1].
	 * @param xmax Abcisse supérieure droite, dans [-X, X], par défaut [-1, 1].
	 * @param ymax Ordonnée supérieure droite, dans [-Y, Y], par défaut [-1, 1].
	 * @param c Numéro de la couleur du block: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
	 * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
	 */
	public static Object addBlock(double xmin, double ymin, double xmax, double ymax, int c) {
		return getPane().scope.add(xmin, ymin, xmax - xmin, ymax - ymin, c, -1);
	}
	
	/*
	 * @see #addBlock(double, double, double, double, int)
	 */
	public static Object addBlock(double xmin, double ymin, double xmax, double ymax) {
		return addBlock(xmin, ymin, xmax, ymax, 0);
	}
	
	/** Trace une ligne.
	 * @param x1 Abcisse du 1er point, dans [-X, X], par défaut [-1, 1].
	 * @param y1 Ordonnée du 1er point, dans [-Y, Y], par défaut [-1, 1].
	 * @param x2 Abcisse du 2eme point, dans [-X, X], par défaut [-1, 1].
	 * @param y2 Ordonnée du 2eme point, dans [-Y, Y], par défaut [-1, 1].
	 * @param c Numéro de la courbe: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
	 * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
	 */
	public static Object addLine(double x1, double y1, double x2, double y2, int c) {
		return getPane().scope.add(x1, y1, x2, y2, c);
	}
	
	/*
	 * @see #addLine(double, double, double, double, int)
	 */
	public static Object addLine(double x1, double y1, double x2, double y2) {
		return addLine(x1, y1, x2, y2, 0);
	}
	
	/** Trace une point.
	 * @param x1 Abcisse du 1er point, dans [-X, X], par défaut [-1, 1].
	 * @param y1 Ordonnée du 1er point, dans [-Y, Y], par défaut [-1, 1].
	 * @param c Numéro de la courbe: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
	 * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
	 */
	public static Object addPoint(double x1, double y1, int c) {
		return getPane().scope.add(x1, y1, x1, y1, c);
	}
	
	/*
	 * @see #addPoint(double, double, int)
	 */
	public static Object addPoint(double x1, double y1) {
		return addPoint(x1, y1, 0);
	}
	
	/** Trace un  cercle.
	 * @param x Abcisse du centre, dans [-X, X], par défaut [-1, 1].
	 * @param y Ordonnée du centre, dans [-Y, Y], par défaut [-1, 1].
	 * @param r Rayon du cercle.
	 * @param c Numéro de la courbe: 0 (noir, défaut), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
	 * @return L'objet graphique créé, utilisé pour détruire l'objet ensuite.
	 */
	public static Object addCircle(double x, double y, double r, int c) {
		return getPane().scope.add(x, y, r, c);
	}
	
	/*
	 * @see #addCircle(double, double, double, int)
	 */
	public static Object addCircle(double x, double y, double r) {
		return addCircle(x, y, r, 0);
	}
	
	/** Détruit l'objet graphique spécifié.
	 * @param object L'objet à détruire.
	 * @return La valeur true si l'objet existait, false sinon.
	 */
	public boolean remove(Object object) {
		return getPane().scope.remove(object);
	}
	
	/** Renvoie la valeur horizontale du réticule. */
	public static double getX() {
		return getPane().inputX.getValue();
	}
	
	/** Renvoie la valeur verticale du réticule. */
	public static double getY() {
		return getPane().inputY.getValue();
	}
	/** Définit la position du réticule.
	 * @param x Abscisse du réticule,  dans [-X, X], par défaut [-1, 1].
	 * @param y Reticule ordinate, dans [-Y, Y], par défaut [-1, 1].
	 */
	public static void setReticule(double x, double y) {
		getPane().scope.setReticule(x, y);
	}
	
	/** Définit une portion de code appelée à chaque modification du réticule.
	 * @param runnable La portion de code à appeler, ou null si il n'y en a pas.
	 */
	public static void setRunnable(Runnable runnable) {
		getPane().runnable = runnable;
	}

	// --------------------------------------------------------------
	// Ajouts à la proglet algoDeMath
	// --------------------------------------------------------------
	
	/** Dessine un diagramme en anneaux à partir du tableau des effectifs
	 * @param effectifs Le tableau des effectifs.
	 * @param legendes Le tableau des legendes.
	 */	
	public static void anneaux(int [] effectifs, String [] legendes){
		if (effectifs.length != legendes.length) {
			Desktop.getInstance().focusOnConsolePanel();
			Console.getInstance().clear();
			Console.getInstance().print("Les 2 tableaux doivent etre de meme longueur.\n\n");
			return;
		}
		Stat4.anneaux(effectifs, legendes);
	}

	/** Dessine un diagramme en anneaux à partir de donnees brutes.
	 * @param donnees Le tableau des donnees brutes.
	 */	
	public static void anneaux(String [] donnees){	
		String [] modalites = Stat1234.oterDoublons(donnees);
		int [] effectifs = Stat1234.getEffectifs(donnees, modalites);
		Stat4.anneaux(effectifs, modalites);
	}
	
	/** Dessine un diagramme en barres à partir du tableau des effectifs
	 * @param effectifs Le tableau des effectifs.
	 * @param legendes Le tableau des legendes.
	 */	
	public static void barres(int [] effectifs, String [] legendes){	
		if (effectifs.length != legendes.length) {
			Desktop.getInstance().focusOnConsolePanel();
			Console.getInstance().clear();
			Console.getInstance().print("Les 2 tableaux doivent etre de meme longueur.\n\n");
			return;
		}
		Stat1.barres(effectifs, legendes);
	}

	/** Dessine un diagramme en barres à partir de donnees brutes.
	 * @param donnees Le tableau des donnees brutes.
	 */	
	public static void barres(String [] donnees) {	
		String [] modalites = Stat1234.oterDoublons(donnees);
		int [] effectifs = Stat1234.getEffectifs(donnees, modalites);
		Stat1.barres(effectifs, modalites);
	}
	
	/** Dessine un diagramme en barres juxtaposées (croisement de 2 variables) à partir de données brutes.
	 * @param donnees1 Le tableau de données de la première variable.
	 * @param donnees2 Le tableau de données de la deuxième variable.
	 */
	public static void barresJuxtaposees(String [] donnees1, String [] donnees2) {	
		if (donnees1.length != donnees2.length) {
			Desktop.getInstance().focusOnConsolePanel();
			Console.getInstance().clear();
			Console.getInstance().print("Les 2 tableaux doivent etre de meme longueur.\n\n");
			return;
		}
		String [] modalites1 = Stat1234.oterDoublons(donnees1);	
		String [] modalites2 = Stat1234.oterDoublons(donnees2);
		Stat1.barresJuxtaposees(donnees1, donnees2, modalites1, modalites2);
	}

	/** Trace le diagramme en batons d'une variable quantitative discrete
	 * @param donnees Les valeurs de la variable quantitative.
	 */	
	public static void batons(int [] donnees) {
		Stat4.batons(donnees);
	}

	/** Dessine un diagramme en camembert à partir du tableau des effectifs
	 * @param effectifs Le tableau des effectifs.
	 * @param legendes Le tableau des legendes.
	 */	
	public static void camembert(int [] effectifs, String [] legendes){
		if (effectifs.length != legendes.length) {
			Desktop.getInstance().focusOnConsolePanel();
			Console.getInstance().clear();
			Console.getInstance().print("Les 2 tableaux doivent etre de meme longueur.\n\n");
			return;
		}
		Stat4.camembert(effectifs, legendes);
	}

	/** Dessine un diagramme en camembert à partir de donnees brutes.
	 * @param donnees Le tableau des donnees brutes.
	 */	
	public static void camembert(String [] donnees){	
		String [] modalites = Stat1234.oterDoublons(donnees);
		int [] effectifs = Stat1234.getEffectifs(donnees, modalites);
		Stat4.camembert(effectifs, modalites);
	}

	/** Trace l'histogramme d'une variable quantitative
	 * @param donnees Les valeurs de la variable quantitative.
	 * @param bornes Les classes de l'histogramme.
	 */	
	public static void histogramme(double [] donnees, double [] bornes) {
		Stat1.histogramme(donnees, bornes);
	}

	/** Trace la boite à moustache d'une variable quantitative
	 * @param donnees Le tableau des donnees.
	 */	
	public static void moustache(double [] donnees){
		Stat3.boite_a_moustache(donnees);
	}

	/** Trace une boite à moustache par modalité
	 * @param quanti Les valeurs de la variable quantitative.
	 * @param quali Les valeurs de la variable qualitative.
	 */	
	public static void moustache(double [] quanti, String [] quali) {	
		if (quanti.length != quali.length) {
			Desktop.getInstance().focusOnConsolePanel();
			Console.getInstance().clear();
			Console.getInstance().print("Les 2 tableaux doivent etre de meme longueur.\n\n");
			return;
		}
		String [] modalites = Stat1234.oterDoublons(quali);
		Stat3.boite_a_moustache(quanti, quali, modalites);
	}
	
	/** Dessine un nuage de points à partir de deux tableaux de valeurs quantitatives.
	* @param t1 le premier tableau de valeurs.
	* @param t2 le deuxième tableau de valeurs.
	*/
	public static void nuage(double [] t1, double [] t2) {	
		if (t1.length != t2.length) {
			Desktop.getInstance().focusOnConsolePanel();
			Console.getInstance().clear();
			Console.getInstance().print("Les 2 tableaux doivent etre de meme longueur.\n\n");
			return;
		}
		Stat2.nuage(t1, t2, false);
	}
	
	/** Dessine un nuage de points et la droite de regression.
	* @param t1 le premier tableau de valeurs.
	* @param t2 le deuxième tableau de valeurs.
	*/
	public static void nuage_regression(double [] t1, double [] t2) {
		if (t1.length != t2.length) {
			Desktop.getInstance().focusOnConsolePanel();
			Console.getInstance().clear();
			Console.getInstance().print("Les 2 tableaux doivent etre de meme longueur.\n\n");
			return;
		}
		Stat2.nuage(t1, t2, true);
	}
	
	/** Affiche le résumé (moyenne, ecart-type, quartiles...) d'une variable quantitative
	 * @param donnees Le tableau des donnees.
	 */	
	public static void resume(double [] donnees) {
		System.out.println("moyenne : " + Stat1234.getMoyenne(donnees.length, donnees));
		System.out.println("ecart type : " + Stat2.ecarttype(donnees.length, donnees));
		System.out.println("minimum : " + Stat1234.getMinimum(donnees.length, donnees));
		System.out.println("quartile 1 : " + Stat3.quartile_1(donnees.length, donnees));
		System.out.println("mediane : " + Stat3.mediane(donnees.length, donnees));
		System.out.println("quartile 3 : " + Stat3.quartile_3(donnees.length, donnees));
		System.out.println("maximum : " + Stat1234.getMaximum(donnees.length, donnees));
	}	

}
