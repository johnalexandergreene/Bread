package org.fleen.bread.app.longGarden;

import java.io.File;

public class LongGarden{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  LongGarden(String[] a){
    initParams(a);
    initUI();
    initChain();
    start();}
  
  /*
   * ################################
   * PARAMS
   * ################################
   */
  
  Config params;
  
  private void initParams(String[] a){
    params=new Config(a);}
  

  /*
   * ################################
   * UTIL
   * ################################
   */
  
  public File getLocalDirectory(){
    
  }
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    new LongGarden(a);}

}
