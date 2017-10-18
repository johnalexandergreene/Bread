package org.fleen.bread.fSLAFG.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.fleen.bread.fSLAFG.Generator;

@SuppressWarnings("serial")
public class Viewer extends JPanel{
  
  Generator gen;
  
  public Viewer(Generator gen){
    this.gen=gen;}
  
  public void paint(Graphics g){
    super.paint(g);
    if(gen==null||gen.frame==null)return;
    Graphics2D g2=(Graphics2D)g;
    g2.drawImage(gen.frame,null,null);}

}
