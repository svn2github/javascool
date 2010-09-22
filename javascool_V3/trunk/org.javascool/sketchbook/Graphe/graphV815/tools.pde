// nombre entouré (pour sauvegarde des chemins)
void circleNumber(int nb, float xCenter, float yCenter, float rayon, color filled, float lgTraitH, float lgTraitV){
  fill(filled);
  pushMatrix();
  translate(xCenter, yCenter);
  stroke(20);
  ellipse(0, 0, 2*rayon,2*rayon);
  line(rayon,0, rayon + lgTraitH,0);
  line(rayon + lgTraitH, -rayon, rayon + lgTraitH, lgTraitV);
  textAlign(CENTER,CENTER);
  fill(0);
  text(str(nb), 0, -rayon/4);
  popMatrix();
}


//centre de gravité du graphe.
float[] gCenter(ArrayList vertices){
  float[] gcenter = new float[2];
  float[] center = new float[2];
  center[0] = 0; 
  center[1] = 0;
  for(int k=0;k<vertices.size();k++){
    float[] Vertex = (float[])vertices.get(k);
    center[0]+= Vertex[0];
    center[1] += Vertex[1];
  }
  gcenter[0] = (1/vertices.size())*center[0];
  gcenter[1] = (1/vertices.size())*center[1];
  return gcenter;
}

// égalité entre deux couples d'entiers-----------------------------------------
boolean isEqual(int[] c1, int[] c2, boolean orientedType){
  boolean test = false;
  if(orientedType == true){
    if((c1[0]==c2[0])&&(c1[1]==c2[1])){
      test = true;
    }
    else{
      test = false;
    }
  }
  else if(orientedType == false){
    if(((c1[0]==c2[0])&&(c1[1]==c2[1]))||((c1[0]==c2[1])&&(c1[1]==c2[0]))){
      test = true;
    }
    else{
      test = false;
    }
  }
  return test;
}

//return list of random couple integer from initial integer list-------------------
ArrayList coupleList(int initListSize, int coupleNb, boolean type, boolean simpleOrNot){
  ArrayList liste = new ArrayList();
  if(coupleNb>0){
    if(simpleOrNot == true){
      while(liste.size()<1){
        int[] firstCouple = new int[2];
        firstCouple[0] = floor(random(initListSize));
        firstCouple[1] = floor(random(initListSize));
        if(firstCouple[0]!=firstCouple[1]){
          liste.add(firstCouple);
        }

      }
      while(liste.size()<coupleNb){
        int test2=0;
        int counter = 0;
        int[] segm = new int[2];
        segm[0] = floor(random(initListSize));
        segm[1] = floor(random(initListSize));

        while(counter<liste.size()){
          if((isEqual(segm,(int[])liste.get(counter),type)==false)&&(segm[0]!=segm[1])){
            test2+=1;
            counter+=1;
          }
          else{
            counter = liste.size();
          }
        }
        if(test2 == liste.size()){
          liste.add(segm);
        }
      }
    }
    else if(simpleOrNot == false){
      int[] firstCouple = new int[2];
      firstCouple[0] = floor(random(initListSize));
      firstCouple[1] = floor(random(initListSize));
      liste.add(firstCouple);

      while(liste.size()<coupleNb){
        int test2=0;
        int counter = 0;
        int[] segm = new int[2];
        segm[0] = floor(random(initListSize));
        segm[1] = floor(random(initListSize));

        while(counter<liste.size()){
          if(isEqual(segm,(int[])liste.get(counter),type)==false){
            test2+=1;
            counter+=1;
          }
          else{
            counter = liste.size();
          }
        }
        if(test2 == liste.size()){
          liste.add(segm);
        }
      }
    }
  }
  return liste;
}
//return list of all possible couple
ArrayList completeList(int initListSize, boolean orientedOrNot, boolean simpleOrNot){
  ArrayList coupleList = new ArrayList();
  if(!orientedOrNot){
    if(simpleOrNot){
      for(int p=0;p<initListSize; p++){
        for(int k=p+1; k< initListSize;k++){
          int[] segm = {
            p,k                                        };
          coupleList.add(segm);
        }
      }
    }
    else{
      for(int p=0;p<initListSize; p++){
        for(int k=p; k< initListSize;k++){
          int[] segm = {
            p,k                                        };
          coupleList.add(segm);
        }
      }
    }
  }
  else if(orientedOrNot){
    if(simpleOrNot){
      for(int p=0;p<initListSize; p++){
        for(int k = 0;k<initListSize; k++){
          if(k!= p){
            int[] segm = {
              p,k                                    };
            coupleList.add(segm);
          }
        }
      } 
    }
    else if(!simpleOrNot){
      for(int p=0;p<initListSize; p++){
        for(int k = 0;k<initListSize; k++){
          int[] segm = {
            p,k                              };
          coupleList.add(segm);
        }
      }
    }
  }
  return coupleList;
}

//go from A (float[]) to B (float[]) straight away
float[] AtoB(float[] initPt, float[] endPt, float t){
  float[] newCoords =new float[2];
  for(int p=0;p<2;p++){
    newCoords[p] = (1-t)*initPt[p]+t*endPt[p];
  }
  return newCoords;
}

//go x_i (float) to x_f (float)
float xToy(float x_i, float x_f, float t){
  return  (1-t)*x_i+t*x_f;
}

//go from A (int[]) to B (int[]) straight away
float[] AtoB(int[] initPt, int[] endPt, float t){
  float[] newCoords =new float[2];
  for(int p=0;p<2;p++){
    newCoords[p] = (1-t)*initPt[p]+t*endPt[p];
  }
  return newCoords;
}


// label ("string") for stroke
void labelStroke(float[] A, float[] B, float offsetLabel, String label){
  float[] K = new float[2];
  float[] ponde = new float[2];
  K[0] = (A[0] +B[0])/2;
  K[1] = (A[1] +B[1])/2;  
  PVector u = new PVector(B[0] - A[0],B[1]-A[1]);
  if(u.x != 0){
    float Alpha = sqrt(1+pow(u.y/u.x,2));
    if(u.x>0){
      textAlign(CENTER,BOTTOM);
      ponde[1] = K[1] - offsetLabel/Alpha;
      ponde[0] = K[0] + (u.y/u.x)*offsetLabel/Alpha;
      PVector n = new PVector(ponde[0]-K[0],ponde[1]-K[1]);
      pushMatrix();
      translate(ponde[0],ponde[1]);
      if(u.y<0){
        rotate(-PVector.angleBetween(n,new PVector(0,-1)));
      }
      else if(u.y>0){
        rotate(PVector.angleBetween(n,new PVector(0,-1)));
      }
      text(label,0,0);
      popMatrix();
    }
    else if(u.x<0){
      textAlign(CENTER,TOP);
      ponde[1] = K[1] + offsetLabel/Alpha;
      ponde[0] = K[0] - (u.y/u.x)*offsetLabel/Alpha;
      PVector n = new PVector(ponde[0]-K[0],ponde[1]-K[1]);
      pushMatrix();
      translate(ponde[0],ponde[1]);
      if(u.y<0){
        rotate(PI-PVector.angleBetween(n,new PVector(0,-1)));
      }
      else if(u.y>0){
        rotate(PI+PVector.angleBetween(n,new PVector(0,-1)));
      }
      text(label,0,0);
      popMatrix();
    }  


  }
  else if(u.x == 0){
    if(u.y<0){
      ponde[1] = K[1];
      ponde[0] = K[0] -offsetLabel;
      PVector n = new PVector(ponde[0]-K[0],ponde[1]-K[1]);
      pushMatrix();
      translate(ponde[0],ponde[1]);
      textAlign(CENTER,BOTTOM);
      rotate(-PVector.angleBetween(n,new PVector(0,-1)));
      text(label,0,0);
      popMatrix();
    }
    else if(u.y>0){
      ponde[1] = K[1];
      ponde[0] = K[0] +offsetLabel;
      PVector n = new PVector(ponde[0]-K[0],ponde[1]-K[1]);
      pushMatrix();
      translate(ponde[0],ponde[1]);
      textAlign(CENTER,BOTTOM);
      rotate(PVector.angleBetween(n,new PVector(0,-1)));
      text(label,0,0);
      popMatrix();
    }
  }
}


// point on a segment [AB] : M(X,Y) on (AB) iff d(M,(AB)) = 0. (A, B different)
boolean onSegment(float X, float Y, float[] A, float[] B, float precision){
  boolean test = false;
  float d = abs((A[1]-B[1])*(X-A[0])+(B[0]-A[0])*(Y-A[1]))/sqrt(pow(A[1]-B[1],2)+pow(B[0]-A[0],2));
  if((X>=min(A[0],B[0])-precision)&&(X<=max(A[0],B[0])+precision)&&(Y>=min(A[1],B[1])-precision)&&(Y<=max(A[1],B[1])+precision)&&(d<precision)){
    test = true;
  }
  else{
    test = false;
  }
  return test;
}
//point on a curve : list of point connectd by segment.
boolean onCurve(float X, float Y, float[][] ptList, float precision){
  boolean test = false;
  int nb = 0;
  for(int p=0;p<ptList.length-1;p++){
    if(onSegment(X,Y,ptList[p],ptList[p+1],precision)){
      nb +=1;
    }
  }
  if(nb == 0){
    test =false;
  }
  else{
    test = true;
  }
  return test;
}
//rotation R(omega, alpha). return image as float[].
float[] rotation(float[] center, float angle, float[] initPt){
  float[] imagePt = new float[2];
  imagePt[0] = cos(angle)*(initPt[0]-center[0])-sin(angle)*(initPt[1]-center[1])+center[0];
  imagePt[1] = cos(angle)*(initPt[1]-center[1])+sin(angle)*(initPt[0]-center[0])+center[1];
  return imagePt;
}

//makeLoopArrow : find intersection between two very particular circle. return vertices of loop (float[])
float[][] makeLoop(float[] axisCenter, float[] O1, float R1, float R2,int resolution){
  PVector u = new PVector(O1[0] - axisCenter[0],O1[1] - axisCenter[1]); 
  u.normalize();
  float O1H = R1 - pow(R2,2)/(2*R1);
  float[] H = new float[2];
  H[0] = O1H * u.x + O1[0];
  H[1] = O1H * u.y + O1[1];

  PVector n = new PVector(u.y, - u.x);
  float h = sqrt(pow(R1,2)-pow(O1H,2));
  float[][] K = new float[2][2];
  K[0][0] = h * n.x + H[0];
  K[0][1] = h * n.y + H[1];
  K[1][0] = -h * n.x + H[0];
  K[1][1] = -h * n.y + H[1];
  float[][] vertices = new float[resolution][2];
  float[] O2 = new float[2]; //center of loop (!! O1O2 = R1)
  O2[0] = R1 * u.x + O1[0];
  O2[1] = R1 * u.y + O1[1];
  float Alpha1 = asin(h/R2);
  float Alpha = 2*(PI- Alpha1)/(resolution-1);
  for(int p=0;p<resolution;p++){
    vertices[p] = rotation(O2,p*Alpha,K[0]);
  }
  return vertices;
}

// construction d'un chemin aléatoire à partir d'un graphe existant
void makeRandomPath(graph g,int depart, int arrivee, int tailleMin, int tailleMax, int tailleExacte){
  for(int k = 0; k < g.gOrder; k++){
    g.pathVertices.set(k,false);
  }
  int[] path = new int[1];
  path[0] = depart;
  while(path[path.length-1] != arrivee){
   int numAuHasard = floor(random(g.thePossible(path[path.length - 1]).length));
   path = append(path, g.thePossible(path[path.length - 1])[numAuHasard]); 
  }
  for(int p = 0;p<path.length; p++){
  print(path[p]);
  }
  /*
  for(int p = 0; p<path.length; p++){
   g.pathVertices.set(path[p],true); 
  }*/
  println("fini");
}


