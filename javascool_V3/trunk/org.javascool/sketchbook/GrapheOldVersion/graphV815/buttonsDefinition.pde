toolbar construcToolbar, displayToolbar, pathToolbar, playToolbar;
multistatePolyButton[] modeChoices, constructModeBtn;
// slider gOrderDef, gConnectionDef;

int constructBtn, pathBtn, coloringBtn, probBtn, emptyBtn, randomBtn, resetBtn, deleteBtn, deleteStrokeBtn, orientedBtn, labelBtn, labelStrokeBtn, makeLoopBtn, makeRegularBtn, makeCompleteBtn, addPathBtn, randomPathBtn, deletePathBtn, savePathBtn, resetPathBtn, debutSimulationBtn, playBtn, endBtn, stepByStepBtn, resetSimulationBtn;
boolean emptyRequested, randomRequested, resetRequested, deleteRequested, deleteStrokeRequested, makeLoopRequested, makeRegularRequested, makeCompleteRequested, addPathRequested, randomPathRequested, deletePathRequested, savePathRequested, resetPathRequested, debutSimulationRequested, playRequested, endRequested, stepByStepRequested, resetSimulationRequested, constructMode, pathMode, coloringMode, probMode;
int showState = 2;
int playState = 2;
int hideState = 1;
int stopState = 1;
int[] U1, U2, U3, U4;
String[] modeChoicesText = {
  "Création du graphe", "Chemin", "Coloration", "Graphe probabiliste"
};
