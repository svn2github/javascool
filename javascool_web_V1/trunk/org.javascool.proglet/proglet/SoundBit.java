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
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

/** This widget defines a general sound bit.
 * @see <a href="SoundBit.java">source code</a>
 */
public class SoundBit {
  private static final long serialVersionUID = 1L;

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //                                                                                                                                                //
  //  Wraps a functional defined (via the get() sound function method) soundbit to an AudioInputStream and the AudioSystem basic functionnalities   //
  //                                                                                                                                                //
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Defines the sound function.
   * <div>This methods is to be overloaded to define your own sound.</div>
   * <div>The default is a mad tone.</div>
   * @param channel Left 'l' or right 'r' channel.
   * @param index  Sound time index. The time in second writes: <tt>time = index / SAMPLING</tt>.
   * @return The sound value between -1.0 and 1.0 (maximal amplitude, linear scale).
   */
  public double get(char channel, long index) {
    double t = index/SAMPLING, a = 0.5 * (1 + sin(t / 4)), f = 440 + 10 * sin(t / 0.5), n = 0.25 * (1 + sin(t / 2.4));
    return (channel == 'l' ? 1 - a : a) * (0.5 * sin(f * t) + n * Math.random());
  }
  private static double sin(double t) { return Math.sin(2 * Math.PI * t); }
  
  /** Gets the sound name.
   * @return Either the sound java class or the sound file name.
   */
  public String getName() { return name == null ? getClass().getName() : name; } protected String name;

  /** Sets the stream length.
   * @param length Stream length in second. If 0, the length remains undefined (in fact maximal).
   * @throws IllegalStateException If it is a buffered sound-bit of fixed length, thus not adjustable.
   */
  public void setLength(double length) { this.length = length; }

  /** Gets the stream length.
   * @return The stream length in second.
   */
  public double getLength() { return length > 0 ? length :  Long.MAX_VALUE/FRAME_SIZE/SAMPLING; } private double length = 0;

  /** Returns the 16 bit, stereo, sound bit, PCM standard, signed PCM, 44.1 KHz sampled, audio stream, supporting mark and reset. 
   * - Notice: this stream is never closed, but can be reset when to be reused.
   */
  public AudioInputStream getStream() { return new Stream(length); }

  /** Sampling frequency. */
  static public final float SAMPLING = 44100.0F;
  static private final int FRAME_SIZE = 4;

  /** Defines the audio stream wrapper. */
  private class Stream extends AudioInputStream {
    private static final long serialVersionUID = 1L;
    
    /** Constructs a 16 bit, stereo, sound bit, with a standard PCM 44.1 KHz signed audio format. 
     * @param length Stream length in second. If 0, the length remains undefined (in fact maximal).
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
	    length > 0 ? (long) (length * SAMPLING) : Long.MAX_VALUE/FRAME_SIZE);
    }

    // Implementation of the stream read methods

    /**  Reads up to byteLength from the stream into the data array of bytes, starting from offset.
     * @return The total number of bytes read into the buffer, or -1 is there is no more to read because the end of the stream has been reached. 
     */
    public int read(byte[] data, int offset, int byteLength) throws IOException {
      final boolean DEBUG = false; if (DEBUG) System.out.println("read([], "+offset+", "+byteLength+") : "+toString());
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
    private long frameMark = 0, frameLimit = Long.MAX_VALUE/FRAME_SIZE;
  }

  /** Saves the sound in a waveformn audio vector file.
   * @param path The file path, the <tt>.wav</tt> extension is automatically added.
   */
  public void save(String path) throws IOException {
    AudioSystem.write(getStream(), AudioFileFormat.Type.WAVE, new File(path.replaceFirst("\\.wav$", "")+".wav")); 
  }

  /** Plays the sound on the standard audio system line. */
  public void play() throws IOException { 
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
    } catch(LineUnavailableException e) { throw new IOException(e.toString()); }
  }

  /** Creates a sound-bit from an audio file.
   * @param location Audio file path.
   * @return A sound-bit defined by the audio-file.
   */
  public static SoundBit getSound(String location) throws IOException { return new FileSoundBit(location); }

  /** Defines the data-file sound-bit wrapper. */
  private static class FileSoundBit extends SoundBit {
    /** Constructs a sound defined from two buffer files.
     * @param location Audio file path.
     */
    public FileSoundBit(String location) throws IOException {    
      this.name = location;
      try {
	AudioInputStream stream = AudioSystem.getAudioInputStream(new URL(location.matches("^(file|ftp|http|https):.*$") ? location : "file:"+location));
	if((stream.getFormat().getChannels() == 1 || stream.getFormat().getChannels() == 2) && 
	   stream.getFormat().getSampleSizeInBits() == 16 && 
	   stream.getFormat().getEncoding()  == AudioFormat.Encoding.PCM_SIGNED &&
	   stream.getFormat().isBigEndian() == false) {
	  c = stream.getFormat().getChannels(); s = stream.getFormat().getFrameSize();
	  buffer = new byte[(int) stream.getFrameLength() * stream.getFormat().getFrameSize()]; stream.read(buffer); stream.close();
	  super.setLength(stream.getFrameLength() / SAMPLING);
	} else
	  throw new IllegalArgumentException("Bad stream format: "+stream.getFormat().toString()+" in "+location);
      } catch(UnsupportedAudioFileException e) { throw new IOException(e.toString()); }
    } 
    private int c, s; private byte[] buffer;
    public double get(char channel, long index) { 
      int i = (int) index * s + (c == 1 || channel == 'l' ? 0 : 2), h = buffer[i+1], l = buffer[i], v = ((128 + h) << 8) | (128 + l); return 1 * (v / 32767.0 - 1);
    }
    public void setLength(double length) { throw new IllegalStateException("Cannot adjust length of buffered sound-bit"); }
  }

  /** Defines the data-buffer sound-bit wrapper. */
  private static class DataSoundBit extends SoundBit {
    /** Constructs a sound defined from two buffer files.
     * @param name Sound name.
     * @param left Left buffer data.
     * @param right Right buffer data if any. Set to null for a monophonic sound.
     */
    public DataSoundBit(String name, double[] left, double[] right) {
      this.name = name;
      if (left == null) throw new IllegalArgumentException("Undefined left channel data");
      if (right != null && left.length != right.length) new IllegalArgumentException("Left and right channel length differs: "+left.length+" != "+right.length);
      this.left = left; this.right = right == null ? left : right; super.setLength(left.length / SAMPLING);
    } private double left[], right[];
    public double get(char channel, long index) { return (index < 0 || index >= left.length) ? 0 : channel == 'r' ? right[(int) index] : left[(int) index]; }
    public void setLength(double length) { throw new IllegalStateException("Cannot adjust length of buffered sound-bit"); }
  }

  /** Converts a mono/stereo 16bit stream to a data buffer.
   * @param stream The audio stream to convert.
   * @param channel Left 'l' or right 'r' channel.
   */
  private static double[] getData(AudioInputStream stream, char channel) throws IOException {
    if((stream.getFormat().getChannels() == 1 || stream.getFormat().getChannels() == 2) && 
       stream.getFormat().getSampleSizeInBits() == 16 && 
       stream.getFormat().getEncoding()  == AudioFormat.Encoding.PCM_SIGNED &&
       stream.getFormat().isBigEndian() == false) {
      if (stream.getFrameLength() > (long) Integer.MAX_VALUE) throw new IllegalArgumentException("Cannot convert huge audio stream to buffer");
      int length = (int) stream.getFrameLength(); double data[] = new double[length]; 
      int n = stream.getFormat().getChannels() * 2, o = stream.getFormat().getChannels() == 1 || channel == 'l' ? 0 : 2; byte read[] = new byte[n];
      for(int i = 0; i < length; i++) {	
	stream.read(read); int h = read[o+1], l = read[o], v = ((128 + h) << 8) | (128 + l); data[i] = 1 * (v / 32767.0 - 1);
      }
      stream.close();
      return data;
    } else
      throw new IllegalArgumentException("Bad stream format: "+stream.getFormat().toString());
  }
  static int nnn = 0;

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
  //             Defines miscellaneous frequency analysis and synthesis methods in order to manipulate sounds and sound-bits                        //
  //                                                                                                                                                //
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


  /** Shows the spectrum and trace's start of the sound.
   * <div>Opens a frame with the frequencies amplitudes (in red, in normalized dB (log coordinates)) and phases (in green, between -Pi and Pi),
   * frequencies being drawn between A0 (27.5Hz) and A9 (6400Hz) around A3 (440Hz).</div>
   * <div>The 1st samples are drawn in yellow.</div>
   * @param channel Left 'l' or right 'r' channel.
   */
  public void show(char channel) throws IOException { new Show(channel); }

  // Defines a drawing of the FTT and of the signal
  private class Show extends JFrame {
    private static final long serialVersionUID = 1L;
    /** Defines and display the spectrum and trace's start of the sound.
     * @param channel Left 'l' or right 'r' channel.
     */
    public Show(char channel) throws IOException { 
      data = getData(getStream(), channel); fft = getFFT(data); 
      freqs = new double[fft.length]; amax = 0; fmax = 0; for(int i = 0; i < fft.length; i++) {
	freqs[i] = Math.sqrt(fft[i].x * fft[i].x + fft[i].y * fft[i].y); 
	double f = i * SAMPLING / fft.length; if (f0 <= f && f <= f1 && amax < freqs[i]) { fmax = f; amax = freqs[i]; }
      }
      label = name + " (|fft| < "+((int)amax)+", f_max = "+((int)fmax)+"Hz) ";
      { pack(); setTitle("FFT"); setSize(width, height); setVisible(true); }
    } 
    int hsize = 16 * 50, vsize = 200, b = 40, m = 20, width = 2 * m +  hsize, height= b + 4 * m + 3 * vsize, height2 = b + 2 * m + 2 * vsize; 
    double f0 = 440.0 / 16, f1 = 440.0 * 16;
    /** Internal routine: do not use. */
    public void paint(Graphics g) {
      super.paint(g); 
      // Backgrounds and axis
      g.setColor(Color.GRAY); g.fillRect(m, b + m, hsize, 2 * vsize); g.fillRect(m, height2 + m, hsize, vsize); 
      g.setColor(Color.BLACK); g.drawLine(m, height2, width - m, height2); 
      g.drawLine(m/2, height2 + m + vsize/2, m, height2 + m + vsize/2); g.drawLine(width - m/2, height2 + m + vsize/2, width - m, height2 + m + vsize/2);
      for(int i = m; i <= width-m; i+= hsize/16) g.drawLine(i, height2-(i == width/2 ? m : m/2), i, height2+(i == width/2 ? m : m/2)); 
      // Amplitude and phase curves
      for(int i = m, a1 = 0, p1 = 0, d1 = 0; i <= width - m; i++) { 
	int k = (int) Math.rint(fft.length * f0 * Math.pow(f1/ f0, (i - m) / (double) hsize) / SAMPLING);
	double a = Math.log(1 + 9 * freqs[k] / amax)/Math.log(10), p = freqs[k] < 0.1 * amax ? 0 : Math.atan2(fft[k].y, fft[k].x);
	int a0 = height2 - m - (int) Math.rint(2 * vsize * a);
	int p0 = height - m - (int) Math.rint(vsize * (0.5 + p / (2 * Math.PI)));
	g.setColor(Color.RED); if (i > m) g.drawLine(i - 1, a1, i, a0); a1 = a0;
	g.setColor(Color.GREEN); if (i > m) g.drawLine(i - 1, p1, i, p0); p1 = p0; 
	// Drawing the 1st data samples
	int d0 = height - m - (int) Math.rint(vsize * (0.5 * (1 + data[i - m])));
	g.setColor(Color.YELLOW); if (i > m) g.drawLine(i - 1, d1, i, d0); d1 = d0;
      }
      g.setColor(Color.BLACK); g.setFont(new Font("Times", Font.BOLD, 16)); g.drawString(label, 2 * m, b + m/2);
    }
    private double data[], freqs[], fmax, amax; private complex fft[]; private String label;
  }

  // Computes the FFT of a stream after http://www.cs.princeton.edu/introcs/97data/FFT.java.html
  private static complex[] getFFT(double data[]) {
    // Calculates the largest power of two not greater than data length
    int length = (int) Math.pow(2, Math.ceil(Math.log(data.length)/Math.log(2)));
    // Builds the complex buffer and computes fft
    complex cdata[] = new complex[length]; for (int i = 0; i < length; i++) cdata[i] = new complex(i < data.length ? data[i] : 0, 0); return fft(cdata);
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

  /** Gets the frequency of a given note.
   * @param note The note is written as <tt>A4</tt> for the middle-board <tt>A</tt> (<i>La</i>) at 440Hz. <ul>
   * <li>The letter: <table>
   * <tr><td><tt>A</tt></td><td><tt>B</tt></td><td><tt>C</tt></td><td><tt>D</tt></td><td><tt>E</tt></td><td><tt>F</tt></td><td><tt>G</tt></td></tr>
   * <tr><td><i>La</i></td><td><i>Si</i></td><td><i>Do</i></td><td><i>Ré</i></td><td><i>Mi</i></td><td><i>Fa</i></td><td><i>Sol</i></td></tr>
   * </table> stands for the note name. The <tt>h</tt> stands for a ``halt´´, a silence.</li>
   * <li>The digit, from <tt>0</tt> to <tt>8</tt> included, stands for the octave, default is <tt>4</tt>.</li>
   * <li>The <tt>#</tt> or <tt>b</tt> suffix stands for the <i>sharp</i> or <i>flat</i> modulation respectively.</li>
   * </ul> e.g., <tt>G1#</tt> is the 1st piano fingerboard G sharp. Lowercase letters are understood.
   * @return The note frequency in Hz.
   */
  private static double getNote(String note) {
    // Frequency constants
    final double 
      SharpPitch = Math.pow(2.0, 1.0/12.0), FlatPitch = Math.pow(SharpPitch, -1), 
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
  
  /** Gets the frequency array of a given note sequence. 
   * @param notes The notes are written as a sequence:<ul>
   * <li> of <a href="#getNote(java.lang.String)">note</a>, separated with spaces;</li>
   * <li>while note's duration is noted by an integer, from <tt>1</tt> to <tt>999</tt>, separated with spaces;
   * here, <tt>1</tt> stands for the minimal note duration (e.g. quaver), the note duration being valid until a new duration is set;</li>
   * <li>other pattern being ignored.</li>
   * </ul>
   * @return An array of frequencies in Hz.
   */
  private static double[] getNotes(String notes) {
    Vector<Double> freqs = new Vector<Double>();
    String n[] = notes.split("[ \t\n]"); int d = 1;
    for(int i = 0; i < n.length; i++) {
      if(n[i].matches("[1-9][0-9]?[0-9]?")) {
	d = Integer.valueOf(n[i]);
      } else if(n[i].matches("[a-h][0-8]?[#b]?")) {
	double f = getNote(n[i]); for(int k = 0; k < d; k++) freqs.add(f);
      }
    }
    final boolean DEBUG = false; if(DEBUG) System.out.println(freqs.toString());
    double f[] = new double[freqs.size()]; for(int i = 0; i < freqs.size(); i++) f[i] = freqs.get(i).doubleValue(); return f;
  }
   // The Elise'letter piano right-hand 1st notes are played as on a ``piccolo´´ (for debugging purpose only !).
  // s.setNotes("e5 e5b e5 e5b e5 e5b e5 b d5 c5 4 a | 1 h c e a 4 b | 1 h e g g# 4 a", 0.25);

  /** Plays and shows a sound.
   * @param usage <tt>java SoundBit ([sound-bit-class-name] [length-in-second] | sound-file-name)</tt>
   * <div>Plays the sound and shows the left channel spectrum and trace's start.</div>
   */
  public static void main(String usage[]) { 
    String sound = usage.length == 1 && !usage[0].matches("[0-9\\.]+") ? usage[0] : "proglet.SoundBit";
    String length = usage.length == 2  && usage[1].matches("[0-9\\.]+") ? usage[1] : usage.length == 1 && usage[0].matches("[0-9\\.]+") ? usage[0] : "10";
    try { 
      SoundBit s = new SoundBit(); try { s = (SoundBit) Class.forName(sound).newInstance(); s.setLength(Double.valueOf(length)); } catch(Exception e1) { 
	try { s = getSound(sound); } catch(Exception e2) { System.err.println("Unable to define the sound: "+sound); System.exit(-1); } }
      s.show('l');
      System.out.print("playing .."); System.out.flush(); s.play(); System.out.println(" done."); 
    } catch(Exception e) { System.err.println(e); e.printStackTrace(); }
  }
}
