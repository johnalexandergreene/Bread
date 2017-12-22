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
  
  private boolean run=false;
  
  public void start(){
    System.out.println("frame generator start");
    run=true;
    new Thread(){
      long 
        frameperiod=lg.config.getFramePeriod(),
        t;
      public void run(){
        setPriority(MAX_PRIORITY);
        //we keep the frame period constant, of course
        while(run){
          t=System.currentTimeMillis();
          incrementFrame();
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
    System.out.println("frame generator stop");
    run=false;}
  
  /*
   * ################################
   * GEOMETRY
   * The position of the frame relative to the stripechain
   * the stripechain is a rectangle 
   *   variable width
   *   same height as ui viewport 
   * the frame is a rectangle
   *   same height and width as the ui viewport
   *   incrementally moves from left to right along the stripechain
   *     which is to say, we increment the x coor of the left edge
   *   also adjust value of that xcoor when we modify the stripechain  
   * ################################
   */
  
  private int framex;
  
  public int getFrameWidth(){
    return lg.ui.getViewport().getWidth();}
  
  public int getFrameHeight(){
    return lg.ui.getViewport().getHeight();}
  
  public int getFrameX(){
    return framex;}
  
  /*
   * use this from stripechaingenerator when we modify the stripechain
   * to account for changes in stripechain length
   */
  public void setFrameX(int x){
    framex=x;}
  
  public int getFrameY(){
    return 0;}

  /*
   * this gives us our scrolling animation
   */
  private void incrementFrame(){
    framex++;
    updateFrameImage();}
  
  /*
   * ################################
   * IMAGE
   * A piece of the stripechain image
   * ################################
   */
  
  public BufferedImage frameimage=null;
  
  private void updateFrameImage(){
    BufferedImage sci=lg.stripechaingenerator.getImage();
    frameimage=sci.getSubimage(getFrameX(),getFrameY(),getFrameWidth(),getFrameHeight());
    lg.ui.getViewport().repaint();}
  
}
