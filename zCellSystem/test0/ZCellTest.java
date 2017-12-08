package org.fleen.bread.zCellSystem.test0;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.bread.hCellSystem.HCellSystem;
import org.fleen.bread.zCellSystem.MappedZCellSystemThing;
import org.fleen.bread.zCellSystem.ZCellSystem;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.geom_2D.DPolygon;

public class ZCellTest{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  ZCellTest(){
    initUI();
    initRenderer();
    //
    initComposition();
    initCompositionCellSystemTransform();
    initMappedThingsList();
    //
    initCellSystem0();
    initCellSystem1();
//    initCompositionFCSTransform();
//    mapCompositionToFCS();
    }
  
  /*
   * ################################
   * MAIN LOOP
   * ################################
   */
  
  public void run(){
//    for(int i=0;i<3;i++){
////      doRules();
//      render();
//    }

    render();
    
  }
  
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
  
  TestComposition001 composition;
  
  private void initComposition(){
    composition=new TestComposition001();}
  
  /*
   * ################################
   * COMPOSITION CELLSYSTEM TRANSFORM
   * scale up the composition because, dimensionally, it's pretty small
   * translate it to put the left and top edges at 0, + margin
   * ################################
   */
  
  int margin=8;
  double scale=222;
  AffineTransform compositioncellsystemtransform;
  
  private void initCompositionCellSystemTransform(){
    //note that we flip the y to convert cartesian coors to array coors
    compositioncellsystemtransform=AffineTransform.getScaleInstance(scale,-scale);
    //
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    double 
      tx=-bounds.x*scale+margin/scale,
      ty=-bounds.y*scale+margin/scale-zcellsystem.getHeight()/scale;
    compositioncellsystemtransform.translate(tx,ty);}
  
  /*
   * ################################
   * MAPPED THINGS LIST
   * ################################
   */
  
  List<MappedZCellSystemThing> mappedthings;
  
  private void initMappedThingsList(){
    mappedthings=new ArrayList<MappedZCellSystemThing>();
    //
    MappedZCellSystemThing margin=new MappedZCellSystemThing(composition.getRootPolygon(),compositioncellsystemtransform,new String[]{"margin"});
    mappedthings.add(margin);
    //
    MappedZCellSystemThing leaf;
    for(FPolygon p:composition.getLeafPolygons()){
      leaf=new MappedZCellSystemThing(p,compositioncellsystemtransform,new String[]{"leaf"});
      mappedthings.add(leaf);}
    //
    mappedthings.addAll(getBoiledPolygonEdgeThings());}
  
  /*
   * TODO
   * a nice symmetricrandom type selection
   */
  private List<MappedZCellSystemThing> getBoiledPolygonEdgeThings(){
    Random r=new Random();
    List<MappedZCellSystemThing> a=new ArrayList<MappedZCellSystemThing>();
    for(FPolygon p:composition.getPolygons())
      if(r.nextDouble()>0.95)
        a.add(new MappedZCellSystemThing(p,compositioncellsystemtransform,new String[]{"boiled"}));
    return a;}
  
  /*
   * ################################
   * CELL SYSTEM
   * ################################
   */
  
  double glowspan=1.5;
  
  ZCellSystem 
    cellsystem0,
    cellsystem1;
  
  void initCellSystem0(){
    cellsystem0=new ZCellSystem(getCellSystemWidth(),getCellSystemHeight(),mappedthings);}
  
  void initCellSystem1(){
    cellsystem1=new ZCellSystem(getCellSystemWidth(),getCellSystemHeight());}
  
  int getCellSystemWidth(){
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    return (int)(bounds.width*scale+margin+margin);}
  
  int getCellSystemHeight(){
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    return (int)(bounds.height*scale+margin+margin);}
//  
////  ZCellSystem zcellsystem;
//  
//  void initFCS(){
//    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
//    int 
//      w=(int)(bounds.width*scale+margin+margin),
//      h=(int)(bounds.height*scale+margin+margin);
//    zcellsystem=new ZCellSystem(w,h);}
//  
//  void mapCompositionToFCS(){
//    mapRootArea();
//    mapLeafAreas();
//    mapHexEdge();
//    mapMargin();
//    zcellsystem.clean();}
//  
//  void mapRootArea(){
//    DPolygon d=composition.getRootPolygon().getDPolygon();
//    zcellsystem.mapPolygonArea(d,compositioncellsystemtransform,glowspan);}
//  
//  void mapLeafAreas(){
//    DPolygon d;
//    for(FPolygon p:composition.getLeafPolygons()){
//      d=p.getDPolygon();
//      zcellsystem.mapPolygonArea(d,compositioncellsystemtransform,glowspan);}}
//  
//  void mapHexEdge(){
//    for(FPolygon p:composition.getLeafPolygons()){
//      if(p.hasTags("hex")){
//        DPolygon d=p.getDPolygon();
//        zcellsystem.mapPolygonEdge(d,compositioncellsystemtransform,glowspan);}}}
//  
//  void mapMargin(){
//    zcellsystem.mapMarginCells(composition.getRootPolygon().getDPolygon(),compositioncellsystemtransform,glowspan);}
  
  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  Renderer renderer;
  BufferedImage image=null;
  
  private void initRenderer(){
    renderer=new Renderer(this);}
  
  private void render(){
    renderer.render();
    ui.repaint();}
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    ZCellTest t=new ZCellTest();
    t.run();}
  
}
