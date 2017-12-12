package org.fleen.bread.zCellSystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
  
  ZCell(int x,int y,ZCSMappedThing thing,double intensity){
    this(x,y);
    addPresence(thing,intensity);}
  
  ZCell(){}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  //coors within the cellarray
  //the cell center is also the cell coors
  public int x,y;
  
  /*
   * ################################
   * PRESENCES
   * The cell holds the presence of 0..n things
   * The things are polygons or polygon edges or whatever
   * ################################
   */
  
  //98% of cells will have just 1 presence
  //1% will have 2
  //1% will have 2..6
  //right?
  private static final int INITPRESENCELISTSIZE=6;
  
  public List<ZCSMappedThingPresence> presences=new ArrayList<ZCSMappedThingPresence>(INITPRESENCELISTSIZE);
  
  void addPresence(ZCSMappedThingPresence p){
    ZCSMappedThingPresence a=getPresence(p.thing);
    if(a!=null){
      a.intensity+=p.intensity;
    }else{
      presences.add(p);}}
  
  void addPresence(ZCSMappedThing thing,double intensity){
    addPresence(new ZCSMappedThingPresence(thing,intensity));}
  
  /*
   * intensity is assumed to be 1.0
   * we use this for polygon interiors.
   */
  void addPresence(ZCSMappedThing thing){
    addPresence(thing,1.0);}
  
  void addPresences(List<ZCSMappedThingPresence> presences){
    for(ZCSMappedThingPresence p:presences)
      addPresence(p);}
  
  /*
   * returns true if the specified thing has nonzero presence at this cell
   */
  boolean hasPresence(ZCSMappedThing thing){
    for(ZCSMappedThingPresence p:presences)
      if(p.thing==thing)
        return true;
    return false;}
  
  /*
   * get the presence of the thing at this cell
   * if the thing has no presence at this cell then return null
   */
  ZCSMappedThingPresence getPresence(ZCSMappedThing thing){
    for(ZCSMappedThingPresence p:presences)
      if(p.thing==thing)
        return p;
    return null;}
  
  /*
   * return the intensity of the specified thing's presence at this cell
   * if the thing has no presence at this cell then return 0
   */
  double getPresenceIntensity(ZCSMappedThing thing){
    for(ZCSMappedThingPresence p:presences)
      if(p.thing==thing)
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
    Iterator<ZCSMappedThingPresence> i=presences.iterator();
    ZCSMappedThingPresence p;
    while(i.hasNext()){
      p=i.next();
      if(p.intensity<ZEROISHINTENSITY){
        i.remove();}}}
  
  private void normalizePresenceIntensities(){
    double s=getPresenceIntensitySum();
    if(s>0){
      double n=1.0/s;
      for(ZCSMappedThingPresence p:presences)
        p.intensity*=n;}}
  
  /*
   * pre-normalization it's something >0
   * post normalization it should be 1.0
   */
  public double getPresenceIntensitySum(){
    double s=0;
    for(ZCSMappedThingPresence p:presences)
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
