package org.fleen.bread.zCellSystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fleen.geom_2D.DPolygon;

/*
 * A cell has a location in the array (x,y) and a number of polygon presences
 * A polygon presence is the presence of a polygon. 
 *   A reference to a polygon and a value describing how present that polygon is range : [0,1]
 * Presences come in 2 varieties
 * Interior presence means that the cell is inside the polygon, presence is 1.0
 * Edge presence means that the cell is near the edge of the polygon.
 *   Right at the edge presence is 0.5. 
 *   Move inward and the presence increases to 1.0
 *   Move outward and the presence decreases to 0.0 
 * 
 */
public class ZCell{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  ZCell(int x,int y){
    this.x=x;
    this.y=y;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  //coors within the cellarray
  //the cell center is also the cell coors
  public int x,y;
  
//  List<Cell> getNeighbors(){
//    List<Cell> n=new ArrayList<Cell>(8);
//    //N
//    Cell a=rds.getCell(x,y+1);
//    if(a!=null)n.add(a);
//    //NE
//    a=rds.getCell(x+1,y+1);
//    if(a!=null)n.add(a);
//    //E
//    a=rds.getCell(x+1,y);
//    if(a!=null)n.add(a);
//    //SE
//    a=rds.getCell(x+1,y-1);
//    if(a!=null)n.add(a);
//    //S
//    a=rds.getCell(x,y-1);
//    if(a!=null)n.add(a);
//    //SW
//    a=rds.getCell(x-1,y-1);
//    if(a!=null)n.add(a);
//    //W
//    a=rds.getCell(x-1,y);
//    if(a!=null)n.add(a);
//    //NW
//    a=rds.getCell(x-1,y+1);
//    if(a!=null)n.add(a);
//    //
//    return n;}
  
  /*
   * this is for getting neighbor cells in a polygoncellmap creation process
   * we sometimes want to refer to cells that are in the polygoncellmap.cellcache but not
   * in the rastermap. So we check the cache first.
   */
  List<ZCell> getNeighbors(ZCellMass m){
    List<ZCell> n=new ArrayList<ZCell>(8);
    //N
    ZCell a=m.getCell(x,y+1);
    if(a!=null)n.add(a);
    //NE
    a=m.getCell(x+1,y+1);
    if(a!=null)n.add(a);
    //E
    a=m.getCell(x+1,y);
    if(a!=null)n.add(a);
    //SE
    a=m.getCell(x+1,y-1);
    if(a!=null)n.add(a);
    //S
    a=m.getCell(x,y-1);
    if(a!=null)n.add(a);
    //SW
    a=m.getCell(x-1,y-1);
    if(a!=null)n.add(a);
    //W
    a=m.getCell(x-1,y);
    if(a!=null)n.add(a);
    //NW
    a=m.getCell(x-1,y+1);
    if(a!=null)n.add(a);
    //
    return n;}
  
  /*
   * ################################
   * PRESENCES
   * The cell holds the presence of 0..n things
   * The things are polygons or polygon edges or whatever
   * ################################
   */
  
  //we make it just bigger than the biggest size we expect
  private static final int INITPRESENCELISTSIZE=9;
  
  public List<Presence> presences=new ArrayList<Presence>(INITPRESENCELISTSIZE);
  
  void addPresence(Presence p){
    presences.add(p);}
  
  void addPresence(DPolygon polygon,double intensity){
    addPresence(new Presence(polygon,intensity));}
  
  /*
   * intensity is assumed to be 1.0
   * we use it for polygon interiors.
   */
  void addPresence(DPolygon polygon){
    addPresence(polygon,1.0);}
  
  /*
   * returns true if the specified polygon has nonzero presence at this cell
   */
  boolean hasPresence(DPolygon polygon){
    for(Presence p:presences)
      if(p.polygon==polygon)
        return true;
    return false;}
  
  /*
   * return the intensity of the specified polygon's presence at this cell
   */
  double getPresenceIntensity(DPolygon polygon){
    for(Presence p:presences)
      if(p.polygon==polygon)
        return p.intensity;
    return 0;}
  
  /*
   * ################################
   * CLEAN
   * remove zero intensity presences
   * normalize presence intensities 
   * ################################
   */
  
  private static final double ZEROISHINTENSITY=0.0000001;
  
  public void clean(){
    cullZeroIntensityPresences();
    normalizePresenceIntensities();}
  
  private void cullZeroIntensityPresences(){
    Iterator<Presence> i=presences.iterator();
    Presence p;
    while(i.hasNext()){
      p=i.next();
      if(p.intensity<ZEROISHINTENSITY){
        i.remove();}}}
  
  private void normalizePresenceIntensities(){
    double s=getPresenceIntensitySum();
    if(s>0){
      double n=1.0/s;
      for(Presence p:presences)
        p.intensity*=n;}}
  
  /*
   * pre-normalization it's something >0
   * post normalization it should be 1.0
   */
  public double getPresenceIntensitySum(){
    double s=0;
    for(Presence p:presences)
      s+=p.intensity;
    return s;}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  private static final int PRIME=104729;
  
  public int hashCode(){
    return x+y*PRIME;}
  
  public boolean equals(Object a){
    ZCell b=(ZCell)a;
    return b.x==x&&b.y==y;}

}
