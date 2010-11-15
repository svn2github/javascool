class Potar
{
  float x,y,yp;
  float valeur=1;//50;//0.5;

  Potar(float x_, float y_)
  {
    x=x_;
    y=y_;
    yp=y;
  }

  void display()
  {
    rectMode(CENTER);
    fill(50);
    noStroke();
    rect(x,y,60,225);
    stroke(255);
    strokeWeight(4);
    line(x,y-100,x,y+100);
    strokeWeight(1);
    fill(10); 
    rect(x,yp,45,22);
    strokeWeight(3);
    line(x+23,yp,x-23,yp);
    //stroke(0);
  } 

  boolean check(float x_,float y_)
  {
    if(x_>x-22 && x_<x+22 && y_>y-100 && y_<y+100)
    {
      return true;
    }
    else
    {
      return false;
    }
  }


  void move(float y_)
  {
    yp=y_;
    yp=constrain(yp,y-100,y+100);  
    valeur = map(yp,y+90,y-100,0,2);
    valeur = constrain(valeur,0,2);
    println(valeur);  
  }

  float getValue()
  {
    return valeur;
  }


}

