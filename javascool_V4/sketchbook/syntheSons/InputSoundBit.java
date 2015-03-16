/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package org.javascool.proglets.syntheSons;

// Used to define an audio stream input
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

import org.javascool.tools.sound.SoundBit;

/** Defines a microphone-input sound-bit wrapper.
 * @see <a href="InputSoundBit.java.html">code source</a>
 * @serial exclude
 * @deprecated
 * WARNING: THIS CLASS IS DEPRECATED, USE org.javascool.tools.sound.InputSoundBit INSTEAD.
 */
@Deprecated
public class InputSoundBit extends SoundBit {
  private TargetDataLine line = null;
  private boolean recording = false;

  /** Constructs a sound defined from an audio input.
   * @param length Duration of the recording in second, unless the method stop() is called.
   *
   * @throws RuntimeException if an I/O exception occurs during command execution.
   *
   * @return This, allowing to use the <tt>SoundBit pml = new InputSoundBit().reset(..)</tt> construct.
   */
  public SoundBit reset(double length) {
    buffer = new byte[2 * (int) (SAMPLING * (this.length = length))];
    AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                         SAMPLING, // Sample rate in Hz
                                         16, // Sampling bits
                                         1, // Number of channels
                                         2, // Frame size in bytes
                                         SAMPLING, // Frame rate in Hz
                                         false); // Little-endian byte order
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
    if(info != null) {
      try {
        line = (TargetDataLine) AudioSystem.getLine(info);
        // Data format: 16 bit mono: each frame contains two bytes x one channel = two bytes
        line.open(format);
        new Thread(new Runnable() {
                     public void run() {
                       int offset = 0, length = line.getBufferSize() / 5;
                       line.start();
                       for(recording = true; offset < buffer.length && recording; offset += length)
                         line.read(buffer, offset, offset + length < buffer.length ? length : buffer.length - offset);
                       InputSoundBit.this.length = (int) (0.5 * offset / SAMPLING);
                       System.err.println("sound input : " + offset + " bytes, " + InputSoundBit.this.length + " sec");
                       line.stop();
                       line.close();
                     }
                   }
                   ).start();
      } catch(LineUnavailableException e) { throw new RuntimeException("No microphone available for this audio system (" + e + ")");
      }
    } else { throw new RuntimeException("Oh, il ne semble pas y avoir de microphone disponible sur votre système");
    }
    return this;
  }
  /** Stops the recording if still pending. */
  public void stop() {
    recording = false;
  }
  /** Returns true if the acquisition is ended. */
  public boolean isStopped() {
    return recording;
  }
  @Override
  public double get(char channel, long index) {
    int i = (int) index * 2;
    if((buffer == null) || (i < 0) || (i >= buffer.length)) {
      return 0;
    }
    int h = buffer[i + 1], l = buffer[i], v = ((128 + h) << 8) | (128 + l);
    return 1 * (v / 32767.0 - 1);
  }
  @Override
  public double get(char channel, double time) {
    return get(channel, (long) (SAMPLING * time));
  }
  private byte[] buffer = null;
  /**/ @Override
  public void setLength(double length) { throw new IllegalStateException("Cannot adjust length of buffered sound-bit of name " + getName());
  }
}
