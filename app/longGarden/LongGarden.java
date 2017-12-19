package org.fleen.bread.app.longGarden;

import java.io.File;
import java.net.URLDecoder;

import org.fleen.bread.app.longGarden.config.Config;
import org.fleen.bread.app.longGarden.frameGenerator.FrameGenerator;
import org.fleen.bread.app.longGarden.stripeChainGenerator.StripeChainGenerator;
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
    ui=new UI(this);}
  
  /*
   * ################################
   * LOOP
   * ################################
   */
  
  private static final long STOP_TEST_PERIOD=3000;
  public StripeChainGenerator stripechaingenerator=new StripeChainGenerator(this);
  public FrameGenerator framegenerator=new FrameGenerator(this);
  private boolean run=true;
  
  private void start(){
    stripechaingenerator.start();
    framegenerator.start();
    while(run)
      try{
        Thread.sleep(STOP_TEST_PERIOD);
      }catch(Exception x){
        x.printStackTrace();}}
    
  public void stop(){
    framegenerator.stop();
    stripechaingenerator.stop();
    run=false;
    System.exit(0);}
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  public File getLocalDir(){
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
    g.start();}

}
