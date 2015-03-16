//
// Ce code est un code exemple pour utiliser une proglet hors de javascool
//
import static java.lang.Math.*;
import static org.javascool.macros.Macros.*;
import static org.javascool.macros.Stdin.*;
import static org.javascool.macros.Stdout.*;
import static org.javascool.proglets.codagePixels.Functions.*;
public class Main1 {
public static void main(String[] args) {
  println("Hello world !"); 
  // Commande qui permet d'ouvrir la proglet
  new org.javascool.widgets.MainFrame().reset("codagePixels", 600, 800, org.javascool.core.ProgletEngine.getInstance().setProglet("codagePixels").getProgletPane());

  // Programme de démo 
  { 
    load("http://javascool.gforge.inria.fr/documents/sketchbook/codagePixels/doisneaubuffon.jpg");   
    // Propose de passer en inverse vidéo
    if (readBoolean("On inverse ?")) 
      for(int j = -getHeight(); j <= getHeight(); j = j + 1)
	for(int i = -getWidth(); i <= getWidth(); i = i + 1)
	  setPixel(i, j, 255 - getPixel(i, j));
  }
 }
}
