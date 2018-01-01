package org.fleen.bread.app.longGarden.stripeChain;

/*
 * A single node in a chain of nodes
 * connects to 0..2 other nodes, prior and next
 * refers to a ForsythiaComposition with a rectangular root.
 * 
 */
public interface Stripe{
  
  int getWidth();
  
  /*
   * location of the left edge of this stripe's image within the stripechain image. 
   */
  int getX();
  
}
