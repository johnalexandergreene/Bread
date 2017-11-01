package org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain;

import java.awt.geom.AffineTransform;

/*
 * A single node in a chain of nodes
 * connects to 0..2 other nodes, prior and next
 * refers to a ForsythiaComposition with a rectangular root.
 * 
 */
public interface Stripe2{
  
  int getStripeImageX();
  
  int getStripeImageWidth();
  
  void setTransform(AffineTransform t);
  
  AffineTransform getTransform();
  
}
