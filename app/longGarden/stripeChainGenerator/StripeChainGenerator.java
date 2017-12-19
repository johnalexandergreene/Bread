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
  
  private boolean run=true;
  static final long CHECK_FOR_NEW_STRIPE_NEEDED_PERIOD=1000;
  
  public void start(){
    new Thread(){
      public void run(){
        while(run){
          if(needAnotherStripe())
            createStripe();}
          try{
            Thread.sleep(CHECK_FOR_NEW_STRIPE_NEEDED_PERIOD);
          }catch(Exception x){
            x.printStackTrace();}}
    }.start();}
  
  public void stop(){
    run=false;}
  
  /*
   * ################################
   * STRIPE CHAIN
   * ################################
   */
  
  //TODO
  private boolean needAnotherStripe(){
    return false;
  }
  
  private void createStripe(){
    
  }
    

}
