package org.fleen.bread.app.radiantWavyThrobber;

import java.awt.geom.Path2D;

import org.fleen.geom_2D.GD;

public class Ring{
  
  Ring(double x,double y,double r){
    this.x=x;
    this.y=y;
    this.r=r;
  }
  
  public double x,y,r;
  
  static final int SIDES=120;
  
  public Path2D getPath(){
    Path2D p=new Path2D.Double();
    double[] a=GD.getPoint_PointDirectionInterval(x,y,0,r);
    p.moveTo(a[0],a[1]);
    for(int i=1;i<SIDES;i++){
      a=GD.getPoint_PointDirectionInterval(x,y,((double)(i*GD.PI2))/SIDES,r);
      p.lineTo(a[0],a[1]);}
    p.closePath();
    return p;}

}
