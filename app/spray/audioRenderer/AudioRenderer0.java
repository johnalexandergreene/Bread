package org.fleen.bread.app.spray.audioRenderer;

import java.util.ArrayList;
import java.util.List;

import org.fleen.bread.app.spray.Target;

/*
 * sample all cells
 * use this to derive sound
 * 
 * get sound for each cell
 *   delta is frequency
 *   closeness to whelmer center-ring is volume
 * average them?
 *   but that would attenuate stuff
 *   maybe we should do weighted average
 *   
 */
public class AudioRenderer0 implements AudioRenderer{
  
  static final double 
    BASEFREQUENCYFACTOR=700,
    DELTAEXPONENT=1.0,//1.2,//1.6,
    CELLDELTACAP=19.0,//17.0//14.0
    STROBEDRONEFILTER=-1.0;//this gets rid of the strobe's influence upon the sound. We don't want it.
  
  public int[] renderAudioFrame(Target target){
    //get metrics
    int soundpiecelength=target.getAudioSampleRatePerFrame();
    //get cell sounds
    //sample cells in strip extending from center to edge of cell array
    double[] samplestrip=getSampleStrip(whelmer);
    List<int[]> cellsounds=new ArrayList<int[]>();
    for(double c:samplestrip)
      cellsounds.add(getCellSound(c,soundpiecelength,whelmer));
    //get averaged cell sounds
    int[] totalsound=new int[soundpiecelength];
    int a;
    for(int i=0;i<soundpiecelength;i++){
      a=0;
      for(int[] cellsound:cellsounds)
        a+=cellsound[i];
      //
      a/=cellsounds.size();
      totalsound[i]=a;}
    //
    return totalsound;}
  
  double[] getSampleStrip(Whelmer whelmer){
    int
      s=whelmer.getCellArraySpan(),
      x=s/2,
      y=x;
    double[] samplestrip=new double[x];
    for(int i=0;i<x;i++)
      samplestrip[i]=whelmer.cells[x+i][y];
    //
    return samplestrip;}
  
  //apparently 32768 is too high. It makes a harsh clipping. so this works fine.
  static final double MAXAMPLITUDE=28000;
  
  /*
   * make a hunk of sine wave
   * start and end at 0 for pop reduction
   * remember, the length of this piece of sound is 1/60 of a second, so even a single sine wave is gonna make a nice tone
   */
  int[] getCellSound(double celldistance,int soundpiecelength,Whelmer whelmer){
    int[] sound=new int[soundpiecelength];
    double a;
    for(int i=0;i<soundpiecelength;i++){
      a=((double)i)/((double)soundpiecelength);
      a=a*Math.PI*2*getFrequencyFactor(celldistance,whelmer);
      sound[i]=(int)(Math.sin(a)*MAXAMPLITUDE*getVolumeFactor(celldistance));}
    return sound;}
    
  private double getFrequencyFactor(double celldistance,Whelmer whelmer){
    double d=whelmer.projector.getDelta(celldistance)+getStrobeSoundFilter(whelmer);
    d=((d%CELLDELTACAP)/CELLDELTACAP)*BASEFREQUENCYFACTOR;
    d=Math.pow(d,DELTAEXPONENT);
    return d;}
  
  private double getStrobeSoundFilter(Whelmer whelmer){
    return whelmer.projector.getStrobe()*STROBEDRONEFILTER;}
  
  /*
   * volume=closeness to whelmer center ring radius 0.5
   */
  private double getVolumeFactor(double celldistance){
    if(celldistance<0||celldistance>1.0)return 0;
    double closeness=(0.5-(Math.abs(0.5-celldistance)))*2;
    return closeness;}
  
}
