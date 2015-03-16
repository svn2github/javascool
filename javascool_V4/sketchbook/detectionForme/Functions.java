/*******************************************************************************
* Christophe.Beasse@ac-rennes.fr, Copyright (C) 2011.  All rights reserved.    *
* from proglet codagePixels /  Thierry.Vieville@sophia.inria.fr                *
*******************************************************************************/

package org.javascool.proglets.detectionForme;
import static org.javascool.macros.Macros.*;

// Used to define the gui
import org.javascool.macros.Macros;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.RenderingHints; 
import java.awt.AlphaComposite;  
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.io.OutputStream;
import java.io.File;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;

  
/** Définit les fonctions de la proglet qui permettent 
 *  de manipuler les pixels d'une image.
 *
 * @see <a href="Functions.java.html">code source</a>
 * @serial exclude
 */
public class Functions {
  private static final long serialVersionUID = 1L;

  // img[0] et g2d[0] ne sont pas utilisés 
  // Les indices d'images utilisés sont 1,2,3,4
  private static BufferedImage [] img = new BufferedImage[5];
  private static Graphics2D []    g2d = new Graphics2D[5];
  
  private static DataInputStream fileR = null;
  private static DataOutputStream fileW = null;  
  

  // @factory
  private Functions() {}
  /** Renvoie l'instance de la proglet pour accéder à ses éléments. */
  private static Panel getPane() {
    return getProgletPane();
  }
  
  /** Création des images de travail
   *  On dispose de 4 images de travail (1,2,3 ou 4)
   * - La taille de l'image ne doit pas être trop importante(pas plus de 500^2).
   * @param width largeur de l'image.
   * @param height hauteur de l'image.
   * @param num numéro de l'image de travail.*    
   */ 
 
  
  public static void createImage(int num, int width, int height) {
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [createImage] : Num image incorrect");  
  try {  
    if (g2d[num] != null) g2d[num].dispose();
    img[num] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    g2d[num] = img[num].createGraphics();
    clearImage(num);
    } catch(Exception e) {
       throw new RuntimeException(e + "Fct [createImage] : Création image "+num+" impossible");
    }
  } 
  
  /** Suppression des images de travail
   *  On dispose de 4 images (1,2,3 ou 4)
   * Par défaut on supprime les 4 images de travail.  
   * @param num numéro de l'image de travail 
   * (si précisé, sinon toutes les images).    
   */  
  public static void disposeImage(int num) { 
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [disposeImage] : Num image incorrect");     
    if (g2d[num] != null) g2d[num].dispose();
    img[num] = null;
  }   
  
  public static void disposeImage() { 
    for (int i=1; i<=4;i++) { 
       if (g2d[i] != null) g2d[i].dispose();
      img[i] = null;
    }
  }    
  
  
  /** Charge une image locale ou distante.
   * La taille de l'image ne doit pas être trop importante (pas plus de 500^2).
   * @param num numéro de l'image de travail dans laquelle charger l'image.   
   *  On dispose de 4 images (1,2,3 ou 4)
   * @param location Une URL (Universal Resource Location) de la forme:    <div id="load-format"><table align="center">
   * <tr><td><tt>http:/<i>path-name</i></tt></td><td>pour aller chercher le contenu sur un site web</td></tr>
   * <tr><td><tt>http:/<i>path-name</i>?param_i=value_i&amp;..</tt></td><td>pour le récupérer sous forme de requête HTTP</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>pour le charger du système de fichier local ou en tant que ressource Java dans le CLASSPATH</td></tr>
   * <tr><td><tt>jar:/<i>jar-path-name</i>!/<i>jar-entry</i></tt></td><td>pour le charger d'une archive
   *  <div>(exemple:<tt>jar:http://javascool.gforge.inria.fr/javascool.jar!/META-INF/MANIFEST.MF</tt>)</div></td></tr>
   * </table></div>
   *
   * @throws IllegalArgumentException Si l'URL est mal formée.
   * @throws RuntimeException Si une erreur d'entrée-sortie s'est produite.
   */
  public static void loadImage(int num, String location) {
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [loadImage] : Num image incorrect");  
    img[num] = null;
    try {
      img[num] = ImageIO.read(Macros.getResourceURL(location));
    } catch(IOException e) { 
       throw new RuntimeException(e + " when loading: " + location + " : "+
                                  Macros.getResourceURL(location));
    }
    if(img[num] == null) throw new RuntimeException("Unable to load: " + location);
  }
      
   
  /** Sauvegarde d'une image de travail.
   * @param num numéro de l'image de travail
   *  On dispose de 4 images (1,2,3 ou 4)      
   * @param location @optional<"stdout:"> Une URL (Universal Resource Location) de la forme: <div id="save-format"><table>
   * <tr><td><tt>ftp:/<i>path-name</i></tt></td><td>pour sauver sur un site FTP.</td></tr>
   * <tr><td><tt>file:/<i>path-name</i></tt></td><td>pour sauver dans le système de fichier local (le <tt>file:</tt> est optionnel).</td></tr>
   * <tr><td><tt>mailto:<i>address</i>?subject=<i>subject</i></tt></td><td>pour envoyer un courriel avec le texte en contenu.</td></tr>
   * <tr><td><tt>stdout:/</tt></td><td>pour l'imprimer dans la console.</td></tr>
   * </table></div>
   *
   * @throws IllegalArgumentException Si l'URL est mal formée.
   * @throws RuntimeException Si une erreur d'entrée-sortie s'est produite.
   */
  public static void saveImage(int num ,String location) {
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [saveImage] : Num image incorrect");    
    location = Macros.getResourceURL(location).toString();
    try {
      if(location.startsWith("file:"))
        ImageIO.write(img[num], "png", new File(location.substring(5)));
      else {
        URLConnection connection = new URL(location).openConnection();
        connection.setDoOutput(true);
        OutputStream writer = connection.getOutputStream();
        ImageIO.write(img[num], "png", writer);
        writer.close();
      }
    } catch(IOException e) { throw new RuntimeException(e + " when saving: " + location);
    }
  }  
  
  /** Création d'une image à partir d'un tableau d'entiers
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.   
   * @param pixMap Tableau d'entiers.    
   * @param width largeur pixMap.   
   * @param height hauteur pixMap.          
   */    
  
  static public void createImageFromPixMap(int num,int pixMap [][],
                                           int width,int height) {
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [createImageFromPixMap] : Num image incorrect");                                             
    try {
    if (g2d[num] != null) g2d[num].dispose();
    img[num] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    g2d[num] = img[num].createGraphics();    

    for(int x = 0; x < width; x++)
      for(int y = 0; y < height; y++)
          img[num].setRGB(x,y,pixMap[x][y]);
    } catch(Exception e) {
      throw new RuntimeException(e + " Fct [createImageFromPixMap] : creation image impossible");
    }
  }
  
  
  /** Chargement d'une image dans un tableau d'entiers
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.   
   * @param pixMap Tableau d'entiers.             
   */  
  
static public void loadImageToPixMap(int num, int pixMap [][]) {
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [loadImageToPixMap] : Num image incorrect");  
    try {
    
     int width = img[num].getWidth();
     int height = img[num].getHeight();
  
     for(int x = 0; x < width; x++)
        for(int y = 0; y < height; y++)
          pixMap[x][y] = img[num].getRGB(x,y);        
    } catch(Exception e) {
        throw new RuntimeException(e + " Fct [loadImageToPixMap] : Impossible de charger le pixmap");
    }
  }
  
  
  /** Affichage des images de travail dans le panel de la proglet
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.* 
   */ 
  public static void showImage(int num) {
  if ((num<1) ||(num>4)) throw new RuntimeException("Fct [showImage] : Num image incorrect");   
  try { 
    getPane().reset(img[num],false);
    } catch(Exception e) {
        throw new RuntimeException(e + " Fct [showImage : Impossible de charger Bufferedimage num "+num);
    }
  }  

  /** Réinitialisation des images de travail (fond blanc)
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.*    
   */      

  public static void clearImage(int num) { 
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [clearImage] : Num image incorrect");   
    g2d[num].setColor(Color.white);  
    g2d[num].fillRect(0,0,img[num].getWidth(),img[num].getHeight());
  }
  
  
  /** Copie de l'image de travail num1 dans l'image de travail num2 
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num1 numéro de l'image source   
   * @param num2 numéro de l'image destination    
   */      

  public static void copyImage(int num1,int num2) { 
  if ((num1<1)||(num1>4)) throw new RuntimeException("Fct [copyImage] : Num1 image incorrect"); 
  if ((num2<1)||(num2>4)) throw new RuntimeException("Fct [copyImage] : Num2 image incorrect");
    ColorModel cm = img[num1].getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = img[num1].getSubimage(0,0,img[num1].getWidth(),
                                          img[num1].getHeight()).copyData(null);  
    img[num2] = new BufferedImage(cm, raster, isAlphaPremultiplied, null);       
    if (g2d[num2] != null) g2d[num2].dispose();                                          
    g2d[num2] = img[num2].createGraphics();
  }
  
  /** Copie d'une partie de l'image de travail num1 dans l'image de travail num2 
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num1 numéro de l'image source   
   * @param num2 numéro de l'image destination     
   * @param x abscisse de la zone à récupérer
   * @param y ordonnée de la zone à récupérer
   * @param w largeur de la zone à récupérer
   * @param h hauteur de la zone à récupérer
   */      

  public static void copyImage(int num1,int num2,int x, int y, int w, int h) { 
    if ((num1<1)||(num1>4)) throw new RuntimeException("Fct [copyImage2] : Num1 image incorrect"); 
    if ((num2<1)||(num2>4)) throw new RuntimeException("Fct [copyImage2] : Num image incorrect"); 
    ColorModel cm = img[num1].getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = img[num1].getSubimage(x,y,w,h).copyData(null);  
    img[num2] = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    if (g2d[num2] != null) g2d[num2].dispose();   
    g2d[num2] = img[num2].createGraphics();
  }  
  
  /** Copie de l'image de travail num1 dans l'image de travail num2 
   * avec ajout d'une bordure de largeur borderWidth.
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num1 numéro de l'image source   
   * @param num2 numéro de l'image destination     
   * @param borderWidth largeur du bord ajouté
   */ 
  public static void copyImage(int num1,int num2,int borderWidth) {  
  if ((num1<1)||(num1>4)) throw new RuntimeException("Fct [copyImage3] : Num1 image incorrect"); 
  if ((num2<1)||(num2>4)) throw new RuntimeException("Fct [copyImage3] : Num image incorrect");      
    int w = img[num1].getWidth();  
    int h = img[num1].getHeight(); 
    createImage(num2,w+2*borderWidth, h+2*borderWidth);  
    Graphics2D g = img[num2].createGraphics();  
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                       RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
    //g.setColor(Color.white);  
    //g.fillRect(0,0,w+2*borderWidth,h+2*borderWidth);                         
    //g.drawImage(img[num1], 0, 0, newW, newH, 0, 0, w, h, null); 
	  g.drawImage(img[num1], borderWidth, borderWidth, w, h, null);    
    g.dispose();   
    }     
  
  /** Rogner une image  
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image à rognenr      
   * @param x abscisse de la zone à garder
   * @param y ordonnée de la zone à garder
   * @param w largeur de la zone à garder
   * @param h hauteur de la zone à garder
   */      

  public static void cutImage(int num,int x, int y, int w, int h) { 
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [clearImage] : Num image incorrect");
    
    ColorModel cm = img[num].getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = img[num].getSubimage(x,y,w,h).copyData(null);  
    img[num] = new BufferedImage(cm, raster, isAlphaPremultiplied, null);    
    if (g2d[num] != null) g2d[num].dispose();
    g2d[num] = img[num].createGraphics();
  }  
  
  /** Change la valeur d'un pixel de l'image de travail.
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.*    
   * @param x Abcisse de l'image.
   * @param y Ordonnée de l'image.
   * @param c Couleur: "black" (default), "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "white", "yellow".
   */  
     
  public static void setPixel(int num, int x, int y, int c) {
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [setPixel(1)] : Num image incorrect");   
    img[num].setRGB(x,y,c);
	}	
	
  public static void setPixel(int num, int x, int y, Color c) {
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [setPixel(2)] : Num image incorrect");   
    img[num].setRGB(x,y,c.getRGB());
	}	
	
  /** Retourne la valeur d'un pixel de l'image de travail.
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.*    
   * @param x Abcisse de l'image.
   * @param y Ordonnée de l'image.
   * @return Renvoie la couleur spécifiée (entier)
   */ 	
  
 public static int getPixelColor(int num, int x, int y) { 
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [getPixelColor] : Num image incorrect");     
    return img[num].getRGB(x,y);	  
	}  	
	
/**
* A pixel is represented by a 4-byte (32 bit) integer, like so:
* 
*  00000000 00000000 00000000 11111111
*  ^ Alpha      ^Red          ^Green      ^Blue
*
* Alpha, which denotes "transparency". 
* An alpha value of 0xff (=255) is fully opaque
* An alpha value of 0 is fully transparent.
* int color = color & 0x00ffffff; use bitwise & to remove alpha component
*
*   pixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
*/

/**
* Returns the alpha component in the range 0-255 in the default sRGB space.
* @param color couleur du pixel (int)  
* @return the alpha component.
*/
 public static int getAlpha(int color) {
           return (color >> 24) & 0xFF;
  }

/**
* Returns the red component in the range 0-255 in the default sRGB space.
* @param color couleur du pixel (int)  
* @return the red component.
*/
 public static int getRed(int color) {
           return (color >> 16) & 0xFF;
  }
  
/**
* Returns the green component in the range 0-255 in the default sRGB space.
* @param color couleur du pixel (int)    
* @return the green component.
*/
 public static int getGreen(int color) {
       return (color >> 8) & 0xFF;
}
 
/**
* Returns the blue component in the range 0-255 in the default sRGB space.
* @param color couleur du pixel (int)  
* @return the blue component.
*/
public static int getBlue(int color) {
     return (color >> 0) & 0xFF;
}
	
	
	
  /** teste la valeur d'un pixel de l'image de travail.
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.*    
   * @param x Abcisse de l'image.
   * @param y Ordonnée de l'image.
   * @param c Couleur: "black" (default), "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "white", "yellow".
   * @return Renvoie true si le pixel est de la couleur spécifiée
   */ 	
  
 public static boolean isPixelColor(int num, int x, int y, Color c) { 
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [isPixelColor] : Num image incorrect");     
    return (img[num].getRGB(x,y) == c.getRGB());	  
	}
	
 public static boolean isPixelColor(int num, int x, int y, int c) { 
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [isPixelColor] : Num image incorrect");     
    return (img[num].getRGB(x,y) == c);	  
	}	
  
  /** Renvoie le code coudeur de la couleur spécifiée
   * @param c Couleur: "black" (default), "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "white", "yellow".
   * @return Renvoie la valeur int RGB de la couleur spécifiée
   */ 	
  
 public static int codeCouleur(Color c) {    
    return c.getRGB();	  
	}    

  /** Tracer d'un rectangle dans l'images de travail
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.   
   * @param xc abscisse coin haut gauche du rectangle.    
   * @param yc ordonnée coin haut gauche du rectangle.   
   * @param w largueur côté du rectangle.  
   * @param h hauteur côté du rectangle.         
   * @param c  couleur du tracé.        
   */         
    
  public static void drawRect(int num, int xc, int yc, int w , int h, Color c) { 
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [drawFillSquare] : Num image incorrect");     
    g2d[num].setColor(c);
    g2d[num].drawRect(xc,yc,w,h);
  }
  
  /** Tracer d'un rectangle plein dans l'images de travail
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.   
   * @param xc abscisse coin haut gauche du rectangle.    
   * @param yc ordonnée coin haut gauche du rectangle.   
   * @param w largueur côté du rectangle.  
   * @param h hauteur côté du rectangle.         
   * @param c  couleur du tracé.        
   */         
    
  public static void drawFillRect(int num, int xc, int yc, int w , int h, Color c) { 
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [drawFillSquare] : Num image incorrect");     
    g2d[num].setColor(c);
    g2d[num].fillRect(xc,yc,w,h);
  }  
 
  /** Tracer d'un ovale dans l'images de travail
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.   
   * @param xc abscisse coin haut gauche du ovale.    
   * @param yc ordonnée coin haut gauche du ovale.   
   * @param w largueur du carré englobant.  
   * @param h hauteur du carré englobant.    
   * @param c  couleur du tracé.        
   */     
  public static void drawOval(int num, int xc, int yc, int w, int h , Color c) {
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [drawFillCircle] : Num image incorrect");    
    g2d[num].setColor(c);
    g2d[num].drawOval(xc,yc,w,h);
  }  
      
  /** Tracer d'un ovale plein dans l'images de travail
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.   
   * @param xc abscisse coin haut gauche du ovale.    
   * @param yc ordonnée coin haut gauche du ovale.   
   * @param w largueur du carré englobant.  
   * @param h hauteur du carré englobant.    
   * @param c  couleur du tracé.        
   */     
  public static void drawFillOval(int num, int xc, int yc, int w, int h , Color c) {
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [drawFillCircle] : Num image incorrect");    
    g2d[num].setColor(c);
    g2d[num].fillOval(xc,yc,w,h);
  } 

  /** Tracer d'un segment dans l'images de travail
   * On dispose de 4 images de travail (1,2,3 ou 4) 
   * @param num numéro de l'image de travail.   
   * @param x1 abscisse point initial.    
   * @param y1 ordonnée point initial.   
   * @param x2 abscisse point final.    
   * @param y2 ordonnée point final. 
   * @param c  couleur du tracé.*          
   */     
  public static void drawSegment(int num,
                                    int x1,int y1,int x2,int y2,Color c) { 
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [drawSegment] : Num image incorrect");                                     
    g2d[num].setColor(c);
    g2d[num].drawLine(x1,y1,x2,y2);
  } 
          
  /** Tracer d'une chaîne de caractères dans l'images de travail
   * On dispose de 4 images de travail (1,2,3 ou 4) 
   * @param num numéro de l'image de travail. 
   * @param str message à afficher.       
   * @param x abscisse coin haut gauche du carré.    
   * @param y ordonnée coin haut gauche du carré.    
   * @param c  couleur du tracé.        
   */         
    
  public static void drawString(int num, String str, int x, int y, Color c) { 
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [drawString] : Num image incorrect");   
    g2d[num].setColor(c);
    g2d[num].drawString(str,x,y);
  }

  /** Renvoie la largeur de l'image.
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.     
  */
  static public int getImageWidth(int num) {
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [getImageWidth] : Num image incorrect");   
    return img[num].getWidth();
  }

  /** Renvoie la hauteur de l'image.
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.     
  */
  static public int getImageHeight(int num) {
    if ((num<1) ||(num>4)) throw new RuntimeException("Fct [getImageHeight] : Num image incorrect");    
    return img[num].getHeight();
  }

  /** Convolue l'image avec le masque spécifié.
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.  
   * @param mask masque de convolution qui doit être défini de la manière suivante :    
   *     float[ ] masque = {
	 *	                      0.1f, 0.1f, 0.1f,
	 *                        0.1f, 0.2f, 0.1f,
	 *                        0.1f, 0.1f, 0.1f};
	*/     
  
public static void convolveImage(int num, float[] mask) {
  if ((num<1) ||(num>4)) throw new RuntimeException("Fct [convolveImage] : Num image incorrect");  
  BufferedImage imgConv = new BufferedImage(getImageWidth(num),
                                            getImageHeight(num), 
                                            BufferedImage.TYPE_INT_RGB);


	Kernel masque = new Kernel(3, 3, mask);
	ConvolveOp operation = new ConvolveOp(masque);
	operation.filter(img[num],imgConv);
	img[num]=imgConv;
  if (g2d[num] != null) g2d[num].dispose();	
  g2d[num] = img[num].createGraphics();

}
    
  /** Redimensionne l'image à la nouvelle taille  spécifiée
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.  
   * @param newW nouvelle largeur    
   * @param newH nouvelle hauteur     
  */  
public static void resizeImage(int num, int newW, int newH) { 
   if ((num<1) ||(num>4)) throw new RuntimeException("Fct [resizeImage] : Num image incorrect");  
    int w = img[num].getWidth();  
    int h = img[num].getHeight(); 
    BufferedImage imgResized = new BufferedImage(newW,newH,
                                                 BufferedImage.TYPE_INT_RGB);            
    Graphics2D g = imgResized.createGraphics();         
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                       RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
	  g.drawImage(img[num], 0, 0, newW, newH, null);    
    g.dispose(); 
	  img[num]=imgResized;
    if (g2d[num] != null) g2d[num].dispose();	
    g2d[num] = img[num].createGraphics(); 
      
    }
        
  /** Redimensionne l'image à la nouvelle taille spécifiée
   * On utilise ici un rendu supérieur (traitement plus long)  
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.  
   * @param newW nouvelle largeur    
   * @param newH nouvelle hauteur     
  */   
    public static void resizeImageWithHint(int num, int newW, int newH) {
  if ((num<1) ||(num>4)) throw new RuntimeException("Fct [resizeImageWithHint] : Num image incorrect");      
    int w = img[num].getWidth();  
    int h = img[num].getHeight(); 
    BufferedImage imgResized = new BufferedImage(newW,newH,
                                                 BufferedImage.TYPE_INT_RGB);            
    Graphics2D g = imgResized.createGraphics();  
	  g.setComposite(AlphaComposite.Src);
	  g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                     RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	  g.setRenderingHint(RenderingHints.KEY_RENDERING,
	                     RenderingHints.VALUE_RENDER_QUALITY);
	  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                     RenderingHints.VALUE_ANTIALIAS_ON);
	  g.drawImage(img[num], 0, 0, newW, newH, null);
	  g.dispose();	
 	  img[num]=imgResized;
    if (g2d[num] != null) g2d[num].dispose();	
    g2d[num] = img[num].createGraphics(); 
    }    


  /** Rotation de l'image suivant l'angle spécifié 
   * On dispose de 4 images de travail (1,2,3 ou 4)
   * @param num numéro de l'image de travail.  
   * @param angle angle de rotation en degrés       
  */
  
 public static void rotateImage(int num, double angle) { 
  if ((num<1) ||(num>4)) throw new RuntimeException("Fct [rotateImage] : Num image incorrect");    
  int w = img[num].getWidth();  
  int h = img[num].getHeight();  
  BufferedImage imgRot = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);            
  Graphics2D g = imgRot.createGraphics();     
  g.setColor(Color.white);  
  g.fillRect(0,0,w,h);                    
  g.rotate(Math.toRadians(angle), w/2, h/2);  
  g.drawImage(img[num], 0, 0, null);  
  g.dispose();  
	img[num]=imgRot;
  if (g2d[num] != null) g2d[num].dispose();	
  g2d[num] = img[num].createGraphics();  
  //g2d.translate(170, 0); Translate the center of our coordinates.           
 
  }  
  
  /** Affiche en simultané les 4 images de travail
   * seules les images existantes sont affichées  
   * PIP : Picture In Picture       
  */  
public static void showPipImage() {   

	int[][] pos = { {0,0},{200,0},{0,200},{200,200}}; // position images
    try { 
    BufferedImage imgPip = new BufferedImage(400,400, 
                                          BufferedImage.TYPE_INT_RGB);    
    Graphics2D g = imgPip.createGraphics();  
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                       RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	  g.setRenderingHint(RenderingHints.KEY_RENDERING,
	                     RenderingHints.VALUE_RENDER_QUALITY);
	  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                     RenderingHints.VALUE_ANTIALIAS_ON);                       
    g.setColor(Color.lightGray);  
    g.fillRect(0,0,400,400);  

    int maxWH = 0;
    for(int i=1; i<=4; i++) {     
       if (img[i] != null){
         if (img[i].getWidth() > maxWH) maxWH= img[i].getWidth(); 
         if (img[i].getHeight() > maxWH) maxWH= img[i].getHeight();
       }
    }
  
    double coef = 1;
   
    if (maxWH > 200) coef = 200.0 / maxWH;

    for(int i=1; i<=4; i++) {       
      if (img[i] != null) {
        int wr = (int)(img[i].getWidth() * coef)-1;
        int hr = (int)(img[i].getHeight() * coef)-1;
        int dx = (200-wr)/2;
        int dy = (200-hr)/2;           
        g.drawImage(img[i],pos[i-1][0]+dx,pos[i-1][1]+dy, wr, hr, null);
     } 
     }
                           
    g.setColor(Color.red);  
    g.drawLine(0,199,400,199);
    g.drawLine(0,200,400,200);
    g.drawLine(0,201,400,201);        
    g.drawLine(199,0,199,400);  
    g.drawLine(200,0,200,400);
    g.drawLine(201,0,201,400);               
    g.dispose();

  
    getPane().reset(imgPip,false);
    } catch(Exception e) {
      System.err.println(e);
      System.out.println("Impossible de charger Bufferedimage imgPip");
    }
}  

// ========================================================
// ==                  Gestion de fichier
// ======================================================== 

/**
* Ouverture du fichier en lecture.
* @param nomFichier nom du fichier à ouvrir  
*/
  public static void openFileReader(String nomFichier) {
    try {  
        fileR = new DataInputStream(new BufferedInputStream
			              (new FileInputStream(nomFichier)));
    }catch (FileNotFoundException e) { 
    }            
  }
  
/**
* Lecture du code suivant dans le fichier. 
* @return valeur suivante lu (type int).
*/  
  public static int readNextCode() {
    int c = -1;
    
    if (fileR == null)
	    throw new RuntimeException("Le fichier READER n'est pas ouvert ! ");  
	    
    try {
        c = fileR.readUnsignedByte();
    }catch (EOFException e) { 
    }catch (IOException e) { 
    }        	           
    return c;
  }  

/**
* Fermeture du fichier ouvert en lecture. 
*/

  public static void closeFileReader() {
    try {  
            fileR.close();
    }catch (IOException e) { 
    }            
  } 

/**
* Ouverture du fichier en Ecriture.
* @param nomFichier nom du fichier à ouvrir  
*/  
    public static void openFileWriter(String nomFichier) {
    try {  
       fileW = new DataOutputStream(new BufferedOutputStream
			            (new FileOutputStream(nomFichier)));
    }catch (IOException e) { 
    }            
  }

/**
* Ecriture du code suivant (octet) dans le fichier ouvert en écriture.
* @param c code à ecrire (type int)  seul l'octet de poid faible est écrit.
*/  
  public static void writeNextCode(int c) {
     
    if (fileW == null)
	    throw new RuntimeException("Le fichier WRITER n'est pas ouvert ! ");  
	    
    try {
        fileW.writeByte(c);
    }catch (IOException e) { 
    }        	           
  }  

/**
* Fermeture du fichier ouvert en Ecriture. 
*/  
  public static void closeFileWriter() {
    try {  
            fileW.close();
    }catch (IOException e) { 
    }            
  } 

// ========================================================
// ==                  Spécifique Activité
// ======================================================== 

  /** Mémorise le type de forme, sa position et ses dimensions.   
  */
  private static String xxshapeType = null;
  private static int xxposx, xxposy;
  private static int xxcote = 0;
  private static double xxangle = 0;
  
  /** Trace aléatoirement la forme à détecter dans l'image
   * on travail dans l'image de travail [1]
   * la fonction définit une image de  400 x 400   
   *  @param scenario définit le type de scénario proposé :
   *         - (1) : détection d'un pixel     
   *         - (2) : détection d'un carré
   *         - (3) : détection d'un cercle
   *         - (4) : détection cercle ou carré
   *         - (5) : détection d'un carré + rotation
   *         - (6) : détection d'un carré + rotation + bruit léger
   *         - (7) : détection d'un cercle + bruit léger                            
   *         - (8) : détection carré ou cercle + rotation
   *         - (9) : détection carré ou cercle + rotation + bruit 
   *         - (10) : détection d'un carré + rotation + bruit fort
   *         - (11) : détection d'un cercle + bruit fort                            
   *         - (12) : détection carré ou cercle + rotation + bruit fort                              
  */
  public static void createRandomShapeImage(int scenario) {
  if ((scenario<1) ||(scenario>11)) throw new RuntimeException("Fct [createRandomShape] : scenario incorrect");  
    createImage(1,400,400);
   
    switch (scenario) {
      case 1 : xxRandomPixelDrawing();
               break;
                   
      case 2 : xxRandomSquareDrawing();
               break;
               
      case 3 : xxRandomCircleDrawing();
               break;
               
      case 4 : xxRandomShapeDrawing();
               break;
               
      case 5 : xxRandomSquareDrawing();
               xxRandomRotate();
               break;
               
      case 6 : xxRandomSquareDrawing();
               xxRandomRotate();
               xxRandomNoiseLight();
               break;
               
      case 7 : xxRandomCircleDrawing();
               xxRandomNoiseLight();
               break;
               
      case 8 : xxRandomShapeDrawing();
               xxRandomRotate();
               break;
               
      case 9 : xxRandomShapeDrawing();
               xxRandomRotate();
               xxRandomNoiseLight();
               break;   
               
      case 10 : xxRandomSquareDrawing();
               xxRandomRotate();
               xxRandomNoiseStrong();
               break;  
               
      case 11 : xxRandomCircleDrawing();
               xxRandomNoiseStrong();
               break;  
               
      case 12 : xxRandomShapeDrawing();
               xxRandomRotate();
               xxRandomNoiseStrong();
               break;                                                                                         
               
    default : System.out.println("Attention ! : drawRandomShape");
              System.out.println("Scénarios possibles:1,2,3,4,5,6,7,8,9,10,11,12");
                break;                                                            

    }
 
  }  
  
  /** Vérification de la détection 
   * @param typeShape type de forme détectée (square ou circle)
   * @param posxShape abscisse du coin haut gauche du carré englobant 
   * @param posyShape ordonnée du coin haut gauche du carré englobant 
   * @param coteShape largeur du carré englobant
   * @param angleRotation angle de la rotation subit par la forme                                  
  */
  public static void checkRandomShape(String typeShape, int posxShape,
                                      int posyShape, int coteShape, 
                                      double angleRotation ) {
  
  if (typeShape == xxshapeType) {
        System.out.println("Detection type de forme ["+typeShape+"]: **CORRECT**");
  } else {
        System.out.println("Detection type de forme ["+typeShape+"]: --NON CORRECT--");
        System.out.println("Il fallait detecter un : "+xxshapeType);
  }     
  
  System.out.println("Abscisse coin haut gauche detecte : "+posxShape+" attendu ["+xxposx+"]");
  System.out.println("Ordonnee coin haut gauche detectee : "+posyShape+" attendue ["+xxposy+"]");
  int err1 = (int) Math.sqrt(((posxShape-xxposx)*(posxShape-xxposx))+((posyShape-xxposy)*(posyShape-xxposy)));
  System.out.println("Erreur commise sur la position: "+err1);
  System.out.println("longueur carre englobant detectee : "+coteShape+" attendue ["+xxcote+"]");
  int err2 = Math.abs(coteShape-xxcote);
  System.out.println("Erreur commise sur la taille: "+  err2);
  System.out.println("Angle de rotation detecte : "+angleRotation+" attendu ["+xxangle+"]");   
  int err3 = Math.round((float)Math.abs(xxangle-angleRotation));
  System.out.println("Erreur commise sur la rotation: "+ err3); 
  System.out.println("Erreurs totales commises [Score] : "+(err1+err2+err3));
  System.out.println("Rappel : cette valeur doit etre la plus faible possible");                             
  }
  
  
  /** Trace aléatoirement un pixel noir dans l'image
   * on travail dans l'image de travail [1]
   * la fonction définit une image de  400 x 400                                
  */
  public static void xxRandomPixelDrawing() {  
    createImage(1,400,400);  
    java.util.Random r=new java.util.Random( ) ;
    xxshapeType = "point";       
    xxposx = 30+r.nextInt(340);
    xxposy = 30+r.nextInt(340);
    xxcote = 1;
    xxangle = 0;    
    setPixel(1,xxposx,xxposy,Color.black);    
  }
     

  /** Trace aléatoirement un carré ou un disque dans l'image de travail 0  
   * on travail dans l'image de travail par défaut : 0
   * celle-ci doit faire au minimum 400 x 400    
  */
  private static void xxRandomShapeDrawing() {
   
      java.util.Random r=new java.util.Random( ) ; 
  
      if (r.nextBoolean()) xxRandomSquareDrawing(); 
      else xxRandomCircleDrawing(); 
        
  
  }
      
  /** Trace un disque circulaire de position et de rayon aléatoire 
   * on travail dans l'image de travail par défaut : 0
   * celle-ci doit faire au minimum 400 x 400   
  */
  private static void xxRandomCircleDrawing() {
	    int width = getImageWidth(1);
	    int height = getImageHeight(1);   
      java.util.Random r=new java.util.Random( ) ;
      xxshapeType = "circle";       
      xxposx = 50+r.nextInt(220);
      xxposy = 50+r.nextInt(220);
      xxcote = 30+r.nextInt(70);
      xxangle = 0;       
      drawFillOval(1,xxposx,xxposy,xxcote,xxcote,Color.black);
  
  } 
  

  /** Trace un carré de position et de longueur de coté aléatoire
   * on travail dans l'image de travail par défaut : 0
   * celle-ci doit faire au minimum 400 x 400       
  */  
  private static void xxRandomSquareDrawing() {
	    int width = getImageWidth(1);
	    int height = getImageHeight(1);  
      java.util.Random r=new java.util.Random( ) ; 
      
      xxshapeType = "square";      
      xxposx = 50+r.nextInt(220);
      xxposy = 50+r.nextInt(220);
      xxcote = 30+r.nextInt(70);
      xxangle = 0;      
      drawFillRect(1,xxposx,xxposy,xxcote,xxcote,Color.black);      
  }
  
  /** On fait subir une rotation à l'image
   * on travail dans l'image de travail par défaut : 0
   * celle-ci doit faire au minimum 400 x 400            
  */  
  private static void xxRandomRotate() {

      java.util.Random r=new java.util.Random( ); 
      xxangle = r.nextInt(45) * 1.0;
      rotateImage(1,xxangle);
  }    
  
  /** Ajoute un bruit léger dans l'image.
   * on travail dans l'image de travail par défaut : 0
   * celle-ci doit faire au minimum 400 x 400            
  */  
  private static void xxRandomNoiseLight() {
	    int width = getImageWidth(1);
	    int height = getImageHeight(1);  
      java.util.Random r=new java.util.Random( ); 
      
      for (int i=0; i< 100 ;i++) 
         setPixel(1,r.nextInt(400),r.nextInt(400),Color.black); 
         
      for (int i=0; i< 100 ;i++) 
         setPixel(1,r.nextInt(400),r.nextInt(400),Color.white);              
  }   
  
  /** Ajoute un bruit important dans l'image.
   * on travail dans l'image de travail par défaut : 0
   * celle-ci doit faire au minimum 400 x 400            
  */  
  private static void xxRandomNoiseStrong() {
	    int width = getImageWidth(1);
	    int height = getImageHeight(1);  
      java.util.Random r=new java.util.Random( ); 
      
      
      for (int i=0; i<300 ;i++) 
         setPixel(1,r.nextInt(400),r.nextInt(400),Color.black); 
         
      for (int i=0; i<300 ;i++) 
         setPixel(1,xxposx+r.nextInt(xxcote),xxposy+r.nextInt(xxcote),Color.white);               
  }   
  
     
// ========================================================
// ==                  Spécifique Démo
// ========================================================  
  
  /** Détection des bords */
  public static void sideDetection(int num) {
  if ((num<1) ||(num>4)) throw new RuntimeException("Fct [sideDetection] : Num image incorrect");    
    int w = img[num].getWidth();  
    int h = img[num].getHeight();    
  
    for(int x = 0; x < w; x++) {
      for(int y = 0; y < h; y++) {
      	if (isPixelColor(num,x,y,Color.black)){
             setPixel(num,x,y,Color.red);
             break;
      	}
      }
    }  

    for(int y = 0; y < h; y++) {
      for(int x = 0; x < w; x++) {
      	if (isPixelColor(num,x,y,Color.black)){
             setPixel(num,x,y,Color.red);
             break;
      	}
      }
    }     

    for(int x = w-1; x > 0; x--) {
      for(int y = h-1; y > 0; y--) {
      	if (isPixelColor(num,x,y,Color.black)){
             setPixel(num,x,y,Color.red);
             break;
      	}
      }
    }  

    for(int y = h-1; y > 0; y--) {
      for(int x = w-1; x > 0; x--) {
      	if (isPixelColor(num,x,y,Color.black)){
             setPixel(num,x,y,Color.red);
             break;
      	}
      }
    }                                    
  }
  
  
   // Ajout de la gestion de la souris
   
  /** Définit une portion de code appelée à chaque clic de souris.
   * @param runnable La portion de code à appeler, ou null si il n'y en a pas.
   */
  public static void setRunnable(Runnable runnable) {
    getPane().setRunnable(runnable);
  }
 
  
}

