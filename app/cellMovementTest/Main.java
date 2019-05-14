package org.fleen.bread.app.cellMovementTest;

import java.awt.image.BufferedImage;

/*
 * DOING IT SQUARE because square is better
 * big upgrade and refinement
 * improving sound too
 */
public class Main{
  
  Main(){
    ui=new UI(this);
    imagething=new CellSmoothingAlgTest();}
  
  UI ui;
  BufferedImage image;
  CellSmoothingAlgTest imagething;
  
  void createImage(){
    image=imagething.getImage();
    ui.imagepanel.repaint();}
  
  public static Main m=null;
  
  public static final void main(String[] a){
    m=new Main();
    m.createImage();}
  
  

}
