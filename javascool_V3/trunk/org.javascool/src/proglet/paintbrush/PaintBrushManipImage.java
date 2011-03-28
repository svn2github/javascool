/*******************************************************************************
* David.Pichardie@inria.fr, Copyright (C) 2011.           All rights reserved. *
*******************************************************************************/

package proglet.paintbrush;

public interface PaintBrushManipImage {
  // Affiche un point de coordonnées (x,y)
  public void affichePoint(int x, int y, int couleur);

  // Supprime les points sur une zone carr de largeur 3 pixels et centrée en (x,y)
  public void supprimePoint(int x, int y);

  // Trace une ligne de diagonale (x0,y0) -- (x1,y1)
  public void afficheLigne(int x0, int y0, int x1, int y1, int couleur);

  // Trace un rectangle de diagonale (x1,y1) -- (x2,y2)
  public void afficheRectangle(int x1, int y1, int x2, int y2, int couleur);

  // Pot de peinture : remplir tous les pixels voisins de (x,y) et ayant la même couleur avec la couleur spécifiée
  public void remplir(int x, int y, int nouvelle_couleur);
}

class ManipImageVide implements PaintBrushManipImage {
  // Affiche un point de coordonnées (x,y)
  public void affichePoint(int x, int y, int couleur) {}

  // Supprime les points sur une zone carr de largeur 3 pixels et centrée en (x,y)
  public void supprimePoint(int x, int y) {}

  // Trace une ligne de diagonale (x0,y0) -- (x1,y1)
  public void afficheLigne(int x0, int y0, int x1, int y1, int couleur) {}

  // Trace un rectangle de diagonale (x1,y1) -- (x2,y2)
  public void afficheRectangle(int x1, int y1, int x2, int y2, int couleur) {}

  // Pot de peinture : remplir tous les pixels voisins de (x,y) et ayant la même couleur avec la couleur spécifiée
  public void remplir(int x, int y, int nouvelle_couleur) {}
}

class ManipImageFinal implements PaintBrushManipImage {
  // affiche un point de coordonnes  (x,y)
  public void affichePoint(int x, int y, int couleur) {
    // System.out.println("affichePoint(image,"+x+","+y+")");
    PaintBrush.setPixel(x, y, couleur);
  }
  // supprime les points sur une zone carre de largeur 3 pixels et centre en (x,y)
  public void supprimePoint(int x, int y) {
    // System.out.println("supprimePoint(image,"+x+","+y+")");
    for(int i = x - 1; i <= x + 1 && i < PaintBrushImage.maxX(); i++)
      for(int j = y - 1; j <= y + 1 && j < PaintBrushImage.maxY(); j++)
        if((i >= 0) && (j >= 0))
          PaintBrush.setPixel(i, j, 15);
  }
  // trace une ligne de diagonale (x0,y0) -- (x1,y1)
  public void afficheLigne(int x0, int y0, int x1, int y1, int couleur) {
    int dx = Math.abs(x1 - x0);
    int dy = Math.abs(y1 - y0);
    int sx = (x0 < x1) ? 1 : -1;
    int sy = (y0 < y1) ? 1 : -1;
    int err = dx - dy;
    while(true) {
      PaintBrush.setPixel(x0, y0, couleur);
      if((x0 == x1) && (y0 == y1))
        return;
      int e2 = 2 * err;
      if(e2 > -dy) {
        err = err - dy;
        x0 = x0 + sx;
      }
      if(e2 < dx) {
        err = err + dx;
        y0 = y0 + sy;
      }
    }
  }
  // trace un rectangle de diagonale (x1,y1) -- (x2,y2)
  public void afficheRectangle(int x1, int y1, int x2, int y2, int couleur) {
    // System.out.println("afficheRectangle(image,"+x1+","+y1+","+x2+","+y2+")");
    int xmin = Math.min(x1, x2);
    int xmax = Math.max(x1, x2);
    int ymin = Math.min(y1, y2);
    int ymax = Math.max(y1, y2);
    for(int i = xmin; i <= xmax; i++) {
      PaintBrush.setPixel(i, ymin, couleur);
      PaintBrush.setPixel(i, ymax, couleur);
    }
    for(int j = ymin + 1; j < ymax; j++) {
      PaintBrush.setPixel(xmin, j, couleur);
      PaintBrush.setPixel(xmax, j, couleur);
    }
  }
  public void remplir_aux(int x, int y, int ancienne_couleur, int nouvelle_couleur) {
    if(PaintBrush.getPixel(x, y) == ancienne_couleur) {
      PaintBrush.setPixel(x, y, nouvelle_couleur);
      if(x > 0)
        remplir_aux(x - 1, y, ancienne_couleur, nouvelle_couleur);
      if(y > 0)
        remplir_aux(x, y - 1, ancienne_couleur, nouvelle_couleur);
      if(x + 1 < PaintBrushImage.maxX())
        remplir_aux(x + 1, y, ancienne_couleur, nouvelle_couleur);
      if(y + 1 < PaintBrushImage.maxY())
        remplir_aux(x, y + 1, ancienne_couleur, nouvelle_couleur);
    }
  }
  // pot de peinture : remplir tous les pixels voisins de (x,y) et ayant la mme couleur avec la couleur 128
  public void remplir(int x, int y, int nouvelle_couleur) {
    // System.out.println("remplir(image,"+x+","+y+")");
    int ancienne_couleur = PaintBrush.getPixel(x, y);
    if(ancienne_couleur != nouvelle_couleur)
      remplir_aux(x, y, ancienne_couleur, nouvelle_couleur);
  }
}
