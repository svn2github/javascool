package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author gmatheron
 */
public class Group extends Accessible implements Iterable<Accessible>{
  private boolean m_destroyed;
  private static final long serialVersionUID = 1L;
  private static final Logger LOG = Logger.getLogger(Group.class.getName());
  private ArrayList<Accessible> m_items;
  private boolean m_deleted;
  private static ArrayList<Group> m_self = new ArrayList<Group>();

  public static void updateAll() {
    for(int i = 0; i < m_self.size(); i++)
      m_self.get(i).update();
  }
  public void update() {
    for(int i = 0; i < m_items.size(); i++)
      if(m_items.get(i).isDeleted()) {
        m_items.remove(m_items.get(i));
        update();
      }
  }
  @SuppressWarnings("CollectionWithoutInitialCapacity")
  public Group() {
    super();
    m_items = new ArrayList<Accessible>();
    m_self.add(this);
  }
  public int size() {
    return m_items.size();
  }
  public void add(Accessible a) {
    m_items.add(a);
  }
  public Accessible get(int i) {
    return m_items.get(i);
  }
  @Override
  public GroupIterator iterator() {
    return new GroupIterator(this);
  }
  @Override
  public boolean isForMe() { throw new UnsupportedOperationException("Not supported.");
  }
  public void remove(int i) {
    m_items.remove(i);
  }
  public void remove(Accessible a) {
    m_items.remove(a);
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   * @param always If set to true, the callback will always be performed for each
   * element of the collection even if the event did not occur on one of the elements
   */
  @Override
  public void onClick(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onClick.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseEntered(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseEntered.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseExited(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseExited.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMousePressed(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMousePressed.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseReleased(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseReleased.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseDown(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseDown.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseUp(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseUp.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseMoved(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseMoved.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseDragged(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseDragged.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseWheelUp(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseWheelUp.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseWheelDown(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseWheelDown.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseWheelMoved(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseWheelMoved.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  public void onFrame(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onFrame.add(new EventListener(s, m_items.get(i), always));
  }
  public void onKeyDown(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onKeyDown.add(new EventListener(s, m_items.get(i), always));
  }
  public void onKeyUp(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onKeyUp.add(new EventListener(s, m_items.get(i), always));
  }
  public void onKeyPressed(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onKeyPressed.add(new EventListener(s, m_items.get(i), always));
  }
  public void onKeyReleased(String s, boolean always) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onKeyReleased.add(new EventListener(s, m_items.get(i), always));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   * <!-- @param always If set to true, the callback will always be performed for each
   * element of the collection even if the event did not occur on one of the elements-->
   */
  @Override
  public void onClick(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onClick.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseEntered(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseEntered.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseExited(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseExited.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMousePressed(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMousePressed.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseReleased(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseReleased.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseDown(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseDown.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseUp(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseUp.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseMoved(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseMoved.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseDragged(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseDragged.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseWheelUp(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseWheelUp.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseWheelDown(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseWheelDown.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  @Override
  public void onMouseWheelMoved(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onMouseWheelMoved.add(new EventListener(s, m_items.get(i)));
  }
  /**
   * Used to create a listener that will callback the specified function
   * with one MouseWheelState argument
   * @param s The function to callback
   */
  public void onFrame(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onFrame.add(new EventListener(s, m_items.get(i)));
  }
  public void onKeyDown(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onKeyDown.add(new EventListener(s, m_items.get(i)));
  }
  public void onKeyUp(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onKeyUp.add(new EventListener(s, m_items.get(i)));
  }
  public void onKeyPressed(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onKeyPressed.add(new EventListener(s, m_items.get(i)));
  }
  public void onKeyReleased(String s) {
    for(int i = 0; i < m_items.size(); i++)
      PrivateFunctions.getFunctionsElementSingleton().m_onKeyReleased.add(new EventListener(s, m_items.get(i)));
  }
}
