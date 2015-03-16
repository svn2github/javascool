package org.javascool.proglets.visages;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.awt.FontMetrics;
import javax.imageio.ImageIO;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;

import java.awt.Color;

import org.javascool.widgets.NumberInput;
import org.javascool.macros.Macros;

/** Définit une proglet javascool qui permet d'implémenter un programme de reconnaissance de visage.
 *
 * @see <a href="Panel.java.html">code source</a>
 * @serial exclude
 */
public class Panel extends JPanel {
  private static final long serialVersionUID = 1L;
private JScrollPane j;
//private Container contain;
  // @bean
 public Panel() {
 		
	 //this.setSize(900,1000);	  
	
	//contain= new Container();
	//contain = this.getParent();
	//this.add(new Panneau());
		   j = new JScrollPane(new Panneau());
		   //this.setLayout(new GridLayout(1, 1));
		    j.setVisible(true);
		   j.setPreferredSize(new Dimension(800,750));
		   j.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		   j.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		   //j.setViewportBorder(new LineBorder(Color.RED));
		   add(j);    
		   j.setAutoscrolls(true);
		//	this.add(j); 
		    
		  
 	  	 
 	      
 	  }
	    protected void paintComponent(Graphics g) {
	               super.paintComponent(g);
	   
	               
	               }
	   
class Panneau extends	  JPanel{ 
private static final long serialVersionUID = 1L;
 	 //Container c = getContentPane(); 
	  //
 
     public Panneau(){	
     	String chemin="C:\\javascool\\sketchbook\\visages\\";
     	 setPreferredSize(new Dimension(900, 1000)); 
	       	       
      	     this.setLayout(new GridLayout(11, 1));
		 
     for (int n = 0; n <= 10; n++) {
 	
 		JPanel affichette = new JPanel();
 		
 		if(n==0){
 			affichette.setLayout(new GridLayout(1, 7));
 			this.add(affichette);
		affichette.setBackground(Color.YELLOW);	 
 		 for (int p=0;p<7;p++){
 	       
 	      affiche affiche = new affiche();
	       
 	      
 	      int m=p+1;
 	      affiche.setnom_image(chemin+"visage"+m+".jpg");
 	      affiche.settexte("visage"+m);
 	      affichette.add(affiche);}
 		}
		 
		 
		 
 		if (n ==1){
 			 affichette.setLayout(new GridLayout(1,1));
 	    	 this.add(affichette);
 			
 			
 		      		       affiche affiche_fleche=new affiche();
 		      		       affiche_fleche.setBackground(Color.YELLOW);
 		      		       affiche_fleche.setnom_image(chemin+"fleche_bas.jpg");
					      affiche_fleche.settexte("Calcul du visage moyen");
					      affichette.add(affiche_fleche);
 		      	 
				}
			if(n==2){
				 	    
				 	    	
				 	    	 affiche affiche = new affiche();
						      affichette.setLayout(new GridLayout(1,1));
						       	    	 this.add(affichette);
				 	   	      
				 	   	      setBackground(Color.YELLOW);
				 	   	    
						      
				 	    	 affiche.setnom_image(chemin+"image_moyenne.jpg");
				 	     affiche.settexte("visage moyen");
					      affiche.setAlignmentX(CENTER_ALIGNMENT);
				 	      affichette.add(affiche);}		
				
				
		if (n ==3){		
				
				affichette.setLayout(new GridLayout(2,1));
 		      		this.add(affichette);	       
					affichette.setBackground(Color.YELLOW);			    
			 JLabel affichelabel2 = new JLabel();
			affichelabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			affichelabel2.setOpaque(true);
			affichelabel2.setBackground(Color.YELLOW);
		       affichelabel2.setText("Puis calcul de la différence entre chaque visage et le visage moyen pour obtenir les visages normalisés.");
		       affichette.add(affichelabel2);
		       
		       affiche affiche_fleche=new affiche();
		       
		        affiche_fleche.setBackground(Color.YELLOW);
		        affiche_fleche.setnom_image(chemin+"fleche_bas.jpg");
		       	affiche_fleche.settexte("  ");
			 affiche_fleche.setsize(50, 50);      
			affichette.add(affiche_fleche, new GridBagConstraints (0, 0, 1, 1, 0, 0,GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets (0,0,0,0), 0, 0));
		       
		       
		       
		       
 		}
 
 	    	 
 	     
 	      
 	      
 	      
 	     if (n==4){
 	    	 affichette.setLayout(new GridLayout(1, 7));
 			this.add(affichette);
 	    	 
 	    	 affichette.setBackground(Color.YELLOW);	
 	    	 
 	    	 for (int p=1;p<8;p++){ 
 	    		 affiche affiche = new affiche();
 	   	      	affiche.setBackground(Color.YELLOW);
 	    		 	    		 
 	    	 affiche.setnom_image(chemin+"image_normalisee"+p+".jpg");
 	     	affiche.settexte("visage normé"+p);
 	      	affichette.add(affiche);}}
		       
		if (n ==5){
		         affichette.setLayout(new GridLayout(1,1));
		        this.add(affichette);
		        		 			
		        		 	
		         affiche affiche_fleche=new affiche();
			affiche_fleche.setBackground(Color.YELLOW);
		        affiche_fleche.setnom_image(chemin+"fleche_bas.jpg");
		        affiche_fleche.settexte("Calcul des visages propres et des composantes principales des images intitales");
		        affichette.add(affiche_fleche, new GridBagConstraints (0, 0, 1, 1, 0, 0,GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets (0,0,0,0), 0, 0));
		        		 		      	 
		   	}	
		       
		       
 	     if (n==6){
 	    	 affichette.setLayout(new GridLayout(1, 5));
 			this.add(affichette);
 	    	 affichette.setBackground(Color.YELLOW);	
 	    	 
 	    	 
 	    	 for (int p=0;p<5;p++){ 
 	    		 affiche affiche = new affiche();
 	   	      	affiche.setBackground(Color.YELLOW);
 	    		 	    		 
 	    	 affiche.setnom_image(chemin+"visage_propre"+p+".jpg");
 	    	 affiche.settexte("visage propre"+p);
 	     	 affichette.add(affiche);}}
	
	if (n==7){
	 affichette.setLayout(new GridLayout(1,1));
			        this.add(affichette);
			        		 			
			        		 	
			         affiche affiche_fleche=new affiche();
				affiche_fleche.setBackground(Color.YELLOW);
			        affiche_fleche.setnom_image(chemin+"fleche_bas.jpg");
			        affiche_fleche.settexte("Reconstruction des images initiales à partir  de leurs composantes principales et des  visages propres.");
			        affichette.add(affiche_fleche, new GridBagConstraints (0, 0, 1, 1, 0, 0,GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets (0,0,0,0), 0, 0));
			        
	
	
	}	       
		       
		       
		       
		       
		       
 	     
 	     if (n==8){
 	    	 affichette.setLayout(new GridLayout(1, 7));
 			this.add(affichette);
 	    	 affichette.setBackground(Color.YELLOW);	
 	    	 
 	    	 
 	    	 for (int p=0;p<7;p++){ 
 	    		 int m = p+1;
 	    		 affiche affiche = new affiche();
 	   	      affiche.setBackground(Color.YELLOW);
 	    		 	    		 
 	    	 affiche.setnom_image(chemin+"image_reconstruite"+p+".jpg");
 	     affiche.settexte("visage reconstruit"+m);
 	      affichette.add(affiche);}
 	     
 	      	     }
 	     
 	      if (n==9){affichette.setLayout(new GridLayout(1, 1));
 	       			this.add(affichette);
 	       	    	 affichette.setBackground(Color.YELLOW);
	       affiche affiche_fleche=new affiche();
	       				affiche_fleche.setBackground(Color.YELLOW);
	       			        affiche_fleche.setnom_image(chemin+"fleche_bas.jpg");
	       			        affiche_fleche.settexte("Reconnaissance d'un visage inconnu");
	       			        affichette.add(affiche_fleche, new GridBagConstraints (0, 0, 1, 1, 0, 0,GridBagConstraints.CENTER,GridBagConstraints.CENTER, new Insets (0,0,0,0), 0, 0));
	       		
 			} 
  if (n==10){
 		   	affichette.setLayout(new GridLayout(1, 2));
 		 	this.add(affichette);
 		   	affichette.setBackground(Color.YELLOW);	
 		 	affiche affiche1 = new affiche();
 		 	affiche1.setBackground(Color.YELLOW);
 		 	affiche1.setnom_image(chemin+"visage8.jpg");
 		 	 affiche1.settexte("visage inconnu1");
 		 	  affichette.add(affiche1);	 
 		 	  affiche affiche2 = new affiche();
 		 	 affiche2.setBackground(Color.YELLOW);
 		 	 affiche2.setnom_image(chemin+"visage31.jpg");
 		 	 affiche2.settexte("visage inconnu2");
 		 	 affichette.add(affiche2);  	 
 		 	    	 
 		 	     
 		 	      	     }		
 	       
     affichette.setAlignmentX(CENTER_ALIGNMENT);
 	       
 	}
	 }       
 	       
 	 }      
 	       
        
 	       
 	       
 	       
 	       
 	        
 	
 
   
 class affiche extends JPanel{
  private static final long serialVersionUID = 1L;
 	public String nom_image=null,texte=null;
	 public int largeur =100, hauteur =100;
 	public affiche(){
 		this.setBackground(Color.YELLOW);	
 	      this.setSize(100, 100);
 	      this.setVisible(true);
 	      this.setAlignmentX(CENTER_ALIGNMENT);
 	}
 	public void setnom_image(String nom_image1){
 		nom_image= nom_image1;
 		
 	}
 	public void settexte(String texte1){
 		texte= texte1;
 		
 	}
 	public void setsize(int largeur1, int hauteur1){
	 this.setSize(largeur, hauteur);
	 largeur = largeur1;
	 hauteur = hauteur1;
	 }
 	 public void paintComponent(Graphics g)
 		{
 			
 		
 			
 		BufferedImage visage =  ouverture_image(nom_image);
 		if (visage==null){g.fillOval(30, 30, 40, 40);}
 		else{
 		
 	
 	int pos_x = this.getWidth()/2 -40;
	 int pos_y = (10*hauteur/100);
	 int largim = largeur-((20*largeur/100));
	 int hautim =  hauteur-((20*hauteur/100));
 	g.drawImage(visage,pos_x ,pos_y  ,largim ,hautim, null);
	 g.setColor(Color.red);
 	Font fonte = new Font("Arial",9,14);
 	FontMetrics metrics = g.getFontMetrics(fonte);
 		int adv = metrics.stringWidth(texte);
 		pos_x=this.getWidth()/2-(adv/2);
 	g.setFont(fonte);
 	g.drawString(texte, pos_x, hauteur-((20*hauteur/100)));}
 
 	}
 
  public BufferedImage ouverture_image(String nom){
      		BufferedImage image;
      			
      			try {
      				boolean exists = new File(nom).exists();
      				if (exists) {
      					//System.out.println("image lue");
      				 image = ImageIO.read(new File(nom));
      				return image;}
      				else {//System.out.println("pas d'image");
   				   return null;
				    }
      			} catch (IOException e) {
      				// TODO Auto-generated catch block
      				e.printStackTrace();
      			}
      			return null;
      						
      			}  
 }
   }

	
