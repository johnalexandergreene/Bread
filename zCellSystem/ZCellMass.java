package org.fleen.bread.zCellSystem;

public interface ZCellMass{
  
  /*
   * returns cell in cell mass
   * returns null if no such cell exists within the mass
   */
  ZCell getCell(int x,int y);
  
  int getCellCount();

}
