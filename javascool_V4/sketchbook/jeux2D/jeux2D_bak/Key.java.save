package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gmatheron
 */
public class Key extends Accessible {
  public int m_code = 0;
  private boolean m_deleted = false;

  public Key(int code) {
    super();
    m_code = code;
  }
  public int getCode() {
    return m_code;
  }
  public void setCode(int code) {
    m_code = code;
  }
  @Override
  public boolean isForMe() {
    return Functions.keyDown(m_code);
  }
}
