package org.fleen.bread.app.forsythiaCompositionGenerator;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface ForsythiaCompositionGenerator{
  
  /*
   * width, height, border thickness, color of background and border
   */
  BufferedImage getComposition(int w,int h,int border,Color c);

}
