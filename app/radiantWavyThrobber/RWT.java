package org.fleen.bread.app.radiantWavyThrobber;

import java.util.ArrayList;
import java.util.List;

import org.fleen.bread.app.radiantWavyThrobber.production.RWTObserver;
import org.fleen.geom_2D.GD;

/*
 * A moving pattern of Emitters.
 * An Emitter is a radiant-wave emitting point
 * it's got concentric rings moving to or from it
 * it moves in a pattern
 *   Like 
 */
public class RWT{
  
  public static final double 
    WIDTH=700,
    HEIGHT=700;
  
  public RWT(){
    initEmitters();
  }
  
  /*
   * ################################
   * EMITTERS
   * ################################
   */
  
  public List<Emitter> emitters=new ArrayList<Emitter>();
  
  void initEmitters(){
    emitters.add(new Emitter(this,WIDTH/2,HEIGHT/2,0));
    emitters.add(new Emitter(this,WIDTH/2,HEIGHT/2,GD.PI2*1/3));
    emitters.add(new Emitter(this,WIDTH/2,HEIGHT/2,GD.PI2*2/3));}
  
  /*
   * ################################
   * STATE
   * ################################
   */
  
  public int age=0;
  
  public void advanceState(){
    age++;
    notifyObservers();}
  
  /*
   * ################################
   * OBSERVERS
   * ################################
   */
  
  public List<RWTObserver> observers=new ArrayList<RWTObserver>();
  
  void notifyObservers(){
    for(RWTObserver a:observers)
      a.advanced();}
  

}
