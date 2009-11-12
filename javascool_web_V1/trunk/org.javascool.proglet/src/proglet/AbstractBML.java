package proglet;

/** Implements methods wrappers for a logical-structure. */
abstract class AbstractBML {
  public abstract boolean isDefined(String name);
  public final boolean isDefined(int index) { return isDefined(Integer.toString(index)); }  
  public abstract BML getChild(String name);
  public final BML getChild(int index) { return getChild(Integer.toString(index)); }
  public abstract String getString(String name, String value);
  public final String getString(int index, String value) { return getString(Integer.toString(index), value); }
  public abstract double getDecimal(String name, double value);
  public final double getDecimal(int index, double value) { return getDecimal(Integer.toString(index), value); }
  public abstract int getInteger(String name, int value);
  public final int getInteger(int index, int value) { return getInteger(Integer.toString(index), value); }
  public abstract BML set(String name, BML value);
  public final BML set(int index, BML value) { return set(Integer.toString(index), value); }
  public final BML set(String name, String value) { BML v = new BML(); v.reset(value); return set(name, v); }
  public final BML set(int index, String value) { return set(Integer.toString(index), value); }
  public final BML set(String name, double value) { return set(name, Double.toString(value)); }
  public final BML set(int index, double value) { return set(Integer.toString(index), value); }
  public final BML set(String name, int value) { return set(name, Integer.toString(value)); }
  public final BML set(int index, int value) { return set(Integer.toString(index), value); }
  public abstract BML add(BML value);
  public final BML add(String value) { BML v = new BML(); v.reset(value); return add(v); }
  public final BML add(double value) { return add(Double.toString(value)); }
  public final BML add(int value) {  return add(Integer.toString(value)); }
}

