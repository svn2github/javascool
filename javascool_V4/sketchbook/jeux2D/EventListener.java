package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.logging.Logger;

/**
 * This class stores an event : it contains a callback method that will be called
 * if the event is triggered and an EventCatcher that should be asked if the event
 * concerns it.
 * Note that this class does not perform any task, it is just a group of variables.
 * @author gmatheron
 */
public class EventListener {
  private String m_method;
  private EventCatcher m_object;
  private boolean m_always;

  /**
   * Creates a new EventListener
   * @param method The callback method
   * @param object The EventCatcher that will tell if the event should be triggered
   */
  public EventListener(String method, EventCatcher object) {
    m_method = method;
    m_object = object;
    m_always = false;
  }
  /**
   * Creates a new EventListener
   * @param method The callback method
   * @param object The EventCatcher that will tell if the event should be triggered
   * @param always If set to true the event will be triggered even if the EventCatcher
   * refuses it (typically if the mouse isn't over the object)
   */
  public EventListener(String method, EventCatcher object, boolean always) {
    m_method = method;
    m_object = object;
    m_always = always;
  }
  /**
   * Gets the callback method's name
   * @return the callback method's name
   */
  public String getMethod() {
    return m_method;
  }
  /**
   * Gets the EventCatcher
   * @return the EventCatcher
   */
  public EventCatcher getObject() {
    return m_object;
  }
  /**
   * Returns true if the event should be called whatever the response of the EventCatcher is
   * @return true if the event should be called whatever the response of the EventCatcher is
   */
  public boolean getAlways() {
    return m_always;
  }
  private static final Logger LOG = Logger.getLogger(EventListener.class.getName());
}
