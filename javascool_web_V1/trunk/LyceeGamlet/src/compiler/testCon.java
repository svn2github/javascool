package compiler;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JApplet;

public class testCon extends JApplet {

	private JPanel jContentPane = null;
	static Konsol console;
	/**
	 * This is the xxx default constructor
	 */
	public testCon() {
		super();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void init() {
		this.setSize(500, 500);
		this.setContentPane(getJContentPane());
		new Thread(new Runnable(){public void run(){  Konsol.echo("Bonjour, qui est tu ?");
    String nom = Konsol.readString();
    Konsol.echo ("Echanté "+nom+" ! Quel age as tu ?");
     int age = Konsol.readInteger();
     for(int n = 0; n < 100; n++)
	Konsol.echo("He je suis plus vieux que toi !!");
		}}).start();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(	getKonsol().getPanel() );
		}
		return jContentPane;
	}
	Konsol getKonsol(){return(console);}
}
