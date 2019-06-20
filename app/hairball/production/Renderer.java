package org.fleen.bread.app.hairball.production;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;

import org.fleen.bread.app.hairball.Cell;


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
  static final int MARGIN=22;
  
  void render(){
    double[] bounds=getHairballBounds();
    int 
      w=(int)(bounds[1]-bounds[0])+MARGIN*2,
      h=(int)(bounds[3]-bounds[2])+MARGIN*2;
    //
    image=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    Graphics2D g=image.createGraphics();
    g.setPaint(Color.white);
    g.fillRect(0,0,w,h);
    g.setRenderingHints(RENDERING_HINTS);
    g.setTransform(AffineTransform.getTranslateInstance(MARGIN-bounds[0],MARGIN-bounds[2]));
    //
    Iterator<Cell> i=test.hairball.getCellIterator();
    Cell c;
    Path2D path=new Path2D.Double();
    c=i.next();
    path.moveTo(c.x,c.y);
    while(i.hasNext()){
      c=i.next();
      path.lineTo(c.x,c.y);}
    path.closePath();
    g.setStroke(new BasicStroke(2.0f));
    g.setPaint(Color.black);
    g.draw(path);
  
  }
  
  private double[] getHairballBounds(){
    double xmin=Double.MAX_VALUE,xmax=Double.MIN_VALUE,ymin=xmin,ymax=xmax;
    Iterator<Cell> i=test.hairball.getCellIterator();
    Cell c;
    while(i.hasNext()){
      c=i.next();
      if(c.x<xmin)xmin=c.x;
      if(c.x>xmax)xmax=c.x;
      if(c.y<ymin)ymin=c.y;
      if(c.y>ymax)ymax=c.y;}
    return new double[]{xmin,xmax,ymin,ymax};}

}
