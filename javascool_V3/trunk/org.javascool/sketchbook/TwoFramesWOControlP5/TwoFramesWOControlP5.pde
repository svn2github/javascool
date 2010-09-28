PFrame f;
  secondApplet s;
  
  void setup() {
   size(400, 300);
   f = new PFrame();

  }
  
  void draw() {
     this.frame.setTitle(" Principale ");
     background(240);
     fill(255);
     stroke(255);
     rect(10,50,frameCount%100,10);

     s.background(0);
     s.fill(100);
     s.rect(10,50,frameCount%120,10);
     s.redraw();
  }
  
  public class PFrame extends Frame {
      public PFrame() {
          setBounds(100,100,400,300); //placement?
          s = new secondApplet();
          add(s);
          s.init();
          show();
      }
  }
  
  public class secondApplet extends PApplet {
      public void setup() {
          size(400, 300);
          noLoop();
      }
  
      public void draw() {
      }
  }
  
  public processing.core.PApplet getControl() {
  
    s.hide();
    return s;
  
}
  
 
