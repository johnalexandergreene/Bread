package org.fleen.bread.zCellSystem;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;

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
  
  public MarginCells(int w,int h,DPolygon rootpolygon,AffineTransform rootpolygontransform,double glowspan){
    super();
    width=w;
    height=h;
    this.rootpolygon=rootpolygon;
    this.glowspan=glowspan;
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
  
  Cell[][] cells;
  //not a polygon, a yard
  public static final DPolygon MARGINYARD=new DPolygon();
  
  private void doCells(){
    //init the cells
    cells=new Cell[width][height];
    Cell c;
    for(int x=0;x<cells.length;x++){
      for(int y=0;y<cells[0].length;y++){
        cells[x][y]=new Cell(x,y);}}
    //map raw margin
    for(int x=0;x<cells.length;x++){
      for(int y=0;y<cells[0].length;y++){
        c=getCell(x,y);
        c.addPresence(new Presence(MARGINYARD,1.0));}}
    //get the root polygon mass
    PolygonAreaCells root=new PolygonAreaCells(rootpolygon,rootpolygontransform,glowspan);
    //subtract the root
    Presence p0,p1;
    Cell c1;
    for(Cell c0:root.getCells()){
      c1=getCell(c0.x,c0.y);
      p0=c0.presences.get(0);
      p1=c1.presences.get(0);
      p1.intensity-=p0.intensity;}}
  
  public Cell getCell(int x,int y){
    if(x<0||x>=cells.length||y<0||y>=cells[0].length)
      return null;
    return cells[x][y];}

  public int getCellCount(){
    return cells.length*cells[0].length;}
  
  public Collection<Cell> getCells(){
    Collection<Cell> a=new ArrayList<Cell>();
    for(int x=0;x<cells.length;x++){
      for(int y=0;y<cells[0].length;y++){
        a.add(cells[x][y]);}}
    return a;}

}
