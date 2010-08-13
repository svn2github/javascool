/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package proglet.synthesons;

// Used to define an audio stream
import org.javascool.SoundBit;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;

// Used to show a curve
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

/** Defines the drawing of a SoundBit spectrum and trace's start.
 * <div>Opens a frame, drawing: <ul>
 *  <li>drawing the frequencies amplitudes (in red, in normalized dB (log coordinates)), frequencies being drawn between A0 (27.5Hz) and A9 (6400Hz) around A3 (440Hz);</li>
 *  <li>drawing the 1st samples of the signal (in yellow, the 1st <tt>11</tt>ms).</li>
 * </ul> while the sound name main frequency and spectral magnitude is printed.
 * @see <a href="SoundBitPanel.java.html">code source</a>
 * @serial exclude
 */
public class SoundBitPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  /** Defines the spectrum and trace's start of the sound.
   * @param sound The sound to visualize.
   * @param channel Left 'l' or right 'r' channel.
   */
  public void reset(SoundBit sound, char channel) { 
    // Calculates the spectrum magnitude and phase
    data = getData(sound, channel); complex fft[] = getFFT(data); mag = new double[hsize+1];
    for(int i = 0; i <= hsize; i++) { 
      double f = f0 * Math.pow(f1/ f0, i / (double) hsize); int k = (int) Math.rint(fft.length * f / SoundBit.SAMPLING); 
      mag[i] = Math.sqrt(fft[k].x * fft[k].x + fft[k].y * fft[k].y);
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

  /** Converts a mono/stereo 16bit stream to a data buffer.
   * @param stream The audio stream to convert.
   * @param channel Left 'l' or right 'r' channel.
   */
  private static double[] getData(SoundBit sound, char channel) {
    AudioInputStream stream = sound.getStream();
    SoundBit.checkFormat(stream);
    if (stream.getFrameLength() > (long) Integer.MAX_VALUE) throw new IllegalArgumentException("Cannot convert huge audio stream to buffer");
    int length = (int) stream.getFrameLength(); double data[] = new double[length]; 
    int n = stream.getFormat().getChannels() * 2, o = stream.getFormat().getChannels() == 1 || channel == 'l' ? 0 : 2; byte read[] = new byte[n];
    try {
      for(int i = 0; i < length; i++) {	
	stream.read(read); int h = read[o+1], l = read[o], v = ((128 + h) << 8) | (128 + l); data[i] = 1 * (v / 32767.0 - 1);
      }
    }  catch(IOException e) {
      throw new RuntimeException(e+" when reading the audio stream "+sound.getName());
    }
    return data;
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
}
