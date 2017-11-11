package org.fleen.bread.RDSystem.test0;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.FSLAFGenerator;

@SuppressWarnings("serial")
public class Viewer extends JPanel{
  
  UI ui;
  
  public Viewer(UI ui){
    this.ui=ui;}
  
  public void paint(Graphics g){
    super.paint(g);
    if(ui==null||ui.test==null||ui.test.image==null)return;
    Graphics2D g2=(Graphics2D)g;
    g2.drawImage(ui.test.image,null,null);}
  
  
}
