package org.fleen.bread.app.cellMovementTest;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{

  private static final long serialVersionUID=581500866418502553L;
  
  /*
   * ################################
   * PAINT
   * ################################
   */
  
  static final AffineTransform OFFSETALITTLE=AffineTransform.getTranslateInstance(50,50);
  
  public void paint(Graphics g){
    super.paint(g);
    if(Main.m.image==null)return;
    Graphics2D g2=(Graphics2D)g;
    g2.drawImage(Main.m.image,OFFSETALITTLE,null);}

}
