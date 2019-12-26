package org.fleen.bread.app.kCellTest.test0;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.fleen.bread.app.hairyParasite.HP;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_Kisrhombille.KCell;
import org.fleen.geom_Kisrhombille.KPoint;

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
  static final double STROKE0=1;
  static final int PADDING=130;//pixels
  
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
    renderCells(g);}
  
  void renderCells(Graphics2D g){
    System.out.println("render cells");
    g.setPaint(Color.black);
    double s=g.getTransform().getScaleX();
    g.setStroke(new BasicStroke((float)(STROKE0/s)));
    for(KCell c:test.kcellsystem.cells){
      g.draw(getPath(c));}}
  
  Path2D getPath(KCell cell){
    Path2D path=new Path2D.Double();
    KPoint[] v=cell.getVertices();
    DPoint 
      p0=v[0].getBasicPoint2D(),
      p1=v[1].getBasicPoint2D(),
      p2=v[2].getBasicPoint2D();
    path.moveTo(p0.x,p0.y);
    path.lineTo(p1.x,p1.y);
    path.lineTo(p2.x,p2.y);
    path.closePath();
    return path;}
  
  AffineTransform getCenterAndFitTransform(double w,double h){
    w-=(PADDING*2);
    h-=(PADDING*2);
    doCellSystemBounds();
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
  
  void doCellSystemBounds(){
    sbbxmin=Double.MAX_VALUE;
    sbbxmax=Double.MIN_VALUE;
    sbbymin=sbbxmin;
    sbbymax=sbbxmax;
    KPoint[] v;
    DPoint p0,p1,p2;
    for(KCell cell:test.kcellsystem.cells){ 
      v=cell.getVertices();
      p0=v[0].getBasicPoint2D();
      p1=v[1].getBasicPoint2D();
      p2=v[2].getBasicPoint2D();
      //
      if(p0.x<sbbxmin)sbbxmin=p0.x;
      if(p0.x>sbbxmax)sbbxmax=p0.x;
      if(p0.y<sbbymin)sbbymin=p0.y;
      if(p0.y>sbbymax)sbbymax=p0.y;
      //
      if(p1.x<sbbxmin)sbbxmin=p1.x;
      if(p1.x>sbbxmax)sbbxmax=p1.x;
      if(p1.y<sbbymin)sbbymin=p1.y;
      if(p1.y>sbbymax)sbbymax=p1.y;
      //
      if(p2.x<sbbxmin)sbbxmin=p2.x;
      if(p2.x>sbbxmax)sbbxmax=p2.x;
      if(p2.y<sbbymin)sbbymin=p2.y;
      if(p2.y>sbbymax)sbbymax=p2.y;}
    sbbw=sbbxmax-sbbxmin;
    sbbh=sbbymax-sbbymin;}
  
}
