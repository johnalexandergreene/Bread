package org.fleen.bread.app.kCellTest.test0;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import org.fleen.bread.app.hairyParasite.HP;
import org.fleen.bread.app.kCellTest.KCellSystem;


public class Test{
  
  public static final int WIDTH=700,HEIGHT=700;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test(){
    kcellsystem=new KCellSystem();
    kcellsystem.observer=observer;
    ui=new UI(this);
    renderer=new Renderer(this);
    exporter=new Exporter(this);}
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  KCellSystemObserver observer=new KCellSystemObserver(){
    public void advanced(){
      renderer.render();
      ui.repaint();
      exporter.export();
      }};
      
  /*
   * ################################
   * EXPORTDIR
   * ################################
   */
      
  static final String EXPORT="/home/john/Desktop/kct_export";
  
  public File exportdir=new File(EXPORT);
  
  /*
   * ################################
   * KCELLSYSTEM, UI, RENDERER AND EXPORTER
   * ################################
   */
  
  KCellSystem kcellsystem;
  UI ui; 
  Renderer renderer;
  Exporter exporter;
  
  static final double EXPORTIMAGESCALE=0.9;
  static final AffineTransform EXPORTIMAGETRANSFORM=
    AffineTransform.getScaleInstance(EXPORTIMAGESCALE,EXPORTIMAGESCALE);
  
  BufferedImage getExportImage(){
//    return renderer.image;
    BufferedImage a=new BufferedImage(
      (int)(renderer.image.getWidth()*EXPORTIMAGESCALE),
      (int)(renderer.image.getHeight()*EXPORTIMAGESCALE),
      BufferedImage.TYPE_INT_RGB);
    Graphics2D g=a.createGraphics();
    g.setRenderingHints(Renderer.RENDERING_HINTS);
    g.drawImage(renderer.image,EXPORTIMAGETRANSFORM,null);
    return a;
    }
  
  /*
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * MAIN
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
   */
  
  public static final int DURATION=77; 
  
  public static final void main(String[] a){
    Test test=new Test();
    for(int i=0;i<DURATION;i++){
      test.kcellsystem.advanceState();
      System.out.println("age = "+test.kcellsystem.age);
      try{
        Thread.sleep(100);
      }catch(Exception x){};}}
    
}
