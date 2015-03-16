import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public class ImageView extends Canvas {
  protected Image myImage;

  public ImageView() {
    myImage = null;

    // this.addActionListener(this);
  }
  public void setImage(Image _img) {
    MediaTracker MT = new MediaTracker(this);
    myImage = _img.getScaledInstance(_img.getWidth(this), _img.getHeight(this), Image.SCALE_SMOOTH);
    MT.addImage(myImage, 0);
    try { MT.waitForID(0);
    } catch(InterruptedException e) {}
    this.repaint();
  }
  public Image getImage() {
    return myImage;
  }
  public void paint(Graphics g) {
    g.drawImage(myImage, 10, 0, 200, 150, this);
  }
}
