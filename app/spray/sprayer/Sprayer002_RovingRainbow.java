package org.fleen.bread.app.spray.sprayer;

import java.util.Random;

import org.fleen.bread.app.spray.Target;

public class Sprayer002_RovingRainbow implements Sprayer{
  
  //as proportion of height
  static final double
    THICKNESSMIN=0.1,
    THICKNESSMAX=0.4;
  
  Random rnd=new Random();
  
  public void spray(Target target){
    int d=(int)(target.age%target.height);
    for(int x=0;x<target.width;x++){
      for(int y=0;y<target.height;y++){
        if(y>d-4&&y<d+4)
          target.cells[x][y]++;
      }
    }
    }

}
