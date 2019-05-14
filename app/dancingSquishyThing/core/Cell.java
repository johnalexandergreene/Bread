package org.fleen.bread.app.dancingSquishyThing.core;

public class Cell{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Cell(double distance){
    this.distance=distance;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  //distance of this cell from from whelmer center in terms of whelmer radius (ie span/2), range [0,1]
  public double distance;
  
  /*
   * ################################
   * GET DELTA
   * ################################
   */
  
  /*
   * The delta is the sum of ring deltas at this cell
   * what is it? 0.001? 20? 500? I dunno.
   */
//  public double getDelta(Whelmer whelmer){
//    double delta=0;
//    for(Ring_Simple ring:whelmer.controller)
//      delta+=ring.getDelta(distance);
//    return delta;}

}
