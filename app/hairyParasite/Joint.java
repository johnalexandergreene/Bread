package org.fleen.bread.app.hairyParasite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.geom_2D.GD;

public class Joint{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Joint(Spine spine,int index){
    this.spine=spine;
    this.index=index;
    initLength();
    createDirectionDeltaDeltas();
    createLengthDeltas();}
  
  /*
   * ################################
   * STUFF
   * ################################
   */
  
  Spine spine;
  int index;
  Random rnd=new Random();
  
  /*
   * ################################
   * DIRECTION
   * ################################
   */
  
  static final int 
    DDLISTRANGEMIN=20,
    DDLISTRANGEMAX=100;
  static final double
    DDAMPLITUDERANGEMIN=0.002,
    DDAMPLITUDERANGEMAX=0.03,
    DDPERIODRANGEMIN=4,
    DDPERIODRANGEMAX=18;
  
  int directiondeltadeltaindex;
  
  List<Double> directiondeltadeltas;
  
  public double directiondelta;
  
  /*
   * a list of direction deltas to be summed with the direction of whatever this joint is attached to 
   *   -1 to 1 * constant
   * probably a mix of 3 sine functions
   * constrained somehow to keep it from getting too kinky
   */
  void createDirectionDeltaDeltas(){
    int ddlen=(int)(rnd.nextDouble()*(DDLISTRANGEMAX-DDLISTRANGEMIN)+DDLISTRANGEMIN);
    directiondeltadeltas=new ArrayList<Double>(ddlen);
    double 
      af0=getDDAmplitudeFactor(),
      af1=getDDAmplitudeFactor(),
      af2=getDDAmplitudeFactor(),
      pf0=getDDPeriodFactor(),
      pf1=getDDPeriodFactor(),
      pf2=getDDPeriodFactor();
    double a,d0,d1,d2,d;
    for(int i=0;i<ddlen;i++){
      a=((double)i)/((double)ddlen);
      d0=Math.sin(a*GD.PI2*pf0)*af0;
      d1=Math.sin(a*GD.PI2*pf1)*af1;
      d2=Math.sin(a*GD.PI2*pf2)*af2;
      d=d0+d1+d2;
      directiondeltadeltas.add(d);}}
  
  double getDDAmplitudeFactor(){
    double f=rnd.nextDouble()*(DDAMPLITUDERANGEMAX-DDAMPLITUDERANGEMIN)+DDAMPLITUDERANGEMIN;
    return f;}
  
  double getDDPeriodFactor(){
    double f=rnd.nextDouble()*(DDPERIODRANGEMAX-DDPERIODRANGEMIN)+DDPERIODRANGEMIN;
    return f;}
  
  /*
   * ################################
   * LENGTH
   * ################################
   */
  
  static final int 
    LDLISTRANGEMIN=100,
    LDLISTRANGEMAX=500;
  static final double
    LDAMPLITUDERANGEMIN=0.002,
    LDAMPLITUDERANGEMAX=0.05,
    LDPERIODRANGEMIN=3,
    LDPERIODRANGEMAX=12;
  
  public double length;
  
  void initLength(){
    //test
    length=0.5;
  }
  
  int lengthdeltaindex;
  
  List<Double> lengthdeltas;
  
  /*
   * a list of length deltas to be summed with the init length
   * probably a mix of 3 sine functions
   * constrained somehow to keep it from getting too weird
   */
  void createLengthDeltas(){
    int ldlen=(int)(rnd.nextDouble()*(LDLISTRANGEMAX-LDLISTRANGEMIN)+LDLISTRANGEMIN);
    lengthdeltas=new ArrayList<Double>(ldlen);
    double 
      af0=getLDAmplitudeFactor(),
      af1=getLDAmplitudeFactor(),
      af2=getLDAmplitudeFactor(),
      pf0=getLDPeriodFactor(),
      pf1=getLDPeriodFactor(),
      pf2=getLDPeriodFactor();
    double a,d0,d1,d2,d;
    for(int i=0;i<ldlen;i++){
      a=((double)i)/((double)ldlen);
      d0=Math.sin(a*GD.PI2*pf0)*af0;
      d1=Math.sin(a*GD.PI2*pf1)*af1;
      d2=Math.sin(a*GD.PI2*pf2)*af2;
      d=d0+d1+d2;
      lengthdeltas.add(d);}}
  
  double getLDAmplitudeFactor(){
    double f=rnd.nextDouble()*(LDAMPLITUDERANGEMAX-LDAMPLITUDERANGEMIN)+LDAMPLITUDERANGEMIN;
    return f;}
  
  double getLDPeriodFactor(){
    double f=rnd.nextDouble()*(LDPERIODRANGEMAX-LDPERIODRANGEMIN)+LDPERIODRANGEMIN;
    return f;}
  
  /*
   * ################################
   * INCREMENT
   * ################################
   */
  
  public void twitch(){
    if(directiondeltadeltaindex==directiondeltadeltas.size()){
      createDirectionDeltaDeltas();
      directiondeltadeltaindex=0;}
    directiondelta+=directiondeltadeltas.get(directiondeltadeltaindex);
    directiondeltadeltaindex++;
    //
    if(lengthdeltaindex==lengthdeltas.size()){
      createLengthDeltas();
      lengthdeltaindex=0;}
    length+=lengthdeltas.get(lengthdeltaindex);
    lengthdeltaindex++;}
  
}
