package org.fleen.bread.fSLAFG;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.bread.composer.Composer;
import org.fleen.bread.composer.Composer001_SplitBoil;
import org.fleen.bread.fSLAFG.renderer.Renderer;
import org.fleen.bread.fSLAFG.renderer.Renderer000;
import org.fleen.bread.fSLAFG.stripeChain.StripeChain;
import org.fleen.bread.fSLAFG.ui.UI;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

/*
 * FORSYTHIA SPINNER LOOPING ANIMATION FRAMES GENERATOR
 * 
 * 
 * Given
 *   a viewport definition (height and width) 
 *   a flow direction (NESW)
 *   a grammar that contains a number of retangular metagons
 *   the path to an export dir for the frames
 *   
 * gather the rectangular metagons
 * 
 * create a strip of rectangles to cover the viewport.
 * This is the startend.
 * 
 * cultivate the rectangles. Render to viewport (and to a frame, and export that frame)
 * move the viewport across the startend, 1 pixel row at a time
 * when we hit the edge create a new adjoining rectangle
 * keep moving.
 * when the viewport leaves a rectangle, discard the rectangle.
 * (but don't discard the startend)
 * keep adding rectangles and reaversing them until the sum of the rectangles (including the startend) meets our desired loop length.
 * After that we move back to the startend
 * keep moving until we arrive back at out start position.
 * then we're done.
 */
public class Generator{
  
  public static final boolean TEST=true;
  
  public void generate(int viewportwidth,int viewportheight,int looplength,int flowdir,int edgerange,String grammarpath,String exportpath){
    this.viewportwidth=viewportwidth;
    this.viewportheight=viewportheight;
    this.looplength=looplength;
    this.flowdir=flowdir;
    this.edgerange=edgerange;
    initGrammar(grammarpath);
    initExport(exportpath);
    createFrames();}
  
  /*
   * ################################
   * VIEWPORT DEFINITION
   * The height and width of the rectangle through which the graphics flow
   * ################################
   */
  
  public int 
    viewportwidth,
    viewportheight;
  
  /*
   * ################################
   * LOOP LENGTH
   * The sum of the spans of the stripe rectangles
   * We shoot for an actual sum that is close to the specified length but greater 
   * ################################
   */
  
  public int looplength;
  
  /*
   * ################################
   * RECTANGLE EDGERANGE
   * This is the range, in pixels, within which rectangles graphically influence each other
   * like a glow or whatever
   * so when the right edge of the viewport approaches the right edge of a rectangle
   * ################################
   */
  
  int edgerange;
  
  /*
   * ################################
   * FLOW DIRECTION
   * ################################
   */
  
  public static final int 
    FLOWDIR_NORTH=0,
    FLOWDIR_EAST=1,
    FLOWDIR_SOUTH=2,
    FLOWDIR_WEST=3;
  
  int flowdir;
  
  /*
   * ################################
   * GRAMMAR
   * ################################
   */
  
  public ForsythiaGrammar grammar=null;
  
  private void initGrammar(String path){
    grammar=null;
    try{
      File f=new File(path);
      grammar=importGrammarFromFile(f);
    }catch(Exception x){
      System.out.println("exception in grammar import");
      x.printStackTrace();}}
    
  private ForsythiaGrammar importGrammarFromFile(File file){
    FileInputStream fis;
    ObjectInputStream ois;
    ForsythiaGrammar g=null;
    try{
      fis=new FileInputStream(file);
      ois=new ObjectInputStream(fis);
      g=(ForsythiaGrammar)ois.readObject();
      ois.close();
    }catch(Exception x){}
    return g;}
  
  /*
   * ################################
   * COMPOSITION CONTROL
   * ################################
   */
  
  public double compositiondetaillimit=0.04;
  
  public Composer composer=new Composer001_SplitBoil();
  
  /*
   * ################################
   * FRAME EXPORT
   * ################################
   */
  
  File exportdir;
  
  private void initExport(String path){
    try{
      exportdir=new File(path);
    }catch(Exception x){
      System.out.println("exception in export path init");
      x.printStackTrace();}}
  
  /*
   * ################################
   * CREATE FRAMES
   * ################################
   */
  
  /*
   * gather the rectangular metagons from the grammar
   * 
   * create a strip of rectangles to cover the viewport.
   * This is the terminus. The beginning and end.
   * 
   * cultivate the rectangles. Render to viewport (and to a frame, and export that frame)
   * move the viewport across the startend, 1 pixel row at a time
   * when we hit the edge create a new adjoining rectangle
   * keep moving.
   * when the viewport leaves a rectangle, discard the rectangle.
   * (but don't discard the startend)
   * keep adding rectangles and reaversing them until the sum of the rectangles (including the startend) meets our desired loop length.
   * After that we move back to the startend
   * keep moving until we arrive back at out start position.
   * then we're done.
   */
  
  boolean finished;
  
  private void createFrames(){
    initRectangularMetagons();
    initTerminus();
    //
    viewportposition=edgerange;
    finished=false;
//    while(!finished){
//      createFrame();
//      updateViewer();
//      exportframe(frame);
//      incrementPerspective();}
    }
  
  /*
   * ################################
   * RECTANGULAR METAGONS
   * The rectangular metagons in our working grammar
   * Used by the stripe node compositions
   * ################################
   */
  
  private List<FMetagon> rectangularmetagons;
  
  private void initRectangularMetagons(){
    rectangularmetagons=new ArrayList<FMetagon>();
    for(FMetagon m:grammar.getMetagons())
      if(isRectangular(m))
        rectangularmetagons.add(m);
    System.out.println("rectangular metagon count = "+rectangularmetagons.size());}
  
  public FMetagon getRandomRectangularMetagon(){
    Random rnd=new Random();
    int a=rnd.nextInt(rectangularmetagons.size());
    FMetagon m=rectangularmetagons.get(a);
    return m;}
  
  private boolean isRectangular(FMetagon m){
    //a rectangular metagon has 2 vectors
    if(m.vectors.length!=2)return false;
    //those vectors have directiondelta=3
    if(!(
      m.vectors[0].directiondelta==3&&
      m.vectors[1].directiondelta==3))return false;
    //the second vector has length==1 (more or less)
    if(!(isOne(m.vectors[1].relativeinterval)))return false;
    //
    return true;}
  
  //wtfe
  //close enough to 1
  static final double ONEERROR=0.000000000000005;
  private boolean isOne(double a){
    return (a>1-ONEERROR)&&(a<1+ONEERROR);}
  
  /*
   * ################################
   * STRIPECHAINS
   * 
   * We have 1 or 2 stripechains
   * terminus and present
   * 
   * terminus ia a strip of rectangles comprising the start and end of our loop of rectangles
   * It covers up the viewport, with a bit of overlap 
   *   to account for inter-rectangle graphic influences. ie edgerange
   *   
   * present changes to keep our viewport filled
   * 
   * At init they are the same chain
   * ################################
   */
  
  public StripeChain 
    terminus,
    present;
  
  private void initTerminus(){
    terminus=new StripeChain(this);
    terminus.createStripeAtEnd();
    while(terminus.getImageWidth()<=viewportwidth+edgerange+edgerange)
      terminus.createStripeAtEnd();
    present=terminus;
    System.out.println("--------------------------------");
    System.out.println("created terminus");
    System.out.println("stripecount = "+terminus.size());
    System.out.println("imagewidth = "+terminus.getImageWidth());
    System.out.println("--------------------------------");}
 
  /*
   * ################################
   * FRAME
   * ################################
   */
  
  public BufferedImage frame;
  
  /*
   * the position of the viewport in the present chain
   * we increment it forward, 1 pixel at a time
   * we also modify it when we modify the chain 
   */
  public int viewportposition=0;
  
  Renderer renderer=new Renderer000();
  
  BufferedImage testimage;
  
  void renderFrame(){
    AffineTransform t=AffineTransform.getTranslateInstance(-viewportposition,0);
    BufferedImage i0=present.getImage();
    frame=new BufferedImage(viewportwidth,viewportheight,BufferedImage.TYPE_INT_RGB);
    Graphics2D g=frame.createGraphics();
    g.drawImage(i0,t,null);
    ui.viewer.repaint();}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  UI ui;
  
  void initUI(){
    ui=new UI(this,viewportwidth+20,viewportheight+20);
  }
  
  /*
   * ################################
   * PALETTE
   * ################################
   */
  
  /*
   * TOY STORY MOVIE
   */
  static final Color[] P_TOY_STORY=new Color[]{
    new Color(168,67,39),
    new Color(250,200,147),
    new Color(163,187,75),
    new Color(154,94,154),
    new Color(232,62,65),
    new Color(249,212,1),
    new Color(249,139,90),
    new Color(236,77,74),
    new Color(0,146,231),
    new Color(251,206,221)};
  
  public Color[] palette=P_TOY_STORY;
  
  /*
   * ################################
   * ################################
   * ################################
   * MAIN
   * ################################
   * ################################
   * ################################
   */
  
  public static final void main(String[] a){
    Generator g=new Generator();
    g.generate(200,300,1000,FLOWDIR_NORTH,5,"/home/john/Desktop/ge/nuther003.grammar","/home/john/Desktop/newstuff");
    g.initUI();
    for(int i=0;i<1000;i++){
      g.renderFrame();
      g.viewportposition++;
      try{
        Thread.sleep(50);
      }catch(Exception x){}}
    
    
  }
  

}
