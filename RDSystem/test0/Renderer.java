package org.fleen.bread.RDSystem.test0;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.fleen.bread.RDSystem.Cell;
import org.fleen.bread.RDSystem.Presence;
import org.fleen.geom_2D.DPolygon;

public class Renderer{
  
  Test0 test;
  
  public Renderer(Test0 test){
    this.test=test;}
  
  public void render(){
    BufferedImage image=new BufferedImage(test.imagewidth,test.imageheight,BufferedImage.TYPE_INT_RGB);
    for(Cell c:test.rds)
      image.setRGB(c.x,c.y,getColor(c).getRGB());
    test.image=image;}
  
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
  
  private Color getPolygonColor(DPolygon p){
    return null;
  }

}
