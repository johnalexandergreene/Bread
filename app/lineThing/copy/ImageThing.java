package org.fleen.bread.app.lineThing.copy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.fleen.geom_2D.GD;

public class ImageThing{
  
  static int WIDTH=900,HEIGHT=900;
  static double SCALE=700;
  
  BufferedImage getImage(){
    BufferedImage a=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
    Graphics2D g=a.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    g.setPaint(new Color(122,122,122));
    g.fillRect(0,0,WIDTH,HEIGHT);
    //
    List<double[]> segs=getSegs();
    g.setTransform(getTransform());
    g.setStroke(new BasicStroke((float)(3.0/SCALE)));
    Path2D.Double p=new Path2D.Double(); 
    for(double[] s:segs){
      g.setPaint(getColor(s));
      p.reset();
      p.moveTo(s[0],s[1]);
      p.lineTo(s[2],s[3]);
      g.draw(p);}
    return a;}
  
  AffineTransform getTransform(){
    AffineTransform t=new AffineTransform();
    t.scale(SCALE,SCALE);
    t.translate(0.65,0.65);
    return t;}
  
  static final double MAXLENGTH=GD.SQRT2;
  
  Color getColor(double[] seg){
    double a=GD.getDistance_PointPoint(seg[0],seg[1],seg[2],seg[3]);
    float h=(float)(a/MAXLENGTH);
    Color b=Color.getHSBColor(h,1,1);
    b=new Color(b.getRed(),b.getGreen(),b.getBlue(),100);
    return b;}
  
  List<double[]> getSegs(){
    List<double[]> points=getPoints();
    List<double[]> segs=new ArrayList<double[]>();
    double[] p0,p1;
    for(int i0=0;i0<points.size();i0++){
      for(int i1=0;i1<points.size();i1++){
        if(i0!=i1){
          p0=points.get(i0);
          p1=points.get(i1);
          segs.add(new double[]{p0[0],p0[1],p1[0],p1[1]});}}}
    System.out.println("segcount="+segs.size());
    return segs;}
  
  List<double[]> getPoints(){
    int z=22;
    List<double[]> points=new ArrayList<double[]>();
    double [] a;
    double d;
    for(int i=0;i<z;i++){
      d=(((double)i)/((double)z))*GD.PI2;
      a=GD.getPoint_PointDirectionInterval(0,0,d,0.5);
      points.add(a);}
    System.out.println("pointcount="+points.size());
    return points;}
  

}
