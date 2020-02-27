package org.fleen.bread.app.buzzingCircles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DVector;
import org.fleen.geom_2D.GD;

/*
 * phenomenon circle
 * a phenomenon in abstract, circular form
 * a circle that 
 *   is created, born among other circles, bounces off other circles like in a mosh pit
 *   grows from an invisible point 
 *   lingers
 *   shrinks to invisible
 *   is discarded
 *   
 * might paint an image on it too
 */
public class PCircle{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public PCircle(RModel rmodel,DPoint center,double radius,double attack,double decay,int lifespan){
    this.rmodel=rmodel;
    centers.add(new DPoint(center));
    this.maxradius=radius;
    this.attack=attack;
    this.decay=decay;
    this.lifespan=lifespan+(int)(maxradius/attack)+(int)(maxradius/decay);
    birthday=rmodel.age;
    initForwardAndSpin();}
  
  /*
   * ################################
   * RMODEL
   * ################################
   */
  
  RModel rmodel;
  
  /*
   * ################################
   * ATTACK AND DECAY
   * Control the growth and shrinkage rate of this circle
   * Indirectly, control the lifespan
   * ################################
   */
  
  double attack,decay;
  
  /*
   * ################################
   * ADVANCE
   * Every frame of the animation we call the advance method
   * (we also call the advance-physics method, which controls the bouncing aroubnd, but that's in RModel)
   * We probably update the image here to fit the location, spin and/or radius
   * ################################
   */
  
  public void advance(){
    
  }
  
  /*
   * ################################
   * CENTER
   * we use the average of a list of centers
   * a new center added at every iteration of the system
   * this acts as a shock-absorber, so the circles don't vibrate so much
   * ################################
   */
  
  static final int CENTERLISTMAXSIZE=22;
  List<DPoint> centers=new ArrayList<DPoint>(CENTERLISTMAXSIZE+1);
  
  public void addCenter(DPoint c){
    centers.add(c);
    if(centers.size()>CENTERLISTMAXSIZE)
      centers.remove(0);}
  
  public DPoint getCenter(){
    DPoint g=GD.getPoint_Mean(centers);
    return g;}
  
  /*
   * ################################
   * LIFE AND DEATH
   * ################################
   */
  
  public int 
    birthday,
    lifespan;
  
  public int getAge(){
    return rmodel.age-birthday;}
  
  public boolean killMe(){
    return getAge()>lifespan;}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  Random rnd=new Random();
  
  /*
   * ++++++++++++++++
   * RADIUS
   * Dependent on age, attack, decay and maxradius
   * ++++++++++++++++
   */
  
  private double maxradius;
  
  public double getRadius(){
    double a=getAge();
    double r=attack*a;
    if(r>maxradius)r=maxradius;
    double shrinkpoint=lifespan-(maxradius/decay);
    if(a>shrinkpoint)
      r=maxradius-((a-shrinkpoint)*decay);
    return r;}
  
  /*
   * ++++++++++++++++
   * FORWARD AND SPIN
   * ++++++++++++++++
   */
  
  
  //TODO descretize these values to make the system more unified?
  static final double[] SPINCREMENTS={
    GD.PI2*0.05,
    GD.PI2*0.01,
    -GD.PI2*0.01,
    -GD.PI2*0.05,};
  
  public double 
    //radians. forward is initincrement+age*spincrement
    initforward,
    spincrement;

  void initForwardAndSpin(){
    initforward=rnd.nextDouble()*GD.PI2;
    spincrement=SPINCREMENTS[rnd.nextInt(SPINCREMENTS.length)];}
  
  public double getForward(){
    double f=spincrement+(getAge()*spincrement);
    return f;}
  
  /*
   * ################################
   * VECTOR
   * ################################
   */
  
  DVector vector=new DVector(0,0);
  static final double 
    GRAVITYVECTORMAG=3.1,
    VECTORRETARD=0.27,//0.05,
    COLLISIONMAGFACTOR=0.005;
  
  public DVector getVector(){
    vector.add(getGravityVector());
    DVector c=getSummedCollisionVectors();
    if(c!=null)vector.add(c);
    vector.magnitude*=VECTORRETARD;
    return vector;}
  
  private DVector getSummedCollisionVectors(){
    //are there a collisions?
    List<Collision> collisions=rmodel.getCollisions(this);
    if(collisions.isEmpty())return null;
    //sum those collision vectors
    DVector s=new DVector(0,0);
    for(Collision c:collisions)
      s.add(getCollisionVector(c));
    return s;}
  
  private DVector getCollisionVector(Collision collision){
    //specify which circle got collided with
    PCircle other;
    if(collision.c0==this)
      other=collision.c1;
    else
      other=collision.c0;
    //get dir and mag
    double dir=other.getCenter().getDirection(getCenter());
    double mag=(other.getRadius()+getRadius())-other.getCenter().getDistance(getCenter())*COLLISIONMAGFACTOR;
    //scale mag by relative size
    //in a collision between small and large circles, the small gets pushed harder than the large
    double relativesizemagfactor=other.getRadius()/getRadius();
    relativesizemagfactor=Math.sqrt(relativesizemagfactor);//flatten the curve a bit
    if(relativesizemagfactor>2)relativesizemagfactor=2;
    if(relativesizemagfactor<0.5)relativesizemagfactor=0.5;
    mag*=relativesizemagfactor;
    //
    DVector v=new DVector(dir,mag);
    return v;}
  
  private DVector getGravityVector(){
    double dir=getCenter().getDirection(rmodel.center);
    return new DVector(dir,GRAVITYVECTORMAG);}
  
  public boolean contains(DPoint p){
    double a=p.getDistance(getCenter());
    return a<getRadius();}

}
