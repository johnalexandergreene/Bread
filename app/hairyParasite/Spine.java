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
   * TORSO, HEAD AND TAIL
   * wider towards the middle
   * squirmier towards the ends
   * ################################
   */
  
  public static final int
    HEADLENGTH=5,
    TAILLENGTH=5;
  static final double 
    TORSOBASELENGTH=1.0,
    JOINTLENGTHMIN=TORSOBASELENGTH*0.5,
    JOINTLENGTHMAX=TORSOBASELENGTH*1.5,
    P1INITOFFSETNOISE=0.1,
    SQUIRMDIRECTIONNOISELEVEL=0.2,
    SQUIRMLENGTHNOISELEVEL=0.2;
  
  Random rnd=new Random();
  //torso
  Torso torso;
  //defines the respective squirmy processes 
  List<Joint> head,tail;
  
  public void init(){
    System.out.println("init torso");
    torso=new Torso();
    System.out.println("init head");
    head=new ArrayList<Joint>();
    for(int i=0;i<HEADLENGTH;i++)
      head.add(new Joint(getJointLength(i,HEADLENGTH)));
    System.out.println("init tail");
    tail=new ArrayList<Joint>();
    for(int i=0;i<TAILLENGTH;i++)
      tail.add(new Joint(getJointLength(i,TAILLENGTH)));}
  
  private double getJointLength(int index,int maxindex){
    double 
      a=maxindex-index,
      b=a/((double)maxindex);
    return b;}
  
  /*
   * ################################
   * BASE FIGURE
   * A polyseg comprised of the tail, torso and head points
   * ################################
   */
  
  public List<DPoint> getBaseFigure(){
    List<DPoint> base=new ArrayList<DPoint>(2+head.size()+tail.size());
    base.add(torso.p0);
    base.add(torso.p1);
    //
    DPoint pnew=new DPoint(torso.p1);
    double dir=torso.p0.getDirection(torso.p1);
    double[] a;
    for(Joint joint:head){
      dir=GD.normalizeDirection(dir+joint.directiondelta);
      a=GD.getPoint_PointDirectionInterval(pnew.x,pnew.y,dir,joint.length);
      pnew=new DPoint(a);
      base.add(pnew);}
    //
    pnew=new DPoint(torso.p0);
    dir=torso.p1.getDirection(torso.p0);
    for(Joint joint:tail){
      dir=GD.normalizeDirection(dir+joint.directiondelta);
      a=GD.getPoint_PointDirectionInterval(pnew.x,pnew.y,dir,joint.length);
      pnew=new DPoint(a);
      base.add(0,pnew);}
    return base;}
  
  public Path2D getBaseFigurePath(){
    Path2D path=new Path2D.Double();
    List<DPoint> base=getBaseFigure();
    DPoint p=base.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<base.size();i++){
      p=base.get(i);
      path.lineTo(p.x,p.y);}
    return path;}
  
  /*
   * ################################
   * SMOOTHED FIGURE
   * ################################
   */
  
  static final int SMOOTHNESS=3;
  
  public List<DPoint> getSmoothedFigure(){
    List<DPoint> base=getBaseFigure();
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
  
  public Path2D getSmoothedFigurePath(){
    Path2D path=new Path2D.Double();
    List<DPoint> smoothbase=getSmoothedFigure();
    DPoint p=smoothbase.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<smoothbase.size();i++){
      p=smoothbase.get(i);
      path.lineTo(p.x,p.y);}
    return path;}
  
  /*
   * ################################
   * TWITCH
   * ################################
   */
  
  public void twitch(){
    torso.twitch();
    for(Joint joint:head)
      joint.twitch();
    for(Joint joint:tail)
      joint.twitch();}
  
}
