package org.fleen.bread.app.spray;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import org.fleen.bread.app.spray.sprayer.Sprayer000_RndHStripeBlinker;
import org.fleen.bread.app.spray.sprayer.Sprayer001_AgeFnk;
import org.fleen.bread.app.spray.sprayer.Sprayer002_RovingRainbow;
import org.fleen.bread.app.spray.videoRenderer.VideoRenderer;

public class Test{
 
  public static final int 
    WIDTH=32,
    HEIGHT=32,
    FRAMECOUNT=300;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test(){
    target=new Target(WIDTH,HEIGHT,observer);
    ui=new UI(this);
    videorenderer=new VideoRenderer(this);
    exporter=new Exporter(this);}
  
  /*
   * ################################
   * FUZZBALLSYSTEM
   * ################################
   */
  
  public Target target;
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  UI ui;
  
  /*
   * ################################
   * VIDEO RENDERER
   * ################################
   */
 
  VideoRenderer videorenderer;
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  TargetObserver observer=new TargetObserver(){
    public void advanced(){
      videorenderer.render();
      ui.repaint();
      //rendersoundframe
      exporter.exportVideoFrame();
      }};
      
  /*
   * ################################
   * EXPORT
   * ################################
   */
      
  static final String EXPORTDIR="/home/john/Desktop/sprayerexport";
  static final double EXPORTSCALE=24;
  static final AffineTransform EXPORTTRANSFORM=AffineTransform.getScaleInstance(EXPORTSCALE,EXPORTSCALE);
  public File exportdir=new File(EXPORTDIR);
  Exporter exporter;
  
  public BufferedImage getImageForExport(){
    BufferedImage a=new BufferedImage(
      (int)(videorenderer.image.getWidth()*EXPORTSCALE),
      (int)(videorenderer.image.getHeight()*EXPORTSCALE),
      BufferedImage.TYPE_INT_RGB);
    Graphics2D g=a.createGraphics();
    g.drawImage(videorenderer.image,EXPORTTRANSFORM,null);
    return a;}

  /*
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * MAIN
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   */
  
  public static final void main(String[] a){
    Test test=new Test();
    test.target.addSprayer(new Sprayer001_AgeFnk());
    test.target.addSprayer(new Sprayer002_RovingRainbow());
    for(int i=0;i<33;i++)
      test.target.addSprayer(new Sprayer000_RndHStripeBlinker());
    //
    for(int i=0;i<FRAMECOUNT;i++){
      test.target.advance();
//      System.out.println("FBS : "+test.fuzzballsystem);
     try{
      Thread.sleep(88);
     }catch(Exception x){};}}
    
}
