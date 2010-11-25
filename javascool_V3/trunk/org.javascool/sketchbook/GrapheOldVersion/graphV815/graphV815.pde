/*---------------------------------------------------
 *  Graph v0.815. Juin 2010. David Marec : paramètres des chemins aléatoires.
 *  ------------------------------------------------------*/

graph g1;
ArrayList ponderation, selectedList, strokePath; // liste des ponderation (textfield), liste des sommets d'un chemin, liste des arêtes d'un chemin, liste des cheins sauvegardés.
int totalNbConnection = 7;
int initOrder = 5;
int topMargin, bottomMargin, leftMargin, rightMargin, spacing, btnSize, ecartMenu, nbEtapesProba, nbEtapesEnCours;
boolean cheminAleaDefinition, isProbaPlaying, removeMode, orderModif, connectionModif, pondModif, pathModif, initialVertexModif, nbEtapesProbaModif, drawBox, fixBox, deleteBox, draggBox, makeRegular, multiSelect, choice1Down, choice2Down, choice3Down, choice1GoUp, choice1GoDown, choice2GoUp, choice2GoDown, choice3GoUp, choice3GoDown, chooseRandPathVertex, cheminDragged; // test for select box, change display mode, multiselection by click
int[][] savedSelectedList;
float[] startBoxPt, endBoxPt, xCheminText; // for select nodes in a box by dragging; abscisse pour chaque liste de chemin
PVector[] vTranslate; // list of vector for set of vertices translation
String selectedListText; // liste des sommets du chemin considéré
String[] infoGraphAnswer, infoStrokeAnswer, infoCheminAnswer, savedPathAnswer0, savedPathAnswer1, savedPathAnswer2;

PVector draggBoxTranslate;
float xChoice, yChoice0, yChoice1, yChoice2, yChoice3, deltaT, deltaT2, deltaT3, c, draggBoxWidth, draggBoxHeight, pathLength, longueurTexteChemin, ajout; // make nodes move : incrementation, celerity
PFont modeFontBtn, orderFont;
myFonts btnFont; // label, stroke ponderation and buttons' font
textfield gOrderDef, gConnectionDef, initialVertexProba, nbEtapesProbaText;
textfield[] cheminAleaParametre;
matrix gProbaTransition, initialMatrixProba, gTransitionPlay;

/*--------------------------------------------------------------------------------------
 */
void setup() {
  size(1000, 800);
  background(bGround);
  frameRate(30);
  smooth();

  // fixed value ------------------------------------------------------

  gWindowWidth = 750;
  gWindowHeight = 750;
  spacing = 35;
  btnSize = 20;
  topMargin = 90;
  bottomMargin = 30;
  leftMargin = 0;
  rightMargin = 250;
  ecartMenu = 200;
  nbEtapesProba = 20;
  nbEtapesEnCours = 0;
  orderFont = loadFont("LucidaBright-20.vlw");
  modeFontBtn = loadFont("CourierNewPSMT-16.vlw");
  btnFont = new myFonts();
  btnFont.createFonts("CourierNewPSMT", 9, 12, 18, 24);
  removeMode = false;
  pondModif = pathModif = initialVertexModif = nbEtapesProbaModif = false;
  drawBox = false;
  fixBox = false;
  deleteBox = false;
  makeRegular = false;
  multiSelect = false;
  isProbaPlaying = false;
  chooseRandPathVertex = false;
  choice1Down = choice2Down = choice3Down = true;
  choice1GoUp = choice1GoDown = choice2GoUp = choice2GoDown = choice3GoUp = choice3GoDown = false;
  cheminDragged = false; // a-t-on fait glisser les chemeins sauvegardés ?
  startBoxPt = new float[2];
  endBoxPt = new float[2];
  xCheminText = new float[1];
  xCheminText[0] = 20.;
  ajout = 0;

  // probleme avec append et String[][] donc :
  savedPathAnswer0 = new String[0]; // sauvegarde des informations des différents chemins - ici, les sommets
  savedPathAnswer1 = new String[0]; // ici le nombre d'aretes
  savedPathAnswer2 = new String[0]; // ici la longueur
  deltaT = 0.;
  deltaT2 = deltaT3 = 0.;
  c = 0.05;
  xChoice = width - rightMargin / 2 - 110;
  yChoice0 = 35 + 11 * btnSize - round(btnSize / 2);
  yChoice1 = 100 + 11 * btnSize - round(btnSize / 2) + 15 * graphTextInfo.length + ecartMenu; // position du bouton recherche de chemin.
  yChoice2 = 100 + 11 * btnSize - round(btnSize / 2) + 15 * graphTextInfo.length + ecartMenu + 1.5 * btnSize; // position du bouton recherche de chemin.
  yChoice3 = 100 + 11 * btnSize - round(btnSize / 2) + 15 * graphTextInfo.length + ecartMenu + 3 * btnSize; // position du bouton recherche de chemin.
  pathLength = 0.;
  btnFont.setFont("medium");
  for(int p = 0; p < strokeTextInfo.length; p++)
    maxWidthInfoStrokeText = max(maxWidthInfoStrokeText, textWidth(strokeTextInfo[p]));
  for(int p = 0; p < graphTextInfo.length; p++)
    maxWidthInfoText = max(maxWidthInfoText, textWidth(graphTextInfo[p]));
  for(int p = 0; p < cheminTextInfo.length; p++)
    maxWidthCheminInfo = max(maxWidthCheminInfo, textWidth(cheminTextInfo[p]));
  for(int p = 0; p < cheminAleaInfo.length; p++)
    maxWidthAleaInfo = max(maxWidthAleaInfo, textWidth(cheminAleaInfo[p]));
  selectedListText = "";
  infoGraphAnswer = new String[graphTextInfo.length];
  infoStrokeAnswer = new String[strokeTextInfo.length];
  infoCheminAnswer = new String[cheminTextInfo.length];

  // buttons shape, build toolbar and working mode---------------------------------
  poly squarePoly = new poly(4);
  squarePoly.assign3(0, 0, 0, 1, 1, 0, 2, 1, 1);
  squarePoly.assign(3, 0, 1);

  modeChoices = new multistatePolyButton[4];
  modeChoices[0] = new multistatePolyButton(1, round(xChoice), 5, btnSize, btnSize);
  modeChoices[0].assign(1, modeChoicesText[0], "", squarePoly, orangeColor);
  modeChoices[0].colors[0] = redColor;
  modeChoices[1] = new multistatePolyButton(1, round(xChoice), round(yChoice1), btnSize, btnSize);
  modeChoices[1].assign(1, modeChoicesText[1], "", squarePoly, Green);
  modeChoices[1].colors[0] = kaki;
  modeChoices[2] = new multistatePolyButton(1, round(xChoice), round(yChoice2), btnSize, btnSize);
  modeChoices[2].assign(1, modeChoicesText[2], "", squarePoly, Green);
  modeChoices[2].colors[0] = Blue;
  modeChoices[3] = new multistatePolyButton(1, round(xChoice), round(yChoice3), btnSize, btnSize);
  modeChoices[3].assign(1, modeChoicesText[3], "", squarePoly, Green);
  modeChoices[3].colors[0] = color(75, 60, 55);
  for(int p = 0; p < modeChoices.length; p++) {
    modeChoices[p].showName = true;
    modeChoices[p].namePos = "right";
  }
  // polygon for construct mode button-------------------------------------------------------
  poly nonOrientedModePoly = new poly(94);
  nonOrientedModePoly.assign(0, 0.820665, 0.160876);
  float[] pt0 = {
    0.820665, 0.160876
  };
  float[] center0 = {
    0.9, 0.1
  };
  for(int p = 0; p < 46; p++)
    nonOrientedModePoly.assign(p + 1, rotation(center0, (p + 1) * PI / 24, pt0)[0], rotation(center0, (p + 1) * PI / 24, pt0)[1]);
  nonOrientedModePoly.assign(47, 0.179335, 0.839124);
  float[] pt02 = {
    0.179335, 0.839124
  };
  float[] center02 = {
    0.1, 0.9
  };
  for(int p = 0; p < 46; p++)
    nonOrientedModePoly.assign(48 + p, rotation(center02, (p + 1) * PI / 24, pt02)[0], rotation(center02, (p + 1) * PI / 24, pt02)[1]);
  poly orientedModePoly = new poly(99);
  float[] ptC = {
    0.8292893, 0.17071068
  };
  for(int p = 0; p < 48; p++)
    orientedModePoly.assign(p, rotation(center0, p * PI / 24, ptC)[0], rotation(center0, p * PI / 24, ptC)[1]);
  orientedModePoly.assign3(48, 0.8292893, 0.17071068 + 0.2 * sqrt(2), 49, 0.69770247, 0.32075667, 50, 0.17933534, 0.83912385);
  for(int p = 0; p < 46; p++)
    orientedModePoly.assign(51 + p, rotation(center02, (p + 1) * PI / 24, pt02)[0], rotation(center02, (p + 1) * PI / 24, pt02)[1]);
  orientedModePoly.assign2(97, 0.67924327, 0.3022975, 98, 0.8292893 - 0.2 * sqrt(2), 0.17071068);

  poly loopPoly = new poly(43);
  loopPoly.assign3(0, 0.5, 1, 1, 0.37059, 0.982963, 2, 0.25, 0.933013);
  loopPoly.assign3(3, 0.146447, 0.853553, 4, 0.0669873, 0.75, 5, 0.0170371, 0.62941);
  loopPoly.assign3(6, 0., 0.5, 7, 0.0170371, 0.37059, 8, 0.0669873, 0.25);
  loopPoly.assign3(9, 0.146447, 0.146447, 10, 0.25, 0.0669873, 11, 0.37059, 0.0170371);
  loopPoly.assign3(12, 0.5, 0., 13, 0.62941, 0.0170371, 14, 0.75, 0.0669873);
  loopPoly.assign3(15, 0.853553, 0.146447, 16, 0.933013, 0.25, 17, 0.982963, 0.37059);
  loopPoly.assign3(18, 1, 0.5, 19, 0.982963, 0.62941, 20, 1.07956, 0.655291);
  loopPoly.assign(21, 0.853553, 0.853553);
  loopPoly.assign3(22, 0.789778, 0.577646, 23, 0.88637, 0.603528, 24, 0.9, 0.5);
  loopPoly.assign3(27, 0.782843, 0.217157, 26, 0.84641, 0.3, 25, 0.88637, 0.396472);
  loopPoly.assign3(30, 0.5, 0.1, 29, 0.603528, 0.11363, 28, 0.7, 0.15359);
  loopPoly.assign3(33, 0.217157, 0.217157, 32, 0.3, 0.15359, 31, 0.396472, 0.11363);
  loopPoly.assign3(36, 0.1, 0.5, 35, 0.11363, 0.396472, 34, 0.15359, 0.3);
  loopPoly.assign3(39, 0.217157, 0.782843, 38, 0.15359, 0.7, 37, 0.11363, 0.603528);
  loopPoly.assign3(42, 0.5, 0.9, 41, 0.396472, 0.88637, 40, 0.3, 0.84641);

  constructModeBtn = new multistatePolyButton[2];
  constructModeBtn[0] = new multistatePolyButton(2, int (width - rightMargin / 2 - 110), 10 + 2 *btnSize, floor(1.5 * btnSize), floor(1.5 * btnSize));
  constructModeBtn[0].assign(2, "Graphe", "non orienté", nonOrientedModePoly, Blue);
  constructModeBtn[0].assign(1, "Graphe", "orienté", orientedModePoly, Blue);
  constructModeBtn[0].current = 2;
  constructModeBtn[1] = new multistatePolyButton(2, int (width - rightMargin / 2 - 110), 60 + 2 *btnSize, floor(1.5 * btnSize), floor(1.5 * btnSize));
  constructModeBtn[1].assign(2, "Graphe", "simple", loopPoly, Blue);
  constructModeBtn[1].assign(1, "Graphe", "non-simple", loopPoly, Blue);
  constructModeBtn[1].current = 2;
  for(int p = 0; p < constructModeBtn.length; p++) {
    constructModeBtn[p].showName = true;
    constructModeBtn[p].namePos = "right";
  }
  // polygon for toolbar------------------------------------------------------------------------
  poly resetPoly = new poly(12);
  resetPoly.assign3(0, .35, 0, 1, .65, 0, 2, .65, .35);
  resetPoly.assign3(3, 1, .35, 4, 1, .65, 5, .65, .65);
  resetPoly.assign3(6, .65, 1, 7, .35, 1, 8, .35, .65);
  resetPoly.assign3(9, 0, .65, 10, 0, .35, 11, .35, .35);
  resetPoly.rotate(-45);

  poly aleaPoly = new poly(26);
  aleaPoly.assign3(0, 0, 0.1, 1, 0.25, 0.1, 2, 0.375, 0.3);
  aleaPoly.assign3(3, 0.5, 0.1, 4, 0.75, 0.1, 5, 0.75, 0);
  aleaPoly.assign3(6, 1, 0.2, 7, 0.75, 0.4, 8, 0.75, 0.3);
  aleaPoly.assign3(9, 0.5, 0.3, 10, 0.42375, 0.5, 11, 0.5, 0.7);
  aleaPoly.assign3(12, 0.75, 0.7, 13, 0.75, 0.6, 14, 1, 0.8);
  aleaPoly.assign3(15, 0.75, 1, 16, 0.75, 0.9, 17, 0.5, 0.9);
  aleaPoly.assign3(18, 0.3475, 0.7, 19, 0.25, 0.9, 20, 0, 0.9);
  aleaPoly.assign3(21, 0, 0.7, 22, 0.25, 0.7, 23, 0.3125, 0.5);
  aleaPoly.assign2(24, 0.25, 0.3, 25, 0, 0.3);

  poly emptyPoly = new poly(12);
  emptyPoly.assign3(0, .35, 0, 1, .65, 0, 2, .65, .35);
  emptyPoly.assign3(3, 1, .35, 4, 1, .65, 5, .65, .65);
  emptyPoly.assign3(6, .65, 1, 7, .35, 1, 8, .35, .65);
  emptyPoly.assign3(9, 0, .65, 10, 0, .35, 11, .35, .35);
  poly deletePoly = new poly(12);
  deletePoly.assign3(0, .45, 0, 1, .55, 0, 2, .55, .45);
  deletePoly.assign3(3, 1, .45, 4, 1, .55, 5, .55, .55);
  deletePoly.assign3(6, .55, 1, 7, .45, 1, 8, .45, .55);
  deletePoly.assign3(9, 0, .55, 10, 0, .45, 11, .45, .45);
  deletePoly.rotate(-45);
  poly deleteStrokePoly = new poly(12);
  deleteStrokePoly.assign3(0, .45, 0, 1, .55, 0, 2, .55, .45);
  deleteStrokePoly.assign3(3, 1, .45, 4, 1, .55, 5, .55, .55);
  deleteStrokePoly.assign3(6, .55, 1, 7, .45, 1, 8, .45, .55);
  deleteStrokePoly.assign3(9, 0, .55, 10, 0, .45, 11, .45, .45);
  deleteStrokePoly.rotate(-30);
  poly labelPoly = new poly(6);
  labelPoly.assign3(0, 0, 0, 1, 0, 1, 2, 1, 1);
  labelPoly.assign3(3, 1, 0.8, 4, 0.2, 0.8, 5, 0.2, 0);
  poly strokePoly = new poly(8);
  strokePoly.assign3(0, 0, 0, 1, 0.9, 0, 2, 1, 0.1);
  strokePoly.assign3(3, 1, 0.3, 4, 0.9, 0.4, 5, 0.2, 0.4);
  strokePoly.assign2(6, 0.2, 1, 7, 0, 1);

  poly savePathPoly = new poly(8);
  savePathPoly.assign3(0, 0, 0, 1, 1, 0, 2, 1, 0.6);
  savePathPoly.assign3(3, 0, 0.6, 4, 0, 0.8, 5, 1, 0.8);
  savePathPoly.assign2(6, 1, 1, 7, 0, 1);

  poly makeRegularPoly = new poly(24);
  makeRegularPoly.assign3(0, 0.8, 0.4, 1, 1, 0.4, 2, 1, 0.6);
  makeRegularPoly.assign(3, 0.8, 0.6);
  float[] center = {
    0.5, 0.5
  };
  float[] pt1 = {
    0.8, 0.4
  };
  float[] pt2 = {
    1, 0.4
  };
  float[] pt3 = {
    1, 0.6
  };
  float[] pt4 = {
    0.8, 0.6
  };
  for(int p = 0; p < 5; p++) {
    makeRegularPoly.assign3(4 * p + 4, rotation(center, (p + 1) * PI / 3, pt1)[0], rotation(center, (p + 1) * PI / 3, pt1)[1], 4 * p + 5, rotation(center, (p + 1) * PI / 3, pt2)[0], rotation(center, (p + 1) * PI / 3, pt2)[1], 4 * p + 6, rotation(center, (p + 1) * PI / 3, pt3)[0], rotation(center, (p + 1) * PI / 3, pt3)[1]);
    makeRegularPoly.assign(4 * p + 7, rotation(center, (p + 1) * PI / 3, pt4)[0], rotation(center, (p + 1) * PI / 3, pt4)[1]);
  }
  poly makeCompletePoly = new poly(34);
  makeCompletePoly.assign(0, 0.75, 0.933013);
  float[] pt = {
    0.75, 0.933013
  };
  for(int p = 0; p < 16; p++)
    makeCompletePoly.assign(p + 1, rotation(center, (p + 1) * PI / 12, pt)[0], rotation(center, (p + 1) * PI / 12, pt)[1]);
  makeCompletePoly.assign(17, 0.7, 0.15359);
  float[] ptbis = {
    0.7, 0.15359
  };
  for(int p = 0; p < 16; p++)
    makeCompletePoly.assign(p + 18, rotation(center, -(p + 1) * PI / 12, ptbis)[0], rotation(center, -(p + 1) * PI / 12, ptbis)[1]);
  poly newSimulationPoly = new poly(12);
  newSimulationPoly.assign3(0, .35, 0, 1, .65, 0, 2, .65, .35);
  newSimulationPoly.assign3(3, 1, .35, 4, 1, .65, 5, .65, .65);
  newSimulationPoly.assign3(6, .65, 1, 7, .35, 1, 8, .35, .65);
  newSimulationPoly.assign3(9, 0, .65, 10, 0, .35, 11, .35, .35);
  poly playPoly = new poly(3);
  playPoly.assign3(0, 0, 1, 1, 0, 0, 2, 0.866, 0.5);
  poly stopPoly = new poly(8);
  stopPoly.assign3(0, 0, 0, 1, 0.333, 0, 2, 0.333, 1);
  stopPoly.assign3(3, 0.666, 1, 4, 0.666, 0, 5, 1, 0);
  stopPoly.assign2(6, 1, 1, 7, 0, 1);
  poly endPoly = new poly(8);
  endPoly.assign3(0, 0, 0, 1, 0.7, 0.45, 2, 0.7, 0);
  endPoly.assign3(3, 1, 0, 4, 1, 1, 5, 0.7, 1);
  endPoly.assign2(6, 0.7, 0.55, 7, 0, 1);
  poly debutPoly = new poly(8);
  debutPoly.assign3(0, 0, 0, 1, 0.7, 0.45, 2, 0.7, 0);
  debutPoly.assign3(3, 1, 0, 4, 1, 1, 5, 0.7, 1);
  debutPoly.assign2(6, 0.7, 0.55, 7, 0, 1);
  debutPoly.rotate(180);
  poly stepByStepPoly = new poly(7);
  stepByStepPoly.assign3(0, 0, 0, 1, 0.5, 0.5, 2, 0.5, 0);
  stepByStepPoly.assign3(3, 1, 0.5, 4, 0.5, 1, 5, 0.5, 0.5);
  stepByStepPoly.assign(6, 0, 1);

  // toolbar for construction mode-----------------------------------------------------
  construcToolbar = new toolbar(spacing, 30, 6 *btnSize + 5 *spacing, btnSize, spacing, 10);
  emptyBtn = construcToolbar.addButton(1, "left");
  construcToolbar.buttons[emptyBtn].assign(1, "Vide", "", emptyPoly, Blue);
  randomBtn = construcToolbar.addButton(1, "left");
  construcToolbar.buttons[randomBtn].assign(1, "Aléatoire", "", aleaPoly, Blue);
  resetBtn = construcToolbar.addButton(1, "left");
  construcToolbar.buttons[resetBtn].assign(1, "Reset()", "", resetPoly, redColor, Blue);
  makeCompleteBtn = construcToolbar.addButton(1, "left");
  construcToolbar.buttons[makeCompleteBtn].assign(1, "Complet", "", makeCompletePoly, Blue);
  makeLoopBtn = construcToolbar.addButton(1, "left");
  construcToolbar.buttons[makeLoopBtn].assign(1, "boucle", "", loopPoly, Blue);
  deleteBtn = construcToolbar.addButton(1, "left");
  construcToolbar.buttons[deleteBtn].assign(1, "supprimer", "un sommet", deletePoly, redColor, Blue);
  deleteStrokeBtn = construcToolbar.addButton(1, "left");
  construcToolbar.buttons[deleteStrokeBtn].assign(1, "supprimer", "une arête", deleteStrokePoly, redColor, Blue);

  // display toolbar-----------------------------------------------------
  displayToolbar = new toolbar(7 *btnSize + 9 *spacing, 30, btnSize + 3 *spacing, btnSize, spacing, 10);
  labelBtn = displayToolbar.addButton(2, "left");
  displayToolbar.buttons[labelBtn].assign(showState, "Afficher", "le nom", labelPoly, Blue);
  displayToolbar.buttons[labelBtn].assign(hideState, "Cacher", "le nom", labelPoly, Blue);
  displayToolbar.buttons[labelBtn].current = 2;
  labelStrokeBtn = displayToolbar.addButton(2, "left");
  displayToolbar.buttons[labelStrokeBtn].assign(showState, "Afficher", "ponderations", strokePoly, Blue);
  displayToolbar.buttons[labelStrokeBtn].assign(hideState, "Cacher", "ponderations", strokePoly, Blue);
  displayToolbar.buttons[labelStrokeBtn].current = 2;
  makeRegularBtn = displayToolbar.addButton(1, "right");
  displayToolbar.buttons[makeRegularBtn].assign(1, "Rendre", "régulier", makeRegularPoly, Blue);

  // pathToolbar---------------------------------------------------------
  pathToolbar = new toolbar(6 *btnSize + 8 *spacing, 30, 5 *btnSize + 4 *spacing, btnSize, spacing, 10);
  addPathBtn = pathToolbar.addButton(1, "left");
  pathToolbar.buttons[addPathBtn].assign(1, "Nouveau", "chemin", emptyPoly, Blue);
  randomPathBtn = pathToolbar.addButton(1, "left");
  pathToolbar.buttons[randomPathBtn].assign(1, "Chemin", "aléatoire", aleaPoly, Blue);
  deletePathBtn = pathToolbar.addButton(1, "left");
  pathToolbar.buttons[deletePathBtn].assign(1, "Supprimer", "ce chemin", deletePoly, redColor, Blue);
  resetPathBtn = pathToolbar.addButton(1, "right");
  pathToolbar.buttons[resetPathBtn].assign(1, "Reset()", "", resetPoly, redColor, Blue);
  savePathBtn = pathToolbar.addButton(1, "right");
  pathToolbar.buttons[savePathBtn].assign(1, "Sauvegarder", "chemin", savePathPoly, Blue, dkGrayColor);

  // playToolbar
  playToolbar = new toolbar(6 *btnSize + 8 *spacing, 30, 5 *btnSize + 4 *spacing, btnSize, spacing, 10);
  debutSimulationBtn = playToolbar.addButton(1, "left");
  playToolbar.buttons[debutSimulationBtn].assign(1, "Début", "", debutPoly, Blue);
  resetSimulationBtn = playToolbar.addButton(1, "left");
  playToolbar.buttons[resetSimulationBtn].assign(1, "Reset()", "", resetPoly, redColor, Blue);
  endBtn = playToolbar.addButton(1, "right");
  playToolbar.buttons[endBtn].assign(1, "FIN", "", endPoly, Blue);
  stepByStepBtn = playToolbar.addButton(1, "right");
  playToolbar.buttons[stepByStepBtn].assign(1, "pas à", "pas", stepByStepPoly, Blue);
  playBtn = playToolbar.addButton(2, "right");
  playToolbar.buttons[playBtn].assign(playState, "PlAY", "", playPoly, Blue);
  playToolbar.buttons[playBtn].assign(stopState, "PAUSE", "", stopPoly, Blue);
  playToolbar.buttons[playBtn].current = 2;

  // first textfield:  set order graph - set connection's number (random distribution)
  gOrderDef = new textfield(int (width - rightMargin / 2) - 40, 60 + 5 *btnSize);
  gOrderDef.setInitText(str(initOrder));
  gOrderDef.setDimension(20, 20);

  gConnectionDef = new textfield(int (width - rightMargin / 2) - 15, 60 + 7 *btnSize);
  gConnectionDef.setInitText(str(totalNbConnection));
  gConnectionDef.setDimension(20, 20);

  // default graph---------------------------------------------------------------------
  g1 = new graph(int (gOrderDef.getValue()));
  g1.setOriented(true);
  g1.setSimple(false);
  g1.setPonderate(true);
  g1.setConnectionList(coupleList(g1.getOrder(), totalNbConnection, g1.getOriented(), g1.getSimple()));
  g1.setRatioRadius(10);
  g1.setVertexCoords("regular");
  g1.setScale(1);
  g1.setGDisplayLabel(false);
  vTranslate = new PVector[g1.getOrder()];
  if(g1.ponderate) {
    ponderation = new ArrayList();
    for(int k = 0; k < g1.gConnection.size(); k++) {
      textfield pond = new textfield();
      pond.setInitText(str(g1.getStrokePonderation(k)));
      ponderation.add(pond);
    }
    initialVertexProba = new textfield();
    initialVertexProba.setInitText(str(0));
    initialVertexProba.setDimension(100, 20);
  }
  cheminAleaParametre = new textfield[cheminAleaInfo.length];
  for(int k = 0; k < cheminAleaParametre.length; k++) {
    cheminAleaParametre[k] = new textfield();
    cheminAleaParametre[k].setInitText("-");
    cheminAleaParametre[k].setDimension(100, 20);
  }
  for(int k = 0; k < g1.gConnection.size(); k++)
    g1.pathStrokes.add(false);
  selectedList = new ArrayList();
  savedSelectedList = new int[0][0];
  strokePath = new ArrayList();

  nbEtapesProbaText = new textfield();
  nbEtapesProbaText.setInitText(str(nbEtapesProba));
  nbEtapesProbaText.setDimension(100, 20);
}
void draw() {
  background(bGround);
  // around the graph-------------------------------------------------------------------
  stroke(0);
  strokeWeight(2);
  line(0, height - 3 * bottomMargin, width, height - 3 * bottomMargin);
  textFont(modeFontBtn);
  infoGraphAnswer[0] = str(g1.getOrder());
  infoGraphAnswer[1] = str(g1.gConnection.size());
  infoGraphAnswer[2] = str(g1.getOriented());
  infoGraphAnswer[3] = str(g1.getPonderate());
  infoGraphAnswer[4] = str(g1.getSimple());
  infoGraphAnswer[5] = str(g1.isCompleted());
  infoGraphAnswer[6] = "-";
  if(choice1Down)
    g1.workingMethod = 0;
  else if((!choice1Down) && (choice2Down))
    g1.workingMethod = 1;
  else if((!choice2Down) && (choice3Down))
    g1.workingMethod = 2;
  else if(!choice3Down)
    g1.workingMethod = 3;
  if(modeChoices[0].update() != 0) {
    g1.makeNoSelected();
    g1.makeNoStrokeSelected();
    deltaT2 = 0;
    deltaT3 = 0;
    choice1GoDown = choice2GoDown = choice3GoDown = true;
    playToolbar.buttons[playBtn].current = 2;
    nbEtapesEnCours = 0;
    isProbaPlaying = false;
  }
  if(modeChoices[1].update() != 0) {
    g1.makeNoSelected();
    g1.makeNoStrokeSelected();
    deltaT2 = 0;
    deltaT3 = 0;
    if(choice1Down)
      choice1GoUp = true;
    else if(!choice3Down)
      choice2GoDown = choice3GoDown = true;
    else if((!choice2Down) && (choice3Down))
      choice2GoDown = true;
  }
  if(modeChoices[2].update() != 0) {
    deltaT2 = 0;
    deltaT3 = 0;
    if(choice1Down)
      choice2GoUp = choice1GoUp = true;
    else if(!choice3Down)
      choice3GoDown = true;
    else if((choice3Down) && (!choice1Down))
      choice2GoUp = true;
  }
  if(modeChoices[3].update() != 0) {
    deltaT2 = 0;
    deltaT3 = 0;
    if(choice3Down)
      choice3GoUp = choice2GoUp = choice1GoUp = true;
    gProbaTransition = g1.makeTransitionMatrix();
    initialMatrixProba = new matrix(1, g1.getOrder());
    for(int k = 0; k < initialMatrixProba.getDimC(); k++) {
      if(k == int (initialVertexProba.getValue()))
        initialMatrixProba.setCoeff(0, k, 1.);
      else
        initialMatrixProba.setCoeff(0, k, 0.);
    }
    for(int k = 0; k < initialMatrixProba.getDimC(); k++)
      g1.probaVertexPotentiel = initialMatrixProba.getRow(0);
    gTransitionPlay = new matrix(g1.getOrder(), g1.getOrder());
    for(int i = 0; i < g1.getOrder(); i++)
      for(int j = 0; j < g1.getOrder(); j++) {
        if(j == i)
          gTransitionPlay.setCoeff(i, j, 1);
        else
          gTransitionPlay.setCoeff(i, j, 0);
      }
  }
  if(choice1GoUp) {
    g1.workingMethod = -1;
    float end = 50 + 15 * graphTextInfo.length + ecartMenu;
    float end2 = spacing;
    if(yChoice1 > round(end)) {
      yChoice0 = xToy(yChoice0, 50, deltaT2);
      yChoice1 = xToy(yChoice1, end, deltaT2);
      deltaT2 += 0.025;
    } else {
      yChoice0 = 50;
      yChoice1 = round(end);
      choice1GoUp = false;
      choice1Down = false;
    }
    if(displayToolbar.getPos()[0] > end2) {
      displayToolbar.setPos(xToy(displayToolbar.getPos()[0], end2, deltaT3), displayToolbar.getPos()[1]);
      deltaT3 += 0.025;
    } else
      displayToolbar.setPos(spacing, displayToolbar.getPos()[1]);
  }
  if(choice1GoDown) {
    g1.workingMethod = -1;
    float end = 100 + 11 * btnSize - round(btnSize / 2) + 15 * graphTextInfo.length + ecartMenu;
    float end2 = 7 * btnSize + 9 * spacing;
    if(yChoice1 < round(end)) {
      yChoice1 = xToy(yChoice1, end, deltaT2);
      yChoice0 = xToy(yChoice0, 35 + 11 * btnSize - round(btnSize / 2), deltaT2);
      deltaT2 += 0.025;
    } else {
      yChoice0 = round(35 + 11 * btnSize - round(btnSize / 2));
      yChoice1 = round(end);
      choice1GoDown = false;
      choice1Down = true;
    }
    if(displayToolbar.getPos()[0] < end2) {
      displayToolbar.setPos(xToy(displayToolbar.getPos()[0], end2, deltaT3), displayToolbar.getPos()[1]);
      deltaT3 += 0.025;
    } else
      displayToolbar.setPos(end2, displayToolbar.getPos()[1]);
  }
  if(choice2GoUp) {
    g1.workingMethod = -1;
    float end = 50 + 15 * graphTextInfo.length + ecartMenu + 1.5 * btnSize;
    if(yChoice2 > round(end)) {
      yChoice2 = xToy(yChoice2, end, deltaT2);
      deltaT2 += 0.025;
    } else {
      yChoice2 = round(end);
      choice2GoUp = false;
      choice2Down = false;
    }
  }
  if(choice2GoDown) {
    g1.workingMethod = -1;
    float end = 100 + 11 * btnSize - round(btnSize / 2) + 15 * graphTextInfo.length + ecartMenu + 1.5 * btnSize;
    if(yChoice2 < round(end)) {
      yChoice2 = xToy(yChoice2, end, deltaT2);
      deltaT2 += 0.025;
    } else {
      yChoice2 = round(end);
      choice2GoDown = false;
      choice2Down = true;
    }
  }
  if(choice3GoUp) {
    g1.workingMethod = -1;
    float end = 50 + 15 * graphTextInfo.length + ecartMenu + 3 * btnSize;
    if(yChoice3 > round(end)) {
      yChoice3 = xToy(yChoice3, end, deltaT2);
      deltaT2 += 0.025;
    } else {
      yChoice3 = round(end);
      choice3GoUp = false;
      choice3Down = false;
    }
  }
  if(choice3GoDown) {
    g1.workingMethod = -1;
    float end = 100 + 11 * btnSize - round(btnSize / 2) + 15 * graphTextInfo.length + ecartMenu + 3 * btnSize;
    if(yChoice3 < round(end)) {
      yChoice3 = xToy(yChoice3, end, deltaT2);
      deltaT2 += 0.025;
    } else {
      yChoice3 = round(end);
      choice3GoDown = false;
      choice3Down = true;
    }
  }
  modeChoices[1].assign(1, round(xChoice), round(yChoice1));
  modeChoices[2].assign(1, round(xChoice), round(yChoice2));
  modeChoices[3].assign(1, round(xChoice), round(yChoice3));
  displayToolbar.buttons[labelBtn].assign(showState, round(displayToolbar.getPos()[0]), round(displayToolbar.getPos()[1]));
  displayToolbar.buttons[labelBtn].assign(hideState, round(displayToolbar.getPos()[0]), round(displayToolbar.getPos()[1]));
  displayToolbar.buttons[makeRegularBtn].assign(1, round(displayToolbar.getPos()[0] + 2 * btnSize + 3 * spacing), round(displayToolbar.getPos()[1]));
  displayToolbar.buttons[labelStrokeBtn].assign(showState, round(displayToolbar.getPos()[0] + btnSize + spacing), round(displayToolbar.getPos()[1]));
  displayToolbar.buttons[labelStrokeBtn].assign(hideState, round(displayToolbar.getPos()[0] + btnSize + spacing), round(displayToolbar.getPos()[1]));

  btnFont.setFont("big");
  textAlign(CENTER, CENTER);
  fill(Blue);
  text("- Affichage -", displayToolbar.getPos()[0] + (btnSize + 3 * spacing + displayToolbar.unoccupied()[1]) / 2, 10);

  // working mode button's action and state---------------------------------------------------
  if(g1.workingMethod == 0) {
    btnFont.setFont("small");
    U1 = construcToolbar.update();
    textAlign(CENTER, CENTER);
    fill(Blue);
    btnFont.setFont("big");
    text("- Construction -", spacing + (7 * btnSize + 6 * spacing) / 2, 10);
    btnFont.setFont("medium");
    if(constructModeBtn[0].current == 1)
      g1.setOriented(false);
    else
      g1.setOriented(true);
    if(constructModeBtn[1].current == 1)
      g1.setSimple(true);
    else
      g1.setSimple(false);
    if(constructModeBtn[0].update() != 0) {
      boolean tempType = g1.getOriented();
      boolean tempSimple = g1.getSimple();
      boolean tempPonderate = g1.getPonderate();
      g1 = new graph(int (gOrderDef.getValue()));
      g1.setOriented(tempType);
      g1.setSimple(tempSimple);
      g1.setPonderate(tempPonderate);
      g1.setConnectionList();
      g1.setRatioRadius(10);
      g1.setVertexCoords("regular");
      g1.setScale(1);
      vTranslate = new PVector[g1.getOrder()];
      if(g1.ponderate) {
        ponderation = new ArrayList();
        for(int k = 0; k < g1.gConnection.size(); k++) {
          textfield pond = new textfield();
          pond.setInitText(str(g1.getStrokePonderation(k)));
          ponderation.add(pond);
        }
      }
      // chemin
      for(int k = 0; k < g1.gConnection.size(); k++)
        g1.pathStrokes.add(false);
      selectedList = new ArrayList();
      strokePath = new ArrayList();
      selectedListText = "";
    }
    if(constructModeBtn[1].update() != 0) {
      boolean tempType = g1.getOriented();
      boolean tempSimple = g1.getSimple();
      boolean tempPonderate = g1.getPonderate();
      g1 = new graph(int (gOrderDef.getValue()));
      g1.setOriented(tempType);
      g1.setSimple(tempSimple);
      g1.setPonderate(tempPonderate);
      g1.setConnectionList();
      g1.setRatioRadius(10);
      g1.setVertexCoords("regular");
      g1.setScale(1);
      vTranslate = new PVector[g1.getOrder()];
      if(g1.ponderate) {
        ponderation = new ArrayList();
        for(int k = 0; k < g1.gConnection.size(); k++) {
          textfield pond = new textfield();
          pond.setInitText(str(g1.getStrokePonderation(k)));
          ponderation.add(pond);
        }
      }
      // chemin
      for(int k = 0; k < g1.gConnection.size(); k++)
        g1.pathStrokes.add(false);
      selectedList = new ArrayList();
      strokePath = new ArrayList();
      selectedListText = "";

      // probabiliste
      gProbaTransition = g1.makeTransitionMatrix();
    }
    textFont(orderFont);
    gOrderDef.display();
    gConnectionDef.display();
    btnFont.setFont("medium");
    textAlign(LEFT, CENTER);
    text("+ Ordre :", xChoice, 68.5 + 5 * btnSize);
    text("+ Nb arêtes :", xChoice, 68.5 + 7 * btnSize);
    for(int k = 0; k < g1.getOrder(); k++) {
      if(selectedList.size() != 0) {
        int p = ((Integer) (selectedList.get(selectedList.size() - 1))).intValue();
        if(g1.isConnected(p, k))
          g1.isOnePathPossible.set(k, true);
        else
          g1.isOnePathPossible.set(k, false);
      } else
        g1.isOnePathPossible.set(k, false);
    }
  } else if(g1.workingMethod == 1) {
    // actualise les pondérations en temps réel (sinon pb de longueur de chemin lors d'un changement de pondération en cours)
    if((pondModif) || (pathModif)) {
      pathLength = 0;
      for(int p = 0; p < strokePath.size(); p++)
        pathLength += g1.getStrokePonderation(((Integer) strokePath.get(p)).intValue());
      pathModif = false;
    }
    btnFont.setFont("big");
    text("- Chemin -", 9 * btnSize + 9 * spacing + 1.5 * spacing, 10);
    btnFont.setFont("small");
    U3 = pathToolbar.update();
    randomPathRequested = (U3[0] == randomPathBtn);
    addPathRequested = (U3[0] == addPathBtn);
    deletePathRequested = (U3[0] == deletePathBtn);
    resetPathRequested = (U3[0] == resetPathBtn);
    savePathRequested = (U3[0] == savePathBtn);
    if(addPathRequested) {
      cheminDragged = false;
      selectedList = new ArrayList();
      strokePath = new ArrayList();
      for(int k = 0; k < g1.pathVertices.size(); k++)
        g1.pathVertices.set(k, false);
      selectedListText = "";
      for(int k = 0; k < g1.gConnection.size(); k++)
        g1.pathStrokes.set(k, false);
      pathModif = true;
    }
    if(resetPathRequested) {
      selectedList = new ArrayList();
      strokePath = new ArrayList();
      savedSelectedList = new int[0][0];
      savedPathAnswer0 = new String[0];
      savedPathAnswer1 = new String[0];
      savedPathAnswer2 = new String[0];
      for(int k = 0; k < g1.pathVertices.size(); k++)
        g1.pathVertices.set(k, false);
      selectedListText = "";
      for(int k = 0; k < g1.gConnection.size(); k++)
        g1.pathStrokes.set(k, false);
      pathModif = true;
    }
    if(savePathRequested) {
      savedPathAnswer0 = (String[])append(savedPathAnswer0, infoCheminAnswer[0]);
      savedPathAnswer1 = (String[])append(savedPathAnswer1, infoCheminAnswer[1]);
      savedPathAnswer2 = (String[])append(savedPathAnswer2, infoCheminAnswer[2]);
      int[] savedListInter = new int[selectedList.size()];
      for(int k = 0; k < selectedList.size(); k++)
        savedListInter[k] = ((Integer) selectedList.get(k)).intValue();
      savedSelectedList = (int[][])append(savedSelectedList, savedListInter);
      if(savedPathAnswer0.length > 1) {
        ajout += 20 + 2 * maxWidthCheminInfo + textWidth(savedPathAnswer0[savedPathAnswer0.length - 2]);
        xCheminText = append(xCheminText, ajout);
      }
    }
    if(randomPathRequested) {
      cheminDragged = false;
      selectedList = new ArrayList();
      strokePath = new ArrayList();
      for(int k = 0; k < g1.pathVertices.size(); k++)
        g1.pathVertices.set(k, false);
      selectedListText = "";
      for(int k = 0; k < g1.gConnection.size(); k++)
        g1.pathStrokes.set(k, false);
      pathModif = true;
    }
    infoCheminAnswer[0] = selectedListText;
    infoCheminAnswer[1] = str(selectedList.size());
    infoCheminAnswer[2] = str(pathLength);

    textAlign(LEFT, CENTER);
    btnFont.setFont("medium");
    for(int p = 0; p < cheminTextInfo.length; p++) {
      fill(textInfoColor);
      text(cheminTextInfo[p], xChoice + 2 * btnSize, yChoice1 + 20 + 15 * (p + 1));
      fill(Blue);
      text(" : " + infoCheminAnswer[p], xChoice + 2 * btnSize + maxWidthCheminInfo, yChoice1 + 20 + 15 * (p + 1));
    }
    for(int k = 0; k < cheminAleaInfo.length; k++) {
      fill(textInfoColor);
      text(cheminAleaInfo[k], xChoice + 2 * btnSize, yChoice1 + 100 + 15 * k);
      fill(Blue);
      cheminAleaParametre[k].setPosition(xChoice + 3 * btnSize + maxWidthAleaInfo, yChoice1 + 90 + 15 * k);
      cheminAleaParametre[k].display();
    }
    for(int k = 0; k < g1.getOrder(); k++) {
      if(selectedList.size() != 0) {
        int p = ((Integer) (selectedList.get(selectedList.size() - 1))).intValue();
        if(g1.isConnected(p, k))
          g1.isOnePathPossible.set(k, true);
        else
          g1.isOnePathPossible.set(k, false);
      } else
        g1.isOnePathPossible.set(k, false);
    }
  } else if(g1.workingMethod == 2) {
    fill(0);
    btnFont.setFont("huge");
    text("Mode en cours d'élaboration", width / 2, height - 1.5 * bottomMargin);
  } else if(g1.workingMethod == 3) {
    fill(0);
    btnFont.setFont("huge");
    text("Mode en cours d'élaboration", width / 2, height - 1.5 * bottomMargin);
    btnFont.setFont("big");
    fill(Blue);
    text("- Animation -", 9 * btnSize + 9 * spacing + 1.5 * spacing, 10);
    btnFont.setFont("small");
    U4 = playToolbar.update();
    debutSimulationRequested = (U4[0] == debutSimulationBtn);
    playRequested = (U4[0] == playBtn);
    endRequested = (U4[0] == endBtn);
    resetSimulationRequested = (U4[0] == resetSimulationBtn);
    stepByStepRequested = (U4[0] == stepByStepBtn);
    textAlign(LEFT, CENTER);
    btnFont.setFont("medium");
    text("+ sommet initial : ", xChoice + 2 * btnSize, yChoice3 + 50);
    text("+ nombres d'étapes : ", xChoice + 2 * btnSize, yChoice3 + 70);
    text("+ rang : ", xChoice + 2 * btnSize, yChoice3 + 90);
    initialVertexProba.setPosition(xChoice + 2 * btnSize + textWidth("+ nombres d'étapes : "), yChoice3 + 40);
    initialVertexProba.display();
    nbEtapesProbaText.setPosition(xChoice + 2 * btnSize + textWidth("+ nombres d'étapes : "), yChoice3 + 60);
    nbEtapesProbaText.display();
    text(str(nbEtapesEnCours), xChoice + 2 * btnSize + textWidth("+ nombres d'étapes : "), yChoice3 + 90);
    if(playRequested) {
      if(playToolbar.buttons[playBtn].current == 1)
        isProbaPlaying = true;
      else
        isProbaPlaying = false;
    }
    if(debutSimulationRequested) {
      isProbaPlaying = false;
      nbEtapesEnCours = 0;
      playToolbar.buttons[playBtn].current = 2;
      gProbaTransition = g1.makeTransitionMatrix();
      initialMatrixProba = new matrix(1, g1.getOrder());
      for(int k = 0; k < initialMatrixProba.getDimC(); k++) {
        if(k == int (initialVertexProba.getValue()))
          initialMatrixProba.setCoeff(0, k, 1.);
        else
          initialMatrixProba.setCoeff(0, k, 0.);
      }
      for(int k = 0; k < initialMatrixProba.getDimC(); k++)
        g1.probaVertexPotentiel = initialMatrixProba.getRow(0);
      gTransitionPlay = new matrix(g1.getOrder(), g1.getOrder());
      for(int i = 0; i < g1.getOrder(); i++)
        for(int j = 0; j < g1.getOrder(); j++) {
          if(j == i)
            gTransitionPlay.setCoeff(i, j, 1);
          else
            gTransitionPlay.setCoeff(i, j, 0);
        }
    }
    if(pondModif)
      gProbaTransition = g1.makeTransitionMatrix();
    if(initialVertexModif) {
      for(int k = 0; k < initialMatrixProba.getDimC(); k++) {
        if(k == int (initialVertexProba.getValue()))
          initialMatrixProba.setCoeff(0, k, 1.);
        else
          initialMatrixProba.setCoeff(0, k, 0.);
      }
      g1.probaVertexPotentiel = initialMatrixProba.getRow(0);
      nbEtapesEnCours = 0;
      gTransitionPlay = new matrix(g1.getOrder(), g1.getOrder());
      for(int i = 0; i < g1.getOrder(); i++)
        for(int j = 0; j < g1.getOrder(); j++) {
          if(j == i)
            gTransitionPlay.setCoeff(i, j, 1);
          else
            gTransitionPlay.setCoeff(i, j, 0);
        }
      initialVertexModif = false;
    }
    if(nbEtapesProbaModif)
      nbEtapesProba = int (nbEtapesProbaText.getValue());
    if(isProbaPlaying) {
      if((nbEtapesEnCours < nbEtapesProba) && (!g1.isThereVertexAnim())) {
        gTransitionPlay = mul(gTransitionPlay, gProbaTransition);
        g1.probaVertexPotentiel = mul(initialMatrixProba, gTransitionPlay).getRow(0);
        nbEtapesEnCours += 1;
      } else if(nbEtapesEnCours == nbEtapesProba) {
        isProbaPlaying = false;
        // nbEtapesEnCours = 0;
        playToolbar.buttons[playBtn].current = 2;
      }
    }
    if(stepByStepRequested)
      if(nbEtapesEnCours < nbEtapesProba) {
        gTransitionPlay = mul(gTransitionPlay, gProbaTransition);
        g1.probaVertexPotentiel = mul(initialMatrixProba, gTransitionPlay).getRow(0);
        nbEtapesEnCours += 1;
      }
    if(endRequested) {
      gTransitionPlay = mPow(gProbaTransition, nbEtapesProba);
      g1.probaVertexPotentiel = mul(initialMatrixProba, gTransitionPlay).getRow(0);
      nbEtapesEnCours = nbEtapesProba;
    }
    if(resetSimulationRequested) {
      isProbaPlaying = false;
      nbEtapesEnCours = 0;
      initialVertexProba.setValue(str(0));
      playToolbar.buttons[playBtn].current = 2;
      gProbaTransition = g1.makeTransitionMatrix();
      initialMatrixProba = new matrix(1, g1.getOrder());
      for(int k = 0; k < initialMatrixProba.getDimC(); k++) {
        if(k == int (initialVertexProba.getValue()))
          initialMatrixProba.setCoeff(0, k, 1.);
        else
          initialMatrixProba.setCoeff(0, k, 0.);
      }
      for(int k = 0; k < initialMatrixProba.getDimC(); k++)
        g1.probaVertexPotentiel = initialMatrixProba.getRow(0);
      gTransitionPlay = new matrix(g1.getOrder(), g1.getOrder());
      for(int i = 0; i < g1.getOrder(); i++)
        for(int j = 0; j < g1.getOrder(); j++) {
          if(j == i)
            gTransitionPlay.setCoeff(i, j, 1);
          else
            gTransitionPlay.setCoeff(i, j, 0);
        }
    }
  }
  // code indépendant de l'environnement de travail utilisé

  textAlign(LEFT, CENTER);
  btnFont.setFont("big");
  fill(textInfoColor);
  text("- Info Graphe -", round(xChoice), round(yChoice0));
  text("- Info Arêtes -", round(xChoice), round(yChoice0 + 15 * graphTextInfo.length + 60));
  btnFont.setFont("medium");
  for(int p = 0; p < graphTextInfo.length; p++)
    text(graphTextInfo[p], xChoice + 2 * btnSize, yChoice0 + 20 + 15 * (p + 1));
  fill(Blue);
  for(int p = 0; p < graphTextInfo.length; p++)
    text(" : " + infoGraphAnswer[p], xChoice + 2 * btnSize + maxWidthInfoText, yChoice0 + 20 + 15 * (p + 1));
  if(!g1.gNoStrokeSelected()) {
    infoStrokeAnswer[0] = str(g1.getSelectedStroke());
    int[] thisConnection = (int[])g1.gConnection.get(g1.getSelectedStroke());
    int vertexOne = thisConnection[0];
    int vertexTwo = thisConnection[1];
    infoStrokeAnswer[1] = str(vertexOne) + " - " + str(vertexTwo);
    for(int p = 0; p < strokeTextInfo.length - 1; p++) {
      fill(textInfoColor);
      text(strokeTextInfo[p], xChoice + 2 * btnSize, yChoice0 + 15 * graphTextInfo.length + 80 + 15 * (p + 1));
      fill(Blue);
      text(" : " + infoStrokeAnswer[p], xChoice + 2 * btnSize + maxWidthInfoStrokeText, yChoice0 + 15 * graphTextInfo.length + 80 + 15 * (p + 1));
    }
    fill(textInfoColor);
    text(strokeTextInfo[strokeTextInfo.length - 1], xChoice + 2 * btnSize, yChoice0 + 15 * graphTextInfo.length + 80 + 15 * (strokeTextInfo.length + 1));
    fill(Blue);
    text(" : ", xChoice + 2 * btnSize + maxWidthInfoStrokeText, yChoice0 + 15 * graphTextInfo.length + 80 + 15 * (strokeTextInfo.length + 1));
    textfield pond = (textfield) ponderation.get(g1.getSelectedStroke());
    pond.setDimension(30, 20);
    pond.setPosition(xChoice + 2 * btnSize + maxWidthInfoStrokeText + textWidth(" + "), yChoice0 + 15 * graphTextInfo.length + 80 + 15 * strokeTextInfo.length + 6);
    pond.display();
    pondModif = true;
  } else
    pondModif = false;
   // affiche les chemins sauvegardés en bas de fenetre.
  if(savedPathAnswer0.length > 0) {
    if((xCheminText[0] + xCheminText[savedPathAnswer0.length - 1] + maxWidthCheminInfo + textWidth(savedPathAnswer0[savedPathAnswer0.length - 1]) > 950) && (!cheminDragged))
      xCheminText[0] -= 20;
     // premier chemin sauvegardé
    for(int j = 0; j < savedPathInfo.length; j++) {
      fill(textInfoColor);
      textAlign(LEFT, CENTER);
      text(savedPathInfo[j], xCheminText[0] + 30, height - 3 * bottomMargin + 15 * (j + 1));
    }
    fill(Blue);
    text(" : " + savedPathAnswer0[0], xCheminText[0] + maxWidthCheminInfo + 20, height - 3 * bottomMargin + 15);
    text(" : " + savedPathAnswer1[0], xCheminText[0] + maxWidthCheminInfo + 20, height - 3 * bottomMargin + 30);
    text(" : " + savedPathAnswer2[0], xCheminText[0] + maxWidthCheminInfo + 20, height - 3 * bottomMargin + 45);
    circleNumber(1, xCheminText[0], height - 3 * bottomMargin + 15, 10, color(135, 88, 204, 100), 10, 45);
    // les autres chemins sauvegardés
    for(int i = 1; i < savedPathAnswer0.length; i++) {
      for(int j = 0; j < savedPathInfo.length; j++) {
        fill(textInfoColor);
        textAlign(LEFT, CENTER);
        text(savedPathInfo[j], xCheminText[0] + xCheminText[i] + 30, height - 3 * bottomMargin + 15 * (j + 1));
      }
      fill(Blue);
      text(" : " + savedPathAnswer0[i], xCheminText[0] + xCheminText[i] + maxWidthCheminInfo + 20, height - 3 * bottomMargin + 15);
      text(" : " + savedPathAnswer1[i], xCheminText[0] + xCheminText[i] + maxWidthCheminInfo + 20, height - 3 * bottomMargin + 30);
      text(" : " + savedPathAnswer2[i], xCheminText[0] + xCheminText[i] + maxWidthCheminInfo + 20, height - 3 * bottomMargin + 45);
      circleNumber(i + 1, xCheminText[0] + xCheminText[i], height - 3 * bottomMargin + 15, 10, color(135, 88, 204, 100), 10, 45);
    }
  }
  btnFont.setFont("small");
  U2 = displayToolbar.update();
  // btn toolbar requested---------------------------------------------------------------------
  makeLoopRequested = (U1[0] == makeLoopBtn);
  makeCompleteRequested = (U1[0] == makeCompleteBtn);
  emptyRequested = (U1[0] == emptyBtn);
  randomRequested = (U1[0] == randomBtn);
  deleteRequested = (U1[0] == deleteBtn);
  resetRequested = (U1[0] == resetBtn);
  deleteStrokeRequested = (U1[0] == deleteStrokeBtn);
  makeRegularRequested = (U2[0] == makeRegularBtn);
  // buttons' toolbar action ----------------------------------------------------------
  if(displayToolbar.buttons[labelBtn].current == hideState)
    g1.setGDisplayLabel(true);
  else
    for(int k = 0; k < g1.getOrder(); k++)
      g1.vLabel[k] = false;
  if(displayToolbar.buttons[labelStrokeBtn].current == hideState)
    g1.gPondLabel = true;
  else
    g1.gPondLabel = false;
  if((orderModif) && (key == ENTER)) {
    emptyRequested = true;
    orderModif = false;
  }
  if((connectionModif) && (key == ENTER)) {
    randomRequested = true;
    connectionModif = false;
  }
  if(makeLoopRequested) {
    if(g1.getSimple() == false) {
      for(int k = 0; k < g1.getOrder(); k++)
        if(g1.vSelected[k] == true) {
          g1.gAddStroke(k, k);
          textfield pond = new textfield();
          pond.setInitText(str(g1.getStrokePonderation(g1.gConnection.size() - 1)));
          ponderation.add(pond);
          vTranslate = new PVector[g1.getOrder()];
        }
    } else
      println("error");
  }
  if(randomRequested) {
    boolean tempType = g1.getOriented();
    boolean tempSimple = g1.getSimple();
    boolean tempPonderate = g1.getPonderate();
    g1 = new graph(int (gOrderDef.getValue()));
    g1.setOriented(tempType);
    g1.setSimple(tempSimple);
    g1.setPonderate(tempPonderate);
    totalNbConnection = int (gConnectionDef.getValue());
    g1.setOriented(g1.getOriented());
    g1.setSimple(g1.getSimple());
    g1.setConnectionList(coupleList(g1.getOrder(), totalNbConnection, g1.getOriented(), g1.getSimple()));
    g1.setRatioRadius(10);
    g1.setVertexCoords("regular");
    g1.setScale(1);
    vTranslate = new PVector[g1.getOrder()];
    if(g1.ponderate) {
      ponderation = new ArrayList();
      for(int k = 0; k < g1.gConnection.size(); k++) {
        textfield pond = new textfield();
        pond.setInitText(str(g1.getStrokePonderation(k)));
        ponderation.add(pond);
      }
    }
    // chemin
    for(int k = 0; k < g1.gConnection.size(); k++)
      g1.pathStrokes.add(false);
    selectedList = new ArrayList();
    strokePath = new ArrayList();
    selectedListText = "";
  }
  if(emptyRequested) {
    boolean tempType = g1.getOriented();
    boolean tempSimple = g1.getSimple();
    boolean tempPonderate = g1.getPonderate();
    g1 = new graph(int (gOrderDef.getValue()));
    g1.setOriented(tempType);
    g1.setSimple(tempSimple);
    g1.setPonderate(tempPonderate);
    g1.setConnectionList();
    g1.setRatioRadius(10);
    g1.setVertexCoords("regular");
    g1.setScale(1);
    vTranslate = new PVector[g1.getOrder()];
    if(g1.ponderate) {
      ponderation = new ArrayList();
      for(int k = 0; k < g1.gConnection.size(); k++) {
        textfield pond = new textfield();
        pond.setInitText(str(g1.getStrokePonderation(k)));
        ponderation.add(pond);
      }
    }
    // chemin
    for(int k = 0; k < g1.gConnection.size(); k++)
      g1.pathStrokes.add(false);
    selectedList = new ArrayList();
    strokePath = new ArrayList();
    selectedListText = "";
  }
  if(resetRequested) {
    boolean tempType = g1.getOriented();
    boolean tempSimple = g1.getSimple();
    boolean tempPonderate = g1.getPonderate();
    gOrderDef.setValue(str(initOrder));
    gConnectionDef.setValue(str(10));
    g1 = new graph(0);
    g1.setOriented(tempType);
    g1.setSimple(tempSimple);
    g1.setPonderate(tempPonderate);
    g1.setConnectionList();
    g1.setRatioRadius(10);
    g1.setVertexCoords("regular");
    g1.setScale(1);
    vTranslate = new PVector[g1.getOrder()];
    if(g1.ponderate) {
      ponderation = new ArrayList();
      for(int k = 0; k < g1.gConnection.size(); k++) {
        textfield pond = new textfield();
        pond.setInitText(str(g1.getStrokePonderation(k)));
        ponderation.add(pond);
      }
    }
    // chemin
    for(int k = 0; k < g1.gConnection.size(); k++)
      g1.pathStrokes.add(false);
    selectedList = new ArrayList();
    strokePath = new ArrayList();
    selectedListText = "";
    pathModif = true;

    gProbaTransition = new matrix();
  }
  if(makeRegularRequested) {
    makeRegular = true;
    fixBox = false;
    g1.makeNoSelected();
  }
  if(deleteRequested)
    removeMode = true;
  if(deleteStrokeRequested) {
    if(!g1.gNoStrokeSelected()) {
      if(strokePath.contains(g1.getSelectedStroke())) {
        int firstIndex = strokePath.indexOf(g1.getSelectedStroke());
        int initSize = strokePath.size();
        int initSizeVertex = selectedList.size();
        for(int k = firstIndex; k < initSize; k++) {
          strokePath.remove(strokePath.size() - 1);
          selectedList.remove(selectedList.size() - 1);
        }
        for(int q = 0; q < strokePath.size(); q++) {
          int valeur = ((Integer) strokePath.get(q)).intValue();
          if(valeur > g1.getSelectedStroke())
            strokePath.set(q, valeur - 1);
        }
        for(int k = 0; k < g1.getOrder(); k++) {
          if(selectedList.contains(k))
            g1.pathVertices.set(k, true);
          else
            g1.pathVertices.set(k, false);
        }
        selectedListText = str(((Integer) selectedList.get(0)).intValue());
        for(int k = 1; k < selectedList.size(); k++)
          selectedListText = selectedListText + "-" + str(((Integer) selectedList.get(k)).intValue());
      }
      ponderation.remove(g1.getSelectedStroke());
      g1.gRemoveStroke(g1.getSelectedStroke());
      pathModif = true;
      for(int j = 0; j < g1.gConnection.size(); j++) {
        if(strokePath.contains(j))
          g1.pathStrokes.set(j, true);
        else
          g1.pathStrokes.set(j, false);
      }
    } else
      println("Aucune arête sélectionnée");
  }
  if(makeCompleteRequested) {
    g1.setConnectionList(completeList(g1.getOrder(), g1.getOriented(), g1.getSimple()));
    ponderation = new ArrayList();
    for(int k = 0; k < g1.gConnection.size(); k++) {
      textfield pond = new textfield();
      pond.setInitText(str(g1.getStrokePonderation(k)));
      ponderation.add(pond);
    }
    for(int k = 0; k < g1.gConnection.size(); k++)
      g1.pathStrokes.add(false);
    selectedList = new ArrayList();
    strokePath = new ArrayList();
  }
  if(g1.ponderate)
    for(int k = 0; k < g1.gConnection.size(); k++) {
      textfield pond = (textfield) ponderation.get(k);
      g1.setStrokePonderation(k, float (pond.getValue()));
    }
   // graph working window----------------------------------------------------------------
  translate(gWindowWidth / 2, gWindowHeight / 2);
  // btnFont.setFont("medium");
  textFont(modeFontBtn);

  g1.display();
  // display changes
  if(makeRegular) {
    float[][] initCoords = new float[g1.getOrder()][2];
    float[][] endCoords = new float[g1.getOrder()][2];
    for(int k = 0; k < g1.getOrder(); k++) {
      initCoords[k] = g1.getCoords(k);
      endCoords[k][0] = g1.regularRadius * cos(2 * k * PI / g1.getOrder());
      endCoords[k][1] = g1.regularRadius * sin(2 * k * PI / g1.getOrder());
    }
    if(deltaT < 1.) {
      for(int k = 0; k < g1.getOrder(); k++)
        g1.setVertexCoords(k, AtoB(initCoords[k], endCoords[k], deltaT)[0], AtoB(initCoords[k], endCoords[k], deltaT)[1]);
      deltaT += c;
    } else {
      g1.setVertexCoords("regular");
      makeRegular = false;
      deltaT = 0;
    }
  }
  for(int p = 0; p < g1.getOrder(); p++)
    if(g1.vDragged[p])
      g1.setVertexCoords(p, (mouseX - gWindowWidth / 2 - vTranslate[p].x) / g1.getScale(), (mouseY - gWindowHeight / 2 - vTranslate[p].y) / g1.getScale());
  for(int p = 0; p < g1.getOrder(); p++)
    if((removeMode) && (g1.vSelected[p] == true)) {
      if(selectedList.contains(p)) {
        int firstIndex = selectedList.indexOf(p);
        int initSize = selectedList.size();
        for(int j = firstIndex; j < initSize; j++)
          selectedList.remove(selectedList.size() - 1);
        for(int q = 0; q < selectedList.size(); q++) {
          int valeur = ((Integer) selectedList.get(q)).intValue();
          if(valeur > p)
            selectedList.set(q, valeur - 1);
        }
      }
      g1.gRemoveVertex(p);
      if(selectedList.size() > 1) {
        strokePath = new ArrayList();
        pathModif = true;
        selectedListText = str(((Integer) selectedList.get(0)).intValue());
        for(int k = 1; k < selectedList.size(); k++)
          selectedListText = selectedListText + "-" + str(((Integer) selectedList.get(k)).intValue());
      } else if(selectedList.size() == 1) {
        strokePath = new ArrayList();
        pathLength = 0;
        selectedListText = str(((Integer) selectedList.get(0)).intValue());
      }
      g1.pathVertices = new ArrayList();
      for(int k = 0; k < g1.getOrder(); k++) {
        if(selectedList.contains(k))
          g1.pathVertices.add(true);
        else
          g1.pathVertices.add(false);
      }
      for(int k = 0; k < selectedList.size() - 1; k++) {
        int newStrokePathIndex = g1.getTheStroke(((Integer) (selectedList.get(k))).intValue(), ((Integer) (selectedList.get(k + 1))).intValue());
        strokePath.add(newStrokePathIndex);
        g1.pathStrokes.set(newStrokePathIndex, true);
      }
      for(int j = 0; j < g1.gConnection.size(); j++) {
        if(strokePath.contains(j))
          g1.pathStrokes.set(j, true);
        else
          g1.pathStrokes.set(j, false);
      }
      if(p < int (initialVertexProba.getValue()))
        initialVertexProba.setValue(str(int (initialVertexProba.getValue()) - 1));
      initialMatrixProba = new matrix(1, g1.getOrder());
      for(int k = 0; k < initialMatrixProba.getDimC(); k++) {
        if(k == int (initialVertexProba.getValue()))
          initialMatrixProba.setCoeff(0, k, 1.);
        else
          initialMatrixProba.setCoeff(0, k, 0.);
      }
    }
   // draw box by dragging
  if(drawBox) {
    noStroke();
    fill(0, 40);
    rect(startBoxPt[0] / g1.getScale(), startBoxPt[1] / g1.getScale(), (mouseX - gWindowWidth / 2 - startBoxPt[0]) / g1.getScale(), (mouseY - gWindowHeight / 2 - startBoxPt[1]) / g1.getScale());
    for(int p = 0; p < g1.getOrder(); p++) {
      if((min(startBoxPt[0], mouseX - gWindowWidth / 2) / g1.getScale() <= g1.getCoords(p)[0]) && (max(startBoxPt[0], mouseX - gWindowWidth / 2) / g1.getScale() >= g1.getCoords(p)[0]) && (min(startBoxPt[1], mouseY - gWindowHeight / 2) / g1.getScale() <= g1.getCoords(p)[1]) && (max(startBoxPt[1], mouseY - gWindowHeight / 2) / g1.getScale() >= g1.getCoords(p)[1]))
        g1.vSelected[p] = true;
      else
        g1.vSelected[p] = false;
    }
  } else if((fixBox) && (!draggBox)) { // select all nodes in drawingBox
    strokeWeight(1.25);
    stroke(0);
    fill(0, 40);
    rect(startBoxPt[0] / g1.getScale(), startBoxPt[1] / g1.getScale(), (endBoxPt[0] - startBoxPt[0]) / g1.getScale(), (endBoxPt[1] - startBoxPt[1]) / g1.getScale());
    for(int p = 0; p < g1.getOrder(); p++)
      if((min(startBoxPt[0], endBoxPt[0]) / g1.getScale() <= g1.getCoords(p)[0]) && (max(startBoxPt[0], endBoxPt[0]) / g1.getScale() >= g1.getCoords(p)[0]) && (min(startBoxPt[1], endBoxPt[1]) / g1.getScale() <= g1.getCoords(p)[1]) && (max(startBoxPt[1], endBoxPt[1]) / g1.getScale() >= g1.getCoords(p)[1]))
        g1.vSelected[p] = true;
  } else if(draggBox) {
    strokeWeight(1.25);
    stroke(0);
    fill(0, 40);
    rect((mouseX - gWindowWidth / 2 - draggBoxTranslate.x) / g1.getScale(), (mouseY - gWindowWidth / 2 - draggBoxTranslate.y) / g1.getScale(), draggBoxWidth / g1.getScale(), draggBoxHeight / g1.getScale());
  }
  if(g1.getNbSelected() == 0) {
    removeMode = false;
    fixBox = false;
  }
  if(keyPressed == true) {
    if(key == 'p')
      makeRandomPath(g1, 0, 2, 0, 0, 0);
    if(key == CODED)
      if(keyCode == 157)
        multiSelect = true;
  } else
    multiSelect = false;
}
// key event--------------------------------------------------------------------------

void keyPressed() {
  if(gOrderDef.focus) {
    gOrderDef.focusOn();
    orderModif = true;
  }
  if(gConnectionDef.focus) {
    gConnectionDef.focusOn();
    connectionModif = true;
  }
  if(g1.ponderate) {
    for(int k = 0; k < ponderation.size(); k++) {
      textfield pond = (textfield) ponderation.get(k);
      if(pond.focus)
        pond.focusOn();
    }
    if(initialVertexProba.focus) {
      initialVertexProba.focusOn();
      initialVertexModif = true;
    }
    for(int k = 0; k < cheminAleaParametre.length; k++)
      if(cheminAleaParametre[k].focus)
        cheminAleaParametre[k].focusOn();
    if(nbEtapesProbaText.focus) {
      nbEtapesProbaText.focusOn();
      nbEtapesProbaModif = true;
    }
  }
  if(key == 's')
    saveFrame("grahp813-###.png");
  if(key == 'Z')
    g1.setScale(g1.getScale() * 1.1);
  if(key == 'z')
    g1.setScale(g1.getScale() * 0.9);
}
// mouse event ----------------------------------------------------------------------
void mousePressed() {
  construcToolbar.onmousepress();
  displayToolbar.onmousepress();
  pathToolbar.onmousepress();
  playToolbar.onmousepress();
  for(int p = 0; p < constructModeBtn.length; p++)
    constructModeBtn[p].onmousepress();
  for(int p = 0; p < modeChoices.length; p++)
    modeChoices[p].onmousepress();
  if(gOrderDef.over(mouseX, mouseY))
    gOrderDef.focus = true;
  else
    gOrderDef.focus = false;
  if(gConnectionDef.over(mouseX, mouseY))
    gConnectionDef.focus = true;
  else
    gConnectionDef.focus = false;
  if(g1.ponderate)
    for(int k = 0; k < ponderation.size(); k++) {
      textfield pond = (textfield) ponderation.get(k);
      if(k == g1.getSelectedStroke()) {
        if(pond.over(mouseX, mouseY))
          pond.focus = true;
      } else
        pond.focus = false;
    }
  if(g1.workingMethod == 1)
    for(int k = 0; k < cheminAleaParametre.length; k++)
      if(cheminAleaParametre[k].over(mouseX, mouseY))
        cheminAleaParametre[k].focus = true;
  if(g1.workingMethod == 3) {
    if(initialVertexProba.over(mouseX, mouseY))
      initialVertexProba.focus = true;
    else
      initialVertexProba.focus = false;
    if(nbEtapesProbaText.over(mouseX, mouseY))
      nbEtapesProbaText.focus = true;
    else
      nbEtapesProbaText.focus = false;
  }
  // click sur le premier chemin sauvegardé
  if((mouseX > xCheminText[0] - 10) && (mouseX < xCheminText[0] + 10) && (mouseY > height - 3 * bottomMargin + 10) && (mouseY < height - 3 * bottomMargin + 20)) {
    selectedList = new ArrayList();
    strokePath = new ArrayList();
    for(int k = 0; k < g1.pathVertices.size(); k++)
      g1.pathVertices.set(k, false);
    for(int k = 0; k < g1.gConnection.size(); k++)
      g1.pathStrokes.set(k, false);
    selectedListText = "";
    for(int k = 0; k < savedSelectedList[0].length; k++) {
      g1.pathVertices.set(savedSelectedList[0][k], true);
      selectedList.add(savedSelectedList[0][k]);
    }
    selectedListText = str(((Integer) selectedList.get(0)).intValue());
    for(int k = 1; k < selectedList.size(); k++)
      selectedListText = selectedListText + "-" + str(((Integer) selectedList.get(k)).intValue());
    for(int p = 0; p < savedSelectedList[0].length - 1; p++) {
      int newStrokePathIndex = g1.getTheStroke(savedSelectedList[0][p], savedSelectedList[0][p + 1]);
      strokePath.add(newStrokePathIndex);
      g1.pathStrokes.set(newStrokePathIndex, true);
    }
    pathModif = true;
  }
  // click sur les autres chemins sauvegardés
  for(int i = 1; i < savedSelectedList.length; i++)
    if((mouseX > xCheminText[0] + xCheminText[i] - 10) && (mouseX < xCheminText[0] + xCheminText[i] + 10) && (mouseY > height - 3 * bottomMargin + 10) && (mouseY < height - 3 * bottomMargin + 20)) {
      selectedList = new ArrayList();
      strokePath = new ArrayList();
      for(int k = 0; k < g1.pathVertices.size(); k++)
        g1.pathVertices.set(k, false);
      for(int k = 0; k < g1.gConnection.size(); k++)
        g1.pathStrokes.set(k, false);
      selectedListText = "";
      for(int k = 0; k < savedSelectedList[i].length; k++) {
        g1.pathVertices.set(savedSelectedList[i][k], true);
        selectedList.add(savedSelectedList[i][k]);
      }
      selectedListText = str(((Integer) selectedList.get(0)).intValue());
      for(int k = 1; k < selectedList.size(); k++)
        selectedListText = selectedListText + "-" + str(((Integer) selectedList.get(k)).intValue());
      for(int p = 0; p < savedSelectedList[i].length - 1; p++) {
        int newStrokePathIndex = g1.getTheStroke(savedSelectedList[i][p], savedSelectedList[i][p + 1]);
        strokePath.add(newStrokePathIndex);
        g1.pathStrokes.set(newStrokePathIndex, true);
      }
      pathModif = true;
    }
  if((mouseX < width - rightMargin) && (mouseX > leftMargin) && (mouseY < height - bottomMargin) && (mouseY > topMargin)) {
    if(g1.workingMethod == 0) {
      if((g1.gNoOver() == true) && (g1.gNoSelected() == true)) {
        startBoxPt[0] = mouseX - gWindowWidth / 2;
        startBoxPt[1] = mouseY - gWindowHeight / 2;
      }
      if(fixBox) {
        draggBoxWidth = endBoxPt[0] - startBoxPt[0];
        draggBoxHeight = endBoxPt[1] - startBoxPt[1];
        draggBoxTranslate = new PVector(mouseX - gWindowWidth / 2 - startBoxPt[0], mouseY - gWindowHeight / 2 - startBoxPt[1]);
      }
      if(multiSelect == true)
        for(int i = 0; i < g1.getOrder(); i++) {
          if((g1.over(i) == true) && (g1.vSelected[i] == false))
            g1.vSelected[i] = true;
          else if((g1.over(i) == true) && (g1.vSelected[i] == true))
            g1.vSelected[i] = false;
        }
      else if(!multiSelect)
        if(!fixBox) { // no select box
          for(int i = 0; i < g1.getOrder(); i++) {
            if((g1.over(i) == true) && (g1.vSelected[i] == false))
              g1.vSelected[i] = true;
            else if((g1.over(i) == true) && (g1.vSelected[i] == true))
              g1.vSelected[i] = false;
          }
          for(int i = 0; i < g1.getOrder(); i++)
            if((g1.over(i) == true) && (g1.gNoSelected() == false))
              for(int p = 0; p < g1.getOrder(); p++)
                if((p != i) && (g1.vSelected[p] == true)) {
                  g1.gAddStroke(p, i);
                  textfield pond = new textfield();
                  pond.setInitText(str(g1.getStrokePonderation(g1.gConnection.size() - 1)));
                  ponderation.add(pond);
                  vTranslate = new PVector[g1.getOrder()];
                }
          if((g1.gNoSelected() == false) && (g1.gNoOver() == true) && (g1.gNoStrokeOver())) {
            g1.gAddVertex();
            for(int i = 0; i < g1.getOrder() - 1; i++) {
              if(g1.vSelected[i] == true) {
                g1.gAddStroke(i, g1.getOrder() - 1);
                textfield pond = new textfield();
                pond.setInitText(str(g1.getStrokePonderation(g1.gConnection.size() - 1)));
                ponderation.add(pond);
              }
              initialMatrixProba = new matrix(1, g1.getOrder());
              for(int k = 0; k < initialMatrixProba.getDimC(); k++) {
                if(k == int (initialVertexProba.getValue()))
                  initialMatrixProba.setCoeff(0, k, 1.);
                else
                  initialMatrixProba.setCoeff(0, k, 0.);
              }
              vTranslate = new PVector[g1.getOrder()];
            }
            g1.makeNoSelected();
          }
        }
    } else if(g1.workingMethod == 1) {
      if(chooseRandPathVertex) {}
      if(g1.isEmptyPath()) {
        for(int i = 0; i < g1.getOrder(); i++)
          if(g1.over(i)) {
            g1.pathVertices.set(i, true);
            Integer inter = i;
            selectedListText = str(i);
            selectedList.add(inter);
          }
      } else
        for(int i = 0; i < g1.getOrder(); i++) {
          if((g1.over(i) == true) && ((Boolean) g1.isOnePathPossible.get(i))) {
            g1.pathVertices.set(i, true);
            Integer inter = i;
            selectedListText = selectedListText + "-" + str(i);
            selectedList.add(inter);
            int newStrokePathIndex = g1.getTheStroke(((Integer) (selectedList.get(selectedList.size() - 2))).intValue(), ((Integer) (selectedList.get(selectedList.size() - 1))).intValue());
            strokePath.add(newStrokePathIndex);
            g1.pathStrokes.set(newStrokePathIndex, true);
            pathModif = true;
          } else if((g1.over(i) == true) && (((Boolean) g1.isOnePathPossible.get(i)) == false) && (!(Boolean) g1.pathVertices.get(i)))
            println("Sommet non accessible");
        }
    }
    for(int i = 0; i < g1.getOrder(); i++)
      vTranslate[i] = new PVector(mouseX - gWindowWidth / 2 - g1.getCoords (i)[0] *g1.getScale(), mouseY - gWindowHeight / 2 - g1.getCoords (i)[1] *g1.getScale());
  }
}
void mouseDragged() {
  if((mouseX < width - rightMargin) && (mouseX > leftMargin) && (mouseY < height - bottomMargin) && (mouseY > topMargin)) {
    if(g1.workingMethod == 0) {
      if(fixBox)
        draggBox = true;
      if(g1.gNoSelected() == true)
        drawBox = true;
      for(int p = 0; p < g1.getOrder(); p++)
        if((g1.vSelected[p]) && (drawBox == false))
          g1.vDragged[p] = true;
    }
    for(int p = 0; p < g1.getOrder(); p++)
      if((g1.vSelected[p]) && (g1.over(p)))
        g1.vDragged[p] = true;
  }
  if(mouseY > (height - 3 * bottomMargin)) {
    cheminDragged = true;
    float dx = mouseX - pmouseX;
    xCheminText[0] += dx;
  }
}
void mouseReleased() {
  if((mouseX < width - rightMargin) && (mouseX > leftMargin) && (mouseY < height - bottomMargin) && (mouseY > topMargin)) {
    if(g1.workingMethod == 0) {
      if((!fixBox) && (multiSelect == false) && (g1.getNbSelected() > 1))
        g1.makeNoSelected();
      if((fixBox) && (!draggBox)) {
        if(g1.gNoOver()) {
          fixBox = false;
          draggBox = false;
          deleteBox = true;
          g1.makeNoSelected();
        } else if(!multiSelect)
          for(int i = 0; i < g1.getOrder(); i++)
            if((g1.over(i) == true) && (g1.gNoSelected() == false))
              for(int p = 0; p < g1.getOrder(); p++)
                if((p != i) && (g1.vSelected[p] == true)) {
                  g1.gAddStroke(p, i);
                  vTranslate = new PVector[g1.getOrder()];
                  textfield pond = new textfield();
                  pond.setInitText(str(g1.getStrokePonderation(g1.gConnection.size() - 1)));
                  ponderation.add(pond);
                }
      } else if((fixBox) && (draggBox)) {
        startBoxPt[0] = mouseX - gWindowWidth / 2 - draggBoxTranslate.x;
        startBoxPt[1] = mouseY - gWindowHeight / 2 - draggBoxTranslate.y;
        endBoxPt[0] = draggBoxWidth + startBoxPt[0];
        endBoxPt[1] = draggBoxHeight + startBoxPt[1];
        draggBox = false;
        deleteBox = true;
      }
      if(drawBox) {
        drawBox = false;
        endBoxPt[0] = mouseX - gWindowWidth / 2;
        endBoxPt[1] = mouseY - gWindowHeight / 2;
        fixBox = true;
      }
      for(int p = 0; p < g1.getOrder(); p++)
        if(g1.vDragged[p] == true)
          g1.vDragged[p] = false;
      if((g1.gNoOver() == true) && (g1.gNoSelected() == true) && (fixBox == false) && (deleteBox == false) && (g1.gNoStrokeOver())) {
        g1.gAddVertex();
        g1.setConnectionList(g1.gConnection);
        g1.vSelected[g1.getOrder() - 1] = true;
        vTranslate = new PVector[g1.getOrder()];
      }
      deleteBox = false;
    } else if(g1.workingMethod == 1)
      for(int p = 0; p < g1.getOrder(); p++)
        if(g1.vDragged[p])
          g1.vDragged[p] = false;
    for(int p = 0; p < g1.gConnection.size(); p++)
      if(g1.strOver[p]) {
        if(!g1.strSelected[p]) {
          g1.makeNoStrokeSelected();
          g1.strSelected[p] = true;
        } else
          g1.strSelected[p] = false;
      }
  }
}
