package org.fleen.bread.FuzzyCellSystem;

import java.awt.geom.AffineTransform;
import java.util.Iterator;

import org.fleen.geom_2D.DPolygon;

/* 
 * It's a reaction diffusion system
 * 
 * Map polygons and other things to cell array
 * 
 * It is assumed that all of the polygons are within the map bounds
 * 
 * We have a margin area
 * 
 * We do cell-ish stuff, to manipulate thing-areas
 * 
 * ---For example, a Presence may go like this for a polygon
 *   Closeness
 *     The Closeness of the Presence corrosponds roughly to the degree to which the polygon covers the cell-square.
 *     Total coverage means intensity of 1.0. Partial or near-the-edge means less than 1.0.
 *     Right on the edge is intensity=0.5  
 *   The Strength of the Presence is the Strength of the polygon * Closeness
 *   
 *   ----------------------
 *   
 *   Here in the 2D lib we need Point, Seg, Opencurve and Polygon
 *   we will have an interface : RasterMapVectorObject
 *   All 4 of those classes will implement RasterMapVectorObject
 *   Our constructors will be with a collection of those or without .
 *   
 */
public class FuzzyCellSystem implements Iterable<Cell>{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public FuzzyCellSystem(int w,int h){
    System.out.println("RD SYSTEM INIT");
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
  
  /*
   * the height and width of a cell
   */
  static final double CELLSPAN=1.0;
  
  Cell[][] cells;
  
  int cellarraywidth,cellarrayheight;
  
  private void initCells(int w,int h){
    cellarraywidth=w;
    cellarrayheight=h;
    cells=new Cell[w][h];
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        cells[x][y]=new Cell(x,y);}}}
  
  /*
   * return the cell that contains the specified point
   * the cell's coors are also the cell's center point
   * the cell's square spans cell.x-0.5 to cell.y+0.5 and cell.y-0.5 to cell.y+0.5
   */
  Cell getCellContainingPoint(double x,double y){
    if(x-Math.floor(x)<0.5)
      x=Math.floor(x);
    else
      x=Math.ceil(x);
    if(y-Math.floor(y)<0.5)
      y=Math.floor(y);
    else
      y=Math.ceil(y);
    return getCell((int)x,(int)y);}
  
  Cell getCell(int x,int y){
    if(x<0||x>=cellarraywidth||y<0||y>=cellarrayheight)
      return null;
    return cells[x][y];}
  
  public Iterator<Cell> iterator(){
    return new CellIterator(this);}
  
  public int getWidth(){
    return cellarraywidth;}
  
  public int getHeight(){
    return cellarrayheight;}
  
  /*
   * ################################
   * MAP THINGS TO CELLS
   * Cast the presence of the thing, like a shadow, on the cells
   * ################################
   */
  
  public PolygonAreaCells mapPolygonArea(DPolygon areapolygon,AffineTransform areapolygontransform,double glowspan){
    PolygonAreaCells c=new PolygonAreaCells(areapolygon,areapolygontransform,glowspan);
    Cell b;
    for(Cell a:c.getCells()){
      b=cells[a.x][a.y];
      b.presences.add(a.presences.get(0));}
    return c;}
  
  public PolygonEdgeCells mapPolygonEdge(DPolygon edgepolygon,AffineTransform edgepolygontransform,double glowspan){
    PolygonEdgeCells c=new PolygonEdgeCells(edgepolygon,edgepolygontransform,glowspan);
    Cell b;
    for(Cell a:c.getCells()){
      b=cells[a.x][a.y];
      b.presences.add(a.presences.get(0));}
    return c;}
  
  public MarginCells mapMarginCells(DPolygon rootpolygon,AffineTransform rootpolygontransform,double glowspan){
    MarginCells c=new MarginCells(cellarraywidth,cellarrayheight,rootpolygon,rootpolygontransform,glowspan);
    Cell b;
    for(Cell a:c.getCells()){
      b=cells[a.x][a.y];
      b.presences.add(a.presences.get(0));}
    return c;}
  
  /*
   * ################################
   * CLEAN CELLS
   * Do this after all mapping is done
   * ################################
   */
  
  public void clean(){
    for(Cell c:this)
      c.clean();}
  
}
