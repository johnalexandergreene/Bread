package org.fleen.bread.app.hairyParasite;

import java.util.ArrayList;
import java.util.List;

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
    init();}
  
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
  
  List<HairJoint> joints;
  
  void init(){
    joints=new ArrayList<HairJoint>();
    joints.add(new HairJoint(0.2));
    joints.add(new HairJoint(0.2));
    joints.add(new HairJoint(0.2));
    joints.add(new HairJoint(0.2));
    joints.add(new HairJoint(0.2));
    
  }
  
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
    for(HairJoint j:joints)
      j.twitch();
  }
  


}
