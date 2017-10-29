package org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * load an appropriately sized and oriented image from a PNG file and use it for a stripe
 */
public class Stripe_Insert implements Stripe{
  
  public Stripe_Insert(StripeChain chain,String path){
    this.chain=chain;
    initImage(path);}

  /*
   * ################################
   * CHAIN
   * ################################
   */
  
  StripeChain chain;
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  BufferedImage image;
  
  private void initImage(String path){
    File f=new File(path);
    try{
      image=ImageIO.read(f);
    }catch(IOException x){
      System.out.println("exception in image init");
      x.printStackTrace();}}

  public double getStripeImageX(){
    return 2;
  }

  public double getStripeImageWidth(){
    return image.getWidth();}

  @Override
  public void setTransform(AffineTransform t){
    // TODO Auto-generated method stub
    
  }

  @Override
  public AffineTransform getTransform(){
    // TODO Auto-generated method stub
    return null;
  }
  
}
