package org.fleen.bread.app.dancingSquishyThing.production;

import java.util.ArrayList;
import java.util.List;

import org.fleen.geom_2D.DVector;
import org.fleen.geom_2D.GD;

public class RModel{
  
  static final int TESTPCOUNT=2;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public RModel(int width,int height){
    this.width=width;
    this.height=height;
    
  //TEST
    
    phenomena.add(new Phenomenon(this,120,100,33));
    phenomena.add(new Phenomenon(this,220,100,55));
    phenomena.add(new Phenomenon(this,120,300,11));
    phenomena.add(new Phenomenon(this,220,300,22));
    
    }
  
  /*
   * ################################
   * FIELD
   * ################################
   */
  
  public int width,height;
  
  /*
   * ################################
   * PHENOMENA
   * ################################
   */
  
  public int age=0; //the number of times that advanceState has been invoked
  public List<Phenomenon> phenomena=new ArrayList<Phenomenon>();
  
  public void clear(){
    phenomena.clear();
    age=0;}
  
  public void advanceState(){
    removeDeadPhenomena();
    advancePhenomena();
    advancePhysics();
    conditionallyCreatePhenomena();
    age++;
    notifyObservers();}
  
  /*
   * a phenomenon changed it's state. Growing or shrinking or maintaining or throbbing or something
   * offering data for a circle, it's location and radius. Its color, sound, throbbing frequency...
   */
  void advancePhenomena(){
    for(Phenomenon p:phenomena)
      p.advance();}
  
  void removeDeadPhenomena(){
    List<Phenomenon> toremove=new ArrayList<Phenomenon>();
    for(Phenomenon p:phenomena)
      if(p.dead)
        toremove.add(p);
    phenomena.removeAll(toremove);
    System.out.println("removed "+toremove.size());}
  
  /*
   * create 0..n new phenomenon objects
   * place them, initialize them
   * 
   * what we do depends on anything we like
   *   the present population of the model
   *   the population of certain phenomenon types
   *   the age of the model (in terms of the number of times that advance has been invoked)
   *   ... etc
   * 
   */
  void conditionallyCreatePhenomena(){
    //TEST
//    if(phenomena.size()<TESTPCOUNT)
//      phenomena.add(new Phenomenon(this));
    
  }
  
  /*
   * ################################
   * PHYSICS
   * ################################
   */
  
  static final double MAXVECTORMAGNITUDE=0.1;
  
  /*
   * the phenomenon-representation-circles (PRC) grow and shrink and press on each other
   * account for the physics of that
   * the PRCs gravitate to the center of the clump, thus grouping and packing themselves. 
   * Account for the physics of that too 
   */
  void advancePhysics(){
    //get raw vectors
    List<DVector> gravity=getGravityVectors();
    List<DVector> bump=getBumpVectors();
    //sum, normalize and apply
    Phenomenon p;
    DVector sum;
    for(int i=0;i<phenomena.size();i++){
      p=phenomena.get(i);
      sum=new DVector(gravity.get(i),bump.get(i));
      if(sum.magnitude>MAXVECTORMAGNITUDE)
        sum.magnitude=MAXVECTORMAGNITUDE;
      p.center.applyVector(sum);}}
  
  //--------------------------------
  
  List<DVector> getGravityVectors(){
    List<DVector> gv=new ArrayList<DVector>();
    for(Phenomenon p:phenomena)
      gv.add(getGravityVector(p));
    return gv;}
  
  static final double GRAVMAGFACTOR=8.0,GRAVMINDIS=199.0;
  
  DVector getGravityVector(Phenomenon p){
    //get neighbors
    List<Phenomenon> neighbors=new ArrayList<Phenomenon>();
    for(Phenomenon a:phenomena)
      if(a!=p)neighbors.add(a);
    //get vectors
    //get distance and direction to each neighbor. Create attractive vector
    List<DVector> gvectors=new ArrayList<DVector>();
    double dir,mag,dis;
    for(Phenomenon p0:neighbors){
      dis=p.center.getDistance(p0.center);
      if(dis<(p.radius+p0.radius+GRAVMINDIS)){
        dir=p.center.getDirection(p0.center);
        mag=(1.0/dis)*GRAVMAGFACTOR;
        gvectors.add(new DVector(dir,mag));}}
    //sum vectors
    DVector sum=new DVector(gvectors);
    //
    return sum;}
  
  //--------------------------------
  
  static final double BUMPERTHICKNESS=4.0,BUMPMAGTWEAK=10;
  
  List<DVector> getBumpVectors(){
    List<DVector> bv=new ArrayList<DVector>();
    for(Phenomenon p:phenomena)
      bv.add(getBumpVector(p));
    return bv;}
  
  DVector getBumpVector(Phenomenon p){
    //get neighbors
    List<Phenomenon> neighbors=new ArrayList<Phenomenon>();
    for(Phenomenon a:phenomena)
      if(a!=p)neighbors.add(a);
    //get vectors
    //get distance and direction to each neighbor. Create repulsive vector
    List<DVector> gvectors=new ArrayList<DVector>();
    double dis,dir,mag,bumpdis;
    for(Phenomenon p0:neighbors){
      System.out.println("------------------");
      System.out.println("p:"+p.center);
      System.out.println("p0:"+p0.center);
      System.out.println("p radius :"+p.radius);
      dis=p.center.getDistance(p0.center);
      System.out.println("p-p0 dis = "+dis);
      bumpdis=p.radius+p0.radius+BUMPERTHICKNESS;
      System.out.println("bumpdis = "+bumpdis);
      if(dis<bumpdis){
        dir=p.center.getDirection(p0.center);
        dir=GD.normalizeDirection(dir+GD.PI);
        mag=((bumpdis-dis)/bumpdis)*BUMPMAGTWEAK;
        gvectors.add(new DVector(dir,mag));}}
    //sum vectors
    DVector sum=new DVector(gvectors);
    //
    System.out.println("bump vector = "+sum);
    return sum;}
  
  /*
   * ################################
   * OBSERVERS
   * ################################
   */
  
  public List<DSTObserver> observers=new ArrayList<DSTObserver>();
  
  void notifyObservers(){
    for(DSTObserver a:observers)
      a.advanced();}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    StringBuffer a=new StringBuffer("RMODEL");
    a.append("\n pcount : "+phenomena.size());
    a.append("\n age : "+age);
    return a.toString();}
  
}
