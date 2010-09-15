import ddf.minim.*;

Minim minim;
AudioPlayer kick;
  AudioInput in;
 AudioOutput out;
 
void setup()
{
  size(512, 200, P2D);
  // always start Minim before you do anything with it
  minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, 512);
  out = minim.getLineOut(Minim.STEREO);
  // load BD.mp3 from the data folder with a 1024 sample buffer
  // kick = Minim.loadSample("BD.mp3");
  // load BD.mp3 from the data folder, with a 512 sample buffer
  kick = minim.loadFile("/home/vthierry/Work/culsci/javascool/javascool_V3/trunk/org.javascool/sketchbook/ExplorationSonore/data/music/Ahmed_Ex3.wav", 2048);
}

void draw()
{
  background(0);
  stroke(255);
  // use the mix buffer to draw the waveforms.
  // because these are MONO files, we could have used the left or right buffers and got the same data
  for(int i = 0; i < in.bufferSize() - 1; i++)
      {
        //println("buffer: " + in.bufferSize());
        float x1 = map(i, 0, in.bufferSize(), 0, width);
        float x2 = map(i+1, 0, in.bufferSize(), 0, width);
        line(x1, 40 + in.left.get(i)*100, x2, 40 + in.left.get(i+1)*100);
        line(x1, 120 + in.right.get(i)*100, x2, 120 + in.right.get(i+1)*100);
      } 
}

void keyPressed()
{
  if ( key == 'k' ) kick.loop();
  
}

void stop()
{
  // always close Minim audio classes when you are done with them
  kick.close();
  minim.stop();
  
  super.stop();
}



