package org.fleen.bread.app.spray;

import java.util.Random;

public class Sprayer000_RndHStripeBlinker implements Sprayer{
  
  //as proportion of height
  static final double
    THICKNESSMIN=0.1,
    THICKNESSMAX=0.4;
  
  Random rnd=new Random();
  
  public void spray(Target target){
    System.out.println("spraying");
    double thickness=rnd.nextDouble()*(THICKNESSMAX-THICKNESSMIN)+THICKNESSMIN;
    int y0=(int)(rnd.nextDouble()*(target.height-thickness));
    for(int y=y0;y<y0+thickness;y++){
      for(int x=0;x<target.width;x++){
        target.cells[x][y]++;}}}

}
