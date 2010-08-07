/*******************************************************************************
 * Rachid.Benarab@unice.fr, Copyright (C) 20109.  All rights reserved.         *
 *******************************************************************************/

package org.javascool;

// Used for the tree data structure
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeModelEvent;

// Used to build the panel
import javax.swing.JPanel;
import java.awt.BorderLayout;
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

public class AlgoTree extends JPanel {
  private static final long serialVersionUID = 1L;
  
  //
  // [1] Builds the button bar
  //
  JComboBox edit;
  {
    setLayout(new BorderLayout());
    JPanel bar = new JPanel();
    bar.add(new JButton(new AbstractAction("Declarer-Variable") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  doDeclaration();
	}}));
    bar.add(new JButton(new AbstractAction("Ajouter-Instruction") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) {
	  doInsertion();
	}}));
    bar.add(new JButton(new AbstractAction("Utiliser-Fonction") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) {
	  doFunctioncall();
	}}));
    edit = new JComboBox(new String[] {"Edit", "- Copier", "- Couper", "- Coller"});
    edit.addActionListener(new ActionListener() {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) {
	  doEdit();
	}});
    bar.add(edit);
    add(bar, BorderLayout.NORTH);
  }

  //
  // [2] Builds the tree input
  //
  private JTree tree;
  private DefaultTreeModel model;
  private DefaultMutableTreeNode root;
  {
    root = new DefaultMutableTreeNode();
    root.add(new DefaultMutableTreeNode("DEBUT DU PROGRAMME"));
    tree = new JTree();
    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    renderer.setLeafIcon(null);
    renderer.setOpenIcon(null);
    renderer.setClosedIcon(null);
    tree.setCellRenderer(renderer);
    model = new DefaultTreeModel(root);
    tree.setModel(model);
    tree.setEditable(true);
    tree.getModel().addTreeModelListener(new TreeModelListener() {
	public void treeNodesChanged(TreeModelEvent e) {
	  System.out.println("Un noeud a été changé !" + getJavaTree());				
	}	
	public void treeNodesInserted(TreeModelEvent event) {
	  System.out.println("Un noeud a été inséré !" + getJavaTree());				
	}
	public void treeNodesRemoved(TreeModelEvent event) {
	  System.out.println("Un noeud a été retiré !" + getJavaTree());
	}
	public void treeStructureChanged(TreeModelEvent event) {
	  System.out.println("La structure d'un noeud a changé !" + getJavaTree());
	}
      });
    add(new JScrollPane(tree), BorderLayout.CENTER);
  }
  // Gets the current node or the current "unleaf" node on which childreb can be added
  private DefaultMutableTreeNode getCurrentNode(boolean unleaf) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    if (unleaf) while(node != null && (!node.getAllowsChildren() || node.toString().matches(insertionPattern))) node = (DefaultMutableTreeNode) node.getParent();
    if (node == null) node = (DefaultMutableTreeNode) root.getChildAt(0);
    return node;
  }

  /** Returns the tree as a String using the Pml syntax. */
  public String getTree() {
    return getTree((DefaultMutableTreeNode) root.getChildAt(0), 0).toString();
  }
  private static StringBuffer getTree(DefaultMutableTreeNode node, int depth) {
    StringBuffer s = new StringBuffer(); 
    if (node.isLeaf()) {
      append(s, depth, "{ \""+node+"\" }");
    } else {
      append(s, depth, "{ \""+node+"\"");
      for(int c = 0; c < node.getChildCount(); c++) s.append(getTree((DefaultMutableTreeNode) node.getChildAt(c), depth+1));
      append(s, depth, "}");
    }
    return s;
  }
  private static void append(StringBuffer s, int tab, String string) {
    for(int d = 0; d < tab; d++) s.append(" "); s.append(string); s.append("\n");
  }

  /** Returns the tree as a String using the Java syntax. */
  public String getJavaTree() {
    return getJavaTree((DefaultMutableTreeNode) root.getChildAt(0), 0).toString();
  }
  private static StringBuffer getJavaTree(DefaultMutableTreeNode node, int depth) {
    StringBuffer s = new StringBuffer(); String what = node.toString();
    if ("DEBUT DU PROGRAMME".equals(what)) {
      append(s, depth, "void main() {");
      appendChilds(s, node, depth);
      append(s, depth, "}");
    } else if (what.matches(declarationPattern)) {
      append(s, depth, what.
	     replaceFirst("CHAINE_DE_CARACTERE", "String").
	     replaceFirst("ENTIER_NATUREL", "int").
	     replaceFirst("NOMBRE_DECIMAL", "double").
	     replaceFirst("BOOLEEN", "boolean").
	     replaceFirst("DEJA_DECLAREE", "").
	     replaceFirst("<-", "=")+"\n");
    } else if (what.matches(insertionPattern)) {
      String construct =  what.replaceFirst(insertionPattern, "$1");
      String expression = what.replaceFirst(insertionPattern, "$2");
      if ("SI_ALORS".equals(construct)) {
	append(s, depth, "if ("+expression+") {");
	appendChilds(s, (DefaultMutableTreeNode) node.getChildAt(0), depth);
	append(s, depth, "}");
      } else if ("SI_ALORS_SINON".equals(construct)) {
	append(s, depth, "if ("+expression+") {");
	appendChilds(s, (DefaultMutableTreeNode) node.getChildAt(0), depth);
	append(s, depth, "} else {");
	appendChilds(s, (DefaultMutableTreeNode) node.getChildAt(1), depth);
	append(s, depth, "}");
      } else if ("TANT_QUE".equals(construct)) {
	append(s, depth, "while ("+expression+") {");
	appendChilds(s, (DefaultMutableTreeNode) node.getChildAt(0), depth);
	append(s, depth, "}");
      }
    } else if (what.matches(functioncallPattern)) {
      append(s, depth, what);
    } else {
      append(s, depth, "/*");
      s.append(getTree(node, depth+1));
      append(s, depth, "*/");
    }
    return s;
  }
  private static void appendChilds(StringBuffer s, DefaultMutableTreeNode node, int depth) {
    for(int c = 0; c < node.getChildCount(); c++) s.append(getJavaTree((DefaultMutableTreeNode) node.getChildAt(c), depth+1));
  }

  /** Sets the tree from a String using the Pml syntax. */
  public void setTree(String string) {
    Pml pml = new Pml().reset(string); root.removeAllChildren(); root.add(setTree(pml));
    
  }
  private static DefaultMutableTreeNode setTree(Pml pml) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(pml.getTag());
    for(int c = 0; c < pml.getCount(); c++) node.add(setTree(pml.getChild(c)));
    return node;
  }
  
  //
  // [3] Builds the input dialogs
  //
  CardLayout dialog = new CardLayout(); JPanel Jdialog = new JPanel(); 
  { 
    Jdialog.setLayout(dialog);
  }

  // [3.1] Message implementation
  JLabel message = new JLabel();
  {
    Jdialog.add(message); dialog.addLayoutComponent(message, "message"); showMessage();
  }
  private void showMessage(String string) {
    message.setText("    "+string); dialog.show(Jdialog, "message");
  }
  private void showMessage() {
    showMessage("Entrer l'algorithme");
  }

  // [3.2] Variable declaration implementation
  String[] types = {"CHAINE_DE_CARACTERE", "ENTIER_NATUREL", "NOMBRE_DECIMAL", "BOOLEEN", "DEJA_DECLAREE"}; JComboBox type = new JComboBox(types); 
  String[] names = {"", "x", "y", "z", "a", "b"}; JComboBox name = new JComboBox(names); 
  String[] values = {"", "LIRE_AU_CLAVIER()"}; JComboBox value = new JComboBox(values); 
  {
    name.setEditable(true);
    value.setEditable(true);
    type.addActionListener(new ActionListener() {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  setDefaultValue();
	}
      });
    JPanel pane = new JPanel(); pane.setLayout(new GridLayout(2, 4));
    pane.add(new JLabel(" Type de la variable:"));
    pane.add(new JLabel(" Nom de la variable:"));
    pane.add(new JLabel(" Valeur de la variable:"));
    pane.add(new JButton(new AbstractAction("Valider") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  setDefaultValue(); if (((String) name.getSelectedItem()).length() == 0) name.setSelectedItem("VARIABLE_SANS_NOM");
	  String label = type.getSelectedItem()+" "+name.getSelectedItem()+" <- "+value.getSelectedItem()+";";
	  for(int l = label.length(); l < 80; l++) label += " ";
	  DefaultMutableTreeNode node = getCurrentNode(false);
	  if (node.toString().matches(declarationPattern)) {
	    node.setUserObject(label);
	  } else {
	    node = new DefaultMutableTreeNode(label);
	    node.setAllowsChildren(false);
	    DefaultMutableTreeNode where = getCurrentNode(true);
	    where.add(node);														
	    model.insertNodeInto(node, where, where.getChildCount() - 1);
	    model.nodeChanged(where);			
	  }
	  tree.scrollPathToVisible(new TreePath(node.getPath()));
	  showMessage();
	}
      }));
    pane.add(type);
    pane.add(name);
    pane.add(value);
    pane.add(new JButton(new AbstractAction("Annuler") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  showMessage();
	}
      }));
    Jdialog.add(pane); dialog.addLayoutComponent(pane, "declaration");
  }  
  private void doDeclaration() {
    String what = getCurrentNode(false).toString();
    if (what.matches(declarationPattern)) {
      type.setSelectedItem(what.replaceFirst(declarationPattern, "$1"));
      name.setSelectedItem(what.replaceFirst(declarationPattern, "$2"));
      value.setSelectedItem(what.replaceFirst(declarationPattern, "$3"));
    }
    dialog.show(Jdialog, "declaration");
  }
  private void setDefaultValue() {
    if (value.getSelectedItem() == null || "".equals(value.getSelectedItem()) || 
	("FAUX".equals(value.getSelectedItem()) && !"BOOLEEN".equals(type.getSelectedItem())) ||
	("0".equals(value.getSelectedItem()) && !("ENTIER_NATUREL".equals(type.getSelectedItem()) || "NOMBRE_DECIMAL".equals(type.getSelectedItem())))) {
      if ("BOOLEEN".equals(type.getSelectedItem())) value.setSelectedItem("FAUX");
      else if ("ENTIER_NATUREL".equals(type.getSelectedItem()) || "NOMBRE_DECIMAL".equals(type.getSelectedItem())) value.setSelectedItem("0");
      else if ("CHAINE_DE_CARACTERE".equals(type.getSelectedItem())) value.setSelectedItem("\"\"");
    }
  }
  private static final String declarationPattern = "^(CHAINE_DE_CARACTERE|ENTIER_NATUREL|NOMBRE_DECIMAL|BOOLEEN|DEJA_DECLAREE) ([^ ]+) <- ([^;]+); *$";

  // [3.3] Instruction insertion implementation
  String[] constructs = {"SI_ALORS", "SI_ALORS_SINON", "TANT_QUE"}; JComboBox construct = new JComboBox(constructs); 
  String[] expressions = {"", "VRAI"}; JComboBox expression = new JComboBox(expressions); 
  {
    expression.setEditable(true);
    JPanel pane = new JPanel(); pane.setLayout(new GridLayout(2, 3));
    pane.add(new JLabel(" Choix de l'instruction:"));
    pane.add(new JLabel(" Valeur de la variable:"));
    pane.add(new JButton(new AbstractAction("Valider") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  if (((String) expression.getSelectedItem()).length() == 0) expression.setSelectedItem("VRAI");
	  String label = construct.getSelectedItem()+" ("+expression.getSelectedItem()+")";
	  for(int l = label.length(); l < 80; l++) label += " ";
	  DefaultMutableTreeNode node = getCurrentNode(false);
	  if (node.toString().matches(insertionPattern)) {
	    node.setUserObject(label);
	    ((DefaultMutableTreeNode) node.getChildAt(0)).setUserObject("TANT_QUE".equals(construct.getSelectedItem()) ? "FAIRE" : "ALORS");
	    if ("SI_ALORS_SINON".equals(construct.getSelectedItem())) {
	      if (node.getChildCount() == 1) {
		DefaultMutableTreeNode n = new DefaultMutableTreeNode("SINON");	    
		node.add(n);														
		model.insertNodeInto(n, node, 1);
	      }
	    } else {
	      if (node.getChildCount() == 2) {
		model.removeNodeFromParent((DefaultMutableTreeNode) node.getChildAt(1));
	      }
	    }
	  } else {
	    node = new DefaultMutableTreeNode(label);
	    if ("SI_ALORS".equals(construct.getSelectedItem())) {
	      node.add(new DefaultMutableTreeNode("ALORS"));
	    } else if ("SI_ALORS_SINON".equals(construct.getSelectedItem())) {
	      node.add(new DefaultMutableTreeNode("ALORS"));
	      node.add(new DefaultMutableTreeNode("SINON"));
	    } else if ("TANT_QUE".equals(construct.getSelectedItem())) {
	      node.add(new DefaultMutableTreeNode("FAIRE"));
	    } 
	    DefaultMutableTreeNode where = getCurrentNode(true);
	    where.add(node);														
	    model.insertNodeInto(node, where, where.getChildCount() - 1);
	    model.nodeChanged(where);			
	  }
	  tree.scrollPathToVisible(new TreePath(((DefaultMutableTreeNode) node.getChildAt(0)).getPath()));
	  showMessage();
	}
      }));
    pane.add(construct);
    pane.add(expression);
    pane.add(new JButton(new AbstractAction("Annuler") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  showMessage();
	}
      }));
    Jdialog.add(pane); dialog.addLayoutComponent(pane, "insertion");
  }
  private void doInsertion() {
    String what = getCurrentNode(false).toString();
    if (what.matches(insertionPattern)) {
      construct.setSelectedItem(what.replaceFirst(insertionPattern, "$1"));
      expression.setSelectedItem(what.replaceFirst(insertionPattern, "$2"));
    }
    dialog.show(Jdialog, "insertion");
  }
  private static final String insertionPattern = "^(SI_ALORS|SI_ALORS_SINON|TANT_QUE) \\((.*)\\) *$";


  // [3.4] Function call insertion implementation
  String[] functions = {"AFFICHER", "NOUVEAU_TRACE", "TRACE_LIGNE", "TRACE_MOT"}; JComboBox function = new JComboBox(functions); 
  String[] arguments = {"", "\"??\""}; JComboBox argument = new JComboBox(arguments); 
  {
    argument.setEditable(true);
    JPanel pane = new JPanel(); pane.setLayout(new GridLayout(2, 3));
    pane.add(new JLabel(" Choix de la fonction:"));
    pane.add(new JLabel(" Valeur de(s) argument(s):"));
    pane.add(new JButton(new AbstractAction("Valider") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  if (((String) argument.getSelectedItem()).length() == 0) argument.setSelectedItem("\"???\"");
	  String label = function.getSelectedItem()+" ("+argument.getSelectedItem()+");";
	  for(int l = label.length(); l < 80; l++) label += " ";
	  DefaultMutableTreeNode node = getCurrentNode(false);
	  if (node.toString().matches(functioncallPattern)) {
	    node.setUserObject(label);
	  } else {
	    node = new DefaultMutableTreeNode(label);
	    node.setAllowsChildren(false);
	    DefaultMutableTreeNode where = getCurrentNode(true);
	    where.add(node);														
	    model.insertNodeInto(node, where, where.getChildCount() - 1);
	    model.nodeChanged(where);			
	  }
	  tree.scrollPathToVisible(new TreePath(node.getPath()));
	  showMessage();
	}
      }));
    pane.add(function);
    pane.add(argument);
    pane.add(new JButton(new AbstractAction("Annuler") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  showMessage();
	}
      }));
    Jdialog.add(pane); dialog.addLayoutComponent(pane, "functioncall");
  }
  private void doFunctioncall() {
    String what = getCurrentNode(false).toString();
    if (what.matches(functioncallPattern)) {
      function.setSelectedItem(what.replaceFirst(functioncallPattern, "$1"));
      argument.setSelectedItem(what.replaceFirst(functioncallPattern, "$2"));
    }
    dialog.show(Jdialog, "functioncall");
  }
  private static final String functioncallPattern = "^(AFFICHER|NOUVEAU_TRACE|TRACE_LIGNE|TRACE_MOT) \\(([^ ]+)\\); *$";

  // [3.5] Node edition implementation
  {
    JPanel pane = new JPanel(); pane.setLayout(new GridLayout(1, 2));
    pane.add(new JButton(new AbstractAction("Confirmer la supression de TOUT l'algo.") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  while(root.getChildAt(0).getChildCount() > 0)
	    model.removeNodeFromParent((DefaultMutableTreeNode) root.getChildAt(0).getChildAt(0));
	  showMessage();
	}
      }));
    pane.add(new JButton(new AbstractAction("Annuler") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  showMessage();
	}
      }));
    Jdialog.add(pane); dialog.addLayoutComponent(pane, "supprimer");
    add(Jdialog, BorderLayout.SOUTH);
  }
  private void doEdit() {
    String action = (String) edit.getSelectedItem();
    System.out.println(action);
    if ("Coller".equals(action)) {
      DefaultMutableTreeNode node = getCurrentNode(false);
      clipboard = copy(node);
    } else if ("Couper".equals(action)) {
      DefaultMutableTreeNode node = getCurrentNode(false);
      if (node == root.getChildAt(0)) {
	dialog.show(Jdialog, "supprimer");
      } else {
	clipboard = node;
	model.removeNodeFromParent(node);
      }
    } else if ("Coller".equals(action)) {
      DefaultMutableTreeNode node = getCurrentNode(false);
      if (node == null) {
	showMessage("Selectionner un position avant de coller");
      } else {
	DefaultMutableTreeNode n = copy(clipboard);
	node.add(n);									
	model.insertNodeInto(n, node, node.getChildCount() - 1);
      }
    }
    edit.setSelectedItem("Edit");
  }
  private DefaultMutableTreeNode copy(DefaultMutableTreeNode node) {
    if (node == null) return null;
    DefaultMutableTreeNode copy = new DefaultMutableTreeNode(node.toString());
    for(int c = 0; c < node.getChildCount(); c++)
      copy.add(copy((DefaultMutableTreeNode) node.getChildAt(c)));
    return copy;
  }
  private DefaultMutableTreeNode clipboard = null;

  /** Used to test the interface. */
  public static void main(String[] args){
    AlgoTree fen = new AlgoTree(); Utils.show(fen, "AlgoTree", 800, 600);
  }
}





