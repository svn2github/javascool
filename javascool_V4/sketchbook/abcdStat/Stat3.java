package org.javascool.proglets.abcdStat;

/** Groupe de projet 3 : quartiles (par modalité), boites à moustaches
 * 
 */
public class Stat3 {

	/** Trie un tableau de donnees par ordre croissant.
	 * @param n Le nombre d'elements du tableau.
	 * @param tab Le tableau.
	 * @return Le tableau trié.
	 */
	public static double [] getTri(int n, double [] tab) {
		double [] t = new double[n];
		// recopie de tab dans t pour ne pas changer le tableau initial
		for (int i = 0; i < n; i++){
			t[i] = tab[i];
		}
		// tri de t
		for (int i = 0; i < n; i++){
			for (int k=i+1; k < n; k++){
				if (t[i] > t[k]){
					double copie = t[i];
					t[i] = t[k];
					t[k] = copie;
				}
			} 
		}
		return t;
	}

	/**	Calcule la mediane d'un tableau de reels.
	 * @param n Le nombre d'elements du tableau.
	 * @param tab Le tableau.
	 * @return La mediane.
	 */
	public static double mediane (int n, double [] tab){ 
		double [] t = getTri (n, tab);
		double mediane;
		if (n % 2>0) { // si n est impair
			mediane=t[(n-1)/2];
		}
		else {
			mediane=(t[(n/2-1)]+t[(n/2)])/2;
		}
		return mediane;
	}

	/** Calcule le premier quartile d'un tableau de reels.
	 * @param n Le nombre d'elements du tableau.
	 * @param tab Le tableau.
	 * @return Le premier quartile.
	 */
	public static double quartile_1(int n, double [] tab){ 
		double [] t = getTri (n, tab);
		int quartile_1;
		if(n % 4==0){
			quartile_1 =(n/4)-1;
		}
		else {
			quartile_1= (int)(Math.ceil(n/4));    
		}
		return t[quartile_1];
	}

	/** Calcule le troisieme quartile d'un tableau de reels.
	 * @param n Le nombre d'elements du tableau.
	 * @param tab Le tableau.
	 * @return Le troisieme quartile.
	 */
	public static double quartile_3 (int n, double [] tab) { 
		double [] t = getTri (n, tab);
		int quartile_3;
		if(n % 4==0){
			quartile_3 = (n*3/4)-1;
		}
		else {
			quartile_3= (int)(Math.ceil(n*3/4));

		}
		return t[quartile_3];
	}

	/** Trace une boite à moustache à partir d'un tableau de données.
	 *	@param t Le tableau.
	 */
	public static void boite_a_moustache (double [] t){
		int n = t.length;
		double min = Stat1234.getMinimum (n, t); 
		double q1 = quartile_1 (n, t); 
		double med = mediane (n, t); 
		double q3 = quartile_3 (n, t); 
		double max = Stat1234.getMaximum (n, t); 
		//echelle
		Functions.reset(min-2.5,max+2.5,-0.5, 20);
		Functions.setReticule(min-2.5,-0.5);
		//traits
		Functions.addLine (min,7.5,min,12.5,0);
		Functions.addLine (med,7.5,med,12.5,0);
		Functions.addLine (max,7.5,max,12.5,0);
		Functions.addLine (min,10,q1,10,0);
		Functions.addLine (q3,10,max,10,0);
		Functions.addRectangle(q1, 7.5, q3, 12.5,0);
		//valeurs
		Functions.addString (q3,19,"minimum ="+min,0);
		Functions.addString (q3,18.5,"quartile 1 ="+q1,0);
		Functions.addString (q3,18,"mediane ="+med,0);
		Functions.addString (q3,17.5,"quartile 3 ="+q3,0);
		Functions.addString (q3,17,"maximum ="+max,0);
	}

	/** Trace une boite à moustache pour chaque modalité
	 * @param valeur Les valeurs de la variable quantitative.
	 * @param qualitative Les valeurs de la variable qualitative.
	 * @param modalite Les modalites (sans doublons) de la variable qualitative.
	 */	
	public static void boite_a_moustache(double [] valeur, String [] qualitative, String [] modalite){
		int nb_modalite = modalite.length;
		int nb_qualitative = qualitative.length;
		int nb_valeur = valeur.length;
		// pour chaque modalite : calcul du minimum, du maximum et des quartiles
		double [] minimum = new double [nb_modalite];
		double [] premier_quartile = new double [nb_modalite];
		double [] mediane = new double [nb_modalite];
		double [] troisieme_quartile = new double [nb_modalite];
		double [] maximum = new double [nb_modalite];
		for (int k = 0; k < nb_modalite; k ++) {
			// extraction des valeurs associees à la modalité k
			double tab [] = new double [nb_valeur];
			int nb_val = 0;
			for (int i = 0; i < nb_qualitative; i ++) {
				if (modalite[k].equals(qualitative[i])) {
					tab[nb_val] = valeur[i];
					nb_val = nb_val+1; 
				}
			}
			// calcul du minimum, du maximum et des quartiles
			minimum[k] = Stat1234.getMinimum(nb_val, tab); 
			premier_quartile[k] = quartile_1(nb_val, tab);
			mediane[k] = mediane(nb_val, tab);
			troisieme_quartile[k] = quartile_3(nb_val, tab);
			maximum[k] = Stat1234.getMaximum(nb_val, tab);
		}
		// construction du graphique
		double min_reset = Stat1234.getMinimum(nb_valeur, valeur);
		double max_reset = Stat1234.getMaximum(nb_valeur, valeur);
		Functions.reset(min_reset -8,max_reset +8,-3, 20 * nb_modalite);
		Functions.setReticule(min_reset -8, -3);
		for (int k = 0; k < nb_modalite; k ++) {
			//construction de la boite à moustaches associée à la modalité k
			boite_a_moustache(k, modalite[k], minimum[k], premier_quartile[k], mediane[k], troisieme_quartile[k], maximum[k]);
		}
	}

	// affiche la boite à moustaches de la kieme modalite
	private static void boite_a_moustache (int k, String modalite, double min, double q1, double med, double q3, double max){
		//traits
		Functions.addLine (min,(5+20*k),min,(15+20*k),0);
		Functions.addLine (med,(5+20*k),med,(15+20*k),0);
		Functions.addLine (max,(5+20*k),max,(15+20*k),0);
		Functions.addLine (min,(10+20*k),q1,(10+20*k),0);
		Functions.addLine (q3,(10+20*k),max,(10+20*k),0);
		Functions.addRectangle (q1, (5+20*k), q3, (15+20*k), 0);
		//valeurs
		Functions.addString (min-7, (8+ 20 * k), modalite, 0);  
	}

}



