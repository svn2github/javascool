/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

//
// To be done:
// Gets a stream from a sound file are recover the get(channel, index)
// Defines note's periodic and amplitude shapes

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

// Used to play/save an audio file
import java.net.URL; 
import java.io.File;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFileFormat;

// Used to build a notes'frequencies
import java.util.Vector;

// Used to dump midi sounds
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.SoundbankResource;

// Used to show a curve
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

/** This widget defines a sound bit and allows to use sampled sound resources.
 * @see <a href="SoundBit.java">source code</a>
 */
public class SoundBit {
  private static final long serialVersionUID = 1L;

  /** Plays and shows a sound.
   * @param usage <tt>java SoundBit ([sound-bit-class-name] [length-in-second] | sound-file-location | 'notes:...')</tt>
   * <div>Plays the sound and shows the left channel spectrum and trace's start.</div>
   */
  public static void main(String usage[]) { 
    // Default sound
    String dsound = "notes:"; // "notes:" // "snd/clarinet.wav"
    // Sound name
    String sound = usage.length == 1 && !usage[0].matches("[0-9\\.]+") ? usage[0] : dsound;
    // Sound length or tempo
    double T = Double.valueOf(usage.length == 2  && usage[1].matches("[0-9\\.]+") ? usage[1] : usage.length == 1 && usage[0].matches("[0-9\\.]+") ? usage[0] : "10");
    try { 
      SoundBit s = null;
      // Consider NotesSoundBit
      if (sound.startsWith("notes:")) {
	// The Elise'letter piano right-hand 1st notes played as on a ``piccolo´´ (for debugging purpose only !).
	if (sound.equals("notes:")) sound = "notes:e5 e5b e5 e5b e5 e5b e5 b d5 c5 4 a | 1 h c e a 4 b | 1 h e g g# 4 a";
	s = newNotesSound(sound.substring(6));
      } else {
	// Consider Java SoundBit
	try { s = (SoundBit) Class.forName(sound).newInstance(); } catch(Exception e) { } if(s != null) s.setLength(T); 
	// Consider FileSoundBit
	if (s == null) { try { s = newFileSound(sound); } catch(Exception e) { } }
      }
      // Plays and shows
      if (s != null) {
	System.out.print("showing."); s.show('l');
	System.out.print("playing .."); System.out.flush(); s.play(); System.out.println(" done."); 
      } else
	System.out.println("Unable to define the sound: "+sound);
    } catch(Exception e) { Proglets.report(e); }
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //                                                                                                                                                //
  //  Wraps a functional defined (via the get() sound function method) soundbit to an AudioInputStream and the AudioSystem basic functionnalities   //
  //                                                                                                                                                //
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the sound function.
   * <div>One <tt>get()</tt> methods i to be overloaded to define your own sound.</div>
   * <div>By contract this method is called for <tt>index = 0, 1, 2, ..</tt> in consecutive increasing order.</tt>
   * @param channel Left 'l' or right 'r' channel.
   * @param index  Sound time index. The time in second writes: <tt>time = index / SAMPLING</tt>.
   * @return The sound value between -1.0 and 1.0 (maximal amplitude, linear scale).
   */
  public double get(char channel, long index) { return get(channel, index/SAMPLING); }

  /** Defines the sound function.
   * <div>One <tt>get()</tt> methods i to be overloaded to define your own sound.</div>
   * <div>By contract this method is called for <tt>index = 0, 1, 2, ..</tt> in consecutive increasing order.</tt>
   * @param channel Left 'l' or right 'r' channel.
   * @param time  Sound time in second.
   * @return The sound value between -1.0 and 1.0 (maximal amplitude, linear scale).
   */
  public double get(char channel, double time) { return 0; } 

  /** Resets the sound properties.
   * <a name ="reset"></a><div>This methods is to be overloaded to manage your own sound property.</div>
   * @param definition The sound properties given as a string: the semantic depends on the sound, it is documented for each sound's type.
   * @return true if the operation succeed, false if it fails.
   */
  public boolean reset(String definition) { return true; }
  
  /** Gets the sound name.
   * @return Either the sound java class or the sound file name.
   */
  public String getName() { return name == null ? getClass().getName() : name; } protected String name = null;

  /** Sets the stream length.
   * @param length Stream length in second. If -1, the length remains undefined (in fact maximal).
   * @throws IllegalStateException If it is a buffered sound-bit of fixed length, thus not adjustable.
   */
  public void setLength(double length) { this.length = length; }

  /** Gets the stream length.
   * @return The stream length in second.
   */
  public double getLength() { return length >= 0 ? length :  Long.MAX_VALUE/FRAME_SIZE/SAMPLING; } protected double length = 0;

  /** Returns the 16 bit, stereo, sound bit, PCM standard, signed PCM, 44.1 KHz sampled, audio stream, supporting mark and reset. 
   * <div>- Notice: this stream is never closed, but can be reset when to be reused.</div>
   * @return A new <a href="http://java.sun.com/javase/6/docs/api/javax/sound/sampled/AudioInputStream.html">javax.sound.sampled.AudioInputStream</a> corresponding to this sound.
   */
  public Stream getStream() { return new Stream(length); }

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
			    SAMPLING,  // Sample rate in Hz
			    16,        // Sampling bits
			    2,         // Number of channels
			    FRAME_SIZE,// Frame size in bytes
			    SAMPLING,  // Frame rate in Hz
			    false),    // Little-endian byte order
	    length >= 0 ? (long) (length * SAMPLING) : Long.MAX_VALUE/FRAME_SIZE);
    }

    // Implementation of the stream read methods

    /**  Reads up to byteLength from the stream into the data array of bytes, starting from offset.
     * @return The total number of bytes read into the buffer, or -1 is there is no more to read because the end of the stream has been reached. 
     */
    public int read(byte[] data, int offset, int byteLength) {
      // Adjust the read size
      byteLength = offset + byteLength > data.length ? data.length - offset : byteLength;
      long nFrame = byteLength / frameSize; if (nFrame > frameLength - framePos) nFrame = frameLength - framePos; byteLength = (int) nFrame * frameSize;
      // Reads sound samples
      for(int n = 0, i = offset; n < nFrame; n++, i += frameSize) {
	// Gets and converts sample's values: max amplitude is: 2^16-1=65535
	long l = Math.round(32767.0 * (1.0 + get('l', framePos))), r = Math.round(32767.0 * (1.0 + get('r', framePos))); framePos++;
	if (l < 0) l = 0; if(l > 65535) l = 65535; if (r < 0) r = 0; if(r > 65535) r = 65535;
	// Reports in buffer: this is valid for 16 bit stereo, little endian
	data[i + 0] = (byte) ((l & 0xFF) - 128);
	data[i + 1] = (byte) (((l >>> 8) & 0xFF) - 128);
	data[i + 2] = (byte) ((r & 0xFF) - 128);
	data[i + 3] = (byte) (((r >>> 8) & 0xFF) - 128);
      }
      return byteLength <= 0 ? -1 : byteLength;
    }
    /**  Reads bytes from the audio input stream and stores them into the buffer array. */
    public int read(byte[] data) { return read(data, 0, data.length); }
    /**  Returns the number of bytes that can be read (or skipped over) from this stream. */
    public int available() { return (int) (frameSize * (frameLength - framePos)); }
    /**  Repositions this stream to the initial position or the position at the time the mark method was last called on this stream. */
    public void reset() { if(framePos > frameLimit) throw new IllegalStateException("Mark limit exceeded"); framePos = frameMark; }   
    /** Marks the current position in this input stream, byteLimit, if positive, being the maximum limit of bytes that can be read before the mark becomes invalid. */
    public void mark(int byteLimit) { frameMark = framePos; frameLimit = byteLimit > 0 ? framePos + byteLimit / frameSize : frameLength; }
    /** Returns true because the mark and reset methods are supported. */
    public boolean markSupported() { return true; }
    /** Skips over and discards byteSize bytes of data (or less, if less data available) from this stream. */
    public void skip(int byteSize) { framePos += byteSize / frameSize; if (framePos > frameLength) framePos = frameLength; }
    /** Forbidden method: do not use (since frames have not one byte size). */
    public int read() { throw new IllegalStateException("SoundBit has no one byte frame size, operation forbidden"); }
    /** Returns a string representation of this stream, for debugging purpose. */
    public String toString() { return "["+super.toString()+" length = "+(frameLength/SAMPLING)+"s pos = "+framePos+" < "+frameLength+" mark = "+frameMark+" < "+frameLimit+"]"; }
    /** Closes this audio input stream. */
    public void close() { }
    private long frameMark = 0, frameLimit = Long.MAX_VALUE/FRAME_SIZE;
  }

  /** Saves the sound in a waveformn audio vector file.
   * @param path The file path, the <tt>.wav</tt> extension is automatically added.
   * @throws IOException If it is not possible to write the data at the given path.
   */
  public void save(String path) throws IOException {
    AudioSystem.write(getStream(), AudioFileFormat.Type.WAVE, new File(path.replaceFirst("\\.wav$", "")+".wav")); 
  }

  /** Plays the sound on the standard audio system line. 
   * @throws RuntimeException If the audio system is unavailable or does not support the current: 16 bit, stereo, standard PCM, 44.1 KHz, signed, little-endian audio format.
   */
  public void play() {
    AudioInputStream stream = getStream();
    // Plays a stream on the standard audio system line. 
    try {
      // Opens the audio line
      SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, stream.getFormat()));
      line.open(stream.getFormat());
      line.start();
      // In/out the sound via a buffer
      {
	final int size = (int) (FRAME_SIZE * SAMPLING); byte data[] = new byte[size];
	for(long t = 0; t < FRAME_SIZE * stream.getFrameLength(); t += size) {
	  int s = stream.read(data, 0, size); if (s > 0) line.write(data, 0, s);
	}
      }
      line.close();
    } catch(IOException e) { throw new RuntimeException(e.toString()); } catch(LineUnavailableException e) { throw new RuntimeException(e.toString()); }
  }

  /** Creates a sound-bit from an audio file.
   * <div>The sound-bit audio-file can be changed using the <tt><a href="#reset">reset</a>(location)</tt> method.</div>
   * @param location Audio file path: either a file-name or a URL-name. 
   * @return The sound-bit stored in the audio-file.
   */
  public static SoundBit newFileSound(String location) { SoundBit s = new FileSoundBit(); s.reset(location); return s; }

  /** Defines the super-class of buffered sound-bit wrappers. */
  private static class BufferedSoundBit extends SoundBit {
    public void setLength(double length) { throw new IllegalStateException("Cannot adjust length of buffered sound-bit of name "+getName()); }
  }

  /** Defines the data-file sound-bit wrapper. */
  private static class FileSoundBit extends BufferedSoundBit {
    /** Constructs a sound defined from two buffer files.
     * @param location Audio file path: either a file-name or a URL-name.
     */
    public boolean reset(String location) {    
      try {
	AudioInputStream stream = AudioSystem.getAudioInputStream(new URL(location.matches("^(file|ftp|http|https):.*$") ? location : "file:"+location));
	checkFormat(stream);
	c = stream.getFormat().getChannels(); s = stream.getFormat().getFrameSize();
	buffer = new byte[(int) stream.getFrameLength() * stream.getFormat().getFrameSize()]; stream.read(buffer); stream.close();
	name = location;
	length = stream.getFrameLength() / SAMPLING;
      } catch(UnsupportedAudioFileException e) { throw new RuntimeException(e); } catch(IOException e) { throw new RuntimeException(e); }
      return true;
    } 
    public double get(char channel, long index) { 
      int i = (int) index * s + (c == 1 || channel == 'l' ? 0 : 2);
      if(buffer == null || i < 0 || i >= buffer.length) return 0;
      int h = buffer[i+1], l = buffer[i], v = ((128 + h) << 8) | (128 + l); return 1 * (v / 32767.0 - 1);
    }
    private int c, s; private byte[] buffer;
  }

  /** Defines the data-buffer sound-bit wrapper. */
  private static class DataSoundBit extends BufferedSoundBit {
    /** Constructs a sound defined from two buffer files.
     * @param name Sound name.
     * @param left Left buffer data.
     * @param right Right buffer data if any. Set to null for a monophonic sound.
     */
    public DataSoundBit(String name, double[] left, double[] right) {
      if (left == null) throw new IllegalArgumentException("Undefined left channel data");
      if (right != null && left.length != right.length) new IllegalArgumentException("Left and right channel length differs: "+left.length+" != "+right.length);
      this.left = left; this.right = right == null ? left : right; 
      this.name = name;
      length = left.length / SAMPLING;
    }
    public double get(char channel, long index) { return (index < 0 || index >= left.length) ? 0 : channel == 'r' ? right[(int) index] : left[(int) index]; }
    private double left[], right[];
  }

  /** Converts a mono/stereo 16bit stream to a data buffer.
   * @param stream The audio stream to convert.
   * @param channel Left 'l' or right 'r' channel.
   */
  private static double[] getData(Stream stream, char channel) {
    checkFormat(stream);
    if (stream.getFrameLength() > (long) Integer.MAX_VALUE) throw new IllegalArgumentException("Cannot convert huge audio stream to buffer");
    int length = (int) stream.getFrameLength(); double data[] = new double[length]; 
    int n = stream.getFormat().getChannels() * 2, o = stream.getFormat().getChannels() == 1 || channel == 'l' ? 0 : 2; byte read[] = new byte[n];
    for(int i = 0; i < length; i++) {	
      stream.read(read); int h = read[o+1], l = read[o], v = ((128 + h) << 8) | (128 + l); data[i] = 1 * (v / 32767.0 - 1);
    }
    return data;
  }

  // Returns true if this is a mono/stereo 16bit compatible stream format
  private static void checkFormat(AudioInputStream stream) {
    if (!((stream.getFormat().getChannels() == 1 || stream.getFormat().getChannels() == 2) && 
	  stream.getFormat().getSampleSizeInBits() == 16 && 
	  stream.getFormat().getEncoding()  == AudioFormat.Encoding.PCM_SIGNED &&
	  stream.getFormat().isBigEndian() == false))
      throw new IllegalArgumentException("Bad stream format: "+stream.getFormat().toString());
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // Used ``once´´ to dump the default MIDI synthetizer sounds as "snd/*.wav" files
  private static void midi_dump() throws IOException {
    try {
      Soundbank s = MidiSystem.getSynthesizer().getDefaultSoundbank();
      System.out.println("sound-bank: " + s.getDescription());
      for(int i = 0; i < s.getResources().length; i++) {
	SoundbankResource r = s.getResources()[i];
	if (r.getDataClass() != null && r.getDataClass().getName().equals("javax.sound.sampled.AudioInputStream")) {
	  String name = r.getName().toLowerCase().replaceAll(" ", "_");
	  AudioInputStream stream = (AudioInputStream) r.getData();
	  int length = AudioSystem.write(stream, AudioFileFormat.Type.WAVE, new File("snd/"+name+".wav"));
	  System.out.println("\t "+name+" \t(lenght = "+stream.getFrameLength()+" <= "+length+", "+stream.getFormat()+")");
	}
      }
    } catch(MidiUnavailableException e) { throw new IOException(e.toString()); }
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //                                                                                                                                                //
  //                                       Defines frequency analysis based on Cooley-Tukey FFT algorithm                                           //
  //                                                                                                                                                //
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Shows the spectrum and trace's start of the sound.
   * <div>Opens a frame, drawing: <ul>
   *  <li>drawing the frequencies amplitudes (in red, in normalized dB (log coordinates)), frequencies being drawn between A0 (27.5Hz) and A9 (6400Hz) around A3 (440Hz);</li>
   *  <li>drawing the 1st samples of the signal (in yellow, the 1st <tt>11</tt>ms).</li>
   * </ul> while the sound name main frequency and spectral magnitude is printed.
   * @param channel Left 'l' or right 'r' channel.
   */
  public void show(char channel) { 
    Panel p = new Panel(); p.reset(this, channel);
    { JFrame f = new JFrame(); f.getContentPane().add(p); f.pack(); f.setTitle(getName()); f.setSize(540, 600); f.setVisible(true); }
  }

  // Defines a drawing of the FTT and of the signal
  static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;
    /** Defines the spectrum and trace's start of the sound.
     * @param channel Left 'l' or right 'r' channel.
     */
    public void reset(SoundBit sound, char channel) { 
      // Calculates the spectrum magnitude and phase
      data = getData(sound.getStream(), channel); complex fft[] = getFFT(data); mag = new double[hsize+1];
      for(int i = 0; i <= hsize; i++) { 
	double f = f0 * Math.pow(f1/ f0, i / (double) hsize); int k = (int) Math.rint(fft.length * f / SAMPLING); mag[i] = Math.sqrt(fft[k].x * fft[k].x + fft[k].y * fft[k].y);
      }
      // Smooths the spectrum magnitude and calculates the maximum
      double f_max = 0; mag_max = 0; smag = new double[hsize+1];
      for(int i = 0, w = 5; i <= hsize; i++) {
	double n = 0, v = 0; for(int j = Math.max(0, i - w); j <= Math.min(hsize, i + w); j++) { n++; v += mag[j]; } smag[i] = v / n;
	double f = f0 * Math.pow(f1/ f0, i / (double) hsize); if (mag_max < smag[i]) { f_max = f; mag_max = smag[i]; }
      }      
      label = sound.getName() + " (|fft| < "+((int)mag_max)+", f_max = "+((int)f_max)+"Hz) ";
      repaint();
    } 
    int hsize = 16 * 31, vsize = 160, b = 0, m = 20, width = 2 * m +  hsize, height= b + 4 * m + 3 * vsize, height2 = b + 2 * m + 2 * vsize; 
    double f0 = 440.0 / 16, f1 = 440.0 * 16;
    /** Internal routine: do not use. */
    public void paint(Graphics g) {
      super.paint(g); 
      // Backgrounds and axis
      g.setColor(Color.GRAY); g.fillRect(m, b + m, hsize, 2 * vsize); g.fillRect(m, height2 + m, hsize, vsize); 
      g.setColor(Color.BLACK); g.drawLine(m, height2, width - m, height2); 
      g.drawLine(m/2, height2 + m + vsize/2, m, height2 + m + vsize/2); g.drawLine(width - m/2, height2 + m + vsize/2, width - m, height2 + m + vsize/2);
      g.setFont(new Font("Times", 0, 10)); for(int i = m; i <= width-m; i+= hsize/16) {
	g.drawLine(i, height2-(i == width/2 ? m : m/2), i, height2+(i == width/2 ? m-2 : m/2)); 
	double f = f0 * Math.pow(f1/ f0, (i - m) / (double) hsize);  if(i < width-m) g.drawString(Integer.toString((int) f), i+1, height2-1);
      }
      // Amplitude and phase curves
      for(int i = m, a1 = 0, d1 = 0; i <= width - m; i++) { 
	double a = Math.log(1 + 9 * mag[i - m] / mag_max) / Math.log(10);
	int a0 = height2 - m - (int) Math.rint(2 * vsize * a); if (a0 < b + m) a0 = b + m;
	g.setColor(Color.RED); if (i > m) g.drawLine(i - 1, a1, i, a0); a1 = a0;
	// Drawing the 1st data samples
	int d0 = height - m - (int) Math.rint(vsize * (0.5 * (1 + (i - m < data.length ? data[i - m] : 0))));
	g.setColor(Color.YELLOW); if (i > m) g.drawLine(i - 1, d1, i, d0); d1 = d0;
      }
      g.setColor(Color.BLACK); g.setFont(new Font("Times", Font.BOLD, 16)); 
      g.drawString("Amplitudes du spectre", m + m/2, b + 2 * m); g.drawString("Début du signal", m + m/2, height2 + 2 * m);
    }
    private double data[], mag[], mag_max, smag[]; private String label;
  }

  // Computes the FFT of a stream after http://www.cs.princeton.edu/introcs/97data/FFT.java.html
  private static complex[] getFFT(double data[]) {
    // Calculates the largest power of two not greater than data length
    int length = (int) Math.pow(2, Math.ceil(Math.log(data.length)/Math.log(2))); if (length == 0) length = 1;
    // Builds the complex buffer and computes fft, appkying a Hann's windowing
    complex cdata[] = new complex[length]; for (int i = 0; i < length; i++) 
      cdata[i] = new complex(i < data.length ? 0.5 * (1 - Math.cos(2.0 * Math.PI * i / data.length)) * data[i] : 0, 0); return fft(cdata);
  }
  // Defines a complex number and its multiplication
  private static class complex { complex(double x, double y) { this.x = x; this.y = y; } double x, y; 
    complex mul(complex w) { return new complex(x * w.x - y * w.y, x * w.y + y * w.x); } 
  }
  // Implements the Cooley-Tukey FFT algorithm assuming x.length is a power of two
  private static complex[] fft(complex[] x) {
    int n = x.length; if(n == 1) return new complex[] { x[0] };
    // Applies FFT on event and odd parts of the signal
    complex e[] = new complex[n/2], o[] =  new complex[n/2]; for(int i = 0; i < n/2; i++) { e[i] = x[2*i]; o[i] = x[2*i+1]; } e = fft(e); o = fft(o);
    // Combines the resukt
    complex s[] = new complex[n];
    for(int i = 0; i < n/2; i++) {
      double k = -2 * i * Math.PI / n; complex w = new complex(Math.cos(k), Math.sin(k)); w = w.mul(o[i]);
      s[i] = new complex(e[i].x + w.x, e[i].y + w.y); s[i+n/2] = new complex(e[i].x - w.x, e[i].y - w.y);  
    }
    return s;
  }
  // Returns the inverse FFT of spectrum which length is a power of two
  private double[] getInvFFT(complex[] spectrum) {
    for(int i = 0; i < spectrum.length; i++) spectrum[i].y = -spectrum[i].y;
    complex[] sp = fft(spectrum); double s[] = new double[spectrum.length];
    for(int i = 0; i < spectrum.length; i++) { s[i] = sp[i].x = sp[i].x/spectrum.length; sp[i].y = -sp[i].y/spectrum.length; spectrum[i].y = -spectrum[i].y; }
    return s;    
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //                                                                                                                                                //
  //                                 Defines a monophonic sound based on textual notes string definition                                            //
  //                                                                                                                                                //
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Creates a monophonic ``piccolo´´ sound-bit from a note sequence (still in development).
   * <div>The sound-bit can be changed using the <tt><a href="#reset">reset</a>(notes)</tt> method.</div>
   * @param notes <a name="notes"></a> Definition of note's sequence and note:<ul>
   * <li>The notes sequences is written as:<ul>
   *   <li> a sequence of notes, separated with spaces;</li>
   *   <li> while note's duration is noted by an integer, from <tt>1</tt> to <tt>999</tt>, separated with spaces;
   *     <br>here, <tt>1</tt> stands for the minimal note duration (e.g. quaver), 
   *     <br>the note duration being valid until a new duration is set;</li>
   *   <li> while note's intensity is written using the construct <tt>I<i>value</i></tt> where value is from <tt>0</tt> to <tt>0.999</tt>, 
   *     e.g. <i>I0.5</tt> or <tt>I5</tt> (the <tt>0.</tt> number prefix can be omitted) refers to a half-scale intensity.</li>
   *   <li>other pattern being ignored.</li>
   * </ul> e.g. <tt>e5 e5b e5 e5b e5 e5b e5 b d5 c5 4 a | 1 h c e a 4 b | 1 h e g g# 4 a</tt> is more or less the Elise'letter piano right-hand 1st notes.</li>
   * <li>Each note is written as <tt>A4</tt> for the middle-board <tt>A</tt> (<i>La</i>) at 440Hz. <ul>
   *   <li>The letter: <table>
   *     <tr><td><tt>A</tt></td><td><tt>B</tt></td><td><tt>C</tt></td><td><tt>D</tt></td><td><tt>E</tt></td><td><tt>F</tt></td><td><tt>G</tt></td><td><tt>H</tt></td></tr>
   *     <tr><td><i>La</i></td><td><i>Si</i></td><td><i>Do</i></td><td><i>Ré</i></td><td><i>Mi</i></td><td><i>Fa</i></td><td><i>Sol</i></td><td><i>silence</i></td></tr>
   *   </table> stands for the note name. The <tt>h</tt> stands for a ``halt´´, a silence.</li>
   *   <li>The digit, from <tt>0</tt> to <tt>8</tt> included, stands for the octave, default is <tt>4</tt>.</li>
   *   <li>The <tt>#</tt> or <tt>b</tt> suffix stands for the <i>sharp</i> or <i>flat</i> modulation respectively.</li>
   *  </ul> e.g., <tt>G1#</tt> is the 1st piano fingerboard G sharp. The notation is case insensitive.</li>
   * </ul>
   * <li>The <i>tempo</i>, i.e. the minimal note duration in second, is declared a the beginning of the note sequence using the <tt>T<i>value</i></tt> construct.
   *   Default is <tt>0.25</tt>s, while this declarations is to be done once.</li>
   * @return The sound-bit defined by the note sequence.
   */
  public static SoundBit newNotesSound(String notes) { SoundBit s = new NotesSoundBit(); s.reset(notes); return s; }

  // Defines a notes sound-bit wrapper.
  static class NotesSoundBit extends BufferedSoundBit {
    /** Constructs a sound defined from a note sequence.
     * @param notes The note sequence.
     * @param tempo The sequence tempo.
     */
    public boolean reset(String notes) { 
      freqs = getNotes(notes); tempo = getTempo(notes); name = "notes:.."; sound.setLength(length = freqs.length * tempo); return true;
    } 
    // Default sound is a piccolo
    public double get(char channel, double time) { return Math.sin(2 * Math.PI * time); }
    // Internal sound used to sample the notes
    private SoundBit sound = new SoundBit() {
	public double get(char channel, double time) { 
	  int i = (int)(time / tempo); if (i < freqs.length) { 
	    double d = freqs[i].f / SAMPLING; return freqs[i].a * NotesSoundBit.this.get(channel, channel == 'l' ? (pl += d) : (pr += d)); 
	  } else 
	    return 0;
	}
	private double pl = 0, pr = 0;
      };
     private note freqs[] = new note[0]; private double tempo = 0.25;
    public Stream getStream() { return sound.getStream(); }
  }

  // Gets the low-level tempo of a given note sequence. 
  private static double getTempo(String notes) {
    return notes.matches(".*[ \t\n]+T[0-9]*\\.?[0-9]+[ \t\n]+.*") ? Double.valueOf(notes.replaceFirst(".*[ \t\n]+T([0-9]*\\.?[0-9]+)[ \t\n]+.*", "\\1")) : 0.25;
  }

  // Gets the low-level array of a given note sequence.
  private static note[] getNotes(String notes) {
    Vector<note> freqs = new Vector<note>();
    String n[] = notes.trim().toLowerCase().split("[ \t\n]"); 
    int d = 1; double a = 0.999;
    for(int i = 0; i < n.length; i++) {
      if(n[i].matches("[1-9][0-9]*")) {
	d = Integer.valueOf(n[i]);
      } else if(n[i].matches("i0?\\.?[0-9]+")) {
	a = Double.valueOf(n[i].matches("[0-9]+") ? "0\\."+n[i] : n[i]);
      } else if(n[i].matches("[a-h][0-8]?[#b]?")) {
	double f = getNote(n[i]); for(int k = 0; k < d; k++) freqs.add(new note(f, a));
      }
    }
    return freqs.toArray(new note[freqs.size()]);
  }
  // Low-level note defined by a frequency and a intensity
  private static class note { note(double f, double a) { this.f = f; this.a = a; } double f, a; }

  // Gets the frequency of a given note.
  private static double getNote(String note) {
    // Frequency constants
    final double 
      SharpPitch = Math.pow(2.0, 1.0 / 12.0), FlatPitch = Math.pow(SharpPitch, -1), 
      Tones[] = new double[] { 1, 
        Math.pow(SharpPitch, 2), Math.pow(SharpPitch, -9), Math.pow(SharpPitch, -7), Math.pow(SharpPitch, -5), Math.pow(SharpPitch, -4), Math.pow(SharpPitch, -2), 0},
      Octaves[] = 
	new double[] { Math.pow(2, -4), Math.pow(2, -3), Math.pow(2, -2), Math.pow(2, -1), Math.pow(2, 0), Math.pow(2, 1), Math.pow(2, 2), Math.pow(2, 3), Math.pow(2, 4) };
    // Note syntax 
    note = note.toLowerCase(); if(note.matches("[a-h][#b]?")) return getNote(note.charAt(0)+"4"+(note.length() == 2 ? note.charAt(1) : ""));
    if (!note.matches("[a-h][0-8][#b]?")) throw new IllegalArgumentException("Bad note format «"+note+"»");
    // Note frequency derivation
    double f = 440.0 * Tones[(int) note.charAt(0) - (int) 'a'] * Octaves[(int) note.charAt(1) - (int) '0'];
    if(note.length() == 3) { switch(note.charAt(2)) { case '#': f *=  SharpPitch; break;  case 'b': f *=  FlatPitch; break; } }
    return f;
  }
}
