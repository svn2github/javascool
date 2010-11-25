/*
 *  following code (GUI elements, font stuff, utilities) are part of :
 *
 *  NPZVISUALIZER 1.5 · Neil Banas, Univ of Washington Applied Physics Lab · Oct 2008
 *  which can be seen at http://coast.ocean.washington.edu/~neil/NPZvisualizer/.
 *
 *  this work is licensed under "Creative Commons Attribution-NonCommercial-ShareAlike2.5 License".
 */

// -----------------------------------------------------------------------------------
// GUI elements
// -----------------------------------------------------------------------------------

// some global defs
color redColor = color(190, 30, 20);
color blueColor = color(55, 93, 129);
color greenColor = color(30, 90, 20);
color purpleColor = color(60, 20, 60);
color yellowColor = color(255, 255, 0);
color orangeColor = color(250, 75, 0);

color grayColor = color(102, 102, 102);
color ltGrayColor = color(200, 200, 200);
color dkGrayColor = color(50, 50, 50);
// added colors -- kuler-1
color lightBlue = color(173, 180, 217);
// color Blue = color(105,127,191);
color Green = color(184, 217, 67);
color kaki = color(86, 115, 44);
color darkGray = color(38, 38, 38);
color orange = color(242, 154, 46);

int smallfontsize, medfontsize, bigfontsize, hugefontsize;

class poly { // ----------------------------------------------------------------------
  int length;
  int[] eachSizePoly;
  float[] x, y;
  int nbPoly;

  poly(int n) {
    nbPoly = 1;
    length = n;
    x = new float[length];
    y = new float[length];
  }

  void assign(int i, float xx, float yy) {
    x[i] = xx;
    y[i] = yy;
  }
  void assign2(int i1, float xx1, float yy1, int i2, float xx2, float yy2) {
    assign(i1, xx1, yy1);
    assign(i2, xx2, yy2);
  }
  void assign3(int i1, float xx1, float yy1, int i2, float xx2, float yy2, int i3, float xx3, float yy3) {
    assign(i1, xx1, yy1);
    assign(i2, xx2, yy2);
    assign(i3, xx3, yy3);
  }
  void draw() {
    beginShape();
    for(int i = 0; i < length; i++)
      vertex(x[i], y[i]);
    endShape();
  }
  float xmin() {
    float ans = x[0];
    for(int i = 1; i < length; i++)
      ans = min(ans, x[i]);
    return ans;
  }
  float xmax() {
    float ans = x[0];
    for(int i = 1; i < length; i++)
      ans = max(ans, x[i]);
    return ans;
  }
  float ymin() {
    float ans = y[0];
    for(int i = 1; i < length; i++)
      ans = min(ans, y[i]);
    return ans;
  }
  float ymax() {
    float ans = y[0];
    for(int i = 1; i < length; i++)
      ans = max(ans, y[i]);
    return ans;
  }
  boolean inside(float xx, float yy) {
    // inside the bounding box: not a proper isinpoly.m
    return ((xx >= xmin()) && (xx <= xmax())) && ((yy >= ymin()) && (yy <= ymax()));
  }
  void rotate(float ang) {
    ang = -ang * PI / 180;
    float tmp;
    for(int i = 0; i < length; i++) {
      tmp = (x[i] - 0.5) * cos(ang) - (y[i] - 0.5) * sin(ang) + 0.5;
      y[i] = (x[i] - 0.5) * sin(ang) + (y[i] - 0.5) * cos(ang) + 0.5;
      x[i] = tmp;
    }
  }
} // end of class poly

poly rectPoly(float x0, float y0, float wd, float ht) {
  poly p = new poly(4);
  p.assign(0, x0, y0);
  p.assign(1, x0 + wd, y0);
  p.assign(2, x0 + wd, y0 + ht);
  p.assign(3, x0, y0 + ht);
  return p;
}
class multistatePolyButton { // ------------------------------------------------------
  int nstates;
  String[] name, secondname;
  color[] colors;
  poly[] polygon; // outline associated with each state, (0..1, 0..1)
  poly[] screenpoly;
  int width, height, x0, y0;
  int current;
  boolean active;
  boolean pressed;
  boolean showName;
  String namePos; // "center", "bottom", "right"
  boolean hidden;

  multistatePolyButton(int n, int x, int y, int wd, int ht) {
    nstates = n;
    name = new String[n + 1]; // state 0 is reserved: currently, means 'mouse not over'
    secondname = new String[n + 1];
    colors = new color[n + 1];
    colors[0] = grayColor;
    polygon = new poly[n + 1];
    screenpoly = new poly[n + 1];
    width = wd;
    height = ht;
    x0 = x;
    y0 = y;
    current = 1;
    active = false;
    pressed = false;
    showName = false;
    namePos = "center";
    hidden = false;
  }

  void assign(int n, String nm, String nm2, poly pgon, color col) {
    name[n] = nm;
    secondname[n] = nm2;
    colors[n] = col;
    polygon[n] = pgon; // relative shape poly
    screenpoly[n] = new poly(polygon[n].length);
    // translate to screen coords
    for(int i = 0; i < polygon[n].length; i++) {
      screenpoly[n].x[i] = x0 + polygon[n].x[i] * width;
      screenpoly[n].y[i] = y0 + polygon[n].y[i] * height;
    }
  }
  void assign(int n, int xx, int yy, poly pgon) {
    x0 = xx;
    y0 = yy;
    polygon[n] = pgon; // relative shape poly
    screenpoly[n] = new poly(polygon[n].length);
    // translate to screen coords
    for(int i = 0; i < polygon[n].length; i++) {
      screenpoly[n].x[i] = x0 + polygon[n].x[i] * width;
      screenpoly[n].y[i] = y0 + polygon[n].y[i] * height;
    }
  }
  void assign(int n, int xx, int yy) {
    x0 = xx;
    y0 = yy;
    screenpoly[n] = new poly(polygon[n].length);
    // translate to screen coords
    for(int i = 0; i < polygon[n].length; i++) {
      screenpoly[n].x[i] = x0 + polygon[n].x[i] * width;
      screenpoly[n].y[i] = y0 + polygon[n].y[i] * height;
    }
  }
  // additive color. initial colors (col[0]) ccould be defined
  void assign(int n, String nm, String nm2, poly pgon, color col0, color col) {
    name[n] = nm;
    secondname[n] = nm2;
    colors[0] = col0;
    colors[n] = col;
    polygon[n] = pgon; // relative shape poly
    screenpoly[n] = new poly(polygon[n].length);
    // translate to screen coords
    for(int i = 0; i < polygon[n].length; i++) {
      screenpoly[n].x[i] = x0 + polygon[n].x[i] * width;
      screenpoly[n].y[i] = y0 + polygon[n].y[i] * height;
    }
  }
  void draw_poly() {
    screenpoly[current].draw();
  }
  void draw() { // displays the button in the state _current_.
    if(!hidden) {
      color fillcolor;
      noStroke();
      if(active)
        fillcolor = darken(colors[current]);
      else if(over())
        fillcolor = colors[current];
      else
        fillcolor = colors[0];
      fill(fillcolor);
      draw_poly();
      if(showName) {
        if(namePos == "center") {
          // fill(lighten(lighten(fillcolor)));
          fill(255);
          textAlign(CENTER);
          if(secondname[current] == "")
            text(name[current], (float) x0 + (float) width / 2, (float) y0 + 0.7 * (float) height);
          else {
            text(name[current], (float) x0 + (float) width / 2, (float) y0 + 0.7 * (float) height - medfontsize / 2);
            text(secondname[current], (float) x0 + (float) width / 2, (float) y0 + 0.7 * (float) height + medfontsize / 2);
          }
        } else if(namePos == "bottom") {
          fill(fillcolor);
          textAlign(CENTER);
          if(secondname[current] == "")
            text(name[current], (float) x0 + (float) width / 2, (float) y0 + 1.5 * (float) height);
          else {
            text(name[current], (float) x0 + (float) width / 2, (float) y0 + 1.5 * (float) height);
            text(secondname[current], (float) x0 + (float) width / 2, (float) y0 + 1.5 * (float) height + medfontsize);
          }
        } else if(namePos == "right") { // additive name position (by d.marec)
          fill(grayColor);
          textAlign(LEFT);
          if(secondname[current] == "")
            text(name[current], (float) x0 + 1.5 * (float) width, (float) y0 + 0.7 * (float) height);
          else {
            text(name[current], (float) x0 + 1.5 * (float) width, (float) y0 + 0.5 * (float) height - medfontsize / 2);
            text(secondname[current], (float) x0 + 1.5 * (float) width, (float) y0 + 0.75 * (float) height + medfontsize / 2);
          }
        }
      }
    }
  }
  boolean over() {
    poly pscreen;
    pscreen = new poly(polygon[current].length);
    for(int i = 0; i < polygon[current].length; i++) {
      pscreen.x[i] = x0 + polygon[current].x[i] * width;
      pscreen.y[i] = y0 + polygon[current].y[i] * height;
    }
    return pscreen.inside(mouseX, mouseY);
  }
  void onmousepress() {
    pressed = over();
  }
  int update() {
    int result = 0;
    if(pressed) {
      active = over();
      pressed = mousePressed;
    }
    if(active && (!mousePressed)) {
      pressed = false;
      active = false;
      result = current;
      current++;
      if(current > nstates)
        current = 1;
    }
    draw();
    return result;
  }
}

class slider // ---------------------------------------------------------------------
// based on processing90 example HScrollbar, but with stuff added
{
  int swidth, sheight;    // width and height of bar
  int xpos, ypos;         // x and y position of bar
  float spos, newspos;    // x position of slider
  int sposMin, sposMax;   // max and min values of slider
  int loose;              // how loose/heavy
  boolean over;           // is the mouse over the slider?
  boolean locked;
  int ratio;
  boolean showName, showVal, frozen;
  String name, displayVal;
  color highlightColor, dimColor;

  slider(int xp, int yp, int sw, int sh, int l) {
    swidth = sw;
    sheight = sh;
    xpos = xp;
    ypos = yp - sheight / 2;
    sposMin = xpos;
    sposMax = xpos + swidth - sheight;
    ratio = sposMax - sposMin;
    loose = l;
    showName = false;
    showVal = false;
    highlightColor = color(255, 0, 0); // color(153, 102, 0);
    dimColor = Blue;
    setPos(0);
    frozen = false;
  }

  void update() {
    if(over() && (!frozen))
      over = true;
    else
      over = false;
    if(mousePressed && over)
      locked = true;
    if(!mousePressed)
      locked = false;
    if(locked)
      newspos = constrain(mouseX - sheight / 2, sposMin, sposMax);
    if(abs(newspos - spos) > 1)
      spos = spos + (newspos - spos) / loose;
    draw();
  }
  int constrain(int val, int minv, int maxv) {
    return min(max(val, minv), maxv);
  }
  boolean over() {
    if((mouseX > xpos) && (mouseX < xpos + swidth) &&
       (mouseY > ypos) && (mouseY < ypos + sheight))
      return true;
    else
      return false;
  }
  void addName(String theName) {
    showName = true;
    name = theName;
  }
  void addDisplayVal(float val, int prec) {
    String valText;

    showVal = true;
    valText = str(round(val * pow(10, prec)) / pow(10, prec));
    displayVal = valText;
  }
  void freeze() {
    frozen = true;
    dimColor = color(255, 255, 255);
  }
  void unfreeze() {
    frozen = false;
    dimColor = color(102, 102, 102);
  }
  void draw() {
    noStroke();
    fill(grayColor);
    rect(xpos, ypos, swidth, sheight);
    if(over || locked)
      fill(lightBlue);
    else
      fill(dimColor);
    rect(spos, ypos, sheight, sheight);
    if(showName) {
      // textSize(round(1.1*sheight));
      textAlign(RIGHT);
      text(name, xpos - sheight / 3, ypos + sheight);
    }
    if(showVal) {
      // textSize(round(1.1*sheight));
      textAlign(LEFT);
      text(displayVal, xpos + swidth + sheight / 3, ypos + sheight);
    }
  }
  float getPos() {
    // convert spos to be values between 0 and 1
    return (spos - sposMin) / ratio;
  }
  void setPos(float pos) {
    // move the slider without user interaction
    spos = min(1, max(0, pos)) * ratio + sposMin;
    newspos = spos;
  }
}

class toolbar {
  multistatePolyButton[] buttons;
  boolean[] hide;
  float height, width, spacing, x0, y0;
  int length, nleft, nright;

  toolbar(float x, float y, float wd, float ht, float spc, int maxBtns) {
    buttons = new multistatePolyButton[maxBtns];
    hide = new boolean[maxBtns];
    length = 0;
    nleft = 0;
    nright = 0;
    x0 = x;
    y0 = y;
    width = wd;
    height = ht;
    spacing = spc;
  }

  float[] getPos() {
    float[] pos = new float[2];
    pos[0] = x0;
    pos[1] = y0;
    return pos;
  }
  int addButton(int nstates, String alignmt) {
    float x;
    int[] unocc = new int[4];
    unocc = unoccupied();
    if(alignmt == "right") {
      nright++;
      x = unocc[0] + unocc[2] - height;
    } else {
      nleft++;
      x = unocc[0];
    }
    length++;
    buttons[length - 1] = new multistatePolyButton(nstates, round(x), round(y0), round(height), round(height));
    buttons[length - 1].showName = true;
    buttons[length - 1].namePos = "bottom";
    hide[length - 1] = false;
    return length - 1;
  }
  int[] unoccupied() { // returns the rect that fills the unused space between the left & right buttons
    float xl = x0 + nleft * (height + spacing);
    float xr = (x0 + width) - nright * (height + spacing) + spacing;
    return defineRect(round(xl), round(y0), round(xr - xl), round(height));
  }
  int[] update() { // update all buttons; if any have been changed, return the state of the last one
    int u;
    int[] ret = new int[2];
    ret[0] = -1;
    ret[1] = 0;
    for(int i = 0; i < length; i++)
      if(!hide[i]) {
        u = buttons[i].update();
        if(u != 0) {
          ret[0] = i;
          ret[1] = u;
        }
      }
    return ret;
  }
  void setPos(float xx, float yy) {
    x0 = xx;
    y0 = yy;
  }
  void onmousepress() {
    for(int i = 0; i < length; i++)
      buttons[i].onmousepress();
  }
}

// -----------------------------------------------------------------------------------
// font stuff
// -----------------------------------------------------------------------------------

class myFonts {
  PFont flexifont, bigfont, medfont, smallfont, hugefont;
  // int smallfontsize, medfontsize, bigfontsize;

  myFonts() {}
  void createFonts(String fontname, int smallsize, int medsize, int bigsize, int hugesize) {
    smallfontsize = smallsize;
    medfontsize = medsize;
    bigfontsize = bigsize;
    hugefontsize = hugesize;
    medfont = createFont(fontname, medfontsize, true);
    smallfont = createFont(fontname, smallfontsize, true);
    bigfont = createFont(fontname, bigfontsize, true);
    hugefont = createFont(fontname, hugefontsize, true);
  }
  // added by d.marec
  void createFonts2(String fontname, int medsize) {
    medfontsize = medsize;
    medfont = createFont(fontname, medfontsize, true);
  }
  void setFont(String sz) {
    if(sz == "flexi")
      textFont(flexifont, medfontsize);
    else if(sz == "small")
      textFont(smallfont, smallfontsize);
    else if((sz == "med") || (sz == "medium"))
      textFont(medfont, medfontsize);
    else if(sz == "big")
      textFont(bigfont, bigfontsize);
    else if(sz == "huge")
      textFont(hugefont, hugefontsize);
    else
      setFont("flexi");
  }
  void setFont2(String sz) {
    if((sz == "med") || (sz == "medium"))
      textFont(medfont, medfontsize);
  }
  void textVAlign(String thetext, float x0, float y0, float txtsize, String align) {
    textAlign(LEFT);
    // align is "top", "bottom", or "middle".
    float y = y0;
    if(align == "top")
      y = y0 + 0.75 * txtsize;
    else if(align == "middle")
      y = y0 + 0.4 * txtsize;
    else if(align == "bottom") {
      // y = y0-0.25*txtsize;
    }
    text(thetext, x0, y);
  }
  // text in a box
  void Text(String thetext, float x0, float y0, float widthBox, float heightBox) {
    text(thetext, x0, y0, widthBox, heightBox);
  }
}

// -----------------------------------------------------------------------------------
// utilties
// -----------------------------------------------------------------------------------

int[] defineRect(float a, float b, float c, float d) {
  int[] r = {
    round(a), round(b), round(c), round(d)
  };
  return r;
}
color lighten(color col) {
  return shift(col, 1.4);
}
color darken(color col) {
  return shift(col, 0.7);
}
color shift(color col, float r) {
  return color(min(255, r * red(col)), min(255, r * green(col)), min(255, r * blue(col)));
}
color randomshift(color col) {
  return shift(col, random(0.5, 2));
}
color transparency(color col, float f) {
  return color(red(col), green(col), blue(col), f * 255);
}
float[][] flux_arc(float x0, float y0, float x1, float y1, float ang, int resolution) {
  // draws an arc (a line of _resolution_ segments) from (x0,y0) to (x1,y1)
  // that spans _arc_ degrees of a circle.
  float small = 1e-6;

  float dx, dy, D, m, xc, yc, xm, ym, angthing, th0m, th1m, dangm;
  float R, thStart, thEnd, mindist, th;
  int j, i;
  float[][] th0, th1, vertices;

  ang = -ang; // because y axis is inverted
  angthing = sqrt(1 + cos(ang)) / sqrt(1 - cos(ang));
  if(abs(ang) < small) { // if ang ~ 0, just draw a straight line
    // line(x0,y0,x1,y1);
    vertices = new float[2][2];
    vertices[0][0] = x0;
    vertices[1][0] = x1;
    vertices[0][1] = y0;
    vertices[1][1] = y1;
  } else {
    // find center of circle (xc,yc) relative to midpoint of starting points (xm,ym).
    // at first, there are two candidates (xm+xc,ym+yc), (xm-xc,ym-yc)
    xm = ((float) x0 + (float) x1) / 2;
    ym = ((float) y0 + (float) y1) / 2;
    dx = x1 - x0;
    dy = y1 - y0;
    D = sqrt(dx * dx + dy * dy);
    m = dy / dx;
    if(abs(m) > (1 / small)) { // endpoints on vertical line
      xc = -0.5 * angthing * D;
      yc = 0;
    } else if(abs(m) < small) { // endpoints on horizontal line
      xc = 0;
      yc = 0.5 * angthing * D;
    } else {
      xc = -0.5 * m / sqrt(m * m + 1) * angthing * D;
      yc = (-1 / m) * xc;
    }
    // now pick one of the candidate centers such that (x1,y1) is a smaller angle off it than (x0,y0)
    // (if ang > 0). There is probably a better way to this.
    th0m = atan2(y0 - ym + yc, x0 - xm + xc); // angle from (x0,y0) to (xm-xc,ym-yc)
    th1m = atan2(y1 - ym + yc, x1 - xm + xc);
    if(th0m < 0)
      th0m = th0m + 2 * PI;
    if(th1m < 0)
      th1m = th1m + 2 * PI;
    dangm = th0m - th1m;
    if(dangm > PI)
      dangm = dangm - 2 * PI;
    if(dangm < -PI)
      dangm = dangm + 2 * PI;
    if(((dangm > 0) && (ang > 0)) || ((dangm < 0) && (ang < 0))) {
      xc = -xc;
      yc = -yc;
    }
    // polar coords of points along the arc
    R = sqrt((y1 - ym - yc) * (y1 - ym - yc) + (x1 - xm - xc) * (x1 - xm - xc));
    // find the shortest way around the circle from th0 to th1: start with all options
    // (this is very elegant in Matlab)
    thStart = atan2(y0 - ym - yc, x0 - xm - xc);
    thEnd = atan2(y1 - ym - yc, x1 - xm - xc);
    th0 = new float[3][3];
    th1 = new float[3][3];
    for(i = 0; i < 3; i++) {
      th0[0][i] = thStart - 2 * PI;
      th0[1][i] = thStart;
      th0[2][i] = thStart + 2 * PI;
      th1[i][0] = thEnd - 2 * PI;
      th1[i][1] = thEnd;
      th1[i][2] = thEnd + 2 * PI;
    }
    mindist = (1 / small);
    for(j = 0; j < 3; j++)
      for(i = 0; i < 3; i++)
        if(abs(th1[j][i] - th0[j][i]) < mindist) {
          mindist = abs(th1[j][i] - th0[j][i]);
          thStart = th0[j][i];
          thEnd = th1[j][i];
        }
       // now assemble line from (R,th)
    vertices = new float[resolution][2];
    noFill();
    for(i = 0; i < resolution; i++) {
      th = thStart + ((float) i / ((float) resolution - 1) * (thEnd - thStart));
      vertices[i][0] = xm + xc + R *cos(th);
      vertices[i][1] = ym + yc + R *sin(th);
    } /*
       *  beginShape();
       *  for (i=0; i<resolution; i++) {
       *  vertex(vertices[i][0],vertices[i][1]);
       *  }
       *  endShape();
       */
  }
  return vertices;
}
void arrowhead(float[][] vertices, float pos, float L, int dir) {
  // draws an arrowhead at fractional position _pos_ along the line (vertices[:][0],vertices[:][1]).
  // dir=1 means forward-facing, dir=-1 means backward facing.
  // L is the length of the arrowhead in pixels.
  float arrowheadangle = 35;
  float baseindent = 0.25;
  float W = 2 *L *tan(arrowheadangle / 180 *PI / 2);

  int x_ = 0;
  int y_ = 1;
  float small = 1e-6;
  int ilen, i, di;
  float x0, y0, xp, yp, xa1, xa2, xb, xb1, xb2, ya1, ya2, yb, yb1, yb2, xl, xl1, xl2, yl, yl1, yl2;
  float len, m, sign_x, sign_y, Dsq1, Dsq2;

  // pick a point on the line for the arrow tip (x0,y0) and a point back along the line (xp,yp) to
  // calculate the local slope from
  ilen = vertices.length;
  i = constrain(round(pos * (ilen - 1)), 0, ilen - 1);
  x0 = vertices[i][x_];
  y0 = vertices[i][y_];
  len = 0;
  for(int j = 0; j < ilen - 1; j++)
    len = len + sqrt(sq(vertices[j + 1][0] - vertices[j][0]) + sq(vertices[j + 1][1] - vertices[j][1]));
  di = round(L / (1 - baseindent) / len * ilen);
  if(dir == 1) { // forward-pointing
    if(i == 0) {
      xp = x0 - (vertices[constrain(di, 1, ilen - 1)][x_] - vertices[0][x_]);
      yp = y0 - (vertices[constrain(di, 1, ilen - 1)][y_] - vertices[0][y_]);
    } else {
      xp = vertices[constrain(i - di, 0, i - 1)][x_];
      yp = vertices[constrain(i - di, 0, i - 1)][y_];
    }
  } else { // backward-pointing
    if(i == ilen - 1) {
      xp = x0 + (vertices[ilen - 1][x_] - vertices[constrain(ilen - 1 - di, 0, ilen - 2)][x_]);
      yp = y0 + (vertices[ilen - 1][y_] - vertices[constrain(ilen - 1 - di, 0, ilen - 2)][y_]);
    } else {
      xp = vertices[constrain(i + di, i + 1, ilen - 1)][x_];
      yp = vertices[constrain(i + di, i + 1, ilen - 1)][y_];
    }
  }
  // base corners of arrowhead (xa1,ya1), (xa2, ya2)
  // indented base point (xb,yb)
  m = ((float) yp - (float) y0) / ((float) xp - (float) x0);
  sign_y = (yp - y0) / abs(yp - y0);
  sign_x = (xp - x0) / abs(xp - x0);
  if(abs(m) > (1 / small)) {
    xa1 = -(W / 2) + x0;
    xa2 = (W / 2) + x0;
    ya1 = L * sign_y + y0;
    ya2 = ya1;
    xb = x0;
    yb = y0 + (1 - baseindent) * L * sign_y;
  } else if(abs(m) < small) {
    xa1 = L * sign_x + x0;
    xa2 = xa1;
    ya1 = -(W / 2) + y0;
    ya2 = (W / 2) + y0;
    xb = x0 + (1 - baseindent) * L * sign_x;
    yb = y0;
  } else {
    xl1 = L / sqrt(m * m + 1) + x0;
    xl2 = -L / sqrt(m * m + 1) + x0;
    yl1 = m * (xl1 - x0) + y0;
    yl2 = m * (xl2 - x0) + y0;
    Dsq1 = (xl1 - xp) * (xl1 - xp) + (yl1 - yp) * (yl1 - yp);
    Dsq2 = (xl2 - xp) * (xl2 - xp) + (yl2 - yp) * (yl2 - yp);
    if(Dsq1 < Dsq2) {
      xl = xl1;
      yl = yl1;
    } else {
      xl = xl2;
      yl = yl2;
    }
    xa1 = -(W / 2 / sqrt(1 + 1 / (m * m))) + xl;
    xa2 = -(xa1 - xl) + xl;
    ya1 = (-1 / m) * (xa1 - xl) + yl;
    ya2 = (-1 / m) * (xa2 - xl) + yl;
    xb1 = -((1 - baseindent) * L / sqrt(m * m + 1)) + x0;
    xb2 = -(xb1 - x0) + x0;
    yb1 = m * (xb1 - x0) + y0;
    yb2 = m * (xb2 - x0) + y0;
    Dsq1 = (xb1 - xp) * (xb1 - xp) + (yb1 - yp) * (yb1 - yp);
    Dsq2 = (xb2 - xp) * (xb2 - xp) + (yb2 - yp) * (yb2 - yp);
    if(Dsq1 < Dsq2) {
      xb = xb1;
      yb = yb1;
    } else {
      xb = xb2;
      yb = yb2;
    }
  }
  // draw it
  beginShape();
  vertex(x0, y0);
  vertex(xa1, ya1);
  vertex(xa2, ya2);
  endShape(CLOSE);
}
