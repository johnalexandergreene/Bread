package org.fleen.bread.app.longGarden;

import java.awt.EventQueue;
import java.io.File;
import java.net.URLDecoder;

import org.fleen.bread.app.longGarden.config.Config;
import org.fleen.bread.app.longGarden.frameGenerator.FrameGenerator;
import org.fleen.bread.app.longGarden.ui.UI;
import org.fleen.forsythia.app.grammarEditor.GE;

public class LongGarden{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  LongGarden(){
    initConfig();
    initUI();}
  
  /*
   * ################################
   * PARAMS
   * ################################
   */
  
  public Config config;
  
  private void initConfig(){
    config=new Config(this);}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  public UI ui;
  
  private void initUI(){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          ui=new UI(LongGarden.this);
        }catch(Exception x){
          x.printStackTrace();}}});}
  
  /*
   * ################################
   * LOOP
   * ################################
   */
  
  public FrameGenerator framegenerator=new FrameGenerator(this);
  public boolean run=false;
  
  public void start(){
    System.out.println("long garden start");
    run=true;
    framegenerator.start();}
    
  public void stop(){
    System.out.println("long garden stop");
    run=false;
    framegenerator.stop();}
  
  /*
   * ################################
   * EXIT
   * ################################
   */
  
  public void exit(){
    if(run)stop();
    System.out.println("LONG GARDEN EXIT");
    System.exit(0);}
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  public File getWorkingDir(){
    String path=GE.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    String decodedpath;
    try{
      decodedpath=URLDecoder.decode(path,"UTF-8");
    }catch(Exception x){
      throw new IllegalArgumentException(x);}
    File f=new File(decodedpath);
    if(!f.isDirectory())f=f.getParentFile();
    return f;}
  
  /*
   * ################################
   * MAIN
   * fullscreen and start
   * ################################
   */
  
  public static final void main(String[] a){
    LongGarden g=new LongGarden();
//    g.start();
    }

}
