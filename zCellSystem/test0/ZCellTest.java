package org.fleen.bread.zCellSystem.test0;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fleen.bread.hCellSystem.HCellSystem;
import org.fleen.bread.zCellSystem.ZCSMappedThing;
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
    initCellSystem1();}
  
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

    render(cellsystem0);
    
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
      ty=-bounds.y*scale+margin/scale-getCellSystemHeight()/scale;
    compositioncellsystemtransform.translate(tx,ty);}
  
  /*
   * ################################
   * MAPPED THINGS
   * ################################
   */
  
  //this is a nice glowspan, makes for nice transitions
  //we use this value for all mappings in the test
  //we could, of course, use different glowspans for all the mappings if we so chose.
  static final double GLOWSPAN=1.5;
  
  List<ZCSMappedThing> mappedthings;
  
  private void initMappedThingsList(){
    mappedthings=new ArrayList<ZCSMappedThing>();
    //
//    MappedZCellSystemThing margin=
//      new MappedZCellSystemThing(composition.getRootPolygon(),compositioncellsystemtransform,GLOWSPAN,new String[]{"margin"});
//    mappedthings.add(margin);
    //
    ZCSMappedThing leaf;
    for(FPolygon p:composition.getLeafPolygons()){
      leaf=new ZCSMappedThing(p,compositioncellsystemtransform,GLOWSPAN,new String[]{"leaf"});
      mappedthings.add(leaf);}
    //
//    mappedthings.addAll(getBoiledPolygonEdgeThings());
    }
  
  /*
   * TODO
   * a nice symmetricrandom type selection
   */
  private List<ZCSMappedThing> getBoiledPolygonEdgeThings(){
    Random r=new Random();
    List<ZCSMappedThing> a=new ArrayList<ZCSMappedThing>();
    for(FPolygon p:composition.getPolygons())
      if(r.nextDouble()>0.95)
        a.add(new ZCSMappedThing(p,compositioncellsystemtransform,GLOWSPAN,new String[]{"boiled"}));
    return a;}
  
  /*
   * ################################
   * CELL SYSTEM
   * ################################
   */
  
  ZCellSystem 
    cellsystem0,
    cellsystem1;
 
  int getCellSystemWidth(){
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    return (int)(bounds.width*scale+margin+margin);}
  
  int getCellSystemHeight(){
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    return (int)(bounds.height*scale+margin+margin);}
  
  void initCellSystem0(){
    cellsystem0=new ZCellSystem(getCellSystemWidth(),getCellSystemHeight(),mappedthings);}
  
  void initCellSystem1(){
    cellsystem1=new ZCellSystem(getCellSystemWidth(),getCellSystemHeight());}

  /*
   * ################################
   * IMAGE
   * ################################
   */
  
  ZCellTestRenderer renderer;
  BufferedImage image=null;
  
  private void initRenderer(){
    renderer=new ZCellTestRenderer(this);}
  
  private void render(ZCellSystem zcs){
    image=renderer.render(zcs);
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
