package org.fleen.bread.app.longGarden.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.fleen.bread.app.longGarden.LongGarden;

@SuppressWarnings("serial")
public class Viewport extends JPanel{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Viewport(LongGarden lg){
    this.lg=lg;}
  
  /*
   * ################################
   * LONG GARDEN
   * ################################
   */
  
  LongGarden lg;
  
  /*
   * ################################
   * PAINT
   * ################################
   */
  
  public void paint(Graphics g){
    super.paint(g);
    if(lg==null||lg.framegenerator==null||lg.framegenerator.frame==null)return;
    Graphics2D g2=(Graphics2D)g;
    g2.drawImage(lg.framegenerator.frame,null,null);}
}
