package proglet.joueravecleschiffres;

public enum Chiffre { 
  ZERO   (0), 
  UN     (1), 
  DEUX   (2),
  TROIS  (3),
  QUATRE (4),
  CINQ   (5),
  SIX    (6),
  SEPT   (7),
  HUIT   (8),
  NEUF   (9);
  
  public int value;  
  Chiffre(int val) { value = val; } 
  
  static public Chiffre ofInt(int i) {
    int j = (i % 10);
    int k = (j<0) ? j+10 : j	;
    switch (k) {
      case 0: return ZERO; 
      case 1: return UN; 
      case 2: return DEUX;
      case 3: return TROIS;
      case 4: return QUATRE;
      case 5: return CINQ;
      case 6: return SIX;
      case 7: return SEPT;
      case 8: return HUIT;
      default: return NEUF;
    }
  }
  public static Chiffre unite_moins(Chiffre dizaine1, Chiffre unite1, Chiffre unite2) { 
    return ofInt(10*dizaine1.value+unite1.value-unite2.value);
  }   
  public static Chiffre unite_plus(Chiffre t, Chiffre c1, Chiffre c2) {  
    return ofInt(t.value+c1.value+c2.value);   
  }   
  public static Chiffre unite_mult(Chiffre t, Chiffre c1, Chiffre c2) {     
    return ofInt(t.value*c1.value+c2.value);  
  }  
  public static Chiffre dizaine_plus(Chiffre t, Chiffre c1, Chiffre c2) {   
    return ofInt((t.value+c1.value+c2.value) / 10);  
  } 
  public static Chiffre dizaine_mult(Chiffre t, Chiffre c1, Chiffre c2) {  
    return ofInt((t.value*c1.value+c2.value) / 10);   
  } 
  
  public String toString() {
    return String.valueOf(value); 
  }
}
