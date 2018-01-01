package org.fleen.bread.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.fleen.bread.colorMap.ColorMap;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;

public class R_SimpleStrokes implements Renderer{

  /*
   * ################################
   * CREATE IMAGE
   * ################################
   */
  
  public static final HashMap<RenderingHints.Key,Object> RENDERING_HINTS=
      new HashMap<RenderingHints.Key,Object>();
    
  static{
    RENDERING_HINTS.put(
      RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    RENDERING_HINTS.put(
      RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    RENDERING_HINTS.put(
      RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DEFAULT);
    RENDERING_HINTS.put(
      RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    RENDERING_HINTS.put(
      RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    RENDERING_HINTS.put(
      RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY); 
    RENDERING_HINTS.put(
      RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);}
  
  public BufferedImage createImage(int width,int height,ForsythiaComposition composition,ColorMap colormap){
    BufferedImage i=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    AffineTransform t=getTransform(width,height,composition);
    Graphics2D g=i.createGraphics();
    g.setRenderingHints(RENDERING_HINTS);
    g.setTransform(t);
    //do fill
    for(FPolygon p:composition.getLeafPolygons()){
      g.setPaint(colormap.getColor(p));
      g.fill(p.getDPolygon().getPath2D());}
    //do stroke
    g.setPaint(strokecolor);
    g.setStroke(createStroke());
    for(FPolygon p:composition.getLeafPolygons())
      g.draw(p.getDPolygon().getPath2D());
    //
    return i;}
  
  /*
   * ################################
   * STROKE
   * ################################
   */
  
//  static final float STROKETHICKNESS_DEFAULT=0.014f;
  static final float STROKETHICKNESS_DEFAULT=0.018f;
//  static final float STROKETHICKNESS_DEFAULT=0.005f;
  static final Color STROKECOLOR_DEFAULT=Color.white;
  float strokethickness=STROKETHICKNESS_DEFAULT;
  Color strokecolor=STROKECOLOR_DEFAULT;
  
  public void setStrokeThickness(double a){
    strokethickness=(float)a;}
  
  public void setStrokeColor(Color a){
    strokecolor=a;}
  
  private Stroke createStroke(){
    Stroke stroke=new BasicStroke(strokethickness,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
    return stroke;}
  
  /*
   * ################################
   * TRANSFORM
   * scale and fit the composition to the image
   * ################################
   */
  
  private AffineTransform getTransform(int width,int height,ForsythiaComposition composition){
    //get all the relevant metrics
    Rectangle2D.Double compositionbounds=composition.getRootPolygon().getDPolygon().getBounds();
    double
      cbwidth=compositionbounds.getWidth(),
      cbheight=compositionbounds.getHeight(),
      cbxmin=compositionbounds.getMinX(),
      cbymin=compositionbounds.getMinY();
    //
    AffineTransform transform=new AffineTransform();
    //scale
    double
      scale,
      p0=cbwidth/cbheight,
      p1=width/height;
    if(p0>p1){
      scale=((double)width)/cbwidth;
    }else{
      scale=((double)height)/cbheight;}
    transform.scale(scale,-scale);//flip y for proper cartesian orientation
    //offset
    double
      xoff=((width/scale-cbwidth)/2.0)-cbxmin,
      yoff=-(((height/scale+cbheight)/2.0)+cbymin);
    transform.translate(xoff,yoff);
    //
    return transform;}

}
