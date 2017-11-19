package org.fleen.bread.cellSystem;

import java.util.Iterator;

class CellSystemCellIterator implements Iterator<Cell> {
  
  CellSystemCellIterator(CellSystem rastermap){
    this.rastermap=rastermap;}
  
  CellSystem rastermap;
  int x=0,y=0;
  
  public boolean hasNext(){
    return y<rastermap.getHeight();}

  public Cell next(){
    Cell c=rastermap.cells[x][y];
    x++;
    if(x==rastermap.getWidth()){
      x=0;
      y++;}
    return c;}

  public void remove(){
    throw new IllegalArgumentException("not implemented");}}
