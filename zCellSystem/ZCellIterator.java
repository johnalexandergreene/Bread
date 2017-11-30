package org.fleen.bread.zCellSystem;

import java.util.Iterator;


class ZCellIterator implements Iterator<ZCell> {
  
  ZCellIterator(ZCellSystem rastermap){
    this.rastermap=rastermap;}
  
  ZCellSystem rastermap;
  int x=0,y=0;
  
  public boolean hasNext(){
    return y<rastermap.cellarrayheight;}

  public ZCell next(){
    ZCell c=rastermap.cells[x][y];
    x++;
    if(x==rastermap.cellarraywidth){
      x=0;
      y++;}
    return c;}

  public void remove(){
    throw new IllegalArgumentException("not implemented");}}
