/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet.dichotomie;

import org.javascool.Utils;
import org.javascool.Macros;

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

/** Définit une proglet javascool qui permet d'expérimenter la recherche dichotomique.
 * @see <a href="doc-files/about-proglet.htm">Description</a>
 * @see <a href="doc-files/the-proglet.htm">La proglet</a>
 * @see <a href="Dichotomie.java.html">code source</a>
 * @serial exclude
 */
public class Dichotomie implements org.javascool.Proglet { private Dichotomie() { }
  private static final long serialVersionUID = 1L;

  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;

    public Panel() {
      super(new BorderLayout()); setBackground(Color.WHITE);
      // Adds the background icon
      JLayeredPane book = new JLayeredPane(); book.setPreferredSize(new Dimension(540, 350)); add(book);
      JLabel icon = new JLabel(); icon.setBounds(10, 0, 540, 350); 
      icon.setIcon(Utils.getIcon("proglet/dichotomie/doc-files/dicho_background.png")); 
      book.add(icon, new Integer(1), 0);
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
      flag.setIcon(Utils.getIcon("proglet/dichotomie/doc-files/"+dicho[page][1]));
    } 
    private JLabel name, flag, num; private int current;
  }

  //
  // This defines the tests on the panel
  //

  /**/public static void test() {
    // Tests if the dico is sorted
    for(int i = 1; i < dicho.length; i++)
      if (compare(dicho[i][0], i - 1) <= 0)
	Macros.echo("Ahhh bad sort between "+dicho[i][0]+"#"+i+(compare(dicho[i][0], i - 1) == 0 ? " == " : " << ")+dicho[i-1][0]+"#"+(i-1));
    /* Tests the index function
    for(int i = 0; i < dicho.length; i++)
      if (i != getPage(dicho[i][0]))
	Macros.echo("Ohhh bad index for "+dicho[i][0]+"#"+i+" <> "+getPage(dicho[i][0]));
    */
    // Shows a few random pages
    for(int i = 0; i < 10; i++) {
      Macros.sleep(500);
      panel.show(((int) (Math.random() * dicho.length)));
    }
  }

  /** Gets the index of a given page.
   * <div><tt>- DO NOT USE !!! This is the solution of the excercice !!!</tt></div>
   * @param name The name to compare with.
   * @return The page index or -1 if the name is not on some page.
   */
  private static int getPage(String pays) {
    int debut = 0, fin = length();
    while(true) {
      int milieu = (debut + fin) / 2;
      int c = compareTo(pays, milieu);
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

  /** Ouvre le libre à une page et compare un nom au nom affiché sur cette page.
   * @param name Le nom à comparer.
   * @param page L'index de la page, de 0 à length() exclu.
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

  // All the data sorted in alphabetic order, flags icons are available thanks to http://fr.wikipedia.org !
  private static String dicho[][] = {
    { "Afghanistan", "100px-Flag_of_Afghanistan.svg.png", "http://fr.wikipedia.org/wiki/Afghanistan" },  
    { "Afrique du Sud", "100px-Flag_of_South_Africa.svg.png", "http://fr.wikipedia.org/wiki/Afrique_du_Sud" },  
    { "Albanie", "100px-Flag_of_Albania.svg.png", "http://fr.wikipedia.org/wiki/Albanie" },  
    { "Algérie", "100px-Flag_of_Algeria.svg.png", "http://fr.wikipedia.org/wiki/Alg%C3%A9rie" },     
    { "Allemagne", "100px-Flag_of_Germany.svg.png", "http://fr.wikipedia.org/wiki/Allemagne" },  
    { "Andorre", "100px-Flag_of_Andorra.svg.png", "http://fr.wikipedia.org/wiki/Andorre" },  
    { "Angola", "100px-Flag_of_Angola.svg.png", "http://fr.wikipedia.org/wiki/Angola" },      
    { "Antigua", "100px-Flag_of_Antigua_and_Barbuda.svg.png", "http://fr.wikipedia.org/wiki/Antigua-et-Barbuda" },    
    { "Arabie Saoudite", "100px-Flag_of_Saudi_Arabia.svg.png", "http://fr.wikipedia.org/wiki/Arabie_saoudite" },    
    { "Argentine", "100px-Flag_of_Argentina.svg.png", "http://fr.wikipedia.org/wiki/Argentine" },    
    { "Arménie", "100px-Flag_of_Armenia.svg.png", "http://fr.wikipedia.org/wiki/Arm%C3%A9nie" },    
    { "Australie", "100px-Flag_of_Australia.svg.png", "http://fr.wikipedia.org/wiki/Australie" },    
    { "Autriche", "100px-Flag_of_Austria.svg.png", "http://fr.wikipedia.org/wiki/Autriche" },    
    { "Azerbaïdjan", "100px-Flag_of_Azerbaijan.svg.png", "http://fr.wikipedia.org/wiki/Azerba%C3%AFdjan" },    
    { "Bahamas", "100px-Flag_of_the_Bahamas.svg.png", "http://fr.wikipedia.org/wiki/Bahamas" },    
    { "Bahreïn", "100px-Flag_of_Bahrain.svg.png", "http://fr.wikipedia.org/wiki/Bahre%C3%AFn" },    
    { "Bangladesh", "100px-Flag_of_Bangladesh.svg.png", "http://fr.wikipedia.org/wiki/Bangladesh" },    
    { "Barbade", "100px-Flag_of_Barbados.svg.png", "http://fr.wikipedia.org/wiki/Barbade" },    
    { "Belgique", "100px-Flag_of_Belgium_%28civil%29.svg.png", "http://fr.wikipedia.org/wiki/Belgique" },    
    { "Belize", "100px-Flag_of_Belize.svg.png", "http://fr.wikipedia.org/wiki/Belize" },    
    { "Bénin", "100px-Flag_of_Benin.svg.png", "http://fr.wikipedia.org/wiki/B%C3%A9nin" },    
    { "Bhoutan", "100px-Flag_of_Bhutan.svg.png", "http://fr.wikipedia.org/wiki/Bhoutan" },    
    { "Biélorussie", "100px-Flag_of_Belarus.svg.png", "http://fr.wikipedia.org/wiki/Bi%C3%A9lorussie" },    
    { "Bolivie", "100px-Flag_of_Bolivia_%28state%29.svg.png", "http://fr.wikipedia.org/wiki/Bolivie" },    
    { "Bosnie-Herzégovine", "100px-Flag_of_Bosnia_and_Herzegovina.svg.png", "http://fr.wikipedia.org/wiki/Bosnie-Herz%C3%A9govine" },    
    { "Botswana", "100px-Flag_of_Botswana.svg.png", "http://fr.wikipedia.org/wiki/Botswana" },    
    { "Brésil", "100px-Flag_of_Brazil.svg.png", "http://fr.wikipedia.org/wiki/Br%C3%A9sil" },    
    { "Brunei", "100px-Flag_of_Brunei.svg.png", "http://fr.wikipedia.org/wiki/Brunei" },    
    { "Bulgarie", "100px-Flag_of_Bulgaria.svg.png", "http://fr.wikipedia.org/wiki/Bulgarie" },    
    { "Burkina Faso", "100px-Flag_of_Burkina_Faso.svg.png", "http://fr.wikipedia.org/wiki/Burkina" },    
    { "Burundi", "100px-Flag_of_Burundi.svg.png", "http://fr.wikipedia.org/wiki/Burundi" },    
    { "Cambodge", "100px-Flag_of_Cambodia.svg.png", "http://fr.wikipedia.org/wiki/Cambodge" },    
    { "Cameroun", "100px-Flag_of_Cameroon.svg.png", "http://fr.wikipedia.org/wiki/Cameroun" },    
    { "Canada", "100px-Flag_of_Canada.svg.png", "http://fr.wikipedia.org/wiki/Canada" },    
    { "Cap-Vert", "100px-Flag_of_Cape_Verde.svg.png", "http://fr.wikipedia.org/wiki/Cap-Vert" },    
    { "Centrafrique", "100px-Flag_of_the_Central_African_Republic.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_centrafricaine" },    
    { "Chili", "100px-Flag_of_Chile.svg.png", "http://fr.wikipedia.org/wiki/Chili" },    
    { "Chine", "100px-Flag_of_the_People%27s_Republic_of_China.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_populaire_de_Chine" },    
    { "Chypre", "100px-Flag_of_Cyprus.svg.png", "http://fr.wikipedia.org/wiki/Chypre_%28pays%29" },    
    { "Colombie", "100px-Flag_of_Colombia.svg.png", "http://fr.wikipedia.org/wiki/Colombie" },    
    { "Comores", "100px-Flag_of_the_Comoros.svg.png", "http://fr.wikipedia.org/wiki/Union_des_Comores" },    
    { "Congo", "100px-Flag_of_the_Democratic_Republic_of_the_Congo.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_d%C3%A9mocratique_du_Congo" },    
    { "Congo-Brazzaville", "100px-Flag_of_the_Republic_of_the_Congo.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_du_Congo" },    
    { "Corée du Nord", "100px-Flag_of_North_Korea.svg.png", "http://fr.wikipedia.org/wiki/Cor%C3%A9e_du_Nord" },    
    { "Corée du Sud", "100px-Flag_of_South_Korea.svg.png", "http://fr.wikipedia.org/wiki/Cor%C3%A9e_du_Sud" },    
    { "Costa Rica", "100px-Flag_of_Costa_Rica.svg.png", "http://fr.wikipedia.org/wiki/Costa_Rica" },    
    { "Côte d'Ivoire", "100px-Flag_of_Cote_d%27Ivoire.svg.png", "http://fr.wikipedia.org/wiki/C%C3%B4te_d%27Ivoire" },    
    { "Croatie", "100px-Flag_of_Croatia.svg.png", "http://fr.wikipedia.org/wiki/Croatie" },    
    { "Cuba", "100px-Flag_of_Cuba.svg.png", "http://fr.wikipedia.org/wiki/Cuba" },    
    { "Danemark", "100px-Flag_of_Denmark.svg.png", "http://fr.wikipedia.org/wiki/Danemark" },    
    { "Djibouti", "100px-Flag_of_Djibouti.svg.png", "http://fr.wikipedia.org/wiki/Djibouti" },    
    { "Dominicaine (république)", "100px-Flag_of_the_Dominican_Republic.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_dominicaine" },    
    { "Dominique", "100px-Flag_of_Dominica.svg.png", "http://fr.wikipedia.org/wiki/Dominique_%28pays%29" },    
    { "Égypte", "100px-Flag_of_Egypt.svg.png", "http://fr.wikipedia.org/wiki/%C3%89gypte" },    
    { "Émirats arabes unis", "100px-Flag_of_the_United_Arab_Emirates.svg.png", "http://fr.wikipedia.org/wiki/%C3%89mirats_arabes_unis" },    
    { "Équateur", "100px-Flag_of_Ecuador.svg.png", "http://fr.wikipedia.org/wiki/%C3%89quateur_%28pays%29" },    
    { "Érythrée", "100px-Flag_of_Eritrea.svg.png", "http://fr.wikipedia.org/wiki/%C3%89rythr%C3%A9e" },    
    { "Espagne", "100px-Flag_of_Spain.svg.png", "http://fr.wikipedia.org/wiki/Espagne" },    
    { "Estonie", "100px-Flag_of_Estonia.svg.png", "http://fr.wikipedia.org/wiki/Estonie" },    
    { "États-Unis", "100px-Flag_of_the_United_States.svg.png", "http://fr.wikipedia.org/wiki/%C3%89tats-Unis" },    
    { "Éthiopie", "100px-Flag_of_Ethiopia.svg.png", "http://fr.wikipedia.org/wiki/%C3%89thiopie" },    
    { "Fidji", "100px-Flag_of_Fiji.svg.png", "http://fr.wikipedia.org/wiki/Fidji" },    
    { "Finlande", "100px-Flag_of_Finland.svg.png", "http://fr.wikipedia.org/wiki/Finlande" },    
    { "France", "100px-Flag_of_France.svg.png", "http://fr.wikipedia.org/wiki/France" },    
    { "Gabon", "100px-Flag_of_Gabon.svg.png", "http://fr.wikipedia.org/wiki/Gabon" },    
    { "Gambie", "100px-Flag_of_The_Gambia.svg.png", "http://fr.wikipedia.org/wiki/Gambie" },    
    { "Géorgie", "100px-Flag_of_Georgia.svg.png", "http://fr.wikipedia.org/wiki/G%C3%A9orgie_%28pays%29" },    
    { "Ghana", "100px-Flag_of_Ghana.svg.png", "http://fr.wikipedia.org/wiki/Ghana" },    
    { "Grèce", "100px-Flag_of_Greece.svg.png", "http://fr.wikipedia.org/wiki/Gr%C3%A8ce" },    
    { "Grenade", "100px-Flag_of_Grenada.svg.png", "http://fr.wikipedia.org/wiki/Grenade_%28pays%29" },    
    { "Guatemala", "100px-Flag_of_Guatemala.svg.png", "http://fr.wikipedia.org/wiki/Guatemala" },    
    { "Guinée", "100px-Flag_of_Guinea.svg.png", "http://fr.wikipedia.org/wiki/Guin%C3%A9e" },    
    { "Guinée équatoriale", "100px-Flag_of_Equatorial_Guinea.svg.png", "http://fr.wikipedia.org/wiki/Guin%C3%A9e_%C3%A9quatoriale" },    
    { "Guinée-Bissau", "100px-Flag_of_Guinea-Bissau.svg.png", "http://fr.wikipedia.org/wiki/Guin%C3%A9e-Bissau" },    
    { "Guyana", "100px-Flag_of_Guyana.svg.png", "http://fr.wikipedia.org/wiki/Guyana" },    
    { "Haïti", "100px-Flag_of_Haiti.svg.png", "http://fr.wikipedia.org/wiki/Ha%C3%AFti" },    
    { "Honduras", "100px-Flag_of_Honduras.svg.png", "http://fr.wikipedia.org/wiki/Honduras" },    
    { "Hongrie", "100px-Flag_of_Hungary.svg.png", "http://fr.wikipedia.org/wiki/Hongrie" },    
    { "Inde", "100px-Flag_of_India.svg.png", "http://fr.wikipedia.org/wiki/Inde" },    
    { "Indonésie", "100px-Flag_of_Indonesia.svg.png", "http://fr.wikipedia.org/wiki/Indon%C3%A9sie" },    
    { "Irak", "100px-Flag_of_Iraq.svg.png", "http://fr.wikipedia.org/wiki/Irak" },    
    { "Iran", "100px-Flag_of_Iran.svg.png", "http://fr.wikipedia.org/wiki/Iran" },    
    { "Irlande", "100px-Flag_of_Ireland.svg.png", "http://fr.wikipedia.org/wiki/Irlande_%28pays%29" },    
    { "Islande", "100px-Flag_of_Iceland.svg.png", "http://fr.wikipedia.org/wiki/Islande" },    
    { "Israël", "100px-Flag_of_Israel.svg.png", "http://fr.wikipedia.org/wiki/Isra%C3%ABl" },    
    { "Italie", "100px-Flag_of_Italy.svg.png", "http://fr.wikipedia.org/wiki/Italie" },    
    { "Jamaïque", "100px-Flag_of_Jamaica.svg.png", "http://fr.wikipedia.org/wiki/Jama%C3%AFque" },    
    { "Japon", "100px-Flag_of_Japan.svg.png", "http://fr.wikipedia.org/wiki/Japon" },    
    { "Jordanie", "100px-Flag_of_Jordan.svg.png", "http://fr.wikipedia.org/wiki/Jordanie" },    
    { "Kazakhstan", "100px-Flag_of_Kazakhstan.svg.png", "http://fr.wikipedia.org/wiki/Kazakhstan" },    
    { "Kenya", "100px-Flag_of_Kenya.svg.png", "http://fr.wikipedia.org/wiki/Kenya" },    
    { "Kirghizistan", "100px-Flag_of_Kyrgyzstan.svg.png", "http://fr.wikipedia.org/wiki/Kirghizie" },    
    { "Kiribati", "100px-Flag_of_Kiribati.svg.png", "http://fr.wikipedia.org/wiki/Kiribati" },    
    { "Koweït", "100px-Flag_of_Kuwait.svg.png", "http://fr.wikipedia.org/wiki/Kowe%C3%AFt" },    
    { "Laos", "100px-Flag_of_Laos.svg.png", "http://fr.wikipedia.org/wiki/Laos" },    
    { "Lesotho", "100px-Flag_of_Lesotho.svg.png", "http://fr.wikipedia.org/wiki/Lesotho" },    
    { "Lettonie", "100px-Flag_of_Latvia.svg.png", "http://fr.wikipedia.org/wiki/Lettonie" },    
    { "Liban", "100px-Flag_of_Lebanon.svg.png", "http://fr.wikipedia.org/wiki/Liban" },    
    { "Libéria", "100px-Flag_of_Liberia.svg.png", "http://fr.wikipedia.org/wiki/Liberia" },    
    { "Libye", "100px-Flag_of_Libya.svg.png", "http://fr.wikipedia.org/wiki/Libye" },    
    { "Liechtenstein", "100px-Flag_of_Liechtenstein.svg.png", "http://fr.wikipedia.org/wiki/Liechtenstein" },    
    { "Lituanie", "100px-Flag_of_Lithuania.svg.png", "http://fr.wikipedia.org/wiki/Lituanie" },    
    { "Luxembourg", "100px-Flag_of_Luxembourg.svg.png", "http://fr.wikipedia.org/wiki/Luxembourg_%28pays%29" },    
    { "Macédoine", "100px-Flag_of_Macedonia.svg.png", "http://fr.wikipedia.org/wiki/Mac%C3%A9doine_%28pays%29" },    
    { "Madagascar", "100px-Flag_of_Madagascar.svg.png", "http://fr.wikipedia.org/wiki/Madagascar" },    
    { "Malaisie", "100px-Flag_of_Malaysia.svg.png", "http://fr.wikipedia.org/wiki/Malaisie" },    
    { "Malawi", "100px-Flag_of_Malawi.svg.png", "http://fr.wikipedia.org/wiki/Malawi" },    
    { "Maldives", "100px-Flag_of_Maldives.svg.png", "http://fr.wikipedia.org/wiki/Maldives" },    
    { "Mali", "100px-Flag_of_Mali.svg.png", "http://fr.wikipedia.org/wiki/Mali" },    
    { "Malte", "100px-Flag_of_Malta.svg.png", "http://fr.wikipedia.org/wiki/Malte" },    
    { "Maroc", "100px-Flag_of_Morocco.svg.png", "http://fr.wikipedia.org/wiki/Maroc" },    
    { "Marshall (Îles)", "100px-Flag_of_the_Marshall_Islands.svg.png", "http://fr.wikipedia.org/wiki/Marshall_%28pays%29" },    
    { "Maurice", "100px-Flag_of_Mauritius.svg.png", "http://fr.wikipedia.org/wiki/Maurice_%28pays%29" },    
    { "Mauritanie", "100px-Flag_of_Mauritania.svg.png", "http://fr.wikipedia.org/wiki/Mauritanie" },    
    { "Mexique", "100px-Flag_of_Mexico.svg.png", "http://fr.wikipedia.org/wiki/Mexique" },    
    { "Micronésie", "100px-Flag_of_Federated_States_of_Micronesia.svg.png", "http://fr.wikipedia.org/wiki/Micron%C3%A9sie_%28pays%29" },    
    { "Moldavie", "100px-Flag_of_Moldova.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_de_Moldavie" },    
    { "Monaco", "100px-Flag_of_Monaco.svg.png", "http://fr.wikipedia.org/wiki/Monaco" },    
    { "Mongolie", "100px-Flag_of_Mongolia.svg.png", "http://fr.wikipedia.org/wiki/Mongolie" },    
    { "Monténégro", "100px-Flag_of_Montenegro.svg.png", "http://fr.wikipedia.org/wiki/Mont%C3%A9n%C3%A9gro" },    
    { "Mozambique", "100px-Flag_of_Mozambique.svg.png", "http://fr.wikipedia.org/wiki/Mozambique" },    
    { "Myanmar", "100px-Flag_of_Myanmar.svg.png", "http://fr.wikipedia.org/wiki/Birmanie" },    
    { "Namibie", "100px-Flag_of_Namibia.svg.png", "http://fr.wikipedia.org/wiki/Namibie" },    
    { "Nauru", "100px-Flag_of_Nauru.svg.png", "http://fr.wikipedia.org/wiki/Nauru" },    
    { "Népal", "100px-Flag_of_Nepal.svg.png", "http://fr.wikipedia.org/wiki/N%C3%A9pal" },    
    { "Nicaragua", "100px-Flag_of_Nicaragua.svg.png", "http://fr.wikipedia.org/wiki/Nicaragua" },    
    { "Niger", "100px-Flag_of_Niger.svg.png", "http://fr.wikipedia.org/wiki/Niger" },    
    { "Nigeria", "100px-Flag_of_Nigeria.svg.png", "http://fr.wikipedia.org/wiki/Nigeria" },    
    { "Norvège", "100px-Flag_of_Norway.svg.png", "http://fr.wikipedia.org/wiki/Norv%C3%A8ge" },    
    { "Nouvelle-Zélande", "100px-Flag_of_New_Zealand.svg.png", "http://fr.wikipedia.org/wiki/Nouvelle-Z%C3%A9lande" },    
    { "Oman", "100px-Flag_of_Oman.svg.png", "http://fr.wikipedia.org/wiki/Oman" },    
    { "Ouganda", "100px-Flag_of_Uganda.svg.png", "http://fr.wikipedia.org/wiki/Ouganda" },    
    { "Ouzbékistan", "100px-Flag_of_Uzbekistan.svg.png", "http://fr.wikipedia.org/wiki/Ouzb%C3%A9kistan" },    
    { "Pakistan", "100px-Flag_of_Pakistan.svg.png", "http://fr.wikipedia.org/wiki/Pakistan" },    
    { "Palaos", "100px-Flag_of_Palau.svg.png", "http://fr.wikipedia.org/wiki/Palaos" },    
    { "Panamà", "100px-Flag_of_Panama.svg.png", "http://fr.wikipedia.org/wiki/Panama" },    
    { "Papouasie-Nouvelle-Guinée", "100px-Flag_of_Papua_New_Guinea.svg.png", "http://fr.wikipedia.org/wiki/Papouasie-Nouvelle-Guin%C3%A9e" },    
    { "Paraguay", "100px-Flag_of_Paraguay.svg.png", "http://fr.wikipedia.org/wiki/Paraguay" },    
    { "Pays-Bas", "100px-Flag_of_the_Netherlands.svg.png", "http://fr.wikipedia.org/wiki/Pays-Bas" },    
    { "Pérou", "100px-Flag_of_Peru.svg.png", "http://fr.wikipedia.org/wiki/P%C3%A9rou" },    
    { "Philippines", "100px-Flag_of_the_Philippines.svg.png", "http://fr.wikipedia.org/wiki/Philippines" },    
    { "Pologne", "100px-Flag_of_Poland.svg.png", "http://fr.wikipedia.org/wiki/Pologne" },    
    { "Portugal", "100px-Flag_of_Portugal.svg.png", "http://fr.wikipedia.org/wiki/Portugal" },    
    { "Qatar", "100px-Flag_of_Qatar.svg.png", "http://fr.wikipedia.org/wiki/Qatar" },    
    { "Roumanie", "100px-Flag_of_Romania.svg.png", "http://fr.wikipedia.org/wiki/Roumanie" },    
    { "Royaume-Uni", "100px-Flag_of_the_United_Kingdom.svg.png", "http://fr.wikipedia.org/wiki/Royaume-Uni" },    
    { "Russie", "100px-Flag_of_Russia.svg.png", "http://fr.wikipedia.org/wiki/Russie" },    
    { "Rwanda", "100px-Flag_of_Rwanda.svg.png", "http://fr.wikipedia.org/wiki/Rwanda" },    
    { "Saint-Christophe-et-Niévès", "100px-Flag_of_Saint_Kitts_and_Nevis.svg.png", "http://fr.wikipedia.org/wiki/Saint-Christophe-et-Ni%C3%A9v%C3%A8s" },    
    { "Saint-Marin", "100px-Flag_of_San_Marino.svg.png", "http://fr.wikipedia.org/wiki/Saint-Marin" },    
    { "Saint-Vincent-et-les Grenadines", "100px-Flag_of_Saint_Vincent_and_the_Grenadines.svg.png", "http://fr.wikipedia.org/wiki/Saint-Vincent-et-les_Grenadines" },    
    { "Sainte-Lucie", "100px-Flag_of_Saint_Lucia.svg.png", "http://fr.wikipedia.org/wiki/Sainte-Lucie" },    
    { "Salomon (Îles)", "100px-Flag_of_the_Solomon_Islands.svg.png", "http://fr.wikipedia.org/wiki/Salomon_%28pays%29" },    
    { "Salvador", "100px-Flag_of_El_Salvador.svg.png", "http://fr.wikipedia.org/wiki/Salvador" },    
    { "Samoa", "100px-Flag_of_Samoa.svg.png", "http://fr.wikipedia.org/wiki/Samoa" },    
    { "São Tomé-et-Principe", "100px-Flag_of_Sao_Tome_and_Principe.svg.png", "http://fr.wikipedia.org/wiki/Sao_Tom%C3%A9-et-Principe" },    
    { "Sénégal", "100px-Flag_of_Senegal.svg.png", "http://fr.wikipedia.org/wiki/S%C3%A9n%C3%A9gal" },    
    { "Serbie", "100px-Flag_of_Serbia.svg.png", "http://fr.wikipedia.org/wiki/Serbie" },    
    { "Seychelles", "100px-Flag_of_the_Seychelles.svg.png", "http://fr.wikipedia.org/wiki/Seychelles" },    
    { "Sierra Leone", "100px-Flag_of_Sierra_Leone.svg.png", "http://fr.wikipedia.org/wiki/Sierra_Leone" },    
    { "Singapour", "100px-Flag_of_Singapore.svg.png", "http://fr.wikipedia.org/wiki/Singapour" },    
    { "Slovaquie", "100px-Flag_of_Slovakia.svg.png", "http://fr.wikipedia.org/wiki/Slovaquie" },    
    { "Slovénie", "100px-Flag_of_Slovenia.svg.png", "http://fr.wikipedia.org/wiki/Slov%C3%A9nie" },    
    { "Somalie", "100px-Flag_of_Somalia.svg.png", "http://fr.wikipedia.org/wiki/Somalie" },    
    { "Soudan", "100px-Flag_of_Sudan.svg.png", "http://fr.wikipedia.org/wiki/Soudan" },    
    { "Sri Lanka", "100px-Flag_of_Sri_Lanka.svg.png", "http://fr.wikipedia.org/wiki/Sri_Lanka" },    
    { "Suède", "100px-Flag_of_Sweden.svg.png", "http://fr.wikipedia.org/wiki/Su%C3%A8de" },    
    { "Suisse", "100px-Flag_of_Switzerland.svg.png", "http://fr.wikipedia.org/wiki/Suisse" },    
    { "Suriname", "100px-Flag_of_Suriname.svg.png", "http://fr.wikipedia.org/wiki/Suriname" },    
    { "Swaziland", "100px-Flag_of_Swaziland.svg.png", "http://fr.wikipedia.org/wiki/Swaziland" },    
    { "Syrie", "100px-Flag_of_Syria.svg.png", "http://fr.wikipedia.org/wiki/Syrie" },    
    { "Tadjikistan", "100px-Flag_of_Tajikistan.svg.png", "http://fr.wikipedia.org/wiki/Tadjikistan" },    
    { "Tanzanie", "100px-Flag_of_Tanzania.svg.png", "http://fr.wikipedia.org/wiki/Tanzanie" },    
    { "Tchad", "100px-Flag_of_Chad.svg.png", "http://fr.wikipedia.org/wiki/Tchad" },    
    { "Tchèquie", "100px-Flag_of_the_Czech_Republic.svg.png", "http://fr.wikipedia.org/wiki/R%C3%A9publique_tch%C3%A8que" },    
    { "Thaïlande", "100px-Flag_of_Thailand.svg.png", "http://fr.wikipedia.org/wiki/Tha%C3%AFlande" },    
    { "Timor oriental", "100px-Flag_of_East_Timor.svg.png", "http://fr.wikipedia.org/wiki/Timor_oriental" },    
    { "Togo", "100px-Flag_of_Togo.svg.png", "http://fr.wikipedia.org/wiki/Togo" },    
    { "Tonga", "100px-Flag_of_Tonga.svg.png", "http://fr.wikipedia.org/wiki/Tonga" },    
    { "Trinité-et-Tobago", "100px-Flag_of_Trinidad_and_Tobago.svg.png", "http://fr.wikipedia.org/wiki/Trinit%C3%A9-et-Tobago" },    
    { "Tunisie", "100px-Flag_of_Tunisia.svg.png", "http://fr.wikipedia.org/wiki/Tunisie" },    
    { "Turkménistan", "100px-Flag_of_Turkmenistan.svg.png", "http://fr.wikipedia.org/wiki/Turkm%C3%A9nistan" },    
    { "Turquie", "100px-Flag_of_Turkey.svg.png", "http://fr.wikipedia.org/wiki/Turquie" },    
    { "Tuvalu", "100px-Flag_of_Tuvalu.svg.png", "http://fr.wikipedia.org/wiki/Tuvalu" },    
    { "Ukraine", "100px-Flag_of_Ukraine.svg.png", "http://fr.wikipedia.org/wiki/Ukraine" },    
    { "Uruguay", "100px-Flag_of_Uruguay.svg.png", "http://fr.wikipedia.org/wiki/Uruguay" },    
    { "Vanuatu", "100px-Flag_of_Vanuatu.svg.png", "http://fr.wikipedia.org/wiki/Vanuatu" },    
    { "Vatican", "100px-Flag_of_the_Vatican_City.svg.png", "http://fr.wikipedia.org/wiki/Vatican" },    
    { "Venezuela", "100px-Flag_of_Venezuela_%28state%29.svg.png", "http://fr.wikipedia.org/wiki/Venezuela" },    
    { "Viêt Nam", "100px-Flag_of_Vietnam.svg.png", "http://fr.wikipedia.org/wiki/Vi%C3%AAt_Nam" },    
    { "Yémen", "100px-Flag_of_Yemen.svg.png", "http://fr.wikipedia.org/wiki/Y%C3%A9men" },    
    { "Zambie", "100px-Flag_of_Zambia.svg.png", "http://fr.wikipedia.org/wiki/Zambie" },    
    { "Zimbabwe", "100px-Flag_of_Zimbabwe.svg.png", "http://fr.wikipedia.org/wiki/Zimbabwe" }
  };  

  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}
