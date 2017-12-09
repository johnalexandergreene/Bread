package org.fleen.bread.hCellSystem.test0;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.fleen.bread.hCellSystem.HCell;
import org.fleen.bread.hCellSystem.HCellSystem;
import org.fleen.bread.palette.Palette;

/*
 * convert each cell to a pixel
 * render to image
 * scale image to fit viewer
 */
public class Renderer{
  
  public static final HashMap<RenderingHints.Key,Object> RENDERING_HINTS=
      new HashMap<RenderingHints.Key,Object>();
    
  static{
    RENDERING_HINTS.put(
      RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
    RENDERING_HINTS.put(
      RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
    RENDERING_HINTS.put(
      RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DISABLE);
    RENDERING_HINTS.put(
      RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    RENDERING_HINTS.put(
      RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
    RENDERING_HINTS.put(
      RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED); 
    RENDERING_HINTS.put(
      RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);}
  
  HCellTest test;
  
  public Renderer(HCellTest test){
    this.test=test;}
  
  /*
   * render rds to image, cells to pixels
   * then scale to whatever and return that.
   */
  public void render(HCellSystem cs){
    //TODO we should have a scale param here, and a final transform, 
    //then scale up the rendered image to a bigger image or whatever to fit the viewer
    int 
      w=test.cellsystem0.getWidth(),
      h=test.cellsystem0.getHeight();
    //
    BufferedImage image0=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    for(HCell c:cs){
      image0.setRGB(c.x,c.y,getColor(c).getRGB());}
    //scale to center and fit in viewer
    int
      imagewidth=image0.getWidth(),
      imageheight=image0.getHeight(),
      viewerwidth=test.ui.viewer.getPaddedWidth(),
      viewerheight=test.ui.viewer.getPaddedHeight();
    double 
      imagedimsratio=((double)imagewidth)/((double)imageheight),
      viewerdimsratio=((double)viewerwidth)/((double)viewerheight),
      scale;
    //scale to width
    if(imagedimsratio>viewerdimsratio){
      scale=((double)viewerwidth)/((double)imagewidth);
    //scale to height  
    }else{
      scale=((double)viewerheight)/((double)imageheight);}
    BufferedImage image1=new BufferedImage(
      (int)(scale*imagewidth),
      (int)(scale*imageheight),
      BufferedImage.TYPE_INT_RGB);
    Graphics2D g=image1.createGraphics();
    AffineTransform t=AffineTransform.getScaleInstance(scale,scale);
    g.setRenderingHints(RENDERING_HINTS);
    g.drawImage(image0,t,null);
    //
    test.image=image1;}
  
  /*
   * ################################
   * CELL COLOR LOGIC
   * ################################
   */
  
  private Color getColor(HCell c){
    return getThingColor(c.thing);}
  
  Random rnd=new Random();
  int colorindex=0;
  
  Map<Object,Color> colorbything=new HashMap<Object,Color>();
  
  private Color getThingColor(Object a){
    Color c=colorbything.get(a);
    if(c==null){
      c=Palette.P_CRUDERAINBOW[colorindex%Palette.P_CRUDERAINBOW.length];
      colorindex+=1;
      colorbything.put(a,c);}
    return c;}

}