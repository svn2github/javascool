package org.javascool.proglets.visages;
import static org.javascool.macros.Macros.*;



import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;



 public class  Functions{
   //@factory
   private Functions() {}
	
   public static float[][] produit(float[][] matrice1, float[][] matrice2){
	/** 
	 *Donne une matrice égale au produit de deux matrices ayant au moins deux colonnes, codées en float.
	 * 
	 */
		int hauteur1 = matrice1.length;
		int largeur1 = matrice1[0].length;
		int hauteur2 = matrice2.length;
		int largeur2 = matrice2[0].length;
		
		float[][] matrice_produit =  new float[hauteur1][largeur2];
		if ((largeur1 == hauteur2)&&(hauteur1 == largeur2)){
		
		for(int n=0;n<hauteur1;n++){
			//System.out.println();
			//System.out.print(n + ": ");
			for (int  p = 0;p<largeur2; p++){
				
				
				float s = 0;
			for (int l = 0;l<largeur1;l++){
				s = s + matrice1[n][l]*matrice2[l][p];
				}
			matrice_produit[n][p]=s;
			//System.out.print(s+" ; ");
			}
			
		}}
		return matrice_produit;
	}
	
	
	
	public static float[][] differrence(float[][] matrice1, float[][] matrice2){
		/** 
		 *Donne une matrice égale à la différence de deux matrices ayant au moins deux colonnes, codées en float.
		 * 
		 */
		int hauteur1 = matrice1.length;
		int largeur1 = matrice1[0].length;
		int hauteur2 = matrice1.length;
		int largeur2 = matrice2[0].length;
		float[][] matrice_difference =  new float[hauteur1][largeur1];
		if ((largeur1 == largeur2)&&(hauteur1 == hauteur2)){
			
			for(int n=0;n<hauteur1;n++){
				for (int  p = 0;p<largeur1; p++){
					matrice_difference[n][p] = matrice1[n][p]-matrice2[n][p];
				}
			}
	
	}
		return matrice_difference;
	} 
	
	public static float[][] somme(float[][] matrice1, float[][] matrice2){
	
		/** 
		 *Donne une matrice égale à la somme de deux matrices ayant au moins deux colonnes, codées en float.
		 * 
		 */
		int hauteur1 = matrice1.length;
		int largeur1 = matrice1[0].length;
		int hauteur2 = matrice1.length;
		int largeur2 = matrice2[0].length;
		float[][] matrice_somme =  new float[hauteur1][largeur1];
		if ((largeur1 == largeur2)&&(hauteur1 == hauteur2)){
			
			for(int n=0;n<hauteur1;n++){
				for (int  p = 0;p<largeur1; p++){
					matrice_somme[n][p] = matrice1[n][p]+matrice2[n][p];
				}
			}
	
	}
		return matrice_somme;
	} 
	
	
	public static float[][] transposee(float[][] matrice1) 
	{
		/** 
		 *Donne une matrice égale à la transposée d'une matrice ayant au moins deux colonnes, codée en float.
		 * 
		 */
	
	
	
	int hauteur1 = matrice1.length;
	int largeur1 = matrice1[0].length;
	float[][] transposee =  new float[largeur1][hauteur1];
	for(int n=0;n<hauteur1;n++){
		
		for (int  p = 0;p<largeur1; p++){
			transposee[p][n] = matrice1[n][p];
			
		}
	}
	return transposee;
	
			
	}
	
	public static float[][] proportionnelle(float[][] matrice1, float coef){
		/** 
		 *Donne une matrice égale au produit d'une matrice ayant au moins deux colonnes par une constante codées en float.
		 * 
		 */
		int hauteur1 = matrice1.length;
		int largeur1 = matrice1[0].length;
		float[][] matrice_proportionnelle =  new float[hauteur1][largeur1];
		for(int n=0;n<hauteur1;n++){
			for (int  p = 0;p<largeur1; p++){
				matrice_proportionnelle[n][p] = coef*matrice1[n][p];
				
			}
		}
		return matrice_proportionnelle;
		
		
		
	}
	
	public static float[][] somme_constante(float[][] matrice1, float coef){
		/** 
		 *Donne une matrice égale à la somme de chaque élements d'une matrice ayant au moins deux colonnes et d'une même constante , codées en float.
		 * 
		 */
		int hauteur1 = matrice1.length;
		int largeur1 = matrice1[0].length;
		float[][] matrice_somme =  new float[hauteur1][largeur1];
		for(int n=0;n<hauteur1;n++){
			for (int  p = 0;p<largeur1; p++){
				matrice_somme[n][p] = coef+matrice1[n][p];
				
			}
		}
		return matrice_somme;
		
		
		
	}
	public static float[][] valeur_absolue(float[][] matrice1){
		/** 
		 *Donne une matrice égale à la valeur absolue d'une matrice ayant au moins deux colonnes, codée en float.
		 * 
		 */
		int hauteur1 = matrice1.length;
		int largeur1 = matrice1[0].length;
		float[][] matrice_val_abs =  new float[hauteur1][largeur1];
		for(int n=0;n<hauteur1;n++){
			for (int  p = 0;p<largeur1; p++){
				matrice_val_abs[n][p] = Math.abs(matrice1[n][p]);
				
			}
		}
		return matrice_val_abs;
		
		
		
	}
	
	public static float[][] racine(float[][] matrice1){
		/** 
		 *Donne une matrice égale à la racine carrée d'une matrice ayant au moins deux colonnes, codées en float.
		 * 
		 */
		int hauteur1 = matrice1.length;
		int largeur1 = matrice1[0].length;
		float[][] matrice_racine =  new float[hauteur1][largeur1];
		for(int n=0;n<hauteur1;n++){
			for (int  p = 0;p<largeur1; p++){
				if (matrice1[n][p]>=0){
				matrice_racine[n][p] =(float) Math.sqrt(matrice1[n][p]);}
				else {matrice_racine[n][p]=0;}
				
			}
		}
		return matrice_racine;
		
		
		
	}
 public static void calcul_vecteurs_propres(float[][] covariance,  int nbvecteurspropres ){
 	/** 
 	 * Calcul des  nbvecteurpropres  premiers vecteurs propres et valeurs propres d'une matrices ayant au moins deux colonnes, codées en float.
 	 * Le classement est obtenu dans l'ordre décroissant des valeurs propres. 
	 *  Les valeurs propres sont calculées par la méthode des puissances ittérées.
	 * La fonction enregistre deux fichiers "texte":
	 *		- un fichier "valeurs_propres.txt" contenant les valeurs propres classées par ordre décroissant, avec une valeur par ligne
	 * 		- un fichier "vecteurs_propres.txt" contenant les composantes de chaque vecteur popre : chaque colonne est un vecteur propre, 
	 *                   les composantes de rang i sont sur la même ligne de numéro i séparées par un point virgule
 	 */
			int nombre = nbvecteurspropres;//nombre de vecteurs propres choisi
		  	int dimension= covariance.length;
			float[] image0 = new float[dimension]	;
			float[][] image_propre= new float[dimension][nombre];
			float[] b = new float[dimension];	
			float norme_image;
			float[] val_propre0 = new float[nombre], val_propre = new float[nombre];
			float[][] Bcov = new float[dimension][dimension];
			float[] valeurs_propres= new float[nombre];
			
			for(int lin = 0; lin<dimension;lin++){
			
				for(int col = 0; col<dimension;col++){	
					
					Bcov[lin][col]=covariance[lin][col];
					
				}
			}
			
			
			
			int num = 0;
			while (num<nombre){
				//Calcul de la matrice B = A - lambda(num-1)*u(num-1)*t(vnum-1)
				if(num>0){
					double norme_carree = 0;
					for(int lin = 0; lin<dimension;lin++){
							
							norme_carree = norme_carree+ image_propre[lin][num-1]*image_propre[lin][num-1];
								
					}	
					
					
				for(int lin = 0; lin<dimension;lin++){
					for(int col = 0; col<dimension;col++){	
						
						Bcov[lin][col]=(float)( Bcov[lin][col]-(val_propre[num-1]*image_propre[lin][num-1]*image_propre[col][num-1]/norme_carree));
						
					}
				}
				
			}
				val_propre0[num]=1;
				val_propre[num]=0;
				int l = 0;
			while(l<dimension) {
			image0[l]= 100;	
			l = l + 1;}
			
			while(Math.abs(val_propre0[num] - val_propre[num])>0.3){//calcul de la valeur propre lambda_num et du vecteur propre
			l = 0;norme_image= 0;
			val_propre0[num] = val_propre[num];
				while (l<dimension){
					norme_image = norme_image+image0[l]*image0[l];
				l = l+1;}
				norme_image=(float) Math.sqrt(norme_image);
			l = 0;
			while (l<dimension){
			b[l] = image0[l]/norme_image;
			l = l+1;}
			int lin =0, col = 0;
			val_propre[num] = 0;
			for(lin = 0; lin<dimension;lin++){
				
				float s= 0;
				for(col = 0; col<dimension;col++){
				s = s+ Bcov[lin][col]*b[col];
				}
				image0[lin] = s; 
				val_propre[num] =val_propre[num]  + b[lin]*image0[lin];
				}
			//System.out.println(val_propre[num]);
				}//fin du calcul de lamda_num
			valeurs_propres[num]= (int)val_propre[num];
			
			
			
				
					
			
			//Chargement et sauvegarde de l'image _propre num
			l=0;
			norme_image = 0;
			while (l<dimension){
				norme_image = norme_image+image0[l]*image0[l];
			l = l+1;}
			norme_image=(float) Math.sqrt(norme_image);
			
			l=0;
			while (l<dimension){
				image_propre[l][num] =  image0[l]/norme_image;
				l = l+1;}
			
			num++;}
			
			String chemin_travail  = System.getProperty("user.dir");
			sauvegarde_text(image_propre,valeurs_propres);// sauvegarde des vecteurs propres et valeurs propres.
			
						}
	  
  
public static float[] image_vers_vecteur_image(String nom,int largeur,int hauteur) {
 		/** 
 		*Transforme une image de dimension largeurxhauteur en une matrice unicolonne: pixels sont rangées dans l'ordre des lignes : 
		* les n pixels d'une lignes se trouvent les uns aux dessus des autres dans l'odre de la lecteur de la ligne.
 		* 
 		*/
 		int[][] pixel;
 		
 		
 		BufferedImage images;
 			float[] vecteur_image= new float[largeur*hauteur];
 			pixel= new int[largeur][hauteur];
 		ouverture_image ouverture_image= new ouverture_image();
 			images = ouverture_image.ouverture_image(nom);	
 		//BufferedImage imageint=images;
 				System.out.println(nom);
 				int width1 = images.getWidth();
 				int height1 = images.getHeight();
 				
 				
 				
 				int m = 0;
 				for (int i = 0; i < height1; i++) {
 			
 					for (int j = 0; j < width1; j++) {
 										
 						 
 								int luma;//=  images[n].getRGB(i, j)& 0xFFFFFF;;
 						 pixel[i][j]= images.getRGB(i, j);
 						   short alpha = (short)(( pixel[i][j] >> 24) & 0x000000FF);
 						 short rouge = (short)(( pixel[i][j] >> 16) & 0x000000FF);
 						short vert = (short)(( pixel[i][j] >> 8 )& 0x000000FF);
 						short bleu = (short)(( pixel[i][j] ) & 0x000000FF);
 						
 						//Test image couleur ou non
 						//short luma;
 						
 						if ((rouge==bleu)&&(bleu==vert)){
 						luma = rouge;	
 							}
 						
 						else {luma = (short)((77*rouge + 151*vert + 28*bleu)/256);}//((bleu + rouge +vert)/3);}*/
 						 
 						 
 						 
 						 //if (luma>255){System.out.println("Valeur erronéée de luma");
 						//System.out.println(m);
 						 vecteur_image[m]=luma;
 						 //System.out.println("image "+ p + " pixel "+ m  +" ; alpha = : "+alpha);
 						/*if (pixel[n][i][j]==0){
 							System.out.println("image "+n+" pixel "+i+j+" nul"); 
 						 }*/
 						
 						 m=m+1;	
 			}	
 			}
 				//System.out.println(nom_image+"; largeur = "+ width1 + "; hauteur = " +height1+ ";dim = "+ m);
 		
 		return vecteur_image;
 		
 		}	
	
	public static void sauvegarde_text(float[][] image_propre,float[] valeurs_propres ){
			/** 
			 *Sauvegarde des vecteurs propres et valeurs propres dans deux fichiers textes distincts.
			 * 
			 */
	 			String nom_fichier3="vecteurs_propres.txt";
	 			String nom_fichier4="valeurs_propres.txt";
	 			int dimension = image_propre.length;
	 			int nombre = image_propre[0].length;
	 			try {		
	 			PrintWriter f3 = new PrintWriter(new FileWriter(nom_fichier3));
	 			
	 				for (int m= 0;m<dimension;m++){
	 					
	 					for (int n= 0;n<nombre;n++){	
	 						float s= image_propre[m][n];
	 				f3.print(s+";");	
	 				
	 				}f3.println();}
	 				f3.close();
	 				PrintWriter f4 = new PrintWriter(new FileWriter(nom_fichier4));
	 				for (int n = 0; n<nombre;n++){
	 					f4.println(valeurs_propres[n]);
	 					
	 				}
	 				f4.close();
	 				
	 				
	 				
	 			} 
	 			catch (FileNotFoundException e) {e.printStackTrace();}
	 			catch (IOException e){};
	 			
	 			;	 
	 		 
	 	 }
		  
		  
		
public static float[] moyenne(float[][] vecteurs_images,int dimension,int nombre_image){
		/** 
		 *Donne une matrice unicolonne dont chaque composante est égale à moyenne  des composantes d'une ligne d'une matrice  codée en float.
		 * 
		 */
		  		 float[] image_moyenne = new float[dimension];
		  		 for(int n = 0;n<dimension;n++){
		  			 for(int p = 0;p<nombre_image;p++){
		  				 image_moyenne[n] = image_moyenne[n] + vecteurs_images[n][p];
		  			 }
		  			 image_moyenne[n] = image_moyenne[n]/nombre_image;	 
		  		 }
		  		 
		  		 return image_moyenne;}
				   
				   
				   
				   
		   public static void sauvegardejpg(float[] vecteur_image, String nom, int largeur, int hauteur){
		   
		   		/** 
		   		 *Sauvegarde d'un vecteur image unicolonne en image jpg.
		   		 * 
		   		 */
				     		 int p = 0, i=0, j=0, k=1;
				     		 int dimension = vecteur_image.length;
				     			int val_pixel; 
				     			File f=new File(nom);
				     			 BufferedImage image= new BufferedImage(largeur,hauteur, BufferedImage.TYPE_BYTE_GRAY);
				     			while (p<dimension)
				     			{
				     				
				     				val_pixel = (int) (vecteur_image[p]);
				     			
				     				int intens= (255<<24)+(val_pixel<< 16) +  (val_pixel<<8)+ (val_pixel);//décalage de 50 et valeur absolue de la valeur pour une représentaion "humaine
				     			 
				     			if (((p%largeur)==0)&&(p!=0)){i = i+1;j= 0;
				     		
				     				}
				     			
				     				image.setRGB(i,j,intens);
				     								
				     			j = j+1;	
				     			p = p+ 1;
				     			}	
				     			
				     			
				     			try {
				     				boolean success = ImageIO.write(image, "JPG", f);
				     				System.out.println(nom);
				     			} catch (IOException e) {
				     				
				     				e.printStackTrace();
				     			}
				     		 
				     	 }
					      
	public static float[][] lecture_tableau(int lignes, int colonnes, String nom_fichier)throws IOException{
			/** 
			 *Lit un fichier texte contenant un tableau de n lignes, p colonnes  codé  en float, 
			 * Chaque composante d'une ligne doit être  séparée de l'autre par un point virgule dans le fichier texte..
			 */
					     			float[][] vecteurs_propres= new float[lignes][colonnes];
					     		
					     			String ligne="";
					     			
					     			BufferedReader entree;
					     			
					     				entree = new BufferedReader(new FileReader(nom_fichier));
					     			
					     			int p = 0, num = 0;
					     			do 
					     			{ligne = entree.readLine();
					     			int longueur_ligne=ligne.length(),n1 =0 ,n2=0;
					     			char c =new Character(';');
					     			
					     			for (int n = 0;n<longueur_ligne;n++){
					     				if (ligne.charAt(n)==c)
					     				{n2 = n;
					     				vecteurs_propres[p][num]= Float.parseFloat(ligne.substring(n1, n2));
					     				
					     				
					     				n1 = n+1;	 
					     				num = num+1;}	
					     				
					     			}
					     			
					     	
					     			num=0;
					     			p++;}
					     			
					     			while (p != lignes);
					     			entree.close();	
					     			
					     			return vecteurs_propres;
								     }
								     
	public static float produit_scalaire(float[] vecteur1, float[] vecteur2){
			/** 
			 *Donne le produit scalaire de deux vecteurs  codés en float.
			 * 
			 */
	
	float produit_scalaire = 0;
	int dim1 = vecteur1.length;
	int dim2 = vecteur2.length;
	if (dim1==dim2) {
	float s = 0;
	for (int n= 0;n<dim1;n++){
	s =  s+ vecteur1[n]*vecteur2[n];
	}
	produit_scalaire= s;
	}
	else{System.out.println("Attention vecteur de dimensions différentes : calcul imposible ! Retourne 0");
	}
	
	return produit_scalaire;
	}	     
								     
	public void sauvegarde(float[][] tableau, String nom_fichier){
			/** 
			 *Sauvegarde d'un tableau de float ayant n colonnes et p lignes,  dans un fichier texte ayant n colonnes, p lignes .
			 *Les composantes sont séparées par un point virgule.
			 */
								     	
		int nombre_ligne = tableau.length;
		int nombre_colonnes = tableau[0].length;
		try {		
				PrintWriter f3 = new PrintWriter(new FileWriter(nom_fichier));
								     		
				for (int m= 0;m<nombre_ligne;m++){
													     				
			for (int n= 0;n<nombre_colonnes;n++){	
				f3.print(tableau[m][n]+";");	
								     			
		}f3.println();}
		f3.close();
		} 
		catch (FileNotFoundException e) {e.printStackTrace();}
		catch (IOException e){};
	;
								     		
}							     
		  
 }//fin classe Function 
 
 
class ouverture_image{
		/** 
		 *Ouvre une image et la stocke dans une image mémoire "BufferedImage" .
		 * 
		 */
 	 public BufferedImage ouverture_image(String nom){
 	  			BufferedImage image;
 	  			
 	  			try {
 	  				
 	  				 image = ImageIO.read(new File(nom));
 	  				return image;
 	  			} catch (IOException e) {
 	  				// TODO Auto-generated catch block
 	  				e.printStackTrace();
					   System.out.println("Erreur lors de la lecture du fichier image");
 	  			}
 	  			return null;
 	  						
 	  			} 
 }

 
  class image_texte{
  		/** 
  		 *Donne une matrice unicolonne  à partir  d'une matrice ayant au moins deux colonnes, codées en float.
  		 * La matrice est lue ligne par ligne et les composantes d'une ligne sont classées dans l'ordre les unes à la suite des autres. 
  		 */
 	 public float[][] image_tableau(float[] image){
 		 int dimension = image.length;
 		 int p = 0,q=0;
 		 float[][] tableau = new float[50][50];
 		 for (int n = 0;n<dimension;n++){
 		  
 			if ((n%50==0)&&(p!=0)){p=0;q = q+1;}
 			tableau[q][p] = image[n];
 			 
 		 p++;}
 		 
 		return tableau; 
 		 
 	 }
 	
 	 
 	 
  }
  class sauvegarde_jpg{
  			
  	 
  	 public void sauvegardejpg(float[] vecteur_image, String nom, int largeur, int hauteur){
	   			/** 
	     			 *Sauvegarde d'un vecteur unicolonne en une image jpg de dimension largeurxhauteur.
	     			 * La dimension du vecteur doit être égale à largeurxhauteur.
	     			 */
  		 int p = 0, i=0, j=0, k=1;
  		 int dimension = vecteur_image.length;
  			int val_pixel; 
  			File f=new File(nom);
  			 BufferedImage image= new BufferedImage(largeur,hauteur, BufferedImage.TYPE_BYTE_GRAY);
  			while (p<dimension)
  			{
  				
  				val_pixel = (int) (vecteur_image[p]);
  			
  				int intens= (255<<24)+(val_pixel<< 16) +  (val_pixel<<8)+ (val_pixel);//décalage de 50 et valeur absolue de la valeur pour une représentaion "humaine
  			 
  			if (((p%largeur)==0)&&(p!=0)){i = i+1;j= 0;
  		
  				}
  			
  				image.setRGB(i,j,intens);
  								
  			j = j+1;	
  			p = p+ 1;
  			}	
  			
  			
  			try {
  				boolean success = ImageIO.write(image, "JPG", f);
  				System.out.println(nom);
  			} catch (IOException e) {
  				
  				e.printStackTrace();
  			}
  		 
  	 }
  }
  
 
