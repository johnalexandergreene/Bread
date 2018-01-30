package org.fleen.bread.app.forsythiaCompositionGenerator;

import java.awt.image.BufferedImage;

public interface ForsythiaCompositionImageGenerator{
  
  void regenerateComposition();
  
  void regenerateColorMap();
  
  BufferedImage getImage();
  
  void setImageDimensions(int w,int h);

}
