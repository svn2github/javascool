/*******************************************************************************
* David.Pichardie@inria.fr, Copyright (C) 2011.           All rights reserved. *
*******************************************************************************/

package proglet.paintbrush;

import java.util.*;

import java.awt.Toolkit;
import java.awt.Graphics; 
import java.awt.Graphics2D; 
import java.awt.RenderingHints;
import java.awt.Font;

public class Image {

  
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
      image[i][j] = 15;
  }
  
  int get(int x, int y) {
    return image[x][y];
  };
  
  void set(int x, int y, int col) {
    image[x][y] = col;
    if (col==15) points.remove(new Point(x,y));
    else points.add(new Point(x,y));
  }

  int maxX() {return width;}
  int maxY() {return height;}
  
  void clear() {
    for (int i=0; i<width; i++) Arrays.fill(image[i],15);
    points.clear();
  }
 
  static String byteToString(int i) {
    if (i<10) return ""+i;
    else if (i==10) return "A";
    else if (i==11) return "B";
    else if (i==12) return "C";
    else if (i==13) return "D";
    else if (i==14) return "E";
    else return "F";
  }
  
  void ascii(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    
    int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
    int fontSize = (int)Math.round(10.0 * screenRes / 72.0);

    Font font = new Font("Arial", Font.PLAIN, fontSize);
    
    g2d.setFont(font);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    for (int i=0; i<width; i++) {
      for (int j=0; j<height; j++) {
        int col = get(i,j);
        g.setColor(ColorPaint.colors[col].getTextColor().getColor());
        g2d.drawString(byteToString(image[i][j]), i*MyPanel.square+MyPanel.square/3, j*MyPanel.square+2*MyPanel.square/3);
      }
    }
  }
  
}

