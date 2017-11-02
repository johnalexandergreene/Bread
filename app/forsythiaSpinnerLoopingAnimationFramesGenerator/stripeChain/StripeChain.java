package org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain;

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

import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.FSLAFGenerator;
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
  
  public StripeChain(FSLAFGenerator generator){
    this.generator=generator;}
  
  public StripeChain(FSLAFGenerator generator,List<Stripe> stripes){
    this(generator);
    addAll(stripes);}
  
  /*
   * ################################
   * GENERATOR
   * The fleen spinner looping animation frames generator
   *   associated with this stripechain
   * ################################
   */
  
  public FSLAFGenerator generator;
  
  /*
   * ################################
   * CHAIN MODIFICATION
   * invalidate image when we do this
   * ################################
   */
  
  public void addRandomForsythiaCompositionStripeToEnd(){
    image=null;
    Stripe s=new Stripe_ForsythiaComposition(this);
    generator.stripewidthsum+=s.getImageWidth();
    add(s);}
  
  public void addInsertStripe(String path){
    image=null;
    Stripe s=new Stripe_Insert(this,path);
    generator.stripewidthsum+=s.getImageWidth();
    add(s);}
  
  /*
   * note that these stripe lengths were accounted for at the beginning of 
   * the loop, when they were added using the other stripe adding methods.
   * Therefor we do no add their lengths to the stripewidthsum again. 
   */
  public void addTerminusStripesToEndForFinishingUp(List<Stripe> stripes){
    image=null;
    addAll(stripes);}
  
  /*
   * if the first stripe has moved entirely outside the viewport then remove it
   * TODO
   *   cache image width and image x
   *   invalidate image x on chain edit
   */
  public void conditionallyRemoveFirstStripe(){
    Stripe stripe=getFirst();
    if(stripe.getImageX()+stripe.getImageWidth()+generator.edgerange-1<generator.viewportposition){
      generator.viewportposition-=stripe.getImageWidth();
      removeFirst();
      image=null;}}
  
  /*
   * ################################
   * IMAGE
   * Render all of the stripes together as one continuous image
   * ################################
   */
  
  private static final double STROKETHICKNESS=2.0;
  
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
  int imagewidth;//the image is imagewidth X viewportheight
  
  public int getImageWidth(){
    int a=0;
    for(Stripe s:this)
      a+=s.getImageWidth();
    return a;}
  
  public BufferedImage getImage(){
    if(image==null)
      createImage();
    return image;}
  
  private void createImage(){
    initImageAndTransforms();
    if(generator.debug)createDebugImage();
    Graphics2D g=image.createGraphics();
    g.setPaint(Color.black);
    g.fillRect(0,0,image.getWidth(),image.getHeight());
    g.setRenderingHints(RENDERING_HINTS);
    renderPolygonFill(g);
    renderInsertImage(g);
    renderPolygonStroke(g);}
  
  /*
   * ################################
   * RENDER INSERT IMAGE
   * ################################
   */
  
  private void renderInsertImage(Graphics2D g){
    for(Stripe stripe:this)
      if(stripe instanceof Stripe_Insert)
        renderInsertImage(g,(Stripe_Insert)stripe);}
  
  private void renderInsertImage(Graphics2D g,Stripe_Insert stripe){
    AffineTransform t=AffineTransform.getTranslateInstance(stripe.getImageX(),0);
    g.drawImage(stripe.image,t,null);}
  
  /*
   * ################################
   * RENDER POLYGON FILL
   * ################################
   */
  
  private void renderPolygonFill(Graphics2D g){
    for(int i=0;i<size();i++)
      if(get(i) instanceof Stripe_ForsythiaComposition)
        renderPolygonFill(g,i);}
  
  private void renderPolygonFill(Graphics2D g,int stripeindex){
    Stripe stripe=get(stripeindex);
    //
    AffineTransform 
      told=g.getTransform(),
      t=new AffineTransform(told);
    t.concatenate(stripeimagetransforms[stripeindex]);
    g.setTransform(t);
    //
    Iterator<TreeNode> i=((Stripe_ForsythiaComposition)stripe).composition.getLeafPolygonIterator();
    FPolygon p;
    Color color;
    while(i.hasNext()){
      p=(FPolygon)i.next();
      color=((Stripe_ForsythiaComposition)stripe).colormap.get(p);
      g.setPaint(color);
      g.fill(p.getDPolygon().getPath2D());}
    g.setTransform(told);}
  
  /*
   * ################################
   * RENDER POLYGON STROKE
   * ################################
   */
  
  private void renderPolygonStroke(Graphics2D g){
    for(int i=0;i<size();i++)
      if(get(i) instanceof Stripe_ForsythiaComposition)
        renderPolygonStroke(g,i);}
  
  private void renderPolygonStroke(Graphics2D g,int stripeindex){
    Stripe stripe=get(stripeindex);
    if(stripe instanceof Stripe_Insert)return;
    //
    AffineTransform 
      told=g.getTransform(),
      t=new AffineTransform(told);
    t.concatenate(stripeimagetransforms[stripeindex]);
    g.setTransform(t);
    //
    Iterator<TreeNode> i=((Stripe_ForsythiaComposition)stripe).composition.getLeafPolygonIterator();
    FPolygon p;
    g.setPaint(Color.black);
    g.setStroke(createStroke((float)(STROKETHICKNESS/stripeimagetransforms[stripeindex].getScaleX())));
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
      imagewidth+=stripe.getImageWidth();}
    image=new BufferedImage((int)imagewidth,generator.viewportheight,BufferedImage.TYPE_INT_RGB);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * FOR DEBUG IMAGE
   * The debug image is the whole stripechain
   * polygons rendered with black strokes
   * rectangle edge stroked according to nature 
   * ++++++++++++++++++++++++++++++++
   */
  
  public BufferedImage debugimage;
  
  private void createDebugImage(){
    debugimage=new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
    Graphics2D g=debugimage.createGraphics();
    g.setPaint(Color.white);
    g.fillRect(0,0,image.getWidth(),image.getHeight());
    g.setRenderingHints(RENDERING_HINTS);
    for(int i=0;i<size();i++)
      renderBlockfill(g,i);
    for(int i=0;i<size();i++)
      renderPolygonStroke(g,i);}
  
  /*
   * fill up the whole stripe with a single color
   */
  private void renderBlockfill(Graphics2D g,int stripeindex){
    Stripe stripe=get(stripeindex);
    if(stripe instanceof Stripe_Insert)return;
    //
    AffineTransform 
      told=g.getTransform(),
      t=new AffineTransform(told);
    t.concatenate(stripeimagetransforms[stripeindex]);
    g.setTransform(t);
    //
    if(generator.terminus.contains(stripe)){
      g.setPaint(Color.red);
    }else{
      g.setPaint(Color.blue);}
    g.fill(((Stripe_ForsythiaComposition)stripe).composition.getRootPolygon().getDPolygon().getPath2D());
    //
    g.setTransform(told);}
  
  /*
   * ################################
   * STRIPE IMAGE GEOM
   * ################################
   */
  
  public double getImageScale(Stripe stripe){
    double ch=((Stripe_ForsythiaComposition)stripe).composition.getRootPolygon().getDPolygon().getBounds().height;
    double s=generator.viewportheight/ch;
    return s;}
  
  private AffineTransform getStripeImageTransform(Stripe stripe,double stripeimageoffset){
    if(stripe instanceof Stripe_Insert)return null;
    //get all the relevant metrics
    Rectangle2D.Double compositionbounds=((Stripe_ForsythiaComposition)stripe).composition.getRootPolygon().getDPolygon().getBounds();
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
      xoff=((stripe.getImageWidth()/scale-cbwidth)/2.0)-cbxmin,
      yoff=-(((generator.viewportheight/scale+cbheight)/2.0)+cbymin);
    transform.translate(xoff+stripeimageoffset/scale,yoff);
    //
    return transform;}
  
}