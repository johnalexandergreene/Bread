package org.fleen.bread.app.flerp0flerp;

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
    grid=new double[width][height];
    this.observer=observer;
    centerx=width/2;
    centery=height/2;
    double dw=width/2,dh=height/2;
    radius=Math.sqrt(dw*dw+dh*dh);
    initFuzzballGuns();}
  
  /*
   * ################################
   * EXECUTIVE
   * ################################
   */
  
  public int age=-1;
  
  void advance(){
    age++;
    updateGrid();
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
   * FUZZBALLGUNS
   * ################################
   */
  
  static final int FUZZBALLGUNCOUNT=1;
  
  List<FuzzballGun> fuzzballguns=new ArrayList<FuzzballGun>();
  
  private void initFuzzballGuns(){
    fuzzballguns.add(new FuzzballGun_000(this));
    fuzzballguns.add(new FuzzballGun_000(this));
    fuzzballguns.add(new FuzzballGun_000(this));
    fuzzballguns.add(new FuzzballGun_000(this));
    fuzzballguns.add(new FuzzballGun_000(this));
    fuzzballguns.add(new FuzzballGun_000(this));
  }
  
  /*
   * ################################
   * GRID
   * ################################
   */
  
  public double[][] grid;
  
  void updateGrid(){
    //init it
    for(int x=0;x<width;x++){
      for(int y=0;y<height;y++){
        grid[x][y]=0;}}
    //sum circles at all cells
    //do it crude
    for(int x=0;x<width;x++){
      for(int y=0;y<height;y++){
        for(FuzzballGun g:fuzzballguns){
          for(Fuzzball z:g.getFuzzballs()){
            grid[x][y]+=getBallPresence(x,y,z);}}}}}
  
  double getBallPresence(int x,int y,Fuzzball z){
    double 
      cellcenterx=x+0.5,
      cellcentery=y+0.5;
    double d=GD.getDistance_PointPoint(cellcenterx,cellcentery,z.x,z.y);
    double v;
    if(d>z.radius){
      v=0;
    }else{ 
      v=(z.radius-d)/z.radius;
      v*=z.density;}
    return (int)v;}
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  Random rnd=new Random();
  

}
