/**
 * ControlListener.java
 *
 *
 * Created: Tue Aug 22 11:31:54 2000
 *
 * @author Markus Braendle
 * @version
 */

import java.awt.event.*;

public class ControlListener implements MouseListener {
  private static ImageChanger main;
  private static String[] texts = new String[4];
  private int controlId;

  public ControlListener(ImageChanger _main, int _id) {
    main = _main;
    controlId = _id;
    if(texts[0] == null) {
      texts[0] = "Choisissez la profondeur de couleur désirée.";
/*	    Wählen Sie die gewünschte Farbtiefe aus.";*/
    }
    if(texts[1] == null) {
      texts[1] = "Choisissez le facteur qui va être appliqué à la résolution.\n";
/*"Wählen Sie einen Faktor aus, mit welchem die Auflösung multipliziert werden soll.\n";*/
    }
    if(texts[2] == null) {
      texts[2] = "Vous pouvez ici choisir comment la palette de couleurs va être déterminée :\nOptimale : la palette est optimisée automatiquement\nMaximale : la palette contient d'abord les couleurs les plus fréquentes\nStandard : la palette contient les couleurs standard du web";

/*
 *  Sie können hier einstellen, welche Farbpalette Sie benutzen möchten.\noptimal: berechnet die optimale Farbpalette\nmaximal: verwendet die im Bild am häufigsten vorkommenden Farben als Palette\nsicherheit: die Farbpalette entspricht der standartisierten Web-Safety-Palette
 */
    }
    if(texts[3] == null) {
      texts[3] = "Vous pouvez ici choisir quelle image sera traitée :\nInitiale : l'image initiale est prise comme source\nModifiée : l'image traitée ci-dessus est prise comme source.";

/*
 *  Sie können hier einstellen, welches Bild Sie bearbeiten möchten.\nHauptbild: als Vorlage wird das gewählte Bild verwendet\nbearbeitetes Bild: als Vorlage wird das andere, von Ihnen veränderte Bild genommen
 */
    }
  }
  public void mouseEntered(MouseEvent e) {
    main.setInfoText(texts[controlId]);
  }
  public void mouseExited(MouseEvent e) {
    main.setInfoText("");
  }
  public void mouseClicked(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
}
