package org.fleen.bread.cellSystem;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;

/*
 * get edge cells via bresenham : c0
 * get cells neighboring c0 : c1
 * and so on : c2, c3... a few times. 
 *   Out to cell layers count = glow + 1 or something
 * Mark all cells with presences according to distance from edge (inward or outward)
 *  
 */
public class PolygonEdgeCells implements CellMass{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  PolygonEdgeCells(DPolygon polygon,AffineTransform transform,double glowspan){
    this.polygon=polygon;
    initTransformedPolygon(transform);
    mapPolygonEdge();}
  
  /*
   * ################################
   * POLYGON
   * ################################
   */
  
  DPolygon 
    polygon,
    transformedpolygon;//x0,y0,x1,y1...
  
  private void initTransformedPolygon(AffineTransform t){
    int s=polygon.size();
    transformedpolygon=new DPolygon(s);
    double[] a=new double[2];
    for(DPoint p:polygon){
      a[0]=p.x;
      a[1]=p.y;
      t.transform(a,0,a,0,1);
      transformedpolygon.add(new DPoint(a));}}
  
  /*
   * ################################
   * CELLS
   * ################################
   */
  
  private Set<Cell> cells;
  
  /*
   * TODO faster
   */
  public Cell getCell(int x,int y){
    for(Cell c:cells)
      if(c.x==x&&c.y==y)
        return c;
    return null;}
  
  public int getCellCount(){
    return cells.size();}
  
  public Collection<Cell> getCells(){
    return cells;}
  
  Cell getCellContainingPoint(double x,double y){
    if(x-Math.floor(x)<0.5)
      x=Math.floor(x);
    else
      x=Math.ceil(x);
    if(y-Math.floor(y)<0.5)
      y=Math.floor(y);
    else
      y=Math.ceil(y);
    return new Cell((int)x,(int)y);}
  
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
    return cells.iterator();}
  
  /*
   * ################################
   * MAP POLYGON EDGE
   * Get edge cells with supercover bresenham
   * ################################
   */
  
  private void mapPolygonEdge(){
    cells=getEdgeCells();
    for(Cell c:cells)
      c.thing=polygon;}
  
  private Set<Cell> getEdgeCells(){
    Set<Cell> cells=new HashSet<Cell>();
    int s=polygon.size(),i1;
    Cell c0,c1;
    DPoint p0,p1;
    for(int i0=0;i0<s;i0++){
      //get end points of a side seg of the polygon
      i1=i0+1;
      if(i1==s)i1=0;
      p0=transformedpolygon.get(i0);
      p1=transformedpolygon.get(i1);
      c0=getCellContainingPoint(p0.x,p0.y);
      c1=getCellContainingPoint(p1.x,p1.y);
      cells.addAll(getSegCells(c0.x,c0.y,c1.x,c1.y));}
    return cells;}
  
  /*
   * PSEUDOBRESENHAM SUPERCOVER LINE DRAW
   * 
   * use Bresenham-like algorithm to address a line of squares from (y1,x1) to (y2,x2) 
   * The difference from Bresenham is that ALL the points of the line are 
   * printed, not only one per x coordinate. 
   * Principles of the Bresenham's algorithm (heavily modified) were taken from: 
   * http://www.intranet.ca/~sshah/waste/art7.html 
   */
  List<Cell> getSegCells(int x0,int y0,int x1,int y1){
    List<Cell> segcells=new ArrayList<Cell>();
    int i;               // loop counter 
    int ystep, xstep;    // the step on y and x axis 
    int error;           // the error accumulated during the increment 
    int errorprev;       // vision the previous value of the error variable 
    int y = y0, x = x0;  // the line points 
    double ddy, ddx;        // compulsory variables: the double values of dy and dx 
    int dx = x1 - x0; 
    int dy = y1 - y0;
//    segcells.add(cells[x][y]); //skip the first cell, otherwise some cells get selected twice
    // NB the last point can't be here, because of its previous point (which has to be verified) 
    if (dy < 0){ 
      ystep = -1; 
      dy = -dy; 
    }else 
      ystep = 1; 
    if (dx < 0){ 
      xstep = -1; 
      dx = -dx; 
    }else 
      xstep = 1; 
    ddy = 2 * dy;  // work with double values for full precision 
    ddx = 2 * dx; 
    if (ddx >= ddy){  // first octant (0 <= slope <= 1) 
      // compulsory initialization (even for errorprev, needed when dx==dy) 
      errorprev = error = dx;  // start in the middle of the square 
      for (i=0 ; i < dx ; i++){  // do not use the first point (already done) 
        x += xstep; 
        error += ddy; 
        if (error > ddx){  // increment y if AFTER the middle ( > ) 
          y += ystep; 
          error -= ddx; 
          // three cases (octant == right->right-top for directions below): 
          if (error + errorprev < ddx){  // bottom square also
            segcells.add(new Cell(x,y-ystep));
          }else if(error + errorprev > ddx){  // left square also 
            segcells.add(new Cell(x-xstep,y));
          }else{  // corner: bottom and left squares also 
            segcells.add(new Cell(x,y-ystep));
            segcells.add(new Cell(x-xstep,y));}} 
        segcells.add(new Cell(x,y));
        errorprev = error;} 
    }else{// the same as above 
      errorprev = error = dy; 
      for (i=0 ; i < dy ; i++){ 
        y += ystep; 
        error += ddx; 
        if (error > ddy){ 
          x += xstep; 
          error -= ddy; 
          if (error + errorprev < ddy){ 
            segcells.add(new Cell(x-xstep,y));
          }else if (error + errorprev > ddy){ 
            segcells.add(new Cell(x,y-ystep));
          }else{ 
            segcells.add(new Cell(x-xstep,y));
            segcells.add(new Cell(x,y-ystep));}}
        segcells.add(new Cell(x,y));
        errorprev = error;}}
    return segcells;}
  
}