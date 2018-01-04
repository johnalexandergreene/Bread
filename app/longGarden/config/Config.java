package org.fleen.bread.app.longGarden.config;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.bread.app.longGarden.LongGarden;
import org.fleen.forsythia.app.grammarEditor.sampleGrammars.SampleGrammars;
import org.fleen.forsythia.core.grammar.FMetagon;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

/*
 * This is all the params
 * We store them in a text file for ez editing
 * 
 * get the params out of the config text file
 *   we look for that in the working directory
 * if the text file isn't there then create one, using all defaults
 *   (and then write that new all-defaults config text file to the 
 *   working directory for future reference)
 * in the case of the grammar, if we are using the default 
 *   (specified by path string param) and it isn't there then 
 *   grab our local copy (and then write it to the working 
 *   directory for future reference)
 */
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
   * Get the config params from the config text file
   * if success then we're done
   * if fail then write our default values to a fresh new config text,
   *   because this is a fine time to do it.
   * And then we use those defaults
   * ################################
   */
  
  private void init(){
    boolean a=getConfigFromText();
    if(!a)
      writeConfigToText();}
  
  /*
   * ################################
   * PARAMS
   * ################################
   */
  
  /*
   * ++++++++++++++++++++++++++++++++
   * FLOW DIRECTION
   * the direction that the graphics flow. NESW
   * ++++++++++++++++++++++++++++++++
   */
  
  static final String PKEY_FLOWDIR="FLOWDIR";
  static final int 
    FLOWDIR_NORTH=0,
    FLOWDIR_EAST=1,
    FLOWDIR_SOUTH=2,
    FLOWDIR_WEST=3;
  String[] FLOWDIRSTRINGS={"NORTH","EAST","SOUTH","WEST"};
  int flowdir=FLOWDIR_NORTH;
  
  public int getFlowDir(){
    return flowdir;}
  
  private String getConfigTextLine_FlowDir(){
    String s=PKEY_FLOWDIR+" "+FLOWDIRSTRINGS[flowdir];
    return s;}
  
  private void setFlowDir(String a){
    Integer b=null;
    for(int i=0;i<FLOWDIRSTRINGS.length;i++)
      if(a.equalsIgnoreCase(FLOWDIRSTRINGS[i]))
        b=i;
    if(b==null)throw new IllegalArgumentException();
    flowdir=b;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * FRAME PERIOD
   * Elapsed time from one frame to the next, in terms of milliseconds
   * Note the default, 34 ms, gives us 29.4117647059 fps
   * ++++++++++++++++++++++++++++++++
   */
  
  static final String PKEY_FRAMEPERIOD="FRAMEPERIOD";
//  static final long FRAMEPERIOD_DEFAULT=34;
  static final long FRAMEPERIOD_DEFAULT=83;
  long frameperiod=FRAMEPERIOD_DEFAULT;
  
  public long getFramePeriod(){
    return frameperiod;}
  
  private String getConfigTextLine_FramePeriod(){
    String s=PKEY_FRAMEPERIOD+" "+frameperiod;
    return s;}
  
  private void setFramePeriod(String a){
    frameperiod=Long.valueOf(a);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * EDGE RANGE
   * The distance between the edge of the viewport and the end of the stripe, in pixels, 
   * below which, adding a new stripe to the chain is warranted.
   * to account for graphical effects that span a few pixels beyond the edge of a stripe's edge.
   * like, for example, blur.
   * Just a few pixels will probably do just fine
   * a big value, like 32, tho overkill, is nice and safe and makes me feel good.
   * ++++++++++++++++++++++++++++++++
   */
  
  static final String PKEY_EDGERANGE="EDGERANGE";
  static final int EDGERANGE_DEFAULT=32; 
  int edgerange=EDGERANGE_DEFAULT;
  
  public int getEdgeRange(){
    return edgerange;}
  
  private String getConfigTextLine_EdgeRange(){
    String s=PKEY_EDGERANGE+" "+edgerange;
    return s;}
  
  private void setEdgeRange(String a){
    edgerange=Integer.valueOf(a);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * GRAMMAR PATH
   * path to a serialized Forsythia Grammar file
   * relative to the working directory, where LongGarden.jar was run
   * ++++++++++++++++++++++++++++++++
   */
  
  static final String PKEY_GRAMMARPATH="GRAMMARPATH";
//  static final String GRAMMARPATH_DEFAULT="/default.grammar";
  static final String GRAMMARPATH_DEFAULT="/home/john/Desktop/grammars/s008.grammar";
  String grammarpath=GRAMMARPATH_DEFAULT;
  
  public String getGrammarPath(){
    return grammarpath;}
  
  private String getConfigTextLine_GrammarPath(){
    String s=PKEY_GRAMMARPATH+" "+grammarpath;
    return s;}
  
  //note that we do not validate the grammar path here
  private void setGrammarPath(String a){
    grammarpath=a;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * DETAIL LIMIT
   * The limit on how small a polygon can be
   * ++++++++++++++++++++++++++++++++
   */
  
  static final String PKEY_DETAILLIMIT="DETAILLIMIT";
  static final double DETAILLIMIT_DEFAULT=0.1;
  double detaillimit=DETAILLIMIT_DEFAULT;
  
  public double getDetailLimit(){
    return detaillimit;}
  
  private String getConfigTextLine_DetailLimit(){
    String s=PKEY_DETAILLIMIT+" "+detaillimit;
    return s;}
  
  private void setDetailLimit(String a){
    detaillimit=Double.valueOf(a);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * STROKE THICKNESS
   * in terms of pixels
   * ++++++++++++++++++++++++++++++++
   */
  
  static final String PKEY_STROKETHICKNESS="STROKETHICKNESS";
  static final double STROKETHICKNESS_DEFAULT=2.1;
  double strokethickness=STROKETHICKNESS_DEFAULT;
  
  public double getStrokeThickness(){
    return strokethickness;}
  
  private String getConfigTextLine_StrokeThickness(){
    String s=PKEY_STROKETHICKNESS+" "+strokethickness;
    return s;}
  
  private void setStrokeThickness(String a){
    strokethickness=Double.valueOf(a);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * STROKE COLOR
   * Like this
   *   12,34,56
   * ++++++++++++++++++++++++++++++++
   */
  
  static final String PKEY_STROKECOLOR="STROKECOLOR";
  static final Color STROKECOLOR_DEFAULT=new Color(0,0,0);
  Color strokecolor=STROKECOLOR_DEFAULT;
  
  public Color getStrokeColor(){
    return strokecolor;}
  
  private String getConfigTextLine_StrokeColor(){
    String s=PKEY_STROKECOLOR+" "+
      strokecolor.getRed()+","+
      strokecolor.getGreen()+","+
      strokecolor.getBlue();
    return s;}
  
  private void setStrokeColor(String a){
    String[] b=a.split(",");
    Color c=new Color(Integer.valueOf(b[0]),Integer.valueOf(b[1]),Integer.valueOf(b[2]));
    strokecolor=c;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * POLYGON PALETTE
   * Like this
   *   {12,34,56},{78,90,12},...
   * ++++++++++++++++++++++++++++++++
   */
  
  static final String PKEY_POLYGONPALETTE="POLYGONPALETTE";
  static final Color[] POLYGONPALETTE_DEFAULT=new Color[]{
    new Color(168,67,39),
    new Color(251,206,89),
    new Color(88,184,121),
    new Color(154,94,154),
    new Color(234,61,65),
    new Color(248,237,23),
    new Color(249,139,90),
    new Color(0,146,232),
    new Color(254,178,213)};
  Color[] polygonpalette=POLYGONPALETTE_DEFAULT;
  
  public Color[] getPolygonPalette(){
    return polygonpalette;}
  
  private String getConfigTextLine_PolygonPalette(){
    StringBuffer s=new StringBuffer();
    s.append(PKEY_POLYGONPALETTE+" ");
    for(Color c:polygonpalette)
      s.append("{"+c.getRed()+","+c.getGreen()+","+c.getBlue()+"},");
    s.deleteCharAt(s.length()-1);//get rid of that last comma
    return s.toString();}
  
  private void setPolygonPalette(String a){
    String[] x=a.split("[{},]");//TODO test this
    int r,g,b;
    Color c;
    List<Color> colors=new ArrayList<Color>();
    for(int i=0;i<x.length;i++){
      r=Integer.valueOf(x[i]);
      i++;
      g=Integer.valueOf(x[i]);
      i++;
      b=Integer.valueOf(x[i]);
      c=new Color(r,g,b);
      colors.add(c);}
    polygonpalette=colors.toArray(new Color[colors.size()]);}
  
  /*
   * ################################
   * GET CONFIG PARAMS FROM TEXT FILE
   * Get the file
   * If it isn't there then fail
   * The file is a bunch of lines
   * format is
   *   PKEY ARBITRARYSTRING
   * PKEY is the code for the data on that line, corrosponds to a config parameter
   * yes, delimited by a space
   * ARBITRARYSTRING is an arbitrary bunch of characters, handled by case 
   * ################################
   */
  
//relative to working directory
  static final String CONFIG_TEXT_FILE_PATH="/LONG_GARDEN_CONFIG";
  static final Charset TEXT_CONFIG_FILE_ENCODING=StandardCharsets.UTF_8;
  static final char CONFIGTEXTCOMMENT='#';
  
  private boolean getConfigFromText(){
    Path path=Paths.get(lg.getWorkingDir().getPath()+CONFIG_TEXT_FILE_PATH);
    if(!Files.exists(path,LinkOption.NOFOLLOW_LINKS))return false;//no config file so fail
    List<String> lines=null;
    try{
      lines=Files.readAllLines(path,TEXT_CONFIG_FILE_ENCODING);
    }catch(Exception x){
      x.printStackTrace();}
    for(String line:lines)
      parseConfigTextLine(line);
    return true;}
  
  private void parseConfigTextLine(String line){
    //if the line leads with out comment character then skip it
    if(line.charAt(0)==CONFIGTEXTCOMMENT)return;//TODO test this
    //we should get 2 strings when we split it, the key and the data
    String[] s=line.split(" ");
    String key=s[0];
    if(s.length!=2){
      System.out.println("Parse config text failed @ key="+key);
      return;}
    //
    try{
      if(key.equalsIgnoreCase(PKEY_FLOWDIR)){
        setFlowDir(s[1]);
      }else if(key.equalsIgnoreCase(PKEY_FRAMEPERIOD)){
        setFramePeriod(s[1]);
      }else if(key.equalsIgnoreCase(PKEY_EDGERANGE)){
        setEdgeRange(s[1]);
      }else if(key.equalsIgnoreCase(PKEY_GRAMMARPATH)){
        setGrammarPath(s[1]);
      }else if(key.equalsIgnoreCase(PKEY_DETAILLIMIT)){
        setDetailLimit(s[1]);
      }else if(key.equalsIgnoreCase(PKEY_STROKETHICKNESS)){
        setStrokeThickness(s[1]);
      }else if(key.equalsIgnoreCase(PKEY_STROKECOLOR)){
        setStrokeColor(s[1]);
      }else if(key.equalsIgnoreCase(PKEY_POLYGONPALETTE)){
        setPolygonPalette(s[1]);
      }else{
        System.out.println("unintelligible key detected");}
    }catch(Exception x){
      System.out.println("Parse config text failed @ key="+key);}}
  
  /*
   * ################################
   * CONVERT THIS CONFIG OBJECT TO A LIST OF STRINGS AND WRITE IT TO A TEXT FILE
   * so we can write it to a text file 
   * ################################
   */
  
  private void writeConfigToText(){
    List<String> lines=getConfigTextLines();
    Path path=Paths.get(lg.getWorkingDir().getPath()+CONFIG_TEXT_FILE_PATH);
    try{
      Files.write(path,lines,TEXT_CONFIG_FILE_ENCODING);
    }catch(Exception x){
      x.printStackTrace();}}
  
  private List<String> getConfigTextLines(){
    List<String> lines=new ArrayList<String>();
    lines.add(getConfigTextLine_FlowDir());
    lines.add(getConfigTextLine_FramePeriod());
    lines.add(getConfigTextLine_EdgeRange());
    lines.add(getConfigTextLine_GrammarPath());
    lines.add(getConfigTextLine_DetailLimit());
    lines.add(getConfigTextLine_StrokeThickness());
    lines.add(getConfigTextLine_StrokeColor());
    lines.add(getConfigTextLine_PolygonPalette());
    return lines;}
  
  /*
   * ################################
   * GRAMMAR
   * ################################
   */
  
  private ForsythiaGrammar grammar=null;
  
  public ForsythiaGrammar getGrammar(){
    if(grammar==null){
      initGrammar(grammarpath);
      //if that failed then we have a bad grammar path
      //use the default
      if(grammar==null){
        System.out.println("bad grammar path at grammar init. using default.");
        initGrammar(GRAMMARPATH_DEFAULT);}}
    //
    return grammar;}
  
  /*
   * load the grammar specified by path
   * if that fails 
   *   is the path the path of our default grammar?
   *     no? 
   *       Fail, just return
   *     yes? 
   *       Write our default grammar file from assets
   *       load it
   */
  private void initGrammar(String path){
    ForsythiaGrammar g=loadGrammar(path);
    //load grammar failed
    if(g==null){
      //we were using our default grammar path
      //so get it out of assets and write it to the working directory and
      //then load it
      if(path.equals(GRAMMARPATH_DEFAULT)){
        writeDefaultGrammar();
        grammar=loadGrammar(path);
      //we were not using our default grammar path so just fail
      }else{
        return;}
    //load grammar succeeded so set it
    }else{
      grammar=g;
      return;}}
  
  private ForsythiaGrammar loadGrammar(String path){
    File file=new File(path);
    FileInputStream fis;
    ObjectInputStream ois;
    ForsythiaGrammar fg=null;
    try{
      fis=new FileInputStream(file);
      ois=new ObjectInputStream(fis);
      fg=(ForsythiaGrammar)ois.readObject();
      ois.close();
    }catch(Exception e){
      System.out.println("#^#^# EXCEPTION IN LOAD GRAMMAR #^#^#");
      e.printStackTrace();
      return null;}
    return fg;}
  
  private void writeDefaultGrammar(){
    ForsythiaGrammar g=getDefaultGrammarFromAssets();
    File f=new File(lg.getWorkingDir()+GRAMMARPATH_DEFAULT);
    FileOutputStream fos;
    ObjectOutputStream oot;
    try{
      fos=new FileOutputStream(f);
      oot=new ObjectOutputStream(fos);
      oot.writeObject(g);
      oot.close();
    }catch(IOException x){
      System.out.println("%-%-% EXCEPTION IN WRITE DEFAULT GRAMMAR %-%-%");
      x.printStackTrace();}}
  
  private ForsythiaGrammar getDefaultGrammarFromAssets(){
    ForsythiaGrammar g=null;
    try{
      String grammarname=GRAMMARPATH_DEFAULT.substring(1);//to get rid of the leading /
      InputStream a=SampleGrammars.class.getResourceAsStream(grammarname);
      ObjectInputStream b=new ObjectInputStream(a);
      g=(ForsythiaGrammar)b.readObject();
      b.close();
    }catch(Exception e){
      System.out.println("get default grammar failed.");
      e.printStackTrace();}
    return g;}
  
  /*
   * ################################
   * RECTANGULAR METAGONS
   * Get the rectangular metagons in our working grammar
   * Used as root polygon by the stripe node compositions
   * ################################
   */
  
  private List<FMetagon> rectangularmetagons=null;
  
  public FMetagon getRandomRectangularMetagon(){
    if(rectangularmetagons==null)
      initRectangularMetagons();
    Random rnd=new Random();
    int a=rnd.nextInt(rectangularmetagons.size());
    FMetagon m=rectangularmetagons.get(a);
    return m;}
  
  private void initRectangularMetagons(){
    System.out.println("^^^ init rectangular metagons");
    rectangularmetagons=new ArrayList<FMetagon>();
    for(FMetagon m:getGrammar().getMetagons())
      if(isRectangular(m))
        rectangularmetagons.add(m);
    System.out.println("rectangular metagon count = "+rectangularmetagons.size());}
  
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
 
      
}