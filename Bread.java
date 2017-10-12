package org.fleen.bread;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JTextField;

import org.fleen.bread.composer.Composer;
import org.fleen.bread.composer.Composer001_SplitBoil;
import org.fleen.bread.renderer.Renderer;
import org.fleen.bread.renderer.Renderer000;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

/*
 * bread is a generator of forsythia compositions
 * we use whatever renderer
 * 
 * fo foo foo
 */
public class Bread{
  
//  int bullshit;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Bread(){
    initUI();}
  
  /*
   * ################################
   * ################################
   * ################################
   * PARAMS
   * ################################
   * ################################
   * ################################
   */
  
  Color[] palette=P_TOY_STORY;
  
  String grammar_file_path="/home/john/Desktop/stripegrammar/s003.grammar";
//  String grammar_file_path="/home/john/Desktop/ge/nuther003.grammar";
//  String grammar_file_path="/home/john/Desktop/ge/aa004.grammar";
  
  Composer composer=new Composer001_SplitBoil();
  static final double DETAIL_LIMIT=0.025;
//  Renderer renderer=new Renderer_Rasterizer003();
  Renderer renderer=new Renderer000();
  String exportdirpath="/home/john/Desktop/newstuff";
  
  /*
   * ++++++++++++++++++++++++++++++++
   * COLOR
   * ++++++++++++++++++++++++++++++++
   */
  
  /*
   * THOUGHT PROVOKING
   * down palette. rich, earthy
   * beth dislikes it
   */
  static final Color[] P_THOUGHT_PROVOKING=new Color[]{
      new Color(236,208,120),
      new Color(217,91,67),
      new Color(192,41,66),
      new Color(84,36,55),
      new Color(83,119,122),
    };

  /*
   * WANDERLUST GLAMPING
   * really juicy, bright, candy. 
   * Beth gives it a big thumbs up.
   */
  static final Color[] P_WANDERLUST_GLAMPING=new Color[]{
    new Color(1,24,107),
    new Color(64,203,200),
    new Color(203,54,166),
    new Color(199,203,54),
    new Color(224,135,48),};
  
  /*
   * THOR MOVIE POSTER
   */
  static final Color[] P_THOR_MOVIE_POSTER=new Color[]{
    new Color(186,137,132),
    new Color(43,44,38),
    new Color(241,240,196),
    new Color(55,62,70),
    new Color(119,39,32),
    new Color(133,133,133),
    new Color(136,120,68),
    new Color(232,170,59),
    new Color(94,89,85),
    new Color(66,48,36)};
  
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
  
  /*
   * ++++++++++++++++++++++++++++++++
   * GRAMMAR
   * ++++++++++++++++++++++++++++++++
   */
    
  private ForsythiaGrammar grammar=null;
  
  public ForsythiaGrammar getGrammar(){
    if(grammar==null)
      initGrammar();
    return grammar;}

  private void initGrammar(){
    grammar=null;
    try{
      File f=new File(grammar_file_path);
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
   * ################################
   * ################################
   * UI
   * ################################
   * ################################
   * ################################
   */
  
  private static final String TITLE="Fleen Bread 0.3";
  public UI ui;
  
  private void initUI(){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          ui=new UI(Bread.this);
          ui.setDefaultWindowBounds();
          ui.setVisible(true);
          ui.setTitle(TITLE);
          ui.txtinterval.setText(String.valueOf(CREATION_INTERVAL_DEFAULT));
         }catch(Exception e){
           e.printStackTrace();}}});}
  
  /*
   * ################################
   * ################################
   * ################################
   * PRODUCTION
   * ################################
   * ################################
   * ################################
   */
  
  ForsythiaComposition composition;
  BufferedImage image=null;
  
  /*
   * ++++++++++++++++++++++++++++++++
   * CREATION MODE
   * continuous or intermittant
   * ++++++++++++++++++++++++++++++++
   */
  
  static final boolean MODE_CONTINUOUS=false,MODE_INTERMITTANT=true; 
  boolean creationmode=MODE_INTERMITTANT;
  
  void toggleCreationMode(){
    if(creating)startStopCreation();
    creationmode=!creationmode;
    if(creationmode==MODE_CONTINUOUS){
      ui.lblmode.setText("CON");
    }else{
      ui.lblmode.setText("INT");}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * START-STOP CREATION
   * ++++++++++++++++++++++++++++++++
   */
  
  private boolean creating=false;
  
  void startStopCreation(){
    if(creationmode==MODE_INTERMITTANT){
      if(!creating){
        ui.lblstartstop.setText("||");
        doIntermittantCreation();
        ui.lblstartstop.setText(">>");}
    }else{
      if(creating){
        stopContinuousCreation();
        ui.lblstartstop.setText(">>");
        creating=false;
      }else{
        startContinuousCreation();
        ui.lblstartstop.setText("||");
        creating=true;}}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * CREATION INTERVAL
   * minimum interval between images in continuous creation mode
   * ++++++++++++++++++++++++++++++++
   */
  
  private static final long CREATION_INTERVAL_DEFAULT=1000;
  
  private long creationinterval=CREATION_INTERVAL_DEFAULT;
  
  void setCreationInterval(JTextField t){
    try{
      long a=Long.valueOf(t.getText());
      creationinterval=a;
    }catch(Exception x){
      if(t.getText().equals(""))creationinterval=0;
      t.setText(String.valueOf(creationinterval));}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * INTERMITTANT CREATION
   * ++++++++++++++++++++++++++++++++
   */
  
  private void doIntermittantCreation(){
    composition=composer.compose(getGrammar(),DETAIL_LIMIT);
    image=renderer.createImage(ui.panimage.getWidth(),ui.panimage.getHeight(),composition,palette,true);
    ui.panimage.repaint();
    //maybe export
    if(isExportModeAuto())
      export();}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * CONTINUOUS CREATION
   * ++++++++++++++++++++++++++++++++
   */
  
  private boolean stopcontinuouscreation;
  
  private void startContinuousCreation(){
    stopcontinuouscreation=false;
    new Thread(){
      public void run(){
        long 
          starttime,
          elapsedtime,
          pausetime;
        while(!stopcontinuouscreation){
          starttime=System.currentTimeMillis();
          //compose and render
          composition=composer.compose(grammar,DETAIL_LIMIT);
          image=renderer.createImage(ui.panimage.getWidth(),ui.panimage.getHeight(),composition,palette,true);
          //pause if necessary
          elapsedtime=System.currentTimeMillis()-starttime;
          pausetime=creationinterval-elapsedtime;
          try{
            if(pausetime>0)Thread.sleep(pausetime,0);
          }catch(Exception x){x.printStackTrace();}
          //paint
          ui.panimage.repaint();
          //maybe export
          if(isExportModeAuto())
            export();}}
    }.start();}
  
  private void stopContinuousCreation(){
    stopcontinuouscreation=true;}
  
  /*
   * ################################
   * ################################
   * ################################
   * EXPORT
   * render the current composition using the current composer and renderer
   * use the image size specified in the ui as a guide for that
   * ################################
   * ################################
   * ################################
   */
  
  private static final int 
    EXPORTMODE_MANUAL=0,
    EXPORTMODE_AUTO=1;
  
  private static final int 
    EXPORT_DEFAULT_WIDTH=1000,
    EXPORT_DEFAULT_HEIGHT=1000;
  
  private int getExportMode(){
    if(ui.chkautoexport.isSelected())
      return EXPORTMODE_AUTO;
    else
      return EXPORTMODE_MANUAL;}
  
  boolean isExportModeManual(){
    return getExportMode()==EXPORTMODE_MANUAL;}
  
  boolean isExportModeAuto(){
    return getExportMode()==EXPORTMODE_AUTO;}
  
  void export(){
    File exportdir=getExportDir();
    //
    int w=EXPORT_DEFAULT_WIDTH,h=EXPORT_DEFAULT_HEIGHT;
    try{
      String a=ui.txtexportsize.getText();
      String[] b=a.split("x");
      w=Integer.valueOf(b[0]);
      h=Integer.valueOf(b[1]);
    }catch(Exception x){
      x.printStackTrace();}
    //
    export(exportdir,w,h);}
  
  private File getExportDir(){
    File exportdir=null;
    try{
      exportdir=new File(exportdirpath);
    }catch(Exception x){}
    return exportdir;}
  
  RasterExporter rasterexporter=new RasterExporter();
  
  private void export(File exportdir,int w,int h){
    System.out.println(">>>EXPORT<<<");
    BufferedImage exportimage=renderer.createImage(w,h,composition,palette,false);
    rasterexporter.setExportDir(exportdir);
    rasterexporter.export(exportimage);}
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    new Bread();}
  
}
