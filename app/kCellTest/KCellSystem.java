package org.fleen.bread.app.kCellTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.fleen.bread.app.kCellTest.test0.KCellSystemObserver;
import org.fleen.geom_Kisrhombille.KCell;

public class KCellSystem{
  
  public KCellSystem(){
    init();
  }
  
  public KCellSystemObserver observer;
  
  public Set<KCell> cells=new HashSet<KCell>();
  
  void init(){
    KCell c=new KCell(0,0,0,0);
    cells.add(c);
  }
  
  public int age=-1;
  
  public void advanceState(){
    
    Set<KCell> newcells=new HashSet<KCell>();
    newcells.addAll(cells);
    for(KCell c:cells)
      newcells.addAll(Arrays.asList(c.getAdjacents()));
    cells=newcells;
    
    age++;
    observer.advanced();
    
  }

}
