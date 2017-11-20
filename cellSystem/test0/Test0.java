package org.fleen.bread.cellSystem.test0;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import org.fleen.bread.cellSystem.CellSystem;
import org.fleen.forsythia.core.composition.FPolygon;
import org.fleen.geom_2D.DPolygon;

public class Test0{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  Test0(){
    initUI();
    initComposition();
    initFCS();
    initCompositionCSTransform();
    mapCompositionToCS();
    initRenderer();}
  
  /*
   * ################################
   * MAIN LOOP
   * ################################
   */
  
  public void run(){
    
    render();
    
    
    
    
    for(int i=0;i<3;i++){
      doRules();
      render();}}
  
  /*
   * use a 2 array system
   * apply rules to array0 to get array1, then render array1
   * apply rules to array1 to get array0, then render array0
   * etc
   * 
   *  
   *  
   */
  private void doRules(){
    
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
  
  Composition composition;
  
  private void initComposition(){
    composition=new Composition();}
  
  /*
   * ################################
   * COMPOSITION CS TRANSFORM
   * scale up the composition because, dimensionally, it's pretty small
   * translate it to put the left and top edges at 0, + margin 
   * ################################
   */
  
  int margin=16;
  double scale=23;
  AffineTransform compositionrdstransform;
  
  private void initCompositionCSTransform(){
    //note that we flip the y to convert cartesian coors to array coors
    compositionrdstransform=AffineTransform.getScaleInstance(scale,-scale);
    //
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    double 
      tx=-bounds.x*scale+margin/scale,
      ty=-bounds.y*scale+margin/scale-cellsystem.getHeight()/scale;
    compositionrdstransform.translate(tx,ty);}
  
  /*
   * ################################
   * CELL SYSTEM
   * ################################
   */
  
  CellSystem cellsystem;
  
  void initFCS(){
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    int 
      w=(int)(bounds.width*scale+margin+margin),
      h=(int)(bounds.height*scale+margin+margin);
    cellsystem=new CellSystem(w,h);}
  
  void mapCompositionToCS(){
//    mapRootArea();
    mapLeafAreas();
    mapHexEdge();
    mapMargin();
//    mapRandomLeafArea();
    cellsystem.clean();}
  
  void mapRootArea(){
    DPolygon d=composition.getRootPolygon().getDPolygon();
    cellsystem.mapPolygonArea(d,compositionrdstransform);}
  
  void mapLeafAreas(){
    DPolygon d;
    for(FPolygon p:composition.getLeafPolygons()){
      d=p.getDPolygon();
      cellsystem.mapPolygonArea(d,compositionrdstransform);}}
  
  void mapRandomLeafArea(){
    List<FPolygon> a=composition.getLeafPolygons();
    Random r=new Random();
    DPolygon d=a.get(r.nextInt(a.size())).getDPolygon();
    cellsystem.mapPolygonArea(d,compositionrdstransform);}
  
  void mapHexEdge(){
    for(FPolygon p:composition.getLeafPolygons()){
      if(p.hasTags("hex")){
        DPolygon d=p.getDPolygon();
        d=new DPolygon(d);
        cellsystem.mapPolygonEdge(d,compositionrdstransform);}}}
  
  void mapMargin(){
    cellsystem.mapMarginCells(composition.getRootPolygon().getDPolygon(),compositionrdstransform);}
  
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
    Test0 t=new Test0();
    t.run();}
  
}
