package org.fleen.bread.app.lineThing.copy;

import java.awt.image.BufferedImage;

/*
 * DOING IT SQUARE because square is better
 * big upgrade and refinement
 * improving sound too
 */
public class Main{
  
  Main(){
    ui=new UI(this);
    imagething=new ImageThing();}
  
  UI ui;
  BufferedImage image;
  ImageThing imagething;
  
  void createImage(){
    image=imagething.getImage();
    ui.imagepanel.repaint();}
  
  public static Main m=null;
  
  public static final void main(String[] a){
    m=new Main();
    m.createImage();}
  
  

}
