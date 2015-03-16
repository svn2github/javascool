/*********************************************************************************
* Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
* Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
*********************************************************************************/
package org.javascool.builder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.javascool.About;
import org.javascool.Build;
import org.javascool.widgets.Console;
import org.javascool.widgets.MainFrame;

/**
 * Définit l'interface graphique pour la construction de proglets.
 *
 * @see <a href="DialogFrame.java.html">code source</a>
 * @serial exclude
 */
public class DialogFrame {
  /** Ouvre une console indépendante pour lancer la construction de proglets. */
  public static void startFrame() {
    DialogFrame.jCreatorButton = Console
                                 .getInstance()
                                 .getToolBar()
                                 .addTool("Créer une nouvelle proglet",
                                          "org/javascool/widgets/icons/new.png", new Runnable() {
                                            @Override
                                            public void run() {
                                              DialogFrame.startProgletCreatorMenu();
                                            }
                                          }
                                          );
    DialogFrame.jBuilderButton = Console
                                 .getInstance()
                                 .getToolBar()
                                 .addTool("Lancement du builder",
                                          "org/javascool/widgets/icons/compile.png",
                                          new Runnable() {
                                            @Override
                                            public void run() {
                                              DialogFrame.startProgletBuilderMenu();
                                            }
                                          }
                                          );
    Console.getInstance()
    .getToolBar()
    .addTool("Progress Bar",
             DialogFrame.jProgressBar = new JProgressBar());
    DialogFrame.jProgressBar.setSize(new Dimension(100, 25));
    Console.getInstance().getToolBar()
    .addTool("Status Bar", DialogFrame.jLabel = new JLabel());
    Console.getInstance().getToolBar()
    .addRightTool("Convertisseur HML", new Runnable() {
                    @Override
                    public void run() {
                      DialogFrame.startConvertisseurHML();
                    }
                  }
                  );
    Console.getInstance().getToolBar()
    .addRightTool(About.getAboutMessage());
    DialogFrame.setUpdate("", 0);
    new MainFrame().reset("Java's Cool 4 Proglet Buidler", Build.logo,
                          Console.getInstance());
  }
  /**
   * Met à jour la progression de la construction.
   *
   * @param statut
   *            Statut sur l'opération en cours. Un message de 64 caractères
   *            max.
   * @param percent
   *            Pourcentage de complétion entre 0 et 100.
   */
  public static void setUpdate(String statut, int percent) {
    while(statut.length() < 64) {
      statut += " ";
    }
    if(DialogFrame.jLabel != null) {
      DialogFrame.jLabel.setText(statut);
    }
    if(DialogFrame.jProgressBar != null) {
      DialogFrame.jProgressBar.setValue(percent);
    }
  }
  private static JButton jBuilderButton = null;
  private static JButton jCreatorButton = null;
  private static JLabel jLabel = null;
  private static JProgressBar jProgressBar = null;

  // Ouvre un menu de sélection des proglets et de lancement de la
  // construction du Jar.
  private static void startProgletCreatorMenu() {
    JPopupMenu jCreatorMenu = new JPopupMenu();
    jCreatorMenu.add(new JLabel(
                       "Entrer le nom de la proglet à construire:",
                       SwingConstants.LEFT));
    DialogFrame.jCreatorMenuDir = new JTextField();
    DialogFrame.jCreatorMenuDir.setText(System.getProperty("user.dir")
                                        + File.separator);
    DialogFrame.jCreatorMenuDir.setEditable(false);
    jCreatorMenu.add(new JPanel() {
                       /**
                        *
                        */
                       private static final long serialVersionUID = -236539671856928833L;

                       {
                         add(DialogFrame.jCreatorMenuDir);
                         add(DialogFrame.jCreatorMenuName = new JTextField(20));
                       }
                     }
                     );
    JMenuItem menuitem = new JMenuItem(
      "Créer le répertoire et les fichiers exemples");
    jCreatorMenu.add(menuitem);
    menuitem.addActionListener(new ActionListener() {
                                 @Override
                                 public void actionPerformed(ActionEvent e) {
                                   new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                  String name = DialogFrame.jCreatorMenuName.getText();
                                                  if(name != null&& name.length() > 0) {
                                                    ProgletCreator
                                                    .mkdirProglet(DialogFrame.jCreatorMenuDir
                                                                  .getText() + name);
                                                  }
                                                }
                                              }
                                              ).start();
                                 }
                               }
                               );
    Component parent = Console.getInstance().getToolBar();
    jCreatorMenu.show(DialogFrame.jCreatorButton, 0, parent.getHeight());
  }
  private static JTextField jCreatorMenuDir, jCreatorMenuName;

  // Ouvre un menu de sélection des proglets et de lancement de la
  // construction du Jar.

  private static void startProgletBuilderMenu() {
    boolean reload = false;
    if((DialogFrame.jBuilderMenu == null) || reload) {
      DialogFrame.jBuilderMenu = new JPopupMenu();
      if(ProgletsBuilder.getProglets().length > 0) {
        DialogFrame.jBuilderMenu.add(new JLabel(
                                       "Sélectionner les proglets à construire:"));
        for(String proglet : ProgletsBuilder.getProglets()) {
          JCheckBox check = new JCheckBox(proglet);
          check.setSelected(true);
          DialogFrame.jBuilderMenu.add(check);
        }
        DialogFrame.jBuilderMenu.addSeparator();
        JMenuItem menuitem = new JMenuItem("Construire le jar");
        menuitem.addActionListener(new ActionListener() {
                                     @Override
                                     public void actionPerformed(ActionEvent e) {
                                       new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                      ArrayList<String> proglets = new ArrayList<String>();
                                                      for(Component c: DialogFrame.jBuilderMenu
                                                          .getComponents())
                                                        if(c instanceof JCheckBox
                                                           && ((JCheckBox) c).isSelected())
                                                        {
                                                          proglets.add(((JCheckBox) c).getText());
                                                        }
                                                      Console.getInstance().clear();
                                                      ProgletsBuilder.build(proglets
                                                                            .toArray(new String[proglets.size()]));
                                                    }
                                                  }
                                                  ).start();
                                     }
                                   }
                                   );
        DialogFrame.jBuilderMenu.add(menuitem);
      } else {
        DialogFrame.jBuilderMenu.add(new JLabel(
                                       "Aucune proglet à construire dans ce répertoire"));
      }
    }
    Component parent = Console.getInstance().getToolBar();
    DialogFrame.jBuilderMenu.show(DialogFrame.jBuilderButton, 0,
                                  parent.getHeight());
  }
  // @ inner-class-variable
  private static JPopupMenu jBuilderMenu = null;

  private static void startConvertisseurHML() {
    JPopupMenu jCreatorMenu = new JPopupMenu();
    jCreatorMenu.add(new Htm2Hml());
    Component parent = Console.getInstance().getToolBar();
    jCreatorMenu.show(DialogFrame.jCreatorButton, 0, parent.getHeight());
  }
}
