package org.fleen.bread.app.communityExhibition2019_11_27;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.fleen.geom_2D.DPoint;

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
      w=(int)test.rmodel.width,
      h=(int)test.rmodel.height;
    image=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
    Graphics2D g=image.createGraphics();
    g.setRenderingHints(RENDERING_HINTS);
    g.setPaint(Color.white);
    g.fillRect(0,0,w,h);
    g.setPaint(Color.orange);
    g.setStroke(new BasicStroke(3.0f));
    for(PCircle p:test.rmodel.circles){
//      renderCircleImage(g,p);
      renderCircle(g,p);
      }}
  
  void renderCircle(Graphics2D g,PCircle p){
    Ellipse2D.Double e=getEllipse2D(p);
    g.draw(e);
  }
  
  void renderCircleImage(Graphics2D g,PCircle p){
    Ellipse2D.Double e=getEllipse2D(p);
    g.setClip(e);
    BufferedImage f=getFillImage(e);
    if(f!=null)
    g.drawImage(f,AffineTransform.getTranslateInstance(e.x,e.y),null);
    g.setClip(null);
    }
  
  BufferedImage getFillImage(Ellipse2D.Double e){
    int 
      w=(int)e.width,
      h=(int)e.height;
    if(w<1||h<1)return null;
    BufferedImage f=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        System.out.println("w="+w+" h="+h);
        f.setRGB(x,y,(x*y)%256);}}
    return f;}
  
  Ellipse2D.Double getEllipse2D(PCircle p){
    double r;
    Ellipse2D.Double e;
    DPoint center;
    r=p.getRadius();
    center=p.getCenter();
    e=new Ellipse2D.Double(center.x-r,center.y-r,r*2,r*2);
    return e;}

}
