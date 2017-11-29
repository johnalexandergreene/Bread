package org.fleen.bread.app.forsythiaSpinner;

import java.awt.Color;
import java.io.File;

public class Params{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Params(String[] s){
    parse(s);}
  
  /*
   * ################################
   * PARAMS
   * ################################
   */
  
  //viewportwidth and viewportheight
  //derived from the physical screen
  
  //the direction that the graphics flow. NESW
  static final String PKEY_FLOWDIR="FLOWDIR";
  static final int 
    FLOWDIR_NORTH=0,
    FLOWDIR_EAST=1,
    FLOWDIR_SOUTH=2,
    FLOWDIR_WEST=3;
  int flowdir=FLOWDIR_NORTH;
  
  //duration between frames
  //in milliseconds
  static final String PKEY_FRAMEPERIOD="FRAMEPERIOD";
  static final long FRAMEPERIOD_DEFAULT=34;
  long frameperiod=FRAMEPERIOD_DEFAULT;
  
  //the distance between the edge of the viewport and the end of the stripe, in pixels, 
  //below which, adding a new stripe to the chain is warranted.
  //to account for graphical effects that span a few pixels beyond the edge of a stripe's edge.
  //like, for example, blur
  //a big value, like 32, or 64 even, is nice and safe.
  static final String PKEY_EDGERANGE="EDGERANGE";
  static final int EDGERANGE_DEFAULT=32; 
  int edgerange=EDGERANGE_DEFAULT;
  
  //path to a serialized Forsythia Grammar file
  static final String PKEY_GRAMMARPATH="GRAMMARPATH";
  //relative to the working directory, where the JAR was run
  static final String GRAMMARPATH_DEFAULT="nice0000.grammar";
  String grammarpath=getLocalDirectory().getAbsolutePath()+"/"+GRAMMARPATH_DEFAULT;
  
  //controls how small the polygons can get in the pattern
  double detaillimit;
  
  //
  double strokethickness;
  
  //
  Color strokecolor;
  
  //
  Color[] polygonpalette;
  
  //the path to the image that gets inserted into the graphics stream now and then
  //an advertisement or credits or logo or something
  String insertpath;
  
  //approximate pixel distance between inserts
  int insertperiod;
  
  //turn on the debug messages and such. all the nonfatal stuff (in addition to the always-on fatal stuff)
  boolean debug;
  
  /*
   * ################################
   * PARSE LINES TO PARAMS
   * ################################
   */
  
  private void parse(String[] s){
    
  }
  
  /*
   * ################################
   * UTIL
   * ################################
   */
  
  public File getLocalDirectory(){
    
  }

}
