package org.fleen.bread.hCellSystem;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fleen.geom_2D.DPolygon;

/*
 * map the whole RDSystem to a single cell mass with all presences at 1.0
 * then subtract the root mass
 * simple
 */
public class MarginCells implements CellMass{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public MarginCells(int w,int h,DPolygon rootpolygon,AffineTransform rootpolygontransform){
    super();
    width=w;
    height=h;
    this.rootpolygon=rootpolygon;
    this.rootpolygontransform=rootpolygontransform;
    doCells();}
  
  int width,height;
  
  /*
   * ################################
   * POLYGON
   * ################################
   */
  
  AffineTransform rootpolygontransform;
  DPolygon rootpolygon;
  
  /*
   * ################################
   * GLOWSPAN
   * The distance to which a thing's presence bleeds out beyond its edge
   * ################################
   */
  
  double glowspan;
  
  /*
   * ################################
   * CELLS
   * ################################
   */
  
  private List<Cell> cells=new ArrayList<Cell>();
  
  public Iterator<Cell> iterator(){
    return cells.iterator();}
  
  public int getCellCount(){
    return cells.size();}
  
  /*
   * ################################
   * MAP MARGIN
   * make a big rectangle of cells
   * subtract the root polygon cells
   * ################################
   */
  
  Cell[][] workingarray;
  
  private void doCells(){
    workingarray=new Cell[width][height];
    for(int x=0;x<width;x++){
      for(int y=0;y<height;y++){
        workingarray[x][y]=new Cell(x,y);}}
    PolygonAreaCells root=new PolygonAreaCells(rootpolygon,rootpolygontransform);
    //subtract the root
    for(Cell c:root)
      workingarray[c.x][c.y]=null;
    //
    for(int x=0;x<width;x++){
      for(int y=0;y<height;y++){
        if(workingarray[x][y]!=null)
          cells.add(workingarray[x][y]);}}}
  
  

}
