/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

//import org.javascool.dicho.Dicho;

// Used to open an window
import javax.swing.JFrame;

public class Test {
  public static void main(String arguments[]) {
    Dicho dicho = new Dicho(); 
    // Opens the panel in a frame
    {
      JFrame frame = new JFrame();
      frame.setTitle("Dicho test"); 
      frame.setSize(600, 400);
      frame.getContentPane().add(dicho);
      frame.pack(); 
      frame.setVisible(true);
    }
    // Plays with the panel

  }
}
