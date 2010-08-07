/*******************************************************************************
 * Rachid.Benarab@unice.fr, Copyright (C) 20109.  All rights reserved.         *
 *******************************************************************************/

package org.javascool;

// Used for the tree data structure
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeModelEvent;

// Used to build the panel
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.awt.CardLayout;

public class AlgoTree extends JPanel {
  private static final long serialVersionUID = 1L;
  
  // Builds the button bar
  {
    setLayout(new BorderLayout());
    JPanel bar = new JPanel();
    bar.add(new JButton(new AbstractAction("Declarer-Variable") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  if(tree.getLastSelectedPathComponent() == null) {
	    showMessage("Sélectionner une position dans le PROGRAMME");
	  } else {
	    dialog.show(Jdialog, "declarer");
	  }
	}
      }));
    bar.add(new JButton(new AbstractAction("Ajouter-Instruction") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) {
	  if(tree.getLastSelectedPathComponent() == null) {
	    showMessage("Sélectionner une position dans le PROGRAMME");
	  } else {
	    dialog.show(Jdialog, "ajouter");
	  }
	}
      }));
    bar.add(new JButton(new AbstractAction("Utiliser-Fonction") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) {
	  if(tree.getLastSelectedPathComponent() == null) {
	    showMessage("Sélectionner une position dans le PROGRAMME");
	  } else {
	    dialog.show(Jdialog, "utiliser");
	  }
	}
      }));
    bar.add(new JButton(new AbstractAction("Supprimer") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) {
	  if(tree.getLastSelectedPathComponent() == null) {
	    showMessage("Sélectionner une position dans le PROGRAMME");
	  } else {
	    dialog.show(Jdialog, "supprimer");
	  }
	}
      }));
    add(bar, BorderLayout.NORTH);
  }

  // Builds the tree input
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
    //tree.setRootVisible(false);
    tree.setEditable(true);
    tree.getModel().addTreeModelListener(new TreeModelListener() {
	public void treeNodesChanged(TreeModelEvent e) {
	  System.out.println("Un noeud a été changé !");				
	}	
	public void treeNodesInserted(TreeModelEvent event) {
	  System.out.println("Un noeud a été inséré !");				
	}
	public void treeNodesRemoved(TreeModelEvent event) {
	  System.out.println("Un noeud a été retiré !");
	}
	public void treeStructureChanged(TreeModelEvent event) {
	  System.out.println("La structure d'un noeud a changé !");
	}
      });
    add(new JScrollPane(tree), BorderLayout.CENTER);
  }

  /** Returns the tree as a String using the Pml syntax. */
  public String getTree() {
    return getTree(root, 0).toString();
  }
  private static StringBuffer getTree(DefaultMutableTreeNode node, int depth) {
    StringBuffer s = new StringBuffer(); 
    if (node.isLeaf()) {
      for(int d = 0; d < depth; d++) s.append(" "); s.append("{ \""+node+"\" }\n");
    } else {
      for(int d = 0; d < depth; d++) s.append(" "); s.append("{ \""+node+"\"\n");
      for(int c = 0; c < node.getChildCount(); c++) s.append(getTree((DefaultMutableTreeNode) node.getChildAt(c), depth+1));
      for(int d = 0; d < depth; d++) s.append(" "); s.append("}\n");
    }
    return s;
  }

  /** Sets the tree from a String using the Pml syntax. */
  public void setTree(String string) {
    Pml pml = new Pml().reset(string);
    tree.setModel(model = new DefaultTreeModel(root = setTree(pml)));
  }
  private static DefaultMutableTreeNode setTree(Pml pml) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(pml.getTag());
    for(int c = 0; c < pml.getCount(); c++) node.add(setTree(pml.getChild(c)));
    return node;
  }

  // Builds the input dialogs
  CardLayout dialog = new CardLayout(); JPanel Jdialog = new JPanel(); 
  { 
    Jdialog.setLayout(dialog);
  }
  // Message card
  JLabel message = new JLabel("Entrer votre algorithme");
  {
    Jdialog.add(message); dialog.addLayoutComponent(message, "message");
  }
  private void showMessage(String string) {
    message.setText("    "+string); dialog.show(Jdialog, "message");
  }
  // Variable declaration card
  String[] types = {"CHAINE_DE_CARACTERE", "ENTIER_NATUREL", "NOMBRE_DECIMAL"}; JComboBox type = new JComboBox(types); 
  JTextField name = new JTextField(12), value = new JTextField(32); 
  {
    JPanel pane = new JPanel(); pane.setLayout(new GridLayout(2, 4));
    pane.add(new JLabel(" Type de la variable:"));
    pane.add(new JLabel(" Nom de la variable:"));
    pane.add(new JLabel(" Valeur de la variable:"));
    pane.add(new JButton(new AbstractAction("Déclarer") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  String label = "{ "+type.getSelectedItem()+" "+name.getText()+" PREND_LA_VALEUR "+value.getText()+"}";
	  DefaultMutableTreeNode node = new DefaultMutableTreeNode(label);
	  DefaultMutableTreeNode where =  (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
	  where.add(node);														
	  model.insertNodeInto(node, where, where.getChildCount() - 1);
	  model.nodeChanged(where);				
	}
      }));
    pane.add(type);
    pane.add(name);
    pane.add(value);
    pane.add(new JButton(new AbstractAction("Annuler") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  showMessage("Entrer votre algorithme");
	}
      }));
    Jdialog.add(pane); dialog.addLayoutComponent(pane, "declarer");
  }

  {
    add(Jdialog, BorderLayout.SOUTH);
  }

	
  public static void main(String[] args){
    AlgoTree fen = new AlgoTree(); Utils.show(fen, "AlgoTree", 800, 600);
  }
}





