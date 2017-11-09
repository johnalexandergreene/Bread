package org.fleen.bread.RDSystem.test;

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

public class Test_SimpleComposition{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test_SimpleComposition(){
    initFrame();
    initForsythiaComposition();
    initRDS();
    rds.castPresence(rectangle0,rectangle1);
    frame.repaint();}
  
  /*
   * ################################
   * FRAME
   * ################################
   */
  
  Frame0 frame;
  
  void initFrame(){
    frame=new Frame0();}
  
  @SuppressWarnings("serial")
  class Frame0 extends JFrame{
    
    Frame0(){
      this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      this.setBounds(100,100,800,800);
      this.setVisible(true);}
    
    public void paint(Graphics g){
      try{
        Graphics2D g2=(Graphics2D)g;
        g2.drawImage(render(),null,null);
      }catch(Exception x){}}}
  
  /*
   * ################################
   * COMPOSITION
   * ################################
   */
  
  static final String GRAMMARPATH="/home/john/Desktop/grammars/rdtest.grammar";
  ForsythiaComposition composition;
  
  private void initForsythiaComposition(){
    composition=new ForsythiaComposition();
    ForsythiaGrammar grammar=getGrammar();
    composition.setGrammar(getGrammar());
    FMetagon rm=getRootMetagon(grammar);
    composition.initTree(rm);
    Jig j=grammar.getJigs(rm).get(0);
    j.createNodes(composition.getRootPolygon());}
  
  private FMetagon getRootMetagon(ForsythiaGrammar grammar){
    for(FMetagon m:grammar.getMetagons()){
      if(m.hasTags("root"))
        return m;}
    throw new IllegalArgumentException("exception is root metagon acquirement");}
  
  private ForsythiaGrammar getGrammar(){
    File file=new File(GRAMMARPATH);
    FileInputStream fis;
    ObjectInputStream ois;
    ForsythiaGrammar g=null;
    try{
      fis=new FileInputStream(file);
      ois=new ObjectInputStream(fis);
      g=(ForsythiaGrammar)ois.readObject();
      ois.close();
    }catch(Exception x){
      System.out.println("exception in gramar import");
      x.printStackTrace();}
    return g;}
  
  /*
   * ################################
   * POLYGONS
   * ################################
   */
  
  DPolygon 
    rectangle0=new DPolygon(new DPoint(0,0),new DPoint(0,10),new DPoint(10,10),new DPoint(10,0)),
    rectangle1=new DPolygon(new DPoint(10,0),new DPoint(10,10),new DPoint(20,10),new DPoint(20,0));
  
  /*
   * ################################
   * RD SYSTEM
   * ################################
   */
  
  private static final int
    CELLARRAYWIDTH=800,
    CELLARRAYHEIGHT=800;
  private static final double GLOWSPAN=1.5;
  RDSystem rds;
  PolygonCells polygoncellmap;//the cells that feel the presence of the polygon
  
  void initRDS(){
    AffineTransform t=new AffineTransform();
    
    t.translate(60,60);
    t.rotate(0.2);
    t.scale(12.0,12.0);
    
    rds=new RDSystem(CELLARRAYWIDTH,CELLARRAYHEIGHT,t,GLOWSPAN);}
  
  /*
   * ################################
   * RENDER
   * ################################
   */
  
  BufferedImage render(){
    BufferedImage image=new BufferedImage(CELLARRAYWIDTH,CELLARRAYHEIGHT,BufferedImage.TYPE_INT_RGB);
    for(Cell c:rds)
      image.setRGB(c.x,c.y,getColor(c).getRGB());
    return image;}
  
  /*
   * ################################
   * CELL COLOR LOGIC
   * ################################
   */
  
  private static final Color 
    COLOR_R0=new Color(112,229,254),
    COLOR_R1=new Color(255,194,111);
  
  private Color getColor(Cell c){
    //get intensity sum
    double intensitysum=0;
    for(Presence p:c.presences)
      intensitysum+=p.intensity;
    //get normalized intensity for each presence and sum weighted r g b color components
    int r=0,g=0,b=0;
    Color color;
    double normalized;
    for(Presence p:c.presences){
      normalized=p.intensity/intensitysum;
      color=getPolygonColor(p.polygon);
      r+=(int)(color.getRed()*normalized);
      g+=(int)(color.getGreen()*normalized);
      b+=(int)(color.getBlue()*normalized);}
    //
    return new Color(r,g,b);}
  
  private Color getPolygonColor(DPolygon polygon){
    if(polygon==rectangle0)
      return COLOR_R0;
    else
      return COLOR_R1;}

  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    new Test_SimpleComposition();}
  
}
