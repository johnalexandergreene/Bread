package org.fleen.bread.app.spray.videoRenderer;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.fleen.bread.app.spray.Test;

public class VideoRenderer{
  
  public VideoRenderer(Test test){
    this.test=test;
    createPalette();
//    palette=PALETTETEST;
    }
  
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
  
  Test test;
  
  public BufferedImage image;
      
  public void render(){
    int 
      w=(int)test.target.width,
      h=(int)test.target.height;
    image=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
    renderGrid(w,h);
    }
  
  void renderGrid(int w,int h){
    Color c;
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        int i=test.target.cells[x][y];
        c=getColor(i);
        image.setRGB(x,y,c.getRGB());}}}
  
  /*
   * ################################
   * PALETTE
   * ################################
   */
  
  static final Color[] PALETTETEST={
    new Color(255,0,0),
    new Color(255,255,0),
    new Color(0,255,0),
    new Color(0,255,255),
    new Color(0,0,255),
    new Color(255,0,255)};
    
  Color getColor(int i){
    Color a=palette[i%palette.length];
    return a;}
  
  Color[] palette;
  
  void createPalette(){
    System.out.println("create palette");
    //load it
    BufferedImage paletteimage=null;
    try{
      paletteimage=ImageIO.read(VideoRenderer.class.getResource("palette001.png"));
    }catch(Exception x){
      System.out.println("exception in get palette image");
      x.printStackTrace();}
    //get the colors out of it
    int w=paletteimage.getWidth();
    palette=new Color[w];
    for(int x=0;x<w;x++){
      palette[x]=new Color(paletteimage.getRGB(x,0));}
    System.out.println("got palette");}

}
