package org.fleen.bread.zCellSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.fleen.bread.hCellSystem.HCell;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;


/*
 * A polygon's shadow upon the raster cell array
 * all of the cells in which the Polygon manifests as a non-zero intensity Presence
 */
public class PolygonAreaZCells implements ZCellMass{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  PolygonAreaZCells(ZCSMappedThing mappedthing){
    this.mappedthing=mappedthing;
    initTransformedPolygon();
    mapPolygonArea();}
  
  /*
   * ################################
   * MAPPED THING
   * In this case, the area of a polygon
   * ################################
   */
  
  private ZCSMappedThing mappedthing;
  
  /*
   * ################################
   * POLYGON
   * ################################
   */
  
  private DPolygon transformedpolygon;
  
  private void initTransformedPolygon(){
    DPolygon polygon=((FPolygon)mappedthing.thing).getDPolygon();
    int s=polygon.size();
    transformedpolygon=new DPolygon(s);
    double[] a=new double[2];
    for(DPoint p:polygon){
      a[0]=p.x;
      a[1]=p.y;
      mappedthing.transform.transform(a,0,a,0,1);
      transformedpolygon.add(new DPoint(a));}}
  
  /*
   * ################################
   * GLOWSPAN
   * The distance to which a thing's presence bleeds out beyond its edge
   * ################################
   */
  
  private double getGlowSpan(){
    return mappedthing.glowspan;}
  
  /*
   * ################################
   * CELLS
   * ################################
   */
  
  private List<ZCell> cells=new ArrayList<ZCell>();
  
  public Iterator<ZCell> iterator(){
    return cells.iterator();}
  
  public int getCellCount(){
    return cells.size();}
  
  /*
   * ################################
   * MAP POLYGON AREA
   * create cell array to enclose polygon, with offsets : enclosingarray 
   *   And margins to allow for glow and safety
   * get edge of polygon using bresenhams : edgecells
   * 
   * get interior of polygon using floodfill
   *   
   * 
   * get outer edge using floodfill constrained to glowdistance
   * 
   * get inner edge using floodfill constrained to glowdistance
   *   
   * 
   * 
   * ################################
   */
  
  static final int SAFETYOFFSET=3;//a bit of padding so we don't have to test for off-array cell queries
  ZCell[][] enclosingarray;
  int eaoffsetx,eaoffsety;
  Set<ZCell> 
    edgecells=new HashSet<ZCell>(),
    inneredge=new HashSet<ZCell>(),
    outeredge=new HashSet<ZCell>(),
    interior=new HashSet<ZCell>();
  
  int testthing;
  
  private void mapPolygonArea(){
    createEnclosingArray();
    initEdge();
    mapCellsToEnclosingArray(edgecells);
    doInterior();
//    initInnerAndOuterEdge();
//    testthing=2;
//    expand(inneredge);
//    testthing=3;
//    expand(outeredge);
//    doInterior();
    
    //set presence for outeredge
    // " for inner edge
    // " for interior
    
    copyCellsToList();
    
    //test
    for(ZCell c:cells)
      c.addPresence(mappedthing,1.0);}
  

  
  
  
  
  /*
   * copy the nonnull cells from the enclosing array to the cells list
   * apply offsets too
   */
  private void copyCellsToList(){
    ZCell c;
    for(int x=0;x<enclosingarray.length;x++){
      for(int y=0;y<enclosingarray[0].length;y++){
        if(enclosingarray[x][y]!=null){
          c=enclosingarray[x][y];
          c.x+=eaoffsetx;
          c.y+=eaoffsety;
          cells.add(c);}}}}
  
  private void mapCellsToEnclosingArray(Collection<ZCell> c){
    for(ZCell a:c){
      enclosingarray[a.x][a.y]=a;}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * DO INTERIOR
   * get a null interior cell
   * floodfill everything inside the edge
   * ++++++++++++++++++++++++++++++++
   */
  
  private void doInterior(){
    ZCell a=getNullInteriorCell();
    floodFill(a);}
  
  private ZCell getNullInteriorCell(){
    ZCell c0=new ZCell();
    for(ZCell c:edgecells){
      c0.x=c.x;
      c0.y=c.y-1;
      if(isNullInteriorCell(c0))return c0;
      c0.y=c.y+1;
      if(isNullInteriorCell(c0))return c0;
      c0.x=c.x-1;
      c0.y=c.y;
      if(isNullInteriorCell(c0))return c0;
      c0.x=c.x+1;
      if(isNullInteriorCell(c0))return c0;}
    throw new IllegalArgumentException("couldn't get a null interior cell");}
  
  private boolean isNullInteriorCell(ZCell c){
    if(enclosingarray[c.x][c.y]!=null)return false;
    return transformedpolygon.containsPoint(c.x+eaoffsetx,c.y+eaoffsety);}
  
  private void floodFill(ZCell a){
    Queue<ZCell> queue=new LinkedList<ZCell>();
    ZCell c;
    queue.add(a);
    while(!queue.isEmpty()) {
      c=queue.remove();
      if(floodFill_(c.x,c.y)){     
        queue.add(new ZCell(c.x,c.y-1)); 
        queue.add(new ZCell(c.x,c.y+1)); 
        queue.add(new ZCell(c.x-1,c.y)); 
        queue.add(new ZCell(c.x+1,c.y));}}}

  private boolean floodFill_(int x,int y){
    if(enclosingarray[x][y]!=null)return false;
    enclosingarray[x][y]=new ZCell(x,y);
    interior.add(enclosingarray[x][y]);
    
    //TEST
    enclosingarray[x][y].itest=4;
    
    return true;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * EXPAND INNER AND OUTER EDGE
   * get all the cells adjacent to the edge cells
   * then get all the cells adjacent to those cells
   * and so on
   * for a number of cycles to encompass glow
   * ++++++++++++++++++++++++++++++++
   */
  
  private void expand(Set<ZCell> edgecells){
    Set<ZCell> 
      active=new HashSet<ZCell>(edgecells),
      newactive=new HashSet<ZCell>();
    int expandcyclescount=getExpandCyclesCount();
    for(int i=0;i<expandcyclescount;i++){
      for(ZCell c:active){
        newactive.addAll(getNullNeighbors(c));}
      mapCellsToEnclosingArray(newactive);
      edgecells.addAll(newactive);
      active.clear();
      active.addAll(newactive);
      newactive.clear();}}
  
  private int getExpandCyclesCount(){
    int a=(int)(Math.ceil(getGlowSpan())+1);
    return a;}
  
  private List<ZCell> getNullNeighbors(ZCell c){
    List<ZCell> n=new ArrayList<ZCell>(8);
    if(enclosingarray[c.x-1][c.y+1]==null)n.add(new ZCell(c.x-1,c.y+1));
    if(enclosingarray[c.x][c.y+1]==null)n.add(new ZCell(c.x,c.y+1));
    if(enclosingarray[c.x+1][c.y+1]==null)n.add(new ZCell(c.x+1,c.y+1));
    if(enclosingarray[c.x+1][c.y]==null)n.add(new ZCell(c.x+1,c.y));
    if(enclosingarray[c.x+1][c.y-1]==null)n.add(new ZCell(c.x+1,c.y-1));
    if(enclosingarray[c.x][c.y-1]==null)n.add(new ZCell(c.x,c.y-1));
    if(enclosingarray[c.x-1][c.y-1]==null)n.add(new ZCell(c.x-1,c.y-1));
    if(enclosingarray[c.x-1][c.y]==null)n.add(new ZCell(c.x-1,c.y));
    
    //test
    for(ZCell f:n)
      f.itest=testthing;
    
    return n;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * INIT INNER AND OUTER EDGE
   * for all the cells in edgecells
   * test for polygon.contained
   * ++++++++++++++++++++++++++++++++
   */
  
  private void initInnerAndOuterEdge(){
    for(ZCell c:edgecells){
      if(transformedpolygon.containsPoint(c.x+eaoffsetx,c.y+eaoffsety)){
        c.itest=2;
        inneredge.add(c);
      }else{
        c.itest=3;
        outeredge.add(c);}}
  
    System.out.println("inneredge="+inneredge.size());
    System.out.println("outeredge="+outeredge.size());
  
  }
  
  /*
   * ++++++++++++++++++++++++++++++++
   * DO EDGE ADJACENTS
   * get the cells adjacent to the edge cells
   * add them to the edge cells set
   * don't even test them or anything
   * ++++++++++++++++++++++++++++++++
   */
  
  private void doEdgeAdjacents(){
    List<ZCell> a=new ArrayList<ZCell>();
    for(ZCell b:edgecells)
      a.addAll(getAdjacents(b));
    edgecells.addAll(a);}
  
  private List<ZCell> getAdjacents(ZCell c){
    List<ZCell> a=new ArrayList<ZCell>(8);
    a.add(new ZCell(c.x-1,c.y+1));
    a.add(new ZCell(c.x,c.y+1));
    a.add(new ZCell(c.x+1,c.y+1));
    a.add(new ZCell(c.x+1,c.y));
    a.add(new ZCell(c.x+1,c.y-1));
    a.add(new ZCell(c.x,c.y-1));
    a.add(new ZCell(c.x-1,c.y-1));
    a.add(new ZCell(c.x-1,c.y));
    return a;}
  
  
  /*
   * ++++++++++++++++++++++++++++++++
   * INIT EDGE CELLS
   * get the edge cells with bresenhams
   * ++++++++++++++++++++++++++++++++
   */
  
  private void initEdge(){
    int s=transformedpolygon.size(),i1;
    ZCell c0,c1;
    DPoint p0,p1;
    for(int i0=0;i0<s;i0++){
      //get end points of a side seg of the polygon
      i1=i0+1;
      if(i1==s)i1=0;
      p0=transformedpolygon.get(i0);
      p1=transformedpolygon.get(i1);
      c0=deGetCellAtPoint(p0.x,p0.y);
      c1=deGetCellAtPoint(p1.x,p1.y);
      bresenhamSupercoverSegDraw(c0.x,c0.y,c1.x,c1.y);}}
  
  private ZCell deGetCellAtPoint(double x,double y){
    if(x-Math.floor(x)<0.5)
      x=Math.floor(x);
    else
      x=Math.ceil(x);
    if(y-Math.floor(y)<0.5)
      y=Math.floor(y);
    else
      y=Math.ceil(y);
    return new ZCell((int)x-eaoffsetx,(int)y-eaoffsety);}
  
  /*
   * BRESENHAM SUPERCOVER LINE DRAW
   * 
   * use Bresenham-like algorithm to address a line of squares from (y1,x1) to (y2,x2) 
   * The difference from Bresenham is that ALL the points of the line are 
   * printed, not only one per x coordinate. 
   * Principles of the Bresenham's algorithm (heavily modified) were taken from: 
   * http://www.intranet.ca/~sshah/waste/art7.html 
   */
  void bresenhamSupercoverSegDraw(int x0,int y0,int x1,int y1){
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
            edgecells.add(new ZCell(x,y-ystep));
          }else if(error + errorprev > ddx){  // left square also 
            edgecells.add(new ZCell(x-xstep,y));
          }else{  // corner: bottom and left squares also 
            edgecells.add(new ZCell(x,y-ystep));
            edgecells.add(new ZCell(x-xstep,y));}} 
        edgecells.add(new ZCell(x,y));
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
            edgecells.add(new ZCell(x-xstep,y));
          }else if (error + errorprev > ddy){ 
            edgecells.add(new ZCell(x,y-ystep));
          }else{ 
            edgecells.add(new ZCell(x-xstep,y));
            edgecells.add(new ZCell(x,y-ystep));}}
        edgecells.add(new ZCell(x,y));
        errorprev = error;}}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * ENCLOSING ARRAY
   * ++++++++++++++++++++++++++++++++
   */
  
  private void createEnclosingArray(){
    List<ZCell> vertexcells=new ArrayList<ZCell>(transformedpolygon.size());
    for(DPoint p:transformedpolygon)
      vertexcells.add(ceaGetCellAtPoint(p.x,p.y));
    //
    int xmin=Integer.MAX_VALUE,xmax=Integer.MIN_VALUE,ymin=Integer.MAX_VALUE,ymax=Integer.MIN_VALUE;
    for(ZCell c:vertexcells){
      if(c.x<xmin)xmin=c.x;
      if(c.x>xmax)xmax=c.x;
      if(c.y<ymin)ymin=c.y;
      if(c.y>ymax)ymax=c.y;}
    //pad it out for glowspan and safety
    int padding=(int)Math.ceil(getGlowSpan()+SAFETYOFFSET);
    xmin-=padding;
    xmax+=padding;
    ymin-=padding;
    ymax+=padding;
    //
    eaoffsetx=xmin;
    eaoffsety=ymin;
    enclosingarray=new ZCell[xmax-xmin+1][ymax-ymin+1];}
  
  private ZCell ceaGetCellAtPoint(double x,double y){
    if(x-Math.floor(x)<0.5)
      x=Math.floor(x);
    else
      x=Math.ceil(x);
    if(y-Math.floor(y)<0.5)
      y=Math.floor(y);
    else
      y=Math.ceil(y);
    return new ZCell((int)x,(int)y);}
  
  
}