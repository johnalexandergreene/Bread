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
   * draw edge of polygon using bresenhams : edgecells
   * get cells adjacent to edgecells, add them to edgecells
   * test centerpoint of each of edgecells for inside polygon
   * cells outside polygon : outsideedge
   * cells inside polygon : insideedge
   * gather outside layers to outsideedge. layercount=glowspan*C
   * gather inside layers to insideedge  
   * get a cell inside insideedge. use that as start for floodfill
   * do floodfill to get the rest of the area cells : inside 
   * set presences for inside cells to 1.0
   * set presences for outsideedge cells to inverse of polygondistance
   * set presences for insideedge cells to polygondistance
   * put all nonnull enclosingarray cells into the cells list.
   * ################################
   */
  
  private void mapPolygonArea(){
    createEnclosingArray();
    doEdgeCells();
    
    //test
//    for(ZCell c:edgecells)
//      c.addPresence(thing);
    
  }
  
  /*
   * ++++++++++++++++++++++++++++++++
   * EDGE CELLS
   * ++++++++++++++++++++++++++++++++
   */
  
  private Set<ZCell> edgecells=new HashSet<ZCell>();
  
  private void doEdgeCells(){
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
      bresenhamSegDraw(c0.x,c0.y,c1.x,c1.y);}}
  
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
  
  private void bresenhamSegDraw(int x0,int y0,int x1,int y1){
    ZCell cell;
    int w=x1-x0;
    int h=y1-y0;
    int dx1=0,dy1=0,dx2=0,dy2=0;
    if(w<0)
      dx1=-1; 
    else if(w>0)
      dx1=1;
    if(h<0)
      dy1=-1; 
    else if(h>0) 
      dy1=1;
    if(w<0)
      dx2=-1; 
    else if(w>0) 
      dx2=1;
    int longest=Math.abs(w);
    int shortest=Math.abs(h);
    if(!(longest>shortest)){
      longest=Math.abs(h);
      shortest=Math.abs(w);
      if(h<0)
        dy2=-1; 
      else if(h>0) 
        dy2=1;
        dx2=0;}
    int numerator=longest>>1;
    for(int i=0;i<=longest;i++){
      cell=new ZCell(x0,y0);
      enclosingarray[x0][y0]=cell;
      edgecells.add(cell);
      numerator+=shortest;
      if(!(numerator<longest)){
        numerator-=longest;
        x0+=dx1;
        y0+=dy1;
      }else{
        x0+=dx2;
        y0+=dy2;}}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * ENCLOSING ARRAY
   * ++++++++++++++++++++++++++++++++
   */
  
  static final int SAFETYOFFSET=3;//so we don't have to test for off-array cell queries
  ZCell[][] enclosingarray;
  int eaoffsetx,eaoffsety;
  
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