package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.logging.Logger;

/**
 *
 * @author gmatheron
 */
public class StoredProperty {
  private String m_name;
  private Object m_value;
  private Class< ? > m_type;

  public StoredProperty() {
    m_name = "";
    m_value = null;
  }
  public StoredProperty(String s, Object o, Class< ? > c) {
    m_name = s;
    m_value = o;
    m_type = c;
  }
  public boolean isNull() {
    return m_value == null;
  }
  public String getName() {
    return m_name;
  }
  public Object getObject() {
    return m_value;
  }
  public Class getType() {
    return m_type;
  }
  public void setObject(Object o) {
    m_value = o;
  }
  private static final Logger LOG = Logger.getLogger(StoredProperty.class.getName());
}
