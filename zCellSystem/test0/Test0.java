package org.fleen.bread.zCellSystem.test0;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.fleen.bread.zCellSystem.ZCellSystem;
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
    initCompositionFCSTransform();
    mapCompositionToFCS();
    initRenderer();}
  
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
   * COMPOSITION FCS TRANSFORM
   * scale up the composition because, dimensionally, it's pretty small
   * translate it to put the left and top edges at 0, + margin 
   * ################################
   */
  
  int margin=8;
  double scale=222;
  AffineTransform compositionrdstransform;
  
  private void initCompositionFCSTransform(){
    //note that we flip the y to convert cartesian coors to array coors
    compositionrdstransform=AffineTransform.getScaleInstance(scale,-scale);
    //
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    double 
      tx=-bounds.x*scale+margin/scale,
      ty=-bounds.y*scale+margin/scale-rds.getHeight()/scale;
    compositionrdstransform.translate(tx,ty);}
  
  /*
   * ################################
   * FCS
   * Our Reaction Diffusion System
   * ################################
   */
  
  double glowspan=1.5;
  
  ZCellSystem rds;
  
  void initFCS(){
    Rectangle2D.Double bounds=composition.getRootPolygon().getDPolygon().getBounds();
    int 
      w=(int)(bounds.width*scale+margin+margin),
      h=(int)(bounds.height*scale+margin+margin);
    rds=new ZCellSystem(w,h);}
  
  void mapCompositionToFCS(){
    mapRootArea();
    mapLeafAreas();
    mapHexEdge();
    mapMargin();
    rds.clean();}
  
  void mapRootArea(){
    DPolygon d=composition.getRootPolygon().getDPolygon();
    rds.mapPolygonArea(d,compositionrdstransform,glowspan);}
  
  void mapLeafAreas(){
    DPolygon d;
    for(FPolygon p:composition.getLeafPolygons()){
      d=p.getDPolygon();
      rds.mapPolygonArea(d,compositionrdstransform,glowspan);}}
  
  void mapHexEdge(){
    for(FPolygon p:composition.getLeafPolygons()){
      if(p.hasTags("hex")){
        DPolygon d=p.getDPolygon();
        rds.mapPolygonEdge(d,compositionrdstransform,glowspan);}}}
  
  void mapMargin(){
    rds.mapMarginCells(composition.getRootPolygon().getDPolygon(),compositionrdstransform,glowspan);}
  
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
