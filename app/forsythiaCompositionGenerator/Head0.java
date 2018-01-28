package org.fleen.bread.app.forsythiaCompositionGenerator;

import java.awt.EventQueue;

/*
 * runs a UI and composition generator
 * provides automation and bitmap export services 
 */
public class Head0{
  
  private static final String NAME="Fleen Bread 0.3";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Head0(){
    initUI();
    initExport();
    }
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  public UI_0 ui;
  
  protected void initUI(){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          ui=new UI_0(Head0.this);
          ui.setDefaultWindowBounds();
          ui.setVisible(true);
          ui.setTitle(NAME);
          ui.txtinterval.setText(String.valueOf(CREATION_INTERVAL_DEFAULT));
         }catch(Exception e){
           e.printStackTrace();}}});}
  
  /*
   * ################################
   * EXPORT
   * ################################
   */
  
  
  /*
   * ################################
   * GENERATOR
   * ################################
   */
  
  ForsythiaCompositionGenerator gen;
  
  public void setGen(ForsythiaCompositionGenerator g){
    gen=g;}
  
  public ForsythiaCompositionGenerator getGen(){
    return gen;}
  
  /*
   * ################################
   * CONTROL
   * ################################
   */
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    new Head0();}
  

}
