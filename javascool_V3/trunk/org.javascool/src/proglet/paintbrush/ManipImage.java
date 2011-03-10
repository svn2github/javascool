/*******************************************************************************
 * David.Pichardie@inria.fr, Copyright (C) 2011.           All rights reserved. *
 *******************************************************************************/

package proglet.paintbrush;

public interface ManipImage {
  
  // Affiche un point de coordonnées (x,y)
  public void affichePoint(Image image, int x, int y, int couleur);

  // Supprime les points sur une zone carr de largeur 3 pixels et centrée en (x,y)
  public void supprimePoint(Image image, int x, int y);

  // Trace une ligne de diagonale (x0,y0) -- (x1,y1)
  public void afficheLigne(Image image, int x0, int y0, int x1, int y1, int couleur);
    
  // Trace un rectangle de diagonale (x1,y1) -- (x2,y2)
  public void afficheRectangle(Image image, int x1, int y1, int x2, int y2, int couleur);

  // Pot de peinture : remplir tous les pixels voisins de (x,y) et ayant la même couleur avec la couleur spécifiée
  public void remplir(Image image, int x, int y, int nouvelle_couleur);
}

class ManipImageVide implements ManipImage {
  
  // Affiche un point de coordonnées (x,y)
  public void affichePoint(Image image, int x, int y, int couleur) { }

  // Supprime les points sur une zone carr de largeur 3 pixels et centrée en (x,y)
  public void supprimePoint(Image image, int x, int y) {  }

  // Trace une ligne de diagonale (x0,y0) -- (x1,y1)
  public void afficheLigne(Image image, int x0, int y0, int x1, int y1, int couleur) {  }
    
  // Trace un rectangle de diagonale (x1,y1) -- (x2,y2)
  public void afficheRectangle(Image image, int x1, int y1, int x2, int y2, int couleur) {  }

  // Pot de peinture : remplir tous les pixels voisins de (x,y) et ayant la même couleur avec la couleur spécifiée
  public void remplir(Image image, int x, int y, int nouvelle_couleur) {  }
}

class ManipImageFinal implements ManipImage {
  
  // affiche un point de coordonnes  (x,y)
   public void affichePoint(Image image, int x, int y, int couleur) {
    //System.out.println("affichePoint(image,"+x+","+y+")");
    image.set(x,y,couleur);
  }

  // supprime les points sur une zone carre de largeur 3 pixels et centre en (x,y)
   public void supprimePoint(Image image, int x, int y) {
    //System.out.println("supprimePoint(image,"+x+","+y+")");
    for (int i=x-1; i<=x+1 && i<image.maxX(); i++)
      for (int j=y-1; j<=y+1 && j<image.maxY(); j++)
      if (i>=0 && j>=0) image.set(i,j,15);
  }

   // trace une ligne de diagonale (x0,y0) -- (x1,y1)
   public void afficheLigne(Image image, int x0, int y0, int x1, int y1, int couleur) {
    int dx = Math.abs(x1-x0);
    int dy = Math.abs(y1-y0);
    int sx = (x0 < x1) ? 1 : -1;
    int sy = (y0 < y1) ? 1 : -1;
    int err = dx-dy;
    while (true) {
      image.set(x0,y0,couleur);
      if (x0 == x1 && y0 == y1) return;
      int e2 = 2*err;
      if (e2 > -dy) {
        err = err - dy;
        x0 = x0 + sx;
      }
      if (e2 <  dx) {
        err = err + dx;
        y0 = y0 + sy; 
      }
    }
  }

    
 // trace un rectangle de diagonale (x1,y1) -- (x2,y2)
   public void afficheRectangle(Image image, int x1, int y1, int x2, int y2, int couleur) {
    //System.out.println("afficheRectangle(image,"+x1+","+y1+","+x2+","+y2+")");
    int xmin = Math.min(x1,x2);
    int xmax = Math.max(x1,x2);
    int ymin = Math.min(y1,y2);
    int ymax = Math.max(y1,y2);
    for (int i=xmin; i<=xmax; i++) {
      image.set(i,ymin,couleur);
      image.set(i,ymax,couleur);      
    }
    for (int j=ymin+1; j<ymax; j++) {
      image.set(xmin,j,couleur);
      image.set(xmax,j,couleur);
    }
  }

   public void remplir_aux(Image image, int x, int y, int ancienne_couleur, int nouvelle_couleur) {
    if (image.get(x,y)==ancienne_couleur) {
      image.set(x,y,nouvelle_couleur);
      if (x>0) remplir_aux(image,x-1,y,ancienne_couleur,nouvelle_couleur);
      if (y>0) remplir_aux(image,x,y-1,ancienne_couleur,nouvelle_couleur);
      if (x+1<image.maxX()) remplir_aux(image,x+1,y,ancienne_couleur,nouvelle_couleur);
      if (y+1<image.maxY()) remplir_aux(image,x,y+1,ancienne_couleur,nouvelle_couleur);
    }
  }
  
 // pot de peinture : remplir tous les pixels voisins de (x,y) et ayant la mme couleur avec la couleur 128
   public void remplir(Image image, int x, int y, int nouvelle_couleur) {
    //System.out.println("remplir(image,"+x+","+y+")");
    int ancienne_couleur = image.get(x,y);
    if (ancienne_couleur!=nouvelle_couleur) 
      remplir_aux(image,x,y,ancienne_couleur,nouvelle_couleur);
  }
}
