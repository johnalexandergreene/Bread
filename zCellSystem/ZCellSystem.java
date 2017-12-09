package org.fleen.bread.zCellSystem;

import java.util.Iterator;
import java.util.List;

import org.fleen.bread.hCellSystem.MarginHCells;
import org.fleen.bread.hCellSystem.PolygonEdgeHCells;
import org.fleen.forsythia.core.composition.FPolygon;

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
public class ZCellSystem implements Iterable<ZCell>{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public ZCellSystem(int w,int h){
    System.out.println("ZCELL SYSTEM INIT");
    System.out.println(w+"x"+h);
    System.out.println("cellcount="+(w*h));
    initCells(w,h);}
  
  public ZCellSystem(int w,int h,List<ZCSMappedThing> mappedthings){
    this(w,h);
    doMappedThings(mappedthings);
    clean();}
  
  /*
   * ################################
   * CELLS
   * A cell's center is its coordinates
   * its square ranges x-0.5 to x+0.5 and y-0.5 to y+0.5
   * ################################
   */
  
  //the height and width of a cell
  static final double CELLSPAN=1.0;
  
  ZCell[][] cells;
  
  public int getWidth(){
    return cells.length;}
  
  public int getHeight(){
    return cells[0].length;}
  
  public int getCellCount(){
    return cells.length*cells[0].length;}
  
  private void initCells(int w,int h){
    cells=new ZCell[w][h];
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        cells[x][y]=new ZCell(x,y);}}}
  
  public ZCell getCell(int x,int y){
    if(x<0||x>=cells.length||y<0||y>=cells[0].length)
      return null;
    return cells[x][y];}
  
  public ZCell[] getNeighbors(ZCell c){
    ZCell[] n=new ZCell[8];
    n[0]=getCell(c.x,c.y+1);
    n[1]=getCell(c.x+1,c.y+1);
    n[2]=getCell(c.x+1,c.y);
    n[3]=getCell(c.x+1,c.y-1);
    n[4]=getCell(c.x,c.y-1);
    n[5]=getCell(c.x-1,c.y-1);
    n[6]=getCell(c.x-1,c.y);
    n[7]=getCell(c.x-1,c.y+1);
    return n;}
  
  public Iterator<ZCell> iterator(){
    return new ZCellIterator(this);}
  
  /*
   * return the cell that contains the specified point
   * the cell's coors are also the cell's center point
   * the cell's square spans cell.x-0.5 to cell.y+0.5 and cell.y-0.5 to cell.y+0.5
   */
  ZCell getCellContainingPoint(double x,double y){
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
   * Cast the presence of the thing, like a shadow, onto the cells
   * At the moment we can map margin, leaf polygon and polygon edge
   * ################################
   */
  
  private void doMappedThings(List<ZCSMappedThing> mappedthings){
    for(ZCSMappedThing t:mappedthings){
      if(t.hasTags("margin")){
        mapMargin(t);
      }else if(t.hasTags("leaf")){
        mapPolygonArea(t);
      }else if(t.hasTags("boiled")){
        mapPolygonBoiledEdge(t);
      }else{
        throw new IllegalArgumentException("mapping thing failed");}}}
  
  private PolygonAreaZCells mapPolygonArea(ZCSMappedThing t){
    PolygonAreaZCells pac=new PolygonAreaZCells(((FPolygon)t.thing).getDPolygon(),t.transform,t.glowspan);
    ZCell c1;
    for(ZCell c0:pac){
        c1=getCell(c0.x,c0.y);
        c1.addPresences(c0.presences);}
    return pac;}
  
  private PolygonEdgeHCells mapPolygonBoiledEdge(ZCSMappedThing t){
    return null;}
//    PolygonEdgeZCells c=new PolygonEdgeZCells(((FPolygon)t.thing).getDPolygon(),t.transform);
//    ZCell b;
//    for(ZCell a:c){
//        b=cells[a.x][a.y];
//        b.thing=t;}
//    return c;}
  
  private MarginHCells mapMargin(ZCSMappedThing t){
    return null;}
//    MarginZCells c=new MarginZCells(getWidth(),getHeight(),((FPolygon)t.thing).getDPolygon(),t.transform);
//    ZCell b;
//    for(ZCell a:c){
//        b=cells[a.x][a.y];
//        b.thing=t;}
//    return c;}
  
  /*
   * ################################
   * CLEAN CELLS
   * Do this after all mapping is done
   * ################################
   */
  
  public void clean(){
    for(ZCell c:this)
      c.clean();}
  
}
