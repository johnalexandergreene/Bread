package org.fleen.bread.app.hairyParasite;

public class Hair{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Hair(double location,int type,boolean polarity){
    this.location=location;
    this.type=type;
    this.polarity=polarity;
    //init();
  }
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public static final int 
    TYPE_TRANSVERSE=0,
    TYPE_HEAD=1,
    TYPE_TAIL=2;
  double location;//distance from spine head. range 0,1
  int type;
  boolean polarity;//pertains to transverse only
  
  /*
   * ################################
   * TWITCH
   * ################################
   */
  
  /*
   * location is position on smoothed spine figure in terms of distance from head, range (0,1)
   * polarity is pointing leftish or rightish
   */
  public void twitch(){
    
  }
  


}
