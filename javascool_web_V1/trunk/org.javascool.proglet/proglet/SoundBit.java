/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/
// Credits: this widget has been developped thanks to http://www.jsresources.org Matthias Pfisterer's OscillatorPlayer example.

package proglet;

// Used to define an audio stream
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import java.io.ByteArrayInputStream;
import java.io.IOException;

// Used to play an audio stream
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

/** This widget defines a general sound bit.
 * @see <a href="MusicScore.java">source code</a>
 */
public class SoundBit extends AudioInputStream {
  /** Sampling frequency. */
  static public final float SAMPLING = 44100.0F;

  /** Constructs a 16 bit, stereo, sound bit, with a standard PCM 44.1 KHz signed audio format.
   * @param length Sound bit length in second.
   */
  public SoundBit(double length) {
    super(new ByteArrayInputStream(new byte[0]),
	  // Data format: 16 bit stereo: each frame contains two bytes x two channels = four bytes
	  new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
			  SAMPLING, // Sample rate in Hz
			  16,       // Sampling bits
			  2,        // Number of channels
			  4,        // Frame size in bytes
			  SAMPLING, // Frame rate in Hz
			  false),   // Little-endian byte order
	  (long) (length * SAMPLING));
    frameLimit = frameLength;
  }

  /** Defines the sound function.
   * <div>This methods is to be overloaded to define your own sound.</div>
   * <div>The default is a tone with white-noise.</div>
   * @param channel Left 'l' or right 'r' channel.
   * @param index  Sound time index. The time in second writes: <tt>time = index / SAMPLING</tt>.
   * @return The sound value between 0.0 and 1.0 (maximal amplitude).
   */
  public double get(char channel, long index) {
    return 0.5 * Math.sin(2 * Math.PI * ((channel == 'l' ? 200 : 100) + 500 * Math.sin(index/SAMPLING)) / SAMPLING * index) + 
      (0.5 + 0.5 * Math.sin(0.44 * index/SAMPLING)) * Math.random();
  }

  // Implementation of the stream read methods
  /**  Reads up to byteLength from the stream into the data array of bytes, starting from offset.
   * @return The total number of bytes read into the buffer, or -1 is there is no more to read because the end of the stream has been reached. 
   */
  public int read(byte[] data, int offset, int byteLength) throws IOException {
    // Adjust the read size
    byteLength = offset + byteLength > data.length ? data.length - offset : byteLength;
    long nFrame = byteLength / frameSize; if (nFrame > frameLength - framePos) nFrame = frameLength - framePos; byteLength = (int) nFrame * frameSize;
    // Reads sound samples
    for(int n = 0, i = offset; n < nFrame; n++, i += frameSize) {
      // Gets and converts sample's values: max amplitude is: 2^16-1=65535
      long l = Math.round(65535 * get('l', framePos)), r =  Math.round(65535 * get('r', framePos)); framePos++;
      if (l < 0) l =0; if(l > 65535) l = 65535; if (r < 0) r = 0; if(r > 65535) r = 65535;
      // Reports in buffer: this is valid for 16 bit stereo, little endian
      data[i + 0] = (byte) (l & 0xFF);
      data[i + 1] = (byte) ((l >>> 8) & 0xFF);
      data[i + 2] = (byte) (r & 0xFF);
      data[i + 3] = (byte) ((r >>> 8) & 0xFF);
    }
    return byteLength <= 0 ? -1 : byteLength;
  }
  /**  Returns the number of bytes that can be read (or skipped over) from this stream. */
  public int available() { return (int) (frameSize * (frameLength - framePos)); }
  /**  Repositions this stream to the initial position or the position at the time the mark method was last called on this stream. */
  public void reset() throws IOException  { if(framePos > frameLimit) throw new IOException("Mark limit exceeded"); framePos = frameMark; }   
  /** Marks the current position in this input stream, byteLimit, if positive, being the maximum limit of bytes that can be read before the mark becomes invalid. */
  public void mark(int byteLimit) { frameMark = framePos; frameLimit = byteLimit > 0 ? framePos + byteLimit / frameSize : frameLength; }
  /** Returns true because the mark and reset methods are supported. */
  public boolean markSupported() { return true; }
  /** Skips over and discards byteSize bytes of data (or less, if less data available) from this stream. */
  public void skip(int byteSize) { framePos += byteSize / frameSize; if (framePos > frameLength) framePos = frameLength; }
  /** Forbidden method: do not use (since frames have not one byte size). */
  public int read() throws IOException { throw new IOException("SoundBit has no one byte frame size, operation forbidden"); }
  /** Returns a string representation of this stream, for debugging purpose. */
  public String toString() { return "["+super.toString()+" length = "+(frameLength/SAMPLING)+"s pos = "+framePos+" < "+frameLength+" mark = "+frameMark+" < "+frameLimit+"]"; }
  private long frameMark = 0, frameLimit = 0;

  /** Plays the sound on the standard audio system line. */
  public void play() throws IOException {
    try {
      // Opens the audio line
      SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, getFormat()));
      line.open(getFormat());
      line.start();
      // In/out the sound via a buffer
      {
	final int size = (int) (frameSize * SAMPLING); byte data[] = new byte[size];
	for(long t = 0; t < frameSize * frameLength; t += size) {
	  int s = read(data); line.write(data, 0, s);
	}
      }
      line.close();
    } catch(LineUnavailableException e) { throw new IOException(e.toString()); }
  }
  
  /** Plays a soundbit.
   * @param usage <tt>java SoundBit [sound-bit-class-name] [length-in-second]</tt>
   * Default is a tone with white-noise during 10 seconds.
   */
  public static void main(String usage[]) { 
    String sound = usage.length == 1 ? usage[0] : "proglet.SoundBit";
    String length = usage.length == 2  && usage[1].matches("[0-9\\.]+") ? usage[1] : usage.length == 1 && usage[0].matches("[0-9\\.]+") ? usage[0] : "10";
    try { 
      SoundBit s = ((SoundBit) Class.forName(sound).getConstructor(Double.TYPE).newInstance(Double.valueOf(length)));
      System.out.print("playing .."); System.out.flush(); s.play(); System.out.print(" done."); 
    } catch(Exception e) { System.err.println(e); e.printStackTrace(); }
  }
}
