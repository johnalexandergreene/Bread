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
public class PolygonAreaZCells implements ZCellMass{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  PolygonAreaZCells(DPolygon polygon,AffineTransform transform,double glowspan){
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
  
  private List<ZCell> cells=new ArrayList<ZCell>();
  
  public Iterator<ZCell> iterator(){
    return cells.iterator();}
  
  public int getCellCount(){
    return cells.size();}
  
  /*
   * ################################
   * MAP POLYGON AREA
   * create cell array to enclose polygon, with offsets. 
   *   And margins to allow for glow and safety
   * draw edge of polygon using bresenhams
   * get interior and exterior layers from that edge, out to glowspan
   * get interior cells with floodfill
   * ################################
   */
  
  private void mapPolygonArea(){
    createEnclosingArray();
    
  }
  
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
    xmin-=(glowspan+SAFETYOFFSET);
    xmax+=(glowspan+SAFETYOFFSET);
    ymin-=(glowspan+SAFETYOFFSET);
    ymax+=(glowspan+SAFETYOFFSET);
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