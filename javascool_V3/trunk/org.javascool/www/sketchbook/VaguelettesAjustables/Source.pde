class Source{
  public float x;
  public float y;
  public float amp;
  public float wavelength;
  
  public Source(float _x,float _y, float _amp,float _wave) {
    x=_x;y=_y;amp=_amp;wavelength=_wave;
  }
  
  public float getPart(float xx,float yy,float time) {
    float distt=mag(xx-x,yy-y);
    return amp*(float)Math.cos(((time-distt)/wavelength));
  }

  public void fade() {
    amp*=DAMPING; // damping factor
  }  
}
