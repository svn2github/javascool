import java.awt.event.*;

public class PaletteListener implements MouseListener {
  private static ImageChanger main;
  private static String text;

  public PaletteListener(ImageChanger _main) {
    main = _main;
    text = "Vous voyez ici la palette de couleurs employée par l'image située près du curseur de la souris";
/*	"Sie sehen hier die für das momentan angezeigte Bild verwendete Farbpalette.";*/
  }
  public void mouseEntered(MouseEvent e) {
    main.setInfoText(text);
  }
  public void mouseExited(MouseEvent e) {
    main.setInfoText("");
  }
  public void mouseClicked(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
}
