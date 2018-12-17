package org.fleen.bread.renderer;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.fleen.bread.zCellSystem.ZCSMappedThingPresence;
import org.fleen.bread.zCellSystem.ZCell;
import org.fleen.bread.zCellSystem.ZCellSystem;
import org.fleen.forsythia.app.compositionGenerator.colorMap.ColorMap;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.ForsythiaComposition;

/*
 * map composition to zcellsystem
 * render zcellsystem in terms of colormap
 */
public class R_ZCell_DarkStrokes implements Renderer{

  static final double GLOWSPAN=1.5;
  
  /*
   * calculate scale and transform
   * init zcs
   * 
   */
  public BufferedImage createImage(
      int width,int height,
      ForsythiaComposition composition,
      ColorMap colormap){
    AffineTransform t=getTransform(width,height,composition);
    ZCellSystem zcs=getZCellSystem(width,height,composition,t);
    BufferedImage i=createImage(colormap,zcs);
    return i;}
  
  //scale and fit
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
  
  private ZCellSystem getZCellSystem(int width,int height,ForsythiaComposition composition,AffineTransform t){
    ZCellSystem zcs=new ZCellSystem(width,height);
    for(FPolygon p:composition.getLeafPolygons())
      zcs.mapPolygonArea(p.getDPolygon(),t,GLOWSPAN);
    zcs.clean();
    return zcs;}
  
  private BufferedImage createImage(ColorMap colormap,ZCellSystem zcs){
    int w=zcs.getWidth(),h=zcs.getHeight();
    BufferedImage i=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    ZCell c;
    Color d;
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        c=zcs.getCell(x,y);
        if(c!=null){
          d=getColor(c,colormap);
          i.setRGB(x,y,d.getRGB());}}}
    return i;}
  
  static final double INTENSITYLIMIT=0.8;
  
  private Color getColor(ZCell c,ColorMap colormap){
    int r=0,g=0,b=0;
    Color color;
    for(ZCSMappedThingPresence p:c.presences){
      color=colormap.getColor((FPolygon)p.polygon.object);//so ya, we put the fpolygon in the dpolygon's gp object when we created that dpolygon
      r+=(int)(color.getRed()*p.intensity);
      g+=(int)(color.getGreen()*p.intensity);
      b+=(int)(color.getBlue()*p.intensity);}
    //yes
    if(r<0)r=0;
    if(r>255)r=255;
    if(g<0)g=0;
    if(g>255)g=255;
    if(b<0)b=0;
    if(b>255)b=255;
    //
    return new Color(r,g,b);}
  
}
