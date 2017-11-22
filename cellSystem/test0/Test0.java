package org.fleen.bread.cellSystem.test0;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.fleen.bread.cellSystem.CellSystem;
import org.fleen.bread.cellSystem.MappedThing;
import org.fleen.bread.cellSystem.RuleSystem;
import org.fleen.forsythia.core.composition.FPolygon;

public class Test0{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  /*
   *
   * 
create composition

create list of mapped things 
  (in order of mapping I guess, for example the boiled edges should be mapped after the polygon areas)
  margin
  leaf polygons
  polygons with edges to be boiled
  
create cellsystem : cellsystem0
  new CellSystem(w,h,mappedthings)
  
(maybe clean it. are there dead cells? if so then do something about them)
   
create second, empty cellsystem of same size but nothing mapped : cellsystem1
  cellsystem1=cellsystem0.getBlankCopy();
  
create rulesystem
  new RuleSystem() : rulesystem 

boolean flipflop=true
while(notdone){
  if(flipflip){
    rulesystem.doRules(cellsystem0,cellsystem1)
    render(cellsystem1)
  }else{
    rulesystem.doRules(cellsystem1,cellsystem0)
    render(cellsystem0)}
  flipflop=!flipflop;
   */
  
  Test0(){
    initUI();
    initRenderer();
    //
    initComposition();
    initCompositionCellSystemTransform();
    initMappedThingsList();
    //
    initCellSystem0();
    initCellSystem1();
    //
    initRuleSystem();
    
    }
  
  
//  Test0(){
//    initUI();
//    initRenderer();
//    //
//    initComposition();
//    initMappedThingsList();
//    //
//    initCellSystem();
//    initCompositionCellSystemTransform();
//    mapCompositionToCellSystem();
//    //
//    
//    }
  
  /*
   * ################################
   * MAIN LOOP
   * ################################
   */
  
  public void run(){
    render(cellsystem0);
    boolean flipflop=true;
    for(int i=0;i<6;i++){
      if(flipflop){
        System.out.println("dorules flipflop="+flipflop);
        rulesystem.doRules(cellsystem0,cellsystem1);
        render(cellsystem1);
      }else{
        System.out.println("dorules flipflop="+flipflop);
        rulesystem.doRules(cellsystem1,cellsystem0);
        render(cellsystem0);}
      flipflop=!flipflop;}}
  
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
   * COMPOSITION
   * ################################
   */
  
  Composition composition;
  
  private void initComposition(){
    composition=new Composition();}
  
  /*
   * ################################
   * COMPOSITION CELLSYSTEM TRANSFORM
   * scale up the composition because, dimensionally, it's pretty small
   * translate it to put the left and top edges at 0, + margin 
   * ################################
   */
  
  int margin=16;
  double scale=23;
  AffineTransform compositionrdstransform;
  
  private void initCompositionCellSystemTransform(){
    //note that we flip the y to convert cartesian coors to array coors
    compositionrdstransform=AffineTransform.getScaleInstance(scale,-scale);
    //
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    double 
      tx=-bounds.x*scale+margin/scale,
      ty=-bounds.y*scale+margin/scale-getCellSystemHeight()/scale;
    compositionrdstransform.translate(tx,ty);}
  
  /*
   * ################################
   * MAPPED THINGS LIST
   * ################################
   */
  
  List<MappedThing> mappedthings;
  
  private void initMappedThingsList(){
    mappedthings=new ArrayList<MappedThing>();
    //
    MappedThing margin=new MappedThing(composition.getRootPolygon(),compositionrdstransform,new String[]{"margin"});
    mappedthings.add(margin);
    //
    MappedThing leaf;
    for(FPolygon p:composition.getLeafPolygons()){
      leaf=new MappedThing(p,compositionrdstransform,new String[]{"leaf"});
      mappedthings.add(leaf);}
    //
    FPolygon hex=null;
    for(FPolygon p:composition.getLeafPolygons())
      if(p.hasTags("hex"))
        hex=p;
    if(hex==null)throw new IllegalArgumentException("couldn't find hex");
    MappedThing boiledhex=new MappedThing(hex,compositionrdstransform,new String[]{"boiled"});
    mappedthings.add(boiledhex);}
  
  /*
   * ################################
   * CELL SYSTEM
   * ################################
   */
  
  CellSystem cellsystem0,cellsystem1;
  
  int getCellSystemWidth(){
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    return (int)(bounds.width*scale+margin+margin);}
  
  int getCellSystemHeight(){
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    return (int)(bounds.height*scale+margin+margin);}
  
  void initCellSystem0(){
    cellsystem0=new CellSystem(getCellSystemWidth(),getCellSystemHeight(),mappedthings);}
  
  void initCellSystem1(){
    cellsystem1=new CellSystem(getCellSystemWidth(),getCellSystemHeight());}
  
  /*
   * ################################
   * RULE SYSTEM
   * ################################
   */
  
  RuleSystem rulesystem;
  
  private void initRuleSystem(){
    rulesystem=new RuleSystem();}
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  Renderer renderer;
  BufferedImage image=null;
  
  private void initRenderer(){
    renderer=new Renderer(this);}
  
  private void render(CellSystem cs){
    renderer.render(cs);
    ui.repaint();}
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    Test0 t=new Test0();
    t.run();}
  
}
