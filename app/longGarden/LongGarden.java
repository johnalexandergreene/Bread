package org.fleen.bread.app.longGarden;

import java.awt.EventQueue;
import java.io.File;
import java.net.URLDecoder;

import org.fleen.bread.app.longGarden.config.Config;
import org.fleen.bread.app.longGarden.frameGenerator.FrameGenerator;
import org.fleen.bread.app.longGarden.stripeChainGenerator.StripeChainGenerator;
import org.fleen.bread.app.longGarden.ui.UI;
import org.fleen.forsythia.app.grammarEditor.GE;
import org.fleen.forsythia.app.grammarEditor.UIMain;

public class LongGarden{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  LongGarden(){
    initConfig();
    initUI(this);}
  
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
  
//  private void initUI(){
//    ui=new UI(this);}
  
  private void initUI(final LongGarden thislg){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          ui=new UI(thislg);
        }catch(Exception x){
          x.printStackTrace();}}});}
  
  /*
   * ################################
   * LOOP
   * ################################
   */
  
  static final long RUN_TEST_PERIOD=3000;
  public StripeChainGenerator stripechaingenerator=new StripeChainGenerator(this);
  public FrameGenerator framegenerator=new FrameGenerator(this);
  public boolean run=false;
  
  public void start(){
    System.out.println("long garden start");
    run=true;
    stripechaingenerator.start();
    framegenerator.start();}
    
  public void stop(){
    System.out.println("long garden stop");
    run=false;
    framegenerator.stop();
    stripechaingenerator.stop();}
  
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
   * ################################
   */
  
  public static final void main(String[] a){
    LongGarden g=new LongGarden();
//    g.start();
    }

}
