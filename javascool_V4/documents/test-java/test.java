import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Dimension;

public class test {
  public static void main (String[] args){
    JFrame frame = new JFrame();
    frame.setTitle("Java installation test");
    frame.setMinimumSize(new Dimension(300, 100));
    frame.add(new JLabel("Java seems to work !", JLabel.CENTER));
    frame.pack();
    frame.setVisible(true);
  }
}
