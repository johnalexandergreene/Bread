package org.fleen.bread.app.dancingSquishyThing.production;

import java.util.Random;

import org.fleen.geom_2D.DPoint;

public class Phenomenon{
  
  public Phenomenon(RModel rmodel){
    this.rmodel=rmodel;
    //TEST
    Random r=new Random();
    center=new DPoint(r.nextDouble()*rmodel.width,r.nextDouble()*rmodel.height);
    radius=64;
    birthday=rmodel.age;
    maxage=r.nextInt(50)+r.nextInt(50)+r.nextInt(50)+1000;}
  
  public Phenomenon(RModel rmodel,double x,double y,double r){
    this.rmodel=rmodel;
    this.center=new DPoint(x,y);
    this.radius=r;
    birthday=rmodel.age;
    maxage=1000;}
  
  RModel rmodel;
  
  boolean dead=false;
  
  public void advance(){
//    if(rmodel.age>(birthday+maxage))
//      dead=true;
    }
  
  /*
   * we'll need lifespan
   * entryspeed,exitspeed
   * type...
   */
  public double radius;
  public int birthday,maxage;
  public DPoint center;

}
