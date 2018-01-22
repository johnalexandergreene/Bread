package org.fleen.bread.app.forsythiaCompositionGenerator;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JTextField;

import org.fleen.bread.colorMap.CM_SymmetricChaos_EggLevelTriplePaletteSplit;
import org.fleen.bread.colorMap.ColorMap;
import org.fleen.bread.composer.Composer;
import org.fleen.bread.composer.Composer002_SplitBoil_DoubleRootEntropy;
import org.fleen.bread.export.RasterExporter;
import org.fleen.bread.palette.Palette;
import org.fleen.bread.renderer.R_SimpleStrokes;
import org.fleen.bread.renderer.Renderer;
import org.fleen.forsythia.core.composition.ForsythiaComposition;
import org.fleen.forsythia.core.grammar.ForsythiaGrammar;

/*
 * a single forsythia composition
 */
public class FCGenerator2{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public FCGenerator2(){
    initUI();
    initRenderer();}
  
  /*
   * ################################
   * ################################
   * ################################
   * PARAMS
   * ################################
   * ################################
   * ################################
   */
  
  ForsythiaGrammar grammar;
  Composer composer;
  ColorMap colormap;
  Renderer renderer;
  int exportw,exporth,borderthickness;
  
  
  
  Color[] palette=Palette.P_GRACIE001;
  String grammar_file_path="/home/john/projects/code/Bread/src/org/fleen/bread/grammar/verynice_2018_01_06.grammar";
  Composer composer=new Composer002_SplitBoil_DoubleRootEntropy();
  String exportdirpath="/home/john/Desktop/newstuff";
  ColorMap colormap;
  int borderthickness=12;
  Color bordercolor=Color.white;
  Color strokecolor=Color.white;
  float strokethickness=0.005f;
  double detaillimit=0.03;
  
  /*
   * ++++++++++++++++++++++++++++++++
   * RENDERER
   * ++++++++++++++++++++++++++++++++
   */
  
  Renderer renderer;
  
  private void initRenderer(){
    renderer=new R_SimpleStrokes();
    R_SimpleStrokes r=(R_SimpleStrokes)renderer;
    r.setBorderColor(bordercolor);
    r.setBorderThickness(borderthickness);
    r.setStrokeColor(strokecolor);
    r.setStrokeThickness(strokethickness);}
  
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
  
  protected void initUI(){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          ui=new UI(FCGenerator2.this);
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
    composition=composer.compose(getGrammar(),detaillimit);
//    colormap=new CM_SymmetricChaos(composition,palette);
    colormap=new CM_SymmetricChaos_EggLevelTriplePaletteSplit(composition,palette);
    renderer.setColorMap(colormap);
    renderer.setComposition(composition);
    image=renderer.createImage(ui.panimage.getWidth(),ui.panimage.getHeight());
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
          composition=composer.compose(grammar,detaillimit);
//          colormap=new CM_SymmetricChaos(composition,palette);
          colormap=new CM_SymmetricChaos_EggLevelTriplePaletteSplit(composition,palette);
          renderer.setColorMap(colormap);
          renderer.setComposition(composition);
          image=renderer.createImage(ui.panimage.getWidth(),ui.panimage.getHeight());
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
    if(colormap==null){
//      colormap=new CM_SymmetricChaos(composition,palette);
      colormap=new CM_SymmetricChaos_EggLevelTriplePaletteSplit(composition,palette);}
    renderer.setColorMap(colormap);
    renderer.setComposition(composition);
    BufferedImage exportimage=renderer.createImage(w,h);
    rasterexporter.setExportDir(exportdir);
    rasterexporter.export(exportimage);}
  
}
