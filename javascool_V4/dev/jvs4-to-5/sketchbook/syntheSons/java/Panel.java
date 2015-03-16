/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
*******************************************************************************/

package org.javascool.proglets.syntheSons;
import static org.javascool.proglets.syntheSons.Functions.*;

import org.javascool.tools.sound.SoundBit;

/** Defines the GUI of this proglet.
 *
 * @see <a href="Panel.java.html">code source</a>
 * @serial exclude
 */
public class Panel extends SoundBitPanel {
  private static final long serialVersionUID = 1L;
  public SoundBit sound = new NotesSoundBit() {
    @Override
    public double get(char channel, double time) {
      return Functions.tone == null ? Math.sin(2 * Math.PI * time) : Functions.tone.get(channel, time);
    }
  };
  // @bean
  public Panel() {
    reset(sound, 'l');
    sound.reset("16 A");
  }
}
