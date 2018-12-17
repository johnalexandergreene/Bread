package org.fleen.bread.app.cruncher;

import java.io.File;

import org.fleen.forsythia.app.compositionGenerator.RasterExporter;

/*
 * 120x120 grid
 */
public class Cruncher2{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  
  UI ui;
  static final int GRIDSPAN=120;//lots of factors
  int incrementindex;
  static final int DURATION=500;
      
  static final double SCALE=0.001;
  
  static final int TOFFSET=10000;
  
  int[][] grid=new int[GRIDSPAN][GRIDSPAN];
  
  void start(){
    exporter.setExportDir(new File(EXPORTDIR));
    for(incrementindex=0;incrementindex<DURATION;incrementindex++){
      try{
        Thread.sleep(60);
      }catch(Exception x){}
      increment();
//      export();
      ui.repaint();}
    
    
    
  }
  
  RasterExporter exporter=new RasterExporter();
  
  void export(){
    exporter.export(ui.renderer.getImage());}

  void increment(){
//    double 
//      position=Math.abs(((double)(incrementindex))/((double)DURATION)-0.5)*2.0+TOFFSET,
//      factor=position*SCALE;
    double 
//    position=Math.abs(((double)(incrementindex))/((double)DURATION)-0.5)*2.0+TOFFSET,
      factor=((DURATION-incrementindex)+TOFFSET)*SCALE;
    for(int x=0;x<GRIDSPAN;x++){
      for(int y=0;y<GRIDSPAN;y++){
        grid[x][y]=(int)(((x^y)*factor));}}}
  
//  void increment(){
//    double 
//      position=Math.abs(((double)incrementindex)/((double)DURATION)-0.5)*2.0,
//      factor=position*SCALE;
//    for(int x=0;x<GRIDSPAN;x++){
//      for(int y=0;y<GRIDSPAN;y++){
//        grid[x][y]=(int)(((x^y)*factor)%Renderer.RAINBOW.length);}}}
  
//  void increment(){
//    double 
//      position=Math.abs(((double)incrementindex)/((double)DURATION)-0.5)*2.0,
//      factor=position*SCALE;
//    for(int x=0;x<GRIDSPAN;x++){
//      for(int y=0;y<GRIDSPAN;y++){
//        grid[x][y]=(int)(((x^y)*factor)%3);}}}
  
//  void increment(){
//    double 
//      position=Math.abs(((double)incrementindex)/((double)DURATION)-0.5)*2.0,
//      factor=position*SCALE;
//    for(int x=0;x<GRIDSPAN;x++){
//      for(int y=0;y<GRIDSPAN;y++){
//        grid[x][y]=(int)(factor*(Math.abs((y*x)*(x%48))));}}}
  
//  void increment(){
//    double 
//      position=Math.abs(((double)incrementindex)/((double)DURATION)-0.5)*2.0,
//      factor=position*SCALE;
//    for(int x=0;x<GRIDSPAN;x++){
//      for(int y=0;y<GRIDSPAN;y++){
//        grid[x][y]=(int)(factor*x*((y+1)*(y+1)));}}}

}
