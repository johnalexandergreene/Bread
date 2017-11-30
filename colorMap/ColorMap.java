package org.fleen.bread.colorMap;

import java.awt.Color;

import org.fleen.forsythia.core.composition.FPolygon;

/*
 * map colors to polygons prettily
 */
public interface ColorMap{
  
  Color getColor(FPolygon p);

}
