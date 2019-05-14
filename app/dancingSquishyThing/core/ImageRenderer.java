package org.fleen.bread.app.dancingSquishyThing.core;

import java.awt.image.BufferedImage;

public interface ImageRenderer{
  
  /*
   * the whelmer
   * the span of the rendered image square
   * the size of the whelmer image as a proportion of the image span : 0>size<=1 
   */
  BufferedImage getImage(Whelmer whelmer,int span,double size);
    
}
