package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import org.javascool.gui.JVSMainPanel;

/**
 *
 * @author gmatheron
 */
public abstract class Accessible extends LinkedEventGroup {
  private boolean m_superDestroyed = false;
  private boolean m_superDeleted = false;
  private ArrayList<StoredProperty> m_props;

  @SuppressWarnings("CollectionWithoutInitialCapacity")
  public Accessible() {
    m_props = new ArrayList<StoredProperty>();
  }
  // TODO javadoc and update doc
  public void addProperty(String name, Object o) {
    for(StoredProperty s : m_props)
      if(s.getName().equals(name))
        JVSMainPanel.reportRuntimeBug("Impossible de rajouter une propriété nommée " + name + " car une propriété de ce nom existe déjà");
    m_props.add(new StoredProperty(name, o, o.getClass()));
  }
  public void removeProperty(String name) {
    for(StoredProperty s : m_props)
      if(s.getName().equals("name"))
        m_props.remove(s);
  }
  public Object getProperty(String name) {
    for(StoredProperty s : m_props)
      if(s.getName().equals(name))
        return s.getType().cast(s.getObject());
    return null;
  }
  public void setProperty(String name, Object o) {
    boolean exists = false;
    StoredProperty prop = null;
    for(StoredProperty s : m_props)
      if(s.getName().equals(name)) {
        exists = true;
        prop = s;
      }
    if(!exists)
      addProperty(name, o);
    else
      prop.setObject(o);
  }
  public void destroy() {
    m_superDestroyed = true;
  }
  @Override
  public boolean isDestroyed() {
    return m_superDestroyed;
  }
  public boolean isDeleted() {
    return m_superDeleted;
  }
  public void delete() {
    m_superDeleted = true;
    Group.updateAll();
  }
}
