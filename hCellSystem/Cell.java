package org.fleen.bread.hCellSystem;

public class Cell{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  Cell(int x,int y){
    this.x=x;
    this.y=y;}
  
  Cell(int x,int y,Object gpobject){
    this.x=x;
    this.y=y;
    this.gpobject=gpobject;}
  
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
   * MAPPED THING
   * The thing associated with this cell. It lends identity, and possibly form, to a mass of cells
   * probably a polygon, or a polygon-edge, but it could be other things too
   * the relationship between the thing and the cells is arbitrary, to be determined by specific case
   * ################################
   */
  
  public MappedThing thing=null;
  
  /*
   * ################################
   * GENERAL PURPOSE OBJECT FOR WHATEVER
   * it's useful 
   * ################################
   */
  
  public Object gpobject=null;
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  private static final int PRIME=104729;
  
  public int hashCode(){
    return x+y*PRIME;}
  
  public boolean equals(Object a){
    Cell b=(Cell)a;
    return b.x==x&&b.y==y;}

}
