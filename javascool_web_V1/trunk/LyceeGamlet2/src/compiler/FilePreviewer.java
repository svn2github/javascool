package compiler;

/**
 * <p>Titre : FilePreviewer</p>
 * <p>Description : Classe permettant d'afficher l'image dans un JFileChooser</p>
 * <p>Copyright : Copyright (c) 2003</p>
 * <p>Société : perso</p>
 * @author Indiana_jules
 * @version 1.0
 */

//Importation des packages nécessaires
import java.awt.*;
import java.io.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.border.*;


public  class FilePreviewer extends JComponent implements PropertyChangeListener {
  //Variables globales
  ImageIcon thumbnail = null;

  /**Constructeur*/
  public FilePreviewer(JFileChooser fc) {
    setPreferredSize(new Dimension(100, 50));
    fc.addPropertyChangeListener(this);
    setBorder(new BevelBorder(BevelBorder.LOWERED));
  }

  /**Méthode permettant le chargement des images*/
  public void loadImage(File f) {
    if (f == null) {
      thumbnail = null;
    } else {
      ImageIcon tmpIcon = new ImageIcon(f.getPath());
      if(tmpIcon.getIconWidth() > 90) {
        thumbnail = new ImageIcon(
            tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
      } else {
        thumbnail = tmpIcon;
      }
    }
  }

  /**Méthode appelé dans le cas d'un changement de fichier sélectionné*/
  public void propertyChange(PropertyChangeEvent e) {
    String prop = e.getPropertyName();
    if(prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
      if(isShowing()) {
        loadImage((File) e.getNewValue());
        repaint();
      }
    }
  }

  /**Permet de rafraîchir le component*/
  public void paint(Graphics g) {
    super.paint(g);
    if(thumbnail != null) {
      int x = getWidth()/2 - thumbnail.getIconWidth()/2;
      int y = getHeight()/2 - thumbnail.getIconHeight()/2;
      if(y < 0) {
        y = 0;
      }

      if(x < 5) {
        x = 5;
      }
      thumbnail.paintIcon(this, g, x, y);
    }
  }
}
