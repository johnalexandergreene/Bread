package org.fleen.bread.app.forsythiaCompositionGenerator;

import java.awt.image.BufferedImage;

public interface ForsythiaCompositionRasterImageGenerator{
  
  void regenerateComposition();
  
  void regenerateColorMap();
  
  BufferedImage getImage();
  
  void setImageDimensions(int w,int h);

}
