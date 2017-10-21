package org.fleen.bread.fSLAFG.stripeChain;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.fleen.bread.fSLAFG.Generator;
import org.fleen.bread.fSLAFG.stripeChain.stripe.Stripe;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.util.tree.TreeNode;

/*
 * A chain of stripe nodes
 */
@SuppressWarnings("serial")
public class StripeChain extends LinkedList<Stripe>{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public StripeChain(Generator generator){
    this.generator=generator;}
  
  public StripeChain(Generator generator,List<Stripe> stripes){
    this(generator);
    addAll(stripes);}
  
  /*
   * ################################
   * GENERATOR
   * The fleen spinner looping animation frames generator
   *   associated with this stripechain
   * ################################
   */
  
  public Generator generator;
  
  /*
   * ################################
   * CHAIN MODIFICATION
   * invalidate image when we do this
   * ################################
   */
  
  public void createStripeAtEnd(){
    image=null;
    add(new Stripe(this));}
  
  public void removeStripeAtStart(){
    removeFirst();
    image=null;}
  
  /*
   * We use this when we've come around the loop and merge our present chain with the terminus chain
   */
  public void mergeChainAtStart(StripeChain chain){
    image=null;
    
  }
  
  /*
   * split off an end section of this chain to create a new chain
   * we use this when the present chain moves beyond the terminus chain at the beginning of the loop
   */
  public StripeChain splitChainAtEnd(Stripe stripe){
    image=null;
    return null;
  }
  
  /*
   * ################################
   * IMAGE
   * Render all of the stripes end-to-end as a continuous strip image
   * For each stripe composition
   *   
   * 
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
  
  BufferedImage image=null;
  AffineTransform[] stripeimagetransforms;
  double imagewidth;//the image is imagewidth X viewportheight
  
  public int getImageWidth(){
    if(image==null)
      createImage();
    return (int)imagewidth;}
  
  public BufferedImage getImage(){
    if(image==null)
      createImage();
    return image;}
  
  private void createImage(){
    initImageAndTransforms();
    if(Generator.TEST)createTestImage();
    Graphics2D g=image.createGraphics();
    g.setPaint(Color.white);
    g.fillRect(0,0,image.getWidth(),image.getHeight());
    g.setRenderingHints(RENDERING_HINTS);
    //render the image
    for(int i=0;i<size();i++)
      renderFill(g,i);
    for(int i=0;i<size();i++)
      renderStroke(g,i);}
  
  private void renderFill(Graphics2D g,int stripeindex){
    Stripe stripe=get(stripeindex);
    //
    AffineTransform 
      told=g.getTransform(),
      t=new AffineTransform(told);
    t.concatenate(stripeimagetransforms[stripeindex]);
    g.setTransform(t);
    //
    Iterator<TreeNode> i=stripe.composition.getLeafPolygonIterator();
    FPolygon p;
    Color color;
    while(i.hasNext()){
      p=(FPolygon)i.next();
      color=stripe.colormap.get(p);
      g.setPaint(color);
      g.fill(p.getDPolygon().getPath2D());}
    g.setTransform(told);}
  
  private void renderStroke(Graphics2D g,int stripeindex){
    Stripe stripe=get(stripeindex);
    System.out.println("render stroke for stripe composition : "+stripe.composition);
    //
    AffineTransform 
      told=g.getTransform(),
      t=new AffineTransform(told);
    t.concatenate(stripeimagetransforms[stripeindex]);
    g.setTransform(t);
    //
    Iterator<TreeNode> i=stripe.composition.getLeafPolygonIterator();
    FPolygon p;
    g.setPaint(Color.black);
    g.setStroke(createStroke((float)(2.0/stripeimagetransforms[stripeindex].getScaleX())));
    while(i.hasNext()){
      p=(FPolygon)i.next();
      g.draw(p.getDPolygon().getPath2D());}
    g.setTransform(told);}
  
  private Stroke createStroke(float strokewidth){
    Stroke stroke=new BasicStroke(strokewidth,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
    return stroke;}
  
  private void initImageAndTransforms(){
    int s=size();
    stripeimagetransforms=new AffineTransform[s];
    double stripeimageoffset;
    imagewidth=0;
    Stripe stripe;
    for(int i=0;i<s;i++){
      stripeimageoffset=imagewidth;
      stripe=get(i);
      stripeimagetransforms[i]=getStripeImageTransform(get(i),stripeimageoffset);
      imagewidth+=getScaledWidth(stripe);}
    image=new BufferedImage((int)imagewidth,generator.viewportheight,BufferedImage.TYPE_INT_RGB);}
  
  private double getScaledWidth(Stripe stripe){
    double
      s=getImageScale(stripe),
      w=stripe.composition.getRootPolygon().getDPolygon().getBounds().width,
      sw=s*w;
    return sw;}
  
  private double getImageScale(Stripe stripe){
    double ch=stripe.composition.getRootPolygon().getDPolygon().getBounds().height;
    double s=generator.viewportheight/ch;
    return s;}
  
  private AffineTransform getStripeImageTransform(Stripe stripe,double stripeimageoffset){
    //get all the relevant metrics
    Rectangle2D.Double compositionbounds=stripe.composition.getRootPolygon().getDPolygon().getBounds();
    double
      cbwidth=compositionbounds.getWidth(),
      cbheight=compositionbounds.getHeight(),
      cbxmin=compositionbounds.getMinX(),
      cbymin=compositionbounds.getMinY();
    AffineTransform transform=new AffineTransform();
    //scale
    double scale=getImageScale(stripe);
    transform.scale(scale,-scale);//flip y for proper cartesian orientation
    //offset
    double
      xoff=((getScaledWidth(stripe)/scale-cbwidth)/2.0)-cbxmin,
      yoff=-(((generator.viewportheight/scale+cbheight)/2.0)+cbymin);
    transform.translate(xoff+stripeimageoffset/scale,yoff);
    //
    return transform;}
  
  /*
   * ################################
   * IMAGE STUFF FOR TEST
   * ################################
   */
  
  public BufferedImage testimage;
  
  private void createTestImage(){
    testimage=new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
    Graphics2D g=testimage.createGraphics();
    g.setPaint(Color.white);
    g.fillRect(0,0,image.getWidth(),image.getHeight());
    g.setRenderingHints(RENDERING_HINTS);
    for(int i=0;i<size();i++)
      renderStroke(g,i);
    for(int i=0;i<size();i++)
      renderBorder(g,i);}
  
  //highlight the borders of each stripe rectangle
  private void renderBorder(Graphics2D g,int stripeindex){
    Stripe stripe=get(stripeindex);
    //
    AffineTransform 
      told=g.getTransform(),
      t=new AffineTransform(told);
    t.concatenate(stripeimagetransforms[stripeindex]);
    g.setTransform(t);
    //
    g.setPaint(Color.red);
    g.setStroke(createStroke((float)(8.0/stripeimagetransforms[stripeindex].getScaleX())));
    g.draw(stripe.composition.getRootPolygon().getDPolygon().getPath2D());
    g.setTransform(told);}
  
}
