package org.fleen.bread.app.flerp0flerp2;

public class Fuzzball{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Fuzzball(FuzzballGun gun,double x,double y,double radius,double density){
    this.gun=gun;
    this.x=x;
    this.y=y;
    this.radius=radius;
    this.density=density;}
  
  /*
   * ################################
   * GUN
   * ################################
   */
  
  FuzzballGun gun;
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */

  public double x,y,radius,density;
  
}
