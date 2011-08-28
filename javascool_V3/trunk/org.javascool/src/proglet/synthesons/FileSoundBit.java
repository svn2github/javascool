/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package proglet.synthesons;

import org.javascool.SoundBit;

// Used to define an audio stream file
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;

// Used to read an audio file
import java.net.URL;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.javascool.Utils;

// Used to dump midi sounds
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.SoundbankResource;

import java.util.HashMap;

/** Defines a data-file sound-bit wrapper.
 * @see <a href="FileSoundBit.java.html">code source</a>
 * @serial exclude
 */
public class FileSoundBit extends SoundBit {
  /** Constructs a sound defined from two buffer files.
   * @param location Audio file path: either a file-name or an URL-name or an URI of the form <tt>midi:<i>name</i></tt> allowing to load a midi sound.
   *
   * @throws RuntimeException if an I/O exception occurs during command execution.
   *
   * @return This, allowing to use the <tt>SoundBit pml= new SoundBit().reset(..)</tt> construct.
   */
  public SoundBit reset(String location) {
    AudioInputStream stream;
    if(location.startsWith("midi:")) {
      getMidiNames();
      String name = location.substring(5);
      if(midis.containsKey(name))
        stream = midis.get(name);
      else throw new RuntimeException("undefined midi sound " + name);
    } else {
      try {
        stream = AudioSystem.getAudioInputStream(Utils.toUrl(location));
      } catch(UnsupportedAudioFileException e) { throw new RuntimeException(e);
      } catch(IOException e) { throw new RuntimeException(e);
      }
    }
    SoundBit.checkFormat(stream);
    c = stream.getFormat().getChannels();
    s = stream.getFormat().getFrameSize();
    buffer = new byte[(int) stream.getFrameLength() * stream.getFormat().getFrameSize()];
    try {
      for(int offset = 0, length; (length = stream.read(buffer, offset, buffer.length - offset)) != -1; offset += length) {}
      stream.close();
    } catch(IOException e) { throw new RuntimeException(e);
    }
    name = location;
    length = stream.getFrameLength() / SAMPLING;
    return this;
  }
  public double get(char channel, long index) {
    int i = (int) index * s + (c == 1 || channel == 'l' ? 0 : 2);
    if((buffer == null) || (i < 0) || (i >= buffer.length))
      return 0;
    int h = buffer[i + 1], l = buffer[i], v = ((128 + h) << 8) | (128 + l);
    return 1 * (v / 32767.0 - 1);
  }
  public double get(char channel, double time) {
    return get(channel, (long) (SAMPLING * time));
  }
  private int c, s;
  private byte[] buffer;
  /**/ public void setLength(double length) { throw new IllegalStateException("Cannot adjust length of buffered sound-bit of name " + getName());
  }
  /** Gets available midi sound names.
   * @return Available midi sound name. Usually "bass2", "bass_drum", "bass", "brass_section", "clarinet", "closed_hi-hat", "crash_cymbal", "distorted_guitar", "epiano", "flute", "grand_piano", "guitar_noise", "guitar", "horn", "melodic_toms", "oboe", "och_strings", "open_hi-hat", "organ", "piano_hammer", "reverse_cymbal", "sax", "side_stick", "snare_drum", "strings", "timpani", "tom", "trombone", "trumpet".
   */
  public static String[] getMidiNames() {
    if(midis == null) {
      midis = new HashMap<String, AudioInputStream>();
      try {
        Soundbank s = MidiSystem.getSynthesizer().getDefaultSoundbank();
        for(int i = 0; i < s.getResources().length; i++) {
          SoundbankResource r = s.getResources()[i];
          if((r.getDataClass() != null) && r.getDataClass().getName().equals("javax.sound.sampled.AudioInputStream")) {
            String name = r.getName().toLowerCase().replaceAll(" ", "_");
            AudioInputStream stream = (AudioInputStream) r.getData();
            try {
              SoundBit.checkFormat(stream);
              midis.put(name, stream);
            } catch(IllegalArgumentException e) {}
          }
        }
      } catch(MidiUnavailableException e) { throw new RuntimeException(e.toString());
      }
    }
    return midis.keySet().toArray(new String[midis.size()]);
  }
  private static HashMap<String, AudioInputStream> midis = null;

  /** Plays a sound.
   * @param location Audio file path: either a file-name or an URL-name or an URI of the form <tt>midi:<i>name</i></tt> allowing to load a midi sound.
   */
  public static void play(String location) {
    play_location = location;
    new Thread(new Runnable() {
                 public void run() {
                   new FileSoundBit().reset(play_location).play();
                 }
               }
               ).start();
  }
  private static String play_location;
}
