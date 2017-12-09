package org.fleen.bread.zCellSystem;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fleen.bread.hCellSystem.HCell;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;


/*
 * A polygon's shadow upon the raster cell array
 * all of the cells in which the Polygon manifests as a non-zero intensity Presence
 */
public class PolygonAreaZCells2 implements ZCellMass{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  PolygonAreaZCells2(DPolygon polygon,AffineTransform transform,double glowspan){
    this.polygon=polygon;
    this.glowspan=glowspan;
    initTransformedPolygon(transform);
    mapPolygonArea();}
  
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
  
  Map<ZCellKey,ZCell> cells=new HashMap<ZCellKey,ZCell>();
  
  //the cells on the edge-line of the polygon
  Set<ZCell> primaryedgecells=new HashSet<ZCell>();
  
  List<Set<ZCell>>
    //the layers of cells near the edge of the polygon but inside
    edgeinteriorlayers=new ArrayList<Set<ZCell>>(),
    //the layers of cells near the edge of the polygon but outside
    edgeexteriorlayers=new ArrayList<Set<ZCell>>(),
    //the layers of cells starting just inside the innermost interior edge layer 
    //and ending at the center of the polygon 
    interiorlayers=new ArrayList<Set<ZCell>>();

  private void mapPolygonArea(){
    doPrimaryEdgeCells();
    doOtherEdgeCells();
    doInteriorCells();}
  
  /*
   * check the cell cache first
   * if the cell isn't there then create it 
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
  
  public Iterator<ZCell> iterator(){
    return cells.values().iterator();}
  
  /*
   * ################################
   * DO INTERIOR CELLS
   * ################################
   */
  
  private void doInteriorCells(){
    Set<ZCell> layer=edgeinteriorlayers.get(edgeinteriorlayers.size()-1);
    while(!layer.isEmpty()){
      layer=getLayerOfUnmarkedCells(layer);
      markInteriorCells(layer);
      interiorlayers.add(layer);}}
  
  /*
   * ################################
   * DO OTHER EDGE CELLS
   * consider the primaryedgecells. gathered into a list and marked with the polygon's presence.
   * 
   * get all the unmarked neighbors of primaryedgecells
   * mark them, testing for interiorexterior and distance
   * 
   * separate them into interior and exterior
   * 
   * that's the first interior and exterior edge cells layers
   * 
   * Then use each of those layers to derive the next layer, and so on
   * 
   * ################################
   */
  
  private void doOtherEdgeCells(){
    //get the first inner and outer edge cell layers
    Set<ZCell> firstinnerouteredgelayer=getLayerOfUnmarkedCells(primaryedgecells);
    markEdgeCells(firstinnerouteredgelayer);
    Set<ZCell> 
      inlayer=new HashSet<ZCell>(),
      exlayer=new HashSet<ZCell>();
    for(ZCell c:firstinnerouteredgelayer){
      if(c.getPresenceIntensity(polygon)>0.5)
        inlayer.add(c);
      else
        exlayer.add(c);}
    edgeinteriorlayers.add(inlayer);
    edgeexteriorlayers.add(exlayer);
    //now the first interior and exterior edge layers are done
    //get the number of additional edge layers to do
    int additionaledgelayerscount=(int)(glowspan/ZCellSystem.CELLSPAN)+1;
    doAdditionalInteriorEdgeLayers(inlayer,additionaledgelayerscount);
    doAdditionalExteriorEdgeLayers(exlayer,additionaledgelayerscount);}
  
  private void doAdditionalInteriorEdgeLayers(Set<ZCell> inlayer,int count){
    Set<ZCell> layer=inlayer;
    for(int i=0;i<count;i++){
      layer=getLayerOfUnmarkedCells(layer);
      markInteriorEdgeCells(layer);
      edgeinteriorlayers.add(layer);}}
  
  private void doAdditionalExteriorEdgeLayers(Set<ZCell> exlayer,int count){
    Set<ZCell> layer=exlayer;
    for(int i=0;i<count;i++){
      layer=getLayerOfUnmarkedCells(layer);
      markExteriorEdgeCells(layer);
      edgeexteriorlayers.add(layer);}}
  
  /*
   * ################################
   * DO PRIMARY EDGE CELLS
   * 
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
    markEdgeCells(primaryedgecells);}
  
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
  
  /*
   * ################################
   * MARK CELLS
   * ################################
   */
  
  /*
   * Given a collection of cells and a polygon and no other
   * useful structure or clues, mark the presence of the polygon
   * upon the cells 
   */
  private void markEdgeCells(Collection<ZCell> cells){
    boolean isinterior;
    double dis,presence;
    for(ZCell c:cells){
      isinterior=transformedpolygon.containsPoint(c.x,c.y);
      dis=transformedpolygon.getDistance(c.x,c.y);
      if(isinterior)
        presence=0.5+(dis/glowspan)*0.5;
      else
        presence=0.5-(dis/glowspan)*0.5;
      if(presence<0)presence=0;
      if(presence>1)presence=1;
      c.addPresence(polygon,presence);}}
  
  private void markInteriorEdgeCells(Collection<ZCell> cells){
    double dis,presence;
    for(ZCell c:cells){
      dis=transformedpolygon.getDistance(c.x,c.y);
      if(dis>glowspan)
        presence=1.0;
      else
        presence=0.5+(dis/glowspan)*0.5;
      c.addPresence(polygon,presence);}}
  
  private void markExteriorEdgeCells(Collection<ZCell> cells){
    double dis,presence;
    for(ZCell c:cells){
      dis=transformedpolygon.getDistance(c.x,c.y);
      if(dis>glowspan)
        presence=0;
      else
        presence=0.5-(dis/glowspan)*0.5;
      c.addPresence(polygon,presence);}}
  
  private void markInteriorCells(Collection<ZCell> cells){
    for(ZCell c:cells)
      c.addPresence(polygon);}
  
  /*
   * ################################
   * UNMARKED CELL LAYER ACCQUIREMENT
   * 
   * Given a collection of marked (with the presence of the polygon) cells
   * Get all neighbors of those cells that are unmarked with the polygon's presence
   * ################################
   */
  
  Set<ZCell> getLayerOfUnmarkedCells(Collection<ZCell> cells){
    Set<ZCell> unmarkedcells=new HashSet<ZCell>();
    for(ZCell c:cells)
      for(ZCell d:getNeighbors(c))
        if(!d.hasPresence(polygon))
          unmarkedcells.add(d);
    return unmarkedcells;}
  
  private ZCell[] getNeighbors(ZCell c){
    HCell[] n=new HCell[8];
    n[0]=enclosingarray[c.x][c.y+1];
    if(n[0]==null)n[0]=new HCell(c.x,c.y+1,NULLTAG);
    n[1]=enclosingarray[c.x+1][c.y+1];
    if(n[1]==null)n[1]=new HCell(c.x+1,c.y+1,NULLTAG);
    n[2]=enclosingarray[c.x+1][c.y];
    if(n[2]==null)n[2]=new HCell(c.x+1,c.y,NULLTAG);
    n[3]=enclosingarray[c.x+1][c.y-1];
    if(n[3]==null)n[3]=new HCell(c.x+1,c.y-1,NULLTAG);
    n[4]=enclosingarray[c.x][c.y-1];
    if(n[4]==null)n[4]=new HCell(c.x,c.y-1,NULLTAG);
    n[5]=enclosingarray[c.x-1][c.y-1];
    if(n[5]==null)n[5]=new HCell(c.x-1,c.y-1,NULLTAG);
    n[6]=enclosingarray[c.x-1][c.y];
    if(n[6]==null)n[6]=new HCell(c.x-1,c.y,NULLTAG);
    n[7]=enclosingarray[c.x-1][c.y+1];
    if(n[7]==null)n[7]=new HCell(c.x-1,c.y+1,NULLTAG);
    return n;}
  
}