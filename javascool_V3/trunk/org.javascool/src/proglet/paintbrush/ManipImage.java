package jvsPaint;
import java.util.*;

class ManipImage {
  
  // affiche un point noir de coordonnées  (x,y)
  static void affichePoint(Image image, int x, int y) {
    //System.out.println("affichePoint(image,"+x+","+y+")");
    image.set(x,y,0);
  }

  // supprime les points sur une zone carrée de largeur 3 pixels et centrée en (x,y)
  static void supprimePoint(Image image, int x, int y) {
    //System.out.println("supprimePoint(image,"+x+","+y+")");
    for (int i=x-1; i<=x+1 && i<image.maxX(); i++)
      for (int j=y-1; j<=y+1 && j<image.maxY(); j++)
	if (i>=0 && j>=0) image.set(i,j,255);
  }

  // trace un rectangle noir de diagonale (x1,y1) -- (x2,y2)
  static void afficheRectangle(Image image, int x1, int y1, int x2, int y2) {
    //System.out.println("afficheRectangle(image,"+x1+","+y1+","+x2+","+y2+")");
    int xmin = Math.min(x1,x2);
    int xmax = Math.max(x1,x2);
    int ymin = Math.min(y1,y2);
    int ymax = Math.max(y1,y2);
    for (int i=xmin; i<=xmax; i++) {
      image.set(i,ymin,0);
      image.set(i,ymax,0);      
    }
    for (int j=ymin+1; j<ymax; j++) {
      image.set(xmin,j,0);
      image.set(xmax,j,0);
    }
  }

  static void remplir_aux(Image image, int x, int y, int ancienne_couleur, int nouvelle_couleur) {
    if (image.get(x,y)==ancienne_couleur) {
      image.set(x,y,nouvelle_couleur);
      if (x>0) remplir_aux(image,x-1,y,ancienne_couleur,nouvelle_couleur);
      if (y>0) remplir_aux(image,x,y-1,ancienne_couleur,nouvelle_couleur);
      if (x+1<image.maxX()) remplir_aux(image,x+1,y,ancienne_couleur,nouvelle_couleur);
      if (y+1<image.maxY()) remplir_aux(image,x,y+1,ancienne_couleur,nouvelle_couleur);
    }
  }
  
  // pot de peinture : remplir tous les pixels voisins de (x,y) et ayant la même couleur avec la couleur 128
  static void remplir(Image image, int x, int y) {
    //System.out.println("remplir(image,"+x+","+y+")");
    int ancienne_couleur = image.get(x,y);
    int nouvelle_couleur = 128;
    if (ancienne_couleur!=nouvelle_couleur) 
      remplir_aux(image,x,y,ancienne_couleur,nouvelle_couleur);
  }
}
