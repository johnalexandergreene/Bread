package org.fleen.bread.app.eggSacDespoiler;

import java.util.ArrayList;
import java.util.List;

import org.fleen.geom_2D.GD;

public class Emitter{
  
  public Emitter(ESD rwt,double x,double y,double d){
    this.rwt=rwt;
    ix=x;
    iy=y;
    idir=d;
  }
  
  ESD rwt;
  double ix,iy,idir;
  
  static final double MAXTHROWFACTOR=0.27;
  
  public double[] getCenter(){
    double 
      a=(((double)(rwt.age%300))/300)*GD.PI2,
      b=GD.sin(a),
      c=b*ESD.WIDTH*MAXTHROWFACTOR;
    double[] d=GD.getPoint_PointDirectionInterval(ix,iy,getDir(),c);
    return d;}
  
  public double getDir(){
    return GD.normalizeDirection(idir+(((double)(rwt.age%300))/300)*GD.PI2);
  }
  
  static final int RINGCOUNT=55;
  static final double RINGOFFSETFACTOR=30.0;
  static final int RINGOFFSETINTERVAL=15;
  
  public List<Ring> getRings(){
    List<Ring> rings=new ArrayList<Ring>();
    double r;
    double[] c=getCenter();
    for(int i=0;i<RINGCOUNT;i++){
      r=(((double)((rwt.age+i*RINGOFFSETINTERVAL)%100))/100)*GD.PI2*RINGOFFSETFACTOR;
      rings.add(new Ring(c[0],c[1],r));}
    return rings;}
  

}
