package org.fleen.bread.renderer;

import java.awt.image.BufferedImage;

import org.fleen.bread.colorMap.ColorMap;
import org.fleen.forsythia.core.composition.ForsythiaComposition;

public interface Renderer{
  
  BufferedImage createImage(int width,int height);
  
  void setComposition(ForsythiaComposition c);
  
  void setColorMap(ColorMap m);

}
