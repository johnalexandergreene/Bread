package org.fleen.bread.app.hairyParasite.production;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import org.fleen.bread.app.eggSacDespoiler.ESD;
import org.fleen.bread.app.hairyParasite.HP;


public class Test{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test(){
    hp=new HP();
    hp.observers.add(observer);
    ui=new UI(this);
    renderer=new Renderer(this);
    exporter=new Exporter(this);}
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  HPObserver observer=new HPObserver(){
    public void advanced(){
      renderer.render();
      ui.repaint();
//      exporter.export();
      }};
      
  /*
   * ################################
   * EXPORTDIR
   * ################################
   */
      
  static final String EXPORT="/home/john/Desktop/hp_export";
  
  public File exportdir=new File(EXPORT);
  
  /*
   * ################################
   * DST, UI, RENDERER AND EXPORTER
   * ################################
   */
  
  HP hp;
  UI ui; 
  Renderer renderer;
  Exporter exporter;
  
  static final int EXPORTIMAGESCALE=1;
  static final AffineTransform EXPORTIMAGETRANSFORM=
    AffineTransform.getScaleInstance(EXPORTIMAGESCALE,EXPORTIMAGESCALE);
  
  BufferedImage getExportImage(){
    return renderer.image;
//    BufferedImage a=new BufferedImage(
//      renderer.image.getWidth()*EXPORTIMAGESCALE,
//      renderer.image.getHeight()*EXPORTIMAGESCALE,
//      BufferedImage.TYPE_INT_RGB);
//    Graphics2D g=a.createGraphics();
//    g.drawImage(renderer.image,EXPORTIMAGETRANSFORM,null);
//    return a;
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
  
  public static final int DURATION=1200; 
  
  public static final void main(String[] a){
    Test test=new Test();
    for(int i=0;i<DURATION;i++){
      test.hp.advanceState();
      try{
        Thread.sleep(1000);
      }catch(Exception x){};}}
    
}
