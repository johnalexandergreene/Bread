package org.fleen.bread.app.simpleAutomata001;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.bread.app.simpleAutomata001.production.SAObserver;
import org.fleen.bread.app.simpleAutomata001.production.Test;

/*
 * at every increment
 *   randomize westmost cells 
 *   apply cell rules to all cells 
 */
public class SA{
  
  public static final int 
    WIDTH=40,
    HEIGHT=40,
    CELLVALUERANGE=8;//values range 0 to CELLVALUERANGE-1

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
  
  int getNeighborNW(int x,int y){
    return getCell(x-1,y-1);}
  
  int getNeighborN(int x,int y){
    return getCell(x,y-1);}
  
  int getNeighborNE(int x,int y){
    return getCell(x+1,y-1);}
  
  int getNeighborE(int x,int y){
    return getCell(x+1,y);}
  
  int getNeighborSE(int x,int y){
    return getCell(x+1,y+1);}
  
  int getNeighborS(int x,int y){
    return getCell(x,y+1);}
  
  int getNeighborSW(int x,int y){
    return getCell(x-1,y+1);}
  
  int getNeighborW(int x,int y){
    return getCell(x-1,y);}
  
  int getLive(int x,int y){
    int[] n=getNeighbors(x,y);
    int l=0;
    for(int i:n)
      if(i==LIVE)l++;
    return l;}
  
  int getNeighborSum(int x,int y){
    int[] n=getNeighbors(x,y);
    int l=0;
    for(int i:n)
      l+=i;
    return l;}
 
  /*
   * ################################
   * STATE
   * ################################
   */
  
  public int age=0;
  
  public void advanceState(){
    age++;
    runAutomaton();
    notifyObservers();
  }
  
  Random random=new Random();
  
  /*
   * ################################
   * CELL AUTOMATON
   * ################################
   */
  
  void runAutomaton(){
    int[][] nextstate=new int[WIDTH][HEIGHT];
    for(int x=0;x<WIDTH;x++){
      for(int y=0;y<HEIGHT;y++){
        nextstate[x][y]=doCellLogic(x,y);}}
    cellfield=nextstate;}
  
  static final int LIVE=1,DEAD=0;
  
  int doCellLogic(int x,int y){
    
    int 
      c=cellfield[x][y],
      s=getNeighborSum(x,y),
      r=0;
    r=c;
    //
    if(x%4>1){
      r=getNeighborS(x,y);
    }else if(y%4>1){
      r=getNeighborW(x,y);}
    if(s>3&&r>1)
      r+=s;
    
    r+=(age+x+y)%4;
    
    if((random.nextDouble()*(Test.DURATION/3))<age)
      r+=age%3;
    
    
    if((random.nextDouble()*(Test.DURATION))<age&&s>0)
      r=s+((x*y)%4);
      
    
    
    
    
    return r;}

  
  
  /*
   * ################################
   * OBSERVERS
   * ################################
   */
  
  public List<SAObserver> observers=new ArrayList<SAObserver>();
  
  void notifyObservers(){
    for(SAObserver a:observers)
      a.advanced();}
  

}
