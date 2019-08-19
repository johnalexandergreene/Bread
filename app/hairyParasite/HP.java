package org.fleen.bread.app.hairyParasite;

import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.bread.app.hairyParasite.production.HPObserver;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.GD;

/*
 * A moving pattern of Emitters.
 * An Emitter is a radiant-wave emitting point
 * it's got concentric rings moving to or from it
 * it moves in a pattern
 *   Like 
 */
public class HP{
  
  public static final double 
    WIDTH=1000,
    HEIGHT=1000;
  
  /*
   * ################################
   * STATE
   * ################################
   */
  
  public int age=-1;
  
  public void advanceState(){
    age++;
    if(age==0)init();
    spine.twitch();
    for(Hair hair:hairs)
      hair.twitch();
    notifyObservers();}
  
  void init(){
    spine=new Spine();
    createHairs();}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  static final int 
    TRANSVERSEHAIRCOUNT=1000,
    HEADHAIRCOUNT=100,
    TAILHAIRCOUNT=100;
  Random rnd=new Random();
  public Spine spine;
  public List<Hair> hairs;
  
  void createHairs(){
    hairs=new ArrayList<Hair>();
    double location;
    boolean polarity;
    for(int i=0;i<TRANSVERSEHAIRCOUNT;i++){
      location=rnd.nextDouble();
      polarity=rnd.nextBoolean();
      hairs.add(new Hair(location,Hair.TYPE_TRANSVERSE,polarity));}
    for(int i=0;i<HEADHAIRCOUNT;i++){
      location=rnd.nextDouble();
      hairs.add(new Hair(location,Hair.TYPE_HEAD,false));}
    for(int i=0;i<TAILHAIRCOUNT;i++){
      location=rnd.nextDouble();
      hairs.add(new Hair(location,Hair.TYPE_TAIL,false));}}
 
  double getSmoothedFigureLength(List<DPoint> sf){
    double a=0;
    DPoint p0,p1;
    for(int i=0;i<sf.size()-1;i++){
      p0=sf.get(i);
      p1=sf.get(i+1);
      a+=p0.getDistance(p1);}
    return a;}
  
  public List<Path2D> getHairPaths(){
    List<DPoint> sf=spine.getSmoothedFigure();
    double sflength=getSmoothedFigureLength(sf);
    List<Path2D> paths=new ArrayList<Path2D>();
    double sfoffset;
    double[] hairstartandforward;
    for(Hair hair:hairs){
      sfoffset=sflength*hair.location;
      hairstartandforward=getPointAndForwardOnSmoothedFigure(sf,sflength,sfoffset);
      paths.add(getHairPath(hair,hairstartandforward));}
    return paths;}
  
  Path2D getHairPath(Hair hair,double[] hairstartandforward){
    Path2D path=new Path2D.Double();
    double basedir=hairstartandforward[2];
    if(hair.polarity)
      basedir=GD.normalizeDirection(basedir+GD.HALFPI);
    else
      basedir=GD.normalizeDirection(basedir-GD.HALFPI);
    //test
    path.moveTo(hairstartandforward[0],hairstartandforward[1]);
    double[] p1=GD.getPoint_PointDirectionInterval(hairstartandforward[0],hairstartandforward[1],basedir,0.3);
    path.lineTo(p1[0],p1[1]);
    return path;}
  
  /*
   * given our polyseg, its precalculated length and an offset
   *   get the point on the polyseg
   *   get the forward on the seg where that point is
   * get the seg
   * the point on that seg
   * forward on that seg
   */
  public double[] getPointAndForwardOnSmoothedFigure(List<DPoint> sf,double sflength,double offset){
    //get the seg (by index) that the point specified by offset falls upon
    //get the offset of our point in that seg
    boolean found=false;
    int i=0;
    double nextpointoffset=0,offsetinseg=0;
    while(!found){
      offsetinseg=nextpointoffset-offset;
      nextpointoffset+=sf.get(i).getDistance(sf.get(i+1));
      if(nextpointoffset>offset){
        found=true;
      }else{
        i++;}}
    //now we have the index of the seg's p0 (i) and the offset in that seg
    //get the point and forward
    DPoint 
      p0=sf.get(i),
      p1=sf.get(i+1);
    double forward=p0.getDirection(p1);
    double[] point=GD.getPoint_PointDirectionInterval(p0.x,p0.y,forward,offsetinseg);
    return new double[]{point[0],point[1],forward};}
  
  /*
   * ################################
   * OBSERVERS
   * ################################
   */
  
  public List<HPObserver> observers=new ArrayList<HPObserver>();
  
  void notifyObservers(){
    for(HPObserver a:observers)
      a.advanced();}
  

}
