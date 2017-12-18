package org.fleen.bread.app.longGarden;

import java.io.File;
import java.net.URLDecoder;

import org.fleen.forsythia.app.grammarEditor.GE;

public class LongGarden{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  LongGarden(String[] a){
    initConfig();
    initUI();
    start();}
  
  /*
   * ################################
   * PARAMS
   * ################################
   */
  
  Config config;
  
  private void initConfig(){
    config=new Config(this);}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  UI ui;
  
  private void initUI(){
    ui=new UI(this);}
  
  /*
   * ################################
   * START STOP
   * ################################
   */
  
  private void start(){
    
  }
  
  public void stop(){
    
  }
  
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
    new LongGarden(a);}

}
