package org.fleen.bread.app.forsythiaCompositionGenerator;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLDecoder;

import javax.swing.JTextField;

import org.fleen.bread.export.RasterExporter;
import org.fleen.forsythia.app.grammarEditor.GE;

/*
 * runs a UI and composition generator
 * provides automation and bitmap export services 
 */
public class Head0{
  
  private static final String NAME="Fleen Bread 0.3";
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Head0(ForsythiaCompositionImageGenerator g){
    gen=g;
    initExport();
    initUI();}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  public Head0UI ui;
  BufferedImage uiviewerimage=null;
  
  protected void initUI(){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          ui=new Head0UI(Head0.this);
          ui.setDefaultWindowBounds();
          ui.setVisible(true);
          ui.setTitle(NAME);
         }catch(Exception e){
           e.printStackTrace();}}});}
  
  public BufferedImage getUIViewerImage(){
    return uiviewerimage;}
  
  /*
   * ################################
   * EXPORT
   * ################################
   */
  
  private static final int 
    EXPORT_DEFAULT_WIDTH=1000,
    EXPORT_DEFAULT_HEIGHT=1000;
  
  RasterExporter rasterexporter=new RasterExporter();
  
  int 
    exportwidth=EXPORT_DEFAULT_WIDTH,
    exportheight=EXPORT_DEFAULT_HEIGHT;
  
  File exportdir;
  
  private void initExport(){
    exportdir=getLocalDir();}
  
  void setExportWidth(int w){
    exportwidth=w;}
  
  int getExportWidth(){
    return exportwidth;}
  
  void setExportHeight(int h){
    exportheight=h;}
  
  int getExportHeight(){
    return exportheight;}
  
  void setExportDir(String s){
    try{
      exportdir=new File(s);
    }catch(Exception x){}}
  
  void export(){
    System.out.println("#-#-#-EXPORT-#-#-#");
    ForsythiaCompositionImageGenerator g=getGen();
    g.setImageDimensions(exportwidth,exportheight);
    BufferedImage exportimage=g.getImage();
    rasterexporter.setExportDir(exportdir);
    rasterexporter.export(exportimage);}
 
  /*
   * ################################
   * GENERATOR
   * ################################
   */
  
  ForsythiaCompositionImageGenerator gen=null;
  
  public ForsythiaCompositionImageGenerator getGen(){
    return gen;}
  
  /*
   * ################################
   * CONTROL
   * ################################
   */
  
  //--------------------------------
  //CREATION MODE
  
  static final boolean 
    MODE_CONTINUOUS=false,
    MODE_INTERMITTANT=true; 
  boolean creationmode=MODE_INTERMITTANT;
  
  void toggleCreationMode(){
    if(creating)startStopCreation();
    creationmode=!creationmode;
    if(creationmode==MODE_CONTINUOUS){
      ui.lblmode.setText("CON");
    }else{
      ui.lblmode.setText("INT");}}
  
  //--------------------------------
  //START-STOP CREATION
  
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
  
  //--------------------------------
  //CREATION INTERVAL
  
  private static final long CREATION_INTERVAL_DEFAULT=1000;
  
  private long creationinterval=CREATION_INTERVAL_DEFAULT;
  
  void setCreationInterval(JTextField t){
    try{
      long a=Long.valueOf(t.getText());
      creationinterval=a;
    }catch(Exception x){
      if(t.getText().equals(""))creationinterval=0;
      t.setText(String.valueOf(creationinterval));}}
  
  //--------------------------------
  //INTERMITTANT CREATION
  
  private void doIntermittantCreation(){
    ForsythiaCompositionImageGenerator g=getGen();
    g.regenerateComposition();
    g.regenerateColorMap();
    g.setImageDimensions(ui.panimage.getWidth(),ui.panimage.getHeight());
    uiviewerimage=g.getImage();
    ui.panimage.repaint();
    //maybe export
    if(isExportModeAuto())
      export();}
  
  //--------------------------------
  //CONTINUOUS CREATION
  
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
          ForsythiaCompositionImageGenerator g=getGen();
          g.regenerateComposition();
          g.regenerateColorMap();
          g.setImageDimensions(ui.panimage.getWidth(),ui.panimage.getHeight());
          uiviewerimage=g.getImage();
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
  
  //--------------------------------
  //EXPORT MODE
  
  private static final int 
    EXPORTMODE_MANUAL=0,
    EXPORTMODE_AUTO=1;

  private int getExportMode(){
    if(ui.chkautoexport.isSelected())
      return EXPORTMODE_AUTO;
    else
      return EXPORTMODE_MANUAL;}

  boolean isExportModeManual(){
    return getExportMode()==EXPORTMODE_MANUAL;}

  boolean isExportModeAuto(){
    return getExportMode()==EXPORTMODE_AUTO;}
  
  /*
   * ################################
   * UTIL
   * ################################
   */

  static final File getLocalDir(){
    String path=GE.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    String decodedpath;
    try{
      decodedpath=URLDecoder.decode(path,"UTF-8");
    }catch(Exception x){
      throw new IllegalArgumentException(x);}
    File f=new File(decodedpath);
    if(!f.isDirectory())f=f.getParentFile();
    return f;}
  
}
