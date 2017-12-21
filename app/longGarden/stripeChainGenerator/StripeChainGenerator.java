package org.fleen.bread.app.longGarden.stripeChainGenerator;

import org.fleen.bread.app.longGarden.LongGarden;

public class StripeChainGenerator{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public StripeChainGenerator(LongGarden lg){
    this.lg=lg;}
  
  /*
   * ################################
   * LONG GARDEN
   * ################################
   */
  
  LongGarden lg;
  
  /*
   * ################################
   * LOOP
   * ################################
   */
  
  static final long UPDATE_STRIPE_CHAIN_PERIOD=500;
  private boolean run=false;
  
  public void start(){
    System.out.println("stripe chain generator start");
    run=true;
    new Thread(){
      public void run(){
        setPriority(NORM_PRIORITY);
        while(run){
          conditionallyUpdateStripeChain();
          try{
            Thread.sleep(UPDATE_STRIPE_CHAIN_PERIOD);
          }catch(Exception x){
            x.printStackTrace();}}}
    }.start();}
  
  public void stop(){
    System.out.println("stripe chain generator stop");
    run=false;}
  
  /*
   * ################################
   * STRIPE CHAIN
   * ################################
   */
  
  /*
   * if the viewport has gotten too close to the end of the stripechain then create another stripe.
   * if the last stripe in the stripeshain is far enough outside the viewport then remove it.
   */
  private void conditionallyUpdateStripeChain(){
    System.out.println("update stripechain");
    
  }
    

}
