package org.fleen.bread.RDSystem.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.FSLAFGenerator;

@SuppressWarnings("serial")
public class Viewer extends JPanel{
  
  Test_SimpleComposition test;
  
  public Viewer(Test_SimpleComposition test){
    this.test=test;}
  
  private static final AffineTransform NICEOFFSET=AffineTransform.getTranslateInstance(700,10);
  
  public void paint(Graphics g){
    super.paint(g);
    if(test==null||test.frame==null)return;
    Graphics2D g2=(Graphics2D)g;
    g2.drawImage(test.frame,NICEOFFSET,null);
    }
  
  
}
