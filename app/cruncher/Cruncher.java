package org.fleen.bread.app.cruncher;

import java.io.File;

import org.fleen.forsythia.app.compositionGenerator.RasterExporter;

/*
 * 120x120 grid
 */
public class Cruncher{
  
  static final String EXPORTDIR="/home/john/Desktop/newstuff";
  
  UI ui;
  static final int GRIDSPAN=120;//lots of factors
  int incrementindex;
  static final int DURATION=300;
      
  static final double SCALE=0.1;
  
  static final int TOFFSET=0;
  
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
    double factor=(incrementindex+TOFFSET)*SCALE;
    for(int x=0;x<GRIDSPAN;x++){
      for(int y=0;y<GRIDSPAN;y++){
        grid[x][y]=(int)((x|y)*(x&y)*factor);}}}
  
//  void increment(){
////    double 
////      position=Math.abs(((double)(incrementindex))/((double)DURATION)-0.5)*2.0+TOFFSET,
////      factor=position*SCALE;
//    double 
////    position=Math.abs(((double)(incrementindex))/((double)DURATION)-0.5)*2.0+TOFFSET,
//      factor=((DURATION-incrementindex))*SCALE;
////    factor=(incrementindex+TOFFSET)*SCALE;
//    for(int x=0;x<GRIDSPAN;x++){
//      for(int y=0;y<GRIDSPAN;y++){
//        grid[x][y]=(int)(((x^y)*((x*x)&y))*factor);}}}
  
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
