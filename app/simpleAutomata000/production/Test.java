package org.fleen.bread.app.simpleAutomata000.production;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import org.fleen.bread.app.simpleAutomata000.SA;;

public class Test{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test(){
    sa=new SA();
    sa.observers.add(observer);
    ui=new UI(this);
    renderer=new Renderer(this);
    exporter=new Exporter(this);}
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  SAObserver observer=new SAObserver(){
    public void advanced(){
      renderer.render();
      ui.repaint();
      exporter.export();}};
      
  /*
   * ################################
   * EXPORTDIR
   * ################################
   */
      
  static final String EXPORT="/home/john/Desktop/sa_export";
  
  public File exportdir=new File(EXPORT);
  
  /*
   * ################################
   * DST, UI, RENDERER AND EXPORTER
   * ################################
   */
  
  SA sa;
  UI ui; 
  Renderer renderer;
  Exporter exporter;
  
  static final int EXPORTIMAGESCALE=16;
  static final AffineTransform EXPORTIMAGETRANSFORM=
    AffineTransform.getScaleInstance(EXPORTIMAGESCALE,EXPORTIMAGESCALE);
  
  BufferedImage getExportImage(){
    BufferedImage a=new BufferedImage(
      renderer.image.getWidth()*EXPORTIMAGESCALE,
      renderer.image.getHeight()*EXPORTIMAGESCALE,
      BufferedImage.TYPE_INT_RGB);
    Graphics2D g=a.createGraphics();
    g.drawImage(renderer.image,EXPORTIMAGETRANSFORM,null);
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
  
  static final int DURATION=1200; 
  
  public static final void main(String[] a){
    Test test=new Test();
    for(int i=0;i<DURATION;i++){
      test.sa.advanceState();
      try{
        Thread.sleep(100);
      }catch(Exception x){};}}
    
}
