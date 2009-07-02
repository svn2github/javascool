package compiler;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AppletFrame {
  public static void main(String[] args) {
	  final InterfacePrincipale myApplet = new InterfacePrincipale(); 
    Frame myFrame = new Frame("Applet Holder"); 
   
  
   
    myApplet.init();	
   
  
    myFrame.add(myApplet, BorderLayout.CENTER);
    myFrame.pack(); 
    myFrame.setVisible(true); 
    myFrame.setSize(myApplet.getSize());
    myFrame.addWindowListener (
            new WindowAdapter()
{
public void windowClosing (
             WindowEvent event)
{
	myApplet.stop();
	myApplet.destroy();
  System.exit(0);
}
});

  } 
} 