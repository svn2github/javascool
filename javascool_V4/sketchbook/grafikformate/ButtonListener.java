/**
 * ButtonListener.java
 *
 *
 * Created: Tue Aug 22 11:31:54 2000
 *
 * @author Markus Braendle
 * @version
 */

import java.awt.event.*;

public class ButtonListener implements MouseListener {
  private static ImageChanger main;
  private static String[] texts = new String[2];
  private int buttonId;

  public ButtonListener(ImageChanger _main, int _id) {
    main = _main;
    buttonId = _id;
    if(texts[0] == null) {
      texts[0] = "Cliquez ici pour appliquer vos choix et transformer l'image en conséquence.";
    }
    if(texts[1] == null) {
      texts[1] = "Cliquez ici pour reprendre l'image initiale.";
    }
  }
  public void mouseEntered(MouseEvent e) {
    main.setInfoText(texts[buttonId]);
  }
  public void mouseExited(MouseEvent e) {
    main.setInfoText("");
  }
  public void mouseClicked(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
}
