package org.fleen.bread.app.hairball.production;

import java.io.File;

import org.fleen.bread.app.hairball.Hairball;


public class Test{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test(){
    hairball=new Hairball();
    hairball.observers.add(observer);
    ui=new UI(this);
    renderer=new Renderer(this);
    exporter=new Exporter(this);}
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  HairballObserver observer=new HairballObserver(){
    public void advanced(){
      renderer.render();
      ui.repaint();
      exporter.export();}};
      
  /*
   * ################################
   * EXPORTDIR
   * ################################
   */
      
  static final String EXPORT="/home/john/Desktop/hairball_export";
  
  public File exportdir=new File(EXPORT);
  
  /*
   * ################################
   * DST, UI, RENDERER AND EXPORTER
   * ################################
   */
  
  Hairball hairball;
  UI ui; 
  Renderer renderer;
  Exporter exporter;
  
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
    for(int i=0;i<300;i++){
      test.hairball.advanceState();
     try{
      Thread.sleep(10);
     }catch(Exception x){};}}
    
}
