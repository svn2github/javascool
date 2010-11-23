class Source {
  
  float x, y; //(positionnement dans la librarie)
  int type;
  int ref;
  String refE;
  
  float[][] tab = new float[2][16];
  
  Source(float x_, float y_, int type_, int ref_)
  {
    x = x_;
    y = y_;
    type = type_;
    ref = ref_; // nb in the tab?
  }
  
  Source(float x_, float y_, int type_, String refE_)
  {
    x = x_;
    y = y_;
    type = type_;
    refE = refE_; // nb in the tab?
  }
  
  boolean posSource(int mx, int my, int type) {
    
    boolean resp=false;
    if(type==0 || type==1) {
    if(mx>=x-2 && mx<=x+25)
    {
      if(my>=y-10 && my<=y+5)
      {
        resp=true;
      }
    }
    return resp;
    } else {
      
      if(mx>=x-2 && mx<=x+50)
    {
      if(my>=y-10 && my<=y+5)
      {
        resp=true;
      }
    }
    return resp;
    }
    
  }
  
}
