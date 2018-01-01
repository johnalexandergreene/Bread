package org.fleen.bread.app.longGarden.stripeChain;

import java.awt.Color;
import java.util.Random;

/*
 * a block of color
 */
public class Stripe_Test implements Stripe{

  Stripe_Test(StripeChain stripechain){
    this.stripechain=stripechain;
    initWidth();
    initColor();
  }
  
  /*
   * ################################
   * STRIPECHAIN
   * ################################
   */
  
  StripeChain stripechain;
  
  /*
   * ################################
   * GEOMETRY
   * note that height is constant over the chain
   * ################################
   */
  
  int width;
  
  public int getWidth(){
    return width;}

  public int getX(){
    int x=0;
    for(Stripe s:stripechain){
      if(s==this)
        return x;
      x+=s.getWidth();}
    return x;}
  
  private void initWidth(){
    int a=new Random().nextInt(3)+1;
    width=(stripechain.fg.lg.ui.getViewport().getHeight()/9)*a;}
  
  /*
   * ################################
   * COLOR
   * ################################
   */
  
  public Color color;
  
  private void initColor(){
    Color[] p=stripechain.fg.lg.config.getPolygonPalette();
    int a=new Random().nextInt(p.length);
    color=p[a];}

}
