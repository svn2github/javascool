package org.javascool.widgets;

// Used to set Win look and feel
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UIManager;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JRootPane;
import java.applet.Applet;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import org.javascool.macros.Macros;
import org.javascool.core.Jvs2Java;

/** Définit une fenêtre principale pour lancer une application. */
public class MainFrame extends JFrame {
  private Component pane;
  // @bean
  public MainFrame() {}

  /** Compte des fenêtres ouvertes pour gérer le exit. */
  private static int frameCount = 0;

  /** Flag définissant l'utilisation de Nimbus. */
  public static final boolean withNimbus = false;

  /** Définit le look and feel de l'application. */
  static void setLookAndFeel() {
    try {
      if (withNimbus)
	for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
	  if("Nimbus".equals(info.getName())) {
	    UIManager.setLookAndFeel(info.getClassName());
	    break;
	  }
    } catch(Exception e1) {
      String os = System.getProperty("os.name");
      if(os.startsWith("Windows")) {
        try {
          UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch(Exception e2) {}
      } else {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e2) {
          System.err.println("Note: Utilisaton du thème Java (et non du système)");
        }
      }
    }
  }
  static {
    setLookAndFeel();
  }

  /** Construit la fenêtre sans boutons de fermeture.
   * <p>- Doit être appelé avant la méthode reset.</p>
   * @return Cet objet, permettant de définir la construction <tt>new MainFrame().asPopup().reset(..)</tt>.
   */
  public MainFrame asPopup() {
    // @todo voir sur mac si ca solde le pb du close intempestif
    if(System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
      setUndecorated(true);
      getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    } else if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
      // Si besoin de faire qq chose sous windows
    }
    return this;
  }
  /** Construit et ouvre une fenêtre principale pour lancer une application.
   * @param title Le titre de la fenêtre.
   * @param icon L'icône de la fenêtre.
   * @param width Largeur de la fenêtre. Si 0 on prend tout l'écran.
   * @param height Hauteur de la fenêtre. Si 0 on prend tout l'écran.
   * @param pane Le composant graphique à afficher.
   * @return Cet objet, permettant de définir la construction <tt>new MainFrame().reset(..)</tt>.
   */
  public MainFrame reset(String title, String icon, int width, int height, Component pane) {
    if(title != null) {
      setTitle(title);
    }
    if(System.getProperty("os.name").toUpperCase().contains("MAC")) {
      try {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", title);
      } catch(Exception e2) {}
    }
    if(icon != null) {
      ImageIcon image = Macros.getIcon(icon);
      if(image != null) {
        setIconImage(image.getImage());
      }
    }
    add(this.pane = pane);
    if(pane instanceof Applet) {
      ((Applet) pane).init();
    }
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                          close();
                        }
                      }
                      );
    pack();
    if((width > 0) && (height > 0)) {
      setSize(width, height);
    } else {
      setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    setLocation(100+frameCount*250,100+frameCount*150);
    setVisible(true);
    if(pane instanceof Applet) {
      ((Applet) pane).start();
    }
    if(firstFrame == null) {
      firstFrame = this;
    }
    frameCount++;
    return this;
  }
  /**
   * @see #reset(String, String, int, int, Component)
   */
  public MainFrame reset(String title, int width, int height, Component pane) {
    return reset(title, null, width, height, pane);
  }
  /**
   * @see #reset(String, String, int, int, Component)
   */
  public MainFrame reset(String title, String icon, Component pane) {
    return reset(title, icon, 0, 0, pane);
  }
  /**
   * @see #reset(String, String, int, int, Component)
   */
  public MainFrame reset(String title, Component pane) {
    return reset(title, null, 0, 0, pane);
  }
  /**
   * @see #reset(String, String, int, int, Component)
   */
  public MainFrame reset(int width, int height, Component pane) {
    return reset(pane.getClass().toString(), null, width, height, pane);
  }
  /**
   * @see #reset(String, String, int, int, Component)
   */
  public MainFrame reset(Component pane) {
    return reset(pane.getClass().toString(), null, 0, 0, pane);
  }
  /** Ajoute le déclenchement d'un runnable.
   * @param runnable Le runnable à exécuter à travers un boutton de start-stop.
   * @return Cet objet, permettant de définir la construction <tt>new MainFrame().reset(..)</tt>.
   */
  public MainFrame setRunnable(Runnable runnable) {
    if (runnable == null) {
      if (runnableStartStopButton != null)
	remove(runnableStartStopButton);
      runnableStartStopButton = null;
    } else {
      if (runnableStartStopButton == null)
	add(runnableStartStopButton = new StartStopButton() {
	    private static final long serialVersionUID = 1L;
	    @Override
	    public void start() {
	      stop();
	      (runnableThread = new Thread(new Runnable() {
		  @Override
		  public void run() {
		    try {
		       MainFrame.this.runnable.run();
		       runnableThread = null;
		      } catch(Throwable e) {
			Jvs2Java.report(e);
		      }
		    }
		  }
		  )).start();
		}
		@Override
		public void stop() {    
		if(runnableThread != null) {
		  runnableThread.interrupt();
		  runnableThread = null;
		}
	      }
	      
	      @Override
		public boolean isRunning() {
		return runnableThread != null;
	      }
	      private Thread runnableThread = null;
	    }, BorderLayout.NORTH);
	  }
    this.runnable = runnable;
    return this;
  }
  private StartStopButton runnableStartStopButton = null;
  private Runnable runnable = null;

  /** Ferme la fenêtre principale à partir du programme.
   * @param force Si true ferme la fenêter même si isClosable() renvoie false.
   */
  public void close(boolean force) {
    if(force || isClosable()) {
      if(MainFrame.this.pane instanceof Applet) {
        try {
          ((Applet) MainFrame.this.pane).stop();
        } catch(Throwable e1) {}
        try {
          ((Applet) MainFrame.this.pane).destroy();
        } catch(Throwable e2) {}
      }
      setVisible(false);
      dispose();
      frameCount--;
      if(frameCount == 0) {
        System.exit(0);
      }
    }
  }
  /**
   * @see #close(boolean)
   */
  public void close() {
    close(false);
  }
  /** Détermine si la fenêtre principale peut-être fermée.
   * @return La valeur true si la fenêtre principale peut-être fermée, sinon false.
   */
  public boolean isClosable() {
    return true;
  }
  /** Renvoie la frame principale ouverte.
   * @return La frame principale ouverte comme parent des dialogues modaux.
   */
  public static JFrame getFrame() {
    return firstFrame;
  }
  private static JFrame firstFrame = null;
}
