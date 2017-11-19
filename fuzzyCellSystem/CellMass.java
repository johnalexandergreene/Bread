package org.fleen.bread.fuzzyCellSystem;

public interface CellMass{
  
  /*
   * returns cell in cell mass
   * returns null if no such cell exists within the mass
   */
  Cell getCell(int x,int y);
  
  int getCellCount();

}