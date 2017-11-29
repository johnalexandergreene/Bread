package org.fleen.bread.app.forsythiaSpinner;

public class FS{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  FS(String[] a){
    initParams(a);
    initUI();
    initChain();
    start();}
  
  /*
   * ################################
   * PARAMS
   * ################################
   */
  
  Params params;
  
  private void initParams(String[] a){
    params=new Params(a);}
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    new FS(a);}

}
