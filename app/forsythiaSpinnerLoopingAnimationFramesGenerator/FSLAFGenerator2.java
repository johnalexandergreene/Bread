package org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator;

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

import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain.StripeChain;
import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.ui.UI;
import org.fleen.bread.composer.Composer;
import org.fleen.bread.composer.Composer001_SplitBoil;
import org.fleen.bread.export.RasterExporter;
import org.fleen.bread.palette.Palette;
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
public class FSLAFGenerator2{
  
  public static final boolean TEST=true;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public FSLAFGenerator2(
    int viewportwidth,int viewportheight,int looplength,int flowdir,
    int edgerange,String grammarpath,String exportpath,double detaillimit,
    Color[] palette,boolean debug){
    this.viewportwidth=viewportwidth;
    this.viewportheight=viewportheight;
    this.looplength=looplength;
    this.flowdir=flowdir;
    this.edgerange=edgerange;
    this.detaillimit=detaillimit;
    this.palette=palette;
    this.debug=debug;
    initGrammar(grammarpath);
    initExport(exportpath);
    initUI();
    initRectangularMetagons();}
  
  /*
   * ################################
   * CREATE FRAMES
   *
   * Init the chains
   *   We use 2 chains of frame-nodes
   *     The first is terminuschain. 
   *     It covers the viewframe's starting position + a little overlap on either end.
   *     Terminus is basically storage for that starting and ending strip of stripes
   *   The second is presentchain.
   *     The viewport is filled with a rendering of presentchain 
   *     As we move the viewport along we create and discard stripe nodes to keep the viewport filled with graphics.
   *     
   * Increment the frame
   *   At each increment we move the viewport forward one pixel.
   *   whe nthe viewport runs out of stripes we create another one
   *   when a stripe runs off the back end we remove it
   *   when the summed length of all of the stripes that we have created is > the 
   *   looplength param then we add terminuschain to the front of presentchain and keep going until frameindex==summedlength
   * 
   * ################################
   */
  

  
  public boolean finished;
  int frameindex;
  int targetframecount;
  public int stripewidthsum=0;
  boolean almostdone=false;
  
  /*
   * the position of the viewport within the present chain
   * we increment it forward, 1 pixel at a time
   * when we modify the chain (by adding or removing a stripe) 
   *   we adjust it to account for the changed length of the chain. 
   */
  public int viewportposition=0;
  
  private void createFrames(){
    finished=false;
    frameindex=0;
    targetframecount=0;
    stripewidthsum=0;
    almostdone=false;
    //
    initChains();
    viewportposition=edgerange;
    while(!finished){
      renderFrame();
      exportFrame();
      incrementPerspective();
      frameindex++;
      if(almostdone&&frameindex>targetframecount)
        finished=true;
      //
      System.out.println("frameindex="+frameindex);
      System.out.println("looplength="+looplength);
      System.out.println("targetframecount="+targetframecount);}}
  
  private void incrementPerspective(){
    viewportposition++;
    if(viewportposition+viewportwidth+edgerange>presentchain.getImageWidth())
      presentchain.addRandomForsythiaCompositionStripeToEnd();
    presentchain.conditionallyRemoveFirstStripe();
    //
    if((!almostdone)&&stripewidthsum>looplength){
      targetframecount=stripewidthsum;
      presentchain.addTerminusStripesToEndForFinishingUp(terminuschain);
      almostdone=true;}}
  
  /*
   * ################################
   * DEBUG
   * When it's true we show some extra stuff in the ui and maybe do some tests 
   * ################################
   */
  
  public boolean debug;
  
  /*
   * ################################
   * PALETTE
   * The colors for the polygons 
   * ################################
   */
  
  public Color[] palette;
  
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
   * 
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
   * This is usually only a few pixels
   * ################################
   */
  
  public int edgerange;
  
  /*
   * ################################
   * FLOW DIRECTION
   * The direction that the chain of stripes flows in the animation
   * Within the generator the stripes always flow right-to-left,
   *   (which is to say, the viewport moves left-to-right)
   *   then we transform the frames afterwards if necessary.
   * ################################
   */
  
  public static final int 
    FLOWDIR_NORTH=0,
    FLOWDIR_EAST=1,
    FLOWDIR_SOUTH=2,
    FLOWDIR_WEST=3;
  
  public int flowdir;
  
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
  
  public double detaillimit=0.013;
  
  public Composer composer=new Composer001_SplitBoil();
  
  /*
   * ################################
   * FRAME EXPORT
   * ################################
   */
  
  File exportdir;
  RasterExporter exporter=new RasterExporter();
  
  private void initExport(String path){
    try{
      exportdir=new File(path);
    }catch(Exception x){
      System.out.println("exception in export path init");
      x.printStackTrace();}
    exporter.setExportDir(exportdir);}
  
  private void exportFrame(){
    exporter.export(frame,frameindex);}
  
  /*
   * ################################
   * RECTANGULAR METAGONS
   * The rectangular metagons in our working grammar
   * Used as root polygon by the stripe node compositions
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
   * We have 2 stripechains
   * terminus and present
   * 
   * terminus ia a strip of rectangles comprising the start and end of our loop of rectangles
   * It covers up the viewport, with a bit of overlap 
   *   to account for inter-rectangle graphic influences. ie edgerange
   *   
   * present changes to keep our viewport filled
   *  
   * ################################
   */
  
  public StripeChain 
    terminuschain,
    presentchain;
  
  private void initChains(){
    //create the terminus
    terminuschain=new StripeChain(this);
    terminuschain.addRandomForsythiaCompositionStripeToEnd();
    while(terminuschain.getImageWidth()<=viewportwidth+edgerange+edgerange)
      terminuschain.addRandomForsythiaCompositionStripeToEnd();
    //copy it to get present
    presentchain=new StripeChain(this,terminuschain);}
 
  /*
   * ################################
   * FRAME
   * ################################
   */
  
  public BufferedImage frame;
  
  private void renderFrame(){
    AffineTransform t=AffineTransform.getTranslateInstance(-viewportposition,0);
    BufferedImage i0=presentchain.getImage();
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
  
  private void initUI(){
    ui=new UI(this);}
  
  /*
   * ################################
   * ++++++++++++++++++++++++++++++++
   * ################################
   * MAIN
   * ################################
   * ++++++++++++++++++++++++++++++++
   * ################################
   */
  
  public static final void main(String[] a){
    FSLAFGenerator2 g=new FSLAFGenerator2(
      300,200,700,FLOWDIR_NORTH,32,"/home/john/Desktop/grammars/g008",
      "/home/john/Desktop/spinnerexport",0.03,Palette.P_TOY_STORY_ADJUSTED,true);
//    FSLAFGenerator g=new FSLAFGenerator(
//      300,200,700,FLOWDIR_NORTH,32,"/home/john/Desktop/grammars/g008",
//      "/home/john/Desktop/spinnerexport",0.013,Palette.P_TOY_STORY_ADJUSTED,true);
    
//    g.generate(800,600,3000,FLOWDIR_NORTH,32,"/home/john/Desktop/ge/nuther003.grammar","/home/john/Desktop/spinnerexport");
//    g.generate(768,1024,54000,FLOWDIR_NORTH,32,"/home/john/Desktop/grammars/g008","/home/john/Desktop/spinnerexport");
    
    g.createFrames();
    
  }
  

}
