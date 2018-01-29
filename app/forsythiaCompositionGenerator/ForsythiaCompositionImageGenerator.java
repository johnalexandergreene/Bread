package org.fleen.bread.app.forsythiaCompositionGenerator;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface ForsythiaCompositionImageGenerator{
  
  /*
   * given the present params, render an image and return it
   */
  BufferedImage getImage();
  
  /*
   * create a new forsythia composition
   */
  void regenerateComposition();
  
  /*
   * create a new colormap
   */
  void regenerateColorMap();
  
  /*
   * specify the dimensions of the image returned by getImage
   */
  void setImageDimensions(int w,int h);
  
  /*
   * specify the empty border surrounding the image of the rendered composition
   */
  void setBorderThickness(int t);
  
  /*
   * specify the color of the background upon which the composition is rendered, 
   *   which is also the color of the border
   */
  void setBackgroundAndBorderColor(Color c);

}
