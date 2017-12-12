package org.fleen.bread.zCellSystem;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
public class ZCells_FPolygonBoiledEdge implements ZCellMass{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  ZCells_FPolygonBoiledEdge(DPolygon polygon,AffineTransform transform,double glowspan){
    this.polygon=polygon;
    this.glowspan=glowspan;
    initTransformedPolygon(transform);
    doCells();}
  
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
  
  /*
   * when we're getting cells we first look in the local cache
   * if the cell isn't there then we get it from the raster map (from the cells array or create it) and stick it in the local cache
   */
  Map<ZCellKey,ZCell> cells=new HashMap<ZCellKey,ZCell>();
  
  /*
   * first check locally for the cell, then check the rds 
   */
  public ZCell getCell(int x,int y){
    ZCellKey k=new ZCellKey(x,y);
    ZCell c=cells.get(k);
    if(c==null){
      c=new ZCell(x,y);
      cells.put(k,c);}
    return c;}
  
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
  
  public int getCellCount(){
    return cells.size();}
  
  public Collection<ZCell> getCells(){
    return cells.values();}
  
  /*
   * ################################
   * GATHER AND MARK CELLS FOR THE PRESENCE OF THE THING
   * get the primaryedgecells : c0 
   * mark them
   * get all the unmarked neighbors of primaryedgecells : c1
   * mark them.
   * keep doing that out to a number of cell-layers
   * ################################
   */
  
  Set<ZCell> primaryedgecells=new HashSet<ZCell>();
  List<Set<ZCell>> otheredgecelllayers=new ArrayList<Set<ZCell>>();
  
  private void doCells(){
    doPrimaryEdgeCells();
    doOtherCells();}
  
  private void doOtherCells(){
    int count=(int)(glowspan/ZCellSystem.CELLSPAN)+1;
    Set<ZCell> layer=primaryedgecells;
    for(int i=0;i<count;i++){
      layer=getLayerOfUnmarkedCells(layer);
      markCells(layer);
      otheredgecelllayers.add(layer);}}
  
  private void markCells(Collection<ZCell> cells){
    double dis,presence;
    for(ZCell c:cells){
      dis=transformedpolygon.getDistance(c.x,c.y);
      if(dis>glowspan)
        presence=0;
      else
        presence=0.5-(dis/glowspan)*0.5;
      c.addPresence(polygon,presence);}}
  
  /*
   * Given a collection of marked cells
   * Get all neighbors of those cells that are unmarked with the polygon's presence
   */
  Set<ZCell> getLayerOfUnmarkedCells(Collection<ZCell> cells){
    Set<ZCell> unmarkedcells=new HashSet<ZCell>();
    for(ZCell c:cells)
      for(ZCell d:c.getNeighbors(this))
        if(!d.hasPresence(polygon))
          unmarkedcells.add(d);
    return unmarkedcells;}
  
  /*
   * ################################
   * DO PRIMARY EDGE CELLS
   * Given a polygon, draw the polygon onto the raster 
   * (using PSEUDOBRESENHAM SUPERCOVER LINE DRAW) 
   * and list the cells that get intersected.
   * ################################
   */
  
  /*
   * do the cells right on the edge-line of the polygon
   */
  private void doPrimaryEdgeCells(){
    int s=polygon.size(),i1;
    ZCell c0,c1;
    DPoint p0,p1;
    for(int i0=0;i0<s;i0++){
      //get end points of a side seg of the polygon
      i1=i0+1;
      if(i1==s)i1=0;
      p0=transformedpolygon.get(i0);
      p1=transformedpolygon.get(i1);
      c0=getCellContainingPoint(p0.x,p0.y);
      c1=getCellContainingPoint(p1.x,p1.y);
      primaryedgecells.addAll(getSegCells(c0.x,c0.y,c1.x,c1.y));}
    markCells(primaryedgecells);}
  
  /*
   * PSEUDOBRESENHAM SUPERCOVER LINE DRAW
   * 
   * use Bresenham-like algorithm to address a line of squares from (y1,x1) to (y2,x2) 
   * The difference from Bresenham is that ALL the points of the line are 
   * printed, not only one per x coordinate. 
   * Principles of the Bresenham's algorithm (heavily modified) were taken from: 
   * http://www.intranet.ca/~sshah/waste/art7.html 
   */
  List<ZCell> getSegCells(int x0,int y0,int x1,int y1){
    List<ZCell> segcells=new ArrayList<ZCell>();
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
            segcells.add(getCell(x,y-ystep));
          }else if(error + errorprev > ddx){  // left square also 
            segcells.add(getCell(x-xstep,y));
          }else{  // corner: bottom and left squares also 
            segcells.add(getCell(x,y-ystep));
            segcells.add(getCell(x-xstep,y));}} 
        segcells.add(getCell(x,y));
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
            segcells.add(getCell(x-xstep,y));
          }else if (error + errorprev > ddy){ 
            segcells.add(getCell(x,y-ystep));
          }else{ 
            segcells.add(getCell(x-xstep,y));
            segcells.add(getCell(x,y-ystep));}}
        segcells.add(getCell(x,y));
        errorprev = error;}}
    return segcells;}
  
}