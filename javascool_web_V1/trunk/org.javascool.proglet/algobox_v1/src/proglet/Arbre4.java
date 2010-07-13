package proglet;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Arbre4 extends JFrame {

	private JTree arbre;
	private DefaultMutableTreeNode racine;
	private DefaultTreeModel model;
	
	private JMenuBar menuBar = new JMenuBar();
	private  JButton test1 = new JButton("Ouvrir");
	private JButton test2 = new JButton("Enregistrer");
	private JMenu test3 = new JMenu("Compiler");
	private JMenu test4 = new JMenu("Executer");	
	
	private JButton ajouter = new JButton("Ajout-Instruction");
	private JButton effacer = new JButton("Supprimer");
	private JButton declarer = new JButton("Declarer-Varaible");
	private JButton parcourir = new JButton("Utiliser-Fonction");
	
	public Arbre4(){
		this.setSize(500, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("ALGOTREE");
		//On invoque la méthode de construction de notre arbre
		buildTree();
		
		//L'ordre d'ajout va déterminer l'ordre d'apparition dans le menu de gauche à droite
        //Le premier ajouté sera tout à gauche de la barre de menu et inversement pour le dernier
	this.menuBar.add(test1);
	this.menuBar.add(test2);
	this.menuBar.add(test3);
	this.menuBar.add(test4);
	
	 
	final JFileChooser save=new JFileChooser();	
	test2.addActionListener(new ActionListener(){	
		
		/**
		 * Return a list of all nodes found in the tree.
		 * All nodes should be of {@link TreeNode} type or
		 * there will be a {@link ClassCastException}.
		 *
		 * @param tree The tree.
		 * @return The list of {@link TreeNode}.
		 */
		public List<DefaultMutableTreeNode>  getAllNodes(JTree tree) {
			List<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
			nodes.add(root);
			appendAllChildrenToList(nodes, root, true);
			return nodes;
		}	 
		/**
		 * Return a list of all nodes that are child, grand-child, etc...
		 * of the given {@link TreeNode}.
		 * All nodes should be of {@link TreeNode} type or
		 * there will be a {@link ClassCastException}.
		 *
		 * @param node The parent node.
		 * @return The list of {@link TreeNode}.
		 */
		public List<DefaultMutableTreeNode> getAllNodes(DefaultMutableTreeNode node) {
			return getAllNodes(node, true);
		}
	 
		/**
		 * Return a list of all nodes that are child, grand-child, etc...
		 * of the given {@link TreeNode}.
		 * All nodes should be of {@link TreeNode} type or
		 * there will be a {@link ClassCastException}.
		 *
		 * @param node The parent node.
		 * @param getChildChildren <code>true</code> if children of each child should be taken too.
		 * @return The list of {@link TreeNode}.
		 */
		public List<DefaultMutableTreeNode> getAllNodes(DefaultMutableTreeNode node, boolean getChildChildren) {
			List<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
			appendAllChildrenToList(nodes, node, getChildChildren);
			return nodes;
		}
	 
		/**
		 * Append all children of <code>parent</code> to the <code>nodes</code> list.
		 *
		 * @param nodes The list to append on.
		 * @param parent The parent node.
		 * @param getChildChildren <code>true</code> if children of each child should be taken too.
		 */
		private void appendAllChildrenToList(List<DefaultMutableTreeNode> nodes, DefaultMutableTreeNode parent, boolean getChildChildren) {
			Enumeration children = parent.children();
			if (children != null) {
				while (children.hasMoreElements()) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
					nodes.add(node);
					if (getChildChildren) {
						appendAllChildrenToList(nodes, node, getChildChildren);
					}
				}
			}
		}

	 
			//quand on appuie sur le bouton sauvegarder
			 public void actionPerformed(ActionEvent sauvegarde) {
						    save.setDialogTitle("Enregister un programme");
						    save.setDialogType(JFileChooser.SAVE_DIALOG);
						    save.setApproveButtonText("Enregister");
						    int value = save.showSaveDialog(null);
						    if (value == 0) {
						      try { 
							String check = save.getSelectedFile().getPath();							
							setMainFile(check); 
						      } catch(Exception e1){ 
							  }
						    }	  
			 		
						    
						    
						    try{
			 		    BufferedWriter out = new BufferedWriter(new FileWriter(path+".jvs"));
			 		    out.write("void main() {"); 
			 		  List<DefaultMutableTreeNode> nodesliste = new ArrayList<DefaultMutableTreeNode>();
			 		   nodesliste= getAllNodes(arbre);
			 		   
			 		  for( Iterator i = nodesliste.iterator(); i.hasNext();) {
			 			 DefaultMutableTreeNode courantnode= (DefaultMutableTreeNode)i.next();
			 			 String nodename = courantnode.toString();
			 			 
			 			 	if (nodename=="VARIABLES"){
				 				  nodesliste.remove(courantnode);
						 		  for( Iterator j = nodesliste.iterator(); j.hasNext();) {
						 		     DefaultMutableTreeNode declarationode= (DefaultMutableTreeNode)j.next();
							 		 String declarationodename = declarationode.toString();
					 				  while(declarationodename!="INSTRUCTIONS"){  
						 	
						 			     String f [] = declarationodename.split (":") ;
						 			     String h = f[0];
						 			     String g = f[1] ;
						 			     if(g=="est-un-entier"){
						 			    	 out.write("int "+h+"=readInteger()");
						 			    	nodesliste.remove(declarationode);
						 			    	 }
						 		    
						 			     if(g=="est-chaine-char"){
						 			    	 out.write("read "+h);
							 			    	nodesliste.remove(declarationode);
						 			    	 }
							 		    
						 			     if(g=="est-un-entier"){
						 			    	 out.write("readInteger "+h);
							 			    	nodesliste.remove(declarationode);
						 			    	 }
							 		    
					 				  }
						 		  }
						 		  
					 			  if (nodename=="INSTRUCTIONS"){
					 				  nodesliste.remove(courantnode);
					 
					 			  }
					 			  if (nodename=="SI"){
							 			 DefaultMutableTreeNode conditionode= (DefaultMutableTreeNode)i.next();
					 			    	 out.write("if("+conditionode.toString()+") {");

							 			 DefaultMutableTreeNode ifinstructionode= (DefaultMutableTreeNode)i.next();
					 			    	 while(ifinstructionode.toString()!="SINON"){
					 			    		 
					 			    		 
									 			out.write("}"); 
					 			    	 }
					 				   			  
							 			out.write("}"); 

					 			  }
						 		  
					 			  if (nodename=="FIN-DU-PROGRAMME"){
						 			out.write("}"); 
						 		  }
						 			
			 			  } 
			 		  }

			 		    out.write("}"); 
			 		    out.close(); 
			 		    System.out.println("Le fichier "+(new File(path+".jvs").getName())+" est sauvegardé");
						    }catch (Exception e){}
			 		 	 }

	});
	
	

	
	
	final JFileChooser fc = new JFileChooser();
    parcourir.addActionListener(new ActionListener(){			
		public void actionPerformed(ActionEvent ajout) {
			if(arbre.getLastSelectedPathComponent() != null){					
			      DefaultMutableTreeNode lenoeud =(DefaultMutableTreeNode)arbre.getLastSelectedPathComponent();
			       // pour afficher le chemin de ce noeud vers la racine de l’arbre
			       boolean nouvelleligne= false;
			      String s = lenoeud .toString();
			      if(lenoeud .toString()=="INSTRUCTIONS") nouvelleligne= true;			      
			      while(!lenoeud.isRoot()) {
			          lenoeud = (DefaultMutableTreeNode)lenoeud.getParent();
			          if(lenoeud .toString()== "INSTRUCTIONS")nouvelleligne= true;
			      }	     
		   		if(!nouvelleligne) {
					JOptionPane jop3 = new JOptionPane();
					jop3.showMessageDialog(null, "Aller dans INSTRUCTIONS", "Erreur", JOptionPane.ERROR_MESSAGE);
					}					
				else{
					int returnVal = fc.showOpenDialog(Arbre4.this);
			          if (returnVal == JFileChooser.APPROVE_OPTION) {
			             File file = fc.getSelectedFile();
			             System.out.println(file.getName());
						 String nodeName = file.getName();			 
						 if(nodeName != null && !nodeName.trim().equals("")){		
							DefaultMutableTreeNode parentNode =  (DefaultMutableTreeNode)arbre.getLastSelectedPathComponent();
							DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(nodeName);
							parentNode.add(childNode);														
							model.insertNodeInto(childNode, parentNode, parentNode.getChildCount()-1);
							model.nodeChanged(parentNode);	
						}
					  }
				}					
			}
			else{
				System.out.println("AUCUNE SELECTION ! ! !");
			}
		}

	});
	
	

    
    
	declarer.addActionListener(new ActionListener(){		
		public void actionPerformed(ActionEvent evnt) {
			if(arbre.getLastSelectedPathComponent() != null){
				
				if(!(arbre.getLastSelectedPathComponent().toString() == "VARIABLES") ){
				
				JOptionPane jop3 = new JOptionPane();
				jop3.showMessageDialog(null, "Veuillez selectionner VARIABLES", "Erreur", JOptionPane.ERROR_MESSAGE);
				}							
			else{				
				JOptionPane jop = new JOptionPane();				
				String NomdeVariable = jop.showInputDialog("Saisir le nom de variable");				
				String[] typedevariable = {"EST_CHAINE_DE_CARACTERE", "EST_ENTIER_NATURELLE", "EST_NOMBRE_REELLE"};
				String TypedeVariable = (String)jop.showInputDialog(null, 
						"Veuillez choisir le type de variable",
						"Nouvelle_Ligne !",
						JOptionPane.QUESTION_MESSAGE,
						null,
						typedevariable,
						typedevariable[2]);
				String NomduNoeud = NomdeVariable +" : " + TypedeVariable;			 
				if(NomduNoeud != null && !NomduNoeud.trim().equals("")){		
					DefaultMutableTreeNode parentNode =  (DefaultMutableTreeNode)arbre.getLastSelectedPathComponent();
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(NomduNoeud);
					parentNode.add(childNode);														
					model.insertNodeInto(childNode, parentNode, parentNode.getChildCount()-1);
					model.nodeChanged(parentNode);				
				
					}				
			}
		}
		
	
			else{
				System.out.println("AUCUNE SELECTION ! ! !");
			}
		}
	});
		
	ajouter.addActionListener(new ActionListener(){			
			public void actionPerformed(ActionEvent ajout) {
				if(arbre.getLastSelectedPathComponent() != null){	
					
				      DefaultMutableTreeNode lenoeud =(DefaultMutableTreeNode)arbre.getLastSelectedPathComponent();
				       // pour afficher le chemin de 
				      // ce noeud vers la racine de l’arbre
				       boolean nouvelleligne= false;
				      String s = lenoeud .toString();
				      if(lenoeud .toString()=="INSTRUCTIONS") nouvelleligne= true;
				      
				      while(!lenoeud.isRoot()) {
				          lenoeud = (DefaultMutableTreeNode)lenoeud.getParent();
				          if(lenoeud .toString()== "INSTRUCTIONS")nouvelleligne= true;
				      }	     
			   		if(!nouvelleligne) {
						JOptionPane jop3 = new JOptionPane();
						jop3.showMessageDialog(null, "Aller dans INSTRUCTIONS", "Erreur", JOptionPane.ERROR_MESSAGE);
						}					
					else{
					JOptionPane jop = new JOptionPane();
							String[] action = {"si_alors_sinon", "pour_finpour", "egale"};
							String nodeName = (String)jop.showInputDialog(null, 
															"Veuillez choisir l'instruction",
															"Nouvelle_Ligne !",
															JOptionPane.QUESTION_MESSAGE,
															null,
															action,
															action[2]);								
						if(nodeName != null && !nodeName.trim().equals("")){						
						DefaultMutableTreeNode parentNode =  (DefaultMutableTreeNode)arbre.getLastSelectedPathComponent();
						
						if(nodeName == "si_alors_sinon"){
							
							DefaultMutableTreeNode childNode1 = new DefaultMutableTreeNode("SI");
							parentNode.add(childNode1);
							model.insertNodeInto(childNode1, parentNode, parentNode.getChildCount()-1);
							model.nodeChanged(parentNode);	
							
							DefaultMutableTreeNode childNode2 = new DefaultMutableTreeNode("ALORS");
							parentNode.add(childNode2);
							model.insertNodeInto(childNode2, parentNode, parentNode.getChildCount()-1);
							model.nodeChanged(parentNode);
							
							DefaultMutableTreeNode childNode3 = new DefaultMutableTreeNode("SINON");
							parentNode.add(childNode3);
							model.insertNodeInto(childNode3, parentNode, parentNode.getChildCount()-1);
							model.nodeChanged(parentNode);
						}
						
						
						else{
						DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(nodeName);
						parentNode.add(childNode);
						model.insertNodeInto(childNode, parentNode, parentNode.getChildCount()-1);
						model.nodeChanged(parentNode);
						}
					}					
				}
				}
				else{
					System.out.println("AUCUNE SELECTION ! ! !");
				}
			}
		});
		
		
		effacer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent supprim) {
				if(arbre.getLastSelectedPathComponent() != null){					
					if(((arbre.getLastSelectedPathComponent().toString() == "VARIABLES")||(arbre.getLastSelectedPathComponent().toString() == "INSTRUCTIONS")||(arbre.getLastSelectedPathComponent().toString() == "FIN DU PROGRAMME") )){
						JOptionPane jop3 = new JOptionPane();
						jop3.showMessageDialog(null, "Impossible de supprimer", "Erreur", JOptionPane.ERROR_MESSAGE);
						}
					else{
					DefaultMutableTreeNode node =  (DefaultMutableTreeNode)arbre.getLastSelectedPathComponent();
					DefaultMutableTreeNode parentNode =  (DefaultMutableTreeNode)node.getParent();
					model.removeNodeFromParent(node);
					model.nodeChanged(parentNode);
					}
				}
				else{
					System.out.println("AUCUNE SELECTION ! ! !");
				}
			}
		});
		
		
		JPanel pan = new JPanel();
		
		pan.add(declarer);
		pan.add(ajouter);	
		pan.add(parcourir);
		pan.add(effacer);
		
		
		this.setJMenuBar(menuBar);
		
		this.getContentPane().add(pan, BorderLayout.SOUTH);
		this.setVisible(true);
	}
		
		
	private void buildTree(){
		this.racine = new DefaultMutableTreeNode();
		//Création d'une racine
		
			
		//les Noeuds principales
		
			DefaultMutableTreeNode rep = new DefaultMutableTreeNode("VARIABLES");
			DefaultMutableTreeNode rep1 = new DefaultMutableTreeNode("INSTRUCTIONS");
			DefaultMutableTreeNode rep2 = new DefaultMutableTreeNode("FIN DU PROGRAMME");
				
			
			//On ajoute les Noeuds à la racine
			racine.add(rep);
			racine.add(rep1);
			racine.add(rep2);
		
			
			//On crée, avec notre hiérarchie, un arbre
			arbre = new JTree();
			this.model = new DefaultTreeModel(this.racine);
			
			arbre.setModel(model);
			//arbre.setRootVisible(false);
			arbre.setEditable(true);
			arbre.getModel().addTreeModelListener(new TreeModelListener() {

		        public void treeNodesChanged(TreeModelEvent evt) {
		        	
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
			//this.getContentPane().add(new JScrollPane(arbre), BorderLayout.CENTER);
			
		//On crée, avec notre hiérarchie, un arbre
		//arbre = new JTree(racine);		
		//Que nous plaçons sur le ContentPane de notre JFrame à l'aide d'un scroll 
		this.getContentPane().add(new JScrollPane(arbre));
	}
	
	
	
		
	private void setMainFile(String pFile) {
	    File file = new File(pFile);
	    String folder = file.getParent() == null ? System.getProperty("user.dir") : file.getParent();
	    String name = file.getName().replaceAll("\\.[A-Za-z]+$", "");
	    if (Translator.isReserved(name)) {
	      main = "my_"+name;
			JOptionPane jop3 = new JOptionPane();		
			jop3.showMessageDialog(null, "nom contient caractere interdit par java", "Erreur", JOptionPane.ERROR_MESSAGE);
	    } else if (!name.matches("[A-Za-z_][A-Za-z0-9_]*")) {
	      main = name.replaceAll("[^A-Za-z0-9_]", "_");
			JOptionPane jop3 = new JOptionPane();
			jop3.showMessageDialog(null, "nom contient caractere interdit par java", "Erreur", JOptionPane.ERROR_MESSAGE);
			
	      //printConsole("Attention: le nom \""+name+"\" contient quelque caractère interdit par Java,\n renommons le \""+main+"\"", 'b');
	    } else
	      main = name;
	    path = folder + File.separatorChar + main;
	  } 
	
	private String main = null, path = null;

	  /**This is the entry point to run the proglet pupil's program: do not modify manually !!. */
	  public static Runnable runnable = null;
	  
	  // Flag whether we are in standalone mode or web-browser mode
	  boolean standalone = true; 

	  // Flags whether we have derived an application derivating this class or if this class hase been constructed.
	  boolean application = false;
	  
	  
	
	public static void main(String[] args){
		
//Cette instruction permet de récupérer tous les looks du système
		try {
			   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (InstantiationException e) {
			} catch (ClassNotFoundException e) {
			} catch (UnsupportedLookAndFeelException e) {
			} catch (IllegalAccessException e) {}
			
		Arbre4 fen = new Arbre4();
	}
}





