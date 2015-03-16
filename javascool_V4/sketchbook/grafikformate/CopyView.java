import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

public class CopyView extends Canvas {
  private static int instanceCounter = 0;
  private boolean working = false;
  protected Image outImage, originalImage;
  protected ImageChanger parent;
  protected colorPalette CP;
  private int ID;
  private int colorDepth = 256;
  private int cPalette;
  private float percentage = 0;

  public CopyView(ImageChanger _parent, colorPalette _CP) {
    CP = _CP;
    outImage = null;
    originalImage = null;
    parent = _parent;
    instanceCounter++;
    ID = instanceCounter;
  }
  public void changeImage(float factor, int resolution, int _palette) {
    if(!working) {
      working = true;
      percentage = 0;
      cPalette = _palette;
      boolean scaled = false;
      drawRect();
      if((factor != 0f) && (factor != 1f)) {
        try {
          int width = (int) (factor * originalImage.getWidth(this));
          int height = (int) (factor * originalImage.getHeight(this));
          this.scaleImage(width, height);
          scaled = true;
        } catch(Exception e) {
          System.out.println("Exeption caught [ImagaView.scaleImage]");
        }
      }
      if((resolution != 24)) {
        colorDepth = resolution;
        this.changeColor(scaled);
      } else {
        CP.setColors(null);
      }
      this.repaint();
      working = false;
    }
  }
  public void scaleImage(int width, int height) {
    if(width == 0) {
      width = 1;
    }
    if(height == 0) {
      height = 1;
    }
    Image tmp;
    MediaTracker MT = new MediaTracker(this);
    tmp = originalImage.getScaledInstance(width, height, Image.SCALE_FAST);
    MT.addImage(tmp, 0, width, height);
    try { MT.waitForID(0);
    } catch(InterruptedException e) {
      System.out.println("MT exception");
    }
    outImage = tmp;
  }
  public void scaleImage(float percentage) {
    try {
      int width = (int) (percentage * originalImage.getWidth(this));
      int height = (int) (percentage * originalImage.getHeight(this));
      this.scaleImage(width, height);
    } catch(Exception e) {
      System.out.println("Exeption caught [ImagaView.scaleImage]");
    }
  }
  public void setImage(Image _img) {
    if(originalImage != null) {
      originalImage.flush();
    }
    if(outImage != null) {
      outImage.flush();
    }
    outImage = null;
    originalImage = parent.createImage(_img.getWidth(this), _img.getHeight(this));
    Graphics g = originalImage.getGraphics();
    g.drawImage(_img, 0, 0, this);
    CP.setColors(null);
    this.repaint();
  }
  public Image getImage() {
    if(outImage == null) {
      return originalImage;
    }
    return outImage;
  }
  public void changeColor(boolean scaled) {
    int width = originalImage.getWidth(this);
    int height = originalImage.getHeight(this);
    int[] in = new int[width * height];
    PixelGrabber grabber;
    if(!scaled) {
      grabber = new PixelGrabber(originalImage, 0, 0, width, height, in, 0, width);
    } else {
      width = outImage.getWidth(this);
      height = outImage.getHeight(this);
      in = new int[width * height];
      grabber = new PixelGrabber(outImage, 0, 0, width, height, in, 0, width);
    }
    try { grabber.grabPixels();
    } catch(InterruptedException e) {
      System.out.println("grabber exception");
    }
    if(colorDepth == 0) {
      grayImage(in, width, height);
    } else {
      if(cPalette == 2) {
        descreteColors(in, width, height);
      } else {
        maxColors(in, width, height);
      }
    }
  }
  public void grayImage(int[] in, int width, int height) {
    int r, g, b, i, j;
    int mean;
    int grayValue;
    Vector colors = new Vector();
    // ////////////////////////////////////////////////////////////////////
    // go thru image and set gray value
    for(i = 0; i < width; i++) {
      for(j = 0; j < height; j++) {
        r = (int) ((in[i + j * width] & 0xFF0000) >> 16);
        g = (int) ((in[i + j * width] & 0xFF00) >> 8);
        b = (int) (in[i + j * width] & 0xFF);
        mean = (int) (0.3f * r + 0.59f * g + 0.11f * b);
        grayValue = 0xFF000000 | (mean << 16) | (mean << 8) | mean;
        in[i + j * width] = grayValue;
        if(!colors.contains(new Integer(grayValue))) {
          colors.addElement(new Integer(grayValue));
        }
      }
      percentage = (i * 1.0f) / width;
      this.forceRepaint();
    }
    CP.setColors(colors);
    outImage = createImage(new MemoryImageSource(width, height, in, 0, width));
  }
  public void maxColors(int[] in, int width, int height) {
    Hashtable colors = new Hashtable();
    Color pixColor;
    Vector palette = new Vector();
    Vector values = new Vector();
    Vector sortedColors = new Vector();
    int r, value, i, j;
    Integer rgb;
    boolean added = false;
    // ////////////////////////////////////////////////////////////////////
    // go thru image and count all occurences of all featured colors
    for(i = 0; i < width; i++) {
      for(j = 0; j < height; j++) {
        r = (int) in[i + j * width];
        rgb = new Integer(r);
        if(colors.containsKey(rgb)) {
          value = ((Integer) (colors.get(rgb))).intValue();
          colors.put(rgb, new Integer(value + 1));
        } else {
          colors.put(rgb, new Integer(1));
        }
      }
      percentage = (i * 0.1f) / width;
      this.forceRepaint();
    }
    // //////////////////////////////////////////////////////////////////
    if((cPalette == 0) && (colors.size() > colorDepth)) {
      colors = clusterColors(colors);
    }
    // //////////////////////////////////////////////////////////////
    // go thru hash and save the 256 most used colors
    int amount = colors.size();
    int runcount = 0;
    int smallest = 0;
    float myRange = 0.3f + 0.3f * cPalette;
    float myStart = 0.4f - 0.3f * cPalette;
    for(Enumeration e = colors.keys(); e.hasMoreElements();) {
      rgb = (Integer) e.nextElement();
      value = ((Integer) (colors.get(rgb))).intValue();
      added = false;
      if(value <= smallest) {
        added = true;
      }
      for(i = 0; (i < palette.size()) && !added; i++)
        if(value > ((Integer) (values.elementAt(i))).intValue()) {
          values.insertElementAt(new Integer(value), i);
          palette.insertElementAt(rgb, i);
          added = true;
        }
      if((values.size() < colorDepth) && !added) {
        values.addElement(new Integer(value));
        palette.addElement(rgb);
      } else if(values.size() > colorDepth) {
        smallest = ((Integer) (values.elementAt(colorDepth))).intValue();
        values.removeElementAt(colorDepth);
        palette.removeElementAt(colorDepth);
      }
      runcount++;
      percentage = myStart + (runcount * myRange) / amount;
      // percentage = 0.4f+(runcount*0.3f)/amount;
      this.forceRepaint();
    }
    // ///////////////////////////////////////////////////////////////
    amount = palette.size();
    for(i = 0; (i < amount); i++) {
      value = ((Integer) palette.elementAt(i)).intValue();
      added = false;
      for(j = 0; (j < sortedColors.size()) && !added; j++)
        if(value > ((Integer) (sortedColors.elementAt(j))).intValue()) {
          sortedColors.insertElementAt(new Integer(value), j);
          added = true;
        }
      if(!added) {
        sortedColors.addElement(new Integer(value));
      }
      percentage = 0.7f + (i * 0.1f) / amount;
      this.forceRepaint();
    }
    squareSubsitution(sortedColors, in, width, height);
  }
  // //////////////////////////////////////////////////////////////
  // go thru all Colors and try to find regions and group them
  public Hashtable clusterColors(Hashtable colors) {
    Integer rgb, thisRGB;
    int r, g, b;
    long thisR, thisG, thisB;
    Hashtable bundledColors = new Hashtable();
    boolean ok = false;
    long dist;
    int thisvalue, value, runcount = 0;
    int amount = colors.size();
    for(Enumeration e = colors.keys(); e.hasMoreElements();) {
      ok = false;
      rgb = (Integer) e.nextElement();
      r = (int) ((rgb.intValue() & 0xFF0000) >> 16);
      g = (int) ((rgb.intValue() & 0xFF00) >> 8);
      b = (int) (rgb.intValue() & 0xFF);
      value = ((Integer) (colors.get(rgb))).intValue();
      // vthierry patch: change variable from enum to enumeration since enum is now a Java keyword
      for(Enumeration enumeration = bundledColors.keys(); (enumeration.hasMoreElements()) && !ok;) {
        thisRGB = (Integer) enumeration.nextElement();
        thisR = (long) ((thisRGB.intValue() & 0xFF0000) >> 16);
        thisG = (long) ((thisRGB.intValue() & 0xFF00) >> 8);
        thisB = (long) (thisRGB.intValue() & 0xFF);
        dist = (thisR - r) * (thisR - r) + (thisG - g) * (thisG - g) + (thisB - b) * (thisB - b);
        if(dist < 35 + (256 - colorDepth)) {
          ok = true;
          thisvalue = ((Integer) (bundledColors.get(thisRGB))).intValue();
          bundledColors.put(thisRGB, new Integer(value + thisvalue));
        }
      }
      if(!ok) {
        bundledColors.put(rgb, new Integer(value));
      }
      runcount++;
      percentage = 0.1f + (runcount * 0.3f) / amount;
      this.forceRepaint();
    }
    return bundledColors;
  }
  // //////////////////////////////////////////////////////////////////////////
  // go thru image and substitute color using least square method
  public void squareSubsitution(Vector sortedColors, int[] in, int width, int height) {
    int current = 0;
    int i, j, r, g, b, k;
    r = 0;
    g = 0;
    b = 0;
    long pr, pg, pb;
    int bestIndex = 0;
    long minDistance = 195075; // equals (255^2)*3
    long cDistance;
    int oldcolor = 0, newcolor = 0;
    for(i = 0; i < width; i++) {
      for(j = 0; j < height; j++) {
        newcolor = in[i + j * width];
        if((newcolor != oldcolor) || ((i == 0) && (j == 0))) {
          oldcolor = newcolor;
          r = (int) ((newcolor & 0xFF0000) >> 16);
          g = (int) ((newcolor & 0xFF00) >> 8);
          b = (int) (newcolor & 0xFF);
          minDistance = (255 * 255) * 3;
          for(k = 0; k < sortedColors.size(); k++) {
            current = ((Integer) (sortedColors.elementAt(k))).intValue();
            pr = (long) ((current & 0xFF0000) >> 16);
            pg = (long) ((current & 0xFF00) >> 8);
            pb = (long) (current & 0xFF);
            cDistance = (pr - r) * (pr - r) + (pg - g) * (pg - g) + (pb - b) * (pb - b);
            if(cDistance < minDistance) {
              minDistance = cDistance;
              bestIndex = k;
            }
          }
        }
        in[i + j * width] = ((Integer) (sortedColors.elementAt(bestIndex))).intValue();
      }
      // System.out.print(" ["+r+","+g+","+b+"]");
      percentage = 0.8f + (i * 0.2f) / width;
      this.forceRepaint();
    }
    CP.setColors(sortedColors);
    outImage = createImage(new MemoryImageSource(width, height, in, 0, width));
  }
  public void descreteColors(int[] in, int width, int height) {
    int redmin, redmax, bluemin, bluemax, greenmin, greenmax;
    Vector sortedColors = new Vector();

    int r, g, b, i, j;

    int range = 255;

    float fstep = range / 5.0f;
    int step = (int) fstep;

    float rf, gf, bf;
    for(i = 0; i < width; i++) {
      for(j = 0; j < height; j++) {
        r = (int) ((in[i + j * width] & 0xFF0000) >> 16);
        g = (int) ((in[i + j * width] & 0xFF00) >> 8);
        b = (int) (in[i + j * width] & 0xFF);

        rf = (r / step);
        r = (int) rf;
        r = (r * step);

        gf = (g / step);
        g = (int) gf;
        g = (g * step);

        bf = (b / step);
        b = (int) bf;
        b = (b * step);

        in[i + j * width] = 0xFF000000 | (r << 16) | (g << 8) | b;
      }
      percentage = (i * 1.0f) / width;
      this.forceRepaint();
    }
    for(i = 0; i < 6; i++)
      for(j = 0; j < 6; j++)
        for(int k = 0; k < 6; k++)
          sortedColors.addElement(new Integer((0xFF000000 | ((i * step) << 16) | ((j * step) << 8) | (k * step))));
    CP.setColors(sortedColors);
    outImage = createImage(new MemoryImageSource(width, height, in, 0, width));
  }
  public void paint(Graphics g) {
    if(outImage == null) {
      g.drawImage(originalImage, 0, 10, 200, 150, this);
    } else {
      g.drawImage(outImage, 0, 10, 200, 150, this);
    }
    if(working) {
      int width = (int) (180 * percentage);
      g.setColor(Color.blue);
      g.fillRect(5, 65, width, 20);
      g.setColor(Color.gray);
      g.fillRect((5 + width), 65, (180 - width), 20);
    }
  }
  public void update(Graphics g) {
    paint(g);
  }
  private void drawRect() {
    Graphics g = getGraphics();
    g.setColor(Color.gray);
    g.fillRect(10, 85, 180, 10);
    g.setColor(Color.blue);
  }
  private void forceRepaint() {
    Graphics g = getGraphics();
    int width = (int) (180 * percentage);
    g.setColor(Color.blue);
    g.fillRect(10, 85, width, 10);
  }
}
