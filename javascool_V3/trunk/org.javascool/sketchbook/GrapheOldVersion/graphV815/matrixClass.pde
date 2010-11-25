class matrix {
  int rDim, cDim; // row and column dimension;
  float[][] tableau; // la matrice

  matrix() {
    tableau = new float[rDim][cDim];
  }
  matrix(int r, int c) {
    rDim = r;
    cDim = c;
    tableau = new float[rDim][cDim];
  }

  void setCoeff(int i, int j, float valeur) {
    tableau[i][j] = valeur;
  }
  float getCoeff(int i, int j) {
    return tableau[i][j];
  }
  int getDimR() {
    return rDim;
  }
  int getDimC() {
    return cDim;
  }
  float[] getRow(int i) {
    return tableau[i];
  }
  float[] getColumn(int j) {
    float[] colonne = new float[getDimR()];
    for(int i = 0; i < getDimR(); i++)
      colonne[i] = tableau[i][j];
    return colonne;
  }
}

// opérations matricielles
// somme de 2 matrices
matrix add(matrix A, matrix B) {
  matrix somme = new matrix(A.getDimR(), A.getDimC());
  for(int i = 0; i < somme.getDimR(); i++)
    for(int j = 0; j < somme.getDimC(); j++)
      somme.setCoeff(i, j, A.getCoeff(i, j) + B.getCoeff(i, j));
  return somme;
}
// produit de 2 matrices
matrix mul(matrix A, matrix B) {
  matrix C = new matrix(A.getDimR(), B.getDimC());
  for(int i = 0; i < A.getDimR(); i++)
    for(int j = 0; j < B.getDimC(); j++) {
      float coeff = 0.;
      for(int k = 0; k < A.getDimC(); k++)
        coeff += A.getCoeff(i, k) * B.getCoeff(k, j);
      C.setCoeff(i, j, coeff);
    }
  return C;
}
// puissance n d'une matrice
matrix mPow(matrix A, int n) {
  matrix C = A;
  int p = 0;
  while(p < n - 1) {
    C = mul(C, A);
    p += 1;
  }
  return C;
}
