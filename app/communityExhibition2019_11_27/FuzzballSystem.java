package org.fleen.bread.app.communityExhibition2019_11_27;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.fleen.geom_2D.GD;

public class FuzzballSystem{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public FuzzballSystem(int width,int height,FuzzballSystemObserver observer){
    this.width=width;
    this.height=height;
    grid=new int[width][height];
    this.observer=observer;
    centerx=width/2;
    centery=height/2;
    double dw=width/2,dh=height/2;
    radius=Math.sqrt(dw*dw+dh*dh);
    initFuzzballs();}
  
  /*
   * ################################
   * EXECUTIVE
   * ################################
   */
  
  int age=0;
  
  void advance(){
    adjustWind();
    moveFuzzballs();
    conditionallyDestroyFuzzballs();
    conditionallyCreateFuzzballs();
    updateGrid();
    age++;
    observer.advanced();}
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  FuzzballSystemObserver observer;
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  double width,height,centerx,centery,radius;
  
  /*
   * ################################
   * FUZZBALLS
   * ################################
   */
  
  static final int INITFUZZBALLCOUNT=12;
  /*
   * a fuzzball enters the system at distance from system center = ENTRYRADIUS*radius
   * a fuzzball exits the system at distance from system center = EXITRADIUS*radius
   * For easy testing, EXITRADIUS>ENTRYRADIUS
   * We use these big radiuses because sometimes a fuzzball's area of rendering can be 
   * somewhat huge. We will tweak these values to ensure that fuzzball creation an destruction always occur offstage  
   */
  static final double 
    ENTRYRADIUS=2.0,
    EXITRADIUS=3.0;
  
  Set<Fuzzball> fuzzballs=new HashSet<Fuzzball>();
  
  void initFuzzballs(){
    int x,y;
    for(int i=0;i<2;i++){
      x=(int)(rnd.nextDouble()*width);
      y=(int)(rnd.nextDouble()*height);
      fuzzballs.add(new Fuzzball(this,x,y));}
    }
  
  void moveFuzzballs(){
    for(Fuzzball z:fuzzballs)
      z.move();}
  
  void conditionallyDestroyFuzzballs(){
    List<Fuzzball> d=new ArrayList<Fuzzball>();
    for(Fuzzball z:fuzzballs)
      if(z.destroyMe())
        d.add(z);
    fuzzballs.removeAll(d);}
  
  /*
   * There are lots of ways to do this
   * We need a creation condition
   * We will have some random
   * We might count the live fuzzballs or we might do a sum of theor weight*radii, or something
   * TODO for now we just use the init count 
   */
  void conditionallyCreateFuzzballs(){
    if(fuzzballs.size()<INITFUZZBALLCOUNT)
      fuzzballs.add(new Fuzzball(this));}
  
  /*
   * ################################
   * WIND
   * we want a wind that gently varies in direction and speed
   * I think we'll set a base direction
   * ################################
   */
  
  static final double 
    WINDSPEEDMAX=12,
    WINDSPEEDMIN=5,
    WINDSPEEDDELTAMAX=GD.PI2/16;
 
  public double winddirection,windspeed,windspeeddelta;
  
  void initWind(){
    winddirection=rnd.nextDouble()*GD.PI2;
    windspeed=rnd.nextDouble()*(WINDSPEEDMAX-WINDSPEEDMIN)+WINDSPEEDMIN;
    windspeeddelta=rnd.nextDouble()*(WINDSPEEDDELTAMAX/4);}
  
  void adjustWind(){
//    double a=rnd.nextDouble()*(WINDSPEEDDELTAMAX/3);
//    if(windspeeddelta+a>WINDSPEEDDELTAMAX)
//      windspeeddelta-=a;
    
  }
  
  /*
   * ################################
   * GRID
   * ################################
   */
  
  int[][] grid;
  
  void updateGrid(){
    //init it
    for(int x=0;x<width;x++){
      for(int y=0;y<height;y++){
        grid[x][y]=0;}}
    //sum circles at all cells
    //do it crude because suave is annoyingly complex
    for(int x=0;x<width;x++){
      for(int y=0;y<height;y++){
        for(Fuzzball z:fuzzballs){
          grid[x][y]+=getBallPresence(x,y,z);}}}}
  
//  void updateGrid(){
//    //init it
//    for(int x=0;x<width;x++){
//      for(int y=0;y<height;y++){
//        grid[x][y]=getCellVal(x,y);}}
//    //sum circles at all cells
//    //do it crude because suave is annoyingly complex
////    for(int x=0;x<width;x++){
////      for(int y=0;y<height;y++){
////        for(Fuzzball z:fuzzballs){
////          grid[x][y]+=getBallPresence(x,y,z);}}}
//    }
  
//  int getCellVal(int x,int y){
//    double 
//      cx=x+0.5,
//      cy=y+0.5;
//    double a=(cx*cy*age/33)%66;
//    return (int)a;
//  }
  
  /*
   * TODO add some fuzz at edge of circle
   * I mean, a circle will have a presence of 10 or so
   * so that value will be consistent throughout its mass but then taper down to 1 at the edge
   */
  
  static final double PRESENCEFACTOR=6;//32;
  
  int getBallPresence(int x,int y,Fuzzball z){
    double 
      cellcenterx=x+0.5,
      cellcentery=y+0.5;
    double d=GD.getDistance_PointPoint(cellcenterx,cellcentery,z.x,z.y);
    double v;
    if(d>z.radius){
      v=0;
    }else{ 
      v=(z.radius-d)/z.radius;
      v*=PRESENCEFACTOR;
      }
    return (int)v;
  }
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  Random rnd=new Random();
  

}
