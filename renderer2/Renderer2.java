package org.fleen.bread.renderer2;

import java.awt.image.BufferedImage;

import org.fleen.bread.colorMap.ColorMap;
import org.fleen.forsythia.core.composition.ForsythiaComposition;

public interface Renderer2{
  
  BufferedImage createImage(
    int width,int height,
    ForsythiaComposition composition,
    ColorMap colormap);

}
