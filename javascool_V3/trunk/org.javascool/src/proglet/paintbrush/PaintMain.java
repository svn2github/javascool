/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package proglet.paintbrush;


import javax.swing.*;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Graphics; 
import java.awt.Graphics2D; 
import java.awt.BorderLayout; 
import java.awt.GridLayout; 
import java.awt.GridBagConstraints; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter; 
import java.util.*;

public class PaintMain {
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
	public void run() {
	  createAndShowGUI(); 
	}
      });
  }
  
  private static void createAndShowGUI() {
    JFrame f = new JFrame("Paint");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    f.add(new MainPanel());
    f.pack();
    f.setVisible(true);
  } 
}

class Point implements Comparable {
  int x;
  int y;
  Point(int _x, int _y) {
    x = _x;
    y = _y;
  }
  public int compareTo(Object o) {
    Point p = (Point) o;
    int cmp1 = p.x - x;
    if (cmp1!=0) return cmp1;
    else return p.y -y;
  }
  public boolean isClosed(Point p) {
    return Math.abs(p.x-x) <= 1 && Math.abs(p.y-y) <= 1;
  }
}

enum Mode { DRAW, RECTANGLE, ERASE, FILL }

class MainPanel extends JPanel implements ActionListener {
  
  private static String button1String = "Trait";
  private static String button2String = "Rectangle";
  private static String button3String = "Gomme";
  private static String button4String = "Remplir";
  private static String buttonClearString = "Effacer tout";
  MyPanel myPanel;
  
  public MainPanel() {
    super(new BorderLayout());
    //    GroupLayout layout = new GroupLayout(this);
    //    this.setLayout(layout);
    //GridBagConstraints c = new GridBagConstraints();


    //Create the radio buttons.
    JRadioButton button1 = new JRadioButton(button1String);
    button1.setActionCommand(button1String);
    button1.setSelected(true);
    
    JRadioButton button2 = new JRadioButton(button2String);
    button2.setActionCommand(button2String);
    
    JRadioButton button3 = new JRadioButton(button3String);
    button3.setActionCommand(button3String);
    
    JRadioButton button4 = new JRadioButton(button4String);
    button4.setActionCommand(button4String);
    
    
    //Group the radio buttons.
    ButtonGroup group = new ButtonGroup();
    group.add(button1);
    group.add(button2);
    group.add(button3);
    group.add(button4);
        
    //Put the radio buttons in a column in a panel.
    JPanel radioPanel = new JPanel(new GridLayout(0, 1));
    radioPanel.add(button1);
    radioPanel.add(button2);
    radioPanel.add(button3);
    radioPanel.add(button4);
    
    JButton buttonClear = new JButton(buttonClearString);
    buttonClear.setActionCommand(buttonClearString);
    radioPanel.add(buttonClear);
    
    Image image = new Image(MyPanel.width,MyPanel.height);    
    AsciiPanel asciiPanel = new AsciiPanel(image);
    myPanel = new MyPanel(image,asciiPanel);
    myPanel.updateMode(Mode.DRAW);
    
    //    c.fill = GridBagConstraints.HORIZONTAL;
    //    c.gridx = 0;
    //    c.gridy = 0;
    //    c.weightx = 0.1;
    //    add(radioPanel,c);
    
    //    c.fill = GridBagConstraints.NONE;
    //    c.gridx = 1;
    //    c.gridy = 0;
    //    c.weightx = 0.4;
    //    c.ipadx = MyPanel.width*MyPanel.square;
    //    c.ipady = MyPanel.height*MyPanel.square;
    //    add(myPanel,c);

    //    c.fill = GridBagConstraints.NONE;
    //    c.gridx = 2;
    //    c.gridy = 0;
    //    c.weightx = 0.5;
    //    c.ipady = 60;
    //    c.ipadx = AsciiPanel.scale*MyPanel.width*MyPanel.square;
    //    c.ipady = AsciiPanel.scale*MyPanel.height*MyPanel.square;
    //    add(asciiPanel,c);
    //setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    
    //   layout.setHorizontalGroup(layout.createSequentialGroup()
    //    .addComponent(radioPanel)
    //    .addComponent(myPanel)
    //    .addComponent(asciiPanel));

    add(radioPanel, BorderLayout.WEST);
    add(myPanel, BorderLayout.CENTER);
    
    //Register a listener for the radio buttons.
    button1.addActionListener(this);
    button2.addActionListener(this);
    button3.addActionListener(this);
    button4.addActionListener(this);
    buttonClear.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    if (action.equals(button1String)) myPanel.updateMode(Mode.DRAW);
    else if (action.equals(button2String)) myPanel.updateMode(Mode.RECTANGLE);
    else if (action.equals(button3String)) myPanel.updateMode(Mode.ERASE);
    else if (action.equals(button4String)) myPanel.updateMode(Mode.FILL);
    else if (action.equals(buttonClearString)) myPanel.clear();
  }
}

class Image {
  
  private int[][] image;
  public Set<Point> points = new TreeSet<Point>();
  private int height;
  private int width;
  
  Image(int _width, int _height) {
    width = _width;
    height = _height;
    image = new int[_width][_height];
    for (int i=0; i<width; i++)
      for (int j=0; j<height; j++) 
	image[i][j] = 255;
  }
  
  int get(int x, int y) {
    return image[x][y];
  };
  
  void set(int x, int y, int col) {
    image[x][y] = col;
    if (col==255) points.remove(new Point(x,y));
    else points.add(new Point(x,y));
  }

  int maxX() {return width;}
  int maxY() {return height;}
  
  void clear() {
    for (int i=0; i<width; i++) Arrays.fill(image[i],255);
    points.clear();
  }
 
  String byteToString(int i) {
    if (i<10) return "00"+i;
    else if (i<100) return "0"+i;
    else return ""+i;
  }
  
  void ascii(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    
    int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
    int fontSize = (int)Math.round(8.0 * screenRes / 72.0);

    Font font = new Font("Arial", Font.PLAIN, fontSize);
    
    g2d.setFont(font);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    for (int i=0; i<width; i++) {
      for (int j=0; j<height; j++) 
        g2d.drawString(byteToString(image[i][j]), i*4*AsciiPanel.scale, j*2*AsciiPanel.scale);
    }
  }
  
}

class AsciiPanel extends JPanel {
 
  private Image image;
  static int scale = 4;
  
  public AsciiPanel(Image _image) {
    image = _image;
  }
  
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);       
    image.ascii(g);
  }  
  
  public Dimension getPreferredSize() {
    return new Dimension(scale*MyPanel.width*MyPanel.square,scale*MyPanel.height*MyPanel.square);
  }
  
}

class MyPanel extends JPanel implements MouseMotionListener {
  
  static int square = 5;
  static int height = 64;
  static int width = 64;
  private static Cursor paint_cursor;
  private static Cursor eraser_cursor;
  private Mode mode;
  public Image image;
  private Point start_point_rectangle;
  private Point end_point_rectangle;
  private Point previous_point;
  private AsciiPanel asciiPanel;
  
  static {
    Toolkit toolkit = Toolkit.getDefaultToolkit();  
    java.awt.Image image = toolkit.getImage("img/cursor_paint.png");  
    paint_cursor = toolkit.createCustomCursor(image, new java.awt.Point(3,24), "Fill");
    java.awt.Image image2 = toolkit.getImage("img/cursor_eraser.png");  
    eraser_cursor = toolkit.createCustomCursor(image2, new java.awt.Point(2,14), "Erase");
  }
  
  int sanitizeX(int x) {
    int max_x = image.maxX()*square;
    return (x<=0) ? 0 : (x>=max_x) ? max_x-1 : x;
  }
  
  int sanitizeY(int y) {
    int max_y = image.maxY()*square;
    return (y<=0) ? 0 : (y>=max_y) ? max_y-1 : y;
  }

  public void updateMode(Mode m) {
    mode = m;
    Cursor c;
    switch (mode) {
    case DRAW : c=Cursor.getPredefinedCursor(Cursor.HAND_CURSOR); break;
    case FILL : c=paint_cursor; break;
    case ERASE : c=eraser_cursor; break;
    case RECTANGLE : c=Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR); break;
    default: c=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    }
    setCursor(c);
  }
  
  public void mouseDragged(MouseEvent e) {
    switch (mode) {
    case DRAW : addOtherSquare(sanitizeX(e.getX()),sanitizeY(e.getY())); break;
    case ERASE : removeOtherSquare(sanitizeX(e.getX()),sanitizeY(e.getY())); break;
    case RECTANGLE : showRect(sanitizeX(e.getX()),sanitizeY(e.getY())); break;
    default : break;
    }
  }
   
  public void mouseMoved(MouseEvent evt) {}

  public MyPanel(Image _image, AsciiPanel _asciiPanel) {
    
    asciiPanel = _asciiPanel;
    setBorder(BorderFactory.createLineBorder(Color.black));
    
    addMouseListener(new MouseAdapter() {
	public void mousePressed(MouseEvent e) {
	  switch (mode) {
          case DRAW : addSquare(e.getX(),e.getY()); break;
          case FILL : fill(e.getX(),e.getY()); break;
          case ERASE : removeSquare(e.getX(),e.getY()); break;
          case RECTANGLE : startRect(e.getX(),e.getY()); break;
	  }
	}
      });
    
    addMouseListener(new MouseAdapter() {
	public void mouseReleased(MouseEvent e) {
	  switch (mode) {
          case RECTANGLE : endRect(sanitizeX(e.getX()),sanitizeY(e.getY())); break;
          default : break;
	  }
	}
      });
  
    addMouseMotionListener(this);

    //    addMouseMotionListener(new MouseAdapter() {
    //      public void mouseDragged(MouseEvent e) {
    //        switch (mode) {
    //          case DRAW : addOtherSquare(sanitizeX(e.getX()),sanitizeY(e.getY())); break;
    //          case ERASE : removeOtherSquare(sanitizeX(e.getX()),sanitizeY(e.getY())); break;
    //          case RECTANGLE : showRect(sanitizeX(e.getX()),sanitizeY(e.getY())); break;
    //          default : break;
    //        }
    //      }
    //    });
    
    image = _image;
  }
  
  void drawPoint(Graphics g, Point p) {
    int col_int = image.get(p.x,p.y);
    Color col = (col_int==0) ? Color.BLACK : Color.BLUE;      
    g.setColor(col);
    g.fillRect(square*p.x,square*p.y,square,square);
  }    
  
  void repaintPoint(Point p) {
    repaint(square*p.x,square*p.y,square+1,square+1);
  }    
  
  void debug() {
    for (int j=0; j<image.maxY(); j++) {
      for (int i=0; i<image.maxX(); i++) {
        if (image.get(i,j)==0) System.out.print("x");
        if (image.get(i,j)==255) System.out.print(".");
      }
      System.out.println("");
    }
  }
  
  void clear() {
    image.clear();
    repaint();
    asciiPanel.repaint();
  }
    

      
  private void addSquare(int x, int y) {
    Point p = new Point(x/square,y/square);
    ManipImage.affichePoint(image,p.x,p.y);
    previous_point = p;
    repaint();     
    asciiPanel.repaint();
  }
  
  private void addOtherSquare(int x, int y) {
    Point p = new Point(x/square,y/square);
    ManipImage.affichePoint(image,p.x,p.y);
    if (!p.isClosed(previous_point)) {
      fillHole(image,previous_point.x,previous_point.y,p.x,p.y);
    }
    previous_point = p;
    repaint();     
    asciiPanel.repaint();
  }

  //Bresenham's line algorithm
  private void fillHole(Image image, int x0, int y0, int x1, int y1) {
    int dx = Math.abs(x1-x0);
    int dy = Math.abs(y1-y0);
    int sx = (x0 < x1) ? 1 : -1;
    int sy = (y0 < y1) ? 1 : -1;
    int err = dx-dy;
    while (true) {
      ManipImage.affichePoint(image,x0,y0);
      if (x0 == x1 && y0 == y1) return;
      int e2 = 2*err;
      if (e2 > -dy) {
        err = err - dy;
        x0 = x0 + sx;
      }
      if (e2 <  dx) {
        err = err + dx;
        y0 = y0 + sy; 
      }
    }
  }
  
  //Bresenham's line algorithm
  private void eraseHole(Image image, int x0, int y0, int x1, int y1) {
    int dx = Math.abs(x1-x0);
    int dy = Math.abs(y1-y0);
    int sx = (x0 < x1) ? 1 : -1;
    int sy = (y0 < y1) ? 1 : -1;
    int err = dx-dy;
    while (true) {
      ManipImage.supprimePoint(image,x0,y0);
      if (x0 == x1 && y0 == y1) return;
      int e2 = 2*err;
      if (e2 > -dy) {
        err = err - dy;
        x0 = x0 + sx;
      }
      if (e2 <  dx) {
        err = err + dx;
        y0 = y0 + sy; 
      }
    }
  }
  
  private void removeSquare(int x, int y) {
    Point p = new Point(x/square,y/square);
    ManipImage.supprimePoint(image,p.x,p.y);
    previous_point = p;
    repaint();
    asciiPanel.repaint();
  }
  
  private void removeOtherSquare(int x, int y) {
    Point p = new Point(x/square,y/square);
    ManipImage.supprimePoint(image,p.x,p.y);
    if (!p.isClosed(previous_point)) {
      eraseHole(image,previous_point.x,previous_point.y,p.x,p.y);
    }
    previous_point = p;
    repaint();     
    asciiPanel.repaint();
  }
   
  private void fill(int x, int y) {
    ManipImage.remplir(image,x/square,y/square);
    repaint();
    asciiPanel.repaint();
  }
  
  private void startRect(int x, int y) {
    start_point_rectangle = new Point(x/square,y/square);
    end_point_rectangle = null;
  }

  private void showRect(int x, int y) {
    end_point_rectangle = new Point(x/square,y/square);
    repaint();
    asciiPanel.repaint();
  }

  private void endRect(int x, int y) {
    ManipImage.afficheRectangle(image,start_point_rectangle.x,start_point_rectangle.y,x/square,y/square);
    end_point_rectangle = null;
    repaint();
    asciiPanel.repaint();
  }

  public Dimension getPreferredSize() {
    return new Dimension(width*square,height*square);
  }
  
  private void draw_rect(Graphics g, Point p1, Point p2) {
    int xmin = Math.min(p1.x,p2.x);
    int xmax = Math.max(p1.x,p2.x);
    int ymin = Math.min(p1.y,p2.y);
    int ymax = Math.max(p1.y,p2.y);
    g.drawRect(square*xmin,square*ymin,square*(xmax-xmin),square*(ymax-ymin));
  }
  
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);       
    //g.drawString("text",10,20);
    for (Point p:image.points) drawPoint(g, p);
    if (end_point_rectangle!=null)
      draw_rect(g,start_point_rectangle,end_point_rectangle);
  }  
}

