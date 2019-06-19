package org.fleen.bread.app.hairball.production;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.fleen.bread.app.dancingSquishyThing.DST;

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
  
  static final int[] COLORS={Color.red.getRGB(),Color.black.getRGB(),Color.yellow.getRGB()};
      
  void render(){
    int 
      w=DST.WIDTH,
      h=DST.HEIGHT;
    image=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        if(test.hairball.cellfield[x][y]==0){
          if(test.hairball.getCenterDist(x,y)<test.hairball.radius)
            image.setRGB(x,y,COLORS[0]);
          else
            image.setRGB(x,y,COLORS[2]);
        }else{
          image.setRGB(x,y,COLORS[1]); }}}}

}
