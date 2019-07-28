package org.fleen.bread.app.hairyParasite;

import java.awt.geom.Path2D;
import java.util.Random;

import org.fleen.geom_2D.GD;

/*
 * an open curve describing a bit of squirm
 * Not complex. Fundamentally just a few points.
 * Then elaborated into a proper squirm via split-tweak
 * it wil have a random wiggle program. Moderate wiggle and fast spastic jerks
 */
public class Spine{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Spine(){
    initBase();
  }
  
  /*
   * ################################
   * GEOMETRY
   * Spine
   *   wider towards the middle
   *   squirmier towards the ends
   * ################################
   */
  
  static final int SPINELENGTH=7;
  static final double 
    BASEJOINTINTERVAL=1.0,
    MINJOINTINTERVAL=BASEJOINTINTERVAL*0.2,
    JOINTINTERVALNOISE=0.1,
    JOINTINITDIROFFSETNOISE=0.2;
  
  public double[][] base;
  Random rnd=new Random();
  
  
  /*
   * first point is 0,0
   * semirandom walk after that.
   */
  public void initBase(){
    base=new double[SPINELENGTH][2];
    base[0][0]=0;
    base[0][1]=0;
    double 
      dir=rnd.nextDouble()*GD.PI2,
      jointinterval=getJointIntervalForInit(0);
    for(int i=1;i<SPINELENGTH;i++){
      base[i]=GD.getPoint_PointDirectionInterval(base[i-1][0],base[i-1][1],dir,jointinterval);
      dir=getNewJointDirForInit(dir,i);
      jointinterval=getJointIntervalForInit(1);}
    System.out.println("spine base init");
  }
  
  double getJointIntervalForInit(double jointindex){
    double 
      a=(double)SPINELENGTH,
      b=(a/2)-Math.abs(jointindex-(a/2)),
      ji=b*BASEJOINTINTERVAL,
      noise=rnd.nextDouble()*(JOINTINTERVALNOISE/2)*BASEJOINTINTERVAL-(BASEJOINTINTERVAL/2);
    ji+=noise;
    if(ji<MINJOINTINTERVAL)
      ji=MINJOINTINTERVAL;
    return ji;}
  
  double getNewJointDirForInit(double dir,double jointindex){
    double 
      a=(double)SPINELENGTH,
      b=Math.abs(jointindex-(a/2)),
      noise=rnd.nextDouble()*JOINTINITDIROFFSETNOISE*GD.PI*b;
    dir=GD.normalizeDirection(dir+noise);
    return dir;}
  
  public Path2D getBasePath(){
    Path2D p=new Path2D.Double();
    p.moveTo(base[0][0],base[0][1]);
    for(int i=1;i<SPINELENGTH;i++)
      p.lineTo(base[i][0],base[i][1]);
    return p;}
  
}
