package org.fleen.bread.app.flerp0flerp2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.geom_2D.GD;

public class FuzzballGun_000 implements FuzzballGun{

  
  Random rnd=new Random();
  
  FuzzballGun_000(FuzzballSystem system){
    this.system=system;
    
    double d=GD.PI2*rnd.nextDouble();
    double[] p=GD.getPoint_PointDirectionInterval(system.centerx,system.centery,d,system.radius+100);
    startx=p[0];
    starty=p[1];
    dir=GD.normalizeDirection(d+GD.PI);
    
  }
  
  FuzzballSystem system;
  double startx,starty;
  double dir;
  
  public List<Fuzzball> getFuzzballs(){
    List<Fuzzball> fuzzballs=new ArrayList<Fuzzball>();
    
    double r=rnd.nextDouble()*10+15,d=rnd.nextDouble()*3+6;
    
    for(int i=0;i<system.radius*2+200;i+=30){
      double[] a=GD.getPoint_PointDirectionInterval(startx,starty,dir,i+system.age%30);
      fuzzballs.add(new Fuzzball(this,a[0],a[1],r,d));}
    
    return fuzzballs;
  }

}
