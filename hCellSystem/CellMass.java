package org.fleen.bread.hCellSystem;

import java.util.Iterator;

/*
 * basic cell ops
 * implemented by CellSystem, PolygonAreaCells and PolygonEdgeCells
 */
public interface CellMass extends Iterable<Cell>{
  
  int getCellCount();
  
  Iterator<Cell> iterator();
  
}
