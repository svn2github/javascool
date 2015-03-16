/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved. *
 *******************************************************************************/

package org.javascool.widgets;

// Used to define the gui
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 * Panneau pour l'entrée de valeurs numériques.
 * 
 * @see <a href="NumberInput.java.html">source code</a>
 * @serial exclude
 */
public class NumberInput extends JPanel {
    private static final long serialVersionUID = 1L;

    // @bean
    public NumberInput() {
	setPreferredSize(new Dimension(400, 62));
	field = new JTextField(12);
	field.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent evt) {
		try {
		    set(new Double(field.getText()).doubleValue(), 'T');
		} catch (NumberFormatException e) {
		}
	    }
	});
	add(field);
	slider = new JSlider();
	slider.setFont(new Font("Dialog", Font.PLAIN, 0));
	slider.addMouseListener(new MouseListener() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    }

	    @Override
	    public void mouseEntered(MouseEvent e) {
	    }

	    @Override
	    public void mouseExited(MouseEvent e) {
	    }

	    @Override
	    public void mousePressed(MouseEvent e) {
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		set((NumberInput.this.max - NumberInput.this.min) / 100.0 * slider.getValue() + NumberInput.this.min, 'S');
	    }
	});
	add(slider);
	setText("");
	setScale(0, 100, 1);
	setValue(0);
    }

    private JTextField field;
    private JSlider slider;

    // Display the value
    private void set(double value, char from) {
	// Retrain value to be step by step and in the min-max interval
	value = step <= 0 ? value : min + step * Math.rint((value - min) / step);
	value = value < min ? min : value > max ? max : value;
	this.value = value;
	field.setText(new Double(value).toString().replaceFirst("(99999|00000).*$", "").replaceFirst(".0$", ""));
	if (from != 'S') {
	    slider.setValue((int) ((max > min) ? 100.0 * (value - min) / (max - min) : value));
	}
	if ((from != ' ') && (runnable != null)) {
	    runnable.run();
	}
    }

    /**
     * Définit le nom de la valeur numérique.
     * 
     * @param name
     *            Nom du paramètre.
     * @return Cet objet, permettant de définir la construction
     *         <tt>new NumberInput().setScale(..)</tt>.
     */
    public final NumberInput setText(String name) {
	setBorder(BorderFactory.createTitledBorder(name));
	return this;
    }

    /**
     * Définit le nom et les paramètres de la valeur numérique.
     * 
     * @param min
     *            Valeur minimale à entrer. 0 par défaut.
     * @param max
     *            Valeur maximale à entrer. 100 par défaut.
     * @param step
     *            Précision de la valeur à entrer. 1 par défaut.
     * @return Cet objet, permettant de définir la construction
     *         <tt>new NumberInput().setScale(..)</tt>.
     */
    public final NumberInput setScale(double min, double max, double step) {
	this.min = min;
	this.max = max;
	this.step = step;
	return this;
    }

    /** @see #setScale(double, double, double) */
    public final NumberInput setScale() {
	return setScale(0, 100, 1);
    }

    /** Renvoie la valeur numérique. */
    public double getValue() {
	return value;
    }

    /** Définit la valeur numérique. */
    public void setValue(double value) {
	set(value, ' ');
    }

    /**
     * Définit une portion de code appellée à chaque modification de la valeur.
     * 
     * @param runnable
     *            La portion de code à appeler, ou null si il n'y en a pas.
     * @return Cet objet, permettant de définir la construction
     *         <tt>new NumberInput().setRunnable(..)</tt>.
     */
    public NumberInput setRunnable(Runnable runnable) {
	this.runnable = runnable;
	return this;
    }

    private Runnable runnable = null;

    private double min, max, step, value;
}
