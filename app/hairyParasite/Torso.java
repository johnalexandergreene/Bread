package org.fleen.bread.app.hairyParasite;

import java.util.Random;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DVector;
import org.fleen.geom_2D.GD;

public class Torso{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Torso(){
    initP0AndP1();}
  
  /*
   * ################################
   * P0 AND P1
   * ################################
   */
  
  static final double INITLENGTH=1.0;
  Random rnd=new Random();
  
  DPoint p0,p1;
  
  void initP0AndP1(){
    p0=new DPoint(0,0);
    double d=rnd.nextDouble()*GD.PI2;
    p1=new DPoint(GD.getPoint_PointDirectionInterval(p0.x,p0.y,d,INITLENGTH));}
  
  /*
   * ################################
   * TWITCH
   * Simple
   * Calculate a random vector for p0
   * do the same for p1
   * if implementing those vectors gives us a new torso seg of length within valid range then do it.
   * ################################
   */
  
  static final double 
  MAGMIN=0.003,
  MAGMAX=0.009,
  LENGTHMIN=0.8,
  LENGTHMAX=1.2;

  public void twitch(){
    double 
      d0=rnd.nextDouble()*GD.PI2,
      d1=rnd.nextDouble()*GD.PI2,
      m0=rnd.nextDouble()*(MAGMAX-MAGMIN)+MAGMIN,
      m1=rnd.nextDouble()*(MAGMAX-MAGMIN)+MAGMIN;
    DPoint 
      newp0=p0.getPoint(new DVector(d0,m0)),
      newp1=p1.getPoint(new DVector(d1,m1));
    //move either p0 or p1 first
    double dtest;
    if(rnd.nextBoolean()){
      dtest=newp0.getDistance(p1);
      if(dtest>LENGTHMIN&&dtest<LENGTHMAX)p0=newp0;
      dtest=newp1.getDistance(p0);
      if(dtest>LENGTHMIN&&dtest<LENGTHMAX)p1=newp1;
    }else{
      dtest=newp1.getDistance(p0);
      if(dtest>LENGTHMIN&&dtest<LENGTHMAX)p1=newp1;
      dtest=newp0.getDistance(p1);
      if(dtest>LENGTHMIN&&dtest<LENGTHMAX)p0=newp0;}}

}
