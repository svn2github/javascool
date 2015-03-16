package org.javascool.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Invoque une méthode sur un objet Java.
 */
public class Invoke {
  /**
   * Invoke une méthode sans argument sur un objet.
   *
   * @param object
   *            L'objet sur lequel on invoque la méthode.
   * @param method
   *            La méthode sans argument à invoquer, souvent : <tt>init</tt>,
   *            <tt>destroy</tt>, <tt>start</tt>, <tt>stop</tt> ou
   *            <tt>run</tt>.
   * @param run
   *            Si true (par défaut) appelle la méthode, si false teste
   *            simplement son existence.
   * @return La valeur true si la méthode est invocable, false sinon.
   * @throws RuntimeException
   *             si la méthode génère une exception lors de son appel.
   */
  public static boolean run(Object object, String method, boolean run) {
    try {
      Method m = object.getClass().getDeclaredMethod(method);
      if(run) {
        m.invoke(object);
      }
    } catch(InvocationTargetException e) { throw new RuntimeException(e.getCause());
    } catch(Throwable e) {
      return false;
    }
    return true;
  }
  /**
   * @see #run(Object, String, boolean)
   */
  public static boolean run(Object object, String method) {
    return Invoke.run(object, method, true);
  }
}
