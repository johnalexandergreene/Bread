package org.fleen.bread.app.hairball;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.bread.app.hairball.production.HairballObserver;
import org.fleen.geom_2D.GD;

public class Hairball{
  
  public static final int WIDTH=100,HEIGHT=100;
  
  
  public Hairball(){
    initChain();
    
  }
  
  /*
   * ################################
   * CELL CHAIN
   * ################################
   */
  
  /*
   * a cell in the chain. Any cell in the chain. 
   * If, in the process of development, this cell is removed, then we use another one.
   */
  Cell reference;
  
  //chain init is a circle of cells
  static final int INITCHAINCELLCOUNT=100;
  static final double INITCHAINRADIUS=20;
  
  private void initChain(){
    for(int i=0;i<INITCHAINCELLCOUNT;i++){
      
    }
    
  }
  
  /*
   * ################################
   * STATE
   * ################################
   */
  
  public int age=0;
  
  public void advanceState(){
    age++;
    runAutomaton();
    notifyObservers();}
  
  /*
   * ################################
   * CELL AUTOMATON
   * ################################
   */
  
  void runAutomaton(){}
  
  /*
   * ################################
   * OBSERVERS
   * ################################
   */
  
  public List<HairballObserver> observers=new ArrayList<HairballObserver>();
  
  void notifyObservers(){
    for(HairballObserver a:observers)
      a.advanced();}
  

}
