package org.fleen.bread.app.hairyParasite;

import java.util.ArrayList;
import java.util.List;

import org.fleen.bread.app.hairyParasite.production.HPObserver;
import org.fleen.geom_2D.DPoint;

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
  
  public Spine spine;
  public List<Hair> hairs;
  
  void createHairs(){
    hairs=new ArrayList<Hair>();
    createTransverseHairs();
    createHeadHairs();
    createTailHairs();}
  
  void createTransverseHairs(){
    List<DPoint> sf=spine.getSmoothedFigure();
    double sflength=getPolySegLength(sf);
    System.out.println("sflength="+sflength);
    
  }
  
  void createHeadHairs(){
    
  }
  
  void createTailHairs(){
    
  }
  
  double getPolySegLength(List<DPoint> polyseg){
    double a=0;
    DPoint p0,p1;
    for(int i=0;i<polyseg.size()-1;i++){
      p0=polyseg.get(i);
      p1=polyseg.get(i+1);
      a+=p0.getDistance(p1);}
    return a;}
  
  
  
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
