package org.fleen.bread.cellSystem;

import java.awt.geom.AffineTransform;
import java.util.Iterator;

import org.fleen.geom_2D.DPolygon;

/* 
 * A cellular automata system
 * For a forsythia post-process
 * For boiling, curve-smoothing, maybe some proportion tweaking
 * It might get fancy. We might translate vertices into special cells, doing a vector-CA combo thing
 * We prefer this to RD because CA is simpler
 */
public class CellSystem implements CellMass{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public CellSystem(int w,int h){
    System.out.println("CS SYSTEM INIT");
    System.out.println(w+"x"+h);
    System.out.println("cellcount="+(w*h));
    initCells(w,h);}
  
  /*
   * ################################
   * CELLS
   * A cell's center is its coordinates
   * its square ranges x-0.5 to x+0.5 and y-0.5 to y+0.5
   * ################################
   */
  
  //the height and width of a cell relative to any mapped geometry
  static final double CELLSPAN=1.0;
  
  Cell[][] cells;
  
  public int getWidth(){
    return cells.length;}
  
  public int getHeight(){
    return cells[0].length;}
  
  public int getCellCount(){
    return cells.length*cells[0].length;}
  
  private void initCells(int w,int h){
    cells=new Cell[w][h];
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        cells[x][y]=new Cell(x,y);}}}
  
  public Cell getCell(int x,int y){
    if(x<0||x>=cells.length||y<0||y>=cells[0].length)
      return null;
    return cells[x][y];}
  
  public Cell[] getNeighbors(Cell c){
   Cell[] n=new Cell[8];
   n[0]=getCell(c.x,c.y+1);
   n[1]=getCell(c.x+1,c.y+1);
   n[2]=getCell(c.x+1,c.y);
   n[3]=getCell(c.x+1,c.y-1);
   n[4]=getCell(c.x,c.y-1);
   n[5]=getCell(c.x-1,c.y-1);
   n[6]=getCell(c.x-1,c.y);
   n[7]=getCell(c.x-1,c.y+1);
   return n;}
  
  public Iterator<Cell> iterator(){
    return new CellSystemCellIterator(this);}
  
  /*
   * return the cell that contains the specified point
   * the cell's coors are also the cell's center point
   * the cell's square spans cell.x-0.5 to cell.y+0.5 and cell.y-0.5 to cell.y+0.5
   */
  public Cell getCellContainingPoint(double x,double y){
    if(x-Math.floor(x)<0.5)
      x=Math.floor(x);
    else
      x=Math.ceil(x);
    if(y-Math.floor(y)<0.5)
      y=Math.floor(y);
    else
      y=Math.ceil(y);
    return getCell((int)x,(int)y);}
  
  /*
   * ################################
   * MAP THINGS TO CELLS
   * Cast the presence of the thing, like a shadow, on the cells
   * ################################
   */
  
  public PolygonAreaCells mapPolygonArea(DPolygon areapolygon,AffineTransform areapolygontransform){
    MappedThing thing=new MappedThing(areapolygon);
    PolygonAreaCells c=new PolygonAreaCells(areapolygon,areapolygontransform);
    Cell b;
    for(Cell a:c){
        b=cells[a.x][a.y];
        b.thing=thing;}
    return c;}
  
  public PolygonEdgeCells mapPolygonEdge(DPolygon edgepolygon,AffineTransform edgepolygontransform){
    MappedThing thing=new MappedThing(edgepolygon);
    PolygonEdgeCells c=new PolygonEdgeCells(edgepolygon,edgepolygontransform);
    Cell b;
    for(Cell a:c){
        b=cells[a.x][a.y];
        b.thing=thing;}
    return c;}
  
  public MarginCells mapMarginCells(DPolygon rootpolygon,AffineTransform rootpolygontransform){
    MappedThing thing=new MappedThing(rootpolygon);
    MarginCells c=new MarginCells(getWidth(),getHeight(),rootpolygon,rootpolygontransform);
    Cell b;
    for(Cell a:c){
        b=cells[a.x][a.y];
        b.thing=thing;}
    return c;}
  
  /*
   * ################################
   * CLEAN CELLS
   * Do this after all mapping is done
   * resolve null cells. 
   * TODO maybe some other stuff 
   * ################################
   */
  
  public void clean(){
    }
  
}
