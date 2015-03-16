package org.javascool.proglets.abcdStat;

/** Groupe de projet 1 : diagramme en barres (simples, empilés, juxtaposés)
 * 
 */
public class Stat1 {

	/** Dessine un diagramme en barres à partir du tableau des effectifs
	 * @param effectifs Le tableau des effectifs.
	 * @param legendes Le tableau des legendes.
	 */	
	public static void barres(int [] effectifs, String [] legendes){
		int nbModalites = legendes.length;
		// on fixe l'echelle
		double maxi = Stat1234.getMaximum(nbModalites, effectifs);
		Functions.reset(-0.5, nbModalites+0.5, -maxi/10, maxi+maxi/10);
		Functions.setReticule(-0.5, 0);
		// diagramme en barres
		for(int i=1; i<=nbModalites; i=i+1) {
			Functions.addBlock(i-1, 0, i, effectifs[i-1], Stat1234.getCouleur(i-1));
			// intervalle [i-1, i] en abscisse 
			// intervalle [0, effectifs[i-1]] en ordonnee
			Functions.addString(i-1+0.3, -maxi/20, legendes[i-1]);
		}
	}

	/** Dessine un diagramme en barres à partir de donnees brutes.
	 * @param donnees Le tableau des donnees brutes.
	 * @param modalites Le tableau des modalites (sans doublons).
	 * @param legendes Le tableau des legendes.
	 */	
	public static void barres(String [] donnees, String [] modalites, String [] legendes){
		int [] effectifs=Stat1234.getEffectifs(donnees,modalites);
		barres(effectifs, legendes);		
	}

	/** Dessine un diagramme en barres juxtaposées (croisement de 2 variables) à partir de données brutes.
	 * @param donnees1 Le tableau de données de la première variable.
	 * @param donnees2 Le tableau de données de la deuxième variable.
	 * @param modalites1 Les modalités (sans doublons) de la première variable.
	 * @param modalites2 Les modalités (sans doublons) de la deuxième variable.
	 */
	public static void barresJuxtaposees(String [] donnees1, String [] donnees2, String [] modalites1, String [] modalites2){
		int [][] contingence = getTabContingence(donnees1,modalites1,donnees2,modalites2);
		barresJuxtaposees(contingence, modalites1, modalites2);
	}

	/** Dessine un diagramme en barres juxtaposées à partir d'un tableau de contingence et de 2 tableaux de modalités.
	 * @param contingence Le tableau de contingence.
	 * @param modalites1 Le tableau de modalités de la première variable.
	 * @param modalites2 Le tableau de modalités de la deuxième variable.
	 */
	public static void barresJuxtaposees(int [][] contingence, String [] modalites1, String [] modalites2){
		int nbModalites1 = modalites1.length;
		int nbModalites2 = modalites2.length;
		// On fixe l'échelle en fonction du nombre de modalités et du nombre d'effectifs maxi du tableau de contingence
		double max_abscisse=nbModalites1*nbModalites2+nbModalites1-0.5;
		double max_ordonnee=getMaximum(nbModalites1,nbModalites2,contingence);
		Functions.reset(-0.5, max_abscisse, -max_ordonnee/10.0, max_ordonnee+max_ordonnee/10.0);
		Functions.setReticule(-0.5, 0);
		//Barres
		for(int ligne=0;ligne<nbModalites1;ligne++) {
			int x = (nbModalites2+1)*ligne;	// abscisse de la premiere barre de la modalite 1
			for(int colonne=0;colonne<nbModalites2;colonne++){
				// la barre correspondant à contingence[ligne][colonne]
				Functions.addBlock(x+colonne, 0, x+colonne+1, contingence[ligne][colonne], Stat1234.getCouleur(colonne));
				// legende de la barre correspondante
				Functions.addString(x+colonne+0.3, contingence[ligne][colonne]/2.0, modalites2[colonne]);
			}
			//Légende sur l'axe des abscisses
			Functions.addString(x+nbModalites2/2.0, -max_ordonnee/20.0, modalites1[ligne]);
		}
	}

	/** Permet d'obtenir un tableau de contingence à double entrée à partir de données brutes de 2 variables qualitatives.
	 * @param donnees1 Le tableau de données de la première variable.
	 * @param modalites1 Les modalités (sans doublons) de la première variable.
	 * @param donnees2 Le tableau de données de la deuxième variable.
	 * @param modalites2 Les modalités (sans doublons) de la deuxième variable.
	 */
	public static int [][] getTabContingence(String [] donnees1, String [] modalites1, String [] donnees2, String [] modalites2){
		int nbModalites1 = modalites1.length;
		int nbModalites2 = modalites2.length;
		//Création du tableau de contingence & initialisation à 0
		int [][] contingence= new int [nbModalites1][nbModalites2];
		for (int i=0;i<nbModalites1;i++){
			for(int j=0; j<nbModalites2; j++){
				contingence[i][j]=0;		
			}
		}
		//Remplissage du tableau de contingence :
		int nbValeurs=donnees1.length;
		int lig, col;
		for(int k=0;k<nbValeurs;k++){
			lig=getCode(donnees1[k],modalites1);
			col=getCode(donnees2[k],modalites2);
			contingence[lig][col]=contingence[lig][col]+1;
		}
		return contingence;
	}

	/** Permet d'obtenir le code d'une donnée en fonction des modalités.
	 * @param donnee La donnée à laquelle on veut attribuer un code
	 * @param modalites Le tableau de modalités.
	 */
	public static int getCode(String donnee, String [] modalites){
		int nbModalites=modalites.length;
		int code=-1; //Que fait-on dans le programme principal s'il y aune erreur ?
		for(int i=0;i<nbModalites;i++){
			if(donnee.equals(modalites[i])){
				code=i;
			}	
		}
		return code;
	}

	/** Calcule de maximum d'un tableau de contingence 
	 * @param nbLig Le nombre de lignes du tableau.
	 * @param nbCol Le nombre de colonnes du tableau.
	 * @param contingence Le tableau de contingence.
	 */
	public static int getMaximum(int nbLig, int nbCol, int [][] contingence){
		int max=contingence[0][0];
		for(int i=0;i<nbLig;i++){
			for(int j=0;j<nbCol;j++){
				if(max<contingence[i][j]){
					max=contingence[i][j];
				}
			}
		}
		return max;
	}

	/** Trace l'histogramme d'une variable quantitative
	 * @param donnees Les valeurs de la variable quantitative.
	 * @param bornes Les classes de l'histogramme.
	 */	
	public static void histogramme(double [] donnees, double [] bornes) {
		int nbValeurs = donnees.length;
		int nbBornes = bornes.length;
		int nbClasses = nbBornes-1;
		// calcul des effectifs et des hauteurs
		int [] effectifs = new int[nbClasses];
		double [] hauteur = new double[nbClasses];
		for(int i=0;i<nbClasses;i++) {
			effectifs[i]=0;
			for(int j=0;j<nbValeurs;j++){
				if(donnees[j]>=bornes[i] && donnees[j]<bornes[i+1]){
					effectifs[i]=effectifs[i]+1;
				}	
			}
			hauteur[i]=effectifs[i]/(bornes[i+1]-bornes[i]);
		}
		// on fixe l'echelle
		double min = bornes[0];
		double max = bornes[nbBornes-1];
		double hauteur_max = Stat1234.getMaximum(hauteur.length, hauteur);
		Functions.reset(min-(max-min)/10.0, max+(max-min)/10.0, -hauteur_max/10.0, hauteur_max+hauteur_max/10.0);
		Functions.setReticule(min-(max-min)/10.0, -hauteur_max/10.0);
		// diagramme en batons
		for(int i=0; i<nbClasses; i=i+1) {
			Functions.addRectangle(bornes[i], 0, bornes[i+1], hauteur[i]);
		}
		// legende
		if (nbBornes<10) {
			for(int i=0; i<nbBornes; i=i+1) {
				Functions.addString(bornes[i], -hauteur_max/20.0, ""+bornes[i]);
			}			
		}
	}

}
