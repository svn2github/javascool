package org.javascool.proglets.rubik;

import static org.javascool.proglets.rubik.Functions.antiback;
import static org.javascool.proglets.rubik.Functions.antidown;
import static org.javascool.proglets.rubik.Functions.antifront;
import static org.javascool.proglets.rubik.Functions.antileft;
import static org.javascool.proglets.rubik.Functions.antiright;
import static org.javascool.proglets.rubik.Functions.antiup;
import static org.javascool.proglets.rubik.Functions.back;
import static org.javascool.proglets.rubik.Functions.down;
import static org.javascool.proglets.rubik.Functions.front;
import static org.javascool.proglets.rubik.Functions.left;
import static org.javascool.proglets.rubik.Functions.randomturn;
import static org.javascool.proglets.rubik.Functions.right;
import static org.javascool.proglets.rubik.Functions.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Test {
  
  private static Panel panel;

  private static void move(char c, boolean clockwise) {
    if (clockwise) {
     switch(c) {
       case 'U': up();return;
       case 'D': down();return;
       case 'L': left();return;
       case 'R': right();return;
       case 'F': front();return;
       case 'B': back();return;
       case '?': randomturn();return;
       case 'c': rotate();return;
       case 'a': antirotate();return;
       case 'u': moveup();return;
       case 'd': movedown();return;
       case 'l': moveleft();return;
       case 'r': moveright();return;
     }
   }
   else {
     switch(c) {
       case 'U': antiup();return;
       case 'D': antidown();return;
       case 'L': antileft();return;
       case 'R': antiright();return;
       case 'F': antifront();return;
       case 'B': antiback();return;
       case '?': randomturn();return;
     }
   } 
 }

 private static boolean move(String s) {

   s=s.replace(" ","");
   for(int i=0;i<s.length();i=i+1) {
     char c=s.charAt(i);
     if ("caudlr?".indexOf(c)!=-1)
       continue;
     if ("UDLRFB".indexOf(c)==-1)
       return false;
     if (i!=s.length()-1 && (s.charAt(i+1)=='2' || s.charAt(i+1)=='\'')) {
         i=i+1;
     }
   }
   
   for(int i=0;i<s.length();i=i+1) {
     char c=s.charAt(i);
     if (i==s.length()-1) {
       move(c,true);
       return true;
     }
     else {
       if (s.charAt(i+1)=='2') {
         move(c,true);
         move(c,true);
         i=i+1;
       }
       else if (s.charAt(i+1)=='\'') {
         move(c,false);
         i=i+1;
       }
       else {
         move(c,true);
       }
     }
   }
   
   return true;
 }


  
  public static void main(String[] args) throws IOException, InterruptedException {
    JFrame frame = new JFrame("Rubik's");
    panel = new Panel();
    frame.add(panel);
    
    final JTextField text = new JTextField(50);
    JButton btn = new JButton("OK");
    ActionListener a = new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean b = move(text.getText());
        if (b) {
          text.setSelectedTextColor(Color.BLACK);
          text.setText("");
          text.requestFocusInWindow();
        }
        else {
          text.setSelectedTextColor(Color.RED);
          text.selectAll();
          text.requestFocusInWindow();
        }
          
      }
    };
    btn.addActionListener(a);
    text.addActionListener(a);
    JPanel dialog = new JPanel();
    dialog.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    dialog.add(new JLabel("Commande : "),c);
    c.fill=GridBagConstraints.HORIZONTAL;
    c.weightx=1;
    dialog.add(text,c);
    c.weightx=0;
    dialog.add(btn,c);
    frame.getContentPane().add(dialog,BorderLayout.SOUTH);

    
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        text.requestFocusInWindow();
      }
    });

    frame.setSize(500, 500);
    frame.setFocusable(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    
    
    
  

  }


}
