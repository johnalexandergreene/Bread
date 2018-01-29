package org.fleen.bread.app.forsythiaCompositionGenerator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Head0Viewer extends JPanel{

  private static final long serialVersionUID=581500866418502553L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Head0Viewer(Head0 head){
    this.head=head;}
  
  /*
   * ################################
   * HEAD
   * ################################
   */
  
  Head0 head;
  
  /*
   * ################################
   * PAINT
   * ################################
   */
  
  public void paint(Graphics g){
    super.paint(g);
    if(head==null)return;
    BufferedImage i=head.getUIViewerImage();
    if(i==null)return;
    Graphics2D g2=(Graphics2D)g;
    g2.drawImage(i,null,null);}

}
