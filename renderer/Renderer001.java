package org.fleen.bread.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.forsythia.core.composition.FPolygonSignature;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.geom_2D.DPoint;
import org.fleen.util.tree.TreeNode;

public class Renderer001 extends Renderer_Abstract{
  
  /*
   * ################################
   * COLOR
   * ################################
   */
  
  private Color strokecolor;
  private static final Color[][] COLORS={
      {
        new Color(227,161,117),  
        new Color(227,117,117),
        new Color(227,117,161),
      },
      
      {
        new Color(182,227,117),  
        new Color(226,227,117),
        new Color(227,184,117),
        
      },
      
      {
        new Color(161,117,227),  
        new Color(117,117,227),
        new Color(117,161,227),
        
      }
        
    };
  
  private Map<FPolygonSignature,Color> polygoncolors=new Hashtable<FPolygonSignature,Color>();
  
  public Color getColor(FPolygon polygon){
    FPolygonSignature sig=polygon.getSignature();
    Color color=polygoncolors.get(sig);
    if(color==null){
      int 
        eggdepth=getTagDepth(polygon,"egg"),
        paletteindex=eggdepth%3,
        colorindex=rnd.nextInt(COLORS[paletteindex].length);
      color=COLORS[paletteindex][colorindex];}
    return color;}
  
  private void initColors(Color[] palette){
    strokecolor=Color.black;
//    int a=palette.length/2;
//    color0=new Color[a];
//    color1=new Color[palette.length-a];
//    for(int i=0;i<a;i++)
//      color0[i]=palette[i];
//    for(int i=a;i<palette.length;i++)
//      color1[i-a]=palette[i];
    
  
  }
  
  /*
   * ################################
   * STROKE
   * ################################
   */
  
  private float strokewidth;
  
  private Stroke createStroke(){
    Stroke stroke=new BasicStroke(strokewidth,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
    return stroke;}
  
  /*
   * ################################
   * RENDER
   * ################################
   */
  
  Random rnd=new Random();
  
  protected void render(ForsythiaComposition forsythia,Color[] palette,Graphics2D graphics,AffineTransform transform){
    
    strokewidth=0.004f;
    
    initColors(palette);
    Path2D path;
    //FILL POLYGONS
    Color color;
    for(FPolygon polygon:forsythia.getLeafPolygons()){
      color=getColor(polygon);
      graphics.setPaint(color);
      path=getPath(polygon);
      graphics.fill(path);}
    //STROKE POLYGONS
    graphics.setPaint(strokecolor);
    graphics.setStroke(createStroke());
    for(FPolygon polygon:forsythia.getLeafPolygons()){
      path=getPath(polygon);
      graphics.draw(path);}
    //
    polygoncolors.clear();}
  
  private Path2D getPath(FPolygon polygon){
    Path2D.Double path=new Path2D.Double();
    List<DPoint> points=polygon.getDPolygon();
    DPoint p=points.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<points.size();i++){
      p=points.get(i);
      path.lineTo(p.x,p.y);}
    path.closePath();
    return path;}
  
  protected int getTagDepth(TreeNode node,String tag){
    int c=0;
    TreeNode n=node;
    FPolygon p;
    while(n!=null){
      if(n instanceof FPolygon){
        p=(FPolygon)n;
        if(p.hasTags(tag))
          c++;}
      n=n.getParent();}
    return c;}
  
}
