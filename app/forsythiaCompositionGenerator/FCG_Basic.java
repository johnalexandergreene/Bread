package org.fleen.bread.app.forsythiaCompositionGenerator;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.fleen.bread.colorMap.ColorMap;
import org.fleen.bread.colorMap.ColorMapper;
import org.fleen.bread.composer.Composer;
import org.fleen.forsythia.core.composition.ForsythiaComposition;

public abstract class FCG_Basic implements ForsythiaCompositionImageGenerator{

  /*
   * ################################
   * COMPOSITION
   * ################################
   */
  
  protected ForsythiaComposition composition=null;
  
  protected abstract Composer getComposer();

  public void regenerateComposition(){
    composition=getComposer().compose();}

  /*
   * ################################
   * COLOR MAP
   * ################################
   */
  
  protected ColorMap colormap=null;

  protected abstract ColorMapper getColorMapper();

  public void regenerateColorMap(){
    ColorMapper m=getColorMapper();
    m.setComposition(composition);
    colormap=m.getColorMap();}

  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  protected int imagewidth,imageheight,borderthickness;
  protected Color backgroundandborder;
  
  public abstract BufferedImage getImage();

  public int[] getImageDimensions(){
    return new int[]{imagewidth,imageheight};}
  
  public void setImageDimensions(int w,int h){
    imagewidth=w;
    imageheight=h;}

  public int getBorderThickness(){
    return borderthickness;}
  
  public void setBorderThickness(int t){
    borderthickness=t;}

  public Color getBackgroundAndBorderColor(){
    return backgroundandborder;}
  
  public void setBackgroundAndBorderColor(Color c){
    backgroundandborder=c;}
  
}
