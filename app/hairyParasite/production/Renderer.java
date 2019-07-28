package org.fleen.bread.app.hairyParasite.production;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.fleen.bread.app.hairyParasite.HP;

/*
 * keep centered!
 * maybe scale if necessary
 * image size is constant 
 * 
 * maybe some speckley noise too
 */
public class Renderer{
  
  public static final HashMap<RenderingHints.Key,Object> RENDERING_HINTS=
      new HashMap<RenderingHints.Key,Object>();
    
  static{
    RENDERING_HINTS.put(
      RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    RENDERING_HINTS.put(
      RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    RENDERING_HINTS.put(
      RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DISABLE);
    RENDERING_HINTS.put(
      RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    RENDERING_HINTS.put(
      RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    RENDERING_HINTS.put(
      RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY); 
    RENDERING_HINTS.put(
      RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);}
  
  Renderer(Test test){
    this.test=test;}
  
  Test test;
  BufferedImage image;
  static final double STROKE0=4;
  static final int PADDING=30;//pixels
  
  void render(){
    System.out.println("render");
    int 
      w=(int)HP.WIDTH,
      h=(int)HP.HEIGHT;
    image=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    Graphics2D g=image.createGraphics();
    g.setRenderingHints(RENDERING_HINTS);
    g.setPaint(Color.white);
    g.fillRect(0,0,w,h);
    AffineTransform t=getCenterAndFitTransform(w,h); 
    //
    g.setTransform(t);
    g.setPaint(Color.black);
    g.setStroke(new BasicStroke((float)(STROKE0/t.getScaleX())));
    //
    g.draw(test.hp.spine.getBasePath());;}
  
  AffineTransform getCenterAndFitTransform(double w,double h){
    w-=(PADDING*2);
    h-=(PADDING*2);
    doSpineBaseBounds();
    double 
      scalex=w/sbbw,//do a getbounds in the hp. account for hair I suppose
      scaley=h/sbbh,
      scale=Math.min(scalex,scaley);
    AffineTransform t=AffineTransform.getScaleInstance(scale,scale);
    double 
      tx=(((w+PADDING*2)/scale)-sbbw)/2-sbbxmin,
      ty=(((h+PADDING*2)/scale)-sbbh)/2-sbbymin;
    t.translate(tx,ty);
    return t;}
  
  //spine base bounds
  double sbbw,sbbh,sbbxmin,sbbxmax,sbbymin,sbbymax;
  
  void doSpineBaseBounds(){
    sbbxmin=Double.MAX_VALUE;
    sbbxmax=Double.MIN_VALUE;
    sbbymin=sbbxmin;
    sbbymax=sbbxmax;
    for(double[] a:test.hp.spine.base){
      if(a[0]<sbbxmin)sbbxmin=a[0];
      if(a[0]>sbbxmax)sbbxmax=a[0];
      if(a[1]<sbbymin)sbbymin=a[1];
      if(a[1]>sbbymax)sbbymax=a[1];}
    sbbw=sbbxmax-sbbxmin;
    sbbh=sbbymax-sbbymin;}
  
}
