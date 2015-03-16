package org.javascool.proglets.rubik;

class Alpha {
  private long startTime;
  private int length;
  private boolean paused;

  public Alpha(int length) {
    this.length = length;
  }

  public float status() {
    long currentTime = System.currentTimeMillis();
    long delay = (currentTime - startTime);
    return delay / (float) length;
  }
  
  public static float value(float status) {
    return status % 1f;
  }
  
  public static int count(float status) {
    return (int)Math.floor(status);
  }
  
  public void setLength(int length) {
    if (length==this.length)
      return;
    long currentTime = System.currentTimeMillis();
    long delay = (currentTime - startTime) % this.length;
    startTime = currentTime-delay*length/this.length;
    this.length = length;
  }
  
  public int getLength() {
    return length;
  }

  public void reset() {
    startTime = System.currentTimeMillis();
    paused = false;
  }

  public boolean isPaused() {
    return paused;
  }
  
  public void pause() {
    paused = true;
  }

}
