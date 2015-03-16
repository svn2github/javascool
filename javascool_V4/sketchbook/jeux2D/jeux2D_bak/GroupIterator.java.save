package org.javascool.proglets.jeux2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Iterator;
import java.util.logging.Logger;

/**
 *
 * @author gmatheron
 */
public class GroupIterator implements Iterator<Accessible>{
  private Group m_group;
  private int m_i;
  private int m_last;

  public GroupIterator(Group g) {
    m_group = g;
    m_i = 0;
    m_last = g.size();
  }
  @Override
  public boolean hasNext() {
    return m_i < m_last;
  }
  @Override
  public Accessible next() {
    m_i++;
    return m_group.get(m_i - 1);
  }
  @Override
  public void remove() { throw new UnsupportedOperationException("Not supported yet.");
  }
  private static final Logger LOG = Logger.getLogger(GroupIterator.class.getName());
}
