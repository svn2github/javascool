package org.javascool.builder;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import org.javascool.tools.FileManager;
import org.javascool.tools.Xml2Xml;

/** Calculette de conversion de HTML en HML. */
public class Htm2Hml extends JPanel {
  private JTextArea htm, hml;
  public Htm2Hml() {
    setLayout(new BorderLayout());
    JToolBar b = new JToolBar();
    b.setFloatable(false);
    b.add(new JLabel("Traduction de [X]HTML en HML (coller le HTML à gauche et copié le HML à droite et cliquer sur le bouton) -> "));
    b.add(new JButton("[Traduire]") {
            {
              addActionListener(new ActionListener() {
                                  private static final long serialVersionUID = 1L;
                                  @Override
                                  public void actionPerformed(ActionEvent e) {
                                    hml.setText(translate(htm.getText()));
                                  }
                                }
                                );
            }
          }
          );
    b.add(Box.createHorizontalGlue());
    add(b, BorderLayout.NORTH);
    JPanel c = new JPanel();
    c.add(new JScrollPane(htm = new JTextArea(40, 64)));
    c.add(new JScrollPane(hml = new JTextArea(40, 64) {
                            private static final long serialVersionUID = 1L;
                            {
                              setBackground(new Color(200, 200, 200));
                              setEditable(false);
                            }
                          }
                          ));
    add(c, BorderLayout.CENTER);
  }
  private static String translate(String htm) {
    String hml = Xml2Xml.html2xhtml(htm);
    try {
      hml = Xml2Xml.run(hml, FileManager.load("org/javascool/builder/htm2hml.xslt"));
    } catch(Exception e) {
      System.out.println("Impossible de traduire le HTML en HML: " + e);
    }
    return hml;
  }
  /** Lanceur de la transformation [X]HTML -&gt; HML.
   * @param usage <tt>java org.javascool.builder.Htm2Hml input-file [output-file]</tt>
   */
  public static void main(String[] usage) {
    // @main
    if(usage.length > 0) {
      FileManager.save(usage.length > 1 ? usage[1] : "stdout:", translate(FileManager.load(usage[0])));
    }
  }
}
