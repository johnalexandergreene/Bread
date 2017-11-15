package org.fleen.bread.RDSystem;

import java.awt.geom.AffineTransform;
import java.util.Iterator;

import org.fleen.geom_2D.DPolygon;

/* RASTER MAP
 * 
 * Map vector geometry objects to this raster
 *  
 * Vector geometry objects that we map:
 *   Point
 *   Seg
 *   OpenCurve (as sequence of points aka polyseg)
 *   ClosedCurve (aka edge of a polygon)
 *   Polygon area
 * 
 * The raster is a rectangular array of 1x1 cells
 * 
 * A vector geometry object is mapped to a cell in terms of Presence
 *   How close is that artifact to that cell?
 *   Specifically
 *     In the case of a Point, Seg, OpenCurve or ClosedCurve, how close is that object to the cell's center point?
 *     In the case of a Polygon, is the cell's center contained within that polygonal area?
 *     And how strong is the VG (all mapped objects have a Strength property)? 
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
public class RDSystem implements Iterable<Cell>{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public RDSystem(int w,int h){
    System.out.println("RD SYSTEM INIT "+w+"x"+h);
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
        cells[x][y]=new Cell(this,x,y);}}}
  
  /*
   * return the cell that contains the specified point
   * the cell's coors are also the cell's center point
   * the cell's square spans cell.x-0.5 to cell.y+0.5 and cell.y-0.5 to cell.y+0.5
   */
  Cell getCellContainingPoint(double x,double y){
    //TODO this could be improved
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
    //if the cell is off array then create it and return it
    if(isOffMap(x,y)){
      return new Cell(this,x,y,true);
    //if the cell is on the array then return that cell
    }else{
      return cells[x][y];}}
  
  boolean isOffMap(int x,int y){
    return x<0||x>=cellarraywidth||y<0||y>=cellarrayheight;}
  
  public Iterator<Cell> iterator(){
    return new CellIterator(this);}
  
  public int getWidth(){
    return cellarraywidth;}
  
  public int getHeight(){
    return cellarrayheight;}
  
  /*
   * ################################
   * MAP THINGS TO CELLS
   * We map polygons and polygon-edges
   * Cast the presence of the thing, like a shadow, on the cells
   * ################################
   */
  
  public PolygonAreaCells mapPolygonArea(DPolygon polygon,AffineTransform transform,double glowspan){
    //cast the presence, create the polygoncellmap and return it
    //if the polygon crosses the edge of the viewport then the polygoncellmap will contain some cells that are not within this rastermap
    //the polygoncellmap is just a way of doing the cell presences in an orderly way
    //Yes we return the PolygonCells object but after this method we will probably never use PolygonCells except for debugging and maybe tools 
    PolygonAreaCells c=new PolygonAreaCells(this,polygon,transform,glowspan);
    return c;}
  
  public PolygonEdgeCells mapPolygonEdge(DPolygon polygon,AffineTransform transform,double glowspan){
    PolygonEdgeCells c=new PolygonEdgeCells(this,polygon,transform,glowspan);
    return c;}
  
  /*
   * ################################
   * CLEAN CELLS
   * Do this after all casting is done
   * ################################
   */
  
  public void clean(){
    for(Cell c:this)
      c.clean();}
  
}
