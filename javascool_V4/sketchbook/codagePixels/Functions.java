/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package org.javascool.proglets.codagePixels;
import static org.javascool.macros.Macros.*;

// Used to define the gui
import java.awt.Dimension;
import java.awt.Color;

/** Définit les fonctions de la proglet qui permettent de manipuler les pixels d'une image.
 *
 * @see <a href="Functions.java.html">code source</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;
  // @factory
  private Functions() {}
  /** Renvoie l'instance de la proglet pour accéder à ses éléments. */
  private static Panel getPane() {
    return getProgletPane();
  }
  /** Initialise l'image.
   * - La taille de l'image ne doit pas être trop importante (pas plus de 500^2).
   * @param width Demi largeur de l'image de taille {-width, width}, si centrée (sinon largeur de l'image)
   * @param height Demi hauteur de l'image de taille {-height, height}, si centrée (sinon hauteur de l'image).
   * @param centered Si l'image est centrée, la valeur vaut true; si l'image n'est pas centrée la valeur vaut false.
   * @param zoom Ajuste automatiquement la taille de l'image au display si true (par défaut), sinon fixe 1 pixel de l'image à 1 pixel de l'affichage.
   */
  static public void reset(int width, int height, boolean centered, boolean zoom) {
    Functions.width = width;
    Functions.height = height;
    Functions.centered = centered;
    if(centered) {
      getPane().reset(2 * width + 1, 2 * height + 1, zoom);
    } else {
      getPane().reset(width, height, zoom);
    }
    org.javascool.gui.Desktop.getInstance().focusOnProgletPanel();
  }
  /**
   * @see #reset(int, int, boolean, boolean)
   */
  static public void reset(int width, int height, boolean centered) {
    reset(width, height, centered, true);
  }
  /**
   * @see #reset(int, int, boolean, boolean)
   */
  static public void reset(int width, int height) {
    reset(width, height, true);
  }
  static private int width = 0, height = 0;
  static private boolean centered = true;

  /** Charge l'image.
   * - La taille de l'image ne doit pas être trop importante (pas plus de 500^2).
   * @param image Nom de l'URL (Universal Resource Location) de ll'URL où se trouve l'image.
   * <p>Reconnait les formats binaires ".png", ".jpg" et ".gif". Reconnait les fichiers ASCII au format <a href="http://fr.wikipedia.org/wiki/Portable_pixmap">".pbm", ".pgm" et ".ppm"</a> par leur extension.</p>
   * @param centered Si l'image est centrée, la valeur vaut true; si l'image n'est pas centrée la valeur vaut false.
   * @param zoom Ajuste automatiquement la taille de l'image au display si true (par défaut), sinon fixe 1 pixel de l'image à 1 pixel de l'affichage.
   */
  static public void load(String image, boolean centered, boolean zoom) {
    try {
      getPane().reset(org.javascool.tools.image.ImageUtils.loadImage(image), zoom);
      Dimension dim = getPane().getDimension();
      Functions.centered = centered;
      if(centered) {
        Functions.width = (dim.width - 1) / 2;
        Functions.height = (dim.height - 1) / 2;
      } else {
        Functions.width = dim.width;
        Functions.height = dim.height;
      }
    } catch(Exception e) {
      reset(200, 200);
      System.err.println(e);
      System.out.println("Impossible de charger " + image);
    }
    org.javascool.gui.Desktop.getInstance().focusOnProgletPanel();
  }
  /**
   * @see #load(String, boolean, boolean)
   *
   */
  static public void load(String image, boolean centered) {
    load(image, centered, true);
  }
  /**
   * @see #load(String, boolean, boolean)
   *
   */
  static public void load(String image) {
    load(image, true);
  }
  /** Sauvegarde l'image actuellement affichée.
   * @param location Une URL (Universal Resource Location) cible où stocker l'image.
   * <p>Reconnait le format de stockage par l'extension. Il est recommandé d'utiliser le format binaire ".pgn". Les formats  ASCIIt <a href="http://fr.wikipedia.org/wiki/Portable_pixmap">".pbm", ".pgm" et ".ppm"</a> sont disponibles aussi.</p>
   * @return La valeur true si la sauvegarde s'est bien passée et la valeur fausse sinon (un message d'erreur s'affiche dans la console).
   */
  static public boolean save(String location) {
    try {
      org.javascool.tools.image.ImageUtils.saveImage(location, getPane().getImage());
      return true;
    } catch(Exception e) {
      System.out.println("Erreur à la sauvegarde de l'image dans '" + location + "' : " + e);
      return false;
    }
  }
  /** Renvoie la demi-largeur de l'image, si elle est centrée (sinon la largeur de l'image). */
  static public int getWidth() {
    return width;
  }
  /** Renvoie la demi-hauteur de l'image, si elle est centrée (sinon la hauteur de l'image). */
  static public int getHeight() {
    return height;
  }
  /** Change la valeur d'un pixel de l'image.
   * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}, si l'image est centrée (sinon valeur entre {0, width{).
   * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre  {-height, height}, si l'image est centrée (sinon valeur entre {0, height{).
   * @param color Couleur: "black" (default), "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "white", "yellow".
   * @return Renvoie true si le pixel est dans l'image, false si il est en dehors des limites d el'image.
   */
  static public boolean setPixel(int x, int y, String color) {
    color = color.toLowerCase();
    if(centered) {
      return getPane().set(x + width, height - y, color);
    } else {
      return getPane().set(x, y, color);
    }
  }
  /** Change la valeur d'un pixel de l'image.
   * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}, si l'image est centrée (sinon valeur entre {0, width{).
   * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre  {-height, height}, si l'image est centrée (sinon valeur entre {0, height{).
   * @param valeur Une valeur entre 0 et 255 (0 pour noir, 255 pour blanc).
   * @return Renvoie true si le pixel est dans l'image, false si il est en dehors des limites de l'image.
   */
  static public boolean setPixel(int x, int y, int valeur) {
    if(centered) {
      return getPane().set(x + width, height - y, valeur);
    } else {
      return getPane().set(x, y, valeur);
    }
  }
  /** Change la valeur couleur d'un pixel de l'image.
   * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}, si l'image est centrée (sinon valeur entre {0, width{).
   * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre  {-height, height}, si l'image est centrée (sinon valeur entre {0, height{).
   * @param red Une valeur pour le rouge entre 0 et 255.
   * @param green Une valeur pour le vert entre 0 et 255.
   * @param blue Une valeur pour le blue entre 0 et 255.
   * @return Renvoie true si le pixel est dans l'image, false si il est en dehors des limites de l'image.
   */
  static public boolean setPixel(int x, int y, int red, int green, int blue) {
    if(centered) {
      return getPane().set(x + width, height - y, new Color(red, green, blue));
    } else {
      return getPane().set(x, y, new Color(red, green, blue));
    }
  }
  /** Lit la valeur d'un pixel de l'image.
   * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}, si l'image est centrée (sinon valeur entre {0, width{).
   * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre {-height, height}, si l'image est centrée (sinon valeur entre {0, height{).
   * @return Une valeur entre 0 et 255 (0 pour noir, 255 pour blanc); Renvoie 0 pour les pixels extérieurs à l'image.
   */
  static public int getPixel(int x, int y) {
    if(centered) {
      return getPane().getIntensity(x + width, height - y);
    } else {
      return getPane().getIntensity(x, y);
    }
  }  /** Lit la valeur couleur d'un pixel de l'image.
      * @param x Abcisse de l'image, comptée à partir du milieu, valeur entre {-width, width}, si l'image est centrée (sinon valeur entre {0, width{).
      * @param y Ordonnée de l'image, comptée à partir du milieu, valeur entre {-height, height}, si l'image est centrée (sinon valeur entre {0, height{).
      * @return Un tableau avec les trois valeurs rouge (index 0), vert (index 1), bleu (index 2) entre 0 et 255; Renvoie noir pour les pixels extérieurs à l'image.
      */
  static public int[] getPixelColor(int x, int y) {
    int rgb = centered ? getPane().getPixelColor(x + width, height - y).getRGB() : getPane().getPixelColor(x, y).getRGB();
    return new int[] { (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF };
  }
  /** Renvoie la valeur horizontale du dernier clic de souris dans l'image. */
  public static int getX() {
    return centered ? getPane().getClicX() - width : getPane().getClicX();
  }
  /** Renvoie la valeur verticale du dernier clic de souris dans l'image. */
  public static int getY() {
    return centered ? height - getPane().getClicY() : getPane().getClicY();
  }
  /** Définit une portion de code appelée à chaque clic de souris.
   * @param runnable La portion de code à appeler, ou null si il n'y en a pas.
   */
  public static void setRunnable(Runnable runnable) {
    getPane().setRunnable(runnable);
  }
}

