import java.awt.*;
import java.util.Vector;

public class InfoView extends Canvas {
  private String text = "";
  private Vector textVec = new Vector();
  private boolean show = false;
  public boolean hide = true;
  public long lastOut = System.currentTimeMillis();
  public InfoView() {
    this.setBackground(new Color(210, 210, 210));
  }
  public void setText(String _text) {
    text = _text;
    if(text.equalsIgnoreCase("")) {
      updateThread ut = new updateThread(this);
      ut.start();
      hide = false;
      show = false;
      lastOut = System.currentTimeMillis();
    } else {
      show = true;
      adjustText();
    }
  }
  public void paint(Graphics g) {
    Dimension d = this.getSize();
    if(show || !hide) {
      String outText;
      g.setColor(new Color(255, 255, 190));
      g.fillRect(0, 0, d.width, d.height);
      g.setColor(Color.black);
      g.drawString("Aide:", 10, 15);
      for(int i = 0; i < textVec.size(); i++) {
        outText = (String) textVec.elementAt(i);
        g.drawString(outText, 15, (35 + 18 * i));
      }
    } else {
      g.setColor(new Color(210, 210, 210));
      g.fillRect(0, 0, d.width, d.height);
    }
  }
  public void update(Graphics g) {
    paint(g);
  }
  private void adjustText() {
    FontMetrics fontmet = this.getFontMetrics(this.getFont());
    textVec = new Vector();
    String lineText, restText;
    char splitChar = '\n';
    int splitPos;
    boolean done = false;
    boolean textDone = false;
    Dimension d = this.getSize();
    while(!done) {
      splitPos = text.indexOf(splitChar);
      if(splitPos != -1) {
        lineText = text.substring(0, splitPos);
        text = text.substring(splitPos + 1, text.length());
      } else {
        done = true;
        lineText = text;
      }
      if(text.length() == 0) {
        done = true;
      }
      int textWidth = fontmet.stringWidth(lineText);
      while(textWidth > (d.width - 20)) {
        textDone = false;
        for(int i = lineText.length() - 1; (!textDone && (i >= 0)); i--)
          if(lineText.charAt(i) == (char) 32) {
            if(fontmet.stringWidth(lineText.substring(0, i)) < (d.width - 20)) {
              textVec.addElement(lineText.substring(0, i));
              if(i < lineText.length()) {
                lineText = lineText.substring(i + 1, lineText.length());
              }
              textWidth = fontmet.stringWidth(lineText);
              textDone = true;
            }
          }
      }
      textVec.addElement(lineText);
    }
  }
  public class updateThread extends Thread {
    private InfoView parent;
    public updateThread(InfoView _parent) {
      parent = _parent;
    }
    public void run() {
      try {
        this.sleep(1000);
      } catch(Exception e) {}
      if((System.currentTimeMillis() - parent.lastOut) >= 1000) {
        if(parent != null) {
          parent.hide = true;
          parent.repaint();
        }
      }
    }
  }
}
