package org.fleen.bread.app.buzzingCircles;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class Test{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test(){
    rmodel=new RModel(SPAN,SPAN);
    rmodel.observers.add(observer);
    ui=new UI(this);
    renderer=new Renderer(this);
    exporter=new Exporter(this);}
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  RModelObserver observer=new RModelObserver(){
    public void advanced(){
      renderer.render();
      ui.repaint();
      exporter.export();
      }};
      
  /*
   * ################################
   * EXPORT
   * ################################
   */
      
  static final String EXPORTDIR="/home/john/Desktop/rmodel_export";
  static final double EXPORTSCALE=1.6;
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
   * ################################
   * RMODEL, UI, RENDERER AND EXPORTER
   * ################################
   */
  
  RModel rmodel;
  UI ui; 
  Renderer renderer;
  
  /*
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * MAIN
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   */
  
  public static final int SPAN=400,FRAMECOUNT=1440;
  
  public static final void main(String[] a){
    Test test=new Test();
    for(int i=0;i<FRAMECOUNT;i++){
      test.rmodel.advanceState();
      System.out.println("RMODEL : "+test.rmodel);
     try{
      Thread.sleep(20);
     }catch(Exception x){};}}
    
}
