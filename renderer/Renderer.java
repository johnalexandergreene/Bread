package org.fleen.bread.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.fleen.forsythia.core.composition.ForsythiaComposition;

/*
 * render forsythia composition to RGB bitmap
 */
public interface Renderer{
  
  BufferedImage createImage(
      //image dims
      int width,int height,
      //the composition we're rendering
      ForsythiaComposition composition,
      //the colors. sometimes order matters
      Color[] palette,
      //if we're rendering a new composition then we want to set this to true
      //if we're rerendering a composition (say, at a new resolution) then we want 
      //to set it to false, so the colors are all the same as the prior rendering
      boolean rebuildcolormap);
  
//  BufferedImage createImage(
//      int width,int height,
//      ForsythiaComposition composition,
//      Color[][] palette,
//      boolean rebuildcolormap);

}
