package org.javascool.proglets.abcdStat;

/** Groupe de projet 4 : diagramme camembert, en anneaux, en batons
 * 
 */
public class Stat4 {

	/** Dessine un diagramme en anneaux à partir du tableau des effectifs
	 * @param effectifs Le tableau des effectifs.
	 * @param legendes Le tableau des legendes.
	 */	
	public static void anneaux(int [] effectifs, String [] legendes){	
		int nbValeurs = effectifs.length;
		int i;
		double total = 0;
		// Calcul du total des effectifs  
		for (i =0; i < nbValeurs; i ++) {
			total = effectifs[i] + total;
		}
		// Dessin du cercle de base    
		double [] freq = new double [nbValeurs];
		Functions.reset( -1.5, 1.5, -1.5, 1.5);
		Functions.setReticule(-1.5, -1.5);
		Functions.addCircle(0, 0, 1, 0);
		Functions.addCircle(0, 0, 0.5, 0);
		// Calcul de la fréquence, de la fréquence cumulée et de l'angle 
		double Fc = 0;
		double [] angle = new double [nbValeurs];
		double [] cos_teta = new double [nbValeurs];
		double [] sin_teta = new double [nbValeurs];
		double [] angL = new double [nbValeurs];
		double FcL = 0;
		double [] cos_tetaL = new double [nbValeurs];
		double [] sin_tetaL = new double [nbValeurs];
		for (i =0; i < nbValeurs; i ++) {
			freq [i] = ((effectifs[i] / total));
			Fc = Fc + freq[i];
			FcL = Fc - freq[i] /2;
			angle[i] = Fc *2 * Math.PI;
			angL[i] = FcL * 2 * Math.PI;
			//Calcul du cos et du sin
			cos_teta[i] = Math.cos(angle[i]);
			sin_teta[i] = Math.sin(angle[i]);
			cos_tetaL[i] = Math.cos(angL[i]);
			sin_tetaL[i] = Math.sin(angL[i]);
		}
		// Remplissage de chaque portion
		double deg =0;
		for (i =0; i < nbValeurs; i ++) {
			if (i >0) {
				while (deg < angle[i] && deg > angle[i -1]) {
					Functions.addLine(Math.cos(deg) *0.5, Math.sin(deg) *0.5, Math.cos(deg), Math.sin(deg), Stat1234.getCouleur(i));
					deg = deg + Math.PI /72000;
				}
			} else {
				while (deg < angle[i]) {
					Functions.addLine(Math.cos(deg) *0.5, Math.sin(deg) *0.5, Math.cos(deg), Math.sin(deg), Stat1234.getCouleur(i));
					deg = deg + Math.PI /72000;
				}
			}
		}
		// Affichage des légendes
		for (i =0; i < nbValeurs; i ++) {
			Functions.addString(cos_tetaL[i] *1.25, sin_tetaL[i] *1.2, legendes[i], 0);
		}
	}

	/** Dessine un diagramme en camembert à partir du tableau des effectifs
	 * @param effectifs Le tableau des effectifs.
	 * @param legendes Le tableau des legendes.
	 */		
	public static void camembert(int [] effectifs, String [] legendes){	
		int nbValeurs = effectifs.length;
		int i;
		double total = 0;
		// Calcul du total des effectifs  
		for (i =0; i < nbValeurs; i ++) {
			total = effectifs[i] + total;
		}
		// Dessin du cercle de base    
		double [] freq = new double [nbValeurs];
		Functions.reset( -1.5, 1.5, -1.5, 1.5);
		Functions.setReticule(-1.5, -1.5);
		Functions.addCircle(0, 0, 1, 0);

		// Calcul de la fréquence, de la fréquence cumulée et de l'angle 
		double Fc = 0;
		double [] angle = new double [nbValeurs];
		double [] cos_teta = new double [nbValeurs];
		double [] sin_teta = new double [nbValeurs];
		double [] angL = new double [nbValeurs];
		double FcL = 0;
		double [] cos_tetaL = new double [nbValeurs];
		double [] sin_tetaL = new double [nbValeurs];
		for (i =0; i < nbValeurs; i ++) {
			freq [i] = ((effectifs[i] / total));
			Fc = Fc + freq[i];
			FcL = Fc - freq[i] /2;
			angle[i] = Fc *2 * Math.PI;
			angL[i] = FcL * 2 * Math.PI;
			//Calcul du cos et du sin
			cos_teta[i] = Math.cos(angle[i]);
			sin_teta[i] = Math.sin(angle[i]);
			cos_tetaL[i] = Math.cos(angL[i]);
			sin_tetaL[i] = Math.sin(angL[i]);
		}
		// Remplissage de chaque portion
		double deg =0;
		for (i =0; i < nbValeurs; i ++) {
			if (i >0) {
				while (deg < angle[i] && deg > angle[i -1]) {
					Functions.addLine(0, 0, Math.cos(deg), Math.sin(deg), Stat1234.getCouleur(i));
					deg = deg + Math.PI /72000;
				}
			} else {
				while (deg < angle[i]) {
					Functions.addLine(0, 0, Math.cos(deg), Math.sin(deg), Stat1234.getCouleur(i));
					deg = deg + Math.PI /72000;
				}
			}
		}
		// Affichage des légendes
		for (i =0; i < nbValeurs; i ++) {
			Functions.addString(cos_tetaL[i] *1.25, sin_tetaL[i] *1.2, legendes[i], 0);
		}	
	}

	/** Trace le diagramme en batons d'une variable quantitative discrete
	 * @param donnees Les valeurs de la variable quantitative.
	 */	
	public static void batons(int [] donnees) {
		int min = Stat1234.getMinimum(donnees.length, donnees);
		int max = Stat1234.getMaximum(donnees.length, donnees);
		int n = donnees.length;
		// calcul des effectifs
		int [] effectifs = new int[max-min+1];
		for(int i=min; i<=max; i=i+1) {
			effectifs[i-min]=0;
		}
		for(int i=0; i<n; i=i+1) {
			int val = donnees[i];
			effectifs[val-min]=effectifs[val-min]+1;
		}
		// on fixe l'echelle
		int eff_max = Stat1234.getMaximum(effectifs.length, effectifs);
		Functions.reset(-1.5+min, max+1.5, -eff_max/10.0, eff_max+eff_max/10.0);
		Functions.setReticule(-1.5+min, 0);
		// diagramme en batons
		for(int i=min; i<=max; i=i+1) {
			Functions.addLine(i, 0, i, effectifs[i-min], 2);
		}
		// legende
		if (max-min<10) {
			for(int i=min; i<=max; i=i+1) {
				Functions.addString(i, -eff_max/20.0, ""+i);
			}			
		}
		else {	
			int pas = (max-min)/5;
			for(int i=min; i<=max; i=i+pas) {
				Functions.addString(i, -eff_max/20.0, ""+i);
			}				
		}
	}

}