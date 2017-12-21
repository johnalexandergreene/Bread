package org.fleen.bread.app.longGarden.frameGenerator;

import java.awt.image.BufferedImage;

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
        frameperiod=lg.config.getFramePeriod(),
        t;
      public void run(){
        setPriority(MAX_PRIORITY);
        //we keep the frame period constant, of course
        while(run){
          t=System.currentTimeMillis();
          doFrame();
          t=System.currentTimeMillis()-t;
          try{
            if(t<frameperiod){
              Thread.sleep(frameperiod-t);
            }else{
              System.out.println("WARNING : Frame generation time exceeded period.");}
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
  
  public BufferedImage frame=null;
  
  private void doFrame(){
    System.out.println("do frame");
  }

}
