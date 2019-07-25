package org.fleen.bread.app.simpleAutomata000.production;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.fleen.bread.app.simpleAutomata000.SA;

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
  
  public static final Color[] P_TOY_STORY_ADJUSTED2=new Color[]{
    new Color(168,67,39),
    new Color(251,206,89),
    new Color(88,184,121),
    new Color(154,94,154),
    new Color(234,61,65),
    new Color(248,237,23),
    new Color(249,139,90),
    new Color(0,146,232),
    new Color(254,178,213)};
  
  static final Color[] COLORS=P_TOY_STORY_ADJUSTED2;
      
  void render(){
    int 
      w=SA.WIDTH,
      h=SA.HEIGHT;
    image=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    int c0,c1;
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        c0=test.sa.cellfield[x][y]%P_TOY_STORY_ADJUSTED2.length;
        if(c0<0)c0=0;
        c1=COLORS[c0].getRGB();
        image.setRGB(x,y,c1); }}}

}
