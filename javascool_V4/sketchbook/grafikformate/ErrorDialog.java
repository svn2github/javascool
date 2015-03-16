/**
 * ErrorDialog.java
 *
 *
 * Created: Wed Aug 23 16:11:50 2000
 *
 * @author Markus Braendle
 * @version
 */

import java.awt.*;
import java.awt.event.*;

public class ErrorDialog extends Dialog implements WindowListener {
  private Button okButton;
  private MultiLineLabel errorText;

  public ErrorDialog(Frame parent, String title, String message, Point loc) {
    super(parent, title, true);
    this.addWindowListener(this);
    this.setLayout(new GridBagLayout());
    this.setLocation(loc.x + 30, loc.y + 30);
    this.setBackground(Color.red);
    errorText = new MultiLineLabel(message);

    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(5, 5, 5, 5);
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.CENTER;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;

    this.add(errorText, c);
    c.gridy = 1;
    okButton = new Button("OK");
    this.add(okButton, c);
    this.pack();
    this.show();
  }
  public boolean action(Event e, Object args) {
    if(e.target == okButton) {
      this.hide();
      this.dispose();
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
} // ErrorDialog
