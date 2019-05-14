package org.fleen.bread.app.dancingSquishyThing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.bread.app.dancingSquishyThing.production.DSTObserver;
import org.fleen.geom_2D.GD;

public class DST{
  
  public static final int WIDTH=100,HEIGHT=100;
  
  
  public DST(){
    randomize();
    
  }
  
  /*
   * ################################
   * CELLFIELD
   * ################################
   */
  
  public int cellfield[][]=new int[WIDTH][HEIGHT];
  
  /*
   * cells are 0 or 1
   * offmap returns 0
   */
  public int getCell(int x,int y){
    if(x<0||x>=WIDTH||y<0||y>=HEIGHT)return 0;
    return cellfield[x][y];}
  
  /*
   * cells are 0 or 1
   * offmap returns 0
   */
  public int[] getNeighbors(int x,int y){
    int[] a=new int[8];
    a[0]=getCell(x-1,y-1);
    a[1]=getCell(x,y-1);
    a[2]=getCell(x+1,y-1);
    a[3]=getCell(x+1,y);
    a[4]=getCell(x+1,y+1);
    a[5]=getCell(x,y+1);
    a[6]=getCell(x-1,y+1);
    a[7]=getCell(x-1,y);
    return a;}
  
  int getLive(int x,int y){
    int[] n=getNeighbors(x,y);
    int l=0;
    for(int i:n)
      if(i==LIVE)l++;
    return l;}
  
  /*
   * ################################
   * CIRCLE
   * ################################
   */
  
  public double radius=WIDTH/3;
  
  public int getCenterDist(int x,int y){
    double cx=((double)WIDTH)/2,cy=((double)HEIGHT)/2;
    return (int)GD.getDistance_PointPoint(x,y,cx,cy);}
  
  /*
   * ################################
   * STATE
   * ################################
   */
  
  public int age=0;
  
  public void advanceState(){
    age++;
    if(age==0)randomize();
    runAutomaton();
    notifyObservers();
  }
  
  Random random=new Random();
  
  void randomize(){
    for(int x=0;x<WIDTH;x++){
      for(int y=0;y<HEIGHT;y++){
        cellfield[x][y]=random.nextInt(2);}}}
  
  /*
   * ################################
   * CELL AUTOMATON
   * ################################
   */
  
  void runAutomaton(){
    int[][] nextstate=new int[WIDTH][HEIGHT];
    for(int x=0;x<WIDTH;x++){
      for(int y=0;y<HEIGHT;y++){
        nextstate[x][y]=doRule(x,y);}}
    cellfield=nextstate;}
  
  static final int LIVE=1,DEAD=0;
  
  int doRule(int x,int y){
    int d=getCenterDist(x,y);
    if(d<radius)
      return doRule0(x,y);
    else
      return doRule1(x,y);}
  
  int doRule0(int x,int y){
    int 
      cell=cellfield[x][y],
      live=getLive(x,y);
    if(cell==DEAD){
      if(live>3)
        return LIVE;
      else
        return DEAD;
    }else{//cell==LIVE
      if(live>6)
        return DEAD;
      else
        return LIVE;}}
  
  int doRule1(int x,int y){
    int 
      cell=cellfield[x][y],
      live=getLive(x,y);
    if(cell==DEAD){
      if(live>2)
        return LIVE;
      else
        return DEAD;
    }else{//cell==LIVE
      if(live>7)
        return DEAD;
      else
        return LIVE;}}
  

  
  /*
   * ################################
   * OBSERVERS
   * ################################
   */
  
  public List<DSTObserver> observers=new ArrayList<DSTObserver>();
  
  void notifyObservers(){
    for(DSTObserver a:observers)
      a.advanced();}
  

}
