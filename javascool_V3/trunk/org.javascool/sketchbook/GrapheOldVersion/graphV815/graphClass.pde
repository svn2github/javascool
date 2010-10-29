// definition of vertex and background color
color bGround = color(204,203,203); //color(255); //color(32,39,55);
color overVertexColor = color(86,115,44);
color showConnectionColor = color(20);
color selectedStrokeColor = color(0); //color(138,134,166);//color(168,110,255);
color draggedVertexColor = color(0,0,255,100);
color labelColor = color(100,100,100);
color strokePonderationColor = color(168,110,255);
color pathStrokeColor = color(135,88,204);
color Blue = color(105,127,191);
color isOnePathPossibleColor = color(24,49,82,200);
color pathVerticesColor = color(135,88,204);
color textInfoColor = color(108,115,60);
int gWindowWidth, gWindowHeight;
//some graph property
String[] graphTextInfo={
  "+ ordre","+ arêtes","+ orienté","+ pondéré","+ simple","+ complet","+ connexe"};
String[] strokeTextInfo={
  "+ arête","+ sommets","+ pondération"};
String[] cheminTextInfo = {
  "+ chemin", "+ taille", "+ longueur"
};
String[] cheminAleaInfo = {
  "+ Départ :", "+ Arrivée :", "+ Taille minimale :", "+ Taille maximale :", "+ Taille exacte :"
};
String[] savedPathInfo = {
  "chemin", "taille", "longueur"
};

float maxWidthInfoText = 0.;
float maxWidthInfoStrokeText = 0.;
float maxWidthCheminInfo = 0.;
float maxWidthAleaInfo = 0.;



class graph{
  float ratioRadius;
  float regularRadius = 250;
  float offsetLabel = 10;
  float gScale; // intensity of zoom in the draw() method.
  float[] r, probaVertexPotentiel; //rayon (ou presque  et variable) des sommets. probabilité du sommet dans un graphe probabiliste.
  float[][] strokeVertices;// list of vertices for drawing strokes between two connected nodes.
  int[] gVertexDegree, gVertexDegreeOut, gVertexDegreeIn;
  int gOrder, getStroke, workingMethod;
  int  arcResolutionLoop = 30;
  int strOverPrecision = 5;
  String displayType;
  ArrayList  gConnection, gVerticesCoords, gPonderation, vColor, isOnePathPossible, pathVertices, pathStrokes;
  boolean[] animVertexEnded, vSelected, vDragged, vLabel, vNew, strOver, strSelected, showConnection; //v means vertex, s means strokes. showConnection is for changing color on a vertex if associate stroke over.
  boolean gLabel, gPondLabel; // vertices' label or strokeLabel (ponderation)
  boolean oriented, simple, ponderate; // type of graph
  
  matrix gTransition; // mtrice de transition pour le graphe probabiliste

  // constructor-----------------------------------------------------------
  // default constructor--------------------------------------------

  // graph defined by order -------------------------------
  graph(int nbVertices){
    workingMethod = 0;
    gOrder = nbVertices;
    gVerticesCoords = new ArrayList();
    vColor = new ArrayList();
    isOnePathPossible = new ArrayList();
    pathVertices = new ArrayList();
    pathStrokes = new ArrayList();
    gVertexDegree = new int[gOrder];
    vSelected = new boolean[gOrder];
    probaVertexPotentiel = new float[gOrder];
    animVertexEnded = new boolean[gOrder];
    for(int k = 0; k < gOrder; k++){
     animVertexEnded[k] = false; 
    }
    for(int k=0;k<gOrder;k++){
      vSelected[k] = false;
    }
    vDragged = new boolean[gOrder];
    for(int k=0;k<gOrder;k++){
      vDragged[k] = false;
    }
    r = new float[gOrder];
    for(int k=0;k<gOrder;k++){
      r[k] = 0; 
    }
    vLabel = new boolean[gOrder];
    for(int p=0; p<gOrder; p++){
      vColor.add(Blue);
      isOnePathPossible.add(false);
      pathVertices.add(false);
    }
  }

  // make new path
  void makeNewPath(){
    isOnePathPossible = new ArrayList();
    pathVertices = new ArrayList();
    for(int p=0; p<gOrder; p++){
      isOnePathPossible.add(false);
      pathVertices.add(false);
    }
  }

  // chemin vide ?
  boolean isEmptyPath(){
    boolean test = true;
    int k = 0;
    while(k<pathVertices.size()){
      if(((Boolean) pathVertices.get(k)) == false){
        k +=1;
      }
      else{
        test = false;
        k = pathVertices.size();
      }
    }
    return test;
  }

  void setPathStrokes(int p, boolean isPathStrokes){
    pathStrokes.set(p, isPathStrokes);
  }

  // set and get some graph's definition------------------------------------------------------
  void setOriented(boolean type){
    oriented = type;
  }
  void setSimple(boolean test){
    simple = test;
  }
  void setPonderate(boolean test){
    ponderate = test;
  }
  void setdType(String type){
    displayType = type;
  }

  void setRatioRadius(float radius){
    ratioRadius = radius;
  }
  void setScale(float Scale){
    gScale = Scale;
  }
  void setOrder(int order){
    gOrder = order;
  }
  void setConnectionList(){
    gConnection = new ArrayList();
    if(ponderate){
      gPonderation = new ArrayList();
      for(int k=0;k<gConnection.size();k++){
        float[] u = {
          0.25                                };
        gPonderation.add(u); 
      }
    }
  }
  void setConnectionList(ArrayList connect){
    gConnection = connect;
    if(ponderate){
      gPonderation = new ArrayList();
      for(int k=0;k<gConnection.size();k++){
        float[] u = {
          0.25                                };
        gPonderation.add(u); 
      }
    }
    strOver = new boolean[gConnection.size()];
    strSelected = new boolean[gConnection.size()];

    if(oriented == false){
      for(int k=0;k<gOrder;k++){
        int degree = 0;
        for(int p=0;p<gConnection.size();p++){
          int[] couple = (int[])gConnection.get(p);
          if(((couple[0]==k)||(couple[1]==k))&&(couple[0]!=couple[1])){
            degree+=1;
          }
          else if(((couple[0]==k)||(couple[1]==k))&&(couple[0]==couple[1])){
            degree+=2;
          }
        }
        gVertexDegree[k] = degree;
      }
    }
    else if(oriented == true){
      gVertexDegreeOut = new int[gOrder];
      gVertexDegreeIn = new int[gOrder];
      for(int k=0;k<gOrder;k++){
        int degreeOut = 0;
        int degreeIn = 0;
        for(int p=0;p<gConnection.size();p++){
          int[] couple = (int[])gConnection.get(p);
          if(couple[0]==k){
            degreeOut+=1;
          }
          if(couple[1] == k){
            degreeIn +=1;
          } 
        }
        gVertexDegreeOut[k] = degreeOut;
        gVertexDegreeIn[k] = degreeIn;
        gVertexDegree[k] = degreeOut+degreeIn;
      }
    }
  }

  void setPonderation(float[] pond){
    if(pond.length != gConnection.size()){
      println("taille de la liste de pondération incorrecte");
    }
    else{
      for(int k=0; k<pond.length; k++){
        float[] inter = {
          pond[k]                                };
        gPonderation.add(inter);
      }
    }
  }
  void setStrokePonderation(int gStroke, float pond){
    float[] inter = {
      pond                };
    gPonderation.set(gStroke,inter);
  }

  float getStrokePonderation(int gStroke){
    float[] inter = (float[]) gPonderation.get(gStroke);
    float t = inter[0];
    return t;
  }

  void setVColor(int p, color Color){
    vColor.set(p,Color);
  }

  int getVColor(int p){
    Integer Color = (Integer) vColor.get(p);
    return Color.intValue();
  }  

  // is i in the connection list. If false, i is an isolated vertex
  boolean isConnectionContains(int i){
    boolean test=false;
    int k=0;
    while(k<gConnection.size()){
      int[] connect = (int[])gConnection.get(k);
      if((connect[0]==i)||(connect[1]==i)){
        test = true;
        k = gConnection.size();
      }
      else{
        k+=1;
      }
    }
    return test;
  }

  boolean getOriented(){
    return oriented;
  }
  boolean getSimple(){
    return simple;
  }
  boolean getPonderate(){
    return ponderate;
  }
  String getdisplayType(){
    return displayType;
  }
  int getOrder(){
    return gOrder;
  }
  float getScale(){
    return gScale;
  }
  int getNbSelected(){
    int nbVertexSelected = 0;
    for(int p=0;p<gOrder;p++){
      if(vSelected[p]==true){
        nbVertexSelected +=1;
      }
    }
    return nbVertexSelected;
  }
  //return a list of integer : label of selected vertices.
  int[] theSelected(){
    int[] liste = new int[0];
    for(int p=0;p<gOrder;p++){
      if(vSelected[p]){
        liste = append(liste,p); 
      }
    }
    return liste;
  }
  //some opetarion on graph------------------------------------------------------------------
  //add vertex
  void gAddVertex(){
    gOrder +=1;
    float[] coords = new float[2];
    coords[0] = (mouseX-gWindowWidth/2)/gScale;
    coords[1] = (mouseY-gWindowHeight/2)/gScale;
    vSelected = (boolean[]) append(vSelected,false);
    vDragged = (boolean[]) append(vDragged, false);
    pathVertices.add(false);
    gVertexDegree = new int[gOrder];
    gVertexDegreeOut = new int[gOrder];
    gVertexDegreeIn = new int[gOrder];
    r = append(r,1);
    if(gLabel==true){
      vLabel = (boolean[]) append(vLabel, true);
    }
    else{
      vLabel = (boolean[]) append(vLabel, false);
    }
    vColor.add(Blue);
    isOnePathPossible.add(false);
    gVerticesCoords.add(coords);
    probaVertexPotentiel = new float[gOrder];
    animVertexEnded = new boolean[gOrder];
  }

  // remove vertex
  void gRemoveVertex(int i){
    pathVertices.remove(i);
    while(isConnectionContains(i) == true){
      for(int k=0; k<gConnection.size();k++){
        int[] connect = (int[])gConnection.get(k);
        if((connect[0]==i)||(connect[1]==i)){
          gConnection.remove(k);
          pathStrokes.remove(k);
        }

      }
    }
    float[] rInter = new float[gOrder];
    for(int p=0;p<gOrder;p++){
      rInter[p] = r[p]; 
    }
    r = new float[gOrder-1];
    for(int p=0;p<r.length;p++){
      if(p<i){
        r[p] = rInter[p];
      }
      else if(p>=i){
        r[p] = rInter[p+1];
      }
    }
    for(int p=0;p<gConnection.size();p++){
      int[] connect2 = (int[])gConnection.get(p);
      if((connect2[0]<i)&&(connect2[1]>i)){
        connect2[1] -=1;
      }
      else if((connect2[0]>i)&&(connect2[1]<i)){
        connect2[0] -=1;
      }
      else if((connect2[0]>i)&&(connect2[1]>i)){
        connect2[0] -=1;
        connect2[1] -=1;
      }
    }
    setConnectionList(gConnection);
    gVerticesCoords.remove(i);
    gOrder -=1;
    if(i==0){
      boolean[] list1 = subset(vSelected,1,vSelected.length-1);
      vSelected = new boolean[gOrder];
      vSelected = list1;
    }
    else{
      boolean[] list1 = subset(vSelected,0,i);
      boolean[] list2 = subset(vSelected,i+1,vSelected.length-i-1);
      vSelected = new boolean[gOrder];
      vSelected = splice(list1,list2,list1.length);
    }
    vDragged = new boolean[gOrder];
    vLabel = new boolean[gOrder];
    vColor.remove(i);
    probaVertexPotentiel = new float[gOrder];
    animVertexEnded = new boolean[gOrder];
  } 


  //add edge between two vertices
  void gAddStroke(int i, int j){
    if(isConnected(i,j)){
      println("error");
    }
    else{
      int[] newStroke = new int[2];
      newStroke[0] = i;
      newStroke[1] = j;
      gConnection.add(newStroke);
      setConnectionList(gConnection);
      pathStrokes.add(false);
    }
    probaVertexPotentiel = new float[gOrder];
    animVertexEnded = new boolean[gOrder];
  }
  void gRemoveStroke(int i){
    gPonderation.remove(i);
    gConnection.remove(i);
    setConnectionList(gConnection);
    makeNoStrokeSelected();
    pathStrokes.remove(i);
    probaVertexPotentiel = new float[gOrder];
    animVertexEnded = new boolean[gOrder];
  }
  // vetex's property------------------------------------------------------------------------
  
  // sommets connectés ?
  boolean isConnected(int i, int j){
    boolean test = false;
    int nb = 0;
    while(nb<gConnection.size()){
      int[]  couple = (int[])gConnection.get(nb);
      if(oriented == false){
        if(((couple[0]==i)&&(couple[1]==j))||((couple[0]==j)&&(couple[1]==i))){
          test = true;
          getStroke = nb;
          nb = gConnection.size();
        }
        else{
          nb+=1;
        }
      }
      if(oriented == true){
        if((couple[0]==i)&&(couple[1]==j)){
          test = true;
          getStroke = nb;
          nb = gConnection.size();
        }
        else{
          nb+=1;
        }
      }
    }
    return test; 
  }

  // donne l'index de l'arête entre deux sommets connus (donne -1 si non-connecté)
  int getTheStroke(int i, int j){
    int test = 0;
    if(!isConnected(i,j)){
      test = -1;
    }
    else if(isConnected(i,j)){
      test = getStroke;
    }
    return test;
  }

  //get vertex degree
  int getVertexDegree(int indice){
    return gVertexDegree[indice]; 
  }
  int getVertexDegreeOut(int indice){
    return gVertexDegreeOut[indice]; 
  }
  int getVertexDegreeIn(int indice){
    return gVertexDegreeIn[indice]; 
  }

  // get vertex coords
  float[] getCoords(int indice){
    float[] coords = new float[2];
    coords = (float[]) gVerticesCoords.get(indice);
    return coords;
  }
  //set coords for one vertex
  void setVertexCoords(int indice,float x, float y){
    float[] Coords = new float[2];
    Coords[0] = x;
    Coords[1] = y;
    gVerticesCoords.set(indice,Coords);
  }
  void setVertexCoords(String dType){
    if(dType=="regular"){
      gVerticesCoords = new ArrayList();
      for(int k=0;k<gOrder;k++){
        float[] coords = new float[2];
        coords[0] = regularRadius*cos(2*k*PI/gOrder);
        coords[1] = regularRadius*sin(2*k*PI/gOrder);
        gVerticesCoords.add(coords);
      }
      displayType = "regular";
    }
  }

  // potentiel probabiliste d'un sommet
  void setProbaVertexPotentiel(int index, float valeur){
    probaVertexPotentiel[index] = valeur;
  }


  // vertex State : over, selected, noSelected, dragged-------------------------------------------------------------
  // One vertex over.
  boolean over(int i) {
    boolean test = false;
    float X = mouseX-gWindowWidth/2;
    float Y = mouseY-gWindowHeight/2;
    float[] coords = new float[2];
    coords = (float[])gVerticesCoords.get(i);
    if(dist(X/gScale,Y/gScale,coords[0],coords[1])<=ratioRadius*(gVertexDegree[i]+1)/2){
      test = true; 
    }
    else{
      test = false;
    }
    return test;
  }

  // no vertex over = graph noOver
  boolean gNoOver(){
    boolean test = true;
    int vTest = 0;
    for(int p=0;p<gOrder;p++){
      if(over(p)==false){
        vTest+=1;
      }
    }
    if(vTest == gOrder){
      test = true; 
    }
    else{
      test = false;
    }
    return test;
  }
  // sommets animés ?
  boolean isThereVertexAnim(){
   boolean test = false;
  int k = 0;
 while((test == false)&&(k < gOrder-1)){
  if(!animVertexEnded[k]){
    test = true;
  }else{
   k +=1; 
  }
 }
 return test;
  }
  // make gNoSelected
  void makeNoSelected(){
    for(int k=0;k<gOrder;k++){
      vSelected[k] = false;
    }
  }
  //no vertex selected ?
  boolean gNoSelected(){
    boolean test = true;
    int vTest=0;
    for(int p=0;p<gOrder;p++){
      if(vSelected[p]==false){
        vTest+=1;
      }
    }
    if(vTest == gOrder){
      test = true; 
    }
    else{
      test = false;
    }
    return test;
  }

  boolean isCompleted(){
    boolean test = false;
    if((!oriented)&&(simple)&&(gConnection.size()== gOrder*(gOrder-1)/2)){
      test = true;
    }
    else if((oriented)&&(simple)&&(gConnection.size()== gOrder*(gOrder-1))){
      test = true;
    }
    else if((oriented)&&(!simple)&&(gConnection.size()==pow(gOrder,2))){
      test = true;
    }
    else if((!oriented)&&(!simple)&&(gConnection.size()==gOrder*(gOrder+1)/2)){
      test = true;
    }
    return test;
  }

  //no stroke over ?
  boolean gNoStrokeOver(){
    boolean test = true;
    int k = 0;
    while(k < gConnection.size()){
      if(strOver[k]){
        test = false;
        k = gConnection.size();
      }
      else if(!strOver[k]){
        k +=1;
      }
    }
    return test;
  }
  //no stroke selected ?
  boolean gNoStrokeSelected(){
    boolean test = true;
    int k = 0;
    while(k < gConnection.size()){
      if(strSelected[k]){
        test = false;
        k = gConnection.size();
      }
      else if(!strSelected[k]){
        k +=1;
      }
    }
    return test;
  }

  //make no stroke selected
  void makeNoStrokeSelected(){
    for(int p= 0;p<gConnection.size();p++){
      strSelected[p] = false;
    }
  }

  //get selected stroke
  int getSelectedStroke(){
    int test=-1;
    int k =0;
    while(k < gConnection.size()){
      if(strSelected[k]){
        test = k;
        k = gConnection.size();
      }
      else if(!strSelected[k]){
        k +=1;
      }
    }
    return test;
  }

  //donne la liste des sommets pouvant être atteints à partir d'un sommet initial
  int[] thePossible(int init){
    int[] The = new int[0];
    for(int k=0;k<gConnection.size();k++){
      if(isConnected(init,k)){
        The = append(The, k);

      }
    }
    return The; 
  }

  // matrice de transition en proba

  matrix makeTransitionMatrix(){
    matrix gTransition = new matrix(gOrder, gOrder);
    for(int i = 0; i < gOrder; i++){
      for(int j = 0; j < gOrder; j++){
        if(isConnected(i,j)){
          gTransition.setCoeff(i,j,getStrokePonderation(getTheStroke(i,j)));
        }
        else {
          gTransition.setCoeff(i,j,0.);
        }
      }
    }
    return gTransition;
  }

  // display graph (different mode)-----------------------------------------------------------
  void display(){
    scale(gScale); 
    showConnection = new boolean[gOrder];

    //affichage des sommets

    for(int p=0;p<gOrder;p++){
      float[] coords = (float[])gVerticesCoords.get(p);
      if(over(p)){
        setVColor(p,overVertexColor);
      }
      else if(showConnection[p]){
        setVColor(p,showConnectionColor);
      }
      else if((Boolean) pathVertices.get(p)){
        setVColor(p,pathVerticesColor);
      }
      else if((Boolean) isOnePathPossible.get(p)){
        setVColor(p,isOnePathPossibleColor);
      }
      else{
        vColor.set(p,Blue);
      }
      noStroke();
      fill(getVColor(p));

      // le rayon dépend du mode de travail : si workingMethod = 0, 1 ou 2, il dépend du degrée.
      // Si workingMethod = 3, il dépend du potentiel probabiliste
      if(workingMethod != 3){
        if(gVertexDegree[p]!=0){
          if(r[p]<gVertexDegree[p]-0.1){
            ellipse(coords[0],coords[1],ratioRadius*(r[p]+1),ratioRadius*(r[p]+1));
            r[p] += 0.2;
          } 
          else if(r[p]>gVertexDegree[p]+0.1){
            ellipse(coords[0],coords[1],ratioRadius*(r[p]+1),ratioRadius*(r[p]+1));
            r[p] -= 0.2;
          }
          else{
            ellipse(coords[0],coords[1],ratioRadius*(gVertexDegree[p]+1),ratioRadius*(gVertexDegree[p]+1));
            r[p] = gVertexDegree[p];
          }
          if(((vSelected[p])&&(workingMethod == 0))||(((Boolean) pathVertices.get(p))&&(workingMethod == 1))){
            noStroke();
            fill(255);
            ellipse(coords[0],coords[1],ratioRadius*gVertexDegree[p]/2,ratioRadius*gVertexDegree[p]/2);
          }

          if((Boolean) isOnePathPossible.get(p)){
            noFill();
            stroke(Blue);
            strokeWeight(1);
            ellipse(coords[0],coords[1],ratioRadius*(r[p]+2),ratioRadius*(r[p]+2));
          }
        }
        else{
          ellipse(coords[0],coords[1],ratioRadius,ratioRadius);
          if(((vSelected[p])&&(workingMethod == 0))||(((Boolean) pathVertices.get(p))&&(workingMethod == 1))){
            noStroke();
            fill(255);
            ellipse(coords[0],coords[1],ratioRadius/2,ratioRadius/2);
          }
        }
      }
      else{ // graphe probabiliste
        if(r[p]<map(probaVertexPotentiel[p],0,1,2,10) - 0.1){
          ellipse(coords[0],coords[1],ratioRadius*(r[p]+1),ratioRadius*(r[p]+1));
          r[p] += 0.2;
          animVertexEnded[p] = false;
        } 
        else if(r[p]>map(probaVertexPotentiel[p],0,1,2,10)+0.1){
          ellipse(coords[0],coords[1],ratioRadius*(r[p]+1),ratioRadius*(r[p]+1));
          r[p] -= 0.2;
          animVertexEnded[p] = false;
        }
        else{
          ellipse(coords[0],coords[1],ratioRadius*(map(probaVertexPotentiel[p],0,1,2,10)+1),ratioRadius*(map(probaVertexPotentiel[p],0,1,2,10)+1));
          r[p] = map(probaVertexPotentiel[p],0,1,2,10);
          animVertexEnded[p] = true;
        }
      }

      if(vLabel[p] == true){
        float ratioText = 1. + 3*ratioRadius*(r[p]+1)/(4*dist(coords[0],coords[1],gCenter(gVerticesCoords)[0],gCenter(gVerticesCoords)[1]));
        fill(labelColor);
        if(coords[0]<=0){
          if(coords[1]<=0){
            textAlign(RIGHT,BOTTOM);
          }
          else{
            textAlign(RIGHT,TOP);
          }
        }
        else{
          if(coords[1]<=0){
            textAlign(TOP,BOTTOM);
          }
          else{
            textAlign(TOP,TOP);
          }
        }
        text(str(p),ratioText*(coords[0]-gCenter(gVerticesCoords)[0]),ratioText*(coords[1]-gCenter(gVerticesCoords)[1]));

      }
    }

    //affichage des aretes.

    if(gConnection.size()>0){
      for(int p=0;p<gConnection.size();p++){
        int[] lineConnect = (int[]) gConnection.get(p);
        float[] coords1 = (float[]) gVerticesCoords.get(lineConnect[0]);
        float[] coords2 = (float[]) gVerticesCoords.get(lineConnect[1]);
        PVector AB = new PVector(coords1[0]-coords2[0],coords1[1]-coords2[1]);
        AB.normalize();
        float xA = -ratioRadius/2*(r[lineConnect[0]]+1)*AB.x+coords1[0];
        float yA = -ratioRadius/2*(r[lineConnect[0]]+1)*AB.y+coords1[1];
        float xB = ratioRadius/2*(r[lineConnect[1]]+1)*AB.x+coords2[0];
        float yB = ratioRadius/2*(r[lineConnect[1]]+1)*AB.y+coords2[1];
        fill(75);
        stroke(75);
        if(lineConnect[0]==lineConnect[1]){
          noFill();
          strokeVertices = makeLoop(gCenter(gVerticesCoords),coords1,ratioRadius*(r[lineConnect[0]]+1)/2,ratioRadius*(r[lineConnect[0]]+2)/2,arcResolutionLoop);
          if((onCurve((mouseX-gWindowWidth/2)/gScale,(mouseY-gWindowHeight/2)/gScale,strokeVertices,strOverPrecision))&&(gNoOver())){
            strOver[p] = true;
            showConnection[lineConnect[0]] = true;
            vLabel[lineConnect[0]] = true;
            strokeWeight(2.25);
          }
          else{
            strOver[p] = false;
            strokeWeight(1);
          }
          if((Boolean) pathStrokes.get(p)){
            strokeWeight(3);
            stroke(pathStrokeColor);
          }
          if(strSelected[p]){
            strokeWeight(3);
          }
          beginShape();
          for (int i=0; i<strokeVertices.length; i++) {
            vertex(strokeVertices[i][0],strokeVertices[i][1]);
          }
          endShape();
          fill(75);
          arrowhead(strokeVertices,1, 10, 1);
        }
        else if(oriented == false){
          strokeVertices = flux_arc(xA,yA,xB,yB,0,2);
          if((onCurve((mouseX-gWindowWidth/2)/gScale,(mouseY-gWindowHeight/2)/gScale,strokeVertices,strOverPrecision))&&(gNoOver())){
            showConnection[lineConnect[0]] = true;
            showConnection[lineConnect[1]] = true;
            strokeWeight(2.25);
            strOver[p] = true;
            vLabel[lineConnect[0]] = true;
            vLabel[lineConnect[1]] = true;
          }
          else{
            strokeWeight(1);
            strOver[p] = false;
          }
          if((Boolean) pathStrokes.get(p)){
            strokeWeight(3);
            stroke(pathStrokeColor);
          }
          if(strSelected[p]){
            stroke(selectedStrokeColor);
            strokeWeight(2.25);
            showConnection[lineConnect[0]] = true;
            showConnection[lineConnect[1]] = true;
          }
          beginShape();
          for (int i=0; i<2; i++) {
            vertex(strokeVertices[i][0],strokeVertices[i][1]);
          }
          endShape();
        }
        else if(oriented == true){
          int testDoubleConnection = 0;
          int k = 0;
          while((testDoubleConnection<1)&&(k<gConnection.size())){
            int[] lineConnect2 = (int[]) gConnection.get(k);
            if((lineConnect[0]==lineConnect2[1])&&(lineConnect[1]==lineConnect2[0])){
              testDoubleConnection +=1;
            }
            else{
              k+=1;
            }
          }
          if(testDoubleConnection == 0){
            strokeVertices = flux_arc(xA,yA,xB,yB,0,2);
            if((onCurve((mouseX-gWindowWidth/2)/gScale,(mouseY-gWindowHeight/2)/gScale,strokeVertices,strOverPrecision))&&(gNoOver())){
              showConnection[lineConnect[0]] = true;
              showConnection[lineConnect[1]] = true;
              strOver[p] = true;
              strokeWeight(2.25);
            }
            else{
              strOver[p] = false;
              strokeWeight(1);
            }
            if((Boolean) pathStrokes.get(p)){
              strokeWeight(3);
              stroke(pathStrokeColor);
            }

            if(strSelected[p]){
              stroke(selectedStrokeColor);
              strokeWeight(2.25);
              showConnection[lineConnect[0]] = true;
              showConnection[lineConnect[1]] = true;
            }
            beginShape();
            for (int i=0; i<2; i++) {
              vertex(strokeVertices[i][0],strokeVertices[i][1]);
            }
            endShape();
            arrowhead(strokeVertices,1, 10, 1);
          }
          else{
            strokeVertices = flux_arc(xA,yA,xB,yB,0.75,20);
            if((onCurve((mouseX-gWindowWidth/2)/gScale,(mouseY-gWindowHeight/2)/gScale, strokeVertices, strOverPrecision))&&(gNoOver())){
              showConnection[lineConnect[0]] = true;
              showConnection[lineConnect[1]] = true;
              strokeWeight(2.25);
              strOver[p] = true;
            }
            else{
              strokeWeight(1);
              strOver[p] = false;
            }
            if((Boolean) pathStrokes.get(p)){
              strokeWeight(3);
              stroke(pathStrokeColor);
            }
            if(strSelected[p]){
              stroke(selectedStrokeColor);
              strokeWeight(2.25);
              showConnection[lineConnect[0]] = true;
              showConnection[lineConnect[1]] = true;
            }
            beginShape();
            noFill();
            for (int i=0; i<20; i++) {
              vertex(strokeVertices[i][0],strokeVertices[i][1]);
            }
            endShape();
            fill(75);
            arrowhead(strokeVertices,1, 10, 1);
          }
        }

        if(gPondLabel){
          fill(strokePonderationColor);
          labelStroke(strokeVertices[strokeVertices.length/2-1], strokeVertices[strokeVertices.length/2], offsetLabel,  str(getStrokePonderation(p)));
        }
      }
    }
  }

  // display options--------------------------------------------------------------------------------------

  // set label display for one vertex
  void setVertexLabel(int i, boolean test){
    vLabel[i] = test;
  }

  void setGDisplayLabel(boolean test){
    gLabel = test;
    if(test == true){
      for(int k=0;k<gOrder;k++){
        vLabel[k] = true;
      }
    }
    else{
      for(int k=0;k<gOrder;k++){
        vLabel[k] = false;
      } 
    }
  }

  boolean getGLabel(){
    return gLabel;
  }

  //end of class---------------------------------------------------------------------------------------------------------
}

