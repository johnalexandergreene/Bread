package org.fleen.bread.zCellSystem;

import java.util.Iterator;


class CellIterator implements Iterator<Cell> {
  
  CellIterator(FuzzyCellSystem rastermap){
    this.rastermap=rastermap;}
  
  FuzzyCellSystem rastermap;
  int x=0,y=0;
  
  public boolean hasNext(){
    return y<rastermap.cellarrayheight;}

  public Cell next(){
    Cell c=rastermap.cells[x][y];
    x++;
    if(x==rastermap.cellarraywidth){
      x=0;
      y++;}
    return c;}

  public void remove(){
    throw new IllegalArgumentException("not implemented");}}
