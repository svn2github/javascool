import java.awt.*;
// import javax.swing.*;

public class ButtonPanel extends Panel {
  private ImagePanel parent;
  private ControlPanel controlPanel;
  private CopyView imageView;
  private ImageChanger main;

  public ButtonPanel(ImagePanel _parent, ImageChanger _main, CopyView _imageView) {
    parent = _parent;
    imageView = _imageView;
    main = _main;
    controlPanel = new ControlPanel(main, parent);

    this.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.CENTER;
    c.anchor = GridBagConstraints.NORTH;
    c.insets = new Insets(5, 5, 5, 20);
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = c.weighty = 1;
    this.add(controlPanel, c);
  }
  public void updateImage() {
    float factor = controlPanel.getSelectedFactor();
    int resolution = controlPanel.getSelectedResolution();
    int palette = controlPanel.getSelectedPalette();
    imageView.changeImage(factor, resolution, palette);
  }
}
