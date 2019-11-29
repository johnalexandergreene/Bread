package org.fleen.bread.app.communityExhibition2019_11_27;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DVector;

public class RModel{
  
  static final int TESTPCOUNT=2;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public RModel(double width,double height){
    this.width=width;
    this.height=height;
    center=new DPoint(width/2,height/2);
    
  //TEST
    
//    circles.add(new PCircle(this,120,100,33));
//    circles.add(new PCircle(this,220,100,55));
//    circles.add(new PCircle(this,120,300,11));
//    circles.add(new PCircle(this,220,300,22));
    
    }
  
  /*
   * ################################
   * FIELD
   * ################################
   */
  
  public double width,height;
  DPoint center;
  
  
  /*
   * ################################
   * CIRCLES
   * ################################
   */
  
  public int age=0; //the number of times that advanceState has been invoked
  public List<PCircle> circles=new ArrayList<PCircle>();
  
  public void clear(){
    circles.clear();
    age=0;}
  
  public void advanceState(){
    removeDeadCircles();
    advanceCircles();
    advancePhysics();
    conditionallyCreateCircles();
    age++;
    notifyObservers();}
  
  /*
   * a phenomenon changed it's state. Growing or shrinking or maintaining or throbbing or something
   * offering data for a circle, it's location and radius. Its color, sound, throbbing frequency...
   */
  void advanceCircles(){
    for(PCircle p:circles)
      p.advance();}
  
  void removeDeadCircles(){
    List<PCircle> toremove=new ArrayList<PCircle>();
    for(PCircle p:circles)
      if(p.killMe())
        toremove.add(p);
    circles.removeAll(toremove);
    System.out.println("removed "+toremove.size());}
  
  static final double BASECIRCLECREATIONPROBABILITY=0.1;
  
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
  void conditionallyCreateCircles(){
    if(circles.isEmpty()){
      createFirstCircle();
    }else{
      if(rnd.nextDouble()<BASECIRCLECREATIONPROBABILITY){
        createCircle();
      }
    }
    
    //TEST
//    if(phenomena.size()<TESTPCOUNT)
//      phenomena.add(new Phenomenon(this));
    
  }
  
  Random rnd=new Random();
  
  static final double CIRCLEMAXRADIUS=70,CIRCLEMINRADIUS=20;
  
  void createCircle(){
    DPoint p=getRandomFreePoint();
    if(p==null)return;
    PCircle c=new PCircle(
      this,
      p,
      getRandomCircleRadius(),
      getRandomCircleAttack(),
      getRandomCircleDecay(),
      getRandomCircleLifespan());
    circles.add(c);}
  
  void createFirstCircle(){
    PCircle c=new PCircle(
      this,
      center,
      getRandomCircleRadius(),
      getRandomCircleAttack(),
      getRandomCircleDecay(),
      getRandomCircleLifespan());
    circles.add(c);}
  
  static final int CIRCLEMAXLIFESPAN=100,CIRCLEMINLIFESPAN=20;
  
  int getRandomCircleLifespan(){
    int a=rnd.nextInt(CIRCLEMAXLIFESPAN-CIRCLEMINLIFESPAN)+CIRCLEMINLIFESPAN;
    return a;}
  
  static final double CIRCLEATTACKMAX=0.25,CIRCLEATTACKMIN=0.08;
  
  double getRandomCircleAttack(){
    return rnd.nextDouble()*(CIRCLEATTACKMAX-CIRCLEATTACKMIN)+CIRCLEATTACKMIN;}
  
  static final double CIRCLEDECAYMAX=0.25,CIRCLEDECAYMIN=0.08;
  
  double getRandomCircleDecay(){
    return rnd.nextDouble()*(CIRCLEDECAYMAX-CIRCLEDECAYMIN)+CIRCLEDECAYMIN;}
  
  double getRandomCircleRadius(){
    return rnd.nextDouble()*(CIRCLEMAXRADIUS-CIRCLEMINRADIUS)+CIRCLEMINRADIUS;}
  
  static final int MAXFREEPOINTTESTS=100;
  
  /*
   * TODO the point gotten should be near another circle. Preferable in the crotch between 3 circles. To maintain clumpiness
   */
  private DPoint getRandomFreePoint(){
    boolean isfree=false;
    int testcount=0;
    DPoint p=null;
    while(!isfree&&testcount<MAXFREEPOINTTESTS){
      p=new DPoint(rnd.nextDouble()*width,rnd.nextDouble()*height);
      isfree=true;
      SEEK:for(PCircle c:circles){
        if(c.contains(p)){
          p=null;
          isfree=false;
          break SEEK;}}
      testcount++;}
    return p;}
  
  /*
   * ################################
   * PHYSICS
   * ################################
   */
  
  void advancePhysics(){
    doCollisions();
    List<DVector> v=new ArrayList<DVector>();
    for(PCircle p:circles)
      v.add(p.getVector());
    PCircle p;
    DPoint newpoint;
    for(int i=0;i<circles.size();i++){
      p=circles.get(i);
      newpoint=p.getCenter();
      newpoint.applyVector(v.get(i));
      p.addCenter(newpoint);}}
  
  private Set<Collision> collisions;
  
  private void doCollisions(){
    collisions=new HashSet<Collision>();
    double d;
    for(PCircle c0:circles){
      for(PCircle c1:circles){
        if(c0!=c1){
          d=(c0.getRadius()+c1.getRadius())-c0.getCenter().getDistance(c1.getCenter());
          if(d>0)
            collisions.add(new Collision(c0,c1));}}}}
  
  /*
   * return all collisions that involve the specified circle
   */
  public List<Collision> getCollisions(PCircle c){
    List<Collision> k=new ArrayList<Collision>();
    for(Collision a:collisions)
      if(a.c0==c||a.c1==c)
        k.add(a);
    return k;}
  
  /*
   * ################################
   * OBSERVERS
   * ################################
   */
  
  public List<FuzzballSystemObserver> observers=new ArrayList<FuzzballSystemObserver>();
  
  void notifyObservers(){
    for(FuzzballSystemObserver a:observers)
      a.advanced();}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  public String toString(){
    StringBuffer a=new StringBuffer("RMODEL");
    a.append("\n pcount : "+circles.size());
    a.append("\n age : "+age);
    return a.toString();}
  
}
