/*////////////////////////////////////////////////////////////////////////////
 *
 * 11.2010 Cécile P-L for Fuscia - ccl.picard@gmail.com -
 *
 * Inspired by Record Line In
 * by Damien Di Fede.
 *
 */

import ddf.minim.*;

Minim minim;
AudioInput in;
AudioRecorder recorder;

int nb = 0;
boolean isSaving = false;
boolean warning = false;
String[] filenames;
String path;

void setup() {
  size(512, 200, P2D);
  textMode(SCREEN);

  minim = new Minim(this);
  path = sketchPath + "/out"; // "../BoiteAMusique/data/effects";

  // get a stereo line-in: sample buffer length of 2048
  // default sample rate is 44100, default bit depth is 16
  in = minim.getLineIn(Minim.STEREO, 2048);
  // create a recorder that  will record from the input to the filename specified, using buffered recording
  // buffered recording means that all captured audio will be written into a sample buffer
  // then when save() is called, the contents of the buffer will actually be written to a file
  // the file will be located in the sketch's root folder.
  // recorder = minim.createRecorder(in, "rec" + int(nb) + ".wav", true);
  recorder = minim.createRecorder(in, "rec" + int (nb) + ".wav", true);
  textFont(createFont("Arial Bold", 12));
}
void draw() {
  background(100); // 0);
  stroke(255, 150, 0); // stroke(255);
  strokeWeight(3);
  smooth();
  // draw the waveforms
  // the values returned by left.get() and right.get() will be between -1 and 1,
  // so we need to scale them up to see the waveform
  for(int i = 0; i < in.bufferSize() - 1; i++) {
    line(i, 50 + in.left.get(i) * 100, i + 1, 50 + in.left.get(i + 1) * 100);
    line(i, 150 + in.right.get(i) * 100, i + 1, 150 + in.right.get(i + 1) * 100);
  }
  // Instructions
  text(" ' r ': record/stop recording, \n" +
       " ' s ': save record.", width - 170, 15);
  if(recorder.isRecording()) {
    if(warning)
      text("Stop recording before saving.", 5, 15);
    else {
      isSaving = false;
      text("Currently recording n°..." + nb, 5, 15);
    }
  } else if(isSaving)
    text("Saved!", 5, 15);
  else
    warning = false;
}
void keyReleased() {
  if(key == 'r') {
    // to indicate that you want to start or stop capturing audio data, you must call
    // beginRecord() and endRecord() on the AudioRecorder object. You can start and stop
    // as many times as you like, the audio data will be appended to the end of the buffer
    // (in the case of buffered recording) or to the end of the file (in the case of streamed recording).
    if(recorder.isRecording())
      recorder.endRecord();
    else {
      recorder = minim.createRecorder(in, path + "/rec" + int (nb) + ".wav", true);
      recorder.beginRecord();
    }
  }
  if(key == 's') {
    if(recorder.isRecording())
      warning = true;
    else {
      isSaving = true;
      // we've filled the file out buffer,
      // now write it to the file we specified in createRecorder
      // in the case of buffered recording, if the buffer is large,
      // this will appear to freeze the sketch for sometime
      // in the case of streamed recording,
      // it will not freeze as the data is already in the file and all that is being done
      // is closing the file.
      // the method returns the recorded audio as an AudioRecording,
      // see the example  AudioRecorder >> RecordAndPlayback for more about that
      recorder.save();
      nb++;

      println("Done saving.");
    }
  }
  if(key == 'i') {
    println("Listing all filenames in a directory: ");
    filenames = listFileNames(path);
    println(filenames);
    println(filenames.length);
  }
}
// This function returns all the files in a directory as an array of Strings
String[] listFileNames(String dir) {
  File file = new File(dir);
  if(file.isDirectory()) {
    String names[] = file.list();
    return names;
  } else
    // If it's not a directory
    return null;
}
void stop() {
  // always close Minim audio classes when you are done with them
  in.close();
  minim.stop();

  super.stop();
}
