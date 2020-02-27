package org.fleen.bread.app.flerp0flerp2;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Renderer{
  
  Renderer(Test test){
    this.test=test;
    createPalette();}
  
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
  
  BufferedImage image;
      
  void render(){
    int 
      w=(int)test.fuzzballsystem.width,
      h=(int)test.fuzzballsystem.height;
    image=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
//    renderFuzzballEdges(w,h);
    renderGrid(w,h);
    }
  
  /*
   * ################################
   * RENDER GRID
   * ################################
   */
  
  void renderGrid(int w,int h){
    Color c;
    double v;
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        v=test.fuzzballsystem.grid[x][y];
        c=getColor(v);
        image.setRGB(x,y,c.getRGB());}}}
  
  /*
   * ################################
   * RENDER FUZZBALL EDGES
   * ################################
   */
  
//  void renderFuzzballEdges(int w,int h){
//    Graphics2D g=image.createGraphics();
//    g.setRenderingHints(RENDERING_HINTS);
//    g.setPaint(Color.white);
//    g.fillRect(0,0,w,h);
//    g.setPaint(Color.black);
//    g.setStroke(new BasicStroke(3.0f));
//    for(Fuzzball p:test.fuzzballsystem.fuzzballs)
//      renderFuzzball(g,p);}
//  
//  void renderFuzzball(Graphics2D g,Fuzzball p){
//    Ellipse2D.Double e=getEllipse2D(p);
//    g.draw(e);}
//  
//  Ellipse2D.Double getEllipse2D(Fuzzball p){
//    double r;
//    Ellipse2D.Double e;
//    r=p.radius;
//    e=new Ellipse2D.Double(p.x-r,p.y-r,r*2,r*2);
//    return e;}
  
  /*
   * ################################
   * PALETTE
   * ################################
   */
  
  static final Color[] TEST={
      new Color(255,0,0),
      new Color(255,255,0),
      new Color(0,255,0),
      new Color(0,255,255),
      new Color(0,0,255),
      new Color(255,0,255)};
    
  static final double COLORGETTERFACTOR=7.4;
    
  Color getColor(double v){
    int i=(int)(v*COLORGETTERFACTOR);
    Color a=palette[i%palette.length];
    return a;}
  
  Color[] palette;
  
  void createPalette(){
    System.out.println("create palette");
    //load it
    BufferedImage paletteimage=null;
    try{
      paletteimage=ImageIO.read(Renderer.class.getResource("palette012.png"));
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
