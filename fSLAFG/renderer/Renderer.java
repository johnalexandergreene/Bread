package org.fleen.bread.fSLAFG.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.fleen.forsythia.core.composition.ForsythiaComposition;

public interface Renderer{
  
  BufferedImage createImage(int width,int height,ForsythiaComposition composition,Color[] palette,boolean rebuildcolormap);

}
