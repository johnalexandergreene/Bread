package org.fleen.bread.app.spray.sprayer;

import java.util.Random;

import org.fleen.bread.app.spray.Target;

public class Sprayer001_AgeFnk implements Sprayer{
  
  //as proportion of height
  static final double
    THICKNESSMIN=0.1,
    THICKNESSMAX=0.4;
  
  Random rnd=new Random();
  
  public void spray(Target target){
    for(int x=0;x<target.width;x++){
      for(int y=0;y<target.height;y++){
        target.cells[x][y]=(x*y*target.age)%4;
      }
    }
    }

}
