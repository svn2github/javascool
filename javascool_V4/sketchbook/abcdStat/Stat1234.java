package org.javascool.proglets.abcdStat;

/** Méthodes communes à plusieurs projets.
 * 
 */
public class Stat1234 {

	/** Calcule le minimum d'un tableau de reels.
	 * @param n Le nombre d'elements du tableau.
	 * @param t Le tableau.
	 * @return Le minimum.
	 */	
	public static double getMinimum(int n, double [] t) {
		double mini = t[0];
		for(int i=0; i<n; i++) {
			if (t[i]<mini) {
				mini = t[i];
			}
		}
		return mini;
	}

	/** Calcule le minimum d'un tableau d'entiers.
	 * @param n Le nombre d'elements du tableau.
	 * @param t Le tableau.
	 * @return Le minimum.
	 */	
	public static int getMinimum(int n, int [] t) {
		int mini = t[0];
		for(int i=0; i<n; i++) {
			if (t[i]<mini) {
				mini = t[i];
			}
		}
		return mini;
	}

	/** Calcule le maximum d'un tableau de reels.
	 * @param n Le nombre d'elements du tableau.
	 * @param t Le tableau.
	 * @return Le maximum.
	 */	
	public static double getMaximum(int n, double [] t) {
		double maxi = t[0];
		for(int i=0; i<n; i++) {
			if (t[i]>maxi) {
				maxi = t[i];
			}
		}
		return maxi;
	}

	/** Calcule le maximum d'un tableau d'entiers.
	 * @param n Le nombre d'elements du tableau.
	 * @param t Le tableau.
	 * @return Le maximum.
	 */	
	public static int getMaximum(int n, int [] t) {
		int maxi = t[0];
		for(int i=0; i<n; i++) {
			if (t[i]>maxi) {
				maxi = t[i];
			}
		}
		return maxi;
	}

	/** Calcule la somme totale d'un tableau de données
	 * @param n Le nombre d'elements du tableau.
	 * @param t Le tableau.
	 * @return La somme totale.
	 */	
	public static double getSomme(int n, double [] t){
		double somme=0;
		for(int i=0; i<n; i++) {
			somme=somme+t[i];
		}
		return somme;
	}
	 
	/** Calcule la moyenne d'un tableau de données
	 * @param n Le nombre d'elements du tableau.
	 * @param t Le tableau.
	 * @return La moyenne.
	 */	
	public static double getMoyenne(int n, double [] t) {
		double somme = getSomme(n,t);		
		double moy=somme/n;
		return moy;
	}
	
	/** Calcule les effectifs de chaque modalite à partir de donnees brutes.
	 * @param donnees Le tableau des donnees brutes.
	 * @param modalites Le tableau des modalites (sans doublons).
	 * @return Le tableau des effectifs.
	 */	
	public static int [] getEffectifs(String [] donnees, String [] modalites) {
		int n = modalites.length;
		int nbValeurs=donnees.length;
		int [] effectifs = new int[n];		
		//Initialisation du tableau t_effectifs à 0 pour pouvoir s'en servir comme compteur pour chaque modalité
		int i;
		for(i=0; i<n; i++) {
			effectifs[i]=0;
		}
		//Comptage de l'effectif de chaque modalité dans le tableau de données brutes
		int j,k;
		for(k=0; k<n; k++){
			for (j=0; j<nbValeurs; j++){
				if (donnees[j].equals(modalites[k])){
					effectifs[k] = effectifs[k] + 1;
				}
			}
		}
		return effectifs;
	}

	/** Associe une couleur à une modalité
	 * @param i Le numéro de la modalité.
	 * @return Sa couleur pour la proglet algoDeMath.
	 */	
	public static int getCouleur(int i) {
		// couleurs de algoDeMath : 0 (noir), 1 (brun), 2 (rouge), 3 (orange), 4 (jaune), 5 (vert), 6 (bleu), 7 (violet), 8 (gris), 9 (blanc).
		int [] mesCouleurs = {2, 4, 1, 3, 5, 8};
		return mesCouleurs[i%mesCouleurs.length] ;
	}
	
	/** Cherche les modalites (sans doublons) à partir de donnees brutes.
	 * @param donnees Le tableau des donnees brutes.
	 * @return modalites Le tableau des modalites (sans doublons).
	 */	
	public static String [] oterDoublons(String [] donnees) {
		int n = donnees.length;
		String [] modalites = new String[n];
		int nbModalites = 1;
		modalites[0] = donnees[0];
		// recherche des autres modalites
		for(int k=1; k<n; k++) {
			// on cherche si donnees[k] est une nouvelle modalite
			boolean nouveau = true;
			for(int i=0;i<nbModalites;i++){
				if(donnees[k].equals(modalites[i])){
					nouveau = false;
				}	
			}
			// on ajoute donnees[k] si nouvelle modalite
			if (nouveau) {
				modalites[nbModalites] = donnees[k];
				nbModalites = nbModalites+1;
			}
		}
		// reduction de la taille du tableau des modalites
		String [] modas = new String[nbModalites];
		for(int i=0;i<nbModalites;i++){
			modas[i] = modalites[i];
		}
		return modas;
	}
}
