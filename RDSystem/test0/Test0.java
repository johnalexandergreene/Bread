package org.fleen.bread.RDSystem.test0;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.fleen.bread.RDSystem.Cell;
import org.fleen.bread.RDSystem.PolygonCells;
import org.fleen.bread.RDSystem.Presence;
import org.fleen.bread.RDSystem.RDSystem;
import org.fleen.bread.RDSystem.Test_2AdjacentRectangles;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;
import org.fleen.forsythia.core.grammar.Jig;
import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;

public class Test0{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test0(){
    initUI();
    initRenderer();
    initComposition();
    initRDS();}
  
  /*
   * ################################
   * MAIN LOOP
   * ################################
   */
  
  public void run(){
    for(int i=0;i<3;i++){
//      doRules();
      render();
    }
    
  }
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  UI ui;
  
  private void initUI(){
    ui=new UI(this);}
  
  /*
   * ################################
   * COMPOSITION
   * ################################
   */
  
  Composition composition;
  
  private void initComposition(){
    composition=new Composition();}
  
  /*
   * ################################
   * RD SYSTEM
   * ################################
   */
  
  int
    rdswidth=800,
    rdsheight=800;
  
  double glowspan=1.5;
  
  RDSystem rds;
  
  void initRDS(){
    rds=new RDSystem(rdswidth,rdsheight,new AffineTransform(),glowspan);}
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  int
    imagewidth=800,
    imageheight=800;
  
  Renderer renderer;
  BufferedImage image=null;
  
  private void initRenderer(){
    renderer=new Renderer(this);}
  
  private void render(){
    renderer.render();
    ui.repaint();}
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    Test0 t=new Test0();
    t.run();}
  
}
