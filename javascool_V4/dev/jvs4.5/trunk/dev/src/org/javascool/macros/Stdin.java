/*********************************************************************************
 * Philippe.Vienne@sophia.inria.fr, Copyright (C) 2011.  All rights reserved.    *
 * Guillaume.Matheron@sophia.inria.fr, Copyright (C) 2011.  All rights reserved. *
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved.   *
 *********************************************************************************/

package org.javascool.macros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.javascool.widgets.Dialog;

/**
 * Cette factory contient des fonctions générales rendues visibles à
 * l'utilisateur de proglets.
 * <p>
 * Elle permet de définir des fonctions statiques qui seront utilisées pour
 * faire des programmes élèves.
 * </p>
 * <p>
 * Elle permet aussi avoir quelques fonctions de base lors de la création de
 * nouvelles proglets.
 * </p>
 * 
 * @see <a href="Stdin.java.html">code source</a>
 * @serial exclude
 */
public class Stdin {
	// @factory
	private Stdin() {
	}

	/**
	 * Lit une chaîne de caractère dans une fenêtre présentée à l'utilisateur.
	 * 
	 * @param question
	 *            Une invite qui décrit la valeur à entrer (optionel).
	 * @return La chaîne lue.
	 */
	public static String readString(String question) {
		if (Stdin.inputBuffer.isPopable())
			return Stdin.inputBuffer.popString();
		Stdin.inputQuestion = question;
		Stdin.inputString = null;
		Stdin.inputDialog = new Dialog();
		Stdin.inputDialog.setTitle("Java's Cool read");
		Stdin.inputDialog.add(new JPanel() {
			/**
		 * 
		 */
			private static final long serialVersionUID = -5892042920567248401L;

			{
				add(new JLabel(Stdin.inputQuestion + " "));
				add(new JTextField(40) {
					/**
							 * 
							 */
					private static final long serialVersionUID = -877837293331380204L;

					{
						addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Stdin.inputString = ((JTextField) e.getSource())
										.getText();
								Stdin.inputDialog.close();
							}
						});
					}
				});
			}
		});
		Stdin.inputDialog.open(true);
		return Stdin.inputString == null ? "" : Stdin.inputString;
	}

	private static Dialog inputDialog;
	private static String inputQuestion, inputString;

	/**
	 * @see #readString(String)
	 */
	public static String readString() {
		return Stdin.readString("Entrez une chaîne :");
	}

	/**
	 * Lit un nombre entier dans une fenêtre présentée à l'utilisateur.
	 * 
	 * @param question
	 *            Une invite qui décrit la valeur à entrer (optionel).
	 * @return La valeur lue.
	 */
	public static int readInteger(String question) {
		if (Stdin.inputBuffer.isPopable())
			return Stdin.inputBuffer.popInteger();
		String s = Stdin.readString(question);
		try {
			return Integer.decode(s);
		} catch (Exception e) {
			if (!question.endsWith(" (Merci d'entrer un nombre entier)")) {
				question = question + " (Merci d'entrer un nombre entier)";
			}
			if (s.equals(""))
				return 0;
			return Stdin.readInteger(question);
		}
	}

	/**
	 * @see #readInteger(String)
	 */
	public static int readInteger() {
		return Stdin.readInteger("Entrez un nombre entier : ");
	}

	/**
	 * @see #readInteger(String)
	 */
	public static int readInt(String question) {
		return Stdin.readInteger(question);
	}

	/**
	 * @see #readInteger(String)
	 */
	public static int readInt() {
		return Stdin.readInteger();
	}

	/**
	 * Lit un nombre décimal dans une fenêtre présentée à l'utilisateur.
	 * 
	 * @param question
	 *            Une invite qui décrit la valeur à entrer (optionel).
	 * @return La valeur lue.
	 */
	public static double readDecimal(String question) {
		if (Stdin.inputBuffer.isPopable())
			return Stdin.inputBuffer.popDecimal();
		String s = Stdin.readString(question);
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			if (!question.endsWith(" (Merci d'entrer un nombre)")) {
				question = question + " (Merci d'entrer un nombre)";
			}
			if (s.equals(""))
				return 0;
			return Stdin.readDecimal(question);
		}
	}

	/**
	 * @see #readDecimal(String)
	 */
	public static double readDecimal() {
		return Stdin.readDecimal("Entrez un nombre décimal : ");
	}

	/**
	 * @see #readDecimal(String)
	 */
	public static double readDouble(String question) {
		return Stdin.readDecimal(question);
	}

	/**
	 * @see #readDecimal(String)
	 */
	public static double readDouble() {
		return Stdin.readDecimal();
	}

	/**
	 * @see #readDecimal(String)
	 */
	public static double readFloat(String question) {
		return Stdin.readDecimal(question);
	}

	/**
	 * @see #readDecimal(String)
	 */
	public static double readFloat() {
		return Stdin.readDecimal();
	}

	/**
	 * Lit une valeur booléenne dans une fenêtre présentée à l'utilisateur.
	 * 
	 * @param question
	 *            Une invite qui décrit la valeur à entrer (optionel).
	 * @return La valeur lue.
	 */
	public static boolean readBoolean(String question) {
		if (Stdin.inputBuffer.isPopable())
			return Stdin.inputBuffer.popBoolean();
		Stdin.inputQuestion = question;
		Stdin.inputString = null;
		Stdin.inputDialog = new Dialog();
		Stdin.inputDialog.setTitle("Java's Cool read");
		Stdin.inputDialog.add(new JPanel() {
			/**
		 * 
		 */
			private static final long serialVersionUID = -3550776051477271733L;

			{
				add(new JLabel(Stdin.inputQuestion + " "));
				add(new JButton("OUI") {
					/**
							 * 
							 */
					private static final long serialVersionUID = 1846013448517515880L;

					{
						addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Stdin.inputString = "OUI";
								Stdin.inputDialog.close();
							}
						});
					}
				});
				add(new JButton("NON") {
					/**
							 * 
							 */
					private static final long serialVersionUID = -5759851092046561872L;

					{
						addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								Stdin.inputString = "NON";
								Stdin.inputDialog.close();
							}
						});
					}
				});
			}
		});
		Stdin.inputDialog.open(true);
		return "OUI".equals(Stdin.inputString);
	}

	/**
	 * @see #readBoolean(String)
	 */
	public static boolean readBoolean() {
		return Stdin.readBoolean("Entrez une valeur booléenne (oui/non) : ");
	}

	/**
	 * @see #readBoolean(String)
	 */
	public static Boolean readBool(String question) {
		return Stdin.readBoolean(question);
	}

	/**
	 * @see #readBoolean(String)
	 */
	public static Boolean readBool() {
		return Stdin.readBoolean();
	}

	/**
	 * Charge une chaine de caractère pour que son contenu serve d'entrée à la
	 * console.
	 * 
	 * @param string
	 *            La chaine de caractère à ajouter.
	 */
	public static void addConsoleInput(String string) {
		Stdin.inputBuffer.add(string);
	}

	/**
	 * Charge le contenu d'un fichier pour que son contenu serve d'entrée à la
	 * console.
	 * 
	 * @param location
	 *            La localisation (chemin du fichier ou localisation internet)
	 *            d'où charger le texte.
	 */
	public static void loadConsoleInput(String location) {
		Stdin.addConsoleInput(org.javascool.tools.FileManager.load(location));
	}

	/**
	 * Définit une zone tampon qui permet de substituer un fichier aux lectures
	 * au clavier.
	 */
	private static class InputBuffer {
		String inputs = new String();

		/**
		 * Ajoute une chaîne en substitution d'une lecture au clavier.
		 * 
		 * @param string
		 *            Le texte à ajouter.
		 */
		public void add(String string) {
			inputs += string.trim() + "\n";
		}

		/**
		 * Teste si il y une chaîne disponible.
		 * 
		 * @return La valeur true si il y une entrée disponible.
		 */
		public boolean isPopable() {
			return inputs.length() > 0;
		}

		/**
		 * Récupére une chaîne en substitution d'une lecture au clavier.
		 * 
		 * @return Le texte suivant à considérer. Ou la chaîne vide si le tampon
		 *         est vide.
		 */
		public String popString() {
			Macros.sleep(500);
			int i = inputs.indexOf("\n");
			if (i != -1) {
				String input = inputs.substring(0, i);
				inputs = inputs.substring(i + 1);
				return input;
			} else
				return "";
		}

		/**
		 * @see #popString(String)
		 */
		public int popInteger() {
			try {
				return Integer.decode(popString());
			} catch (Exception e) {
				return 0;
			}
		}

		/**
		 * @see #popString(String)
		 */
		public double popDecimal() {
			try {
				return Double.parseDouble(popString());
			} catch (Exception e) {
				return 0;
			}
		}

		/**
		 * @see #popString(String)
		 */
		public boolean popBoolean() {
			// Renvoie vrai si [t]rue [y]es [v]rai [o]ui 1
			return popString().toLowerCase().matches("[tyvo1].*");
		}
	}

	private static InputBuffer inputBuffer = new InputBuffer();

	/**
	 * Définit une portion de code appelée à chaque entrée d'un caractère au
	 * clavier.
	 * <p>
	 * Les caractères du clavier ne sont détectés que si la souris est sur la
	 * fenêtre de la proglet de façon à ce qu'elle est le focus.
	 * </p>
	 * <p>
	 * Les caractères du clavier et quelques touches de contrôle sont gérés.
	 * </p>
	 * 
	 * @param runnable
	 *            La portion de code à appeler, ou null pour annuler l'appel à
	 *            la portion de code précédent.
	 */
	public static void setKeyListener(Runnable runnable) {
		if (Stdin.keyKeyListener != null) {
			Macros.getProgletPane().removeKeyListener(Stdin.keyKeyListener);
		}
		if (Stdin.keyMouseListener != null) {
			Macros.getProgletPane().removeMouseListener(Stdin.keyMouseListener);
		}
		if (Macros.getProgletPane() != null
				&& (Stdin.keyListenerRunnable = runnable) != null) {
			Macros.getProgletPane().addMouseListener(
					Stdin.keyMouseListener = new MouseListener() {
						@Override
						public void mousePressed(MouseEvent e) {
						}

						@Override
						public void mouseReleased(MouseEvent e) {
						}

						@Override
						public void mouseClicked(MouseEvent e) {
						}

						@Override
						public void mouseEntered(MouseEvent e) {
							Macros.getProgletPane().requestFocusInWindow();
						}

						@Override
						public void mouseExited(MouseEvent e) {
						}
					});
			Macros.getProgletPane().addKeyListener(
					Stdin.keyKeyListener = new KeyListener() {
						@Override
						public void keyPressed(KeyEvent e) {
						}

						@Override
						public void keyReleased(KeyEvent e) {
							String s = KeyEvent.getKeyText(e.getKeyCode());
							if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
								Stdin.lastKey = "Ctrl+" + s;
							} else {
								int c = e.getKeyChar();
								if (32 <= c && c < 127) {
									Stdin.lastKey = "" + e.getKeyChar();
								} else {
									if ("Shift".equals(s) || "Ctrl".equals(s))
										return;
									Stdin.lastKey = s;
								}
							}
							if (Stdin.keyListenerRunnable != null) {
								Stdin.keyListenerRunnable.run();
							}
						}

						@Override
						public void keyTyped(KeyEvent e) {
						}
					});
		}
	}

	/**
	 * Renvoie la dernière touche entrée au clavier ou la chaine vide sinon.
	 * 
	 * @return Renvoie le caractère associée à la touche si il est défini, sinon
	 *         une chaîne qui représente le caractère de contrôle, par exemple
	 *         'Left', 'Up, 'Right', 'Down' pour les flèches, 'F1, 'F2', .. pour
	 *         les touches de fonctions, 'Alt', 'Escape', 'Backspace', 'Enter',
	 *         'Page Down', 'Page Up', 'Home', 'end' pour les autres touches,
	 *         'Ctrl+A' pour la combinaison de la touche 'Control' et 'A', etc.
	 */
	public static String getLastKey() {
		return Stdin.lastKey;
	}

	private static Runnable keyListenerRunnable = null;
	private static KeyListener keyKeyListener = null;
	private static String lastKey = "";
	private static MouseListener keyMouseListener = null;
}
