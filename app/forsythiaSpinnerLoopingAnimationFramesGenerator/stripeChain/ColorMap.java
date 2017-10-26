package org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain;

import java.awt.Color;
import java.util.Map;

import org.fleen.forsythia.core.composition.FPolygon;

public interface ColorMap extends Map<FPolygon,Color>{
  
  Color getColor(FPolygon p);

}
