package org.fleen.bread.cellSystem;

import java.util.Iterator;

/*
 * basic cell ops
 * implemented by CellSystem, PolygonAreaCells and PolygonEdgeCells
 */
public interface CellMass extends Iterable<Cell>{
  
  int getCellCount();
  
  Iterator<Cell> iterator();
  
}
