class textfield {
  float xBox, yBox, wBox, hBox;
  String currentInput, lastInput;
  boolean pressed, focus; // mouse over, mouse pressed, box selected (waiting for text), le texte a  été modifié et c'est fini
  color fillColor, strokeColor, textColor;

  textfield() {
    lastInput = new String();
    currentInput = "";
  }

  textfield(float x, float y) {
    xBox = x;
    yBox = y;
    lastInput = new String();
    currentInput = "";
  }
  void setInitText(String txt) {
    lastInput = txt;
  }
  void setPosition(float x, float y) {
    xBox = x;
    yBox = y;
  }
  void setDimension(float w, float h) {
    wBox = w;
    hBox = h;
  }
  float[] getPosition() {
    float[] pos = {
      xBox, yBox
    };
    return pos;
  }
  float[] getDimension() {
    float[] dim = {
      wBox, hBox
    };
    return dim;
  }
  String getValue() {
    return lastInput;
  }
  void setValue(String texte) {
    lastInput = texte;
  }
  void display() {
    fill(Blue);
    textAlign(LEFT, CENTER);
    if(!focus)
      text(lastInput, xBox, yBox + hBox / 2);

      /*
       *  stroke(120);
       *  line(xBox,yBox,xBox+textWidth(lastInput)+5,yBox);
       *  line(xBox,yBox+hBox,xBox+textWidth(lastInput)+5,yBox+hBox);
       */
    else if(focus)
      text(currentInput, xBox, yBox + hBox / 2);

      /*
       *  stroke(120);
       *  line(xBox,yBox,xBox+textWidth(currentInput)+5,yBox);
       *  line(xBox,yBox+hBox,xBox+textWidth(currentInput)+5,yBox+hBox);
       */
  }
  void display(float x, float y, float w, float h) {
    fill(0);
    textAlign(LEFT, CENTER);
    if(!focus) {
      text(lastInput, xBox, yBox + hBox / 2);
      stroke(120);
      line(x, y, x + textWidth(lastInput) + 5, y);
      line(x, y + h, x + textWidth(lastInput) + 5, y + h);
    } else if(focus) {
      text(currentInput, xBox, yBox + hBox / 2);
      stroke(120);
      line(x, y, x + textWidth(currentInput) + 5, y);
      line(x, y + h, x + textWidth(currentInput) + 5, y + h);
    }
  }
  void display(float x, float y, float w, float h, String initTxt) {
    fill(0);
    textAlign(LEFT, CENTER);
    if(!focus)
      text(lastInput, x, y + h / 2);
    else if(focus)
      text(currentInput, xBox, yBox + hBox / 2);
    stroke(120);
    line(x, y, x + textWidth(currentInput) + 5, y);
    line(x, y + h, x + textWidth(currentInput) + 5, y + h);
  }
  boolean over(float X, float Y) {
    boolean test;
    if(((X > xBox) && (X < xBox + wBox)) && ((Y > yBox) && (Y < yBox + hBox)))
      test = true;
    else
      test = false;
    return test;
  }
  void focusOn() {
    if(key == ENTER) {
      lastInput = currentInput;
      currentInput = "";
      focus = false;
    } else if((key == BACKSPACE) && (currentInput.length() > 0))
      currentInput = currentInput.substring(0, currentInput.length() - 1);
    else if(key == CODED) {
      if(key == SHIFT)
        currentInput = "";
    } else
      currentInput = currentInput + key;
  }
}

