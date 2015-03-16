public class MacrosChiffre {
  private MacrosChiffre() {}

  // Pour l'activité 2
  public static char[] chaineVersLettres(String s) {
    return s.toCharArray();
  }
  public static String lettresVersChaine(char[] t) {
    return new String(t);
  }
  // Pour l'activité 3
  public static String nombreVersChaine(Chiffre[] n) {
    String res = "";
    for(Chiffre c : n)
      res = res + c;
    return res;
  }
  public static Chiffre ZERO = Chiffre.ZERO;
  public static Chiffre UN = Chiffre.UN;
  public static Chiffre DEUX = Chiffre.DEUX;
  public static Chiffre TROIS = Chiffre.TROIS;
  public static Chiffre QUATRE = Chiffre.QUATRE;
  public static Chiffre CINQ = Chiffre.CINQ;
  public static Chiffre SIX = Chiffre.SIX;
  public static Chiffre SEPT = Chiffre.SEPT;
  public static Chiffre HUIT = Chiffre.HUIT;
  public static Chiffre NEUF = Chiffre.NEUF;
  public static Chiffre unite(NombreA2Chiffres n) {
    return NombreA2Chiffres.unite(n);
  }
  public static Chiffre dizaine(NombreA2Chiffres n) {
    return NombreA2Chiffres.dizaine(n);
  }
  public static boolean estPlusPetit(NombreA2Chiffres x, NombreA2Chiffres y) {
    return NombreA2Chiffres.estPlusPetit(x, y);
  }
  public static NombreA2Chiffres nombre(Chiffre x, Chiffre y) {
    return NombreA2Chiffres.nombre(x, y);
  }
  public static NombreA2Chiffres plus(NombreA2Chiffres x, NombreA2Chiffres y) {
    return NombreA2Chiffres.plus(x, y);
  }
  public static NombreA2Chiffres moins(NombreA2Chiffres x, NombreA2Chiffres y) {
    return NombreA2Chiffres.moins(x, y);
  }
  public static NombreA2Chiffres mult(NombreA2Chiffres x, NombreA2Chiffres y) {
    return NombreA2Chiffres.mult(x, y);
  }
}
