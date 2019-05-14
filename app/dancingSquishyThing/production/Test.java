package org.fleen.bread.app.dancingSquishyThing.production;

import java.io.File;

import org.fleen.bread.app.dancingSquishyThing.DST;

public class Test{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test(){
    dst=new DST();
    dst.observers.add(observer);
    ui=new UI(this);
    renderer=new Renderer(this);
    exporter=new Exporter(this);}
  
  /*
   * ################################
   * OBSERVER
   * ################################
   */
  
  DSTObserver observer=new DSTObserver(){
    public void advanced(){
      renderer.render();
      ui.repaint();
      exporter.export();}};
      
  /*
   * ################################
   * EXPORTDIR
   * ################################
   */
      
  static final String EXPORT="/home/john/Desktop/dst_export";
  
  public File exportdir=new File(EXPORT);
  
  /*
   * ################################
   * DST, UI, RENDERER AND EXPORTER
   * ################################
   */
  
  DST dst;
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
    for(int i=0;i<100;i++){
      test.dst.advanceState();
//      System.out.println("DANCING SQUISHY THING : "+test.dst);
     try{
      Thread.sleep(100);
     }catch(Exception x){};}}
    
}
