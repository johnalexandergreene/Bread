package org.fleen.bread.app.hairyParasite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.geom_2D.GD;

public class HairJoint{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public HairJoint(double length){
    this.length=length;
    createDirectionDeltaDeltas();
    createLengthDeltas();}
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
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
    DDAMPLITUDERANGEMIN=0.05,
    DDAMPLITUDERANGEMAX=0.1,
    DDPERIODRANGEMIN=2,
    DDPERIODRANGEMAX=6,
    DDNOISEPROBABILITY=0.3;
  
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
    //first we do 4 random waves : sine, cosine, invsine and inversecosine
    //scaled with our factors 
    //and combine them
    //
    //first do the factors for amplitude and period
    double 
      af0=getDDAmplitudeFactor(),
      af1=getDDAmplitudeFactor(),
      af2=getDDAmplitudeFactor(),
      af3=getDDAmplitudeFactor(),
      pf0=getDDPeriodFactor(),
      pf1=getDDPeriodFactor(),
      pf2=getDDPeriodFactor(),
      pf3=getDDPeriodFactor();
    double a,d0,d1,d2,d3,d4,d;
    for(int i=0;i<ddlen;i++){
      a=((double)i)/((double)ddlen);
      d0=Math.sin(a*GD.PI2*pf0)*af0;
      d1=-Math.sin(a*GD.PI2*pf1)*af1;
      d2=Math.cos(a*GD.PI2*pf2)*af2;
      d3=-Math.cos(a*GD.PI2*pf3)*af3;
      d4=getDDNoise();
      d=d0+d1+d2+d3+d4;
      directiondeltadeltas.add(d);}}
  
  double getDDAmplitudeFactor(){
    double f=rnd.nextDouble()*(DDAMPLITUDERANGEMAX-DDAMPLITUDERANGEMIN)+DDAMPLITUDERANGEMIN;
    return f;}
  
  double getDDPeriodFactor(){
    double f=rnd.nextDouble()*(DDPERIODRANGEMAX-DDPERIODRANGEMIN)+DDPERIODRANGEMIN;
    return f;}
  
  double getDDNoise(){
    double n=0;
    if(rnd.nextDouble()<DDNOISEPROBABILITY){
      n=rnd.nextDouble()*(DDAMPLITUDERANGEMAX-DDAMPLITUDERANGEMIN)+DDAMPLITUDERANGEMIN;
      if(rnd.nextBoolean())
        n*=-1;}
    return n;}
  
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
      af3=getLDAmplitudeFactor(),
      pf0=getLDPeriodFactor(),
      pf1=getLDPeriodFactor(),
      pf2=getLDPeriodFactor(),
      pf3=getLDPeriodFactor();
    double a,d0,d1,d2,d3,d;
    for(int i=0;i<ldlen;i++){
      a=((double)i)/((double)ldlen);
      d0=Math.sin(a*GD.PI2*pf0)*af0;
      d1=-Math.sin(a*GD.PI2*pf1)*af1;
      d2=Math.cos(a*GD.PI2*pf2)*af2;
      d3=-Math.cos(a*GD.PI2*pf3)*af3;
      d=d0+d1+d2+d3;
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
//    if(lengthdeltaindex==lengthdeltas.size()){
//      createLengthDeltas();
//      lengthdeltaindex=0;}
//    length+=lengthdeltas.get(lengthdeltaindex);
//    lengthdeltaindex++;
    
  }
  
}
