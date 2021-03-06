package org.fleen.bread.hCellSystem;

import java.util.Iterator;
import java.util.List;

import org.fleen.forsythia.core.composition.FPolygon;

/* 
 * A cellular automata system
 * For a forsythia post-process
 * For boiling, curve-smoothing, maybe some proportion tweaking
 * It might get fancy. We might translate vertices into special cells, doing a vector-CA combo thing
 * We prefer this to RD because CA is simpler
 */
public class HCellSystem implements HCellMass{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */

  public HCellSystem(int w,int h){
    System.out.println("HCELL SYSTEM INIT");
    System.out.println(w+"x"+h);
    System.out.println("cellcount="+(w*h));
    initCells(w,h);}
  
  public HCellSystem(int w,int h,List<HCSMappedThing> mappedthings){
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
  
  //the height and width of a cell relative to any mapped geometry
  static final double CELLSPAN=1.0;
  
  HCell[][] cells;
  
  public int getWidth(){
    return cells.length;}
  
  public int getHeight(){
    return cells[0].length;}
  
  public int getCellCount(){
    return cells.length*cells[0].length;}
  
  private void initCells(int w,int h){
    cells=new HCell[w][h];
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        cells[x][y]=new HCell(x,y);}}}
  
  public HCell getCell(int x,int y){
    if(x<0||x>=cells.length||y<0||y>=cells[0].length)
      return null;
    return cells[x][y];}
  
  public HCell[] getNeighbors(HCell c){
   HCell[] n=new HCell[8];
   n[0]=getCell(c.x,c.y+1);
   n[1]=getCell(c.x+1,c.y+1);
   n[2]=getCell(c.x+1,c.y);
   n[3]=getCell(c.x+1,c.y-1);
   n[4]=getCell(c.x,c.y-1);
   n[5]=getCell(c.x-1,c.y-1);
   n[6]=getCell(c.x-1,c.y);
   n[7]=getCell(c.x-1,c.y+1);
   return n;}
  
  public Iterator<HCell> iterator(){
    return new HCellSystemCellIterator(this);}
  
  /*
   * return the cell that contains the specified point
   * the cell's coors are also the cell's center point
   * the cell's square spans cell.x-0.5 to cell.y+0.5 and cell.y-0.5 to cell.y+0.5
   */
  public HCell getCellContainingPoint(double x,double y){
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
   * ################################
   */
  
  private void doMappedThings(List<HCSMappedThing> mappedthings){
    for(HCSMappedThing t:mappedthings){
      if(t.hasTags("margin")){
        mapMargin(t);
      }else if(t.hasTags("leaf")){
        mapPolygonArea(t);
      }else if(t.hasTags("boiled")){
        mapPolygonBoiledEdge(t);
      }else{
        throw new IllegalArgumentException("mapping thing failed");}}}
  
  private PolygonAreaHCells mapPolygonArea(HCSMappedThing t){
    PolygonAreaHCells c=new PolygonAreaHCells(((FPolygon)t.thing).getDPolygon(),t.transform);
    HCell b;
    for(HCell a:c){
        b=cells[a.x][a.y];
        b.thing=t;}
    return c;}
  
  private PolygonEdgeHCells mapPolygonBoiledEdge(HCSMappedThing t){
    PolygonEdgeHCells c=new PolygonEdgeHCells(((FPolygon)t.thing).getDPolygon(),t.transform);
    HCell b;
    for(HCell a:c){
        b=cells[a.x][a.y];
        b.thing=t;}
    return c;}
  
  private MarginHCells mapMargin(HCSMappedThing t){
    MarginHCells c=new MarginHCells(getWidth(),getHeight(),((FPolygon)t.thing).getDPolygon(),t.transform);
    HCell b;
    for(HCell a:c){
        b=cells[a.x][a.y];
        b.thing=t;}
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
