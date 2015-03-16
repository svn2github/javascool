import java.awt.*;
import java.util.Vector;
import java.awt.event.*;

public class colorPalette extends Canvas {
  private Vector colors;
  private int width;
  private ImageChanger parent;
  private PaletteListener myListener;

  public colorPalette(ImageChanger _parent) {
    colors = null;
    parent = _parent;
    myListener = new PaletteListener(parent);
  }
  public void setColors(Vector _colors) {
    colors = _colors;
    if(colors != null) {
      int size = colors.size();
      if(size <= 4) {
        width = 4;
      } else if(size <= 16) {
        width = 4;
      } else if(size <= 64) {
        width = 8;
      } else {
        width = 16;
      }
      this.addMouseListener(myListener);
    } else {
      this.removeMouseListener(myListener);
    }
    this.repaint();
  }
  public void paint(Graphics g) {
    int j = 0;
    int k = 1;
    if(colors != null) {
      for(int i = 0; i < colors.size(); i++) {
        j++;
        if(j > width) {
          k++;
          j = 1;
        }
        g.setColor(new Color(((Integer) (colors.elementAt(i))).intValue()));
        g.fillRect(9 * j, 9 * k, 8, 8);
      }
    }
  }
}