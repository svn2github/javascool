/*********************************************************************************
* Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
* Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
*********************************************************************************/
package org.javascool.builder;

import org.javascool.widgets.MainFrame;
import javax.swing.JPopupMenu;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.javascool.widgets.Console;
import javax.swing.JProgressBar;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.javascool.gui.About;

/** Définit l'interface graphique pour la construction de proglets.
 *
 * @see <a href="DialogFrame.java.html">code source</a>
 * @serial exclude
 */
public class DialogFrame {
  /** Ouvre une console indépendante pour lancer la construction de proglets. */
  public static void startFrame() {
    jCreatorButton = getConsoleInstance().getToolBar().addTool("Créer une nouvelle proglet", "org/javascool/widgets/icons/new.png",
                                                                new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                    startProgletCreatorMenu();
                                                                  }
                                                                }
                                                                );
    jBuilderButton = getConsoleInstance().getToolBar().addTool("Lancement du builder", "org/javascool/widgets/icons/compile.png",
                                                                new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                    startProgletBuilderMenu();
                                                                  }
                                                                }
                                                                );
    getConsoleInstance().getToolBar().addTool("Progress Bar", jProgressBar = new JProgressBar());
    jProgressBar.setSize(new Dimension(100, 25));
    getConsoleInstance().getToolBar().addTool("Status Bar", jLabel = new JLabel());
    getConsoleInstance().getToolBar().addRightTool("Convertisseur HML", new Runnable() {
                                                      @Override
                                                      public void run() {
                                                        startConvertisseurHML();
                                                      }
                                                    }
                                                    );
    getConsoleInstance().getToolBar().addRightTool(About.getAboutMessage());
    setUpdate("", 0);
    new MainFrame().reset("Java's Cool 4 Proglet Buidler", Build.logo, getConsoleInstance());
  }
  /** Met à jour la progression de la construction.
   * @param statut Statut sur l'opération en cours. Un message de 64 caractères max.
   * @param percent Pourcentage de complétion entre 0 et 100.
   */
  public static void setUpdate(String statut, int percent) {
    while(statut.length() < 64) {
      statut += " ";
    }
    if(jLabel != null) {
      jLabel.setText(statut);
    }
    if(jProgressBar != null) {
      jProgressBar.setValue(percent);
    }
  }
  private static JButton jBuilderButton = null;
  private static JButton jCreatorButton = null;
  private static JLabel jLabel = null;
  private static JProgressBar jProgressBar = null;
  // Ouvre un menu de sélection des proglets et de lancement de la construction du Jar.
  private static void startProgletCreatorMenu() {
    JPopupMenu jCreatorMenu = new JPopupMenu();
    jCreatorMenu.add(new JLabel("Entrer le nom de la proglet à construire:", JLabel.LEFT));
    jCreatorMenuDir = new JTextField();
    jCreatorMenuDir.setText(System.getProperty("user.dir") + File.separator);
    jCreatorMenuDir.setEditable(false);
    jCreatorMenu.add(new JPanel() {
                       {
                         add(jCreatorMenuDir);
                         add(jCreatorMenuName = new JTextField(20));
                       }
                     }
                     );
    JMenuItem menuitem = new JMenuItem("Créer le répertoire et les fichiers exemples");
    jCreatorMenu.add(menuitem);
    menuitem.addActionListener(new ActionListener() {
                                 @Override
                                 public void actionPerformed(ActionEvent e) {
                                   new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                  String name = jCreatorMenuName.getText();
                                                  if(name != null&& name.length() > 0) {
                                                    ProgletCreator.mkdirProglet(jCreatorMenuDir.getText() + name);
                                                  }
                                                }
                                              }
                                              ).start();
                                 }
                               }
                               );
    Component parent = getConsoleInstance().getToolBar();
    jCreatorMenu.show(jCreatorButton, 0, parent.getHeight());
  }
  private static JTextField jCreatorMenuDir, jCreatorMenuName;
  // Ouvre un menu de sélection des proglets et de lancement de la construction du Jar.

  private static void startProgletBuilderMenu() {
    boolean reload = false;
    if((jBuilderMenu == null) || reload) {
      jBuilderMenu = new JPopupMenu();
      if(ProgletsBuilder.getProglets().length > 0) {
        jBuilderMenu.add(new JLabel("Sélectionner les proglets à construire:"));
        for(String proglet : ProgletsBuilder.getProglets()) {
          JCheckBox check = new JCheckBox(proglet);
          check.setSelected(true);
          jBuilderMenu.add(check);
        }
        jBuilderMenu.addSeparator();
        JMenuItem menuitem = new JMenuItem("Construire le jar");
        menuitem.addActionListener(new ActionListener() {
                                     @Override
                                     public void actionPerformed(ActionEvent e) {
                                       new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                      ArrayList<String> proglets = new ArrayList<String>();
                                                      for(Component c: jBuilderMenu.getComponents())
                                                        if(c instanceof JCheckBox && ((JCheckBox) c).isSelected()) {
                                                          proglets.add(((JCheckBox) c).getText());
                                                        }
                                                      getConsoleInstance().clear();
                                                      ProgletsBuilder.build(proglets.toArray(new String[proglets.size()]));
                                                    }
                                                  }
                                                  ).start();
                                     }
                                   }
                                   );
        jBuilderMenu.add(menuitem);
      } else {
        jBuilderMenu.add(new JLabel("Aucune proglet à construire dans ce répertoire"));
      }
    }
    Component parent = getConsoleInstance().getToolBar();
    jBuilderMenu.show(jBuilderButton, 0, parent.getHeight());
  }
  // @ inner-class-variable
  private static JPopupMenu jBuilderMenu = null;

  private static void startConvertisseurHML() {
    JPopupMenu jCreatorMenu = new JPopupMenu();
    jCreatorMenu.add(new Htm2Hml());
    Component parent = getConsoleInstance().getToolBar();
    jCreatorMenu.show(jCreatorButton, 0, parent.getHeight());
  }  
  /** Renvoie la console courante. 
   * @return La console courante.
   */
  private static Console getConsoleInstance() {
    return console;
  }
  private static Console console = Console.newInstance();
}
