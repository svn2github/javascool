/*******************************************************************************
* Rachid.Benarab@unice.fr, Copyright (C) 2010.  All rights reserved.          *
*******************************************************************************/

package org.javascool;

// Used for the tree data structure
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.util.Enumeration;

// Used to build the panel
import javax.swing.JPanel;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.awt.CardLayout;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

/** This widget defines a re-oriented graohic algorithm editor.
 * @see <a href="doc-files/about-keystrokes.htm">key-strokes (in French)</a>
 * @see <a href="AlgoEditor.java.html">source code</a>
 * @see Jvs2Java
 * @serial exclude
 */
public class AlgoEditor extends JPanel implements Widget, Editor {
  private static final long serialVersionUID = 1L;

  //
  // [1] Builds the button bar
  //
  private JComboBox edit;
  {
    setLayout(new BorderLayout());
    JToolBar bar = new JToolBar("AlgoEditor", JToolBar.VERTICAL);
    bar.setFloatable(false);
    bar.setBorderPainted(false);
    bar.setLayout(new GridLayout(0, 1));

    bar.add(new JButton(new AbstractAction("Déclarer Variable", Utils.getIcon("org/javascool/doc-files/icones16/add.png")) {
                          private static final long serialVersionUID = 1L;
                          public void actionPerformed(ActionEvent e) {
                            doDeclaration();
                          }
                        }
                        ));
    bar.add(new JButton(new AbstractAction("Ajout Instruction", Utils.getIcon("org/javascool/doc-files/icones16/add.png")) {
                          private static final long serialVersionUID = 1L;
                          public void actionPerformed(ActionEvent e) {
                            doInsertion();
                          }
                        }
                        ));
    bar.add(new JButton(new AbstractAction("Utiliser Fonction", Utils.getIcon("org/javascool/doc-files/icones16/add.png")) {
                          private static final long serialVersionUID = 1L;
                          public void actionPerformed(ActionEvent e) {
                            doFunctioncall();
                          }
                        }
                        ));
    bar.addSeparator();
    bar.add(new JButton(new AbstractAction("Modifier élément", Utils.getIcon("org/javascool/doc-files/icones16/edit.png")) {
                          private static final long serialVersionUID = 1L;
                          public void actionPerformed(ActionEvent e) {
                            doModify();
                          }
                        }
                        ));
    bar.add(new JButton(new AbstractAction("Copier élément", Utils.getIcon("org/javascool/doc-files/icones16/copy.png")) {
                          private static final long serialVersionUID = 1L;
                          public void actionPerformed(ActionEvent e) {
                            doCopy();
                          }
                        }
                        ));
    bar.add(new JButton(new AbstractAction("Couper élément", Utils.getIcon("org/javascool/doc-files/icones16/cut.png")) {
                          private static final long serialVersionUID = 1L;
                          public void actionPerformed(ActionEvent e) {
                            doCut();
                          }
                        }
                        ));
    bar.add(new JButton(new AbstractAction("Coller élément", Utils.getIcon("org/javascool/doc-files/icones16/paste.png")) {
                          private static final long serialVersionUID = 1L;
                          public void actionPerformed(ActionEvent e) {
                            doPaste();
                          }
                        }
                        ));
    bar.addSeparator();
    for(int i = 0; i < bar.getComponentCount(); i++)
      if(bar.getComponent(i) instanceof JButton)
        ((JButton) bar.getComponent(i)).setHorizontalAlignment(JButton.LEADING);
    add(bar, BorderLayout.EAST);
  }

  //
  // [2] Builds the tree input
  //
  private JTree tree;
  private DefaultTreeModel model;
  private DefaultMutableTreeNode root;
  {
    root = new DefaultMutableTreeNode();
    model = new DefaultTreeModel(root);
    root.add(new DefaultMutableTreeNode("DEBUT_PROGRAMME"));
    addTrailer((DefaultMutableTreeNode) root.getChildAt(0), "FIN_PROGRAMME");
    tree = new JTree();
    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    renderer.setLeafIcon(null);
    renderer.setOpenIcon(null);
    renderer.setClosedIcon(null);
    tree.setCellRenderer(renderer);
    tree.setModel(model);
    tree.setEditable(true);
    add(new JScrollPane(tree), BorderLayout.CENTER);
  }
  // [2.1] Interface between algo edition and Jtree
  // Gets the current node or the current "unleaf" node on which childreb can be added
  private DefaultMutableTreeNode getCurrentNode(boolean unleaf) {
    node_index = -1;
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    if(unleaf)
      while(node != null && !node.toString().matches(blockPattern)) {
        node_index = node.getParent() != null ? node.getParent().getIndex(node) + 1 : -1;
        node = (DefaultMutableTreeNode) node.getParent();
      }
    if(node == null)
      node = (DefaultMutableTreeNode) root.getChildAt(0);
    return node;
  }
  private static final String blockPattern = "^(DEBUT_PROGRAMME|ALORS|SINON|FAIRE).*$";
  // Gets the current node insertion index of the last getCurrentNode call
  private int getCurrentNodeIndex(DefaultMutableTreeNode where) {
    if((0 <= node_index) && (node_index < where.getChildCount() - 1))
      return node_index;
    return 1 < where.getChildCount() ? where.getChildCount() - 2 : 0;
  }
  private int node_index = -1;
  // Adds a trailer tag
  private void addTrailer(DefaultMutableTreeNode where, String label) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(label);
    node.setAllowsChildren(false);
    where.add(node);
    model.insertNodeInto(node, where, where.getChildCount() - 1);
  }
  /** Returns the tree as a String using the Pml syntax. */
  public String getText() {
    return getText((DefaultMutableTreeNode) root.getChildAt(0), 0).toString();
  }
  private static StringBuffer getText(DefaultMutableTreeNode node, int depth) {
    StringBuffer s = new StringBuffer();
    String what = node.toString().replaceAll("\"", "\\\\\"");
    if(node.isLeaf())
      append(s, depth, "{ \"" + what + "\" }");
    else {
      append(s, depth, "{ \"" + what + "\"");
      for(int c = 0; c < node.getChildCount(); c++)
        s.append(getText((DefaultMutableTreeNode) node.getChildAt(c), depth + 1));
      append(s, depth, "}");
    }
    return s;
  }
  private static void append(StringBuffer s, int tab, String string) {
    for(int d = 0; d < tab; d++)
      s.append(" ");
    s.append(string);
    s.append("\n");
  }
  /** Sets the tree from a String using the Pml syntax.
   * @param string A string with the following the French algorithm syntax: <ul>
   * <li>Constructs: <ul>
   * <li><tt>{ DEBUT_PROGRAMME <i>instructions ..</i> FIN_PROGRAMME }</tt>
   * defines the <tt>void main() { ..}</tt> root construct.</li>
   * <li><tt>{ "SI_ALORS (<i>expression booléenne</i>)" {ALORS <i>instructions ..</i> FIN_ALORS }}</tt>
   * defines the <tt>if(expression) { .. }</tt> conditional construct.</li>
   * <li><tt>{ "SI_ALORS_SINON (<i>expression booléenne</i>)" {ALORS <i>instructions ..</i> FIN_ALORS } {SINON <i>instructions ..</i> FIN_SINON }}</tt>
   * defines the <tt>if(expression) { .. } else { .. }</tt> conditional construct.</li>
   * <li><tt>{ "TANT_QUE (<i>expression booléenne</i>)" {FAIRE <i>instructions ..</i> FIN_FAIRE }}</tt>
   * defines the <tt>while(expression) { .. }</tt> loop construct.</li>
   * </ul><li>Variable affectations: <ul>
   * <li><tt>{ "CHAINE_CARACTERE <i>name</i> &lt;- <i>\"value\"_or_expression</i>;" }</tt>
   * defines a textual <tt>String <i>name</i> = "<i>value</i>"; variable.<li>
   * <li><tt>{ "ENTIER_NATUREL <i>name</i> &lt;- <i>value_or_expression</i>;" }</tt>
   * defines a numerical <tt>int <i>name</i> = <i>value</i>; variable.<li>
   * <li><tt>{ "NOMBRE_DECIMAL <i>name</i> &lt;- <i>value_or_expression</i>;" }</tt>
   * defines a floating-point <tt>double <i>name</i> = <i>value</i>; variable.<li>
   * <li><tt>{ "BOOLEEN <i>name</i> &lt;- <i>value_or_expression</i>;" }</tt>
   * defines a logical value <tt>boolean <i>name</i> = <i>value</i>; variable.<li>
   * <li><tt>{ "DEJA_DECLAREE <i>name</i> &lt;- <i>value_or_expression</i>"; }</tt>
   * redefines the variable value.</li>
   * <li><tt>{ "(CHAINE_DE_CARACTERE|ENTIER_NATUREL|NOMBRE_DECIMAL|BOOLEEN|DEJA_DECLAREE) <i>name</i> &lt;- LIRE_AU_CLAVIER();" }</tt>
   * reads the variable value from the user keyboard.</li>
   * </ul><li>Function call affectations: <ul>
   * <li><tt>{ "AFFICHER (<i>expression</i>);" }</tt>
   * prints the expression value on the user display.</li>
   * <li><tt>{ "TRACE_LIGNE (<i>x1</i>, <i>y1</i>, <i>x1</i>, <i>y1</i>);" }</tt>
   * draws a segment line from the point (<tt>x1, y1</tt>) to  the point (<tt>x2, y2</tt>).</li>
   * <li><tt>{ "TRACE_MOT (<i>x</i>, <i>y</i>, "<i>mot</i>");" }</tt>
   * draws a the string <tt>mot</tt> at the point (<tt>x, y</tt>).</li>
   * </ul></ul>
   */
  public Editor setText(String string) {
    Pml pml = new Pml().reset(string);
    DefaultMutableTreeNode node = setText(pml);
    // -System.out.println("setText = "+getText(node, 0));
    removeAlgo();
    for(int c = 0; node.getChildCount() > 0; c++) {
      DefaultMutableTreeNode n = (DefaultMutableTreeNode) node.getChildAt(0);
      if("FIN_PROGRAMME".equals(n.toString()))
        node.remove(0);
      else
        model.insertNodeInto(n, (DefaultMutableTreeNode) root.getChildAt(0), c);
      modified = false;
    }
    model.nodeChanged((DefaultMutableTreeNode) root.getChildAt(0));
    tree.scrollPathToVisible(new TreePath(root.getChildAt(0)));
    return this;
  }
  private static DefaultMutableTreeNode setText(Pml pml) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(pml.getTag());
    for(int c = 0; c < pml.getCount(); c++)
      node.add(setText(pml.getChild(c)));
    return node;
  }
  public boolean isModified() {
    return modified;
  }
  private boolean modified = false;

  /** Returns the tree as a String using the Java syntax. */
  public String getJavaSource() {
    return Jvs2Java.reformat(getJavaSource(root.getChildCount() > 0 ? (DefaultMutableTreeNode) root.getChildAt(0) : null, 0).toString());
  }
  private StringBuffer getJavaSource(DefaultMutableTreeNode node, int depth) {
    StringBuffer s = new StringBuffer();
    if(node != null) {
      String what = node.toString();
      if("DEBUT_PROGRAMME".equals(what)) {
        append(s, depth, "// DEBUT_PROGRAMME");
        append(s, depth, "void main() {");
        appendChilds(s, node, depth);
        append(s, depth, "}");
      } else if(what.matches(declarationPattern)) {
        append(s, depth, "//" + getJavaSourceComment(node, depth + 1));
        append(s, depth, what.
               replaceFirst("LIRE_AU_CLAVIER", "read" + getType(node, what)).
               replaceFirst("(DEJA_DECLAREE.*)(LIRE_AU_CLAVIER)", "$1IMPOSSIBLE_DE_LIRE_AU_CLAVIER_UNE_CHAINE_DEJA_DECLAREE").
               replaceFirst("CHAINE_DE_CARACTERE", "String").
               replaceFirst("ENTIER_NATUREL", "int").
               replaceFirst("NOMBRE_DECIMAL", "double").
               replaceFirst("BOOLEEN", "boolean").
               replaceFirst("DEJA_DECLAREE", "").
               replaceAll("VRAI", "true").
               replaceAll("FAUX", "false").
               replaceFirst("<-", "=")
               + "\n");
      } else if(what.matches(insertionPattern)) {
        String construct = what.replaceFirst(insertionPattern, "$1");
        String expression = what.replaceFirst(insertionPattern, "$2");
        if("SI_ALORS".equals(construct)) {
          append(s, depth, "// SI_ALORS");
          append(s, depth, "if (" + expression + ") {");
          append(s, depth + 1, "// ALORS");
          appendChilds(s, (DefaultMutableTreeNode) node.getChildAt(0), depth);
          append(s, depth, "}");
        } else if("SI_ALORS_SINON".equals(construct)) {
          append(s, depth, "// SI_ALORS_SINON");
          append(s, depth, "if (" + expression + ") {");
          append(s, depth + 1, "// ALORS");
          appendChilds(s, (DefaultMutableTreeNode) node.getChildAt(0), depth);
          append(s, depth, "} else {");
          append(s, depth + 1, "// SINON");
          appendChilds(s, (DefaultMutableTreeNode) node.getChildAt(1), depth);
          append(s, depth, "}");
        } else if("TANT_QUE".equals(construct)) {
          append(s, depth, "// TANT_QUE");
          append(s, depth, "while (" + expression + ") {");
          append(s, depth + 1, "// FAIRE");
          appendChilds(s, (DefaultMutableTreeNode) node.getChildAt(0), depth);
          append(s, depth, "}");
        }
      } else if(what.matches(functioncallPattern)) {
        append(s, depth, "//" + getJavaSourceComment(node, depth + 1));
        append(s, depth, what.
               replaceFirst("AFFICHER", "println").
               replaceFirst("NOUVEAU_TRACE", "scopeReset").
               replaceFirst("TRACE_LIGNE", "scopeAddLine").
               replaceFirst("TRACE_MOT", "scopeAddString").
               replaceAll("VRAI", "true").
               replaceAll("FAUX", "false")
               + "\n");
      } else
        append(s, depth, "//" + getJavaSourceComment(node, depth + 1));
    }
    return s;
  }
  private static String getJavaSourceComment(DefaultMutableTreeNode node, int depth) {
    return getText(node, depth).toString().replaceAll("[{}]", "").replaceAll("\\s+", " ").replaceAll("([^\\\\])\"", "$1").replaceAll("\\\\\"", "\"");
  }
  private void appendChilds(StringBuffer s, DefaultMutableTreeNode node, int depth) {
    for(int c = 0; c < node.getChildCount(); c++)
      s.append(getJavaSource((DefaultMutableTreeNode) node.getChildAt(c), depth + 1));
  }
  private String getType(DefaultMutableTreeNode node, String what) {
    String name = what.replaceFirst(declarationPattern, "$2");
    int l = node.getLevel();
    for(Enumeration e = root.depthFirstEnumeration(); e.hasMoreElements();) {
      DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.nextElement();
      if(n.getLevel() >= l) {
        String s = n.toString();
        if(s.matches(declarationPattern) && name.equals(s.replaceFirst(declarationPattern, "$2"))) {
          String type = s.replaceFirst(declarationPattern, "$1");
          if(!"DEJA_DECLAREE".equals(type))
            return type.
                   replaceFirst("CHAINE_DE_CARACTERE", "String").
                   replaceFirst("ENTIER_NATUREL", "Int").
                   replaceFirst("NOMBRE_DECIMAL", "Double").
                   replaceFirst("BOOLEEN", "Boolean");
        }
      }
      if(n == node)
        break;
    }
    return "UnknownType";
  }
  //
  // [3] Builds the input dialogs
  //
  private CardLayout dialog = new CardLayout();
  private JPanel Jdialog = new JPanel();
  {
    Jdialog.setLayout(dialog);
  }

  // [3.1] Message implementation
  private JLabel message = new JLabel();
  {
    Jdialog.add(message, "message");
    showMessage();
  }
  private void showMessage(String string) {
    message.setText("    " + string);
    dialog.show(Jdialog, "message");
  }
  private void showMessage() {
    showMessage("Entrer l'algorithme");
  }
  // [3.2] Variable declaration implementation
  private String[] types = { "CHAINE_DE_CARACTERE", "ENTIER_NATUREL", "NOMBRE_DECIMAL", "BOOLEEN", "DEJA_DECLAREE" };
  private JComboBox type = new JComboBox(types);
  private String[] names = { "", "x", "y", "z", "a", "b" };
  private JComboBox name = new JComboBox(names);
  private String[] values = { "", "LIRE_AU_CLAVIER()" };
  private JComboBox value = new JComboBox(values);
  {
    name.setEditable(true);
    value.setEditable(true);
    type.addActionListener(new ActionListener() {
                             private static final long serialVersionUID = 1L;
                             public void actionPerformed(ActionEvent e) {
                               name.setSelectedItem(((String) name.getSelectedItem()).replaceAll("[^A_Za-z0-9]", "_").replaceFirst("^([0-9])", "_$1"));
                             }
                           }
                           );
    type.addActionListener(new ActionListener() {
                             private static final long serialVersionUID = 1L;
                             public void actionPerformed(ActionEvent e) {
                               setDefaultValue();
                             }
                           }
                           );
    JPanel pane = new JPanel();
    pane.setLayout(new GridLayout(2, 4));
    pane.add(new JLabel(" Type de la variable:"));
    pane.add(new JLabel(" Nom de la variable:"));
    pane.add(new JLabel(" Valeur de la variable:"));
    pane.add(new JButton(new AbstractAction("Valider") {
                           private static final long serialVersionUID = 1L;
                           public void actionPerformed(ActionEvent e) {
                             modified = true;
                             setDefaultValue();
                             if(((String) name.getSelectedItem()).length() == 0)
                               name.setSelectedItem("VARIABLE_SANS_NOM");
                             String label = type.getSelectedItem() + " " + name.getSelectedItem() + " <- " + value.getSelectedItem() + ";";
                             for(int l = label.length(); l < 80; l++)
                               label += " ";
                             DefaultMutableTreeNode node = getCurrentNode(false);
                             if(node.toString().matches(declarationPattern))
                               node.setUserObject(label);
                             else {
                               node = new DefaultMutableTreeNode(label);
                               node.setAllowsChildren(false);
                               DefaultMutableTreeNode where = getCurrentNode(true);
                               where.add(node);
                               model.insertNodeInto(node, where, getCurrentNodeIndex(where));
                               model.nodeChanged(where);
                             }
                             tree.scrollPathToVisible(new TreePath(node.getPath()));
                             showMessage();
                           }
                         }
                         ));
    pane.add(type);
    pane.add(name);
    pane.add(value);
    pane.add(new JButton(new AbstractAction("Annuler") {
                           private static final long serialVersionUID = 1L;
                           public void actionPerformed(ActionEvent e) {
                             showMessage();
                           }
                         }
                         ));
    Jdialog.add(pane, "declaration");
  }
  private void doDeclaration() {
    String what = getCurrentNode(false).toString();
    if(what.matches(declarationPattern)) {
      type.setSelectedItem(what.replaceFirst(declarationPattern, "$1"));
      name.setSelectedItem(what.replaceFirst(declarationPattern, "$2"));
      value.setSelectedItem(what.replaceFirst(declarationPattern, "$3"));
    }
    dialog.show(Jdialog, "declaration");
  }
  private void setDefaultValue() {
    if((value.getSelectedItem() == null) || "".equals(value.getSelectedItem()) ||
       ("FAUX".equals(value.getSelectedItem()) && !"BOOLEEN".equals(type.getSelectedItem())) ||
       ("0".equals(value.getSelectedItem()) && !("ENTIER_NATUREL".equals(type.getSelectedItem()) || "NOMBRE_DECIMAL".equals(type.getSelectedItem()))))
    {
      if("BOOLEEN".equals(type.getSelectedItem()))
        value.setSelectedItem("FAUX");
      else if("ENTIER_NATUREL".equals(type.getSelectedItem()) || "NOMBRE_DECIMAL".equals(type.getSelectedItem()))
        value.setSelectedItem("0");
      else if("CHAINE_DE_CARACTERE".equals(type.getSelectedItem()))
        value.setSelectedItem("\"\"");
    }
  }
  private static final String declarationPattern = "^(CHAINE_DE_CARACTERE|ENTIER_NATUREL|NOMBRE_DECIMAL|BOOLEEN|DEJA_DECLAREE) ([^ ]+) <- ([^;]+); *$";

  // [3.3] Instruction insertion implementation
  private String[] constructs = { "SI_ALORS", "SI_ALORS_SINON", "TANT_QUE" };
  private JComboBox construct = new JComboBox(constructs);
  private String[] expressions = { "", "VRAI" };
  private JComboBox expression = new JComboBox(expressions);
  {
    expression.setEditable(true);
    JPanel pane = new JPanel();
    pane.setLayout(new GridLayout(2, 3));
    pane.add(new JLabel(" Choix de l'instruction:"));
    pane.add(new JLabel(" Valeur de l'expression:"));
    pane.add(new JButton(new AbstractAction("Valider") {
                           private static final long serialVersionUID = 1L;
                           public void actionPerformed(ActionEvent e) {
                             doValidate();
                           }
                         }
                         ));
    pane.add(construct);
    pane.add(expression);
    pane.add(new JButton(new AbstractAction("Annuler") {
                           private static final long serialVersionUID = 1L;
                           public void actionPerformed(ActionEvent e) {
                             showMessage();
                           }
                         }
                         ));
    Jdialog.add(pane, "insertion");
  }
  private void doValidate() {
    modified = true;
    if(((String) expression.getSelectedItem()).length() == 0)
      expression.setSelectedItem("VRAI");
    String label = construct.getSelectedItem() + " (" + expression.getSelectedItem() + ")";
    for(int l = label.length(); l < 80; l++)
      label += " ";
    DefaultMutableTreeNode node = getCurrentNode(false);
    if(node.toString().matches(insertionPattern)) {
      node.setUserObject(label);
      ((DefaultMutableTreeNode) node.getChildAt(0)).setUserObject("TANT_QUE".equals(construct.getSelectedItem()) ? "FAIRE" : "ALORS");
      if("SI_ALORS_SINON".equals(construct.getSelectedItem())) {
        if(node.getChildCount() == 1) {
          DefaultMutableTreeNode n = new DefaultMutableTreeNode("SINON");
          node.add(n);
          model.insertNodeInto(n, node, 1);
        }
      } else if(node.getChildCount() == 2)
        model.removeNodeFromParent((DefaultMutableTreeNode) node.getChildAt(1));
    } else {
      node = new DefaultMutableTreeNode(label);
      if("SI_ALORS".equals(construct.getSelectedItem())) {
        node.add(new DefaultMutableTreeNode("ALORS"));
        addTrailer((DefaultMutableTreeNode) node.getChildAt(0), "FIN_ALORS");
      } else if("SI_ALORS_SINON".equals(construct.getSelectedItem())) {
        node.add(new DefaultMutableTreeNode("ALORS"));
        addTrailer((DefaultMutableTreeNode) node.getChildAt(0), "FIN_ALORS");
        node.add(new DefaultMutableTreeNode("SINON"));
        addTrailer((DefaultMutableTreeNode) node.getChildAt(1), "FIN_SINON");
      } else if("TANT_QUE".equals(construct.getSelectedItem())) {
        node.add(new DefaultMutableTreeNode("FAIRE"));
        addTrailer((DefaultMutableTreeNode) node.getChildAt(0), "FIN_FAIRE");
      }
      DefaultMutableTreeNode where = getCurrentNode(true);
      where.add(node);
      model.insertNodeInto(node, where, getCurrentNodeIndex(where));
      model.nodeChanged(where);
    }
    tree.scrollPathToVisible(new TreePath(((DefaultMutableTreeNode) node.getChildAt(0)).getPath()));
    showMessage();
  }
  private void doInsertion() {
    String what = getCurrentNode(false).toString();
    if(what.matches(insertionPattern)) {
      construct.setSelectedItem(what.replaceFirst(insertionPattern, "$1"));
      expression.setSelectedItem(what.replaceFirst(insertionPattern, "$2"));
    }
    dialog.show(Jdialog, "insertion");
  }
  private static final String insertionPattern = "^(SI_ALORS|SI_ALORS_SINON|TANT_QUE) \\((.*)\\) *$";

  // [3.4] Function call insertion implementation
  private String[] functions = { "AFFICHER", "NOUVEAU_TRACE", "TRACE_LIGNE", "TRACE_MOT" };
  private JComboBox function = new JComboBox(functions);
  private String[] arguments = { "", "\"??\"" };
  private JComboBox argument = new JComboBox(arguments);
  {
    argument.setEditable(true);
    JPanel pane = new JPanel();
    pane.setLayout(new GridLayout(2, 3));
    pane.add(new JLabel(" Choix de la fonction:"));
    pane.add(new JLabel(" Valeur de(s) argument(s):"));
    pane.add(new JButton(new AbstractAction("Valider") {
                           private static final long serialVersionUID = 1L;
                           public void actionPerformed(ActionEvent e) {
                             modified = true;
                             if(((String) argument.getSelectedItem()).length() == 0)
                               argument.setSelectedItem("\"???\"");
                             String label = function.getSelectedItem() + " (" + argument.getSelectedItem() + ");";
                             for(int l = label.length(); l < 80; l++)
                               label += " ";
                             DefaultMutableTreeNode node = getCurrentNode(false);
                             if(node.toString().matches(functioncallPattern))
                               node.setUserObject(label);
                             else {
                               node = new DefaultMutableTreeNode(label);
                               node.setAllowsChildren(false);
                               DefaultMutableTreeNode where = getCurrentNode(true);
                               where.add(node);
                               model.insertNodeInto(node, where, getCurrentNodeIndex(where));
                               model.nodeChanged(where);
                             }
                             tree.scrollPathToVisible(new TreePath(node.getPath()));
                             showMessage();
                           }
                         }
                         ));
    pane.add(function);
    pane.add(argument);
    pane.add(new JButton(new AbstractAction("Annuler") {
                           private static final long serialVersionUID = 1L;
                           public void actionPerformed(ActionEvent e) {
                             showMessage();
                           }
                         }
                         ));
    Jdialog.add(pane, "functioncall");
  }
  private void doFunctioncall() {
    String what = getCurrentNode(false).toString();
    if(what.matches(functioncallPattern)) {
      function.setSelectedItem(what.replaceFirst(functioncallPattern, "$1"));
      argument.setSelectedItem(what.replaceFirst(functioncallPattern, "$2"));
    }
    dialog.show(Jdialog, "functioncall");
  }
  private static final String functioncallPattern = "^(AFFICHER|NOUVEAU_TRACE|TRACE_LIGNE|TRACE_MOT) \\(([^;]+)\\); *$";

  // [3.5] Node edition implementation
  {
    JPanel pane = new JPanel();
    pane.setLayout(new GridLayout(1, 2));
    pane.add(new JButton(new AbstractAction("Confirmer la supression de TOUT l'algo.") {
                           private static final long serialVersionUID = 1L;
                           public void actionPerformed(ActionEvent e) {
                             removeAlgo();
                             showMessage();
                           }
                         }
                         ));
    pane.add(new JButton(new AbstractAction("Annuler") {
                           private static final long serialVersionUID = 1L;
                           public void actionPerformed(ActionEvent e) {
                             showMessage();
                           }
                         }
                         ));
    Jdialog.add(pane, "supprimer");
    add(Jdialog, BorderLayout.NORTH);
  }
  private void removeAlgo() {
    while(root.getChildAt(0).getChildCount() > 0)
      model.removeNodeFromParent((DefaultMutableTreeNode) root.getChildAt(0).getChildAt(0));
    addTrailer((DefaultMutableTreeNode) root.getChildAt(0), "FIN_PROGRAMME");
  }
  private void doModify() {
    String what = getCurrentNode(false).toString();
    if(what.matches(declarationPattern))
      doDeclaration();
    else if(what.matches(insertionPattern))
      doInsertion();
    else if(what.matches(functioncallPattern))
      doFunctioncall();
    else
      showMessage("Sélectionner une position avant de modifier");
  }
  private void doCopy() {
    DefaultMutableTreeNode node = getCurrentNode(false);
    clipboard = copy(node);
  }
  private void doCut() {
    modified = true;
    DefaultMutableTreeNode node = getCurrentNode(false);
    if(node == root.getChildAt(0))
      dialog.show(Jdialog, "supprimer");
    else {
      clipboard = node;
      model.removeNodeFromParent(node);
    }
  }
  private void doPaste() {
    modified = true;
    DefaultMutableTreeNode node = getCurrentNode(true);
    if(node == null)
      showMessage("Sélectionner une position avant de coller");
    else {
      DefaultMutableTreeNode n = copy(clipboard);
      node.add(n);
      model.insertNodeInto(n, node, getCurrentNodeIndex(node));
    }
  }
  private DefaultMutableTreeNode copy(DefaultMutableTreeNode node) {
    if(node == null)
      return null;
    DefaultMutableTreeNode copy = new DefaultMutableTreeNode(node.toString());
    for(int c = 0; c < node.getChildCount(); c++)
      copy.add(copy((DefaultMutableTreeNode) node.getChildAt(c)));
    return copy;
  }
  // Defines the key bidding
  {
    tree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK), "copier");
    tree.getActionMap().put("copier", new AbstractAction("copier") {
                              private static final long serialVersionUID = 1L;
                              public void actionPerformed(ActionEvent e) {
                                doCopy();
                              }
                            }
                            );
    tree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK), "couper");
    tree.getActionMap().put("couper", new AbstractAction("couper") {
                              private static final long serialVersionUID = 1L;
                              public void actionPerformed(ActionEvent e) {
                                doCut();
                              }
                            }
                            );
    tree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK), "coller");
    tree.getActionMap().put("coller", new AbstractAction("coller") {
                              private static final long serialVersionUID = 1L;
                              public void actionPerformed(ActionEvent e) {
                                doPaste();
                              }
                            }
                            );
    tree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK), "check");
    /*DEBUG*/ tree.getActionMap().put("check", new AbstractAction("check") {
                                        private static final long serialVersionUID = 1L;
                                        public void actionPerformed(ActionEvent e) {
                                          System.out.println(getText());
                                          System.out.println(getJavaSource());
                                        }
                                      }
                                      );
  }
  private DefaultMutableTreeNode clipboard = null;

  /*DEBUG*/ public static void main(String[] args) {
    String algo =
      "{ DEBUT_PROGRAMME" +
      "  { \"AFFICHER (\\\"Hello World!\\\");\" }" +
      "  { \"AFFICHER (\\\"How do you do ?\\\");\" }" +
      "  { FIN_PROGRAMME }" +
      "}";
    AlgoEditor fen = new AlgoEditor();
    fen.setText(algo);
    Utils.show(fen, "AlgoEditor", 800, 600);
  }
}

