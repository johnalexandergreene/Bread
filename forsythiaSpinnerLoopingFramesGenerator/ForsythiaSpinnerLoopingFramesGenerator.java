package org.fleen.bread.forsythiaSpinnerLoopingFramesGenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

/*
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
public class ForsythiaSpinnerLoopingFramesGenerator{
  
  public void generate(int w,int h,int d,String grammarpath,String exportpath){
    viewportwidth=w;
    viewportheight=h;
    flowdir=d;
    initGrammar(grammarpath);
    initExport(exportpath);
    createFrames();}
  
  /*
   * ################################
   * VIEWPORT DEFINITION
   * ################################
   */
  
  int 
    viewportwidth,
    viewportheight;
  
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
  
  ForsythiaGrammar grammar=null;
  
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
  private void createFrames(){
    initRectangularMetagons();
    initTerminus();
    //
    boolean finished=false;
    BufferedImage frame;
    while(!finished){
      frame=createFrame();
      if(frame!=null)
        exportframe(frame);
      else
        finished=true;}}
  
  /*
   * ################################
   * RECTANGULAR METAGONS
   * ################################
   */
  
  List<FMetagon> rectangularmetagons;
  
  void initRectangularMetagons(){
    rectangularmetagons=new ArrayList<FMetagon>();
    for(FMetagon m:grammar.getMetagons())
      if(isRectangular(m))
        rectangularmetagons.add(m);}
  
  private boolean isRectangular(FMetagon m){
    //a rectangular metagon has 3 vectors
    if(m.vectors.length!=3)return false;
    //all 3 vectors have direction=3
    if(!(
      m.vectors[0].directiondelta==3&&
      m.vectors[1].directiondelta==3&&
      m.vectors[2].directiondelta==3))return false;
    //the first and last vectors have equal length
    if(
      m.vectors[0].relativeinterval!=
      m.vectors[2].directiondelta)return false;
    //
    return true;}
  
  /*
   * ################################
   * TERMINUS
   * A strip of rectangles comprising the start and end of our loop of rectangles
   * It covers up the viewport, with a bit of overlap to account for inter-rectangle graphic influences
   * ################################
   */
  
  private void initTerminus(){
    
  }
  
  /*
   * ################################
   * GET RANDOM NEXT RECTANGLE
   * each rectangle is a forsythia composition
   * get a random rectangular metagon
   * create a root grid that puts that metagon polygon rectangle at the edge of our previous rectangle
   * cultivate it up
   * init the leaf polygon colors  
   * 
   * I think that we should do it left-to-right (AKA east) and then, 
   * depending on the flow direction, apply a transform to the image if necessary. 
   * 
   * ################################
   */
  
  
  
  

}
