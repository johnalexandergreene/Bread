package org.fleen.bread.app.longGarden;

import java.awt.Color;
import java.io.File;

public class Config{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Config(LongGarden lg){
    this.lg=lg;
    init();}
  
  /*
   * ################################
   * LONG GARDEN
   * ################################
   */
  
  LongGarden lg;
  
  /*
   * ################################
   * INIT
   * Get the config text file from the working directory
   * if it's there
   *   parse it 
   *   get params
   * if it isn't there 
   *   go with param defaults
   *   write a fresh all-defaults config text file
   * ################################
   */
  
  static final String CONFIG_TEXT_FILE_NAME="LONG_GARDEN_CONFIG";
  
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
  
  //turn on the debug messages and such. all the nonfatal stuff (in addition to the always-on fatal stuff)
  boolean debug;
  
  /*
   * ################################
   * PARSE TEXT FILE
   * ################################
   */
  
  private void parse(String[] s){
    
  }

}
