package org.fleen.bread.zCellSystem;

import java.util.Iterator;


public interface ZCellMass extends Iterable<ZCell>{
  
  int getCellCount();
  
  Iterator<ZCell> iterator();
  
}
