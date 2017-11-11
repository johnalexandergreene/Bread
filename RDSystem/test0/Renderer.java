package org.fleen.bread.RDSystem.test0;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.fleen.bread.RDSystem.Cell;
import org.fleen.bread.RDSystem.Presence;
import org.fleen.bread.palette.Palette;
import org.fleen.geom_2D.DPolygon;

/*
 * convert each cell to a pixel
 * render to image
 * scale image to fit viewer
 */
public class Renderer{
  
  Test0 test;
  
  public Renderer(Test0 test){
    this.test=test;}
  
  /*
   * render rds to image, cells to pixels
   * then scale to whatever and return that.
   */
  public void render(){
    //TODO we should have a scale param here, and a final transform, 
    //then scale up the rendered image to a bigger image or whatever to fit the viewer
    int 
      w=test.rds.getWidth(),
      h=test.rds.getHeight();
    //
    BufferedImage image0=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    for(Cell c:test.rds){
      image0.setRGB(c.x,c.y,getColor(c).getRGB());}
    //scale to center and fit in viewer
    int
      imagewidth=image0.getWidth(),
      imageheight=image0.getHeight(),
      viewerwidth=test.ui.viewer.getWidth(),
      viewerheight=test.ui.viewer.getHeight();
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
    g.drawImage(image0,t,null);
    
    test.image=image1;}
  
  /*
   * ################################
   * CELL COLOR LOGIC
   * ################################
   */
  
  private Color getColor(Cell c){
    //get intensity sum
    double intensitysum=0;
    for(Presence p:c.presences)
      intensitysum+=p.intensity;
    //get normalized intensity for each presence and sum weighted r g b color components
    int r=0,g=0,b=0;
    Color color;
    double normalized;
    for(Presence p:c.presences){
      normalized=p.intensity/intensitysum;
      color=getPolygonColor(p.polygon);
      r+=(int)(color.getRed()*normalized);
      g+=(int)(color.getGreen()*normalized);
      b+=(int)(color.getBlue()*normalized);}
    //
    return new Color(r,g,b);}
  
  int colorindex=0;
  
  Map<DPolygon,Color> colorbypolygon=new HashMap<DPolygon,Color>();
  
  private Color getPolygonColor(DPolygon p){
    Color c=colorbypolygon.get(p);
    if(c==null){
      c=Palette.P_TOY_STORY_ADJUSTED2[colorindex%Palette.P_TOY_STORY_ADJUSTED2.length];
      colorindex++;
      colorbypolygon.put(p,c);}
    return c;}

}
