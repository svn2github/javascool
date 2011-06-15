package proglet.exosdemaths;

public class NombreA2Chiffres {
  private Chiffre unit;
  private Chiffre ten;
  private NombreA2Chiffres(Chiffre _ten, Chiffre _unit) {
    unit = _unit;
    ten = _ten;
  }
  static NombreA2Chiffres nombre(Chiffre dizaine, Chiffre unite) {
    return new NombreA2Chiffres(dizaine, unite);
  }
  static Chiffre unite(NombreA2Chiffres n) {
    return n.unit;
  }
  static Chiffre dizaine(NombreA2Chiffres n) {
    return n.ten;
  }
  static private NombreA2Chiffres ofInt(int i) {
    return new NombreA2Chiffres(Chiffre.ofInt(i / 10), Chiffre.ofInt(i % 10));
  }
  private int value() {
    return ten.value * 10 + unit.value;
  }
  public String toString() {
    return ten.toString() + unit.toString();
  }
  public static boolean estPlusPetit(NombreA2Chiffres x, NombreA2Chiffres y) {
    return x.value() <= y.value();
  }
  public static NombreA2Chiffres plus(NombreA2Chiffres x, NombreA2Chiffres y) {
    return ofInt(x.value() + y.value());
  }
  public static NombreA2Chiffres moins(NombreA2Chiffres x, NombreA2Chiffres y) {
    int res = x.value() - y.value();
    return ofInt((res > 0) ? res : 0);
  }
  public static NombreA2Chiffres mult(NombreA2Chiffres x, NombreA2Chiffres y) {
    return ofInt(x.value() * y.value());
  }
}
