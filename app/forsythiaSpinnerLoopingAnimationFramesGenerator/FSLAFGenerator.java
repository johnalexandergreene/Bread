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

import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain.Stripe;
import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.stripeChain.StripeChain;
import org.fleen.bread.app.forsythiaSpinnerLoopingAnimationFramesGenerator.ui.UI;
import org.fleen.bread.composer.Composer;
import org.fleen.bread.composer.Composer002_SplitBoil_WithALittleNoiseNearTheRoot;
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
public class FSLAFGenerator{
  
  public static final boolean TEST=true;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public FSLAFGenerator(
    int viewportwidth,int viewportheight,//dimensions of the viewport and videoframe
    int looplength,//the desired length of the video loop. The actual length will be a bit higher
    int flowdir,//the direction that the graphics flow. NESW
    int edgerange,
    String grammarpath,
    String exportpath,
    double detaillimit,
    Color[] palette,
    String insertpath,
    int insertfrequency,
    boolean debug){
    this.viewportwidth=viewportwidth;
    this.viewportheight=viewportheight;
    this.looplength=looplength;
    this.flowdir=flowdir;
    this.edgerange=edgerange;
    this.detaillimit=detaillimit;
    this.palette=palette;
    this.insertpath=insertpath;
    this.insertfrequency=insertfrequency;
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
  
  /*
   * The number of frames created so far
   */
  int framecount;
  /*
   * the sum length of all stripes that have been created so far
   */
  public int stripewidthsum;
  /*
   * The stripewidthsum after we're done creating stripes
   * The actual finishing value for frameindex
   */
  int adjustedlooplength;
  /*
   * set to true when our total stripe width becomes greater than the param looplength 
   */
  boolean almostdone;
  /*
   * the position of the viewport within the present chain
   * we increment it forward, 1 pixel at a time
   * when we modify the chain (by adding or removing a stripe) we adjust 
   *   it to account for the changed length of the chain. 
   */
  public int viewportposition;
  
  private void createFrames(){
    System.out.println("+++++++++++++++++++++++");
    System.out.println("+ CREATE FRAMES START +");
    System.out.println("+++++++++++++++++++++++");
    //
    framecount=0;
    adjustedlooplength=0;
    stripewidthsum=0;//aka stripe length sum
    almostdone=false;
    //
    initChains();
    viewportposition=edgerange;
    while(!(almostdone&&framecount==adjustedlooplength)){
      System.out.println("frameindex="+framecount+" looplength="+looplength+" adjustedlooplength="+adjustedlooplength);
      renderFrame();
      exportFrame();
      incrementPerspective();
      framecount++;
      //
//      try{
//        Thread.sleep(20);
//      }catch(Exception x){}
    //
    }
    //
    System.out.println("---------------------");
    System.out.println("- CREATE FRAMES END -");
    System.out.println("---------------------");}
  
  int insertcount=1;
  
  /*
   * at every increment test the position of the stripechain relative to the viewport
   * when we need another stripe, create
   * when we're done with a stripe, discard
   */
  private void incrementPerspective(){
    viewportposition++;
    if(viewportposition+viewportwidth+edgerange>chain.getImageWidth()){
      if(
        (INSERTPATH!=null)&&
        framecount>(((double)insertcount)/((double)insertfrequency))*looplength){
        insertcount++;
        chain.addInsertStripe(INSERTPATH);
      }else{
        chain.addRandomForsythiaCompositionStripeToEnd();}} 
    chain.conditionallyRemoveFirstStripe();
    //
    if((!almostdone)&&stripewidthsum>looplength){
      adjustedlooplength=stripewidthsum;
      chain.addTerminusStripesToEndForFinishingUp(terminus);
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
   * The *specified*, in params, sum of the spans of the stripe rectangles
   * The actual loop length is generally a bit higher.
   * We can only specify an estimate because we pick our stripes at random 
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
  
  public Composer composer=new Composer002_SplitBoil_WithALittleNoiseNearTheRoot();
  
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
   * STRIPECHAIN
   * 
   * terminus ia a strip of rectangles comprising the start and end of our loop of rectangles
   * It covers up the viewport, with a bit of overlap 
   *   to account for inter-rectangle graphic influences. ie edgerange
   *   
   * present changes to keep our viewport filled
   *  
   * ################################
   */
  
  public StripeChain chain;
  public List<Stripe> terminus;
  
  private void initChains(){
    //create the chain
    chain=new StripeChain(this);
    chain.addRandomForsythiaCompositionStripeToEnd();//add a composition stripe
    chain.addInsertStripe(insertpath);//add an insert stripe
    while(chain.getImageWidth()<=viewportwidth+edgerange+edgerange)
      chain.addRandomForsythiaCompositionStripeToEnd();
    //store terminus stripes
    terminus=new ArrayList<Stripe>(chain);}
 
  /*
   * ################################
   * RENDER FRAME
   * ################################
   */
  
  public BufferedImage frame;
  
  private void renderFrame(){
    AffineTransform t=AffineTransform.getTranslateInstance(-viewportposition,0);
    BufferedImage i0=chain.getImage();
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
   * INSERT
   * if insertpath is null then we do no inserts
   * ################################
   */
  
  public String insertpath;
  
  public int insertfrequency;
  
  /*
   * ################################
   * FRAME EXPORT
   * Write a frame PNG
   * This is also where we do the transform for getting NESW flow
   * ################################
   */
  
  File exportdir=null;
  RasterExporter exporter=new RasterExporter();
  
  private void initExport(String path){
    if(path==null)return;//no export
    try{
      exportdir=new File(path);
    }catch(Exception x){
      System.out.println("exception in export path init");
      x.printStackTrace();}
    exporter.setExportDir(exportdir);}
  
  private void exportFrame(){
    if(exportdir==null)return;//no export
    BufferedImage tf=getFlowDirTransformedFrameForExport();
    exporter.export(tf,framecount);}
  
  /*
   * we're gonna do this brutishly
   * getpixel and setpixel
   */
  private BufferedImage getFlowDirTransformedFrameForExport(){
    BufferedImage transformed=null;
    if(flowdir==FLOWDIR_NORTH){
      transformed=getNorthFlowDirTransformedFrameForExport();
    }else if(flowdir==FLOWDIR_EAST){
      transformed=getEastFlowDirTransformedFrameForExport();
    }else if(flowdir==FLOWDIR_SOUTH){
      transformed=getSouthFlowDirTransformedFrameForExport();
    }else if(flowdir==FLOWDIR_WEST){
      transformed=getWestFlowDirTransformedFrameForExport();
    }else{
      throw new IllegalArgumentException("invali flow dir specified");}
    return transformed;}
  
  private BufferedImage getNorthFlowDirTransformedFrameForExport(){
    int w=frame.getWidth(),h=frame.getHeight();
    BufferedImage i=new BufferedImage(h,w,BufferedImage.TYPE_INT_RGB);
    int rgb;
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        rgb=frame.getRGB(x,y);
        i.setRGB(h-y-1,x,rgb);}}
    return i;}
  
  private BufferedImage getEastFlowDirTransformedFrameForExport(){
    int w=frame.getWidth(),h=frame.getHeight();
    BufferedImage i=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    int rgb;
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        rgb=frame.getRGB(x,y);
        i.setRGB(w-x-1,h-y-1,rgb);}}
    return i;}
  
  private BufferedImage getSouthFlowDirTransformedFrameForExport(){
    int w=frame.getWidth(),h=frame.getHeight();
    BufferedImage i=new BufferedImage(h,w,BufferedImage.TYPE_INT_RGB);
    int rgb;
    for(int x=0;x<w;x++){
      for(int y=0;y<h;y++){
        rgb=frame.getRGB(x,y);
        i.setRGB(y,w-x-1,rgb);}}
    return i;}
  
  private BufferedImage getWestFlowDirTransformedFrameForExport(){
    return frame;}//because that's the default
  
  /*
   * ################################
   * ++++++++++++++++++++++++++++++++
   * ################################
   * MAIN
   * ################################
   * ++++++++++++++++++++++++++++++++
   * ################################
   */
  
  private static final String 
//    GRAMMARPATH="/home/john/Desktop/grammars/g008",
  GRAMMARPATH="/home/john/Desktop/grammars/s008.grammar",
    EXPORTPATH="/home/john/Desktop/spinnerexport",
//    INSERTPATH="/home/john/Desktop/foobert.png";
    INSERTPATH="/home/john/projects/graphics/spinner logo insert/i000_1280.png";
  
  
  public static final void main(String[] a){
    FSLAFGenerator g=new FSLAFGenerator(
        300,300,2000,FLOWDIR_NORTH,32,GRAMMARPATH,
        EXPORTPATH,
        0.03,Palette.P_TOY_STORY_ADJUSTED,
        INSERTPATH,3,
        true);
//    FSLAFGenerator g=new FSLAFGenerator(
//      720,1280,54000,FLOWDIR_NORTH,32,GRAMMARPATH,
//      EXPORTPATH,
//      0.008,Palette.P_TOY_STORY_ADJUSTED2,
//      INSERTPATH,3,
//      true);
    
    g.createFrames();
    
  }
  

}