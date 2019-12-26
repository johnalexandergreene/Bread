package org.fleen.bread.app.spray;

import java.util.ArrayList;
import java.util.List;

import org.fleen.bread.app.spray.sprayer.Sprayer;

/*
 * An array of ints (cells)
 * 
 * with arbitrary sprayer/s. Paint to the grid
 * Paint with 1s or 2s or whatever. Arbitrary integer.
 * sum value at cell for each sprayer applied. Value added (or removed) will be in range 0..N
 * then use cell values as colors.
 * 
 * So all the magic is in the sprayers. Pure stupid math 
 * And we have summative coloring 
 * So we have the sum of N sprayers' contribution. A Summative Sprayer Grid 
 * 
 * We also have time to consider. Thus video
 * 
 */
public class Target{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Target(int width,int height,TargetObserver observer){
    this.width=width;
    this.height=height;
    cells=new int[width][height];
    this.observer=observer;
    //get a few useful metrics
    centerx=width/2;
    centery=height/2;
    double dw=width/2,dh=height/2;
    radius=Math.sqrt(dw*dw+dh*dh);}
  
  /*
   * ################################
   * EXECUTIVE
   * ################################
   */
  
  void advance(){
    System.out.println("advance");
    regenerate();
    age++;
    observer.advanced();}
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  TargetObserver observer;
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public int age=0;
  public double width,height,centerx,centery,radius;
  
  /*
   * ################################
   * CELLS
   * ################################
   */
  
  public int[][] cells;
  public List<Sprayer> sprayers=new ArrayList<Sprayer>();
  
  void setSprayers(List<Sprayer> s){
    sprayers.clear();
    sprayers.addAll(sprayers);}
  
  void addSprayer(Sprayer s){
    sprayers.add(s);}
  
  void regenerate(){
    for(int x=0;x<width;x++){
      for(int y=0;y<width;y++){
        cells[x][y]=0;}}
    //
    for(Sprayer s:sprayers)
      s.spray(this);}
  
}
