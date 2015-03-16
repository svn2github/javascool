import java.awt.*;

public class Title extends Canvas {
  private String title;

  public Title(String _title) {
    title = _title;
    // this.setBackground(Color.green);
  }
  public void paint(Graphics g) {
    g.drawString(title, 10, 15);
  }
}
