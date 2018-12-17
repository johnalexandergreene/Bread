package org.fleen.bread.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.fleen.forsythia.app.compositionGenerator.colorMap.ColorMap;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;

public class R_SimpleStrokes implements Renderer{
  
  /*
   * ################################
   * PARAMS
   * ################################
   */

  /*
   * ++++++++++++++++++++++++++++++++
   * COMPOSITION
   * ++++++++++++++++++++++++++++++++
   */
  
  private ForsythiaComposition composition=null;
  
  public void setComposition(ForsythiaComposition c){
    composition=c;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * COLOR MAP
   * ++++++++++++++++++++++++++++++++
   */
  
  private ColorMap colormap=null;
  
  public void setColorMap(ColorMap m){
    colormap=m;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * STROKE THICKNESS
   * ++++++++++++++++++++++++++++++++
   */
  
  static final float STROKETHICKNESS_DEFAULT=0.014f;
  
  float strokethickness=STROKETHICKNESS_DEFAULT;
  
  public void setStrokeThickness(double a){
    strokethickness=(float)a;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * STROKE COLOR
   * ++++++++++++++++++++++++++++++++
   */
  
  static final Color STROKECOLOR_DEFAULT=Color.white;
  
  Color strokecolor=STROKECOLOR_DEFAULT;
  
  public void setStrokeColor(Color a){
    strokecolor=a;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * BORDER THICKNESS
   * ++++++++++++++++++++++++++++++++
   */
  
  static final int BORDERTHICKNESS_DEFAULT=32;
  
  int borderthickness=BORDERTHICKNESS_DEFAULT;
  
  public void setBorderThickness(int a){
    borderthickness=a;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * BORDER COLOR
   * ++++++++++++++++++++++++++++++++
   */
  
  static final Color BORDERCOLOR_DEFAULT=Color.white;
  
  Color bordercolor=BORDERCOLOR_DEFAULT;
  
  public void setBorderColor(Color a){
    bordercolor=a;}
  
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
  
  public BufferedImage createImage(int width,int height){
    BufferedImage i=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    AffineTransform t=getTransform(width,height,composition);
    Graphics2D g=i.createGraphics();
    //fill background, this also does the border
    g.setPaint(bordercolor);
    g.fillRect(0,0,width,height);
    //
    g.setRenderingHints(RENDERING_HINTS);
    g.setTransform(t);
    //fill composition polygons
    for(FPolygon p:composition.getLeafPolygons()){
      g.setPaint(colormap.getColor(p));
      g.fill(p.getDPolygon().getPath2D());}
    //stroke compsoiton polygons
    g.setPaint(strokecolor);
    g.setStroke(
      new BasicStroke(strokethickness,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0));
    for(FPolygon p:composition.getLeafPolygons())
      g.draw(p.getDPolygon().getPath2D());
    //
    return i;}
  
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
    int 
      awidth=width-borderthickness*2,
      aheight=height-borderthickness*2;
    AffineTransform transform=new AffineTransform();
    //scale
    double
      scale,
      p0=cbwidth/cbheight,
      p1=awidth/aheight;
    if(p0>p1){
      scale=((double)awidth)/cbwidth;
    }else{
      scale=((double)aheight)/cbheight;}
    transform.scale(scale,-scale);//flip y for proper cartesian orientation
    //offset
    double
      xoff=((width/scale-cbwidth)/2.0)-cbxmin,
      yoff=-(((height/scale+cbheight)/2.0)+cbymin);
    transform.translate(xoff,yoff);
    //
    return transform;}

}
