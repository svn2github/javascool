package org.javascool.proglets.abcdStat;

/** Groupe de projet 2 : nuage, régression linéaire
 * 
 */
public class Stat2 {
	/** Calcule la variance d'élements entiers ou réels
	 * @param n Le nombre d'elements du tableau.
	 * @param t Le tableau.
	 */	
	public static double variance(int n, double [] t){
		double somme;
		int i;
		somme=t[0];
		for (i=1;i<n;i=i+1){
			somme=somme+t[i];
		}
		double moyenne;
		moyenne=somme/n;
		double somme2;
		somme2=Math.pow((t[0]-moyenne),2);
		for (i=1;i<n;i=i+1){
			somme2=somme2+(t[i]-moyenne)*(t[i]-moyenne);
		}
		double variance=somme2/n;
		return variance;
	}

	/** Calcule l'écart-type d'éléments entiers ou réels
	 * @param n Le nombre d'elements du tableau.
	 * @param t Le tableau.
	 */	
	public static double ecarttype(int n, double [] t){
		double var=variance(n,t);
		double ecarttype;
		ecarttype=Math.sqrt(var);
		return ecarttype;
	}

	/** Calcule la covariance de 2 variables numériques
	 * @param t1 Le premier tableau de valeurs.
	 * @param t2 Le deuxième tableau de valeurs.
	 * @param n Le nombre d'elements du tableau.
	 */	
	public static double covariance(double []t1, double []t2, int n) {
		double sommeProd = 0;
		for (int i=0;i<n;i++){
			sommeProd = t1[i] * t2[i] + sommeProd;	
		}
		double Moy1=Stat1234.getMoyenne(n, t1);
		double Moy2=Stat1234.getMoyenne(n, t2);
		double covariance =(sommeProd/n) - Moy1 * Moy2;
		return  covariance;
	}

	/** Dessine un point (sous forme d'un +).
	 * @param x abscisse du point.
	 * @param y ordonnee du point.
	 * @param taille_x taille du point en abscisse.
	 * @param taille_y taille du point en ordonnee.
	 */
	public static void dessinePoint(double x, double y, double taille_x, double taille_y) {
		Functions.addLine(x-0.5*taille_x, y, x+0.5*taille_x, y, 2);
		Functions.addLine(x, y-0.5*taille_y, x, y+0.5*taille_y, 2);
	}

	/** Dessine un nuage de points à partir de deux tableaux de valeurs quantitatives.
	 * @param t1 le premier tableau de valeurs.
	 * @param t2 le deuxième tableau de valeurs.
	 * boolean avecRegression sans ou avec la droite de regression.
	 */
	public static void nuage(double [] t1, double [] t2, boolean avecRegression){
		int n=t1.length;
		double min1=Stat1234.getMinimum(n, t1);
		double max1=Stat1234.getMaximum(n, t1);
		double min2=Stat1234.getMinimum(n, t2);
		double max2=Stat1234.getMaximum(n, t2);
		// echelle du graphique
		double ecart1 = max1 - min1;
		double ecart2 = max2 - min2;
		Functions.reset(min1-ecart1/10, max1+ecart1/10, min2-ecart2/10, max2+ecart2/10);
		Functions.setReticule(min1-ecart1/20, min2-ecart2/20);
		// droite de regression
		if (avecRegression) {
			double a=covariance(t1,t2,n)/variance(n, t1);
			double b=Stat1234.getMoyenne(n, t2) -a * Stat1234.getMoyenne(n, t1);
			System.out.println("droite de regression");
			System.out.println("y = " + a + "*x" + " + " + b);
			if (a*min1+b<min2) {
				min2 = a*min1+b;
			}
			if (a*max1+b>max2) {
				max2 = a*max1+b;
			}
			ecart2 = max2 - min2;
			Functions.reset(min1-ecart1/10, max1+ecart1/10, min2-ecart2/10, max2+ecart2/10);
			Functions.setReticule(min1-ecart1/20, min2-ecart2/20);
			Functions.addLine(min1, a*min1+b, max1, a*max1+b, 2);
		}
		// affichage des points
		for (int i =0; i < n; i++) {
			dessinePoint(t1[i], t2[i], ecart1/100, ecart2/100);
		}
	}

}