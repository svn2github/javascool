import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends Panel implements ItemListener {
  private ImageChanger main;
  private ImagePanel parent;
  private Choice selecter = new Choice();
  private Choice colorDepth = new Choice();
  private Choice colorAlgo = new Choice();
  private Choice imageSource = new Choice();

  private Button updateButton = new Button("mettre à jour");
  private Button resetButton = new Button("refaire");
  private ButtonListener[] buttonListeners = new ButtonListener[2];

  private ControlListener[] controlListeners = new ControlListener[4];

  private Title title = new Title("Résolution :");
  private Title ctitle = new Title("Profondeur :");
  private Title ptitle = new Title("Palette :");
  private Title ititle = new Title("Source :");
  public ControlPanel(ImageChanger _main, ImagePanel _parent) {
    main = _main;
    parent = _parent;
    buttonListeners[0] = new ButtonListener(main, 0);
    buttonListeners[1] = new ButtonListener(main, 1);

    updateButton.addActionListener(parent);
    resetButton.addActionListener(parent);
    updateButton.addMouseListener(buttonListeners[0]);
    resetButton.addMouseListener(buttonListeners[1]);

    this.setLayout(new GridBagLayout());
    title.setSize(90, 20);
    ctitle.setSize(90, 20);
    ptitle.setSize(90, 20);
    ititle.setSize(90, 20);
    controlListeners[0] = new ControlListener(main, 0);
    controlListeners[1] = new ControlListener(main, 1);
    controlListeners[2] = new ControlListener(main, 2);
    controlListeners[3] = new ControlListener(main, 3);

    selecter.add("4");
    selecter.add("2");
    selecter.add("1");
    selecter.add("0.5");
    selecter.add("0.25");
    selecter.add("0.125");
    selecter.add("0.0625");
    selecter.select("1");

    colorDepth.add("24 bits");
    colorDepth.add("256 couleurs");
    colorDepth.add("16 couleurs");
    colorDepth.add("4 couleurs");
    colorDepth.add("2 couleurs");
    colorDepth.add("Niveaux de gris");

    colorAlgo.add("Optimale");
    colorAlgo.add("Maximale");
    colorAlgo.add("Standard");

    imageSource.add("Initiale");
    imageSource.add("Modifiée");

    colorDepth.addMouseListener(controlListeners[0]);
    selecter.addMouseListener(controlListeners[1]);
    colorAlgo.addMouseListener(controlListeners[2]);
    imageSource.addMouseListener(controlListeners[3]);
    imageSource.addItemListener(parent);

    colorDepth.addItemListener(this);
    colorAlgo.addItemListener(this);

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.CENTER;
    c.insets = new Insets(5, 5, 5, 5);
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = c.weighty = 1;
    c.anchor = GridBagConstraints.EAST;
    this.add(ctitle, c);
    c.gridx = 1;
    c.anchor = GridBagConstraints.WEST;
    this.add(colorDepth, c);
    c.gridx = 0;
    c.gridy = 1;
    c.anchor = GridBagConstraints.EAST;
    this.add(ptitle, c);
    c.gridx = 1;
    c.anchor = GridBagConstraints.WEST;
    this.add(colorAlgo, c);
    c.gridx = 0;
    c.gridy = 2;
    c.anchor = GridBagConstraints.EAST;
    this.add(title, c);
    c.gridx = 1;
    c.anchor = GridBagConstraints.WEST;
    this.add(selecter, c);
    c.gridx = 0;
    c.gridy = 3;
    c.anchor = GridBagConstraints.EAST;
    this.add(ititle, c);
    c.gridx = 1;
    c.anchor = GridBagConstraints.WEST;
    this.add(imageSource, c);
    c.gridx = 0;
    c.gridy = 4;
    c.anchor = GridBagConstraints.EAST;
    this.add(resetButton, c);
    c.gridx = 1;
    c.anchor = GridBagConstraints.CENTER;
    this.add(updateButton, c);
  }
  public float getSelectedFactor() {
    if(selecter.getSelectedItem() != null) {
      return (Float.valueOf(selecter.getSelectedItem())).floatValue();
    } else {
      return 0f;
    }
  }
  public int getSelectedResolution() {
    String selected, number;
    int where;
    if(colorDepth.getSelectedItem() != null) {
      if((colorDepth.getSelectedItem()).equalsIgnoreCase("Niveaux de gris")) {
        return 0;
      } else {
        selected = colorDepth.getSelectedItem();
        where = selected.indexOf(' ');
        number = selected.substring(0, where);
        return (Integer.valueOf(number)).intValue();
      }
    } else {
      return 24;
    }
  }
  public int getSelectedPalette() {
    if((colorAlgo.getSelectedItem()).equalsIgnoreCase("Optimale")) {
      return 0;
    } else if((colorAlgo.getSelectedItem()).equalsIgnoreCase("Maximale")) {
      return 1;
    } else if((colorAlgo.getSelectedItem()).equalsIgnoreCase("Standard")) {
      return 2;
    }
    return 0;
  }
  public void itemStateChanged(ItemEvent e) {
    String itemName = (String) e.getItem();
    if(itemName.equalsIgnoreCase("Niveaux de gris")) {
      colorAlgo.setVisible(false);
      ptitle.setVisible(false);
    } else if(itemName.indexOf("couleurs") > 0) {
      colorAlgo.setVisible(true);
      ptitle.setVisible(true);
      if((colorAlgo.getSelectedItem()).equalsIgnoreCase("Standard")) {
        colorAlgo.select("Optimale");
      }
    } else if(itemName.equalsIgnoreCase("24 bits")) {
      colorAlgo.setVisible(true);
      ptitle.setVisible(true);
      if((colorAlgo.getSelectedItem()).equalsIgnoreCase("Standard")) {
        colorAlgo.select("Optimale");
      }
    } else if(itemName.equalsIgnoreCase("Standard")) {
      colorDepth.select("256 couleurs");
    }
  }
}
