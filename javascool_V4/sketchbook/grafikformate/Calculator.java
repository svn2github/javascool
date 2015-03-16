/**
 * Calculator.java
 *
 *
 * Created: Wed Aug 23 11:59:52 2000
 *
 * @author Markus Braendle
 */

import java.awt.*;
import java.awt.event.*;

public class Calculator extends Frame implements ItemListener, WindowListener {
  Title widthTitle = new Title("Largeur :");
  Title heightTitle = new Title("Hauteur :");
  Title resTitle = new Title("Résolution :");
  Title depthTitle = new Title("Profondeur :");
  Title resultTitle = new Title("Taille image :");

  TextField width = new TextField("20", 5);
  TextField height = new TextField("30", 5);
  TextField resolution = new TextField("72 dpi", 5);
  TextField result = new TextField("", 10);

  Choice wScale = new Choice();
  Choice hScale = new Choice();
  Choice colorDepth = new Choice();

  Button ok = new Button("calculer");

  public Calculator() {
    super("Quelle sera la taille de mon image ?");
    this.setSize(400, 250);
    this.addWindowListener(this);
    this.setBackground(new Color(210, 210, 210));
    this.setLocation(100, 100);

    widthTitle.setSize(60, 20);
    heightTitle.setSize(60, 20);
    resTitle.setSize(80, 20);
    depthTitle.setSize(80, 20);
    resultTitle.setSize(100, 20);

    result.setEditable(false);
    // resTitle.hide();
    // resolution.hide();

    wScale.add("cm");
    wScale.add("dpi");
    hScale.add("cm");
    hScale.add("dpi");

    wScale.addItemListener(this);
    hScale.addItemListener(this);

    colorDepth.add("24 bits");
    colorDepth.add("256 couleurs");
    colorDepth.add("16 couleurs");
    colorDepth.add("4 couleurs");
    colorDepth.add("2 couleurs");
    colorDepth.add("Niveaux de gris");

    colorDepth.addItemListener(this);

    this.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.CENTER;
    c.insets = new Insets(10, 3, 3, 3);
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = c.weighty = 0;

    c.gridx = 0;
    this.add(widthTitle, c);
    c.gridx = 1;
    this.add(width, c);
    c.gridx = 2;
    this.add(wScale, c);
    c.gridx = 3;
    this.add(resTitle, c);
    c.gridx = 4;
    this.add(resolution, c);

    c.gridx = 0;
    c.gridy = 1;
    this.add(heightTitle, c);
    c.gridx = 1;
    this.add(height, c);
    c.gridx = 2;
    this.add(hScale, c);
    c.gridx = 3;
    this.add(depthTitle, c);
    c.gridx = 4;
    c.gridwidth = 2;
    this.add(colorDepth, c);

    c.fill = GridBagConstraints.CENTER;
    c.gridx = 1;
    c.gridy = 2;
    c.gridwidth = 3;
    this.add(ok, c);

    c.gridy = 3;
    c.gridx = 1;
    c.gridwidth = 2;
    this.add(resultTitle, c);
    c.gridx = 3;
    c.gridwidth = 2;
    c.fill = GridBagConstraints.WEST;
    this.add(result, c);

    this.show();
  }
  public void itemStateChanged(ItemEvent e) {
    String itemName = (String) e.getItem();
    if(itemName.equalsIgnoreCase("cm")) {
      resTitle.setVisible(true);
      resolution.setVisible(true);
      hScale.select(itemName);
      wScale.select(itemName);
    } else if(itemName.equalsIgnoreCase("dpi")) {
      resTitle.setVisible(false);
      resolution.setVisible(false);
      hScale.select(itemName);
      wScale.select(itemName);
    }
    this.repaint();
  }
  private void calculate() {
    String scaleString = wScale.getSelectedItem();
    String selected = colorDepth.getSelectedItem();
    String number, errorString = "";
    int depth, selectedVal, where, heightV = 0, widthV = 0;
    if(selected != null) {
      if(selected.equalsIgnoreCase("Graustufen")) {
        depth = 8;
      } else {
        where = selected.indexOf(' ');
        number = selected.substring(0, where);
        selectedVal = (Integer.valueOf(number)).intValue();
        if(selectedVal == 24) {
          depth = 24;
        } else if(selectedVal == 256) {
          depth = 8;
        } else if(selectedVal == 16) {
          depth = 4;
        } else if(selectedVal == 4) {
          depth = 2;
        } else if(selectedVal == 2) {
          depth = 1;
        } else { depth = 24;
        }
      }
    } else {
      depth = 24;
    }
    try {
      heightV = (Integer.valueOf(height.getText()).intValue());
    } catch(Exception e) {
      errorString += "la valeur donnÃ©e pour la hauteur est incorrecte.\n";
    }
    try {
      widthV = (Integer.valueOf(width.getText()).intValue());
    } catch(Exception e) {
      errorString += "la valeur donnÃ©e pour la largeur est incorrecte.\n";
    }
    if(scaleString.equalsIgnoreCase("cm")) {
      try {
        String resString = resolution.getText();
        int pos = resString.indexOf("dpi");
        if(pos > 0) {
          resString = resString.substring(0, pos);
        }
        pos = resString.indexOf(' ');
        if(pos > 0) {
          resString = resString.substring(0, pos);
        }
        int res = (Integer.valueOf(resString).intValue());
        float tmp = (widthV / 2.54f * res);
        widthV = (int) tmp;
        tmp = (heightV / 2.54f) * res;
        heightV = (int) tmp;
      } catch(Exception e) {
        errorString += "la valeur donnÃ©e pour la rÃ©solution est incorrecte.\n";
      }
    }
    if(errorString.equalsIgnoreCase("")) {
      float size = 1.0f * widthV * heightV * depth;
      float ksize = size / 8;
      if(ksize > (1024 * 1024.0f)) {
        ksize = ksize / 1024 / 1024;
        int tmp = (int) (10 * ksize);
        ksize = tmp / 10.0f;
        result.setText("" + ksize + " Mo");
      } else if(ksize > 1024.0f) {
        ksize = ksize / 1024;
        int tmp = (int) (10 * ksize);
        ksize = tmp / 10.0f;
        result.setText("" + ksize + " Ko");
      } else {
        result.setText("" + ksize + " octets");
      }
    } else {
      ErrorDialog ED = new ErrorDialog(this, "Erreur", errorString, this.getLocationOnScreen());
    }
    this.repaint();
  }
  public boolean action(Event e, Object arg) {
    if(e.target == ok) {
      calculate();
    }
    return true;
  }
  public void windowClosing(WindowEvent e) {
    this.dispose();
  }
  public void windowOpened(WindowEvent e) {}
  public void windowClosed(WindowEvent e) {}
  public void windowIconified(WindowEvent e) {}
  public void windowDeiconified(WindowEvent e) {}
  public void windowActivated(WindowEvent e) {}
  public void windowDeactivated(WindowEvent e) {}
} // Calculator
