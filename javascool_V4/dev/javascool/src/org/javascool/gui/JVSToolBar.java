package org.javascool.gui;

import javax.swing.JButton;

import org.javascool.About;
import org.javascool.builder.DialogFrame;
import org.javascool.builder.ProgletsBuilder;
import org.javascool.core.ProgletEngine;
import org.javascool.widgets.StartStopButton;
import org.javascool.widgets.ToolBar;

/**
 * La barre d'outils de Java's cool Elle est placée en haut de l'interface. Elle
 * contient les boutons de gestion des fichiers, de compilation et d'éxecution.
 * 
 * @see org.javascool.widgets.StartStopButton
 * @see org.javascool.gui.JVSPanel
 */
public class JVSToolBar extends ToolBar {

	private static final long serialVersionUID = 1L;
	/** Boutons de l'interface. */
	private JButton compileButton;
	private JButton demoButton;
	private StartStopButton runButton;
	/** Instance de la classe */
	private static JVSToolBar jvstb;

	public static JVSToolBar getInstance() {
		if (JVSToolBar.jvstb == null) {
			JVSToolBar.jvstb = new JVSToolBar();
		}
		return JVSToolBar.jvstb;
	}

	// @ inner-class-variable
	@SuppressWarnings("unused")
	private JButton pbutton;

	private JVSToolBar() {
		setName("Java's cool ToolBar");
		init();
	}

	public void disableCompileButton() {
		compileButton.setVisible(false);
		revalidate();
	}

	public void disableDemoButton() {
		demoButton.setVisible(false);
		revalidate();
	}

	public void disableStartStopButton() {
		runButton.setVisible(false);
		revalidate();
	}

	public void enableCompileButton() {
		compileButton.setVisible(true);
		revalidate();
	}

	public void enableDemoButton() {
		demoButton.setVisible(true);
		revalidate();
	}

	public void enableStartStopButton() {
		runButton.setVisible(true);
		revalidate();
	}

	/** Initialize la barre d'outils en créant les bouttons */
	private void init() {
		setOpaque(false);
		setBorder(null);
		addTool("Nouvelle activité", "org/javascool/widgets/icons/new.png",
				new Runnable() {

					@Override
					public void run() {
						Desktop.getInstance().closeProglet();
					}
				});
		addTool("Nouveau fichier", "org/javascool/widgets/icons/new.png",
				new Runnable() {

					@Override
					public void run() {
						Desktop.getInstance().openNewFile();
					}
				});
		addTool("Ouvrir un fichier", "org/javascool/widgets/icons/open.png",
				new Runnable() {

					@Override
					public void run() {
						Desktop.getInstance().openFile();
					}
				});
		addTool("Sauver", "org/javascool/widgets/icons/save.png",
				new Runnable() {

					@Override
					public void run() {
						Desktop.getInstance().saveCurrentFile();
					}
				});
		addTool("Sauver sous", "org/javascool/widgets/icons/saveas.png",
				new Runnable() {

					@Override
					public void run() {
						Desktop.getInstance().saveAsCurrentFile();
					}
				});

		compileButton = addTool("Compiler",
				"org/javascool/widgets/icons/compile.png", new Runnable() {

					@Override
					public void run() {
						Desktop.getInstance().compileFile();
					}
				});

		addTool("Executer", runButton = new StartStopButton() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRunning() {
				return ProgletEngine.getInstance().isRunning();
			}

			@Override
			public void start() {
				JVSWidgetPanel.getInstance().focusOnProgletPanel();
				ProgletEngine.getInstance().doRun();
			}

			@Override
			public void stop() {
				ProgletEngine.getInstance().doStop();
			}
		});
		runButton.setVisible(false);
		demoButton = addTool("Demo", "org/javascool/widgets/icons/play.png",
				new Runnable() {

					@Override
					public void run() {
						JVSWidgetPanel.getInstance().focusOnProgletPanel();
						ProgletEngine.getInstance().getProglet().doDemo();
					}
				});
		demoButton.setVisible(false);
		// Crée le menu de construction de proglets si pertinent
		if (ProgletsBuilder.canBuildProglets()) {
			pbutton = addRightTool("Proglet Builder", new Runnable() {

				@Override
				public void run() {
					DialogFrame.startFrame();
				}
			});
		}
		// @todo ici on a mit un platrage car sous Windows XP un
		// java.lang.NoClassDefFoundError: org/javascool/About incomprehensible
		// apparait !!!
		try {
			this.add(About.getAboutMessage(), 0);
		} catch (final Exception e) {
			System.err.println("Dysfonctionnement bizarre:" + e);
		}
	}
}
