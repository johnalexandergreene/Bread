package org.fleen.bread.app.radiantWavyThrobber.production;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import org.fleen.bread.app.radiantWavyThrobber.Emitter;
import org.fleen.bread.app.radiantWavyThrobber.RWT;
import org.fleen.bread.app.radiantWavyThrobber.Ring;

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
  
  void render(){
    int 
      w=(int)RWT.WIDTH,
      h=(int)RWT.HEIGHT;
    image=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    Graphics2D g=image.createGraphics();
    g.setRenderingHints(RENDERING_HINTS);
    g.setPaint(Color.white);
    g.fillRect(0,0,w,h);
    g.setPaint(Color.black);
    g.setStroke(new BasicStroke(2.0f));
    for(Emitter e:test.rwt.emitters){
      render(g,e);}}

  void render(Graphics2D g,Emitter e){
    List<Ring> r=e.getRings();
    for(Ring a:r)
      g.draw(a.getPath());}
  

}
