class HScrollbar
{
  int swidth, sheight;    // width and height of bar
  int xpos, ypos;         // x and y position of bar
  float spos, newspos;    // x position of slider
  int sposMin, sposMax;   // max and min values of slider
  int loose;              // how loose/heavy
  boolean over;           // is the mouse over the slider?
  boolean locked;
  boolean show = false;
  float ratio;

  HScrollbar(int xp, int yp, int sw, int sh, int l) {
    swidth = sw;
    sheight = sh;
    int widthtoheight = sw - sh;
    ratio = (float) sw / (float) widthtoheight;
    xpos = xp - 3 * swidth / 4;
    ypos = yp - sheight / 2;
    spos = xpos; // + swidth/2 - sheight/2;
    newspos = spos;
    sposMin = xpos;
    sposMax = xpos + swidth - sheight;
    loose = l;
  }

  void update() {
    if(over()) {
      newspos = width / 2;  // constrain(mouseX-sheight/2, sposMin, sposMax);
    } else {
      newspos = 0;
    }
    if(abs(newspos - spos) > 1) {
      spos = spos + (newspos - spos) / loose;
    }
  }
  boolean over() {
    if((mouseX > xpos) && (mouseX < xpos + swidth) &&
       (mouseY > ypos) && (mouseY < ypos + sheight))
    {
      return true;
    } else {
      return false;
    }
  }
  void display() {
    fill(255);
    strokeWeight(0.1);
    if(over()) {
      fill(0, 40, 63);
    } else {
      fill(255, 150, 0);
    }
    rect(sheight / 2, ypos, sheight * 5, sheight);
    fill(130);
    textFont(font, 11);
    text("I N F O >>>", sheight / 2 + sheight / 5, ypos + 4 * sheight / 5);
  }
  float getPos() {
    // Convert spos to be values between
    // 0 and the total width of the scrollbar
    return spos * ratio;
  }
}
