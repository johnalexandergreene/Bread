package org.fleen.bread.zCellSystem.test0;

import org.fleen.bread.zCellSystem.ZCellSystem;

public interface Rule{
  
  /*
   * for each cell in cs0
   *   apply rule to get new cell presences
   *   set presences of corresponding cell in cs1 appropriately
   */
  void doRule(ZCellSystem s0,ZCellSystem s1);
  
}
