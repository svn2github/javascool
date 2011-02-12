import static org.javascool.Macros.*;import static java.lang.Math.*;import static proglet.ingredients.Console.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class Exemple extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 3L;  static { org.javascool.Jvs2Java.runnable = new Exemple(); }  public void run() { main(); }    
    // Programme exemple d'usage de la proglet
    
    // Génération d'un parterre de cinq spots Nord Sud Est Ouest Centre
    String spots[] = {"Nord", "Sud",  "Est", "Ouest", "Centre"};
    
    void build() {
      EnVoiture.addSpot("Nord",   0x000000, 'B', 0, 1000,  20, 20, 20);
      EnVoiture.addSpot("Est",    0xff00ff, 'P', 1000, 0,  20, 10, 20);
      EnVoiture.addSpot("Ouest",  0xffff00, 'O', -1000, 0, 20, 20, 20);
      EnVoiture.addSpot("Sud",    0x00ffff, 'C', 0, 1000,  20, 30, 20);
      EnVoiture.addSpot("Centre", 0xffffff, 'C', 0, 0,     20, 10, 20);
      EnVoiture.addLink("Nord", "Est");
      EnVoiture.addLink("Sud", "Est");
      EnVoiture.addLink("Nord", "Ouest");
      EnVoiture.addLink("Sud", "Ouest");
      EnVoiture.addLink("Sud", "Centre");
      EnVoiture.addLink("Nord", "Centre");
    }
    
    // Affichage des connections
    
    void dump(String what) {
      println("Etat du graphe "+what);
      for (String spot1 : spots) 
        for (String spot2 : spots)
          if (EnVoiture.isLink(spot1, spot2))
    	println("Les spots "+spot1+" et "+spot2+" sont connectés");
    }
    
    void main() {
      // Parterre initial
      build();
      dump("initial");
      println("Le spot le plus prêt de (10, 10) est le "+EnVoiture.getClosestSpot(10, 10));
      println("Le spot le plus prêt de (50, 50) est le "+EnVoiture.getClosestSpot(50, 50));
      sleep(5);
      // On détruit un spot et un lien
      EnVoiture.removeSpot("Centre");
      EnVoiture.removeLink("Nord", "Ouest");
      dump("amputé");
    }
}