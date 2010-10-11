 /** 
 * Fonctions pour la seconde fenetre
 */
 
 public class PFrame extends Frame {
      public PFrame() {
          setBounds(1,25,400,400); //placement?
          //setBounds(gX+50, gY+120, 200, 20);
          //setLocation(gX+300,gY+height-200);
          setUndecorated(true);
          s = new secondApplet();
          add(s);
          s.init();
          show();
      }
  }
  
  public class secondApplet extends PApplet {
      public void setup() {
          size(400, 400);
          noLoop();
      }
  
      public void draw() {
      }
  }
  
  public processing.core.PApplet getControl() {
  
    s.hide();
    return s;
  
  }
