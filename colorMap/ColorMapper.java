package org.fleen.bread.colorMap;

import java.awt.Color;

import org.fleen.forsythia.core.composition.ForsythiaComposition;

public interface ColorMapper{
  
  ColorMap getColorMap();
  
  void setPalette(Color[] p);
  
  Color[] getPalette();
  
  void setComposition(ForsythiaComposition c);
  
  ForsythiaComposition getComposition();

}
