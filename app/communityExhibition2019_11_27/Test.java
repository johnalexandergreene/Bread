package org.fleen.bread.app.communityExhibition2019_11_27;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class Test{
 
  public static final int 
    WIDTH=100,
    HEIGHT=100,
    FRAMECOUNT=1800;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test(){
    fuzzballsystem=new FuzzballSystem(WIDTH,HEIGHT,observer);
    ui=new UI(this);
    renderer=new Renderer(this);
    exporter=new Exporter(this);}
  
  /*
   * ################################
   * FUZZBALLSYSTEM
   * ################################
   */
  
  FuzzballSystem fuzzballsystem;
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  UI ui;
  
  /*
   * ################################
   * RENDERER
   * ################################
   */
 
  Renderer renderer;
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  FuzzballSystemObserver observer=new FuzzballSystemObserver(){
    public void advanced(){
      renderer.render();
      ui.repaint();
      //rendersoundframe
      exporter.exportVideoFrame();
      }};
      
  /*
   * ################################
   * EXPORT
   * ################################
   */
      
  static final String EXPORTDIR="/home/john/Desktop/fuzzballexport";
  static final double EXPORTSCALE=7;
  static final AffineTransform EXPORTTRANSFORM=AffineTransform.getScaleInstance(EXPORTSCALE,EXPORTSCALE);
  public File exportdir=new File(EXPORTDIR);
  Exporter exporter;
  
  public BufferedImage getImageForExport(){
    BufferedImage a=new BufferedImage(
      (int)(renderer.image.getWidth()*EXPORTSCALE),
      (int)(renderer.image.getHeight()*EXPORTSCALE),
      BufferedImage.TYPE_INT_RGB);
    Graphics2D g=a.createGraphics();
    g.drawImage(renderer.image,EXPORTTRANSFORM,null);
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
    for(int i=0;i<FRAMECOUNT;i++){
      test.fuzzballsystem.advance();
//      System.out.println("FBS : "+test.fuzzballsystem);
     try{
//      Thread.sleep(20);
     }catch(Exception x){};}}
    
}
