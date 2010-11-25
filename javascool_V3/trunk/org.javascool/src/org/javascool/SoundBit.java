/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package org.javascool;

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

// Used to play/save an audio file
import java.io.File;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFileFormat;

/** This widget defines a sound bit and allows to use sampled sound resources.
 * @see <a href="SoundBit.java.html">source code</a>
 * @serial exclude
 */
public class SoundBit implements Widget {
  private static final long serialVersionUID = 1L;

  /** Defines the sound function.
   * <div>One <tt>get()</tt> methods i to be overloaded to define your own sound.</div>
   * <div>By contract this method is called for <tt>index = 0, 1, 2, ..</tt> in consecutive increasing order.</tt>
   * @param channel Left 'l' or right 'r' channel.
   * @param index  Sound time index. The time in second writes: <tt>time = index / SAMPLING</tt>.
   * @return The sound value between -1.0 and 1.0 (maximal amplitude, linear scale).
   */
  public double get(char channel, long index) {
    return get(channel, index / SAMPLING);
  }
  /** Defines the sound function.
   * <div>One <tt>get()</tt> methods i to be overloaded to define your own sound.</div>
   * <div>By contract this method is called for <tt>index = 0, 1, 2, ..</tt> in consecutive increasing order.</tt>
   * @param channel Left 'l' or right 'r' channel.
   * @param time  Sound time in second.
   * @return The sound value between -1.0 and 1.0 (maximal amplitude, linear scale).
   */
  public double get(char channel, double time) {
    return 0;
  }
  /** Resets the sound properties.
   * <a name ="reset"></a><div>This methods is to be overloaded to manage your own sound property.</div>
   * @param definition The sound properties given as a string: the semantic depends on the sound, it is documented for each sound's type.
   * @return This, allowing to use the <tt>SoundBit pml= new SoundBit().reset(..)</tt> construct.
   */
  public SoundBit reset(String definition) {
    return this;
  }
  /** Gets the sound name.
   * @return Either the sound java class or the sound file name.
   */
  public String getName() {
    return name == null ? getClass().getName() : name;
  }
  protected String name = null;

  /** Sets the stream length.
   * @param length Stream length in second. If -1, the length remains undefined (in fact maximal).
   * @throws IllegalStateException If it is a buffered sound-bit of fixed length, thus not adjustable.
   */
  public void setLength(double length) {
    this.length = length;
  }
  /** Gets the stream length.
   * @return The stream length in second.
   */
  public double getLength() {
    return length >= 0 ? length : Long.MAX_VALUE / FRAME_SIZE / SAMPLING;
  }
  protected double length = 0;

  /** Returns the 16 bit, stereo, sound bit, PCM standard, signed PCM, 44.1 KHz sampled, audio stream, supporting mark and reset.
   * <div>- Notice: this stream is never closed, but can be reset when to be reused.</div>
   * @return A new <a href="http://java.sun.com/javase/6/docs/api/javax/sound/sampled/AudioInputStream.html">javax.sound.sampled.AudioInputStream</a> corresponding to this sound.
   */
  public AudioInputStream getStream() {
    return new Stream(length);
  }
  /** Sampling frequency. */
  static public final float SAMPLING = 44100.0F;
  static private final int FRAME_SIZE = 4;

  /** Defines the audio stream wrapper. */
  private class Stream extends AudioInputStream {
    private static final long serialVersionUID = 1L;

    /** Constructs a 16 bit, stereo, sound bit, with a standard PCM 44.1 KHz signed audio format.
     * @param length Stream length in second. If -1, the length remains undefined (in fact maximal).
     */
    public Stream(double length) {
      super(new ByteArrayInputStream(new byte[0]),
            // Data format: 16 bit stereo: each frame contains two bytes x two channels = four bytes
            new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                            SAMPLING, // Sample rate in Hz
                            16, // Sampling bits
                            2, // Number of channels
                            FRAME_SIZE, // Frame size in bytes
                            SAMPLING, // Frame rate in Hz
                            false), // Little-endian byte order
            length >= 0 ? (long) (length * SAMPLING) : Long.MAX_VALUE / FRAME_SIZE);
    }
    // Implementation of the stream read methods

    /**  Reads up to byteLength from the stream into the data array of bytes, starting from offset.
     * @return The total number of bytes read into the buffer, or -1 is there is no more to read because the end of the stream has been reached.
     */
    public int read(byte[] data, int offset, int byteLength) {
      // Adjust the read size
      byteLength = offset + byteLength > data.length ? data.length - offset : byteLength;
      long nFrame = byteLength / frameSize;
      if(nFrame > frameLength - framePos)
        nFrame = frameLength - framePos;
      byteLength = (int) nFrame * frameSize;
      // Reads sound samples
      for(int n = 0, i = offset; n < nFrame; n++, i += frameSize) {
        // Gets and converts sample's values: max amplitude is: 2^16-1=65535
        long l = Math.round(32767.0 * (1.0 + get('l', framePos))), r = Math.round(32767.0 * (1.0 + get('r', framePos)));
        framePos++;
        if(l < 0)
          l = 0;
        if(l > 65535)
          l = 65535;
        if(r < 0)
          r = 0;
        if(r > 65535)
          r = 65535;
        // Reports in buffer: this is valid for 16 bit stereo, little endian
        data[i + 0] = (byte) ((l & 0xFF) - 128);
        data[i + 1] = (byte) (((l >>> 8) & 0xFF) - 128);
        data[i + 2] = (byte) ((r & 0xFF) - 128);
        data[i + 3] = (byte) (((r >>> 8) & 0xFF) - 128);
      }
      return byteLength <= 0 ? -1 : byteLength;
    }
    /**  Reads bytes from the audio input stream and stores them into the buffer array. */
    public int read(byte[] data) {
      return read(data, 0, data.length);
    }
    /**  Returns the number of bytes that can be read (or skipped over) from this stream. */
    public int available() {
      return (int) (frameSize * (frameLength - framePos));
    }
    /**  Repositions this stream to the initial position or the position at the time the mark method was last called on this stream. */
    public void reset() {
      if(framePos > frameLimit) throw new IllegalStateException("Mark limit exceeded");
      framePos = frameMark;
    }
    /** Marks the current position in this input stream, byteLimit, if positive, being the maximum limit of bytes that can be read before the mark becomes invalid. */
    public void mark(int byteLimit) {
      frameMark = framePos;
      frameLimit = byteLimit > 0 ? framePos + byteLimit / frameSize : frameLength;
    }
    /** Returns true because the mark and reset methods are supported. */
    public boolean markSupported() {
      return true;
    }
    /** Skips over and discards byteSize bytes of data (or less, if less data available) from this stream. */
    public void skip(int byteSize) {
      framePos += byteSize / frameSize;
      if(framePos > frameLength)
        framePos = frameLength;
    }
    /** Forbidden method: do not use (since frames have not one byte size). */
    public int read() { throw new IllegalStateException("SoundBit has no one byte frame size, operation forbidden");
    }
    /** Returns a string representation of this stream, for debugging purpose. */
    public String toString() {
      return "[" + super.toString() + " length = " + (frameLength / SAMPLING) + "s pos = " + framePos + " < " + frameLength + " mark = " + frameMark + " < " + frameLimit + "]";
    }
    /** Closes this audio input stream. */
    public void close() {}
    private long frameMark = 0, frameLimit = Long.MAX_VALUE / FRAME_SIZE;
  }

  /** Returns a string representation of this stream, for debugging purpose. */
  public String toString() {
    return "SoundBit \"" + getName() + "\" : " + getStream();
  }
  /** Saves the sound in a waveformn audio vector file.
   * @param path The file path, the <tt>.wav</tt> extension is automatically added.
   * @throws IOException If it is not possible to write the data at the given path.
   */
  public void save(String path) throws IOException {
    AudioSystem.write(getStream(), AudioFileFormat.Type.WAVE, new File(path.replaceFirst("\\.wav$", "") + ".wav"));
  }
  /** Plays the sound on the standard audio system line.
   * @param period Period in second at which the sample() method is called to generate an event. Default is 0 without any sampling.
   * @throws RuntimeException If the audio system is unavailable or does not support the current: 16 bit, stereo, standard PCM, 44.1 KHz, signed, little-endian audio format.
   */
  public void play(double period) {
    AudioInputStream stream = getStream();
    // Plays a stream on the standard audio system line.
    try {
      // Opens the audio line
      SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, stream.getFormat()));
      line.open(stream.getFormat());
      line.start();
      // In/out the sound via a buffer
      {
        final int size = (int) (FRAME_SIZE * (period <= 0 ? 1 : period) * SAMPLING);
        int n = 0;
        byte data[] = new byte[size];
        for(long t = 0; t < FRAME_SIZE * stream.getFrameLength(); t += size, n++) {
          if(period > 0)
            sample(n);
          int s = stream.read(data, 0, size);
          if(s > 0)
            line.write(data, 0, s);
          Macros.sleep(0);
        }
      }
      line.close();
    } catch(IOException e) { throw new RuntimeException(e.toString());
    } catch(LineUnavailableException e) { throw new RuntimeException(e.toString());
    }
  }
  /**/public void play() {
    play(0);
  }
  /** Called at each sampling during a play.
   * - The sampling period is defined in the play method.
   * @param n Sampling index. This method is called at each period of time, with <tt>n = 0, 1, ..</tt>.
   */
  public void sample(int n) {}

  /** Defines the data-buffer sound-bit wrapper. */
  private static class DataSoundBit extends SoundBit {
    /** Constructs a sound defined from two buffer files.
     * @param name Sound name.
     * @param left Left buffer data.
     * @param right Right buffer data if any. Set to null for a monophonic sound.
     */
    public DataSoundBit(String name, double[] left, double[] right) {
      if(left == null) throw new IllegalArgumentException("Undefined left channel data");
      if((right != null) && (left.length != right.length))
        new IllegalArgumentException("Left and right channel length differs: " + left.length + " != " + right.length);
      this.left = left;
      this.right = right == null ? left : right;
      this.name = name;
      length = left.length / SAMPLING;
    }
    public double get(char channel, long index) {
      return (index < 0 || index >= left.length) ? 0 : channel == 'r' ? right[(int) index] : left[(int) index];
    }
    private double left[], right[];
    /**/public void setLength(double length) { throw new IllegalStateException("Cannot adjust length of buffered sound-bit of name " + getName());
    }
  }

  /** Tests if the stream is a mono/stereo 16bit compatible stream format.
   * @throws IllegalArgumentException If the format is incorrect.
   */
  public static void checkFormat(AudioInputStream stream) {
    if(!(((stream.getFormat().getChannels() == 1) || (stream.getFormat().getChannels() == 2)) &&
         (stream.getFormat().getSampleSizeInBits() == 16) &&
         (stream.getFormat().getEncoding() == AudioFormat.Encoding.PCM_SIGNED) &&
         (stream.getFormat().isBigEndian() == false))) throw new IllegalArgumentException("Bad stream format: " + stream.getFormat().toString());
  }
  /** Detects sound events.
   * @param frequence Sound event frequence in Hz.
   * @param period    Sound event detection period in second.
   * @param cut       Sound event related threshold (typically 0 to 2).
   * @return An array of values sampled at the given period with 0 if no event, else with the event amplitude.
   */
  public double[] events(double frequence, double period, double cut) {
    // Calculates the correlations at the given frequency
    int size = 2 + (int) (getLength() / period);
    double events[] = new double[size], c = 0, s = 0, a = 0, g = 1.0 - 1.0 / (SAMPLING * period), max = 0, moy = 0, var = 0;
    int n = 0;
    for(double t = 0, l = period, dt = 1.0 / SAMPLING, T = getLength(); t < T; t += dt) {
      double v = get('l', t) + get('r', t);
      c += g * (Math.cos(2 * Math.PI * frequence * t) * v - c);
      s += g * (Math.sin(2 * Math.PI * frequence * t) * v - s);
      a += g * (c * c + s * s - a);
      if((l <= t) && (n < size)) {
        events[n++] = a;
        l += period;
        if(a > max)
          max = a;
        moy += a;
        var += a * a;
      }
    }
    if(n > 0) {
      moy /= n;
      var = Math.sqrt(var / n - moy * moy);
    }
    // Thresholds events
    double thres = moy + cut * var, nthres = 0;
    for(int k = 0; k < size; k++)
      if(events[k] < thres)
        events[k] = 0;
      else
        nthres++;
    // Verboses results
    System.err.println("events(" + frequence + ", " + period + ", " + cut + ") = " + moy + " +- " + var + " < " + max + ", " + nthres + "/" + size + " = " + (100.0 * nthres / size) + "% < " + thres);
    return events;
  }
}
