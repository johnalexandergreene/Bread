package org.fleen.bread.cellSystem;

public interface Rule{
  
  /*
   * for each cell in cs0
   *   apply rule to get new cell contents
   *   set contents of corresponding cell in cs1 appropriately
   */
  void doRule(CellSystem cs0,CellSystem cs1);
  
}
