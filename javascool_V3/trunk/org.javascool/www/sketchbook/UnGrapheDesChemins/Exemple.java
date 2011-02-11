import static org.javascool.Macros.*;import static java.lang.Math.*;import static proglet.ingredients.Console.*;import static proglet.synthesons.SoundDisplay.*;import static proglet.exosdemaths.CurveDisplay.*;import static proglet.pixelsetcie.ImageDisplay.*;import static proglet.tortuelogo.TortueLogo.*;import static proglet.javaprog.JavaPanel.*;import static proglet.convanalogique.ConvAnalogique.*;import static proglet.dichotomie.Dichotomie.*;public class Exemple extends org.javascool.ProgletApplet implements Runnable {  private static final long serialVersionUID = 4L;  static { org.javascool.Jvs2Java.runnable = new Exemple(); }  public void run() { main(); }    
    // Programme exemple d'usage de la proglet
    
    // Génération d'un parterre de cinq nodes Nord Sud Est Ouest Centre
    String nodes[] = {"Nord", "Sud",  "Est", "Ouest", "Centre"};
    
    void build() {
      int x0 = 450, y0 = 250;
      UnGrapheDesChemins.addNode("Nord",  x0, y0 - 100);
      UnGrapheDesChemins.addNode("Est",   x0 + 300, y0);
      UnGrapheDesChemins.addNode("Ouest",  x0 - 300, y0);
      UnGrapheDesChemins.addNode("Sud", x0, y0 + 100);
      UnGrapheDesChemins.addNode("Centre", x0, y0);
      UnGrapheDesChemins.addLink("Nord", "Est", 1);
      UnGrapheDesChemins.addLink("Sud", "Est", 2);
      UnGrapheDesChemins.addLink("Nord", "Ouest", 3);
      UnGrapheDesChemins.addLink("Sud", "Ouest", 4);
      UnGrapheDesChemins.addLink("Sud", "Centre", 5);
      UnGrapheDesChemins.addLink("Nord", "Centre", 6);
    }
    
    void unbuild() { 	UnGrapheDesChemins.removeNode("Nord");
    UnGrapheDesChemins.removeNode("Sud");
    UnGrapheDesChemins.removeNode("Est");
    UnGrapheDesChemins.removeNode("Ouest");
    UnGrapheDesChemins.removeNode("Centre");
    }
    
    // Affichage des connections
    
    void dump(String what) {
      println("Etat du graphe "+what);
      for (String node1 : nodes) 
        for (String node2 : nodes)
          if (UnGrapheDesChemins.isLink(node1, node2))
    	println("Les nodes "+node1+" et "+node2+" sont connectés avec un poids de "+UnGrapheDesChemins.getLink(node1, node2));
    }
    
    void main() {
       unbuild();
       sleep(5000);
      // Parterre initial
      build();
      dump("initial");
      println("Le node le plus prêt de (10, 10) est le "+UnGrapheDesChemins.getClosestNode(10, 10));
      println("Le node le plus prêt de (50, 50) est le "+UnGrapheDesChemins.getClosestNode(50, 50));
      sleep(5000);
      // On détruit un node et un lien
      UnGrapheDesChemins.removeNode("Centre");
      UnGrapheDesChemins.removeLink("Nord", "Ouest");
      dump("amputé");
     // On nettoie avant de finir
       sleep(5000);
      unbuild();
    }
}
