package org.fleen.bread.app.hairyParasite;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.geom_2D.CurveSmoother_Open;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DVector;
import org.fleen.geom_2D.GD;

/*
 * an open curve describing a bit of squirm
 * Not complex. Fundamentally just a few points.
 * Then elaborated into a proper squirm via split-tweak
 * it will have a random wiggle program. Moderate wiggle and fast spastic jerks
 * 
 * Torso. Basically a seg, p0 and p1. And an implied direction : p0 -> p1. And some stuff for handling twitches 
 * 2 chains of Joints. One extending forward, the other extending backward.
 *   A Joint is a vector + some other stuff for controlling twitch
 * 
 * Twitching the Torso gives us one kind of movement
 * Twitching the Joints gives us another
 * Combined I think we should get a nice spastic jerky writhe  
 * 
 */
public class Spine{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Spine(){
    init();}
  
  /*
   * ################################
   * GEOMETRY
   * wider towards the middle
   * squirmier towards the ends
   * ################################
   */
  
  static final int
    MINSQUIRMLENGTH=4,
    MAXSQUIRMLENGTH=7;
  static final double 
    BASEJOINTLENGTH=1.0,
    JOINTLENGTHMIN=BASEJOINTLENGTH*0.5,
    JOINTLENGTHMAX=BASEJOINTLENGTH*1.5,
    P1INITOFFSETNOISE=0.1,
    SQUIRMDIRECTIONNOISELEVEL=0.2,
    SQUIRMLENGTHNOISELEVEL=0.2;
  
  Random rnd=new Random();
  //defines the central seg
  DPoint p0,p1;
  //defines the respective squirmy processes 
  List<Joint> 
    vforward,
    vbackward;
  
  public void init(){
    System.out.println("init base");
    p0=new DPoint(0,0);
    initP1();
    vforward=initJoints();
    vbackward=initJoints();}
  
  /*
   * offset from p0
   * direction is random
   * interval is BASEJOINTLENGTH+noise
   */
  void initP1(){
    double 
      d=rnd.nextDouble()*GD.PI2,
      noise=rnd.nextDouble()*P1INITOFFSETNOISE;
    if(rnd.nextBoolean())noise*=-1;
    double i=(1.0+noise)*BASEJOINTLENGTH;
    if(i<JOINTLENGTHMIN)i=JOINTLENGTHMIN;
    p1=new DPoint(GD.getPoint_PointDirectionInterval(p0.x,p0.y,d,i));}
    
  List<Joint> initJoints(){
    List<Joint> joints=new ArrayList<Joint>();
    double a=rnd.nextInt(MAXSQUIRMLENGTH-MINSQUIRMLENGTH)+MINSQUIRMLENGTH;
    for(int i=0;i<a;i++){
      joints.add(new Joint(this,i));}
    //
    return joints;}
  
  public List<DPoint> getBase(){
    List<DPoint> base=new ArrayList<DPoint>(2+vforward.size()+vbackward.size());
    base.add(p0);
    base.add(p1);
    //
    DPoint pnew=new DPoint(p1.x,p1.y);
    double dir=p0.getDirection(p1);
    double[] a;
    for(Joint joint:vforward){
      dir=GD.normalizeDirection(dir+joint.directiondelta);
      a=GD.getPoint_PointDirectionInterval(pnew.x,pnew.y,dir,joint.length);
      pnew=new DPoint(a);
      base.add(pnew);}
    //
    pnew=new DPoint(p0.x,p0.y);
    dir=p1.getDirection(p0);
    for(Joint joint:vbackward){
      dir=GD.normalizeDirection(dir+joint.directiondelta);
      a=GD.getPoint_PointDirectionInterval(pnew.x,pnew.y,dir,joint.length);
      pnew=new DPoint(a);
      base.add(0,pnew);}
    return base;}
  
  public Path2D getBasePath(){
    Path2D path=new Path2D.Double();
    List<DPoint> base=getBase();
    DPoint p=base.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<base.size();i++){
      p=base.get(i);
      path.lineTo(p.x,p.y);}
    return path;}
  
  /*
   * ################################
   * SMOOTHED GEOMETRY
   * ################################
   */
  
  static final int SMOOTHNESS=3;
  
  public List<DPoint> getSmoothedBase(){
    List<DPoint> base=getBase();
    double[][] base0=new double[base.size()][2];
    DPoint dp;
    for(int i=0;i<base.size();i++){
      dp=base.get(i);
      base0[i][0]=dp.x;
      base0[i][1]=dp.y;}
    //
    double[][] a=new CurveSmoother_Open().getSmoothedOpenCurve(base0,SMOOTHNESS);
    //
    List<DPoint> b=new ArrayList<DPoint>();
    for(double[] d:a)
      b.add(new DPoint(d));
    return b;}
  
  public Path2D getRefinedPath(){
    Path2D path=new Path2D.Double();
    List<DPoint> smoothbase=getSmoothedBase();
    DPoint p=smoothbase.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<smoothbase.size();i++){
      p=smoothbase.get(i);
      path.lineTo(p.x,p.y);}
    return path;}
  
  /*
   * ################################
   * MOVE
   * Shift p0 and p1
   * twiddle the vectors
   * ################################
   */
  
  static final double 
    P0P1SHIFTVECTORMAGMIN=0.0001,
    P0P1SHIFTVECTORMAGMAX=0.002;
  
  public void shiver(){
    twitchTorso();
    twitchJoints();}
  
  void twitchTorso(){
    DVector 
      v0=new DVector(
        rnd.nextDouble()*GD.PI2,
        rnd.nextDouble()*(P0P1SHIFTVECTORMAGMAX-P0P1SHIFTVECTORMAGMIN)+P0P1SHIFTVECTORMAGMIN),
      v1=new DVector(
        rnd.nextDouble()*GD.PI2,
        rnd.nextDouble()*(P0P1SHIFTVECTORMAGMAX-P0P1SHIFTVECTORMAGMIN)+P0P1SHIFTVECTORMAGMIN);
    DPoint 
      newp0=p0.getPoint(v0),
      newp1=p1.getPoint(v1);
    double d=newp0.getDistance(newp1);
    if(d<JOINTLENGTHMAX&&d>JOINTLENGTHMIN){
      p0=newp0;
      p1=newp1;}}
  
  void twitchJoints(){
    for(Joint joint:vforward)
      joint.twitch();
    for(Joint joint:vbackward)
      joint.twitch();
    
  }
  
  
  
  
}
