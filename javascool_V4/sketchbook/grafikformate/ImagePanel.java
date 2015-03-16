import java.awt.*;
import java.awt.event.*;

public class ImagePanel extends Panel implements ActionListener, ItemListener {
  private ButtonPanel controller;
  private CopyView imageView;
  private Image myImage;
  private ImageChanger parent;
  private int myNumber;
  private colorPalette CP;

  public ImagePanel(ImageChanger _parent, Image _img, int _number) {
    parent = _parent;
    myImage = _img;
    myNumber = _number;

    this.setLayout(new BorderLayout());
    this.setBackground(new Color(210, 210, 210));
    CP = new colorPalette(parent);
    imageView = new CopyView(parent, CP);
    imageView.setImage(myImage);
    imageView.setSize(myImage.getWidth(this), myImage.getHeight(this) + 20);
    controller = new ButtonPanel(this, parent, imageView);
    CP.setSize(160, myImage.getHeight(this) + 20);

    this.add(controller, "West");
    this.add(imageView, "Center");
    this.add(CP, "East");
    this.repaint();
  }
  public Image getCurrentImage() {
    return imageView.getImage();
  }
  public void setImage(Image img) {
    imageView.setImage(img);
  }
  public void itemStateChanged(ItemEvent e) {
    String source = (String) e.getItem();
    if(source.equalsIgnoreCase("Image initiale")) {
      imageView.setImage(parent.getOriginal());
    } else {
      imageView.setImage(parent.getOtherImage(myNumber));
    }
  }
  public void actionPerformed(ActionEvent e) {
    if(e.getActionCommand() == "mettre Ã  jour") {
      controller.updateImage();
    } else if(e.getActionCommand() == "refaire") {
      imageView.setImage(parent.getOriginal());
    }
  }
}
