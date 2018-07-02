package org.fleen.bread.app.cruncher;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class UI extends JFrame{
  
  Cruncher cruncher;
  Renderer renderer;
  
  UI(Cruncher cruncher){
    this.cruncher=cruncher;
    renderer=new Renderer(cruncher);
    cruncher.ui=this;
    setBounds(100,100,500,500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);}
  
  public void paint(Graphics g){
    Graphics2D g2=(Graphics2D)g;
    if(renderer==null)return;
    BufferedImage i=renderer.getImage();
    g2.drawImage(i,null,null);
  }

}
