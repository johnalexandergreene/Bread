package org.fleen.bread.app.communityExhibition2019_11_27;

import java.util.Random;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DVector;
import org.fleen.geom_2D.GD;

public class Fuzzball{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Fuzzball(FuzzballSystem system){
    this.system=system;
    init();}
  
  Fuzzball(FuzzballSystem system,int x,int y){
    this.system=system;
    init();
    this.x=x;
    this.y=y;}
  
  /*
   * ################################
   * SYSTEM
   * ################################
   */
  
  FuzzballSystem system;
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */

  double x,y;
  double radius,density;
  DVector v;
  
  public void move(){
    DVector w=new DVector(system.windspeed,system.winddirection);
    w.add(v);
    DPoint p=new DPoint(x,y);
    p.applyVector(w);
    x=p.x;
    y=p.y;}
  
  /*
   * ################################
   * LIFE AND DEATH
   * ################################
   */
 
  void init(){
    double r=system.radius*FuzzballSystem.ENTRYRADIUS;
    double d=rnd.nextDouble()*GD.PI2;
    double[] p=GD.getPoint_PointDirectionInterval(system.centerx,system.centery,d,r);
    x=p[0];
    y=p[1];
    initRadius();
    initDensity();
    initVector();}
  
  //TODO
  //there should be a tendancy towards a mean
  void initRadius(){
    radius=rnd.nextDouble()*99+29;
  }
  
  //TODO
  //there should be a tendancy towards a mean
  void initDensity(){
    density=rnd.nextDouble()*100+29;
  }
  
  static final double 
    VECTORMAGMIN=0.1,
    VECTORMAGMAX=3;
  
  //TODO
  //there should be a tendancy towards a mean
  void initVector(){
    double m=rnd.nextDouble()*(VECTORMAGMAX-VECTORMAGMIN)+VECTORMAGMIN;
    double d=GD.getDirection_PointPoint(x,y,system.centerx,system.centery);
    double a=rnd.nextDouble()*GD.HALFPI;
    if(rnd.nextBoolean())a=-a;
    d+=a;
    d=GD.normalizeDirection(d);
    v=new DVector(d,m);}
  
  boolean destroyMe(){
    double d=GD.getDistance_PointPoint(x,y,system.centerx,system.centery);
    double exitradius=system.radius*FuzzballSystem.EXITRADIUS;
    return d>exitradius;}
  
  /*
   * ################################
   * STROBE
   * ################################
   */
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  private Random rnd=new Random();
  

}
