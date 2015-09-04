/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package org.javascool.proglets.syntheSons;

// Used to define an audio stream
import javax.sound.sampled.AudioInputStream;

// Used to build a notes'frequencies
import java.util.ArrayList;

import org.javascool.tools.sound.SoundBit;

/** Creates a monophonic ``piccolo´´ sound-bit from a note sequence.
 * <div>The sound-bit can be changed using the <tt><a href="#reset(java.lang.String)">reset</a>(notes)</tt> method.</div>
 * Notes <a name="notes"></a> Definition of note's sequence and note:<ul>
 * <li>The notes sequences is written as:<div><ul>
 *   <li> a sequence of notes, separated with spaces;</li>
 *   <li> while note's duration is noted by an integer, from <tt>1</tt> to <tt>999</tt>, separated with spaces;
 *     <br>here, <tt>1</tt> stands for the minimal note duration (e.g. quaver),
 *     <br>the note duration being valid until a new duration is set;</li>
 *   <li> while note's intensity is written using the construct <tt>I<i>value</i></tt> where value is from <tt>0</tt> to <tt>0.999</tt>,
 *     e.g. <tt>I0.5</tt> or <tt>I5</tt> (the <tt>0.</tt> number prefix can be omitted) refers to a half-scale intensity.</li>
 *   <li>other pattern being ignored.</li>
 * </ul></div> e.g. <tt>e5 e5b e5 e5b e5 e5b e5 b d5 c5 4 a | 1 h c e a 4 b | 1 h e g g# 4 a</tt> is more or less the Elise'letter piano right-hand 1st notes.</li>
 * <li>Each note is written as <tt>A4</tt> for the middle-board <tt>A</tt> (<i>La</i>) at 440Hz. <div><ul>
 *   <li>The letter: <table>
 *     <caption>Note codes</caption>
 *     <tr><td style="width:10%;"><tt>A</tt></td><td style="width:10%;"><tt>B</tt></td><td style="width:10%;"><tt>C</tt></td><td style="width:10%;"><tt>D</tt></td><td style="width:10%;"><tt>E</tt></td><td style="width:10%;"><tt>F</tt></td><td style="width:10%;"><tt>G</tt></td><td style="width:10%;"><tt>H</tt></td></tr>
 *     <tr><td><i>La</i></td><td><i>Si</i></td><td><i>Do</i></td><td><i>Ré</i></td><td><i>Mi</i></td><td><i>Fa</i></td><td><i>Sol</i></td><td><i>silence</i></td></tr>
 *   </table> stands for the note name. The <tt>h</tt> stands for a ``halt´´, a silence.</li>
 *   <li>The digit, from <tt>0</tt> to <tt>8</tt> included, stands for the octave, default is <tt>4</tt>.</li>
 *   <li>The <tt>#</tt> or <tt>b</tt> suffix stands for the <i>sharp</i> or <i>flat</i> modulation respectively.</li>
 *  </ul></div> e.g., <tt>G1#</tt> is the 1st piano fingerboard G sharp. The notation is case insensitive.</li>
 * <li>The <i>tempo</i>, i.e. the minimal note duration in second, is declared a the beginning of the note sequence using the <tt>T value</tt> construct.
 *   Default is <tt>0.25</tt>s, while this declarations is to be done once.</li>
 * </ul>
 *
 * @see <a href="NotesSoundBit.java.html">code source</a>
 * @serial exclude
 */
public class NotesSoundBit extends SoundBit {
  /** Constructs a sound defined from a note sequence.
   * @param notes The note sequence.
   * @return This, allowing to use the <tt>SoundBit pml= new SoundBit().reset(..)</tt> construct.
   */
  @Override
  public SoundBit reset(String notes) {
    freqs = getNotes(notes);
    tempo = getTempo(notes);
    name = "notes:..";
    sound.setLength(length = freqs.length * tempo);
    return this;
  }
  // Default sound is a piccolo
  @Override
  public double get(char channel, double time) {
    return Math.sin(2 * Math.PI * time);
  }
  /** Internal sound used to sample the notes. */
  public SoundBit sound = new SoundBit() {
    @Override
    public double get(char channel, double time) {
      int i = (int) (time / tempo);
      if(i < freqs.length) {
        double d = freqs[i].f / SAMPLING;
        return freqs[i].a * NotesSoundBit.this.get(channel, channel == 'l' ? (pl += d) : (pr += d));
      } else {
        return 0;
      }
    }
  };
  private note freqs[] = new note[0];
  private double tempo = 0.25;
  private double pl = 0, pr = 0;
  @Override
  public void play() {
    pl = 0;pr = 0;
    super.play();
  }
  @Override
  public AudioInputStream getStream() {
    return sound.getStream();
  }
  /**/ @Override
  public void setLength(double length) { throw new IllegalStateException("Cannot adjust length of buffered sound-bit of name " + getName());
  }
  // Gets the low-level tempo of a given note sequence.
  private static double getTempo(String notes) {
    return notes.matches(".*[ \t\n]+T[0-9]*\\.?[0-9]+[ \t\n]+.*") ? Double.valueOf(notes.replaceFirst(".*[ \t\n]+T([0-9]*\\.?[0-9]+)[ \t\n]+.*", "\\1")) : 0.25;
  }
  // Gets the low-level array of a given note sequence.
  private static note[] getNotes(String notes) {
    ArrayList<note> freqs = new ArrayList<note>();
    String n[] = notes.trim().toLowerCase().split("[ \t\n]");
    int d = 1;
    double a = 0.999;
    for(int i = 0; i < n.length; i++) {
      if(n[i].matches("[1-9][0-9]*")) {
        d = Integer.valueOf(n[i]);
      } else if(n[i].matches("i0?\\.?[0-9]+")) {
        a = Double.valueOf(n[i].matches("[0-9]+") ? "0\\." + n[i] : n[i]);
      } else if(n[i].matches("[a-h][0-8]?[#b]?")) {
        double f = getNote(n[i]);
        for(int k = 0; k < d; k++)
          freqs.add(new note(f, a));
      }
    }
    return freqs.toArray(new note[freqs.size()]);
  }
  // Low-level note defined by a frequency and a intensity
  private static class note { 
    note(double f, double a) {
      this.f = f;
      this.a = a;
    }
    double f, a;
  }

  // Gets the frequency of a given note.
  private static double getNote(String note) {
    // Frequency constants
    final double
      SharpPitch = Math.pow(2.0, 1.0 / 12.0), FlatPitch = Math.pow(SharpPitch, -1),
      Tones[] = new double[] { 1, Math.pow(SharpPitch, 2), Math.pow(SharpPitch, -9), Math.pow(SharpPitch, -7), Math.pow(SharpPitch, -5), Math.pow(SharpPitch, -4), Math.pow(SharpPitch, -2), 0 },
      Octaves[] = new double[] { Math.pow(2, -4), Math.pow(2, -3), Math.pow(2, -2), Math.pow(2, -1), Math.pow(2, 0), Math.pow(2, 1), Math.pow(2, 2), Math.pow(2, 3), Math.pow(2, 4) };
    // Note syntax
    note = note.toLowerCase();
    if(note.matches("[a-h][#b]?")) {
      return getNote(note.charAt(0) + "4" + (note.length() == 2 ? note.charAt(1) : ""));
    }
    if(!note.matches("[a-h][0-8][#b]?")) { throw new IllegalArgumentException("Bad note format «" + note + "»");
    }
    // Note frequency derivation
    double f = 440.0 * Tones[(int) note.charAt(0) - (int) 'a'] * Octaves[(int) note.charAt(1) - (int) '0'];
    if(note.length() == 3) {
      switch(note.charAt(2)) {
      case '#':
        f *= SharpPitch;
        break;
      case 'b':
        f *= FlatPitch;
        break;
      }
    }
    return f;
  }
}
