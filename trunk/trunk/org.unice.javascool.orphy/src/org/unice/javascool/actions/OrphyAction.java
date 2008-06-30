package org.unice.javascool.actions;

import gnu.io.UnsupportedCommOperationException;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.unice.javascool.orphy.Capteur;
import org.unice.javascool.orphy.Record;
import org.unice.javascool.orphyRMI.IOrphy;
import org.unice.javascool.orphyRMI.OrphyRegister;
import org.unice.javascool.util.rmi.JavaScoolServerRegister;
import org.eclipse.swt.events.SelectionAdapter;


public class OrphyAction implements IWorkbenchWindowActionDelegate {
	private Shell fenetre, graphe,askTypeShell;
	private Label ltemps, lcoords, erreur;
	private Text torphy6, torphy7;
	private double chronoTime, interTime, stopTime, interTimeDelay;
	private int nbAcqu;
	private static Timer timer;
	public static Thread threadCompteur,  threadUI,  getValues;
	private static IOrphy orphy;
	private DecimalFormat df;
	private Display display;
	private double valueEA10, valueEA11;
	private Record recorderG, recorderH;
	private Combo typeButtonG, typeButtonH, uniteDuree, uniteInter;
	private Button stop, lecture, tracerG, tracerH, reset, recordButtonG, recordButtonH,aquiParam, 
	checkBoxType1, checkBoxType2, checkBoxType3, checkBoxType4, checkBoxType5, addRecord,validerType, valider, launchAcqu;
	private Spinner duree,intervalle,nombreAquisitions;
	private GridData gridData;
	private FillLayout fillLayout;
	private Table table;
	private Group valeureA, parameters, autoAcqu;
	private boolean choiceType, launched, acquType1, acquType2, acquType3, acquType4;
	private static volatile boolean EA10, EA11, UI, CPT, values, PROG;
	private String typeG, typeH, port;
	private double[] res10, res11;

	private ArrayList<Capteur> listCapteurs;

	/**
	 * Constructeur, initialisation des variables, créations des capteurs existants.
	 */
	public OrphyAction(){
		//storage of the values in function of time
		recorderG = new Record();
		recorderH = new Record();
		//initialize timer
		chronoTime = interTime = nbAcqu = 0;
		//initialize values of orphy's entries
		valueEA10 = valueEA11 = 0.0;
		//set the output of the orphy's values
		df = new DecimalFormat(Messages.getString("OrphyAction.0")); //$NON-NLS-1$
		//default type for the entries
		typeG = Messages.getString("OrphyAction.1");	 //$NON-NLS-1$
		typeH = Messages.getString("OrphyAction.2"); //$NON-NLS-1$

		timer = null;

		listCapteurs = new ArrayList<Capteur>();

		Capteur capteurThermo = new Capteur("Thermom\u00e8tre", " C", "T");
		Capteur capteurVolt = new Capteur("Voltm\u00e8tre", " V", "V");
		Capteur capteurPression = new Capteur("Capteur de pression", " hPa", "P");
		Capteur capteurConduct = new Capteur("Conductim\u00e8tre", " mS", "C");
		listCapteurs.add(capteurThermo);
		listCapteurs.add(capteurVolt);
		listCapteurs.add(capteurPression);
		listCapteurs.add(capteurConduct);

	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		if(threadUI == null){
			//initialization of the booleans for the thread's loops 
			UI = CPT = values = true;
			launched = false;

			//on va chercher l'objet Orphy sur le serveur RMI.
			final String string = JavaScoolServerRegister.getProtocol() + OrphyRegister.getServer();
			try {
				orphy = (IOrphy)Naming.lookup(string);
			}catch(Exception e){
				e.printStackTrace();
			}

			try {
				if(!orphy.CodeUsed()){
					port = orphy.findPort("UI");
					if(port.compareTo("") != 0){
						if(orphy.openPort(port,"UI") == -1)
							return;
						initPlugIn();
					}
					else
						System.out.println("Aucun Orphy n'est actuellement branch\u00e9.");
				}
				else
					initPlugIn();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			fenetre.setFocus();
		}
	}

	/**
	 * Initialise les threads
	 */
	public void initPlugIn(){
		//initialization of the pluged material
		try {
			initPlugs();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		//creation of the UI
		createWindow();

		//creation of the threads for the orphy's pluged material
		createPlugThreads();

		//creation of the ui's thread
		createUIThread();
	}

	/**
	 * Initialise les variables booléennes et detecte les ports utilisés
	 * 
	 * @throws RemoteException
	 */
	public void initPlugs() throws RemoteException{

		PROG = false;
		UI = EA10 = EA11 = values = true;

		if(orphy.isAnalogInputEnabled(10) == false){
			EA10 = false;
			valueEA10 = 0.0;
		}
		if(orphy.isAnalogInputEnabled(11) == false){
			EA11 = false;
			valueEA11 = 0.0;
		}

		if(!EA11 && !EA10)
			values = false;

	}

	/**
	 * Cré le thread gérant l'interface graphique d'orphy
	 */
	public void createUIThread(){
		//thread updating the UI
		threadUI = new Thread(new Runnable() {
			public void run() {
				while (UI) {
					try { Thread.sleep(50); } catch (Exception e) { }

					//use of the SWT thread to modify SWT components
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							if(!fenetre.isDisposed()){
								// get the type of the entry
								typeG = typeButtonG.getText();
								typeH = typeButtonH.getText();

								// display entry in function of their type
								ltemps.setText(df.format(chronoTime) + " sec"); //$NON-NLS-1$ //$NON-NLS-2$

								torphy6.setText(df.format(valueEA10) + findType(typeG).getUnite());
								torphy7.setText(df.format(valueEA11) + findType(typeH).getUnite());
								valeureA.layout();

							}
						}
					});
				}
			}
		});
		threadUI.start();
	}

	/**
	 * cré le thread gérant l'acquisition automatique des entrés activées
	 */
	public void createPlugThreads(){

		getValues = new Thread(new Runnable() {
			public void run() {
				while (values) {
					if(EA10 && !PROG){
						try{ Thread.sleep(50);}catch(Exception e){}
						try {
							valueEA10 = orphy.getAnalogInput(10, typeG);
						} catch (RemoteException e) {
							System.out.println("probl\u00e8me rencontr\u00e9 durant l'acquisition");
						}
					}

					if(EA11 && !PROG){
						try{ Thread.sleep(50);} catch(Exception e){}
						try {
							valueEA11 = orphy.getAnalogInput(11, typeH);
						} catch (RemoteException e) {
							System.out.println("probl\u00e8me rencontr\u00e9 durant l'acquisition");
						}
					}

					if(PROG){
						try {
							res10 = orphy.getProgramedInput( typeG, (int)nbAcqu, (int)(interTime*1000), (int)10);
							res11 = orphy.getProgramedInput( typeH, (int)nbAcqu, (int)(interTime*1000), (int)11);
						} catch (RemoteException e1) {
							System.out.println("probl\u00e8me rencontr\u00e9 durant l'acquisition programm\u00e9e");
						}
						final Runnable tableFill=new Runnable(){
							public void run() {
								for(int i = 0; i <res10.length ; i++){
									TableItem item = new TableItem (table, SWT.NONE);
									item.setText (0, (interTime*i) + Messages.getString("OrphyAction.27")); //$NON-NLS-1$
									recorderG.add ( interTime*i, res10[i]);
									recorderH.add ( interTime*i, res11[i]);

									item.setText (1, df.format(res10[i]) + findType(typeG).getUnite());

									item.setText (2, df.format(res11[i]) + findType(typeH).getUnite());

									fenetre.layout();
								}

							};
						};
						Display.getDefault().syncExec(tableFill);
					
					}
					try {
						orphy.resetSerialPort();
					} catch (RemoteException e) {
						e.printStackTrace();
					}

				}
			}
		});

		getValues.start();

	}

	/**
	 * Cré l'interface
	 */
	public void createWindow(){

		GridLayout gridLayout = new GridLayout();
		display = Display.getCurrent();
		fenetre = new Shell(display,SWT.TITLE | SWT.CLOSE);
		fenetre.setSize(425 , 560);
		fenetre.setLocation(200, 200);

		fenetre.setText(Messages.getString("OrphyAction.18")); //$NON-NLS-1$
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.numColumns = 2;
		fenetre.setLayout(gridLayout);

		Group manuAcqu = new Group(fenetre, SWT.NULL);
		manuAcqu.setText("Acquisition manuelle");
		manuAcqu.setLayout(new GridLayout());
		manuAcqu.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		//stop button
		stop = new Button(manuAcqu, SWT.PUSH);
		final GridData bgridData = new GridData(GridData.FILL_HORIZONTAL);
		stop.setLayoutData(bgridData);
		stop.setText(Messages.getString("OrphyAction.19")); //$NON-NLS-1$
		stop.setEnabled(false);
		Listener stoplistener = new Listener() {
			public void handleEvent(Event event) {

				CPT = false;
				threadCompteur = null;
				lecture.setEnabled(true);
				stop.setEnabled(false);
				tracerG.setEnabled(true);
				tracerH.setEnabled(true);
				reset.setEnabled(true);
				recordButtonG.setEnabled(true);
				recordButtonH.setEnabled(true);
				addRecord.setEnabled(false);
				autoAcqu.setEnabled(true);
			}
		};
		stop.addListener(SWT.Selection, stoplistener);

		//play button
		lecture = new Button(manuAcqu, SWT.PUSH);
		lecture.setLayoutData(bgridData);
		lecture.setText(Messages.getString("OrphyAction.23")); //$NON-NLS-1$

		Listener lecturelistener = new Listener() {
			public void handleEvent(Event event) {


				CPT = true;
				addRecord.setEnabled(true);
				recordButtonG.setEnabled(false);
				recordButtonH.setEnabled(false);
				tracerG.setEnabled(false);
				tracerH.setEnabled(false);
				lecture.setEnabled(false);
				stop.setEnabled(true);
				reset.setEnabled(false);
				autoAcqu.setEnabled(false);

				final TimerTask lec=new TimerTask(){
					public void run(){
						if(CPT)
							chronoTime += 0.001;
						else
							timer.cancel();
					}
				};


				timer = new Timer();
				timer.scheduleAtFixedRate(lec, 0, 1);

			}

		};
		lecture.addListener(SWT.Selection, lecturelistener);

		Font font = new Font(display,Messages.getString("OrphyAction.34"),16, SWT.BOLD); //$NON-NLS-1$

		//the time
		Group time = new Group(manuAcqu,SWT.NULL);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		time.setLayoutData(gridData);
		fillLayout = new FillLayout();
		time.setLayout(fillLayout);
		time.setText(Messages.getString("OrphyAction.35")); //$NON-NLS-1$
		ltemps = new Label(time, SWT.CENTER);
		ltemps.setText(Messages.getString("OrphyAction.36")); //$NON-NLS-1$
		ltemps.setFont(font);

		//button to record a value or start set acquisition
		addRecord = new Button(manuAcqu, SWT.PUSH);
		addRecord.setLayoutData(bgridData);
		addRecord.setText(Messages.getString("OrphyAction.37")); //$NON-NLS-1$
		addRecord.setEnabled(false);
		Listener recordlistener = new Listener() {
			public void handleEvent(Event event) {

				TableItem item = new TableItem (table, SWT.NONE);
				item.setText (0, df.format(chronoTime) + Messages.getString("OrphyAction.38")); //$NON-NLS-1$
				recorderG.add(chronoTime,valueEA10);
				recorderH.add(chronoTime,valueEA11);

				item.setText (1, df.format(valueEA10) + findType(typeG).getUnite());

				item.setText (2, df.format(valueEA11) + findType(typeH).getUnite());

				fenetre.layout();
			}
		};
		addRecord.addListener(SWT.Selection, recordlistener);

		//the values table
		table = new Table (fenetre, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.verticalSpan = 9;
		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = {Messages.getString("OrphyAction.20"),Messages.getString("OrphyAction.21"),Messages.getString("OrphyAction.22")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}

		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}	

		//Set acquisition 

		autoAcqu = new Group(fenetre, SWT.NULL);
		autoAcqu.setText("Acquisition automatique");
		autoAcqu.setLayout(new FillLayout());
		autoAcqu.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));


		launchAcqu = new Button(autoAcqu,SWT.PUSH);
		launchAcqu.setText("Lancer");
		launchAcqu.setEnabled(false);

		Listener launchAcquListener=new Listener(){
			public void handleEvent(Event event){

				final Runnable ennable = new Runnable(){
					public void run() {
						lecture.setEnabled(true);
						launchAcqu.setEnabled(false);
					}
				};

				final Runnable print=new Runnable(){
					public void run() {
						TableItem item = new TableItem (table, SWT.NONE);
						item.setText (0, df.format(chronoTime) + Messages.getString("OrphyAction.27")); //$NON-NLS-1$
						recorderG.add ( chronoTime, valueEA10);
						recorderH.add ( chronoTime, valueEA11);

						item.setText (1, df.format(valueEA10) + findType(typeG).getUnite());

						item.setText (2, df.format(valueEA11) + findType(typeH).getUnite());

						fenetre.layout();
					}
				};
				if(!launched){
					CPT = true;
					lecture.setEnabled(false);
					aquiParam.setEnabled(false);
					interTimeDelay = 0.000;

					if(acquType2){
						interTime = stopTime / nbAcqu;
					}
					if(interTime < 0.01){
						// makes the programmation enabled in the thread getvalues
						PROG = true;

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						PROG = false;
						launchAcqu.setEnabled(false);

					}

					else if(acquType1){
						final TimerTask acquType1=new TimerTask(){
							public void run(){
								if(df.format(stopTime).compareTo(df.format(chronoTime)) != 0 && CPT ){
									chronoTime += 0.001;
									interTimeDelay += 0.001;
									if(df.format(interTimeDelay).compareTo(df.format(interTime)) == 0){
										Display.getDefault().syncExec(print);
										interTimeDelay = 0;
									}
								}
								else{
									timer.cancel();
									Display.getDefault().syncExec(ennable);
								}
							}
						};
						timer = new Timer();
						timer.scheduleAtFixedRate(acquType1, 0, 1);
					}

					else if(acquType2){
						final TimerTask acquType2=new TimerTask(){
							public void run(){
								if(df.format(stopTime).compareTo(df.format(chronoTime)) != 0 && CPT ){
									chronoTime += 0.001;
									interTimeDelay += 0.001;
									if(df.format(interTimeDelay).compareTo(df.format(interTime)) == 0){
										if(!fenetre.isDisposed())
											Display.getDefault().syncExec(print);
										interTimeDelay = 0;
									}
								}
								else{
									timer.cancel();
									if(!Display.getDefault().isDisposed())
										Display.getDefault().syncExec(ennable);
								}
							}
						};
						timer = new Timer();
						timer.scheduleAtFixedRate(acquType2, 0, 1);
					}
					else if(acquType3){
						final TimerTask acquType3=new TimerTask(){
							public void run(){
								if(nbAcqu != 0 && CPT ){
									chronoTime += 0.001;
									interTimeDelay += 0.001;
									if(df.format(interTimeDelay).compareTo(df.format(interTime)) == 0){

										if(!Display.getDefault().isDisposed())
											Display.getDefault().syncExec(print);
										nbAcqu--;
										interTimeDelay = 0;
									}
								}
								else{
									timer.cancel();
									if(!Display.getDefault().isDisposed())
										Display.getDefault().syncExec(ennable);
								}
							}
						};
						timer = new Timer();
						timer.scheduleAtFixedRate(acquType3, 0, 1);
					}
					else if(acquType4){
						final TimerTask acquType3=new TimerTask(){
							public void run(){
								if(CPT){
									chronoTime += 0.001;
									interTimeDelay += 0.001;
									if(df.format(interTimeDelay).compareTo(df.format(interTime)) == 0){
										if(!Display.getDefault().isDisposed())
											Display.getDefault().syncExec(print);
										interTimeDelay = 0;
									}
								}
								else{
									timer.cancel();
									if(!Display.getDefault().isDisposed())
										Display.getDefault().syncExec(ennable);
								}
							}
						};
						timer = new Timer();
						timer.scheduleAtFixedRate(acquType3, 0, 1);
					}
					launchAcqu.setText("Arr\u00eater");
					launched = true;
				}
				else{
					CPT = false;
					lecture.setEnabled(true);
					launchAcqu.setText("Lancer");
					launchAcqu.setEnabled(false);
					aquiParam.setEnabled(false);
					reset.setEnabled(true);
					launched = false;

				}
			}
		};
		launchAcqu.addListener(SWT.Selection,launchAcquListener);


		aquiParam=new Button(autoAcqu, SWT.PUSH);
		aquiParam.setText("Param\u00e9trer"); //$NON-NLS-1$
		aquiParam.setEnabled(true);
		Listener aquiParamListener=new Listener(){
			public void handleEvent(Event event){

				choiceType = false;
				createAskTypeWindow();

			}
		};
		aquiParam.addListener(SWT.Selection,aquiParamListener);


		//the orphy's analogics values
		valeureA = new Group(fenetre,SWT.NULL);
		valeureA.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_BEGINNING));
		gridLayout= new GridLayout();
		valeureA.setLayout(gridLayout);
		valeureA.setText(Messages.getString("OrphyAction.55")); //$NON-NLS-1$
		gridLayout.numColumns = 2;

		//entry G
		Label lentre = new Label(valeureA,SWT.NULL);
		lentre.setText(Messages.getString("OrphyAction.56")); //$NON-NLS-1$
		lentre.setFont(font);
		torphy6 = new Text(valeureA, SWT.BORDER);
		torphy6.setText(Messages.getString("OrphyAction.57")); //$NON-NLS-1$
		torphy6.setFont(font);
		Label typeG = new Label(valeureA,SWT.NULL);
		typeG.setText(Messages.getString("OrphyAction.58")); //$NON-NLS-1$
		typeButtonG = new Combo(valeureA,SWT.NULL);
		String[] capteursCombo = new String[listCapteurs.size()];
		for(int i = 0; i < listCapteurs.size() ; i++){
			capteursCombo[i] = listCapteurs.get(i).getType();
		}
		typeButtonG.setItems(capteursCombo);
		typeButtonG.select(0);

		//entry H
		lentre = new Label(valeureA,SWT.NULL);
		lentre.setText(Messages.getString("OrphyAction.61")); //$NON-NLS-1$
		lentre.setFont(font);
		torphy7 = new Text(valeureA, SWT.BORDER);
		torphy7.setText(Messages.getString("OrphyAction.62")); //$NON-NLS-1$
		torphy7.setFont(font);
		Label typeH = new Label(valeureA,SWT.NULL);
		typeH.setText(Messages.getString("OrphyAction.63")); //$NON-NLS-1$
		typeButtonH = new Combo(valeureA,SWT.NULL);
		typeButtonH.setItems(capteursCombo);
		typeButtonH.select(0);


		//reset button
		reset = new Button(fenetre, SWT.PUSH);
		reset.setLayoutData(bgridData);
		reset.setText(Messages.getString("OrphyAction.66")); //$NON-NLS-1$
		Listener addplistener = new Listener() {
			public void handleEvent(Event event) {
				recorderG.reset();
				recorderH.reset();

				lecture.setEnabled(true);

				closePlugThreads();

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.out.println(Messages.getString("OrphyAction.67")); //$NON-NLS-1$
				}

				try {
					orphy.close("UI");
					orphy.openPort(orphy.findPort("UI"),"UI");
					initPlugs();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				createPlugThreads();				
				table.removeAll();
				table.clearAll();
				table.redraw();

				if(timer != null)
					timer.cancel();

				chronoTime = 0.000;
				interTime = 0.000;
				nbAcqu = 0;
				stopTime = 0.000;

				aquiParam.setEnabled(true);
				launchAcqu.setText("Lancer");
				launched = false;

				typeButtonG.select(0);
				typeButtonH.select(0);
			}
		};

		reset.addListener(SWT.Selection, addplistener);

		//button to draw recorded points on a graph
		Group graph = new Group(fenetre, SWT.NULL);
		graph.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		graph.setLayout(new RowLayout());
		Label labelGraph = new Label(graph, SWT.NONE);
		labelGraph.setText(Messages.getString("OrphyAction.68")); //$NON-NLS-1$
		tracerG = new Button(graph, SWT.PUSH);
		tracerG.setText(Messages.getString("OrphyAction.69")); //$NON-NLS-1$
		Listener traceGlistener = new Listener() {
			public void handleEvent(Event event) {
				//drawing of the points on a graph
				tracerGraphe(recorderG);
			}
		};
		tracerG.addListener(SWT.Selection, traceGlistener);

		tracerH = new Button(graph, SWT.PUSH);
		tracerH.setText(Messages.getString("OrphyAction.70")); //$NON-NLS-1$
		Listener traceHlistener = new Listener() {
			public void handleEvent(Event event) {
				//drawing of the points on a graph
				tracerGraphe(recorderH);
			}
		};
		tracerH.addListener(SWT.Selection, traceHlistener);

		//button to export recorded points in a file
		Group save = new Group(fenetre, SWT.NULL);
		save.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		save.setLayout(new RowLayout());
		Label labelSave = new Label(save, SWT.NONE);
		labelSave.setText(Messages.getString("OrphyAction.71")); //$NON-NLS-1$
		recordButtonG = new Button(save, SWT.PUSH);
		recordButtonG.setText(Messages.getString("OrphyAction.72")); //$NON-NLS-1$
		Listener recordButtonGListener = new Listener() {
			public void handleEvent(Event event) {
				String newdest = new FileDialog(fenetre).open();
				if(newdest != null)
					recorderG.setDestination(newdest);
				recorderG.save(typeButtonG.getText());
			}
		};
		recordButtonG.addListener(SWT.Selection, recordButtonGListener);
		recordButtonH = new Button(save, SWT.PUSH);
		recordButtonH.setText(Messages.getString("OrphyAction.73")); //$NON-NLS-1$
		Listener recordButtonHListener = new Listener() {
			public void handleEvent(Event event) {
				String newdest = new FileDialog(fenetre).open();
				if(newdest != null)
					recorderH.setDestination(newdest);
				recorderH.save(typeButtonH.getText());
			}
		};
		recordButtonH.addListener(SWT.Selection, recordButtonHListener);

		//close button
		Button close = new Button(fenetre, SWT.PUSH);
		close.setText(Messages.getString("OrphyAction.74")); //$NON-NLS-1$
		GridData closeData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		closeData.horizontalSpan = 2;
		close.setLayoutData(closeData);
		Listener closelistener = new Listener() {
			public void handleEvent(Event event) {
				fenetre.close();
				fenetre.dispose();
			}
		};
		close.addListener(SWT.Selection, closelistener);

		fenetre.addListener (SWT.Close, new Listener () {
			public void handleEvent (Event event) {
				cleanClose();
				chronoTime = 0.0;
			}
		});
		fenetre.open();
	}

	/**
	 * Cré la fenêtre pour les paramètres de l'acquisition paramétrée
	 */
	public void createAskTypeWindow(){
		GridLayout aquisitionParamLayout=new GridLayout();
		aquisitionParamLayout.numColumns = 2;

		askTypeShell=new Shell(Display.getCurrent(),SWT.ON_TOP | SWT.TITLE | SWT.CLOSE);
		askTypeShell.setSize(370,185);
		askTypeShell.setText(Messages.getString("OrphyAction.46")); //$NON-NLS-1$
		askTypeShell.setLocation(250,250);
		askTypeShell.setLayout(aquisitionParamLayout);		

		Label questionType = new Label(askTypeShell, SWT.NULL);
		questionType.setText("Vous voulez faire une acquisition de donn\u00e9es avec comme param\u00e8tres : \n");
		GridData questionTypeData = new GridData();
		questionTypeData.horizontalSpan = 2;
		questionType.setLayoutData(questionTypeData);

		checkBoxType1 = new Button(askTypeShell, SWT.CHECK);
		checkBoxType1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//Determines if the checkBox is checked or not
				boolean selected = checkBoxType1.getSelection();
				// If checked then execute this code
				if (selected == true) {
					checkBoxType2.setSelection(false);
					checkBoxType3.setSelection(false);
					checkBoxType4.setSelection(false);
					acquType1 = true;
					acquType2 = false;
					acquType3 = false;
					acquType4 = false;
					if(valider != null)
						valider.setEnabled(false);
				}
				else{
					System.out.println("on met a faux");
					acquType1 = false;
				}
			}
		});

		Label type1param = new Label(askTypeShell, SWT.NULL);
		type1param.setText(" une dur\u00e9e et un intervalle de temps entre chaque acquisition.");


		checkBoxType2 = new Button(askTypeShell, SWT.CHECK);
		checkBoxType2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//Determines if the checkBox is checked or not
				boolean selected = checkBoxType2.getSelection();
				// If checked then execute this code
				if (selected == true) {
					checkBoxType1.setSelection(false);
					checkBoxType3.setSelection(false);
					checkBoxType4.setSelection(false);

					acquType1 = false;
					acquType2 = true;
					acquType3 = false;
					acquType4 = false;
					if(valider != null)
						valider.setEnabled(false);
				}
				else
					acquType2 = false;
			}
		});

		Label type2param = new Label(askTypeShell, SWT.NULL);
		type2param.setText(" une dur\u00e9e et un nombre d'acquisitions.");

		checkBoxType3 = new Button(askTypeShell, SWT.CHECK);
		checkBoxType3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//Determines if the checkBox is checked or not
				boolean selected = checkBoxType3.getSelection();
				// If checked then execute this code
				if (selected == true) {
					checkBoxType1.setSelection(false);
					checkBoxType2.setSelection(false);
					checkBoxType4.setSelection(false);

					acquType1 = false;
					acquType2 = false;
					acquType3 = true;
					acquType4 = false;
					if(valider != null)
						valider.setEnabled(false);
				}
				else
					acquType3 = false;
			}
		});
		Label type3param = new Label(askTypeShell, SWT.NULL);
		type3param.setText(" un intervalle de temps et un nombre d'acquisitions.");

		checkBoxType4 = new Button(askTypeShell, SWT.CHECK);
		checkBoxType4.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//Determines if the checkBox is checked or not
				boolean selected = checkBoxType4.getSelection();
				// If checked then execute this code
				if (selected == true) {
					checkBoxType1.setSelection(false);
					checkBoxType2.setSelection(false);
					checkBoxType3.setSelection(false);

					acquType1 = false;
					acquType2 = false;
					acquType3 = false;
					acquType4 = true;
					if(valider != null)
						valider.setEnabled(false);
				}
				else
					acquType4 = false;
			}
		});

		Label type4param = new Label(askTypeShell, SWT.NULL);
		type4param.setText(" un intervalle de temps pour une dur\u00e9e indetermin9e.");

		validerType = new Button(askTypeShell, SWT.PUSH);
		validerType.setText("valider");
		GridData valideTypeData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		valideTypeData.horizontalSpan = 2;
		validerType.setLayoutData(valideTypeData);
		Listener validerListener = new Listener() {
			public void handleEvent(Event event) {
				if(choiceType == true){
					parameters.dispose();
				}

				final String[] listUnits = {"min", "s", "ms"};
				parameters = new Group(askTypeShell, SWT.NULL);				
				askTypeShell.setSize(370, 300);


				GridLayout paramLayout = new GridLayout();
				paramLayout.numColumns = 3;
				parameters.setLayout(paramLayout);
				GridData paramData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
				paramData.horizontalSpan = 2;
				parameters.setLayoutData(paramData);

				if(acquType1 || acquType2){

					Label ltemps = new Label(parameters, SWT.CENTER);
					ltemps.setText(Messages.getString("OrphyAction.47")); //$NON-NLS-1$
					duree=new Spinner(parameters,SWT.NONE);
					duree.setMinimum(0);
					duree.setMaximum(1000);
					duree.setSelection(0);
					duree.setIncrement(1);
					duree.setPageIncrement(100);

					duree.setBounds(new org.eclipse.swt.graphics.Rectangle(45,45,61,16));
					duree.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));

					uniteDuree = new Combo(parameters, SWT.NONE);
					uniteDuree.setItems(listUnits);

				}

				if(!acquType2){
					Label lintervalle = new Label(parameters, SWT.CENTER);
					lintervalle.setText(Messages.getString("OrphyAction.49")); //$NON-NLS-1$
					intervalle=new Spinner(parameters,SWT.NONE);
					intervalle.setMinimum(0);
					intervalle.setMaximum(1000);
					intervalle.setSelection(0);
					intervalle.setIncrement(1);
					intervalle.setPageIncrement(100);

					intervalle.setBounds(new org.eclipse.swt.graphics.Rectangle(45,45,61,16));
					intervalle.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));

					uniteInter = new Combo(parameters, SWT.NONE);
					uniteInter.setItems(listUnits);
				}

				if(acquType2 || acquType3){
					Label nbAquisitions=new Label(parameters,SWT.CENTER);
					nbAquisitions.setText(Messages.getString("OrphyAction.51")); //$NON-NLS-1$
					nombreAquisitions=new Spinner(parameters,SWT.NONE);
					nombreAquisitions.setMinimum(0);
					nombreAquisitions.setMaximum(2000);
					nombreAquisitions.setSelection(0);
					nombreAquisitions.setIncrement(1);
					nombreAquisitions.setPageIncrement(100);

					nombreAquisitions.setBounds(new org.eclipse.swt.graphics.Rectangle(45,45,61,16));
					nombreAquisitions.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));

				}
				valider=new Button(parameters, SWT.PUSH);
				valider.setText(Messages.getString("OrphyAction.54")); //$NON-NLS-1$
				GridData valideData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
				valideData.horizontalSpan = 3;
				valider.setLayoutData(valideData);
				Listener validerListener = new Listener() {
					public void handleEvent(Event event) {
						boolean stop = false;

						if(acquType1 || acquType2){
							if(uniteDuree.getSelectionIndex() == 0)
								stopTime = duree.getSelection() * 60;
							else
								if(uniteDuree.getSelectionIndex() == 1)
									stopTime = duree.getSelection();
								else
									if(uniteDuree.getSelectionIndex() == 2)
										stopTime = duree.getSelection() / 1000.0;
									else
										stop = true;
						}
						if(!acquType2){
							if(uniteInter.getSelectionIndex() == 0)
								interTime = intervalle.getSelection() * 60;
							else
								if(uniteInter.getSelectionIndex() == 1)
									interTime = intervalle.getSelection();
								else
									if(uniteInter.getSelectionIndex() == 2)
										interTime = intervalle.getSelection() / 1000.0;
									else
										stop = true;
						}
						if(acquType2 || acquType3){
							nbAcqu = nombreAquisitions.getSelection();
						}
						if(stop == true){

							erreur.setText("Selectionnez une unite.");
							stop = false;
							askTypeShell.layout();
						}
						else
							askTypeShell.close();
						launchAcqu.setEnabled(true);
					}
				};
				valider.addListener(SWT.Selection,validerListener);
				valider.setEnabled(true);
				validerType.setText("modifier le choix");
				choiceType = true;

				erreur = new Label(parameters,SWT.NULL);
				erreur.setText("                                      ");
				gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
				gridData.horizontalSpan = 3;
				erreur.setLayoutData(gridData);

				askTypeShell.layout();
			}

		};
		validerType.addListener(SWT.Selection,validerListener);

		askTypeShell.open();
	}

	/**
	 * Ferme de facon propre tout ce qui doit l'etre
	 */
	public static void cleanClose(){
		closePlugThreads();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadUI.interrupt();
		UI = false;
		threadUI = null;
		try { 
			orphy.close("UI");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if(timer != null){
			timer.cancel();

		}

	}

	/**
	 * Ferme les threads de la classe
	 */
	public static void closePlugThreads(){
		if(threadCompteur != null){
			threadCompteur.interrupt();
			CPT = false;
			threadCompteur = null;
		}
		if(getValues != null){
			getValues.interrupt();
			values = false;
			getValues = null;
		}
	}

	/**
	 * Trace l'apercu graphique d'un ensemble de relevé
	 * 
	 * @param recorder l'ensemble de relevés à tracer
	 */
	public void tracerGraphe(final Record recorder){
		display = Display.getCurrent();
		graphe = new Shell(display);
		graphe.setSize(600, 600);
		graphe.setLocation(100, 100);

		graphe.setText(Messages.getString("OrphyAction.75")); //$NON-NLS-1$
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		graphe.setLayout(gridLayout);	

		//canvas to draw the graph
		final Canvas canvas = new Canvas(graphe,SWT.BORDER);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		canvas.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		canvas.setDragDetect(true);

		//display the mouse details
		canvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent me) {
				Rectangle clientArea = canvas.getClientArea();
				lcoords.setText(Messages.getString("OrphyAction.76") + ((me.x - 10 ) / 10.0 + Messages.getString("OrphyAction.77") + ((clientArea.height / 2 )- me.y) / 5.0)); //$NON-NLS-1$ //$NON-NLS-2$
				graphe.layout();
			}
		});
		canvas.addPaintListener(new PaintListener(){
			public void paintControl(final PaintEvent e){

				//drawing of the axes
				Rectangle clientArea = canvas.getClientArea();
				e.gc.drawLine(10, 0, 10, clientArea.height);
				e.gc.drawLine(0, clientArea.height / 2 , clientArea.width, clientArea.height / 2);
				for(int k = clientArea.height / 2; k > 0 ; k = k - 10){
					e.gc.drawLine( 5 , k, 15 , k);
					e.gc.drawLine( 5 , clientArea.height / 2 + (clientArea.height / 2 - k), 15, clientArea.height / 2 + (clientArea.height / 2 - k));

				}
				for(int k = 10; k<clientArea.width ; k = k + 10){
					e.gc.drawLine(k, clientArea.height / 2 - 5, k, clientArea.height / 2 + 5);

				}

				//drawing of the points
				int firstPointCoord, secondPointCoord,firstPointAbs,secondPointAbs;

				for(int k = 0; k < recorder.getSizeTemps()-1 ; k++){
					firstPointCoord = (int) (recorder.getValue(k) * 10) / 2;
					secondPointCoord = (int) (recorder.getValue(k+1) * 10) / 2;

					firstPointAbs = (int)(recorder.getTemps(k) * 10);
					secondPointAbs = (int)(recorder.getTemps(k+1) * 10);
					e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
					e.gc.drawLine(firstPointAbs + 10, clientArea.height / 2 - firstPointCoord, secondPointAbs + 10 ,clientArea.height / 2 - secondPointCoord);

					e.gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
					e.gc.drawOval( firstPointAbs + 10 - 2,  clientArea.height / 2 - firstPointCoord - 2, 4, 4);
					if(k == recorder.getSizeTemps() - 2){
						e.gc.drawOval( secondPointAbs + 10 - 2,  clientArea.height / 2 - secondPointCoord - 2, 4, 4);
					}
				}

			}
		});

		//the details of the mouse on the graph
		lcoords = new Label(graphe, SWT.BORDER);
		lcoords.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		lcoords.setText(Messages.getString("OrphyAction.78")); //$NON-NLS-1$

		graphe.open();
	}

	/**
	 * Renvoi le capteur ayant le type donné en paramètres
	 * 
	 * @param type le type du capteur cherché
	 * @return le Capteur ayant ce type
	 */
	public Capteur findType(String type){
		for(int i=0 ; i<listCapteurs.size() ; i++){
			if(listCapteurs.get(i).getType().compareTo(type) == 0)
				return listCapteurs.get(i);
		}
		return null;
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {

	}

	@Override
	public void init(IWorkbenchWindow window) {

	}

}
