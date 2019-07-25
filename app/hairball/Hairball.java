package org.fleen.bread.app.hairball;

import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.fleen.bread.app.hairball.production.HairballObserver;
import org.fleen.geom_2D.CurveSmoother_Closed;
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
  static final int INITCHAINCELLCOUNT=12;
  static final double INITCHAINRADIUS=111;
  
  private void initChain(){
    double
      dir,
      dirincrement=GD.PI2/INITCHAINCELLCOUNT;
    double[] p;
    Cell c,cprior=null;
    for(int i=0;i<INITCHAINCELLCOUNT;i++){
      dir=i*dirincrement;
      p=GD.getPoint_PointDirectionInterval(0,0,dir,INITCHAINRADIUS);
      c=new Cell(p);
      if(i==0){
        reference=c;
      }else if(i<(INITCHAINCELLCOUNT-1)){
        cprior.n1=c;
        c.n0=cprior;
      }else{
        cprior.n1=c;
        c.n0=cprior;
        c.n1=reference;
        reference.n0=c;}
      cprior=c;}
    System.out.println("CHAIN INITITIALIZED");
    System.out.println("chain size = "+getChainSize());}
  
  public int getChainSize(){
    Iterator<Cell> i=getCellIterator();
    int a=0;
    while(i.hasNext()){
      i.next();
      a++;}
    return a;}
  
  public Iterator<Cell> getCellIterator(){
    return new CellIterator();}
  
  class CellIterator implements Iterator<Cell>{
    
    boolean hasiterated=false;
    Cell present=reference;

    public boolean hasNext(){
      if(present.n0==present||present.n1==present)
        throw new IllegalArgumentException("DEGENERATE!");
      //
      return !(hasiterated&&(present.n1==reference));}

    public Cell next(){
      if(!hasiterated){
        hasiterated=true;
        return present;
      }else{
        present=present.n1;
        return present;}}

    public void remove(){}}
  
  /*
   * ################################
   * SMOOTHED PATH
   * ################################
   */
  
  static final int SMOOTHNESS=2;
  CurveSmoother_Closed smoother=new CurveSmoother_Closed();
  
  public Path2D getSmoothedPath(){
    List<double[]> 
      p=getPoints(),
      sp=smoother.getSmoothedPolygon(p,SMOOTHNESS);
    Path2D path=new Path2D.Double();
    double[] a=sp.get(0);
    path.moveTo(a[0],a[1]);
    for(int i=1;i<sp.size();i++){
      a=sp.get(i);
      path.lineTo(a[0],a[1]);}
    path.closePath();
    return path;}
  
  List<double[]> getPoints(){
    List<double[]> points=new ArrayList<double[]>();
    Iterator<Cell> i=getCellIterator();
    Cell c;
    while(i.hasNext()){
      c=i.next();
      points.add(new double[]{c.x,c.y});}
    return points;}
  
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
  
  /*
   * randomly move a cell relative to its neighbors to make a kink or unkink
   * 
   * then
   * 
   * check distance between cells
   * if the distance between 2 cells is too large, create a new cell between them
   * if a cell's neighbors are too close, center it or remove it.
   * if we remove a cell and that cell is the reference, get its neighor for a new reference 
   */
  void runAutomaton(){
    randomKink();
    fillGaps();
    
    
    
  }
  
  void randomKink(){
    List<double[]> newlocations=new ArrayList<double[]>();
    Iterator<Cell> i=getCellIterator();
    Cell c;
    while(i.hasNext()){
      c=i.next();
      newlocations.add(getNewLocation(c));}
    //
    i=getCellIterator();
    int a=0;
    while(i.hasNext()){
      c=i.next();
      c.setLocation(newlocations.get(a));
      a++;}}
  
  Random rnd=new Random();
  
  static final double KINKFACTOR0=0.1,KINKFACTOR1=0.3;
  
  double[] getNewLocation(Cell c){
    double dir=GD.getDirection_PointPoint(c.n0.x,c.n0.y,c.n1.x,c.n1.y);
    if(rnd.nextBoolean())
      dir=GD.normalizeDirection(dir+GD.HALFPI);
    else
      dir=GD.normalizeDirection(dir-GD.HALFPI);
    double dis=GD.getDistance_PointPoint(c.n0.x,c.n0.y,c.n1.x,c.n1.y);
    double f=rnd.nextDouble()*(KINKFACTOR1-KINKFACTOR0)+KINKFACTOR0;
    dis*=f;
    double[] l=GD.getPoint_PointDirectionInterval(c.x,c.y,dir,dis);
    return l;}
  
  static final double MINGAP=44;
  
  void fillGaps(){
    Iterator<Cell> i=getCellIterator();
    List<Cell> needfill=new ArrayList<Cell>();
    Cell c;
    double dis;
    while(i.hasNext()){
      c=i.next();
      dis=GD.getDistance_PointPoint(c.x,c.y,c.n1.x,c.n1.y);
      if(dis>MINGAP)
        needfill.add(c);}
    //
    for(Cell a:needfill)
      fillGap(a);}
  
  void fillGap(Cell c){
    double[] a=GD.getPoint_Mid2Points(c.x,c.y,c.n1.x,c.n1.y);
    Cell b=new Cell(a),d;
    d=c.n1;
    c.n1=b;
    b.n0=c;
    b.n1=d;
    d.n0=b;}
  
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
