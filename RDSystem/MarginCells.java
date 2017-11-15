package org.fleen.bread.RDSystem;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class MarginCells extends ArrayList<Cell> implements CellMass{
  
  public MarginCells(){
    super();}

  public Cell getCell(int x,int y){
    for(Cell c:this){
      if(c.x==x&&c.y==y)
        return c;}
    return null;}

  public int getCellCount(){
    return size();}

}
