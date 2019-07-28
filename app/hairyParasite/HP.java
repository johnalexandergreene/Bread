package org.fleen.bread.app.hairyParasite;

import java.util.ArrayList;
import java.util.List;

import org.fleen.bread.app.hairyParasite.production.HPObserver;

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
    
    //test
    spine.initBase();
    
    notifyObservers();}
  
  void init(){
    spine=new Spine();
  }
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public Spine spine;
  
  
  
  
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
