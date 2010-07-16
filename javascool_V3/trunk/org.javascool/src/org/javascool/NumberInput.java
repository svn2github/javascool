/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// Used to define the gui
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;

// Used for the text field
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Used for the slider
import javax.swing.JSlider;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/** This widget defines a numeric input.
 * @see <a href="NumberInput.java">source code</a>
 */
public class NumberInput extends JPanel {
  private static final long serialVersionUID = 1L;

  /** Construct the field.
   * @param name Field name.
   */
  public NumberInput(String name) {
    setBorder(BorderFactory.createTitledBorder(name)); setPreferredSize(new Dimension(400, 62));
    field = new JTextField(12);
    field.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent evt) {
	  try { set(new Double(field.getText()).doubleValue(), 'T'); } catch(NumberFormatException e) { } 
	}
      });
    add(field);
    slider = new JSlider();
    slider.setFont(new Font("Dialog", Font.PLAIN, 0));
    slider.addMouseListener(new MouseListener() {
	public void mouseClicked(MouseEvent e) { } 
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) {
	  set((NumberInput.this.max - NumberInput.this.min) / 100.0 * slider.getValue() + NumberInput.this.min, 'S');
	}
      });
    add(slider);
    setScale(0, 100, 1);
    setValue(0);
  }
  private JTextField field; private JSlider slider;
  // Display the value
  private void set(double value, char from) {
    // Retrain value to be step by step and in the min-max interval
    value = step <= 0 ? value : min + step * Math.rint((value - min) / step); value = value < min ? min : value > max ? max : value;
    this.value = value;
    field.setText(new Double(value).toString().replaceFirst("(99999|00000).*$", "").replaceFirst(".0$", ""));
    if (from != 'S') slider.setValue((int) ((max > min) ? 100.0 * (value - min) / (max - min) : value));
    if (from != ' ' && run != null) run.run();
  }

  /** Sets the scales.
   * @param min Minimal input value.
   * @param max Maximal input value.
   * @param step Precision of the input value.
   */
  public void setScale(double min, double max, double step) {
    this.min = min; this.max = max; this.step = step; 
  }

  /** Gets the input value. */
  public double getValue() { return value; }

  /** Sets the input value. */
  public void setValue(double value) { set(value, ' ' ); }

  /** Sets the runnable called when the input is changed. 
   * @param run The runnable to call, set to null if no runnable.
   */
  public void setRunnable(Runnable run) { this.run = run; } private Runnable run = null;

  private double min, max, step, value;
}
