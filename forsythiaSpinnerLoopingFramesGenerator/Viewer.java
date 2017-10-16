package org.fleen.bread.forsythiaSpinnerLoopingFramesGenerator;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Viewer extends JFrame{
  
  ForsythiaSpinnerLoopingFramesGenerator gen;
  
  public Viewer(ForsythiaSpinnerLoopingFramesGenerator gen,int w,int h){
    this.gen=gen;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(50,50,800,800);
    setVisible(true);}
  
  public void paint(Graphics g){
    super.paint(g);
    if(gen==null||gen.frame==null)return;
    Graphics2D g2=(Graphics2D)g;
    g2.drawImage(gen.frame,null,null);}

}
