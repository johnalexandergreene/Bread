package org.fleen.bread.app.longGarden.stripeChain;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import org.fleen.bread.app.longGarden.frameGenerator.FrameGenerator;

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
  
  public StripeChain(FrameGenerator fg){
    this.fg=fg;}
  
  /*
   * ################################
   * FRAME GENERATOR
   * ################################
   */
  
  FrameGenerator fg;
  
  /*
   * ################################
   * STRIPE MANAGEMENT
   * 
   * if the viewport has gotten too close to the end of the stripechain then create another stripe.
   * if the last stripe in the stripechain is far enough outside the viewport then remove it.
   * if changes were made then 
   *   update the stripechain image
   *   update framegenerator.framex to account for the changes in the stripechain image
   * do this all on a separate thread
   * 
   * 
   * TODO maybe we could do this every 20 frames or so, if it bogs things
   * maybe put this stuff in the stripechain itself
   * 
   * 
   * ################################
   */
  
  private boolean updatingstripechain=false;
  
  public void updateStripes(){
    if(!updatingstripechain){
      updatingstripechain=true;
      new Thread(){
        public void run(){
          setPriority(NORM_PRIORITY);
          boolean changed=false;
          if(conditionallyRemoveStripe())
            changed=true;
          if(conditionallyAddStripe())
            changed=true;
          if(changed){
            updateImage();}}  
      }.start();
      updatingstripechain=false;}}
  
  private boolean conditionallyRemoveStripe(){
    Stripe a=getFirst();
    if(a.getWidth()+fg.lg.config.getEdgeRange()<fg.getFrameX()){
      removeFirst();
      System.out.println("remove stripe");
      System.out.println("stripecount="+size());
      fg.setFrameX(fg.getFrameX()-a.getWidth());//adjust framex because we just removed a stripe 
      return true;}
    return false;}
  
  private boolean conditionallyAddStripe(){
    if(fg.getFrameX()+fg.getFrameWidth()+fg.lg.config.getEdgeRange()>getWidth()){
      addStripe();
      return true;}
    return false;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * INIT
   * ++++++++++++++++++++++++++++++++
   */
  
  /*
   * add the credits stripe
   * then add composition stripes until chainwidth > viewportwidth+2*viewportmargin
   */
  private void initStripes(){
    while(getWidth()<fg.lg.ui.getViewport().getWidth())
      addStripe();}
  
  //TODO
  /*
   * if there are no stripes then add the credits message stripe
   * otherwise add a composition stripe
   * or something
   */
  private void addStripe(){
    add(new Stripe_Test(this));
    System.out.println("add stripe");
    System.out.println("stripecount="+size());
  }
  
  /*
   * ################################
   * GEOM
   * ################################
   */
  
  public int getWidth(){
    int w=0;
    for(Stripe s:this)
      w+=s.getWidth();
    return w;}
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  public int getHeight(){
    return fg.lg.ui.getViewport().getHeight();}
  
  BufferedImage image=null;
  
  public BufferedImage getImage(){
    if(image==null)
      updateImage();
    return image;}
  
  /*
   * first check to see if we have some stripes
   *   if we don't then this stripechain needs to be initialized by creating 
   *   enough stripes to cover the viewport. do that.
   * render the stripes to the image
   * the frame image is a subimage of this image
   */
  public void updateImage(){
    if(isEmpty())initStripes();
    //
    System.out.println("update image");
    image=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
    Graphics2D g=image.createGraphics();
    renderTestStripes(g);
//    renderMessages();
//    renderCompositionPolygonFills();
//    renderCompositionPolygonStrokes();
    }
  
  private void renderTestStripes(Graphics2D g){
    Stripe_Test st;
    for(Stripe s:this){
      if(s instanceof Stripe_Test){
        st=(Stripe_Test)s;
        g.setPaint(st.color);
        g.fillRect(st.getX(),0,st.getWidth(),getHeight());}}
    
  }
  
}
