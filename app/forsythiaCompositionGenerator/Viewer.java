package org.fleen.bread.app.forsythiaCompositionGenerator;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Viewer extends JPanel{

  private static final long serialVersionUID=581500866418502553L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Viewer(FCGenerator generator){
    this.generator=generator;}
  
  /*
   * ################################
   * GENERATOR
   * ################################
   */
  
  FCGenerator generator;
  
  /*
   * ################################
   * PAINT
   * ################################
   */
  
  public void paint(Graphics g){
    super.paint(g);
    if(generator==null||generator.image==null)return;
    Graphics2D g2=(Graphics2D)g;
    g2.drawImage(generator.image,null,null);}

}
