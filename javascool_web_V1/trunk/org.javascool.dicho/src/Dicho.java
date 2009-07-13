/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

//package org.javascool.dicho;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JLayeredPane;
import java.awt.Dimension;

// Used to define an icon/label
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;

// Used to define a button
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Used to open an window
import javax.swing.JFrame;

/** Définit une proglet javascool qui permet d'expérimenter la recherche dichotomique.
 * Méthodes statiques à importer: <pre>
 * import static Dicho.compare;
 * import static Dicho.length;
 * </pre>
 * Fichiers utilisés: <pre>
 * ./dicho_background.jpg
 * http://upload.wikimedia.org/wikipedia/*
 * </pre>
 * Documentation: <a href="sujet.html">sujet</a> et <a href="correction.html">correction</a>.
 */
public class Dicho {

  // This defines the panel to display
  private static class Panel extends JPanel {
    public Panel() {
      super(new BorderLayout()); 
      setBackground(Color.WHITE);
      // Adds the background icon
      JLayeredPane book = new JLayeredPane(); book.setPreferredSize(new Dimension(550, 350)); add(book);
      JLabel icon = new JLabel(); icon.setBounds(10, 0, 550, 350); icon.setIcon(new ImageIcon("dicho_background.jpg")); book.add(icon, new Integer(1), 0);
      // Adds the label and flag
      name = new JLabel(); name.setBounds(90, 50, 150, 100); book.add(name, new Integer(2), 0);
      flag = new JLabel(); flag.setBounds(340, 100, 200, 100); book.add(flag, new Integer(2), 1);
      // Adds the prev/next buttons and page count label
      JPanel tail = new JPanel(); add(tail, BorderLayout.SOUTH);
      JButton prev = new JButton("<-"); tail.add(prev); 
      prev.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
	    show(--current);
      }});
      JButton next = new JButton("->"); tail.add(next); 
      next.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
	show(++current);
      }});
      tail.add(new JLabel("       ")); 
      num = new JLabel(); tail.add(num);
      show(63);
    }

    /** Affiche une page.
     * @param page L'index de la page de 0 à getSize() exclu.
     */
    public void show(int page) {
      if (page < 0) page = 0; if (page >= length()) page = length() - 1; current = page;
      num.setText(""+page);
      name.setText("<html><h2>"+dicho[page][0]+"</h2></html>"); 
      try { flag.setIcon(new ImageIcon(new URL(dicho[page][1]))); } catch(Exception e) { }
    } 
    private JLabel name, flag, num; private int current;
  }

  //
  // This defines the tests on the panel
  //

  /** Test du panel. */
  static void test() {
    // Tests if the dico is sorted
    for(int i = 1; i < dicho.length; i++)
      if (compare(dicho[i][0], i - 1) <= 0)
	System.out.println("Ahhh bad sort between "+dicho[i][0]+"#"+i+(compare(dicho[i][0], i - 1) == 0 ? " == " : " << ")+dicho[i-1][0]+"#"+(i-1));
    // Tests the index function
    for(int i = 0; i < dicho.length; i++)
      if (i != getIndex(dicho[i][0]))
	System.out.println("Ohhh bad index for "+dicho[i][0]+"#"+i+" <> "+getIndex(dicho[i][0]));
  }

  /** Gets the index of a given page.
   * <div><tt>- DO NOT USE !!! This is the solution of the excercice !!!</tt></div>
   * @param name The name to compare with.
   * @return The page index or -1 if the name is not on some page.
   */
  private static int getIndex(String name) {
    int debut = 0, fin = length();
    while(true) {
      int milieu = (debut + fin) / 2;
      int c = compareTo(name, milieu);
      // System.out.println("'"+name+"'"+(c < 0 ? " < " : c > 0 ? " > " : " = ")+ "dicho["+debut+"<"+milieu+"<"+fin+"] = '"+dicho[i][0]+"'");
      if (c == 0) {
	return milieu;
      } else {
	if (debut == fin)
	  return -1;
	if (c < 0)
	  fin = milieu;
	else
	  debut = milieu;
      }
    }
  }

  //
  // This defines the javascool interface
  //

  /** Renvoie le nombre de page. */
  public static int length() { return dicho.length; }

  /** Compare un nom au nom affiché sur une page.
   * @param name Le nom à comparer.
   * @param page L'index de la page, de 0 à length() exclut.
   * @return -1 si le nom se situe avant celui de la page, +1 si le nom se situe après celui de la page, 0 si il correspond à celui de la page.
   */
  public static int compare(String name, int page) { 
    if (page < 0) page = 0; if (page >= length()) page = length() - 1;
    panel.show(page); 
    return compareTo(name, page);
  }
  // Compares without accents
  private static int compareTo(String name, int page) { 
    return noAccent(name).compareTo(noAccent(dicho[page][0])); 
  }
  private static String noAccent(String name) {
    return name.replaceAll("[éè]", "e").replace("É", "E").replace("Î", "I").replace("ô", "o").replace("ã", "a");
  }

  // All the data sorted in alphabetic order
  private static String dicho[][] = {
    { "Afghanistan", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/Flag_of_Afghanistan.svg/100px-Flag_of_Afghanistan.svg.png", "http://fr.wikipedia.org/wiki/Afghanistan" },  
    { "Afrique du Sud", "http://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Flag_of_South_Africa.svg/100px-Flag_of_South_Africa.svg.png", "http://fr.wikipedia.org/wiki/Afrique_du_Sud" },  
    { "Albanie", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Flag_of_Albania.svg/100px-Flag_of_Albania.svg.png", "http://fr.wikipedia.org/wiki/Albanie" },  
    { "Algérie", "http://upload.wikimedia.org/wikipedia/commons/thumb/7/77/Flag_of_Algeria.svg/100px-Flag_of_Algeria.svg.png", "http://fr.wikipedia.org/wiki/Alg%C3%A9rie" },     
    { "Allemagne", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Flag_of_Germany.svg/100px-Flag_of_Germany.svg.png", "http://fr.wikipedia.org/wiki/Allemagne" },  
    { "Andorre", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Flag_of_Andorra.svg/100px-Flag_of_Andorra.svg.png", "http://fr.wikipedia.org/wiki/Andorre" },  
    { "Angola", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9d/Flag_of_Angola.svg/100px-Flag_of_Angola.svg.png", "http://fr.wikipedia.org/wiki/Angola" },      
    { "Antigua", "http://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Flag_of_Antigua_and_Barbuda.svg/100px-Flag_of_Antigua_and_Barbuda.svg.png", "http://fr.wikipedia.org/wiki/Antigua-et-Barbuda" },    
    { "Arabie Saoudite", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/Flag_of_Saudi_Arabia.svg/100px-Flag_of_Saudi_Arabia.svg.png", "http://fr.wikipedia.org/wiki/Arabie_saoudite" },    
    { "Argentine", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Flag_of_Argentina.svg/100px-Flag_of_Argentina.svg.png", "http://fr.wikipedia.org/wiki/Argentine" },    
    { "Arménie", "http://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Flag_of_Armenia.svg/100px-Flag_of_Armenia.svg.png", "http://fr.wikipedia.org/wiki/Arm%C3%A9nie" },    
    { "Australie", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/b9/Flag_of_Australia.svg/100px-Flag_of_Australia.svg.png", "http://fr.wikipedia.org/wiki/Australie" },    
    { "Autriche", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Flag_of_Austria.svg/100px-Flag_of_Austria.svg.png", "http://fr.wikipedia.org/wiki/Autriche" },    
    { "Azerbaïdjan", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Flag_of_Azerbaijan.svg/100px-Flag_of_Azerbaijan.svg.png", "http://fr.wikipedia.org/wiki/Azerba%C3%AFdjan" },    
    { "Bahamas", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Flag_of_the_Bahamas.svg/100px-Flag_of_the_Bahamas.svg.png", "http://fr.wikipedia.org/wiki/Bahamas" },    
    { "Bahreïn", "http://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Flag_of_Bahrain.svg/100px-Flag_of_Bahrain.svg.png", "http://fr.wikipedia.org/wiki/Bahre%C3%AFn" },    
    { "Bangladesh", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/f9/Flag_of_Bangladesh.svg/100px-Flag_of_Bangladesh.svg.png", "http://fr.wikipedia.org/wiki/Bangladesh" },    
    { "Barbade", "http://upload.wikimedia.org/wikipedia/commons/thumb/e/ef/Flag_of_Barbados.svg/100px-Flag_of_Barbados.svg.png", "http://fr.wikipedia.org/wiki/Barbade" },    
    { "Belgique", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Flag_of_Belgium_%28civil%29.svg/100px-Flag_of_Belgium_%28civil%29.svg.png", "http://fr.wikipedia.org/wiki/Belgique" },    
    { "Belize", "http://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Flag_of_Belize.svg/100px-Flag_of_Belize.svg.png", "http://fr.wikipedia.org/wiki/Belize" },    
    { "Bénin", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Flag_of_Benin.svg/100px-Flag_of_Benin.svg.png", "http://fr.wikipedia.org/wiki/B%C3%A9nin" },    
    { "Bhoutan", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Flag_of_Bhutan.svg/100px-Flag_of_Bhutan.svg.png", "http://fr.wikipedia.org/wiki/Bhoutan" },    
    { "Biélorussie", "http://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Flag_of_Belarus.svg/100px-Flag_of_Belarus.svg.png", "http://fr.wikipedia.org/wiki/Bi%C3%A9lorussie" },    
    { "Bolivie", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/de/Flag_of_Bolivia_%28state%29.svg/100px-Flag_of_Bolivia_%28state%29.svg.png", "http://fr.wikipedia.org/wiki/Bolivie" },    
    { "Bosnie-Herzégovine", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/bf/Flag_of_Bosnia_and_Herzegovina.svg/100px-Flag_of_Bosnia_and_Herzegovina.svg.png", "http://fr.wikipedia.org/wiki/Bosnie-Herz%C3%A9govine" },    
    { "Botswana", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Flag_of_Botswana.svg/100px-Flag_of_Botswana.svg.png", "http://fr.wikipedia.org/wiki/Botswana" },    
    { "Brésil", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Flag_of_Brazil.svg/100px-Flag_of_Brazil.svg.png", "http://fr.wikipedia.org/wiki/Br%C3%A9sil" },    
    { "Brunei", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/Flag_of_Brunei.svg/100px-Flag_of_Brunei.svg.png", "http://fr.wikipedia.org/wiki/Brunei" },    
    { "Bulgarie", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/Flag_of_Bulgaria.svg/100px-Flag_of_Bulgaria.svg.png", "http://fr.wikipedia.org/wiki/Bulgarie" },    
    { "Burkina Faso", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Flag_of_Burkina_Faso.svg/100px-Flag_of_Burkina_Faso.svg.png", "http://fr.wikipedia.org/wiki/Burkina" },    
    { "Burundi", "http://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Flag_of_Burundi.svg/100px-Flag_of_Burundi.svg.png", "http://fr.wikipedia.org/wiki/Burundi" },    
    { "Cambodge", "http://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Flag_of_Cambodia.svg/100px-Flag_of_Cambodia.svg.png", "http://fr.wikipedia.org/wiki/Cambodge" },    
    { "Cameroun", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/4f/Flag_of_Cameroon.svg/100px-Flag_of_Cameroon.svg.png", "http://fr.wikipedia.org/wiki/Cameroun" },    
    { "Canada", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Flag_of_Canada.svg/100px-Flag_of_Canada.svg.png", "http://fr.wikipedia.org/wiki/Canada" },    
    { "Cap-Vert", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Flag_of_Cape_Verde.svg/100px-Flag_of_Cape_Verde.svg.png", "http://fr.wikipedia.org/wiki/Cap-Vert" },    
    { "Centrafrique", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Flag_of_the_Central_African_Republic.svg/100px-Flag_of_the_Central_African_Republic.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_centrafricaine" },    
    { "Chili", "http://upload.wikimedia.org/wikipedia/commons/thumb/7/78/Flag_of_Chile.svg/100px-Flag_of_Chile.svg.png", "http://fr.wikipedia.org/wiki/Chili" },    
    { "Chine", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Flag_of_the_People%27s_Republic_of_China.svg/100px-Flag_of_the_People%27s_Republic_of_China.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_populaire_de_Chine" },    
    { "Chypre", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Flag_of_Cyprus.svg/100px-Flag_of_Cyprus.svg.png", "http://fr.wikipedia.org/wiki/Chypre_%28pays%29" },    
    { "Colombie", "http://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Flag_of_Colombia.svg/100px-Flag_of_Colombia.svg.png", "http://fr.wikipedia.org/wiki/Colombie" },    
    { "Comores", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/94/Flag_of_the_Comoros.svg/100px-Flag_of_the_Comoros.svg.png", "http://fr.wikipedia.org/wiki/Union_des_Comores" },    
    { "Congo", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Flag_of_the_Democratic_Republic_of_the_Congo.svg/100px-Flag_of_the_Democratic_Republic_of_the_Congo.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_d%C3%A9mocratique_du_Congo" },    
    { "Congo-Brazzaville", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Flag_of_the_Republic_of_the_Congo.svg/100px-Flag_of_the_Republic_of_the_Congo.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_du_Congo" },    
    { "Corée du Nord", "http://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Flag_of_North_Korea.svg/100px-Flag_of_North_Korea.svg.png", "http://fr.wikipedia.org/wiki/Cor%C3%A9e_du_Nord" },    
    { "Corée du Sud", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/09/Flag_of_South_Korea.svg/100px-Flag_of_South_Korea.svg.png", "http://fr.wikipedia.org/wiki/Cor%C3%A9e_du_Sud" },    
    { "Costa Rica", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/f2/Flag_of_Costa_Rica.svg/100px-Flag_of_Costa_Rica.svg.png", "http://fr.wikipedia.org/wiki/Costa_Rica" },    
    { "Côte d'Ivoire", "http://upload.wikimedia.org/wikipedia/commons/thumb/8/86/Flag_of_Cote_d%27Ivoire.svg/100px-Flag_of_Cote_d%27Ivoire.svg.png", "http://fr.wikipedia.org/wiki/C%C3%B4te_d%27Ivoire" },    
    { "Croatie", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Flag_of_Croatia.svg/100px-Flag_of_Croatia.svg.png", "http://fr.wikipedia.org/wiki/Croatie" },    
    { "Cuba", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/bd/Flag_of_Cuba.svg/100px-Flag_of_Cuba.svg.png", "http://fr.wikipedia.org/wiki/Cuba" },    
    { "Danemark", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/Flag_of_Denmark.svg/100px-Flag_of_Denmark.svg.png", "http://fr.wikipedia.org/wiki/Danemark" },    
    { "Djibouti", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/34/Flag_of_Djibouti.svg/100px-Flag_of_Djibouti.svg.png", "http://fr.wikipedia.org/wiki/Djibouti" },    
    { "Dominicaine (république)", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Flag_of_the_Dominican_Republic.svg/100px-Flag_of_the_Dominican_Republic.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_dominicaine" },    
    { "Dominique", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/Flag_of_Dominica.svg/100px-Flag_of_Dominica.svg.png", "http://fr.wikipedia.org/wiki/Dominique_%28pays%29" },    
    { "Égypte", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Egypt.svg/100px-Flag_of_Egypt.svg.png", "http://fr.wikipedia.org/wiki/%C3%89gypte" },    
    { "Émirats arabes unis", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Flag_of_the_United_Arab_Emirates.svg/100px-Flag_of_the_United_Arab_Emirates.svg.png", "http://fr.wikipedia.org/wiki/%C3%89mirats_arabes_unis" },    
    { "Équateur", "http://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/Flag_of_Ecuador.svg/100px-Flag_of_Ecuador.svg.png", "http://fr.wikipedia.org/wiki/%C3%89quateur_%28pays%29" },    
    { "Érythrée", "http://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Flag_of_Eritrea.svg/100px-Flag_of_Eritrea.svg.png", "http://fr.wikipedia.org/wiki/%C3%89rythr%C3%A9e" },    
    { "Espagne", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/Flag_of_Spain.svg/100px-Flag_of_Spain.svg.png", "http://fr.wikipedia.org/wiki/Espagne" },    
    { "Estonie", "http://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Flag_of_Estonia.svg/100px-Flag_of_Estonia.svg.png", "http://fr.wikipedia.org/wiki/Estonie" },    
    { "États-Unis", "http://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/Flag_of_the_United_States.svg/100px-Flag_of_the_United_States.svg.png", "http://fr.wikipedia.org/wiki/%C3%89tats-Unis" },    
    { "Éthiopie", "http://upload.wikimedia.org/wikipedia/commons/thumb/7/71/Flag_of_Ethiopia.svg/100px-Flag_of_Ethiopia.svg.png", "http://fr.wikipedia.org/wiki/%C3%89thiopie" },    
    { "Fidji", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Flag_of_Fiji.svg/100px-Flag_of_Fiji.svg.png", "http://fr.wikipedia.org/wiki/Fidji" },    
    { "Finlande", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Flag_of_Finland.svg/100px-Flag_of_Finland.svg.png", "http://fr.wikipedia.org/wiki/Finlande" },    
    { "France", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Flag_of_France.svg/100px-Flag_of_France.svg.png", "http://fr.wikipedia.org/wiki/France" },    
    { "Gabon", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/04/Flag_of_Gabon.svg/100px-Flag_of_Gabon.svg.png", "http://fr.wikipedia.org/wiki/Gabon" },    
    { "Gambie", "http://upload.wikimedia.org/wikipedia/commons/thumb/7/77/Flag_of_The_Gambia.svg/100px-Flag_of_The_Gambia.svg.png", "http://fr.wikipedia.org/wiki/Gambie" },    
    { "Géorgie", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Flag_of_Georgia.svg/100px-Flag_of_Georgia.svg.png", "http://fr.wikipedia.org/wiki/G%C3%A9orgie_%28pays%29" },    
    { "Ghana", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Flag_of_Ghana.svg/100px-Flag_of_Ghana.svg.png", "http://fr.wikipedia.org/wiki/Ghana" },    
    { "Grèce", "http://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Flag_of_Greece.svg/100px-Flag_of_Greece.svg.png", "http://fr.wikipedia.org/wiki/Gr%C3%A8ce" },    
    { "Grenade", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Flag_of_Grenada.svg/100px-Flag_of_Grenada.svg.png", "http://fr.wikipedia.org/wiki/Grenade_%28pays%29" },    
    { "Guatemala", "http://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Flag_of_Guatemala.svg/100px-Flag_of_Guatemala.svg.png", "http://fr.wikipedia.org/wiki/Guatemala" },    
    { "Guinée", "http://upload.wikimedia.org/wikipedia/commons/thumb/e/ed/Flag_of_Guinea.svg/100px-Flag_of_Guinea.svg.png", "http://fr.wikipedia.org/wiki/Guin%C3%A9e" },    
    { "Guinée équatoriale", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Flag_of_Equatorial_Guinea.svg/100px-Flag_of_Equatorial_Guinea.svg.png", "http://fr.wikipedia.org/wiki/Guin%C3%A9e_%C3%A9quatoriale" },    
    { "Guinée-Bissau", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Flag_of_Guinea-Bissau.svg/100px-Flag_of_Guinea-Bissau.svg.png", "http://fr.wikipedia.org/wiki/Guin%C3%A9e-Bissau" },    
    { "Guyana", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Flag_of_Guyana.svg/100px-Flag_of_Guyana.svg.png", "http://fr.wikipedia.org/wiki/Guyana" },    
    { "Haïti", "http://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Flag_of_Haiti.svg/100px-Flag_of_Haiti.svg.png", "http://fr.wikipedia.org/wiki/Ha%C3%AFti" },    
    { "Honduras", "http://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Flag_of_Honduras.svg/100px-Flag_of_Honduras.svg.png", "http://fr.wikipedia.org/wiki/Honduras" },    
    { "Hongrie", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Flag_of_Hungary.svg/100px-Flag_of_Hungary.svg.png", "http://fr.wikipedia.org/wiki/Hongrie" },    
    { "Inde", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Flag_of_India.svg/100px-Flag_of_India.svg.png", "http://fr.wikipedia.org/wiki/Inde" },    
    { "Indonésie", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Flag_of_Indonesia.svg/100px-Flag_of_Indonesia.svg.png", "http://fr.wikipedia.org/wiki/Indon%C3%A9sie" },    
    { "Irak", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Flag_of_Iraq.svg/100px-Flag_of_Iraq.svg.png", "http://fr.wikipedia.org/wiki/Irak" },    
    { "Iran", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Flag_of_Iran.svg/100px-Flag_of_Iran.svg.png", "http://fr.wikipedia.org/wiki/Iran" },    
    { "Irlande", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Flag_of_Ireland.svg/100px-Flag_of_Ireland.svg.png", "http://fr.wikipedia.org/wiki/Irlande_%28pays%29" },    
    { "Islande", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/Flag_of_Iceland.svg/100px-Flag_of_Iceland.svg.png", "http://fr.wikipedia.org/wiki/Islande" },    
    { "Israël", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Flag_of_Israel.svg/100px-Flag_of_Israel.svg.png", "http://fr.wikipedia.org/wiki/Isra%C3%ABl" },    
    { "Italie", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/03/Flag_of_Italy.svg/100px-Flag_of_Italy.svg.png", "http://fr.wikipedia.org/wiki/Italie" },    
    { "Jamaïque", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Flag_of_Jamaica.svg/100px-Flag_of_Jamaica.svg.png", "http://fr.wikipedia.org/wiki/Jama%C3%AFque" },    
    { "Japon", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9e/Flag_of_Japan.svg/100px-Flag_of_Japan.svg.png", "http://fr.wikipedia.org/wiki/Japon" },    
    { "Jordanie", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/c0/Flag_of_Jordan.svg/100px-Flag_of_Jordan.svg.png", "http://fr.wikipedia.org/wiki/Jordanie" },    
    { "Kazakhstan", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Flag_of_Kazakhstan.svg/100px-Flag_of_Kazakhstan.svg.png", "http://fr.wikipedia.org/wiki/Kazakhstan" },    
    { "Kenya", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/49/Flag_of_Kenya.svg/100px-Flag_of_Kenya.svg.png", "http://fr.wikipedia.org/wiki/Kenya" },    
    { "Kirghizistan", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Flag_of_Kyrgyzstan.svg/100px-Flag_of_Kyrgyzstan.svg.png", "http://fr.wikipedia.org/wiki/Kirghizie" },    
    { "Kiribati", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Flag_of_Kiribati.svg/100px-Flag_of_Kiribati.svg.png", "http://fr.wikipedia.org/wiki/Kiribati" },    
    { "Koweït", "http://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/Flag_of_Kuwait.svg/100px-Flag_of_Kuwait.svg.png", "http://fr.wikipedia.org/wiki/Kowe%C3%AFt" },    
    { "Laos", "http://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Flag_of_Laos.svg/100px-Flag_of_Laos.svg.png", "http://fr.wikipedia.org/wiki/Laos" },    
    { "Lesotho", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Flag_of_Lesotho.svg/100px-Flag_of_Lesotho.svg.png", "http://fr.wikipedia.org/wiki/Lesotho" },    
    { "Lettonie", "http://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Flag_of_Latvia.svg/100px-Flag_of_Latvia.svg.png", "http://fr.wikipedia.org/wiki/Lettonie" },    
    { "Liban", "http://upload.wikimedia.org/wikipedia/commons/thumb/5/59/Flag_of_Lebanon.svg/100px-Flag_of_Lebanon.svg.png", "http://fr.wikipedia.org/wiki/Liban" },    
    { "Libéria", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/b8/Flag_of_Liberia.svg/100px-Flag_of_Liberia.svg.png", "http://fr.wikipedia.org/wiki/Liberia" },    
    { "Libye", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Flag_of_Libya.svg/100px-Flag_of_Libya.svg.png", "http://fr.wikipedia.org/wiki/Libye" },    
    { "Liechtenstein", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Flag_of_Liechtenstein.svg/100px-Flag_of_Liechtenstein.svg.png", "http://fr.wikipedia.org/wiki/Liechtenstein" },    
    { "Lituanie", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Flag_of_Lithuania.svg/100px-Flag_of_Lithuania.svg.png", "http://fr.wikipedia.org/wiki/Lituanie" },    
    { "Luxembourg", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/da/Flag_of_Luxembourg.svg/100px-Flag_of_Luxembourg.svg.png", "http://fr.wikipedia.org/wiki/Luxembourg_%28pays%29" },    
    { "Macédoine", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Flag_of_Macedonia.svg/100px-Flag_of_Macedonia.svg.png", "http://fr.wikipedia.org/wiki/Mac%C3%A9doine_%28pays%29" },    
    { "Madagascar", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Flag_of_Madagascar.svg/100px-Flag_of_Madagascar.svg.png", "http://fr.wikipedia.org/wiki/Madagascar" },    
    { "Malaisie", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/66/Flag_of_Malaysia.svg/100px-Flag_of_Malaysia.svg.png", "http://fr.wikipedia.org/wiki/Malaisie" },    
    { "Malawi", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Flag_of_Malawi.svg/100px-Flag_of_Malawi.svg.png", "http://fr.wikipedia.org/wiki/Malawi" },    
    { "Maldives", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Flag_of_Maldives.svg/100px-Flag_of_Maldives.svg.png", "http://fr.wikipedia.org/wiki/Maldives" },    
    { "Mali", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Flag_of_Mali.svg/100px-Flag_of_Mali.svg.png", "http://fr.wikipedia.org/wiki/Mali" },    
    { "Malte", "http://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Flag_of_Malta.svg/100px-Flag_of_Malta.svg.png", "http://fr.wikipedia.org/wiki/Malte" },    
    { "Maroc", "http://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Flag_of_Morocco.svg/100px-Flag_of_Morocco.svg.png", "http://fr.wikipedia.org/wiki/Maroc" },    
    { "Marshall (Îles)", "http://upload.wikimedia.org/wikipedia/commons/thumb/2/2e/Flag_of_the_Marshall_Islands.svg/100px-Flag_of_the_Marshall_Islands.svg.png", "http://fr.wikipedia.org/wiki/Marshall_%28pays%29" },    
    { "Maurice", "http://upload.wikimedia.org/wikipedia/commons/thumb/7/77/Flag_of_Mauritius.svg/100px-Flag_of_Mauritius.svg.png", "http://fr.wikipedia.org/wiki/Maurice_%28pays%29" },    
    { "Mauritanie", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Flag_of_Mauritania.svg/100px-Flag_of_Mauritania.svg.png", "http://fr.wikipedia.org/wiki/Mauritanie" },    
    { "Mexique", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/fc/Flag_of_Mexico.svg/100px-Flag_of_Mexico.svg.png", "http://fr.wikipedia.org/wiki/Mexique" },    
    { "Micronésie", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Flag_of_Federated_States_of_Micronesia.svg/100px-Flag_of_Federated_States_of_Micronesia.svg.png", "http://fr.wikipedia.org/wiki/Micron%C3%A9sie_%28pays%29" },    
    { "Moldavie", "http://upload.wikimedia.org/wikipedia/commons/thumb/2/27/Flag_of_Moldova.svg/100px-Flag_of_Moldova.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_de_Moldavie" },    
    { "Monaco", "http://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Flag_of_Monaco.svg/100px-Flag_of_Monaco.svg.png", "http://fr.wikipedia.org/wiki/Monaco" },    
    { "Mongolie", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Flag_of_Mongolia.svg/100px-Flag_of_Mongolia.svg.png", "http://fr.wikipedia.org/wiki/Mongolie" },    
    { "Monténégro", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/64/Flag_of_Montenegro.svg/100px-Flag_of_Montenegro.svg.png", "http://fr.wikipedia.org/wiki/Mont%C3%A9n%C3%A9gro" },    
    { "Mozambique", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Flag_of_Mozambique.svg/100px-Flag_of_Mozambique.svg.png", "http://fr.wikipedia.org/wiki/Mozambique" },    
    { "Myanmar", "http://upload.wikimedia.org/wikipedia/commons/thumb/8/8c/Flag_of_Myanmar.svg/100px-Flag_of_Myanmar.svg.png", "http://fr.wikipedia.org/wiki/Birmanie" },    
    { "Namibie", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/00/Flag_of_Namibia.svg/100px-Flag_of_Namibia.svg.png", "http://fr.wikipedia.org/wiki/Namibie" },    
    { "Nauru", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/30/Flag_of_Nauru.svg/100px-Flag_of_Nauru.svg.png", "http://fr.wikipedia.org/wiki/Nauru" },    
    { "Népal", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9b/Flag_of_Nepal.svg/100px-Flag_of_Nepal.svg.png", "http://fr.wikipedia.org/wiki/N%C3%A9pal" },    
    { "Nicaragua", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Flag_of_Nicaragua.svg/100px-Flag_of_Nicaragua.svg.png", "http://fr.wikipedia.org/wiki/Nicaragua" },    
    { "Niger", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/Flag_of_Niger.svg/100px-Flag_of_Niger.svg.png", "http://fr.wikipedia.org/wiki/Niger" },    
    { "Nigeria", "http://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Flag_of_Nigeria.svg/100px-Flag_of_Nigeria.svg.png", "http://fr.wikipedia.org/wiki/Nigeria" },    
    { "Norvège", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Flag_of_Norway.svg/100px-Flag_of_Norway.svg.png", "http://fr.wikipedia.org/wiki/Norv%C3%A8ge" },    
    { "Nouvelle-Zélande", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Flag_of_New_Zealand.svg/100px-Flag_of_New_Zealand.svg.png", "http://fr.wikipedia.org/wiki/Nouvelle-Z%C3%A9lande" },    
    { "Oman", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Flag_of_Oman.svg/100px-Flag_of_Oman.svg.png", "http://fr.wikipedia.org/wiki/Oman" },    
    { "Ouganda", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Flag_of_Uganda.svg/100px-Flag_of_Uganda.svg.png", "http://fr.wikipedia.org/wiki/Ouganda" },    
    { "Ouzbékistan", "http://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Flag_of_Uzbekistan.svg/100px-Flag_of_Uzbekistan.svg.png", "http://fr.wikipedia.org/wiki/Ouzb%C3%A9kistan" },    
    { "Pakistan", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/32/Flag_of_Pakistan.svg/100px-Flag_of_Pakistan.svg.png", "http://fr.wikipedia.org/wiki/Pakistan" },    
    { "Palaos", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Flag_of_Palau.svg/100px-Flag_of_Palau.svg.png", "http://fr.wikipedia.org/wiki/Palaos" },    
    { "Panamà", "http://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Flag_of_Panama.svg/100px-Flag_of_Panama.svg.png", "http://fr.wikipedia.org/wiki/Panama" },    
    { "Papouasie-Nouvelle-Guinée", "http://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/Flag_of_Papua_New_Guinea.svg/100px-Flag_of_Papua_New_Guinea.svg.png", "http://fr.wikipedia.org/wiki/Papouasie-Nouvelle-Guin%C3%A9e" },    
    { "Paraguay", "http://upload.wikimedia.org/wikipedia/commons/thumb/2/27/Flag_of_Paraguay.svg/100px-Flag_of_Paraguay.svg.png", "http://fr.wikipedia.org/wiki/Paraguay" },    
    { "Pays-Bas", "http://upload.wikimedia.org/wikipedia/commons/thumb/2/20/Flag_of_the_Netherlands.svg/100px-Flag_of_the_Netherlands.svg.png", "http://fr.wikipedia.org/wiki/Pays-Bas" },    
    { "Pérou", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Flag_of_Peru.svg/100px-Flag_of_Peru.svg.png", "http://fr.wikipedia.org/wiki/P%C3%A9rou" },    
    { "Philippines", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Flag_of_the_Philippines.svg/100px-Flag_of_the_Philippines.svg.png", "http://fr.wikipedia.org/wiki/Philippines" },    
    { "Pologne", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/12/Flag_of_Poland.svg/100px-Flag_of_Poland.svg.png", "http://fr.wikipedia.org/wiki/Pologne" },    
    { "Portugal", "http://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Flag_of_Portugal.svg/100px-Flag_of_Portugal.svg.png", "http://fr.wikipedia.org/wiki/Portugal" },    
    { "Qatar", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/65/Flag_of_Qatar.svg/100px-Flag_of_Qatar.svg.png", "http://fr.wikipedia.org/wiki/Qatar" },    
    { "Roumanie", "http://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Flag_of_Romania.svg/100px-Flag_of_Romania.svg.png", "http://fr.wikipedia.org/wiki/Roumanie" },    
    { "Royaume-Uni", "http://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Flag_of_the_United_Kingdom.svg/100px-Flag_of_the_United_Kingdom.svg.png", "http://fr.wikipedia.org/wiki/Royaume-Uni" },    
    { "Russie", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Flag_of_Russia.svg/100px-Flag_of_Russia.svg.png", "http://fr.wikipedia.org/wiki/Russie" },    
    { "Rwanda", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Flag_of_Rwanda.svg/100px-Flag_of_Rwanda.svg.png", "http://fr.wikipedia.org/wiki/Rwanda" },    
    { "Saint-Christophe-et-Niévès", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Saint_Kitts_and_Nevis.svg/100px-Flag_of_Saint_Kitts_and_Nevis.svg.png", "http://fr.wikipedia.org/wiki/Saint-Christophe-et-Ni%C3%A9v%C3%A8s" },    
    { "Saint-Marin", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Flag_of_San_Marino.svg/100px-Flag_of_San_Marino.svg.png", "http://fr.wikipedia.org/wiki/Saint-Marin" },    
    { "Saint-Vincent-et-les Grenadines", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Flag_of_Saint_Vincent_and_the_Grenadines.svg/100px-Flag_of_Saint_Vincent_and_the_Grenadines.svg.png", "http://fr.wikipedia.org/wiki/Saint-Vincent-et-les_Grenadines" },    
    { "Sainte-Lucie", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Flag_of_Saint_Lucia.svg/100px-Flag_of_Saint_Lucia.svg.png", "http://fr.wikipedia.org/wiki/Sainte-Lucie" },    
    { "Salomon (Îles)", "http://upload.wikimedia.org/wikipedia/commons/thumb/7/74/Flag_of_the_Solomon_Islands.svg/100px-Flag_of_the_Solomon_Islands.svg.png", "http://fr.wikipedia.org/wiki/Salomon_%28pays%29" },    
    { "Salvador", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/34/Flag_of_El_Salvador.svg/100px-Flag_of_El_Salvador.svg.png", "http://fr.wikipedia.org/wiki/Salvador" },    
    { "Samoa", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Flag_of_Samoa.svg/21px-Flag_of_Samoa.svg.png", "http://fr.wikipedia.org/wiki/Samoa" },    
    { "São Tomé-et-Principe", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/4f/Flag_of_Sao_Tome_and_Principe.svg/100px-Flag_of_Sao_Tome_and_Principe.svg.png", "http://fr.wikipedia.org/wiki/Sao_Tom%C3%A9-et-Principe" },    
    { "Sénégal", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/fd/Flag_of_Senegal.svg/100px-Flag_of_Senegal.svg.png", "http://fr.wikipedia.org/wiki/S%C3%A9n%C3%A9gal" },    
    { "Serbie", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/ff/Flag_of_Serbia.svg/100px-Flag_of_Serbia.svg.png", "http://fr.wikipedia.org/wiki/Serbie" },    
    { "Seychelles", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Flag_of_the_Seychelles.svg/100px-Flag_of_the_Seychelles.svg.png", "http://fr.wikipedia.org/wiki/Seychelles" },    
    { "Sierra Leone", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Flag_of_Sierra_Leone.svg/100px-Flag_of_Sierra_Leone.svg.png", "http://fr.wikipedia.org/wiki/Sierra_Leone" },    
    { "Singapour", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Flag_of_Singapore.svg/100px-Flag_of_Singapore.svg.png", "http://fr.wikipedia.org/wiki/Singapour" },    
    { "Slovaquie", "http://upload.wikimedia.org/wikipedia/commons/thumb/e/e6/Flag_of_Slovakia.svg/100px-Flag_of_Slovakia.svg.png", "http://fr.wikipedia.org/wiki/Slovaquie" },    
    { "Slovénie", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/f0/Flag_of_Slovenia.svg/100px-Flag_of_Slovenia.svg.png", "http://fr.wikipedia.org/wiki/Slov%C3%A9nie" },    
    { "Somalie", "http://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Flag_of_Somalia.svg/100px-Flag_of_Somalia.svg.png", "http://fr.wikipedia.org/wiki/Somalie" },    
    { "Soudan", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Flag_of_Sudan.svg/100px-Flag_of_Sudan.svg.png", "http://fr.wikipedia.org/wiki/Soudan" },    
    { "Sri Lanka", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Flag_of_Sri_Lanka.svg/100px-Flag_of_Sri_Lanka.svg.png", "http://fr.wikipedia.org/wiki/Sri_Lanka" },    
    { "Suède", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Flag_of_Sweden.svg/100px-Flag_of_Sweden.svg.png", "http://fr.wikipedia.org/wiki/Su%C3%A8de" },    
    { "Suisse", "", "http://fr.wikipedia.org/wiki/Suisse" },    
    { "Suriname", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Flag_of_Suriname.svg/100px-Flag_of_Suriname.svg.png", "http://fr.wikipedia.org/wiki/Suriname" },    
    { "Swaziland", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/Flag_of_Swaziland.svg/100px-Flag_of_Swaziland.svg.png", "http://fr.wikipedia.org/wiki/Swaziland" },    
    { "Syrie", "http://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Flag_of_Syria.svg/100px-Flag_of_Syria.svg.png", "http://fr.wikipedia.org/wiki/Syrie" },    
    { "Tadjikistan", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Flag_of_Tajikistan.svg/100px-Flag_of_Tajikistan.svg.png", "http://fr.wikipedia.org/wiki/Tadjikistan" },    
    { "Tanzanie", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Flag_of_Tanzania.svg/100px-Flag_of_Tanzania.svg.png", "http://fr.wikipedia.org/wiki/Tanzanie" },    
    { "Tchad", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Flag_of_Chad.svg/100px-Flag_of_Chad.svg.png", "http://fr.wikipedia.org/wiki/Tchad" },    
    { "Tchèquie", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Flag_of_the_Czech_Republic.svg/100px-Flag_of_the_Czech_Republic.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_tch%C3%A8que" },    
    { "Thaïlande", "http://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Flag_of_Thailand.svg/100px-Flag_of_Thailand.svg.png", "http://fr.wikipedia.org/wiki/Tha%C3%AFlande" },    
    { "Timor oriental", "http://upload.wikimedia.org/wikipedia/commons/thumb/2/26/Flag_of_East_Timor.svg/100px-Flag_of_East_Timor.svg.png", "http://fr.wikipedia.org/wiki/Timor_oriental" },    
    { "Togo", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Flag_of_Togo.svg/100px-Flag_of_Togo.svg.png", "http://fr.wikipedia.org/wiki/Togo" },    
    { "Tonga", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/Flag_of_Tonga.svg/100px-Flag_of_Tonga.svg.png", "http://fr.wikipedia.org/wiki/Tonga" },    
    { "Trinité-et-Tobago", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/64/Flag_of_Trinidad_and_Tobago.svg/100px-Flag_of_Trinidad_and_Tobago.svg.png", "http://fr.wikipedia.org/wiki/Trinit%C3%A9-et-Tobago" },    
    { "Tunisie", "http://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/Flag_of_Tunisia.svg/100px-Flag_of_Tunisia.svg.png", "http://fr.wikipedia.org/wiki/Tunisie" },    
    { "Turkménistan", "http://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Flag_of_Turkmenistan.svg/100px-Flag_of_Turkmenistan.svg.png", "http://fr.wikipedia.org/wiki/Turkm%C3%A9nistan" },    
    { "Turquie", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/b4/Flag_of_Turkey.svg/100px-Flag_of_Turkey.svg.png", "http://fr.wikipedia.org/wiki/Turquie" },    
    { "Tuvalu", "http://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Flag_of_Tuvalu.svg/100px-Flag_of_Tuvalu.svg.png", "http://fr.wikipedia.org/wiki/Tuvalu" },    
    { "Ukraine", "http://upload.wikimedia.org/wikipedia/commons/thumb/4/49/Flag_of_Ukraine.svg/100px-Flag_of_Ukraine.svg.png", "http://fr.wikipedia.org/wiki/Ukraine" },    
    { "Uruguay", "http://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Flag_of_Uruguay.svg/100px-Flag_of_Uruguay.svg.png", "http://fr.wikipedia.org/wiki/Uruguay" },    
    { "Vanuatu", "http://upload.wikimedia.org/wikipedia/commons/thumb/b/bc/Flag_of_Vanuatu.svg/100px-Flag_of_Vanuatu.svg.png", "http://fr.wikipedia.org/wiki/Vanuatu" },    
    { "Vatican", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/00/Flag_of_the_Vatican_City.svg/18px-Flag_of_the_Vatican_City.svg.png", "http://fr.wikipedia.org/wiki/Vatican" },    
    { "Venezuela", "http://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Flag_of_Venezuela_%28state%29.svg/100px-Flag_of_Venezuela_%28state%29.svg.png", "http://fr.wikipedia.org/wiki/Venezuela" },    
    { "Viêt Nam", "http://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Flag_of_Vietnam.svg/100px-Flag_of_Vietnam.svg.png", "http://fr.wikipedia.org/wiki/Vi%C3%AAt_Nam" },    
    { "Yémen", "http://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Flag_of_Yemen.svg/100px-Flag_of_Yemen.svg.png", "http://fr.wikipedia.org/wiki/Y%C3%A9men" },    
    { "Zambie", "http://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Flag_of_Zambia.svg/100px-Flag_of_Zambia.svg.png", "http://fr.wikipedia.org/wiki/Zambie" },    
    { "Zimbabwe", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6a/Flag_of_Zimbabwe.svg/100px-Flag_of_Zimbabwe.svg.png", "http://fr.wikipedia.org/wiki/Zimbabwe" }
  };  

  //
  // This defines the javascool embedded
  //

  /** Renvoie le panel affiché. */
  static JPanel getPanel() { return panel; } 
  
  private static Panel panel = new Panel();
}
