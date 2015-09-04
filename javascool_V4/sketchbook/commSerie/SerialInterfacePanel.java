package org.javascool.proglets.commSerie;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/** Définit un panneau graphique permettant de piloter une interface série.
 * <p><img src="http://javascool.gforge.inria.fr/documents/sketchbook/commSerie/screenshot.png" alt="screenshot"></p>
 * @see <a href="SerialInterfacePanel.java.html">source code</a>
 * @serial exclude
 */
public class SerialInterfacePanel extends JPanel {
  private static final long serialVersionUID = 1L;

  /** Construit un panneau de contôle pour l'interface série donné.
   * @param serialInterface Interface série à piloter. Si null, crée une interface série.
   * @param displayMode Précise si:<ul>
   *  <li>"C" : le panneau de contrôle des paramètres et d'ouverture/fermeture du port est affiché</li>
   *  <li>"D" : le panneau de dialogue entrée/sortie avec le port est affiché</li>
   *  <li>"CD" : les deux panneaux sont affichés (défaut)</li>
   *  <li>"" : rien n'est affiché.</li>
   * </ul>
   */
  @SuppressWarnings("unchecked") public SerialInterfacePanel(SerialInterface serialInterface, String displayMode) {
    serial = serialInterface == null ? new SerialInterface() : serialInterface;
    setBorder(BorderFactory.createTitledBorder("Interface de contrôle d'un port série"));
    setLayout(new BorderLayout());
    if(0 <= displayMode.indexOf("C")) {
      add(new JPanel() {
            private static final long serialVersionUID = 1L;
            {
              add(new JComboBox/*!<String>*/(SerialInterface.getPortNames()) {
                    private static final long serialVersionUID = 1L;
                    {
                      setBorder(BorderFactory.createTitledBorder("Nom du port"));
                      setPreferredSize(new Dimension(100, 70));
                      addActionListener(new ActionListener() {
                                          private static final long serialVersionUID = 1L;
                                          @Override
                                          public void actionPerformed(ActionEvent e) {
                                            serial.setName((String) ((JComboBox) e.getSource()).getSelectedItem());
                                          }
                                        }
                                        );
                    }
                  }
                  );
              add(new JComboBox/*!<Integer>*/(new Integer[] { 19200, 9600, 4800, 2400, 1200, 600, 300 }) {
                    private static final long serialVersionUID = 1L;
                    {
                      setBorder(BorderFactory.createTitledBorder("Débit en b./s."));
                      setPreferredSize(new Dimension(100, 70));
                      addActionListener(new ActionListener() {
                                          private static final long serialVersionUID = 1L;
                                          @Override
                                          public void actionPerformed(ActionEvent e) {
                                            serial.setRate((Integer) ((JComboBox) e.getSource()).getSelectedItem());
                                          }
                                        }
                                        );
                    }
                  }
                  );
              add(new JComboBox/*!<String>*/(new String[] { "aucun", "pair", "impair" }) {
                    private static final long serialVersionUID = 1L;
                    {
                      setBorder(BorderFactory.createTitledBorder("Bit de parité"));
                      setPreferredSize(new Dimension(100, 70));
                      addActionListener(new ActionListener() {
                                          private static final long serialVersionUID = 1L;
                                          @Override
                                          public void actionPerformed(ActionEvent e) {
                                            String v = (String) ((JComboBox) e.getSource()).getSelectedItem();
                                            serial.setParity("pair".equals(v) ? 'E' : "impair".equals(v) ? 'O' : 'N');
                                          }
                                        }
                                        );
                    }
                  }
                  );
              add(new JComboBox/*!<Integer>*/(new Integer[] { 8, 7 }) {
                    private static final long serialVersionUID = 1L;
                    {
                      setBorder(BorderFactory.createTitledBorder("Taille du mot"));
                      setPreferredSize(new Dimension(100, 70));
                      addActionListener(new ActionListener() {
                                          private static final long serialVersionUID = 1L;
                                          @Override
                                          public void actionPerformed(ActionEvent e) {
                                            serial.setSize((Integer) ((JComboBox) e.getSource()).getSelectedItem());
                                          }
                                        }
                                        );
                    }
                  }
                  );
              add(new JComboBox/*!<Double>*/(new Double[] { 1.0, 1.5, 2.0 }) {
                    private static final long serialVersionUID = 1L;
                    {
                      setBorder(BorderFactory.createTitledBorder("Bits de stop"));
                      setPreferredSize(new Dimension(100, 70));
                      addActionListener(new ActionListener() {
                                          private static final long serialVersionUID = 1L;
                                          @Override
                                          public void actionPerformed(ActionEvent e) {
                                            serial.setStop((Double) ((JComboBox) e.getSource()).getSelectedItem());
                                          }
                                        }
                                        );
                    }
                  }
                  );
              add(new JButton() {
                    private static final long serialVersionUID = 1L;
                    private static final String open = "OUVRIR", close = "FERMER";
                    {
                      setBorder(BorderFactory.createTitledBorder("O/F le port"));
                      setPreferredSize(new Dimension(100, 70));
                      setText(open);
                      addActionListener(new ActionListener() {
                                          private static final long serialVersionUID = 1L;
                                          @Override
                                          public void actionPerformed(ActionEvent e) {
                                            JButton b = (JButton) e.getSource();
                                            if(open.equals(b.getText())) {
                                              b.setText(close);
                                              System.out.println("Opening serial interface : " + serial);
                                              serial.open();
                                            } else {
                                              b.setText(open);
                                              serial.close();
                                            }
                                          }
                                        }
                                        );
                    }
                  }
                  );
            }
          }, BorderLayout.NORTH);
    }
    if(0 <= displayMode.indexOf("D")) {
      add(new Box(BoxLayout.X_AXIS) {
            private static final long serialVersionUID = 1L;
            {
              add(new Box(BoxLayout.Y_AXIS) {
                    private static final long serialVersionUID = 1L;
                    {
                      add(new JScrollPane(writeChar = new JTextArea(1, 8) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                              addKeyListener(new KeyListener() {
                                                               private static final long serialVersionUID = 1L;
                                                               @Override
                                                               public void keyPressed(KeyEvent e) {}
                                                               public void keyReleased(KeyEvent e) {}
                                                               public void keyTyped(KeyEvent e) {
                                                                 char c = e.getKeyChar();
                                                                 external = false;
                                                                 serial.write(c);
                                                                 external = true;
                                                               }
                                                             }
                                                             );
                                            }
                                          }, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS) {
                            private static final long serialVersionUID = 1L;
                            {
                              setBorder(BorderFactory.createTitledBorder("Envoyer un caractère :"));
                            }
                          }
                          );
                      add(new JScrollPane(writeHexa = new JTextArea(1, 8) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                              setBackground(new Color(200, 200, 200));
                                              setEditable(false);
                                            }
                                          }, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS) {
                            private static final long serialVersionUID = 1L;
                            {
                              setBorder(BorderFactory.createTitledBorder("Code ASCII du caractère :"));
                            }
                          }
                          );
                      add(new JButton("Effacer") {
                            private static final long serialVersionUID = 1L;
                            {
                              addActionListener(new ActionListener() {
                                                  private static final long serialVersionUID = 1L;
                                                  @Override
                                                  public void actionPerformed(ActionEvent e) {
                                                    writeChar.setText("");
                                                    writeHexa.setText("");
                                                  }
                                                }
                                                );
                            }
                          }
                          );
                    }
                  }
                  );
              add(new Box(BoxLayout.Y_AXIS) {
                    private static final long serialVersionUID = 1L;
                    {
                      add(new JScrollPane(readChar = new JTextArea(1, 8) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                              setBackground(new Color(200, 200, 200));
                                              setEditable(false);
                                            }
                                          }, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS) {
                            private static final long serialVersionUID = 1L;
                            {
                              setBorder(BorderFactory.createTitledBorder("Caractère reçu :"));
                            }
                          }
                          );
                      add(new JScrollPane(readHexa = new JTextArea(1, 8) {
                                            private static final long serialVersionUID = 1L;
                                            {
                                              setBackground(new Color(200, 200, 200));
                                              setEditable(false);
                                            }
                                          }, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS) {
                            private static final long serialVersionUID = 1L;
                            {
                              setBorder(BorderFactory.createTitledBorder("Code ASCII du caractère :"));
                            }
                          }
                          );
                      add(new JButton("Effacer") {
                            private static final long serialVersionUID = 1L;
                            {
                              addActionListener(new ActionListener() {
                                                  private static final long serialVersionUID = 1L;
                                                  @Override
                                                  public void actionPerformed(ActionEvent e) {
                                                    readChar.setText("");
                                                    readHexa.setText("");
                                                  }
                                                }
                                                );
                            }
                          }
                          );
                    }
                  }
                  );
            }
          }, BorderLayout.CENTER);
      serial.setReader(new SerialInterface.Reader() {
                         public void reading(int c) {
                           readChar.setText(readChar.getText() + ((char) c));
                           readHexa.setText(readHexa.getText() + " " + Integer.toString(c, 16));
                         }
                       }
                       );
      serial.setWriter(new SerialInterface.Writer() {
                         public void writing(int c) {
                           if(external) {
                             writeChar.setText(writeChar.getText() + ((char) c));
                           }
                           writeHexa.setText(writeHexa.getText() + " " + Integer.toString(c, 16));
                         }
                       }
                       );
    }
    // Permet d'afficher les messages de la console dans l'interface.
    {
      JPanel c = org.javascool.widgets.Console.newInstance();
      c.setPreferredSize(new Dimension(600, 200));
      add(c, BorderLayout.SOUTH);
    }
  }
  /**
   * @see #SerialInterfacePanel(SerialInterface, String)
   */
  public SerialInterfacePanel(SerialInterface serial) {
    this(serial, "CD");
  }
  /**
   * @see #SerialInterfacePanel(SerialInterface, String)
   */
  public SerialInterfacePanel(String displayMode) {
    this(null, displayMode);
  }
  /**
   * @see #SerialInterfacePanel(SerialInterface, String)
   */
  public SerialInterfacePanel() {
    this(null, "CD");
  }
  private SerialInterface serial;
  private JTextArea writeChar, writeHexa, readChar, readHexa;
  private boolean external = true;

  /** Renvoie l'interface série, pour pouvoir accéder à ses fonctions. */
  public SerialInterface getSerialInterface() {
    return serial;
  }
  /** Renvoie la liste des des noms de ports séries disponibles ce qui teste l'installation des librairies.
   * @param usage <tt>java -cp javascool-proglets.jar org.javascool.proglets.commSerie.SerialInterfacePanel</tt>
   */
  public static void main(String[] usage) {
    new org.javascool.widgets.MainFrame().reset("Interface série", 800, 600, new SerialInterfacePanel());
  }
}

