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
 * it wil have a random wiggle program. Moderate wiggle and fast spastic jerks
 * 
 * A seg : p0 and p1. An implied direction : p0 -> p1 
 * A series of forward vectors (we use direction offsets)
 * A series of reverse vectors
 * 
 * Twiddling the points gives us one kind of movement
 * Wiggling the vectors gives us another
 * 
 */
public class Spine{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Spine(){
    initBase();}
  
  /*
   * ################################
   * GEOMETRY
   * Spine
   *   wider towards the middle
   *   squirmier towards the ends
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
  List<DVector> 
    vforward,
    vbackward;
  
  public void initBase(){
    System.out.println("init base");
    p0=new DPoint(0,0);
    initP1();
    vforward=initVectors();
    vbackward=initVectors();}
  
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
    
  List<DVector> initVectors(){
    List<DVector> vectors=new ArrayList<DVector>();
    double
      a=rnd.nextInt(MAXSQUIRMLENGTH-MINSQUIRMLENGTH)+MINSQUIRMLENGTH,
      length,
      lengthnoiselevel,
      directionnoiselevel;
    for(int i=0;i<a;i++){
      length=BASEJOINTLENGTH;
      lengthnoiselevel=SQUIRMLENGTHNOISELEVEL;
      directionnoiselevel=SQUIRMDIRECTIONNOISELEVEL;
      vectors.add(createDVector(length,lengthnoiselevel,directionnoiselevel));}
    //
    return vectors;}
  
  DVector createDVector(double length,double lengthnoiselevel,double directionnoiselevel){
    double 
      lengthnoise=rnd.nextDouble()*lengthnoiselevel,
      directionnoise=rnd.nextDouble()*directionnoiselevel;
    if(rnd.nextBoolean())lengthnoise*=-1;
    double adjustedlength=length*(1.0+lengthnoise);
    if(adjustedlength<JOINTLENGTHMIN)
      adjustedlength=JOINTLENGTHMIN;
    if(rnd.nextBoolean())directionnoise*=-1;
    double direction=GD.normalizeDirection(directionnoise*GD.PI);
    return new DVector(direction,adjustedlength);}
  
  public List<DPoint> getBase(){
    List<DPoint> base=new ArrayList<DPoint>(2+vforward.size()+vbackward.size());
    base.add(p0);
    base.add(p1);
    //
    DPoint pnew=new DPoint(p1.x,p1.y);
    double dir=p0.getDirection(p1);
    double[] a;
    for(DVector v:vforward){
      dir=GD.normalizeDirection(dir+v.direction);
      a=GD.getPoint_PointDirectionInterval(pnew.x,pnew.y,dir,v.magnitude);
      pnew=new DPoint(a);
      base.add(pnew);}
    //
    pnew=new DPoint(p0.x,p0.y);
    dir=p1.getDirection(p0);
    for(DVector v:vbackward){
      dir=GD.normalizeDirection(dir+v.direction);
      a=GD.getPoint_PointDirectionInterval(pnew.x,pnew.y,dir,v.magnitude);
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
    P0P1SHIFTVECTORMAGMIN=0.01,
    P0P1SHIFTVECTORMAGMAX=0.2;
  
  public void shiver(){
    shiftP0P1();
    wiggleSquirms();}
  
  void shiftP0P1(){
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
  
  static final 
    double MAXANGLEOFFSET=GD.PI*0.4,
    ANGLENOISE=0.15,
    LENGTHNOISE=0.05;
  
  
  void wiggleSquirms(){
    double a,n;
    for(DVector v:vforward){
      n=rnd.nextDouble()*ANGLENOISE*GD.PI;
      if(rnd.nextBoolean())n*=-1;
      a=v.direction+n;
      if(a<MAXANGLEOFFSET&&a>-MAXANGLEOFFSET)
        v.direction=a;
      //
      n=rnd.nextDouble()*LENGTHNOISE;
      if(rnd.nextBoolean())n*=-1;
      a=v.magnitude*(1.0+n);
      if(a<JOINTLENGTHMAX&&a>JOINTLENGTHMIN)
        v.magnitude=a;}
    //
    for(DVector v:vbackward){
      n=rnd.nextDouble()*ANGLENOISE*GD.PI;
      if(rnd.nextBoolean())n*=-1;
      a=v.direction+n;
      if(a<MAXANGLEOFFSET&&a>-MAXANGLEOFFSET)
        v.direction=a;
      //
      n=rnd.nextDouble()*LENGTHNOISE;
      if(rnd.nextBoolean())n*=-1;
      a=v.magnitude*(1.0+n);
      if(a<JOINTLENGTHMAX&&a>JOINTLENGTHMIN)
        v.magnitude=a;}}
  
}
