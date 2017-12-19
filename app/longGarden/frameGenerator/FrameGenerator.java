package org.fleen.bread.app.longGarden.frameGenerator;

import org.fleen.bread.app.longGarden.LongGarden;

public class FrameGenerator{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public FrameGenerator(LongGarden lg){
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
  
  public void start(){
    new Thread(){
      long 
        frameperiod=lg.config.frameperiod,
        t;
      public void run(){
        while(run){
          t=System.currentTimeMillis();
          doFrame();
          t=System.currentTimeMillis()-t;
          try{
            Thread.sleep(frameperiod-t);
          }catch(Exception x){
            x.printStackTrace();}}}
    }.start();}
  
  public void stop(){
    run=false;}
  
  /*
   * ################################
   * FRAME
   * ################################
   */
  
  private void doFrame(){
    
  }

}
